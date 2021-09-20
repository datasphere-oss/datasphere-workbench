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
import java.lang.invoke.MethodHandles;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.huahui.datasphere.workbench.datasource.exception.DataSourceNotFoundException;
import com.huahui.datasphere.workbench.datasource.model.KerberosLogin;

@Service("HiveDataSourceSvcImpl")
public class HiveDataSourceSvcImpl implements IHiveDataSourceSvc{
	
	private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	@Override
	public String getConnectionStatusWithKerberos(KerberosLogin objKerberosLogin, String hostName, String port,
			String query) throws ClassNotFoundException, SQLException, IOException, DataSourceNotFoundException {
		logger.debug("Creating kerberos keytab for hostname " + hostName + " using principal "
				+ objKerberosLogin.getKerberosLoginUser() + " for hive connectivity testing.");
		StringBuilder sb = new StringBuilder();
		sb.append(System.getProperty("user.dir")).append(System.getProperty("file.separator"))
				.append(objKerberosLogin.getKerberosLoginUser()).append(".kerberos.keytab");
		
		
		return null;
	}

}
