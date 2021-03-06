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

package com.datasphere.datasource.connections.jdbc.accessor;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.pf4j.Extension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.datasphere.datasource.connections.jdbc.accessor.AbstractJdbcDataAccessor;
import com.datasphere.datasource.connections.jdbc.exception.JdbcDataConnectionErrorCodes;
import com.datasphere.datasource.connections.jdbc.exception.JdbcDataConnectionException;


@Extension
public class DruidDataAccessor extends AbstractJdbcDataAccessor {

  private static final Logger LOGGER = LoggerFactory.getLogger(DruidDataAccessor.class);

  @Override
  public void useDatabase(String catalog, String database) {
    LOGGER.debug("Druid does not support USE statements.");
  }

  @Override
  public Map<String, Object> getDatabases(String catalog, String schemaPattern, Integer pageSize, Integer pageNumber) throws JdbcDataConnectionException {
    Map<String, Object> databaseMap = new LinkedHashMap<>();
    List<String> databaseNames = null;

    String schemaListQuery = dialect.getDataBaseQuery(connectionInfo, catalog, schemaPattern, getExcludeSchemas(), pageSize, pageNumber);
    LOGGER.debug("Execute Schema List query : {}", schemaListQuery);

    try {
      databaseNames = executeQueryForList(this.getConnection(), schemaListQuery, (resultSet, rowNum) -> resultSet.getString(1));
    } catch (Exception e) {
      LOGGER.error("Fail to get list of database : {}", e.getMessage());
      throw new JdbcDataConnectionException(JdbcDataConnectionErrorCodes.INVALID_QUERY_ERROR_CODE,
                                            "Fail to get list of database : " + e.getMessage());
    }

    List<String> excludeSchemas = getExcludeSchemas();
    if (excludeSchemas != null) {
      //filter after query execute for hive
      databaseNames = databaseNames.stream()
                                   .filter(databaseName -> excludeSchemas.indexOf(databaseName) < 0)
                                   .collect(Collectors.toList());
    }

    int databaseCount = databaseNames.size();

    databaseMap.put("databases", databaseNames);
    databaseMap.put("page", createPageInfoMap(databaseCount, databaseCount, 0));
    return databaseMap;
  }
}
