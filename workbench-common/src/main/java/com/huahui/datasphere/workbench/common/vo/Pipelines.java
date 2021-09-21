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
public class Pipelines implements Serializable {

	private static final long serialVersionUID = 2773091969379373036L;
	private List<Pipeline> pipelines;

	
	
	
	/**
	 * Parameterized Constructor
	 * @param pipelines
	 * 		List of Pipeline
	 */
	public Pipelines(List<Pipeline> pipelines) {
		super();
		this.pipelines = pipelines;
	}

	/**
	 * Default Constructor 
	 */
	public Pipelines() { 
		this(new ArrayList<Pipeline>());
	}
	
	/**
	 * @return the pipelines
	 */
	public List<Pipeline> getPipelines() {
		return pipelines;
	}

	/**
	 * @param pipelines the pipelines to set
	 */
	public void setPipelines(List<Pipeline> pipelines) {
		this.pipelines = pipelines;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		return ((pipelines == null) ? 0 : pipelines.hashCode());
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
		if (!(obj instanceof Pipelines)) {
			return false;
		}
		Pipelines other = (Pipelines) obj;
		if (pipelines == null) {
			if (other.pipelines != null) {
				return false;
			}
		} else if (!pipelines.equals(other.pipelines)) {
			return false;
		}
		return true;
	}
	
	
}