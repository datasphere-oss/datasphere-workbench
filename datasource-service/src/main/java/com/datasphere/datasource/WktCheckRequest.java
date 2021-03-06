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

package com.datasphere.datasource;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.apache.commons.collections4.CollectionUtils;

import java.io.Serializable;
import java.util.List;

import com.datasphere.server.common.datasource.LogicalType;
import com.datasphere.server.common.entity.SearchParamValidator;
import com.datasphere.server.common.exception.BadRequestException;

/**
 *
 */
public class WktCheckRequest implements Serializable {

    LogicalType geoType;

    List<String> values;

    public WktCheckRequest() {
    }

    @JsonCreator
    public WktCheckRequest(@JsonProperty("geoType") String geoType,
                           @JsonProperty("values") List<String> values) throws BadRequestException {
        LogicalType type = SearchParamValidator.enumUpperValue(LogicalType.class, geoType, "geoType");
        if (!type.isGeoType()) {
            throw new BadRequestException("Invalid type for geo : " + geoType);
        }
        this.geoType = type;

        if (CollectionUtils.isEmpty(values)) {
            throw new BadRequestException("WKT values required.");
        }
        this.values = values;
    }

    public LogicalType getGeoType() {
        return geoType;
    }

    public List<String> getValues() {
        return values;
    }
}
