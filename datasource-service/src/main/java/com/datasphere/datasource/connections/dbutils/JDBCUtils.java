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

package com.datasphere.datasource.connections.dbutils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

public class JDBCUtils {
	
	public static void set(PreparedStatement pst, int index, String value) throws SQLException {
		if(value == null) {
			pst.setNull(index, Types.NULL);
		}else if(value.trim().equals("")){
			pst.setNull(index, Types.NULL);
		} 
		else if(value.equals("None")){
			pst.setNull(index, Types.NULL);
		}else{
			pst.setString(index, value);
		}
	}
	
	public static String getString(ResultSet set, int index) throws SQLException {
/*		Object obj = set.getObject(index);
		if(obj == null) {
			return null;
		}*/
		return set.getString(index);
	}
}
