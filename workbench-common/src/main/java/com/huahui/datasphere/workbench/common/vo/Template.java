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

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class Template implements Serializable {

	private static final long serialVersionUID = -6157688127053150699L;
	private Identifier templateId;

	
	
	/**
	 * Parameterized Constructor
	 * @param templateId
	 * 		Identifier of Template
	 */
	public Template(Identifier templateId) {
		super();
		this.templateId = templateId;
	}
	
	public Template() { 
		super();
	}

	/**
	 * @return the templateId
	 */
	public Identifier getTemplateId() {
		return templateId;
	}

	/**
	 * @param templateId the templateId to set
	 */
	public void setTemplateId(Identifier templateId) {
		this.templateId = templateId;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return ((templateId == null) ? 0 : templateId.hashCode());
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
		if (!(obj instanceof Template)) {
			return false;
		}
		Template other = (Template) obj;
		if (templateId == null) {
			if (other.templateId != null) {
				return false;
			}
		} else if (!templateId.equals(other.templateId)) {
			return false;
		}
		return true;
	}

	
}