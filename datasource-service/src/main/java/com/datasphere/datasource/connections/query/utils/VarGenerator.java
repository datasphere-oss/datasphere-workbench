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

package com.datasphere.datasource.connections.query.utils;

import org.hibernate.annotations.common.util.StringHelper;

import java.util.Random;

/**
 * Variable names generator.
 */
public class VarGenerator {

    /**
     * Random number generator.
     */
    private static Random random = new Random();

    /**
     * Method generates the variable name of a specified length.
     *
     * @param description the description
     * @return the string
     */
    public static String gen(String description) {
        return StringHelper.generateAlias(description.replaceAll("\\(|\\)", ""), random.nextInt(1000));
    }
}
