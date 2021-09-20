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

import com.huahui.datasphere.datasphere.workbench.common.vo.DataSource;

import com.huahui.datasphere.workbench.datasource.exception.DataSourceNotFoundException;

public interface IDataSourceValidationService {
	
	/**
	 * This Method validates the input json structure
	 * @param authenticatedUserId
	 * 		The Acumos Login Id
	 * @param dataSourceKey
	 * 		The DataSourceKey
	 * @param dataSource
	 * 		The DataSource object with values
	 * @throws DataSourceNotFoundException 
	 */
	public void validateRequest(String authenticatedUserId, String dataSourceKey, DataSource dataSource) throws DataSourceNotFoundException;

	/**
	 * Validate Input Parameter
	 * @param category
	 * 			category 
	 * @param category1
	 * 			category1
	 */
	public void validateInputParameter(String category, String category1);

	
}
