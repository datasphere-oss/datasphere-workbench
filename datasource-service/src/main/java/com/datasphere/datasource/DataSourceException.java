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

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.datasphere.server.common.exception.ErrorCodes;
import com.datasphere.server.common.exception.DSSException;

@ResponseStatus(value= HttpStatus.INTERNAL_SERVER_ERROR, reason="DataSource Error")
public class DataSourceException extends DSSException {

  public DataSourceException(ErrorCodes errorCodes, String message) {
    super(errorCodes, message);
  }

  public DataSourceException(ErrorCodes errorCodes, String message, Throwable cause) {
    super(errorCodes, message, cause);
  }

}
