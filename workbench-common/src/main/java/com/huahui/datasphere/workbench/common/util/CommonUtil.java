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

import java.lang.invoke.MethodHandles;
import java.net.URI;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.util.UriComponentsBuilder;

public class CommonUtil {

	private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
	
	/**
	 * Builds the rest API URI
	 * @param url
	 * 		URL of the rest API to be invoked.
	 * @param uriParams
	 * 		URI Path variable key-value map.
	 * @return URI
	 * 		Return UIR constructed based on the input parameters.
	 */
	public static URI buildURI(String url, Map<String, String> uriParams) { 
		logger.debug("buildURI() Begin");
		URI resultURI = null;
		UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(url);
		if(null != uriParams) { 
			resultURI = uriBuilder.buildAndExpand(uriParams).encode().toUri();
		} else {
			resultURI = uriBuilder.build().encode().toUri();
		}
		logger.debug("buildURI() End");
		return resultURI;
	}
}
