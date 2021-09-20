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

package com.huahui.datasphere.workbench.datasource.exception;

public class DataSourceNotFoundException extends RuntimeException {

	private static final long serialVersionUID = -1154726640423870697L;

	/**
	 * To Handle DataSourceNotFoundException and returns appropriate response to UI.
	 * @param msg
	 * 			Accepts the Customized Exception Message
	 */
	public DataSourceNotFoundException(String msg) {
		super(msg);
	}
	/**
	 * To handle DataSourceNotFoundException and returns appropriate response to UI.
	 * @param string
	 * 			Accepts the Customized Exception Message
	 * @param statusCode
	 * 			Exception Status Code 
	 */
	public DataSourceNotFoundException(String message, int statusCode) {
		super(message);
	}
	
	/**
	 * To handle DataSourceNotFoundException and returns appropriate response to UI.
	 * @param message
	 * 			Accepts the Customized Exception Message
	 * @param object
	 * 			Throwable class object
	 */
	public DataSourceNotFoundException(String message, Throwable object) {
		super(message, object);
	}
	

}
