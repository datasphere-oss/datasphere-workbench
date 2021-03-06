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
public class Queues implements Serializable {

	private static final long serialVersionUID = -2706498941636497320L;
	private List<Queue> queues;

	
	/**
	 * Parameterized Constructor
	 * @param queues
	 * 		List of Queue
	 */
	public Queues(List<Queue> queues) {
		super();
		this.queues = queues;
	}

	/**
	 * Default Constructor
	 */
	public Queues()	{
		this(new ArrayList<Queue>());
	}
	
	/**
	 * @return the queues
	 */
	public List<Queue> getQueues() {
		return queues;
	}

	/**
	 * @param queues the queues to set
	 */
	public void setQueues(List<Queue> queues) {
		this.queues = queues;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return ((queues == null) ? 0 : queues.hashCode());
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
		if (!(obj instanceof Queues)) {
			return false;
		}
		Queues other = (Queues) obj;
		if (queues == null) {
			if (other.queues != null) {
				return false;
			}
		} else if (!queues.equals(other.queues)) {
			return false;
		}
		return true;
	}
	
	
}