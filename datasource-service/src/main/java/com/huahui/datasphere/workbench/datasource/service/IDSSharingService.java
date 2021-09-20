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


package com.huahui.datasphere.workbench.datasource.service;

import java.util.List;

import com.huahui.datasphere.datasphere.workbench.common.vo.DataSourcom.huahui.datasphere com.huahui.datasphere.workbench.common.vo.Users;

public interface IDSSharingService {
	
	/**
	 * Check the Collaborator already exists for the DataSource
	 * @param dataSourceKey
	 * 			The DataSoruce Key
	 * @param collaborators
	 * 			The DataSource Collaborators
	 */
	public void collaboratorExists(String dataSourceKey, Users collaborators);

	/**
	 * Share the DataSource to a User
	 * @param authenticatedUserId
	 * 			The Acumos User Login Id
	 * @param dataSourceKey
	 * 			The DataSource Id
	 * @param collaborators
	 * 			The DataSource Collaborator Details		
	 * @return
	 * 			returns DataSource object
	 */
	public DataSource shareDataSource(String authenticatedUserId, String dataSourceKey, Users collaborators, DataSource datasource);

	/**
	 * To Check the input Collaborator can be removable or not from datasource
	 * @param dataSourceKey
	 * 			The DataSource Key
	 * @param collaborators
	 * 			The Collaborator Details
	 */
	public void isCollaboratorRemovable(String dataSourceKey, Users collaborators);

	/**
	 * To Remove the Collaborator from the Data Source
	 * @param authenticatedUserId
	 * 			The Acumos User Login Id
	 * @param dataSourceKey
	 * 			The DataSource Id
	 * @param collaborators
	 * 			The DataSource Collaborator Details
	 * @param datasource
	 * 			The DataSource to be updated
	 * @return
	 * 			The DataSource object
	 */
	public DataSource removeCollaborator(String authenticatedUserId, String dataSourceKey, Users collaborators, DataSource datasource);

	/**
	 * Get the Shared DataSources for the login user
	 * @param authenticatedUserId
	 *       	The Acumos User Login Id
	 * @return
	 * 			List<DataSource> DataSources list
	 */
	public List<DataSource> getSharedDataSources(String authenticatedUserId);

}
