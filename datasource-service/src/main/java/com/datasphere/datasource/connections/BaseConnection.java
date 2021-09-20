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

package com.datasphere.datasource.connections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.datasphere.datasource.connections.constant.ConnectionInfo;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * 数据连接
 */
public class BaseConnection {

    private static final Logger log = LoggerFactory.getLogger(BaseConnection.class);

    /**
     * 取得数据连接
     * @param connInfo
     * @return
     */
    public static Connection getConnection(ConnectionInfo connInfo) {
        Connection conn = null;
        String url;
        try {
            switch (connInfo.getTypeName()) {
                case DatabaseConstants.DATABASE_ORACLE:
                    Class.forName("oracle.jdbc.driver.OracleDriver");

                    url =
                            "jdbc:oracle:thin:@" +
                                    connInfo.getHostIP() + ":" +
                                    connInfo.getHostPort() + ":" +
                                    connInfo.getDatabaseName();

                    conn = DriverManager.getConnection(
                            url,
                            connInfo.getUserName(),
                            connInfo.getUserPassword());
                    break;
                case DatabaseConstants.DATABASE_MYSQL:
                    Class.forName("com.mysql.jdbc.Driver");
                    url =
                            "jdbc:mysql://" +
                                    connInfo.getHostIP() + ":" +
                                    connInfo.getHostPort() + "/" +
                                    connInfo.getDatabaseName()+"?useUnicode=true&characterEncoding=UTF-8";

                    conn = DriverManager.getConnection(
                            url,
                            connInfo.getUserName(),
                            connInfo.getUserPassword());
                    break;
                case DatabaseConstants.DATABASE_POSTGRES:
                    Class.forName("org.postgresql.Driver");

                    url =
                            "jdbc:postgresql://" +
                                    connInfo.getHostIP() + ":" +
                                    connInfo.getHostPort() + "/" +
                                    connInfo.getDatabaseName();

                    conn = DriverManager.getConnection(
                            url,
                            connInfo.getUserName(),
                            connInfo.getUserPassword());
                    break;
            }

        } catch (Exception ex) {
            log.error("can not create connection: {}", ex);
            conn = null;
        }
        return conn;
    }

}
