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

var express = require("express");
var https = require("https");
var bodyParser = require('body-parser');
var cookieParser = require('cookie-parser');
var methodOverride = require('method-override');
var cors = require('cors')

var app = express();
var port = process.env.PORT || 9091;

app.use(cors());
app.use(express.static("../vue-component/dist"));
app.use(cookieParser());
app.use(methodOverride());

app.use(bodyParser.json());

app.use(bodyParser.raw());

app.use(bodyParser.urlencoded({
	extended : true,
}));

app.use(bodyParser.text());

require('./routes_services.js')(app);

var server = app.listen(port, function() {
	console.info('running on ...'+ port);
});

server.timeout = process.env.timeout || 840000; 
