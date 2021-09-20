/*
 * Copyright 2019, Huahuidata, Inc.
 * DataSphere is licensed under the Mulan PSL v1.
 * You can use this software according to the terms and conditions of the Mulan PSL v1.
 * You may obtain a copy of Mulan PSL v1 at:
 * http://license.coscl.org.cn/MulanPSL
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND, EITHER EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT, MERCHANTABILITY OR FIT FOR A PARTICULAR
 * PURPOSE.
 * See the Mulan PSL v1 for more details.
 */

package com.datasphere.datasource.ingestion.job;

import com.google.common.collect.Maps;

import net.jodah.failsafe.Failsafe;
import net.jodah.failsafe.RetryPolicy;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.client.ResourceAccessException;

import static com.datasphere.datasource.DataSourceErrorCodes.INGESTION_COMMON_ERROR;
import static com.datasphere.datasource.DataSourceErrorCodes.INGESTION_ENGINE_REGISTRATION_ERROR;
import static com.datasphere.datasource.DataSourceErrorCodes.INGESTION_ENGINE_TASK_ERROR;
import static com.datasphere.datasource.ingestion.IngestionHistory.IngestionStatus.FAILED;
import static com.datasphere.datasource.ingestion.IngestionHistory.IngestionStatus.SUCCESS;
import static com.datasphere.datasource.ingestion.job.IngestionProgress.END_INGESTION_JOB;
import static com.datasphere.datasource.ingestion.job.IngestionProgress.ENGINE_INIT_TASK;
import static com.datasphere.datasource.ingestion.job.IngestionProgress.ENGINE_REGISTER_DATASOURCE;
import static com.datasphere.datasource.ingestion.job.IngestionProgress.ENGINE_RUNNING_TASK;
import static com.datasphere.datasource.ingestion.job.IngestionProgress.FAIL_INGESTION_JOB;
import static com.datasphere.datasource.ingestion.job.IngestionProgress.PREPARATION_HANDLE_LOCAL_FILE;
import static com.datasphere.datasource.ingestion.job.IngestionProgress.PREPARATION_LOAD_FILE_TO_ENGINE;
import static com.datasphere.datasource.ingestion.job.IngestionProgress.START_INGESTION_JOB;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import com.datasphere.datasource.DataSource;
import com.datasphere.datasource.DataSourceErrorCodes;
import com.datasphere.datasource.DataSourceIngestionException;
import com.datasphere.datasource.DataSourceService;
import com.datasphere.datasource.DataSourceSummary;
import com.datasphere.datasource.Field;
import com.datasphere.datasource.connection.jdbc.JdbcConnectionService;
import com.datasphere.datasource.ingestion.HdfsIngestionInfo;
import com.datasphere.datasource.ingestion.HiveIngestionInfo;
import com.datasphere.datasource.ingestion.IngestionHistory;
import com.datasphere.datasource.ingestion.IngestionHistoryRepository;
import com.datasphere.datasource.ingestion.IngestionInfo;
import com.datasphere.datasource.ingestion.IngestionOptionService;
import com.datasphere.datasource.ingestion.LocalFileIngestionInfo;
import com.datasphere.datasource.ingestion.jdbc.JdbcIngestionInfo;
import com.datasphere.server.common.GlobalObjectMapper;
import com.datasphere.server.common.ProgressResponse;
import com.datasphere.server.common.fileloader.FileLoaderFactory;
import com.datasphere.server.domain.engine.DruidEngineMetaRepository;
import com.datasphere.server.domain.engine.DruidEngineRepository;
import com.datasphere.server.domain.engine.EngineIngestionService;
import com.datasphere.server.domain.engine.EngineProperties;
import com.datasphere.server.domain.engine.EngineQueryService;
import com.datasphere.server.domain.engine.model.IngestionStatusResponse;
import com.datasphere.server.domain.engine.model.SegmentMetaDataResponse;
import com.datasphere.server.domain.mdm.MetadataService;
import com.datasphere.server.domain.storage.StorageProperties;
import com.datasphere.server.util.PolarisUtils;

@Component
public class IngestionJobRunner {

  private static final Logger LOGGER = LoggerFactory.getLogger(IngestionJobRunner.class);

  public static final String TOPIC_INGESTION_PROGRESS = "/topic/datasources/%s/progress";

  @Autowired
  private PlatformTransactionManager platformTransactionManager;

  @Autowired
  private EngineProperties engineProperties;

  @Autowired(required = false)
  private StorageProperties storageProperties;

  @Autowired
  private FileLoaderFactory fileLoaderFactory;

  @Autowired
  private DataSourceService dataSourceService;

