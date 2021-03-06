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

package com.datasphere.datasource.connections.jdbc.connector;

import java.io.IOException;
import java.sql.Connection;

import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.security.UserGroupInformation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.datasphere.datasource.connections.jdbc.JdbcConnectInformation;
import com.datasphere.datasource.connections.jdbc.dialect.JdbcDialect;
import com.datasphere.datasource.connections.jdbc.dialect.HiveDialect;

/**
 *
 */
@Component
public class KerberosJdbcConnector extends CachedUserJdbcConnector {

  private static final Logger LOGGER = LoggerFactory.getLogger(KerberosJdbcConnector.class);

  @Value("${datasphere.engine.ingestion.hive.keytab:}")
  String keyTabPath;

  @Override
  public Connection getConnection(JdbcConnectInformation connectionInfo, JdbcDialect dialect, String database, boolean includeDatabase, String username, String password) {
    if(dialect instanceof HiveDialect){
      String connectionUrl = getConnectionUrl(connectionInfo, dialect, database, includeDatabase);
      boolean acceptKerberosAuth = ((HiveDialect) dialect).acceptKerberosAuth(connectionUrl);
      if (acceptKerberosAuth) {
        try {
          Configuration conf = new Configuration();
          conf.set("hadoop.security.authentication", "Kerberos");
          UserGroupInformation.setConfiguration(conf);

          String kerberosUser = StringUtils.isNotEmpty(username)
              ? username
              : ((HiveDialect) dialect).getKerberosPrincipal(connectionUrl);
          LOGGER.debug("kerberosUser : {}", kerberosUser);
          LOGGER.debug("keyTabPath : {}", keyTabPath);
          UserGroupInformation.loginUserFromKeytab(kerberosUser, keyTabPath);
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
    return super.getConnection(connectionInfo, dialect, database, includeDatabase, username, password);
  }
}
