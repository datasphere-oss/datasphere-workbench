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

package com.datasphere.datasource.realtime;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by aladin on 2019. 3. 12..
 */
@Component
@ConfigurationProperties(prefix="polaris.realtime")
public class RealTimeProperties {

  String zkConnectUrls;

  Integer sessionTimeout = 10 * 1000;

  Integer connectionTimeout = 8 * 1000;

  public RealTimeProperties() {
  }

  public String getZkConnectUrls() {
    return zkConnectUrls;
  }

  public void setZkConnectUrls(String zkConnectUrls) {
    this.zkConnectUrls = zkConnectUrls;
  }

  public Integer getSessionTimeout() {
    return sessionTimeout;
  }

  public void setSessionTimeout(Integer sessionTimeout) {
    this.sessionTimeout = sessionTimeout;
  }

  public Integer getConnectionTimeout() {
    return connectionTimeout;
  }

  public void setConnectionTimeout(Integer connectionTimeout) {
    this.connectionTimeout = connectionTimeout;
  }
}
