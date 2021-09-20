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

package com.datasphere.datasource.connections.utils;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class ObjectMapperUtils {

	public static ObjectMapper objMapper = new ObjectMapper();
	
	public static <T> T readValue(String json, Class<T> clz) {
		try {
			return objMapper.readValue(json, clz);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}
	
	public static String writeValue(Object o) {
		try {
			return objMapper.writeValueAsString(o);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e.getMessage());
		}
	}
	public static <T> List<T> fromListJson(String str,Class<T> clazz){
		return new Gson().fromJson(str, new TypeToken<List<T>>(){}.getType());
	}
}
