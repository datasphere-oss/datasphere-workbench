/*
 * Copyright 2019, Huahuidata, Inc.
 * DataSphere is licensed under the Mulan PSL v1.
 * You can use this software according to the terms and conditions of the Mulan PSL v1.
 * You may obtain a copy of Mulan PSL v1 at:
 * http://license.coscl.org.cn/MulanPSL
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND, EITHER EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT, MERCHANTABILITY OR FIT FOR A PARTICULAR
 * PURPOSE.
 * See the Mulan PSL v1 for more details.
 */

package com.datasphere.datasource.connections.jdbc.exception;

import com.datasphere.server.common.exception.DSSException;

/**
 * Created by aladin on 2019. 7. 2..
 */
public class JdbcDataConnectionException extends DSSException {

  public JdbcDataConnectionException(JdbcDataConnectionErrorCodes codes, String message, Throwable cause) {
    super(codes, message, cause);
  }

  public JdbcDataConnectionException(JdbcDataConnectionErrorCodes codes, Throwable cause) {
    super(codes, cause);
  }

  public JdbcDataConnectionException(JdbcDataConnectionErrorCodes codes, String message) {
    super(codes, message);
  }
}
