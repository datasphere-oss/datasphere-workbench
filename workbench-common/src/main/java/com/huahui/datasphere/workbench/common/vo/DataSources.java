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

package com.huahui.datasphere.workbench.common.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class DataSources implements Serializable {

	private static final long serialVersionUID = -7201737502336052564L;
	private List<DataSource> dataSources;

	
	
	/**
	 * Parameterized Constructor
	 * @param dataSources
	 * 		List of DataSource
	 */
	public DataSources(List<DataSource> dataSources) {
		super();
		this.dataSources = dataSources;
	}

	/**
	 * Default Constructor
	 */
	public DataSources(){ 
		this(new ArrayList<DataSource>());
	}
	/**
	 * @return the dataSources
	 */
	public List<DataSource> getDataSources() {
		return dataSources;
	}

	/**
	 * @param dataSources the dataSources to set
	 */
	public void setDataSources(List<DataSource> dataSources) {
		this.dataSources = dataSources;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return (dataSources == null) ? 0 : dataSources.hashCode();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof DataSources)) {
			return false;
		}
		DataSources other = (DataSources) obj;
		if (dataSources == null) {
			if (other.dataSources != null) {
				return false;
			}
		} else if (!dataSources.equals(other.dataSources)) {
			return false;
		}
		return true;
	}
	
	

}