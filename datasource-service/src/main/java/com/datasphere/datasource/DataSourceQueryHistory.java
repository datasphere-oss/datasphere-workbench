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

import org.hibernate.annotations.GenericGenerator;

import static com.datasphere.datasource.DataSourceQueryHistory.QueryType.CANDIDATE;
import static com.datasphere.datasource.DataSourceQueryHistory.QueryType.COVARIANCE;
import static com.datasphere.datasource.DataSourceQueryHistory.QueryType.META;
import static com.datasphere.datasource.DataSourceQueryHistory.QueryType.SEARCH;
import static com.datasphere.datasource.DataSourceQueryHistory.QueryType.SIMILARITY;
import static com.datasphere.datasource.DataSourceQueryHistory.QueryType.SUMMARY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import com.datasphere.datasource.data.CandidateQueryRequest;
import com.datasphere.datasource.data.CovarianceQueryRequest;
import com.datasphere.datasource.data.MetaQueryRequest;
import com.datasphere.datasource.data.QueryRequest;
import com.datasphere.datasource.data.SearchQueryRequest;
import com.datasphere.datasource.data.SummaryQueryRequest;
import com.datasphere.datasource.data.forward.ResultForward;
import com.datasphere.server.common.GlobalObjectMapper;
import com.datasphere.server.common.domain.AbstractHistoryEntity;
import com.datasphere.server.common.domain.DSSDomain;

/**
 * Created by aladin on 2019. 8. 30..
 */
@Entity
@Table(name = "datasource_query")
public class DataSourceQueryHistory extends AbstractHistoryEntity implements DSSDomain<String>  {

  /**
   *  ID
   */
  @Id
  @GeneratedValue(generator = "uuid")
  @GenericGenerator(name = "uuid", strategy = "uuid2")
  @Column(name = "id")
  String id;

  /**
   *  DataSphere Query specification
   */
  @Column(name = "query")
  @Lob
  String query;

  /**
   *  Query target DataSource Id
   */
  @Column(name = "query_ds_id")
  String dataSourceId;

  /**
   *  Query target connection type
   */
  @Column(name = "query_connection_type")
  DataSource.ConnectionType connType;

  /**
   *  DataSphere Query type
   */
  @Column(name = "query_type")
  @Enumerated(EnumType.STRING)
  QueryType queryType;

  /**
   * Engine query specification
   */
  @Column(name = "query_engine")
  @Lob
  String engineQuery;

  /**
   * Engine query ID
   */
  @Column(name = "query_engine_id")
  String engineQueryId;

  /**
   * Engine query type
   */
  @Column(name = "query_engine_type")
  @Enumerated(EnumType.STRING)
  EngineQueryType engineQueryType;

  /**
   * Engine Result Processing Type
   */
  @Column(name = "query_engine_forward")
  @Enumerated(EnumType.STRING)
  ResultForward.ForwardType forwardType;

  /**
   * Query success
   */
  @Column(name = "query_succeed")
  Boolean succeed;

  /**
   * Display message when query fails
   */
  @Column(name = "query_message")
  @Size(max = 5000)
  String message;

  /**
   * Query result count
   */
  @Column(name = "query_result_count")
  Long resultCount;

  /**
   * Query result Size
   */
  @Column(name = "query_result_size")
  Long resultSize;

  /**
   * Total query time
   */
  @Column(name = "query_elapsed_time")
  Long elapsedTime;

  /**
   * Engine query time
   */
  @Column(name = "query_engine_elapsed_time")
  Long engineElapsedTime;

  @Column(name = "query_from_uri", length = 65535, columnDefinition = "TEXT")
  String fromUri;

  @Column(name = "query_from_dashboard_id")
  String fromDashBoardId;

  @Column(name = "query_from_widget_id")
  String fromWidgetId;

  public DataSourceQueryHistory() {
  }

  public void initRequest(QueryRequest request) {

    if (request.getDataSource() != null) {
      DataSource dataSource = request.getDataSource().getMetaDataSource();
      if (dataSource != null) {
        dataSourceId = dataSource.getId();
        connType = dataSource.getConnType();
      }
    }

    query = GlobalObjectMapper.writeValueAsString(request);

    if (request instanceof SearchQueryRequest) {
      queryType = SEARCH;
      SearchQueryRequest searchQueryRequest = (SearchQueryRequest) request;
      if (searchQueryRequest.getResultForward() != null) {
        forwardType = searchQueryRequest.getResultForward().getForwardType();
      }
    } else if (request instanceof CandidateQueryRequest) {
      queryType = CANDIDATE;
    } else if (request instanceof SummaryQueryRequest) {
      queryType = SUMMARY;
    } else if (request instanceof CovarianceQueryRequest) {
      queryType = COVARIANCE;
    } else if (request instanceof MetaQueryRequest) {
      queryType = META;
    } else if (request instanceof SimilarityQueryRequest) {
      queryType = SIMILARITY;
    }

    this.fromUri = request.getContextValue(QueryRequest.CONTEXT_ROUTE_URI);
    this.fromDashBoardId = request.getContextValue(QueryRequest.CONTEXT_DASHBOARD_ID);
    this.fromWidgetId = request.getContextValue(QueryRequest.CONTEXT_WIDGET_ID);
  }

