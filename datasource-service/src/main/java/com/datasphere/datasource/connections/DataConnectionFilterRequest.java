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

package com.datasphere.datasource.connections;

import java.util.List;

import com.datasphere.server.common.criteria.ListFilterRequest;

/**
 * 连接过滤请求
 */
public class DataConnectionFilterRequest extends ListFilterRequest {
  List<String> workspace;
  List<String> userGroup;
  List<Boolean> published;
  List<String> implementor;
  List<String> authenticationType;

  public List<String> getWorkspace() {
    return workspace;
  }

  public void setWorkspace(List<String> workspace) {
    this.workspace = workspace;
  }

  public List<String> getUserGroup() {
    return userGroup;
  }

  public void setUserGroup(List<String> userGroup) {
    this.userGroup = userGroup;
  }

  public List<Boolean> getPublished() {
    return published;
  }

  public void setPublished(List<Boolean> published) {
    this.published = published;
  }

  public List<String> getImplementor() {
    return implementor;
  }

  public void setImplementor(List<String> implementor) {
    this.implementor = implementor;
  }

  public List<String> getAuthenticationType() {
    return authenticationType;
  }

  public void setAuthenticationType(List<String> authenticationType) {
    this.authenticationType = authenticationType;
  }
}
