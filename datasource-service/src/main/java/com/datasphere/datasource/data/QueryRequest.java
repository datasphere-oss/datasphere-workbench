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

package com.datasphere.datasource.data;


import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.datasphere.datasource.data.alias.Alias;
import com.datasphere.server.domain.workbook.configurations.datasource.DataSource;

public interface QueryRequest extends Serializable {

  String CONTEXT_ROUTE_URI = "discovery.route.uri";
  String CONTEXT_DASHBOARD_ID = "discovery.dashboard.id";
  String CONTEXT_WIDGET_ID = "discovery.chart.id";
  String CONTEXT_QUERY_ID = "queryId";

  /**
   * 질의 대상 데이터 소스
   */
  DataSource getDataSource();

  /**
   * 쿼리 개별 설정
   */
  Map<String, Object> getContext();

  /**
   * 쿼리 개별 설정
   */
  <T> T getContextValue(String key);

  /**
   * Value Alias Reference Id
   * @return
   * @deprecated
   */
  String getValueAliasRef();

  List<Alias> getAliases();

  void addAlias(Alias alias);
}
