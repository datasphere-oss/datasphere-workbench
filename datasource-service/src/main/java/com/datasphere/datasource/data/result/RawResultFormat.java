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

package com.datasphere.datasource.data.result;

import com.datasphere.datasource.DataSource;
import com.fasterxml.jackson.databind.JsonNode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RawResultFormat extends SearchResultFormat {

  private static final Logger LOGGER = LoggerFactory.getLogger(RawResultFormat.class);

  public RawResultFormat() {
    // Empty Constructor
  }

  public RawResultFormat(DataSource.ConnectionType connType) {
    super(connType);
  }

  @Override
  public Object makeResult(JsonNode obj) {
    return obj;
  }
}
