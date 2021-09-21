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

package com.huahui.datasphere.workbench.common.vo;

import java.io.Serializable;

public class DBDetailsInfo implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	protected String databaseName;
	protected String dbServerUsername;
	protected String dbServerPassword;
	protected String jdbcURL;
	protected String dbQuery; //contains query statement for read datasource or insert statement for write
	private String dbCollectionName;
	
	public String getDatabaseName() {
		return databaseName;
	}
	public void setDatabaseName(String databaseName) {
		this.databaseName = databaseName;
	}
	public String getDbServerUsername() {
		return dbServerUsername;
	}
	public void setDbServerUsername(String dbServerUsername) {
		this.dbServerUsername = dbServerUsername;
	}
	public String getDbServerPassword() {
		return dbServerPassword;
	}
	public void setDbServerPassword(String dbServerPassword) {
		this.dbServerPassword = dbServerPassword;
	}
	public String getJdbcURL() {
		return jdbcURL;
	}
	public void setJdbcURL(String jdbcURL) {
		this.jdbcURL = jdbcURL;
	}
	public String getDbQuery() {
		return dbQuery;
	}
	public void setDbQuery(String query) {
		this.dbQuery = query;
	}
	public String getDbCollectionName() {
		return dbCollectionName;
	}
	public void setDbCollectionName(String collectionName) {
		this.dbCollectionName = collectionName;
	}
}
