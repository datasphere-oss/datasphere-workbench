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

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.datasphere.datasource.connections.jdbc.JdbcConnectInformation;
import com.datasphere.datasource.connections.jdbc.dialect.JdbcDialect;
import com.datasphere.server.common.exception.ResourceNotFoundException;
import com.datasphere.server.user.service.CachedUserService;
import com.datasphere.server.user.User;
import com.datasphere.server.util.AuthUtils;

/**
 *
 */
@Component("CachedUserJdbcConnector")
public class CachedUserJdbcConnector extends SimpleJdbcConnector {

  private static final Logger LOGGER = LoggerFactory.getLogger(CachedUserJdbcConnector.class);

  @Autowired
  CachedUserService cachedUserService;

  @Override
  protected String getUsername(JdbcConnectInformation connectionInfo, JdbcDialect dialect) {
    if (connectionInfo.getAuthenticationType() == JdbcConnectInformation.AuthenticationType.USERINFO) {
      return StringUtils.isEmpty(connectionInfo.getUsername())
          ? AuthUtils.getAuthUserName()
          : connectionInfo.getUsername();
    }
    return super.getUsername(connectionInfo, dialect);
  }

  @Override
  protected String getPassword(JdbcConnectInformation connectionInfo, JdbcDialect dialect) {
    if (connectionInfo.getAuthenticationType() == JdbcConnectInformation.AuthenticationType.USERINFO) {
      String username = getUsername(connectionInfo, dialect);
      User user = cachedUserService.findUser(username);
      if (user == null) {
        try {
			throw new ResourceNotFoundException("User(" + username + ")");
		} catch (ResourceNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
      }
      return user.getPassword();
    }
    return super.getPassword(connectionInfo, dialect);
  }
}
