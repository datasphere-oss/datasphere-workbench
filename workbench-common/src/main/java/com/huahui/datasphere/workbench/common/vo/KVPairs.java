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
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class KVPairs implements Serializable {

	private static final long serialVersionUID = 1103073714502598140L;
	private List<KVPair> kv;

	
	
	/**
	 * Parameterized Constructor
	 * @param kv
	 * 		List of KVPair
	 */
	public KVPairs(List<KVPair> kv) {
		super();
		this.kv = kv;
	}
	
	/**
	 * Default Constructor
	 */
	public KVPairs() 	{ 
		this(new ArrayList<KVPair>());
	}

	/**
	 * @return the kv
	 */
	public List<KVPair> getKv() {
		return kv;
	}

	/**
	 * @param kv the kv to set
	 */
	public void setKv(List<KVPair> kv) {
		this.kv = kv;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return Objects.hash(kv);
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
		if (!(obj instanceof KVPairs)) {
			return false;
		}
		KVPairs other = (KVPairs) obj;
		if (kv == null) {
			if (other.kv != null) {
				return false;
			}
		} else if (!kv.equals(other.kv)) {
			return false;
		}
		return true;
	}
	
	
}