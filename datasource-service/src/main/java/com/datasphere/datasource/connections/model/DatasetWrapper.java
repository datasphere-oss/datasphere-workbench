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

package com.datasphere.datasource.connections.model;

import com.datasphere.common.data.Dataset;


public class DatasetWrapper extends Dataset {
	
	Pager pager;

	public Pager getPager() {
		return pager;
	}

	public DatasetWrapper setPager(Pager pager) {
		this.pager = pager;
		return this;
	}
	
	public static DatasetWrapper from(Dataset dataset) {
		DatasetWrapper res = new DatasetWrapper();
		res.setDataKey(dataset.getDataKey());
		res.setMessage(dataset.getMessage());
		res.setData(dataset.getData());
		res.setColumnsMeta(dataset.getColumnsMeta());
		res.setCreateTime(dataset.getCreateTime());
		res.setLastModified(dataset.getLastModified());
		return res;
	}
}
