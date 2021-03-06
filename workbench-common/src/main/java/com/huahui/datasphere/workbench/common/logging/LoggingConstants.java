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

package com.huahui.datasphere.workbench.common.logging;

public class LoggingConstants {

	public LoggingConstants() {
		throw new UnsupportedOperationException();
	}
	
	public static final class MDCs{
		
		/** MDC correlating messages for a logical transaction. */
		public static final String REQUEST_ID = "X-ACUMOS-Request-Id";
		
		/** MDC recording target entity. */
		public static final String TARGET_ENTITY = "TargetEntity";
		
		/** MDC recording calling service. */
		public static final String PARTNER_NAME = "PartnerName";

		/** MDC recording current service. */
		public static final String SERVICE_NAME = "ServiceName";

		/** MDC recording target service. */
		public static final String TARGET_SERVICE_NAME = "TargetServiceName";

		/** MDC recording current service instance. */
		public static final String INSTANCE_UUID = "InstanceUUID";

		/** MDC recording caller address. */
		public static final String CLIENT_IP_ADDRESS = "ClientIPAddress";

		/** MDC recording server address. */
		public static final String SERVER_FQDN = "ServerFQDN";

		/**
		 * MDC recording timestamp at the start of the current request, with the same
		 * scope as {@link #REQUEST_ID}.
	    */
		public static final String ENTRY_TIMESTAMP = "EntryTimestamp";

		/** MDC reporting outcome code. */
		public static final String RESPONSE_CODE = "ResponseCode";

		/** MDC reporting outcome description. */
		public static final String RESPONSE_DESCRIPTION = "ResponseDescription";

		/** MDC reporting outcome error level. */
		public static final String RESPONSE_SEVERITY = "Severity";

		/** MDC reporting outcome error level. */
		public static final String RESPONSE_STATUS_CODE = "StatusCode";
		
		/** MDC correlating messages for User. */
		public static final String USER = "User";
		
		/**
		 * Hide and forbid construction.
		 */
		private MDCs() {
			throw new UnsupportedOperationException();
		}
		
	}
	
	public static final class Headers{
		public static final String REQUEST_ID = "X-ACUMOS-Request-Id";
		
		public static final String INVOCATION_ID = "Invocation-ID";

		public static final String PARTNER_NAME = "PartnerName";
		
		public static final String RESPONSE_CODE = "ResponseCode";
		
		public static final String RESPONSE_DESCRIPTION = "ResponseDescription";
		
		public static final String RESPONSE_SEVERITY = "ResponseSeverity";
		
		public static final String RESPONSE_STATUS_CODE = "ResponseStatusCode";

		/**
		 * Hide and forbid construction.
		 */
		private Headers() {
			throw new UnsupportedOperationException();
		}
	}
	
	public enum ResponseStatus{
		/** Success. */
		COMPLETED,

		/** Not. */
		ERROR,
		
		/** Inprogress. */
		INPROGRESS 
	}
	
	/**
	 * Response of log level, for setting Severity.
	 */
	public enum ResponseSeverity {

		INFO,
		ERROR,
		TRACE,
		DEBUG,
		WARN,
		FATAL
	}
	

}
