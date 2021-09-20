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

public interface IDataSourceInputValidationService {
	
	/**
	 * This method will validates the input json structure
	 * @param dataSource
	 * 		The DataSource object with input values
	 * @throws DataSourceNotFoundException 
	 */
	public void validateInputJson(DataSource dataSource) throws DataSourceNotFoundException;

	/**
	 * To Check is this value is exists or not
	 * @param fieldName
	 * 			input field name
	 * @param value
	 * 			input value
	 */
	public void isValuePresent(String fieldName, String value);
	
	/**
	 * To validate the connection parameters
	 * @param dataSource
	 * 			input DataSource model object
	 */
	public void validateConnectionParameters(DataSource dataSource);

}