  @Autowired
  private JdbcConnectionService jdbcConnectionService;

  @Autowired
  private IngestionHistoryRepository historyRepository;

  @Autowired
  private DruidEngineMetaRepository engineMetaRepository;

  @Autowired
  private DruidEngineRepository engineRepository;

  @Autowired
  private EngineIngestionService ingestionService;

  @Autowired
  private EngineQueryService queryService;

  @Autowired
  private IngestionOptionService ingestionOptionService;

  @Autowired
  private MetadataService metadataService;

  private SimpMessageSendingOperations messagingTemplate;

  private TransactionTemplate transactionTemplate;

  @Value("${polaris.datasource.ingestion.retries.delay:3}")
  private Long delay;

  @Value("${polaris.datasource.ingestion.retries.maxDelay:60}")
  private Long maxDelay;

  @Value("${polaris.datasource.ingestion.retries.maxDuration:3600}")
  private Long maxDuration;

  public IngestionJobRunner() {
    // Empty Constructor
  }

  @Autowired
  public void setMessagingTemplate(SimpMessageSendingOperations messagingTemplate) {
    this.messagingTemplate = messagingTemplate;
  }

  @PostConstruct
  public void init() {
    transactionTemplate = new TransactionTemplate(platformTransactionManager);
  }

  public void ingestion(DataSource dataSource) {

    String sendTopicUri = String.format(TOPIC_INGESTION_PROGRESS, dataSource.getId());

    IngestionHistory history = null;
    Map<String, Object> results = Maps.newLinkedHashMap();
    Boolean isResultEmpty = false;

    try {

      // Temporary Process
      Thread.sleep(3000L);
      history = createNewHistory(dataSource.getId(), dataSource.getIngestionInfo());

      sendTopic(sendTopicUri, new ProgressResponse(0, START_INGESTION_JOB));
      history = updateHistoryProgress(history.getId(), START_INGESTION_JOB);

      IngestionJob ingestionJob = getJob(dataSource, history);

      sendTopic(sendTopicUri, new ProgressResponse(20, PREPARATION_HANDLE_LOCAL_FILE));
      history = updateHistoryProgress(history.getId(), PREPARATION_HANDLE_LOCAL_FILE);

      ingestionJob.preparation();

      if(!isResultEmpty){
        sendTopic(sendTopicUri, new ProgressResponse(40, PREPARATION_LOAD_FILE_TO_ENGINE));
        history = updateHistoryProgress(history.getId(), PREPARATION_LOAD_FILE_TO_ENGINE);

        ingestionJob.loadToEngine();

        sendTopic(sendTopicUri, new ProgressResponse(50, ENGINE_INIT_TASK));
        history = updateHistoryProgress(history.getId(), ENGINE_INIT_TASK);

        ingestionJob.buildSpec();

        // Call engine api.
        String taskId = ingestionJob.process();

        history = updateHistoryProgress(history.getId(), ENGINE_RUNNING_TASK, taskId);

        results.put("history", history);
        sendTopic(sendTopicUri, new ProgressResponse(70, ENGINE_RUNNING_TASK, results));

        // Check ingestion Task.
        IngestionStatusResponse statusResponse = checkIngestion(taskId);
        if (statusResponse.getStatus() == FAILED) {
          throw new DataSourceIngestionException(INGESTION_ENGINE_TASK_ERROR, "An error occurred while loading the data source : " + statusResponse.getCause());
        }

        // Check registering datasource
        sendTopic(sendTopicUri, new ProgressResponse(90, ENGINE_REGISTER_DATASOURCE));
        history = updateHistoryProgress(history.getId(), ENGINE_REGISTER_DATASOURCE, taskId);
      }

      SegmentMetaDataResponse segmentMetaData = checkDataSource(dataSource.getEngineName());
      if (segmentMetaData == null) {
        throw new DataSourceIngestionException(INGESTION_ENGINE_REGISTRATION_ERROR, "An error occurred while registering the data source");
      }

      // FIXME: fix deprecated code with DataSourceCheckJob
      DataSourceSummary summary = new DataSourceSummary(segmentMetaData);
      summary.updateSummary(segmentMetaData);

      if (BooleanUtils.isTrue(dataSource.getIncludeGeo())) {
        List<Field> geoFields = dataSource.getGeoFields();

        Map<String, Object> result = queryService.geoBoundary(dataSource.getEngineName(), geoFields);
        summary.updateGeoCorner(result);
      }

      results.put("summary", summary);
      results.put("history", history);

      // create metadata
      createMetadata(dataSource);

      ProgressResponse successResponse = new ProgressResponse(100, END_INGESTION_JOB);
      successResponse.setResults(results);

      sendTopic(sendTopicUri, successResponse);
      setSuccessProgress(history.getId(), summary);

    } catch (Exception e) {

      DataSourceIngestionException ie;
      if (!(e instanceof DataSourceIngestionException)) {
        ie = new DataSourceIngestionException(INGESTION_COMMON_ERROR, e);
      } else {
        ie = (DataSourceIngestionException) e;
      }

      try {
        history = setFailProgress(history.getId(), ie);
      } catch (TransactionException ex) {
        LOGGER.warn("Fail to save fail process : {}", ex.getMessage());
      }

      results.put("history", history);
      sendTopic(sendTopicUri, new ProgressResponse(-1, FAIL_INGESTION_JOB, results));

      LOGGER.error("Fail to ingestion : {}", history, ie);
    }

  }

