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

package com.datasphere.datasource.connections.jdbc.exception;

import com.datasphere.server.common.exception.ErrorCodes;

public enum JdbcDataConnectionErrorCodes implements ErrorCodes{

  GENERAL_ERROR_CODE("JDC0001")
  , HIVE_METASTORE_ERROR_CODE("JDC0002")
  , INVALID_QUERY_ERROR_CODE("JDC0003")
  , CSV_IO_ERROR_CODE("JDC0004")
  , WEBSOCKET_NOT_FOUND_ERROR_CODE("JDC0005")
  , PARTITION_NOT_EXISTED("JDC0006")
  , NO_SUITABLE_DRIVER("JDC0007")
  , DATASOURCE_CONNECTION_ERROR("JDC0008")
  , NOT_FOUND_SUITABLE_DIALECT("JDC0009")
  , NOT_FOUND_SUITABLE_DATA_ACCESSOR("JDC0010")
  , PREVIEW_TABLE_SQL_ERROR("error.server.connections.jdbc.preview.table")
  , STAGEDB_PREVIEW_TABLE_SQL_ERROR("error.server.connections.stagedb.preview.table")
  ;

  String errorCode;

  JdbcDataConnectionErrorCodes(String errorCode) {
    this.errorCode = errorCode;
  }

  public String getCode() {
    return errorCode;
  }
}
