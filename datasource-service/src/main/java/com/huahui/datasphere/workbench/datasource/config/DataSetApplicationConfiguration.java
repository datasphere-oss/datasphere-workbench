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

package com.huahui.datasphere.workbench.datasource.config;

import com.huahui.datasphere.client.CommonDataServiceRestClientImpl;
import com.huahui.datasphere.workbench.common.logging.LoggingHandlerInterceptor;
import com.huahui.datasphere.workbench.common.service.ProjectServiceRestClientImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.huahui.datasphere.workbench.datasource.util.CustomConfigProperties;

@Configuration
public class DataSetApplicationConfiguration {
	
	@Autowired
	private CustomConfigProperties customconfigProps;
	
	@Bean
	public LoggingHandlerInterceptor loggingHandlerInterceptor() {
		return new LoggingHandlerInterceptor();
	}
	
	@Bean
	@Lazy(value = true)
	public CommonDataServiceRestClientImpl commonDataServiceRestClientImpl() {
		CommonDataServiceRestClientImpl cdsClient = (CommonDataServiceRestClientImpl) CommonDataServiceRestClientImpl
				.getInstance(customconfigProps.getCmndatasvcurl(), customconfigProps.getCmndatasvcuser(), customconfigProps.getCmndatasvcpwd());
		return cdsClient;
	}
	
	@Bean
	@Lazy(value = true) 
	public ProjectServiceRestClientImpl projectServiceRestClientImpl() {
		ProjectServiceRestClientImpl projectRestClient = new ProjectServiceRestClientImpl(customconfigProps.getProjectServiceURL());
		return projectRestClient;
	}

	@SuppressWarnings("deprecation")
	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurerAdapter() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/mlWorkbench/v1/**").allowedOrigins("*").allowedMethods("GET", "POST", "PUT",
						"DELETE");
			}
		};
	}

}
