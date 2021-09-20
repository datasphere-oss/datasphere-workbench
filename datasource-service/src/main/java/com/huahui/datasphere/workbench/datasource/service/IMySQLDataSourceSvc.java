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

package com.huahui.datasphere.workbench.datasource.service;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import com.huahui.datasphere.workbench.datasource.exception.DataSourceNotFoundException;
import com.huahui.datasphere.workbench.datasource.model.MySQLConnectorModel;

public interface IMySQLDataSourceSvc {

	/**
	 * To get the MySQL Connection status as success/failure 
	 * @param MysqlConnectorModel
	 * 			The MySQLConnectorModel model object
	 * @param query
	 * 			The MySQL query
	 * @return
	 * 			returns the connection status as success/failure 
	 * @throws ClassNotFoundException
	 * 			throws ClassNotFoundException in case of any failure
	 * @throws SQLException
	 * 			throws SQLException in case of any failure
	 * @throws IOException
	 * 			throws IOException in case of any failure
	 * @throws DataSourceNotFoundException
	 * 			throws DataSourceException in case of any failure
	 */
	public String getConnectionStatus(MySQLConnectorModel MysqlConnectorModel, String query) throws ClassNotFoundException, SQLException, IOException, DataSourceNotFoundException;
	
	
	/**
	 * To get the MqSQL connection Object
	 * @param server
	 * 		the input server
	 * @param port
	 * 		the input port
	 * @param dbName
	 * 		the database name
	 * @param username
	 * 		database username
	 * @param password
	 * 		database password
	 * @return
	 * 		returns connection object
	 * @throws ClassNotFoundException
	 * 		throws ClassNotFoundException in case of any failure
	 * @throws IOException
	 * 		throws IOException in case of any failure
	 * @throws SQLException
	 * 		throws SQLException in case of any failure
	 * @throws DataSourceNotFoundException
	 * 		throws DataSourceException in case of any failure
	 */
	public Connection getConnection(String server, String port, String dbName, String username, String password) throws ClassNotFoundException, IOException, SQLException, DataSourceNotFoundException;
}