  public DataSourceQueryHistory(String dataSourceId) {
    this.dataSourceId = dataSourceId;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getDataSourceId() {
    return dataSourceId;
  }

  public void setDataSourceId(String dataSourceId) {
    this.dataSourceId = dataSourceId;
  }

  public DataSource.ConnectionType getConnType() {
    return connType;
  }

  public void setConnType(DataSource.ConnectionType connType) {
    this.connType = connType;
  }

  public String getQuery() {
    return query;
  }

  public void setQuery(String query) {
    this.query = query;
  }

  public String getEngineQueryId() {
    return engineQueryId;
  }

  public void setEngineQueryId(String engineQueryId) {
    this.engineQueryId = engineQueryId;
  }

  public QueryType getQueryType() {
    return queryType;
  }

  public void setQueryType(QueryType queryType) {
    this.queryType = queryType;
  }

  public String getEngineQuery() {
    return engineQuery;
  }

  public void setEngineQuery(String engineQuery) {
    this.engineQuery = engineQuery;
  }

  public EngineQueryType getEngineQueryType() {
    return engineQueryType;
  }

  public void setEngineQueryType(EngineQueryType engineQueryType) {
    this.engineQueryType = engineQueryType;
  }

  public ResultForward.ForwardType getForwardType() {
    return forwardType;
  }

  public void setForwardType(ResultForward.ForwardType forwardType) {
    this.forwardType = forwardType;
  }

  public Boolean getSucceed() {
    return succeed;
  }

  public void setSucceed(Boolean succeed) {
    this.succeed = succeed;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public Long getResultCount() {
    return resultCount;
  }

  public void setResultCount(Long resultCount) {
    this.resultCount = resultCount;
  }

  public Long getResultSize() {
    return resultSize;
  }

  public void setResultSize(Long resultSize) {
    this.resultSize = resultSize;
  }

  public Long getElapsedTime() {
    return elapsedTime;
  }

  public void setElapsedTime(Long elapsedTime) {
    this.elapsedTime = elapsedTime;
  }

  public Long getEngineElapsedTime() {
    return engineElapsedTime;
  }

  public void setEngineElapsedTime(Long engineElapsedTime) {
    this.engineElapsedTime = engineElapsedTime;
  }

  public String getFromUri() {
    return fromUri;
  }

  public void setFromUri(String fromUri) {
    this.fromUri = fromUri;
  }

  public String getFromDashBoardId() {
    return fromDashBoardId;
  }

  public void setFromDashBoardId(String fromDashBoardId) {
    this.fromDashBoardId = fromDashBoardId;
  }

  public String getFromWidgetId() {
    return fromWidgetId;
  }

  public void setFromWidgetId(String fromWidgetId) {
    this.fromWidgetId = fromWidgetId;
  }

  @Override
  public String toString() {
    return "DataSourceQueryHistory{" +
        "id='" + id + '\'' +
        ", dataSourceId='" + dataSourceId + '\'' +
        ", connType=" + connType +
        ", queryType=" + queryType +
        ", engineQueryType=" + engineQueryType +
        ", forwardType=" + forwardType +
        ", succeed=" + succeed +
        ", message='" + message + '\'' +
        ", resultCount=" + resultCount +
        ", resultSize=" + resultSize +
        ", elapsedTime=" + elapsedTime +
        ", fromUri='" + fromUri + '\'' +
        ", fromDashBoardId='" + fromDashBoardId + '\'' +
        ", fromWidgetId='" + fromWidgetId + '\'' +
        "} ";
  }

  public enum QueryType {
    CANDIDATE, META, SEARCH, SUMMARY, COVARIANCE, SIMILARITY
  }

  public enum EngineQueryType {
    TOPN, TIMEBOUNDARY, SEARCH, SELECT, SEGMENTMETA, SELECTMETA, GROUPBY, GROUPBYMETA, SUMMARY, COVARIANCE, SIMILARITY
  }
}
