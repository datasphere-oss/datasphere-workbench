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
public class Models implements Serializable {

	private static final long serialVersionUID = 1822273056452006869L;
	private List<Model> models;

	
	
	
	/**
	 * Parameterized Constructor 
	 * @param models
	 * 		The models list
	 */
	public Models(List<Model> models) {
		super();
		this.models = models;
	}
	
	public Models() { 
		this(new ArrayList<Model>());
	}

	/**
	 * @return the models
	 */
	public List<Model> getModels() {
		return models;
	}

	/**
	 * @param models the models to set
	 */
	public void setModels(List<Model> models) {
		this.models = models;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return ((models == null) ? 0 : models.hashCode());
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
		if (!(obj instanceof Models)) {
			return false;
		}
		Models other = (Models) obj;
		if (models == null) {
			if (other.models != null) {
				return false;
			}
		} else if (!models.equals(other.models)) {
			return false;
		}
		return true;
	}
	
	
	
}