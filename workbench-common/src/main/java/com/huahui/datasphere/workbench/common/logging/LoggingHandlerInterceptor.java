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

package com.huahui.datasphere.workbench.common.logging;

import java.lang.invoke.MethodHandles;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * Logging Interceptor called by Spring MVC for each controller handling a
 * RESTful Http request as a server.
 * 
 * Handles logging that should occur before and/or after each HTTP request
 *
 */

@Component
public class LoggingHandlerInterceptor extends HandlerInterceptorAdapter {

	/**
	 * Invokes LogAdapter. Intercept the execution of a handler. Called after
	 * HandlerMapping determined an appropriate handler object, but before
	 * HandlerAdapter invokes the handler.
	 */

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		LoggingAdapter loggingAdapter = new LoggingAdapter(
				LoggerFactory.getLogger(MethodHandles.lookup().lookupClass()));
		loggingAdapter.entering(request);
		return true;
	}

	/**
	 * Callback after completion of request processing, that is, after rendering
	 * the view. Will be called on any outcome of handler execution, thus allows
	 * for proper resource cleanup.
	 * 
	 */

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {

		final LoggingAdapter loggingAdapter = new LoggingAdapter(
				LoggerFactory.getLogger((MethodHandles.lookup().lookupClass())));
		loggingAdapter.exiting();
	}

}
