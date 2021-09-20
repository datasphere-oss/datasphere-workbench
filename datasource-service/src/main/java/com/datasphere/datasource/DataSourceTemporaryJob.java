/*
 * Apache License
 * 
 * Copyright (c) 2020 HuahuiData
 * 
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.datasphere.datasource;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Trigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.datasphere.server.domain.engine.EngineLoadService;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

import java.util.Optional;

@Component
@Scope(SCOPE_PROTOTYPE)
@Transactional
@DisallowConcurrentExecution
public class DataSourceTemporaryJob extends QuartzJobBean {

  private static Logger LOGGER = LoggerFactory.getLogger(DataSourceTemporaryJob.class);

  @Autowired
  DataSourceTemporaryRepository temporaryRepository;

  @Autowired
  EngineLoadService engineLoadService;

  public DataSourceTemporaryJob() {
  }

  @Override
  public void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
    Trigger trigger = jobExecutionContext.getTrigger();
    JobDataMap jobData = trigger.getJobDataMap();

    Optional<DataSourceTemporary> dataSourceTemporary =
        temporaryRepository.findById(jobData.getString("temporaryId"));

    if(dataSourceTemporary == null) {
      LOGGER.warn("Job({}) - Fail to find temporary entity.", trigger.getKey().getName());
      return;
    }

    // Check Temporary DataSource!

//    engineLoadService.

    LOGGER.info("Job({}) - Successfully ingest incremental datasource.", trigger.getKey().getName());
  }
}
