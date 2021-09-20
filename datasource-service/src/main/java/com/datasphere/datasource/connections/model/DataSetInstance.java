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

package com.datasphere.datasource.connections.model;

/**
 * dataset_instance 表实例
 */
public class DataSetInstance {
    private String id; //组件实例id
    private String columnsJSON;//对应的字段，JSONArray格式
    private String ciSql;//对应的sql语句，等价于工作流处理的中间结果集，用于datsphere jdbc查询
    /** 版本号对应着结果集的全路径："spaceName".dbName.tableName */
    private String newVersion;//单表查询对应的版本号
    private String joinNewVersion;//两表关联查询对应的版本号
    private String description;//描述

    private String sqlBackup;//sql语句备份，用于common jdbc查询
    public String getSqlBackup() {
        return sqlBackup;
    }

    public void setSqlBackup(String sqlBackup) {
        this.sqlBackup = sqlBackup;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getColumnsJSON() {
        return columnsJSON;
    }

    public void setColumnsJSON(String columnsJSON) {
        this.columnsJSON = columnsJSON;
    }

    public String getCiSql() {
        return ciSql;
    }

    public void setCiSql(String ciSql) {
        this.ciSql = ciSql;
    }

    public String getNewVersion() {
        return newVersion;
    }

    public void setNewVersion(String newVersion) {
        this.newVersion = newVersion;
    }

    public String getJoinNewVersion() {
        return joinNewVersion;
    }

    public void setJoinNewVersion(String joinNewVersion) {
        this.joinNewVersion = joinNewVersion;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
