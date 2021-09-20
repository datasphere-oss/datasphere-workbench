/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specic language governing permissions and
 * limitations under the License.
 */

/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specic language governing permissions and
 * limitations under the License.
 */

package com.datasphere.datasource.ingestion.job;

import com.google.common.collect.Lists;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.datasphere.datasource.DataSourceErrorCodes.INGESTION_FILE_EXCEL_CONVERSION_ERROR;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.datasphere.datasource.DataSource;
import com.datasphere.datasource.DataSourceIngestionException;
import com.datasphere.datasource.ingestion.IngestionHistory;
import com.datasphere.datasource.ingestion.IngestionOption;
import com.datasphere.datasource.ingestion.LocalFileIngestionInfo;
import com.datasphere.datasource.ingestion.file.CsvFileFormat;
import com.datasphere.datasource.ingestion.file.ExcelFileFormat;
import com.datasphere.datasource.ingestion.file.FileFormat;
import com.datasphere.datasource.ingestion.file.JsonFileFormat;
import com.datasphere.server.domain.engine.EngineProperties;
import com.datasphere.server.spec.druid.ingestion.BatchIndex;
import com.datasphere.server.spec.druid.ingestion.Index;
import com.datasphere.server.spec.druid.ingestion.IngestionSpec;
import com.datasphere.server.spec.druid.ingestion.IngestionSpecBuilder;
import com.datasphere.server.util.PolarisUtils;

public class FileIngestionJob extends AbstractIngestionJob implements IngestionJob {

  private static final Logger LOGGER = LoggerFactory.getLogger(FileIngestionJob.class);

  private LocalFileIngestionInfo ingestionInfo;

  private String srcFilePath;

  private String loadFileName;

  private Index indexSpec;

  public FileIngestionJob(DataSource dataSource, IngestionHistory ingestionHistory) {
    super(dataSource, ingestionHistory);
    ingestionInfo = dataSource.getIngestionInfoByType();
  }

  @Override
  public void preparation() {

    EngineProperties.IngestionInfo ingestionProperties = engineProperties.getIngestion();

    if (StringUtils.isNotEmpty(this.ingestionInfo.getOriginalFileName())) {
      srcFilePath = System.getProperty("java.io.tmpdir") + File.separator + this.ingestionInfo.getPath();
    } else {
      srcFilePath = this.ingestionInfo.getPath();
      this.ingestionInfo.setOriginalFileName(FilenameUtils.getName(srcFilePath));
    }

    loadFileName = createDestFileName(dataSource.getEngineName(), this.ingestionInfo.getFormat());
    Path destFilePath = Paths.get(ingestionProperties.getBaseDir(), loadFileName);

    if (this.ingestionInfo.getFormat() instanceof ExcelFileFormat) {

      boolean removeFirstRow = this.ingestionInfo.getRemoveFirstRow();

      ExcelFileFormat excelFileFormat = (ExcelFileFormat) this.ingestionInfo.getFormat();
      try {
        PolarisUtils.convertExcelToCSV(excelFileFormat.getSheetIndex(), removeFirstRow, srcFilePath, destFilePath.toString());
        srcFilePath = destFilePath.toString();
      } catch (Exception e) {
        LOGGER.error("Error converting the Excel file.", e);
//        throw new DataSourceIngestionException(INGESTION_FILE_EXCEL_CONVERSION_ERROR, "Error converting the Excel file", e);
      }
    }

  }

  @Override
  public void loadToEngine() {
	loadFileToEngine(Lists.newArrayList(srcFilePath), Lists.newArrayList(loadFileName));
  }

  @Override
  public void buildSpec() {
    IngestionSpec spec = new IngestionSpecBuilder()
        .dataSchema(dataSource)
        .batchTuningConfig(ingestionOptionService.findTuningOptionMap(IngestionOption.IngestionType.BATCH,
                                                                      ingestionInfo.getTuningOptions()))
        .localIoConfig(engineProperties.getIngestion().getBaseDir(), loadFileName)
        .build();

    indexSpec = new BatchIndex(spec, dedicatedWorker);
  }

  @Override
  public String process() {
    String taskId = null;
	try {
		taskId = doIngestion(indexSpec);
	} catch (DataSourceIngestionException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
    LOGGER.info("Successfully creating task : {}", ingestionHistory);
    return taskId;
  }

  private String createDestFileName(String dataSourceName, FileFormat fileFormat) {
    StringBuilder sb = new StringBuilder();
    sb.append(dataSourceName).append("_")
      .append(System.currentTimeMillis()).append(".");

    if (fileFormat instanceof CsvFileFormat
        || fileFormat instanceof ExcelFileFormat) {
      sb.append("csv");
    } else if (fileFormat instanceof JsonFileFormat) {
      sb.append("json");
    } else {
      throw new IllegalArgumentException("Not supported file type.");
    }

    return sb.toString();
  }
}

