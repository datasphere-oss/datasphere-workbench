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

package com.huahui.datasphere.workbench.common.util;

import java.util.HashMap;
import java.util.Map;

public enum ServiceStatus {
	ACTIVE("AC"), // READY FOR USE
    INACTIVE("IN"), // Can be Made Active
    FAILED("FA"), // FAILED TO START SERVICE
    EXCEPTION("EX"), // GETTING EXCEPTION FROM DEPENDENCIES
    INPROGRESS("IP"), // IN PROCESS OF STARTING
	COMPLETED("CO"), //REUESTED OPERATION WAS SUCCESSFULLY COMPLETED
	ERROR("ER"); //Service API returned an error
	
	private final String serviceStatusCode;
	
	private ServiceStatus(String serviceStatusCode) {
		this.serviceStatusCode = serviceStatusCode;
	}
	
	
	/**
	 * To return String value for ServiceStatusCode. 
	 * @return int
	 * 		return int value for ServiceStatusCode.
	 */
	public String getServiceStatusCode() { 
		return this.serviceStatusCode;
	}
	
	
	//Create reverse lookup hash map 
	private static final Map<String, ServiceStatus> lookup = new HashMap<String, ServiceStatus>();
	
	static {
        for(ServiceStatus ss : ServiceStatus.values()) {
            lookup.put(ss.getServiceStatusCode(), ss);
        }
    }
	
	/**
	 * Returns ServiceStatus Enum for the input code value else null.
	 * @param serviceStatusCode
	 * 		the service status code
	 * @return ServiceStatus
	 */
	public static ServiceStatus get(String serviceStatusCode) { 
        //the reverse lookup by simply getting the value from the lookup HsahMap.
		if(lookup.containsKey(serviceStatusCode)) {
			return lookup.get(serviceStatusCode);
		} else { 
			return null;
		}
         
    }
	
}