  public IngestionHistory createNewHistory(final String datasourceId, final IngestionInfo ingestionInfo) {
    return transactionTemplate.execute(transactionStatus -> {
      IngestionHistory history = new IngestionHistory();
      history.setIngestionInfo(ingestionInfo);
      history.setDataSourceId(datasourceId);
      history.setStatus(IngestionHistory.IngestionStatus.RUNNING);
      history.setHostname(PolarisUtils.getLocalHostname());
      return historyRepository.saveAndFlush(history);
    });
  }

  public IngestionHistory updateHistoryProgress(final Long id, final IngestionProgress progress) {
    return updateHistoryProgress(id, progress, null);
  }

  public IngestionHistory updateHistoryProgress(final Long id, final IngestionProgress progress, final String taskId) {
    return transactionTemplate.execute(transactionStatus -> {
      IngestionHistory history = historyRepository.findById(id).get();
      history.setProgress(progress);

      if (StringUtils.isNotEmpty(taskId)) {
        history.setIngestionId(taskId);
      }

      return historyRepository.save(history);
    });
  }

  public IngestionHistory setSuccessProgress(final Long historyId, final DataSourceSummary summary) {
    return transactionTemplate.execute(transactionStatus -> {
      IngestionHistory history = historyRepository.findById(historyId).get();
      history.setStatus(SUCCESS);
      history.setProgress(END_INGESTION_JOB);

      dataSourceService.setDataSourceStatus(history.getDataSourceId(), DataSource.Status.ENABLED, summary, null);

      return historyRepository.save(history);
    });
  }

  public IngestionHistory setFailProgress(final Long historyId, final DataSourceIngestionException ie) {
    return transactionTemplate.execute(transactionStatus -> {
      IngestionHistory history = historyRepository.findById(historyId).get();
      history.setStatus(FAILED, ie.getMessage());

      dataSourceService.setDataSourceStatus(history.getDataSourceId(), DataSource.Status.FAILED,
                                            null,
                                            history.getProgress() == ENGINE_REGISTER_DATASOURCE);

      return historyRepository.save(history);
    });
  }

  public IngestionJob getJob(DataSource dataSource, IngestionHistory ingestionHistory) {
    IngestionInfo ingestionInfo = dataSource.getIngestionInfo();

    if (ingestionInfo instanceof LocalFileIngestionInfo) {
      FileIngestionJob ingestionJob = new FileIngestionJob(dataSource, ingestionHistory);
      ingestionJob.setEngineProperties(engineProperties);
      ingestionJob.setStorageProperties(storageProperties);
      ingestionJob.setEngineMetaRepository(engineMetaRepository);
      ingestionJob.setEngineRepository(engineRepository);
      ingestionJob.setFileLoaderFactory(fileLoaderFactory);
      ingestionJob.setHistoryRepository(historyRepository);
      ingestionJob.setIngestionOptionService(ingestionOptionService);
      return ingestionJob;

    } else if (ingestionInfo instanceof JdbcIngestionInfo) {
      JdbcIngestionJob ingestionJob = new JdbcIngestionJob(dataSource, ingestionHistory);
      ingestionJob.setEngineProperties(engineProperties);
      ingestionJob.setStorageProperties(storageProperties);
      ingestionJob.setEngineMetaRepository(engineMetaRepository);
      ingestionJob.setEngineRepository(engineRepository);
      ingestionJob.setFileLoaderFactory(fileLoaderFactory);
      ingestionJob.setHistoryRepository(historyRepository);
      ingestionJob.setIngestionOptionService(ingestionOptionService);
      ingestionJob.setJdbcConnectionService(jdbcConnectionService);
      return ingestionJob;

    } else if (ingestionInfo instanceof HdfsIngestionInfo) {
      HdfsIngestionJob ingestionJob = new HdfsIngestionJob(dataSource, ingestionHistory);
      ingestionJob.setEngineProperties(engineProperties);
      ingestionJob.setStorageProperties(storageProperties);
      ingestionJob.setEngineMetaRepository(engineMetaRepository);
      ingestionJob.setEngineRepository(engineRepository);
      ingestionJob.setFileLoaderFactory(fileLoaderFactory);
      ingestionJob.setHistoryRepository(historyRepository);
      ingestionJob.setIngestionOptionService(ingestionOptionService);
      return ingestionJob;

    } else if (ingestionInfo instanceof HiveIngestionInfo) {
      HiveIngestionJob ingestionJob = new HiveIngestionJob(dataSource, ingestionHistory);
      ingestionJob.setEngineProperties(engineProperties);
      ingestionJob.setStorageProperties(storageProperties);
      ingestionJob.setEngineMetaRepository(engineMetaRepository);
      ingestionJob.setEngineRepository(engineRepository);
      ingestionJob.setFileLoaderFactory(fileLoaderFactory);
      ingestionJob.setHistoryRepository(historyRepository);
      ingestionJob.setIngestionOptionService(ingestionOptionService);
      ingestionJob.setJdbcConnectionService(jdbcConnectionService);
      return ingestionJob;
    } else {
      throw new IllegalArgumentException("Not supported ingestion information.");
    }

  }

