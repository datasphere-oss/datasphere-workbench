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

package com.datasphere.datasource.data;

import static com.datasphere.datasource.DataSourceErrorCodes.INVALID_EXPR_CODE;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.datasphere.server.common.exception.DSSException;

/**
 * 유효하지 않은 표현식 오류
 */
@ResponseStatus(value= HttpStatus.BAD_REQUEST, reason="Invalid expression")
public class InvalidExpressionException extends DSSException {

  public InvalidExpressionException(String message) {
    super(INVALID_EXPR_CODE, message);
  }
}
