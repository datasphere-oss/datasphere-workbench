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

package com.huahui.datasphere.workbench.datasource.exception;

import com.huahui.datasphere.datasphere.workbench.common.exception.ValueNotFoundExcepticom.huahui.datasphere com.huahui.datasphere.workbench.common.util.com.huahui.dataspheretus;
import com.huahui.datasphere.workbench.common.vo.ServiceState;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@RestController
public class CustomizedResponseEntityExceptionHandler extends ResponseEntityExceptionHandler{
	
	/**
	 * The method to handle ValueNotFoundException and return the appropriate
	 * response to UI.
	 * 
	 * @param ex
	 *            the exception thrown by the service methods.
	 * @param request
	 *            the Web request.
	 * @return ResponseEntitiy<ServiceState> returns ServiceStatus indicating error
	 */

	@ExceptionHandler(ValueNotFoundException.class)
	public final ResponseEntity<?> handleValueNotFoundException(ValueNotFoundException ex, WebRequest request) {
		ServiceState serviceState = getDataSetErrorDetails(ex);
		return new ResponseEntity<ServiceState>(serviceState, HttpStatus.BAD_REQUEST);
	}
	
	/**
	 * The method to handle DataSourceException and return the appropriate
	 * response to UI.
	 * 
	 * @param ex
	 *            the exception thrown by the service methods.
	 * @param request
	 *            the Web request.
	 * @return ResponseEntitiy<ServiceState> returns ServiceStatus indicating error
	 */
	@ExceptionHandler(DataSourceException.class)
	public final ResponseEntity<?> handleDataSourceException(DataSourceException ex, WebRequest request) {
		ServiceState serviceState = getDataSetErrorDetails(ex);
		return new ResponseEntity<ServiceState>(serviceState, HttpStatus.BAD_REQUEST);
	}
	
	
	/**
	 * The method to handle DataSourceNotFoundException and return the appropriate
	 * response to UI.
	 * 
	 * @param ex
	 *            the exception thrown by the service methods.
	 * @param request
	 *            the Web request.
	 * @return ResponseEntitiy<ServiceState> returns ServiceStatus indicating error
	 */

	@ExceptionHandler(DataSourceNotFoundException.class)
	public final ResponseEntity<?> handleDataSourceNotFoundException(DataSourceNotFoundException ex, WebRequest request) {
		ServiceState serviceState = getDataSetErrorDetails(ex);
		return new ResponseEntity<ServiceState>(serviceState, HttpStatus.BAD_REQUEST);
	}
	
	/**
	 * To handle CouchDBException and returns appropriate response to UI
	 * @param ex
	 * 		the exception thrown by the service method
	 * @param request
	 * 		the WebRequest
	 * @return ResponseEntitiy<ServiceState> 
	 * 		returns ResponseEntity<ServiceState> indicating error
	 */
	@ExceptionHandler(CouchDBException.class)
	public final ResponseEntity<?> handleCouchDBException(CouchDBException ex, WebRequest request) {
		ServiceState serviceState = getDataSetErrorDetails(ex);
		return new ResponseEntity<ServiceState>(serviceState, HttpStatus.NOT_FOUND);
	}
	
	/**
	 * To handle the Association Exception
	 * @param ex
	 * 		the exception thrown by the service method
	 * @param request
	 * 		the WebRequest
	 * @return
	 * 		returns ResponseEntity<ServiceState> indicating error
	 */
	@ExceptionHandler(AssociationException.class)
	public final ResponseEntity<?> handleAssociationException(AssociationException ex, WebRequest request) {
		ServiceState serviceState = getDataSetErrorDetails(ex);
		return new ResponseEntity<ServiceState>(serviceState, HttpStatus.BAD_REQUEST);
	}
	
	/**
	 * To handle the ServiceConnectivityException
	 * @param ex
	 * 		the exception thrown by the service method
	 * @param request
	 * 		the WebRequest
	 * @return
	 * 		returns ResponseEntity<ServiceState> indicating error
	 */
	@ExceptionHandler(ServiceConnectivityException.class)
	public final ResponseEntity<?> handleServiceConnectivityException(ServiceConnectivityException ex, WebRequest request) {
		ServiceState serviceState = getDataSetErrorDetails(ex);
		return new ResponseEntity<ServiceState>(serviceState, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	/**
	 * To handle the CollaboratorExistsException
	 * @param ex
	 * 			the exception thrown by the service method
	 * @param request
	 * 			the WebRequest
	 * @return
	 * 			returns ResponseEntity<ServiceState> indicating error
	 */
	@ExceptionHandler(CollaboratorExistsException.class)
	public final ResponseEntity<?> handleCollaboratorExistsException(CollaboratorExistsException ex, WebRequest request) {
		ServiceState serviceState = getDataSetErrorDetails(ex);
		return new ResponseEntity<ServiceState>(serviceState, HttpStatus.BAD_REQUEST);
	}
	
	private ServiceState getDataSetErrorDetails(RuntimeException ex) {
		ServiceState serviceState = new ServiceState();
		serviceState.setStatus(ServiceStatus.ERROR);
		serviceState.setStatusMessage(ex.getMessage());
		return serviceState;
	}

}
