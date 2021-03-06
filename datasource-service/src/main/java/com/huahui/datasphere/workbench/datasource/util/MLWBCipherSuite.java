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

package com.huahui.datasphere.workbench.datasource.util;

import java.lang.invoke.MethodHandles;
import java.nio.charset.StandardCharsets;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.io.BaseEncoding;
import com.huahui.datasphere.workbench.datasource.exception.DataSourceNotFoundException;

public class MLWBCipherSuite {
	private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	private String gDatasourceKey; // global

	private static final String ALGORITHM = "AES";

	public MLWBCipherSuite(String datasourceKey) {
		logger.debug("MLWBCipherSuite() Begin");
		if(datasourceKey != null && datasourceKey.length() > 16) {
			datasourceKey = datasourceKey.substring(datasourceKey.length() - 16, datasourceKey.length());
		}
		logger.debug("MLWBCipherSuite() End");
		this.gDatasourceKey = datasourceKey;
	}

	/**
	 * To encrypt the username and password
	 * @param inStr
	 * 		input String
	 * @return
	 * 		returns the encoded string
	 * @throws DataSourceNotFoundException
	 * 		throws DataSourceException in case of any failure
	 */
	 public String encrypt(String inStr) throws DataSourceNotFoundException {
		 logger.debug("encrypt() Begin");
	    	try {
		        SecretKeySpec secretKey = new SecretKeySpec(gDatasourceKey.getBytes(), ALGORITHM);
		        Cipher cipher = Cipher.getInstance(ALGORITHM);
		        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
		
		        byte[] encryptedBytes = cipher.doFinal(inStr.getBytes(StandardCharsets.UTF_8));
		        
		        //encode binary data into Standard and return
		        logger.debug("encrypt() End");
		        return BaseEncoding.base64().encode(encryptedBytes);
		        
		    } catch (Exception exc) {	
		    	throw new DataSourceNotFoundException("Exception occurred during Encryption of Credentials.", exc);
			}
	    	
	    }
	    
	 /**
	  * To decrypt the user name and password
	  * @param inStr
	  * 	input String
	  * @return
	  * 	returns the encoded string
	  * @throws DataSourceNotFoundException
	  * 	throws DataSourceException in case of any failure
	  */
	    public String decrypt(String inStr) throws DataSourceNotFoundException {
	    	logger.debug("decrypt() Begin");
	    	try {
		        SecretKeySpec secretKey = new SecretKeySpec(gDatasourceKey.getBytes(), ALGORITHM);
		        Cipher cipher = Cipher.getInstance(ALGORITHM);
		        cipher.init(Cipher.DECRYPT_MODE, secretKey);
		
		        //decode standard into binary
		        byte[] brOutput = BaseEncoding.base64().decode(inStr);
		        
		        byte[] decryptedBytes = cipher.doFinal(brOutput);
		        logger.debug("decrypt() End");		
		        return new String(decryptedBytes);
		        
	    	} catch (Exception exc) {	
	    		throw new DataSourceNotFoundException("Exception occurred during Decryption of Credentials.", exc);
	    	}
	    }
	

}
