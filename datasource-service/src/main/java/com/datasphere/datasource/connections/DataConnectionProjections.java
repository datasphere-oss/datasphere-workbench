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

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;

import com.datasphere.server.user.UserProfile;

/**
 * Created by aladin on 2018. 11. 12..
 */
public class DataConnectionProjections {

  @Projection(name = "default", types = { DataConnection.class })
  public interface defaultProjection {

    String getId();

    String getName();

    String getDescription();

    String getType();

    String getImplementor();

    String getHostname();

    String getPort();

    String getUrl();

    String getDatabase();

    String getCatalog();

    String getSid();

    DataConnection.AuthenticationType getAuthenticationType();

    String getUsername();

    String getPassword();

    Boolean getPublished();

    Integer getLinkedWorkspaces();

    @Value("#{@cachedUserService.findUserProfile(target.createdBy)}")
    UserProfile getCreatedBy();

    DateTime getCreatedTime();

    @Value("#{@cachedUserService.findUserProfile(target.modifiedBy)}")
    UserProfile getModifiedBy();

    DateTime getModifiedTime();

    @Value("#{target.getPropertiesMap()}")
    Object getProperties();

    @Value("#{@jdbcConnectionService.isSupportSaveAsHiveTable(target)}")
    Boolean isSupportSaveAsHiveTable();

    @Value("#{@jdbcConnectionService.getConnectionInformation(target)}")
    Object getConnectionInformation();
  }

  @Projection(name = "forSimpleListView", types = { DataConnection.class })
  public interface ForSimpleListViewProjection {

    String getId();

    String getName();

    String getType();

    String getImplementor();

  }

  @Projection(name = "list", types = { DataConnection.class })
  public interface listProjection {

    String getId();

    String getName();

    String getDescription();

    String getType();

    String getImplementor();

    String getHostname();

    String getPort();

    String getUrl();

    String getDatabase();

    String getAuthenticationType();

    Boolean getPublished();

    Integer getLinkedWorkspaces();

    @Value("#{@cachedUserService.findUserProfile(target.createdBy)}")
    UserProfile getCreatedBy();

    DateTime getCreatedTime();

    @Value("#{@cachedUserService.findUserProfile(target.modifiedBy)}")
    UserProfile getModifiedBy();

    DateTime getModifiedTime();
  }

}
