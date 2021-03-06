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

package com.datasphere.datasource.data.result;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.datasphere.datasource.data.SearchQueryRequest;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by aladin on 2019. 7. 18..
 */
public class ColumnResultFormat {

  public ColumnResultFormat() {
  }

  public Map<String, Object> makeResult(String result, SearchQueryRequest request) {

    JsonNode resultNode = null; //= processRawData(result, request.getProjections());

    if (resultNode.size() == 0) {
      return Maps.newHashMap();
    }

    List<String> fieldNames = request.getProjections().stream()
            .map(field -> field.getAlias())
            .collect(Collectors.toList());

    List<List<Object>> rows = Lists.newArrayList();
    for(JsonNode node : resultNode) {
      List<Object> row = Lists.newArrayList();
      for(String fieldName : fieldNames) {
        row.add(node.get(fieldName).asText());
      }
      rows.add(row);
    }

    Map<String, Object> resultSet = Maps.newHashMap();
    resultSet.put("columns", fieldNames);
    resultSet.put("rows", rows);

    return resultSet;
  }

  public Object makeResult(JsonNode node, SearchQueryRequest request) {

    if (node.size() == 0) {
      return Maps.newHashMap();
    }

    List<String> fieldNames = request.getProjections().stream()
            .map(field -> field.getAlias())
            .collect(Collectors.toList());

    List<List<Object>> rows = Lists.newArrayList();
    for(JsonNode aNode : node) {
      List<Object> row = Lists.newArrayList();
      for(String fieldName : fieldNames) {
        row.add(aNode.get(fieldName).asText());
      }
      rows.add(row);
    }

    Map<String, Object> resultSet = Maps.newHashMap();
    resultSet.put("columns", fieldNames);
    resultSet.put("rows", rows);

    return resultSet;
  }
}
