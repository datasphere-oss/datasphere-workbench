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

package com.huahui.datasphere.workbench.common.exception;
/**
 * This exception is thrown for invalid or incorrect application configuration.
 *
 */
public class InvalidConfiguration extends RuntimeException {

	private static final long serialVersionUID = 7799680694143418475L;

	/**
	 * Default Constructor
	 */
	public InvalidConfiguration() {
		super();
	}
	
	/**
	 * Parameterized Constructor
	 * @param msg
	 * 		Message to be listed in log.
	 */
	public InvalidConfiguration(String msg) { 
		super(msg);
	}
	
	/**
	 * Parameterized Constructor 
	 * @param msg
	 * 		Message to be listed in log.
	 * @param cause
	 * 		Root Exception
	 * 		
	 */
	public InvalidConfiguration(String msg, Throwable cause) {
        super(msg, cause);
    }
	
}
