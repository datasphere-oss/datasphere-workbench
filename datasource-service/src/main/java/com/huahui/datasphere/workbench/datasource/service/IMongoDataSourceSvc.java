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

import com.huahui.datasphere.workbench.datasource.exception.DataSourceNotFoundException;
import com.huahui.datasphere.workbench.datasource.model.MongoDBConnectionModel;
import com.mongodb.MongoClient;


public interface IMongoDataSourceSvc {

	/**
	 * To get the Mongo DB connection status
	 * @param mongoDbConnectionModel
	 * 			The MongoDBConnectionModel model object
	 * @param query
	 * 			The database query
	 * @return
	 * 			returns connection status success/failure
	 * @throws IOException
	 * 		throws IOException in case of any failure
	 * @throws DataSourceNotFoundException
	 * 		throws DataSourceException in case of any failure
	 */
	public String getMongoConnectionStatus(MongoDBConnectionModel mongoDbConnectionModel, String query) throws IOException, DataSourceNotFoundException;
	
	/**
	 * To get the Mongo DB Connection 
	 * @param mongoDbConnectionModel
	 * 			The MongoDBConnectionModel model object
	 * @return
	 * 			returns the MongoClient object
	 * @throws IOException
	 * 			throws IOException in case of any failure
	 * @throws DataSourceNotFoundException
	 * 			throws DataSourceException in case of any failure
	 */
	public MongoClient getConnection(MongoDBConnectionModel mongoDbConnectionModel) throws IOException, DataSourceNotFoundException;
}
