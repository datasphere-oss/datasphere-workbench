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
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class DataSet implements Serializable {

	private static final long serialVersionUID = 8799755569537046836L;
	private Identifier dataSetId;
	private DataSource dataSource;
	
	
	
	/**
	 * Parameterized Constructor
	 * 数据集
	 * @param dataSetId
	 * 		the dataSetid
	 * @param dataSource
	 * 		the dataSource
	 */
	public DataSet(Identifier dataSetId, DataSource dataSource) {
		super();
		this.dataSetId = dataSetId;
		this.dataSource = dataSource;
	}
	
	/**
	 * Default Constructor
	 */
	public DataSet() {
		this(null, null);
	}

	/**
	 * @return the dataSetId
	 */
	public Identifier getDataSetId() {
		return dataSetId;
	}

	/**
	 * @param dataSetId the dataSetId to set
	 */
	public void setDataSetId(Identifier dataSetId) {
		this.dataSetId = dataSetId;
	}

	/**
	 * @return the dataSource
	 */
	public DataSource getDataSource() {
		return dataSource;
	}

	/**
	 * @param dataSource the dataSource to set
	 */
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return Objects.hash(((dataSetId == null) ? 0 : dataSetId), ((dataSource == null) ? 0 : dataSource));
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
		if (!(obj instanceof DataSet)) {
			return false;
		}
		DataSet other = (DataSet) obj;
		if (dataSetId == null) {
			if (other.dataSetId != null) {
				return false;
			}
		} else if (!dataSetId.equals(other.dataSetId)) {
			return false;
		}
		if (dataSource == null) {
			if (other.dataSource != null) {
				return false;
			}
		} else if (!dataSource.equals(other.dataSource)) {
			return false;
		}
		return true;
	}
	
	
}