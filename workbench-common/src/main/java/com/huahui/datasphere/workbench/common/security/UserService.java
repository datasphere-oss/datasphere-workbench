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

package com.huahui.datasphere.workbench.common.security;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.acumos.cds.client.ICommonDataServiceRestClient;
import org.acumos.cds.domain.MLPUser;
import org.acumos.cds.transport.RestPageRequest;
import org.acumos.cds.transport.RestPageResponse;

public class UserService {
	
	private ICommonDataServiceRestClient cdsClient;
	
	/**
	 * Parameterized constructor
	 * @param cdsClient
	 * 		Object implementing ICommonDataServiceClient.
	 */
	public UserService(ICommonDataServiceRestClient cdsClient) {
		this.cdsClient = cdsClient;
	}
	
	/**
	 * Gets the MLPUser for the specified User login Name.
	 * @param userName
	 * 		User login name
	 * @return
	 * 		MLPUser instance associated to login Name.
	 */
	public MLPUser findUserByUsername(String userName) {
		MLPUser result = null;
		Map<String, Object> queryParams = new HashMap<>();
		queryParams.put("loginName", userName);
		RestPageRequest pageRequest = new RestPageRequest(0, 1);
		RestPageResponse<MLPUser> responseMLPUsers = cdsClient.searchUsers(queryParams, false,
				pageRequest);
		List<MLPUser> users = responseMLPUsers.getContent();
		if (null != users && users.size() > 0) {
			result = users.get(0);
		}
		return result;
	}
}