  public void sendTopic(String topicUri, ProgressResponse progressResponse) {
    LOGGER.debug("Send Progress Topic : {}, {}", topicUri, progressResponse);
    try {
      messagingTemplate.convertAndSend(topicUri,
                                       GlobalObjectMapper.writeValueAsString(progressResponse));
    } catch (Exception e) {
      LOGGER.error("Fail to send message : {}, {}", topicUri, progressResponse, e);
    }

  }

  public SegmentMetaDataResponse checkDataSource(String engineName) {

    // @formatter:off
    RetryPolicy retryPolicy = new RetryPolicy()
        .retryOn(ResourceAccessException.class)
        .retryOn(Exception.class)
        .retryIf(result -> result == null)
        .withBackoff(delay, maxDelay, TimeUnit.SECONDS)
        .withMaxDuration(maxDuration, TimeUnit.SECONDS);
		// @formatter:on

    Callable<SegmentMetaDataResponse> callable = () -> queryService.segmentMetadata(engineName);

    // @formatter:off
    SegmentMetaDataResponse response = Failsafe.with(retryPolicy)
            .onRetriesExceeded((o, throwable) -> {
              throw new DataSourceIngestionException("Retries exceed for checking datasource : " + engineName);
            })
            .onComplete((o, throwable, ctx) -> {
              if(ctx != null) {
                LOGGER.debug("Completed checking datasource({}). {} tries. Take time {} seconds.", engineName, ctx.getExecutions(), ctx.getElapsedTime().toSeconds());
              }
            })
            .get(callable);
    // @formatter:on

    return response;
  }

  public IngestionStatusResponse checkIngestion(String taskId) {

    // @formatter:off
    RetryPolicy retryPolicy = new RetryPolicy()
        .retryOn(ResourceAccessException.class)
        .retryOn(Exception.class)
        .retryIf(result -> {
          if(result instanceof IngestionStatusResponse) {
            IngestionStatusResponse response = (IngestionStatusResponse) result;
            if(response.getStatus() == SUCCESS || response.getStatus() == FAILED) {
              return false;
            }
          }
          return true;
        })
        .withBackoff(delay, maxDelay, TimeUnit.SECONDS)
        .withMaxDuration(maxDuration, TimeUnit.SECONDS);
		// @formatter:on


    Callable<IngestionStatusResponse> callable = () -> ingestionService.doCheckResult(taskId);

    // @formatter:off
    @SuppressWarnings("unchecked")
	IngestionStatusResponse statusResponse = (Failsafe.with(retryPolicy))
            .onRetriesExceeded((o, throwable) -> {
              throw new DataSourceIngestionException("Retries exceed for ingestion task : " + taskId);
            })
            .onComplete((o, throwable, ctx) -> {
              if(ctx != null) {
                LOGGER.debug("Completed checking task ({}). {} tries. Take time {} seconds.", taskId, ctx.getExecutions(), ctx.getElapsedTime().toSeconds());
              }
            })
            .get(callable);
    // @formatter:on

    return statusResponse;
  }

  public void createMetadata(DataSource dataSource){
    try{
      // create metadata
      metadataService.saveFromDataSource(dataSource);
    }catch (Exception e){
      LOGGER.error("Fail to create Metadata : {}", e);
    }
  }
}

