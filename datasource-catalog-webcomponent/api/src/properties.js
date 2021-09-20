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

var config = {
	local : {
		ENVIRONMENT : "dev",
		projectmSURL : "http://localhost:9088/mlWorkbench/v1/project",
		notebookmSURL : "http://localhost:9089/mlWorkbench/v1/notebook",
		pipelinemSURL : "http://localhost:9090/mlWorkbench/v1/pipeline",
		datasourcemSURL : "http://localhost:9097/mlWorkbench/v1/datasource",
		datasourceWikiURL : "https://wiki.datasphere.io/display/TRAIN",
	},
	deploy : {
		ENVIRONMENT : process.env.ENVIRONMENT,
		projectmSURL : process.env.projectmSURL,
		notebookmSURL : process.env.notebookmSURL,
		datasourcemSURL : process.env.datasourcemSURL,
		pipelinemSURL : process.env.pipelinemSURL,
		datasourceWikiURL : process.env.datasourceWikiURL,
	}
};

exports.get = function get(env) {
	return config[env] || config.deploy;
};

