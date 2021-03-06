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
import java.util.Map;

import javax.validation.constraints.NotNull;

import com.datasphere.datasource.ingestion.jdbc.JdbcIngestionInfo;

/**
 * Created by aladin on 2019. 6. 20..
 */
public class ConnectionRequest {

  @NotNull
  DataConnection connection;

  String database;

  String table;

  JdbcIngestionInfo.DataType type;

  String query;

  List<Map<String, Object>> partitions;

  public DataConnection getConnection() {
    return connection;
  }

  public void setConnection(DataConnection connection) {
    this.connection = connection;
  }

  public String getDatabase() {
    return database;
  }

  public void setDatabase(String database) {
    this.database = database;
  }

  public String getTable() {
    return table;
  }

  public void setTable(String table) {
    this.table = table;
  }

  public JdbcIngestionInfo.DataType getType() {
    return type;
  }

  public void setType(JdbcIngestionInfo.DataType type) {
    this.type = type;
  }

  public String getQuery() {
    return query;
  }

  public void setQuery(String query) {
    this.query = query;
  }

  public List<Map<String, Object>> getPartitions() {
    return partitions;
  }

  public void setPartitions(List<Map<String, Object>> partitions) {
    this.partitions = partitions;
  }
}
