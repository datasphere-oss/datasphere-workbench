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

import java.io.IOException;
import java.sql.SQLException;

import com.huahui.datasphere.workbench.datasource.exception.DataSourceNotFoundException;
import com.huahui.datasphere.workbench.datasource.model.KerberosLogin;

public interface IHiveDataSourceSvc {
	
	/**
	 * Get the Connection Status with K8s Kerberos
	 * @param objKerberosLogin
	 * 		kerberos login Id
	 * @param hostName
	 * 		kerberos hostname
	 * @param port
	 * 		kerberos port
	 * @param query
	 * 		kerberos query
	 * @return
	 * 		return kerberos connection status
	 * @throws ClassNotFoundException
	 * 		throws ClassNotFoundException in case of class not available
	 * @throws SQLException
	 * 		throws SQLException in case of Invalid query
	 * @throws IOException
	 * 		throws IOException in case of doing IO stream operations
	 * @throws DataSrcException
	 * 		throws DataSrcException in case of any failure
	 */
	public String getConnectionStatusWithKerberos(KerberosLogin kerberosLogin, String hostName, String port, String query) throws ClassNotFoundException, SQLException, IOException, DataSourceNotFoundException;

}
