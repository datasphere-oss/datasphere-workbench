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

import com.huahui.datasphere.datasphere.cds.client.CommonDataServiceRestClientImcom.huahui.datasphere com.huahui.datasphere.workbench.common.security.JWTAuthorizationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.huahui.datasphere.workbench.datasource.util.CustomConfigProperties;

@Configuration
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Autowired
	private CustomConfigProperties customconfigProps;
	
	@Autowired
	private CommonDataServiceRestClientImpl cdsClient;

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/v2/api-docs", "/swagger-resources/configuration/ui", "/swagger-resources",
				"/swagger-resources/configuration/security", "/swagger-ui.html", "/csrf", "/webjars/**", "/error");
	}

	// Secure the endpoints with HTTP Basic authentication
	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http.csrf().disable().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
				.authorizeRequests().antMatchers("/swagger-ui.html").permitAll().anyRequest().authenticated().and()
				.addFilterBefore(jwtAuthorizationFilterBean(), UsernamePasswordAuthenticationFilter.class);
	}

	@Bean
	public JWTAuthorizationFilter jwtAuthorizationFilterBean() throws Exception {
		JWTAuthorizationFilter jwtAuthorizationFilter = new JWTAuthorizationFilter(authenticationManagerBean(),
				customconfigProps.getJwtSecretKey(), cdsClient);
		return jwtAuthorizationFilter;
	}
}