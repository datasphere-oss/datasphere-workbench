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

package com.datasphere.datasource.controller;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.PersistentEntityResourceAssembler;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.net.URI;
import java.util.Map;

import com.datasphere.datasource.DataSource;
import com.datasphere.datasource.DataSourceAlias;
import com.datasphere.datasource.DataSourceAliasRepository;
import com.datasphere.datasource.DataSourceRepository;
import com.datasphere.server.common.GlobalObjectMapper;
import com.datasphere.server.common.exception.ResourceNotFoundException;
import com.datasphere.server.domain.workbook.DashBoard;
import com.datasphere.server.domain.workbook.DashboardRepository;

/**
 * Created by aladin on 2019. 7. 22..
 */
@RepositoryRestController
public class DataSourceAliasController {

  private static Logger LOGGER = LoggerFactory.getLogger(DataSourceAliasController.class);

  @Autowired
  DataSourceRepository dataSourceRepository;

  @Autowired
  DashboardRepository dashboardRepository;

  @Autowired
  DataSourceAliasRepository dataSourceAliasRepository;

  public DataSourceAliasController() {
  }

  /**
   * Save Data Source Alias ​​Information
   *
   * @param dataSourceAlias
   * @return
 * @throws ResourceNotFoundException 
   */
  @RequestMapping(value = "/datasources/aliases", method = RequestMethod.POST)
  public ResponseEntity<?> createDataSourceAlias(@RequestBody DataSourceAlias dataSourceAlias) throws ResourceNotFoundException {

    // Check if data source exists
    DataSource dataSource = dataSourceRepository.findById(dataSourceAlias.getDataSourceId()).get();
    if (dataSource == null) {
      throw new ResourceNotFoundException(dataSourceAlias.getDataSourceId());
    }

    // Check if field name exists
    if(!dataSource.existFieldName(dataSourceAlias.getFieldName())) {
      throw new ResourceNotFoundException(dataSourceAlias.getFieldName());
    }

    // Check if dashboard exists
    DashBoard dashBoard = dashboardRepository.findById(dataSourceAlias.getDashBoardId()).get();
    if (dashBoard == null) {
      throw new ResourceNotFoundException(dataSourceAlias.getDashBoardId());
    }

    // Check that no Alias ​​or Name is specified
    if(StringUtils.isEmpty(dataSourceAlias.getNameAlias()) && StringUtils.isEmpty(dataSourceAlias.getValueAlias())) {
      throw new IllegalArgumentException("Alias value required.");
    }

    return ResponseEntity.created(URI.create(""))
                         .body(dataSourceAliasRepository.save(dataSourceAlias));
  }

  /**
   * Modify Field Alias ​​Values ​​in Data Sources
   *
   * @param aliasId
   * @param paramMap
   * @return
 * @throws ResourceNotFoundException 
   */
  @RequestMapping(path = "/datasources/aliases/{aliasId}", method = { RequestMethod.PATCH })
  public @ResponseBody
  ResponseEntity<?> appendDataSource(@PathVariable("aliasId") Long aliasId,
                                     @RequestBody Map<String, Object> paramMap) throws ResourceNotFoundException {

    DataSourceAlias alias = dataSourceAliasRepository.findById(aliasId).get();
    if (alias == null) {
      throw new ResourceNotFoundException(String.valueOf(aliasId));
    }

    if(paramMap.containsKey("nameAlias")) {
      alias.setNameAlias((String) paramMap.get("nameAlias"));
    }

    if(paramMap.containsKey("valueAlias")) {
      Object object = paramMap.get("valueAlias");
      if(object != null && object instanceof Map) {
        alias.setValueAlias(GlobalObjectMapper.writeValueAsString(object));
      }
    }

    return ResponseEntity.ok(dataSourceAliasRepository.save(alias));

  }

  /**
   * Delete Alias ​​Information.
   *
   * @param aliasId
   * @return
 * @throws ResourceNotFoundException 
   */
  @RequestMapping(value = "/datasources/aliases/{aliasId}", method = RequestMethod.DELETE)
  public ResponseEntity<?> findDataSources(@PathVariable("aliasId") Long aliasId) throws ResourceNotFoundException {

    DataSourceAlias alias = dataSourceAliasRepository.findById(aliasId).get();
    if (alias == null) {
      throw new ResourceNotFoundException(String.valueOf(aliasId));
    }

    dataSourceAliasRepository.delete(alias);

    return ResponseEntity.noContent().build();
  }

  /**
   * Get alias details.
   *
   * @param aliasId
   * @param resourceAssembler
   * @return
 * @throws ResourceNotFoundException 
   */
  @Transactional(readOnly = true)
  @RequestMapping(value = "/datasources/aliases/{aliasId}", method = RequestMethod.GET)
  public ResponseEntity<?> findDataSources(@PathVariable("aliasId") Long aliasId,
                                           PersistentEntityResourceAssembler resourceAssembler) throws ResourceNotFoundException {

    DataSourceAlias alias = dataSourceAliasRepository.findById(aliasId).get();
    if (alias == null) {
      throw new ResourceNotFoundException(String.valueOf(aliasId));
    }

    return ResponseEntity.ok(resourceAssembler.toResource(alias));
  }
}
