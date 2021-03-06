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

package com.huahui.datasphere.workbench.common.service;

import java.lang.invoke.MethodHandles;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.huahui.datasphere.workbench.common.exception.InvalidConfiguration;
import com.huahui.datasphere.workbench.common.exception.TargetServiceInvocationException;
import com.huahui.datasphere.workbench.common.security.SecurityConstants;
import com.huahui.datasphere.workbench.common.util.CommonUtil;
import com.huahui.datasphere.workbench.common.vo.Project;


public class ProjectServiceRestClientImpl implements ProjectServiceRestClient {
	private static final String PROJECT_SERVICE_GET_PROJECT_EXCP = "Project Service - Get Project";

	private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	private static final String PATH_VAR_AUTHENTICATED_USER_ID_KEY = "authenticatedUserId";
	private static final String PATH_VAR_PROJECT_ID_KEY = "projectId";
	private static final String GET_PROJECT_PATH = "/users/{authenticatedUserId}/projects/{projectId}";

	private RestTemplate restTemplate;
	private String basePSURL;
	
	
	public ProjectServiceRestClientImpl(String projServiceURL) { 
		logger.debug("initializeRestClient() Begin");
		
		String surl = projServiceURL;
		try {
			URL url = new URL(surl);
			this.basePSURL = surl;
		} catch (MalformedURLException e) {
			logger.error("Invalid project-service URL " + surl, e);
			throw new InvalidConfiguration("Invalid project-service URL " + surl, e);
		}
				
		restTemplate = new RestTemplate();
		logger.debug("initializeRestClient() End");
	}
	
	
	@Override
	public ResponseEntity<Project> getProject(String authenticatedUserId,String projectId, String authToken) throws TargetServiceInvocationException {
		logger.debug("getProject() Begin");
		ResponseEntity<Project> response = null;
		try { 
			Map<String, String> uriParams = new HashMap<String, String>();
			uriParams.put(PATH_VAR_AUTHENTICATED_USER_ID_KEY, authenticatedUserId);
			uriParams.put(PATH_VAR_PROJECT_ID_KEY, projectId);
			URI uri = CommonUtil.buildURI(this.basePSURL + GET_PROJECT_PATH, uriParams);
			
			//create headers you need to send
			HttpHeaders httpHeaders = new HttpHeaders();
			httpHeaders.set(SecurityConstants.AUTHORIZATION_HEADER_KEY, authToken);
			
			//Create entity to pass on to restTemplate
			HttpEntity<String> entity = new HttpEntity<String>(httpHeaders);
			
			response = restTemplate.exchange(uri, HttpMethod.GET, entity, Project.class);
		} catch (Exception e) { 
			logger.error(PROJECT_SERVICE_GET_PROJECT_EXCP, e);
			throw new TargetServiceInvocationException(PROJECT_SERVICE_GET_PROJECT_EXCP, e);
		}
		logger.debug("getProject() End");
		return response;
	}
	
	
}
