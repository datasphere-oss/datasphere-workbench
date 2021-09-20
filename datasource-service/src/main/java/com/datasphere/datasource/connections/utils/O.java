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

import java.text.SimpleDateFormat;
import java.util.Date;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 快捷输出类；
 * 1.输出到System.out对象
 *
 */
public class O {
	
	public static void log(Object obj) {
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		System.out.println(sdf.format(new Date()) + " Thread(" + String.valueOf(Thread.currentThread().getId()) + ")--" + obj);
	}
	
	public static void json(Object o) {
		try {
			System.out.println(new ObjectMapper().writeValueAsString(o));
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
