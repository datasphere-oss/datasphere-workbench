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
import com.huahui.datasphere.workbench.common.util.ServiceStatus;

@JsonInclude(Include.NON_NULL)
public class ServiceState implements Serializable {

	private static final long serialVersionUID = 1723033320612927322L;
	private ServiceStatus status;
	private String statusMessage;
	
	/**
	 * Parameterized Constructor
	 * @param status
	 * 		ServiceStatus 
	 * @param statusMessage
	 * 		Message
	 */
	public ServiceState(ServiceStatus status, String statusMessage) {
		super();
		this.status = status;
		this.statusMessage = statusMessage;
	}

	public ServiceState() { 
		super();
	}
	
	/**
	 * @return the status
	 */
	public ServiceStatus getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(ServiceStatus status) {
		this.status = status;
	}

	/**
	 * @return the statusMessage
	 */
	public String getStatusMessage() {
		return statusMessage;
	}

	/**
	 * @param statusMessage the statusMessage to set
	 */
	public void setStatusMessage(String statusMessage) {
		this.statusMessage = statusMessage;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return Objects.hash(((status == null) ? 0 : status),
				((statusMessage == null) ? 0 : statusMessage));
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
		if (!(obj instanceof ServiceState)) {
			return false;
		}
		ServiceState other = (ServiceState) obj;
		if (status != other.status) {
			return false;
		}
		if (statusMessage == null) {
			if (other.statusMessage != null) {
				return false;
			}
		} else if (!statusMessage.equals(other.statusMessage)) {
			return false;
		}
		return true;
	}

	
}
