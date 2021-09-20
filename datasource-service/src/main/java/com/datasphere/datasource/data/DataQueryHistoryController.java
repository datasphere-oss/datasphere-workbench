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

package com.datasphere.datasource.data;

import com.google.common.collect.Lists;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

import com.datasphere.datasource.DataSourceQueryHistory;
import com.datasphere.datasource.DataSourceQueryHistoryPredicate;
import com.datasphere.datasource.DataSourceQueryHistoryProjections;
import com.datasphere.datasource.DataSourceQueryHistoryRepository;
import com.datasphere.datasource.DataSourceRepository;
import com.datasphere.datasource.DataSourceSizeHistoryRepository;
import com.datasphere.server.common.entity.SearchParamValidator;
import com.datasphere.server.common.exception.ResourceNotFoundException;
import com.datasphere.server.config.ApiResourceConfig;
import com.datasphere.server.util.ProjectionUtils;
import com.datasphere.server.util.TimeUtils;

@RestController
@RequestMapping(value = ApiResourceConfig.API_PREFIX)
public class DataQueryHistoryController {

  @Autowired
  DataSourceRepository dataSourceRepository;

  @Autowired
  DataSourceQueryHistoryRepository queryHistoryRepository;

  @Autowired
  DataSourceSizeHistoryRepository sizeHistoryRepository;

  @Autowired
  PagedResourcesAssembler pagedResourcesAssembler;

  @Autowired
  ProjectionFactory projectionFactory;

  DataSourceQueryHistoryProjections queryHistoryProjections = new DataSourceQueryHistoryProjections();

  @RequestMapping(value = "/datasources/{id}/query/histories", method = RequestMethod.GET)
  public ResponseEntity<?> findQueryHistories(@PathVariable("id") String dataSourceId,
                                              @RequestParam(value = "queryType", required = false) String queryType,
                                              @RequestParam(value = "succeed", required = false) Boolean succeed,
                                              @RequestParam(value = "from", required = false)
                                                @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE_TIME) DateTime from,
                                              @RequestParam(value = "to", required = false)
                                                @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE_TIME) DateTime to,
                                              @RequestParam(value = "projection", required = false, defaultValue = "default") String projection,
                                              Pageable pageable) {

    if (dataSourceRepository.findById(dataSourceId) == null) {
      throw new ResourceNotFoundException(dataSourceId);
    }

    DataSourceQueryHistory.QueryType type = SearchParamValidator.enumUpperValue(DataSourceQueryHistory.QueryType.class, queryType, "queryType");

    // 기본 정렬 조건 셋팅
    if (pageable.getSort() == null || !pageable.getSort().iterator().hasNext()) {
      pageable = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(),
                                 new Sort(Sort.Direction.DESC, "createdTime"));
    }

    Page<DataSourceQueryHistory> results = queryHistoryRepository.findAll(
        DataSourceQueryHistoryPredicate.searchList(dataSourceId, type, succeed, from, to), pageable);

    return ResponseEntity.ok(pagedResourcesAssembler.toResource(ProjectionUtils.toPageResource(projectionFactory,
                                                                                               queryHistoryProjections.getProjectionByName(projection),
                                                                                               results)));
  }

  @RequestMapping(value = "/datasources/{id}/query/histories/{historyId}", method = RequestMethod.GET)
  public ResponseEntity<?> findQueryHistories(@PathVariable("id") String dataSourceId,
                                              @PathVariable("historyId") String historyId,
                                              @RequestParam(value = "projection", required = false, defaultValue = "default") String projection) throws ResourceNotFoundException {

    if (dataSourceRepository.findById(dataSourceId) == null) {
      throw new ResourceNotFoundException(dataSourceId);
    }

    Optional<DataSourceQueryHistory> history = queryHistoryRepository.findById(historyId);
    if (history == null) {
      throw new ResourceNotFoundException(historyId);
    }

    return ResponseEntity.ok(ProjectionUtils.toResource(projectionFactory,
                                                        queryHistoryProjections.getProjectionByName(projection),
                                                        history));
  }

  @RequestMapping(value = "/datasources/{id}/query/histories/stats/count", method = RequestMethod.GET, produces = "application/json")
  public ResponseEntity<?> findByQueryCountPerHour(@PathVariable("id") String dataSourceId,
                                              @RequestParam(value = "duration", defaultValue = "-P1D") String duration) {
    if (dataSourceRepository.findById(dataSourceId) == null) {
      return ResponseEntity.notFound().build();
    }

    // http://www.kanzaki.com/docs/ical/duration-t.html
    if(!duration.matches(TimeUtils.PATTERN_DURATION_FORMAT.pattern()) ) {
      throw new IllegalArgumentException("Invalid 'duration' parameter. see icalendar duration expression.");
    }

    List<Object> objects = queryHistoryRepository.findByQueryCountPerHour(dataSourceId,
            TimeUtils.getDateTimeByDuration(DateTime.now(), duration));

    ChartSeriesResponse<DateTime> result = new ChartSeriesResponse<>(1);
    for(Object obj : objects) {
      Object[] row = (Object[]) obj;
      if(row.length == 5) {
        DateTime time = new DateTime(DateTimeZone.UTC)
                .withDate((Integer) row[0], (Integer) row[1], (Integer) row[2])
                .withTime((Integer) row[3], 0, 0, 0);
        result.add(time, ((Long) row[4]) * 1d);
      }
    }

    return ResponseEntity.ok(result);
  }

  @RequestMapping(value = "/datasources/{id}/query/histories/stats/user", method = RequestMethod.GET, produces = "application/json")
  public ResponseEntity<?> findByQueryCountPerUser(@PathVariable("id") String dataSourceId,
                                                   @RequestParam(value = "duration", defaultValue = "-P1D") String duration) {
    if (dataSourceRepository.findById(dataSourceId) == null) {
      return ResponseEntity.notFound().build();
    }

    // http://www.kanzaki.com/docs/ical/duration-t.html
    if(!duration.matches(TimeUtils.PATTERN_DURATION_FORMAT.pattern()) ) {
      throw new IllegalArgumentException("Invalid 'duration' parameter. see icalendar duration expression.");
    }

    List<Object> objects = queryHistoryRepository.findByQueryCountPerUser(dataSourceId,
            TimeUtils.getDateTimeByDuration(DateTime.now(), duration));

    ChartSeriesResponse<String> result = new ChartSeriesResponse<>(1);
    for(Object obj : objects) {
      Object[] row = (Object[]) obj;
      if(row.length == 2) {
        result.add((String) row[0], ((Long) row[1]) * 1d);
      }
    }

    return ResponseEntity.ok(result);
  }

  @RequestMapping(value = "/datasources/{id}/query/histories/stats/elapsed", method = RequestMethod.GET, produces = "application/json")
  public ResponseEntity<?> findByQueryCountPerElapsedTime(@PathVariable("id") String dataSourceId,
                                                   @RequestParam(value = "duration", defaultValue = "-P1D") String duration) {
    if (dataSourceRepository.findById(dataSourceId) == null) {
      return ResponseEntity.notFound().build();
    }

    // http://www.kanzaki.com/docs/ical/duration-t.html
    if(!duration.matches(TimeUtils.PATTERN_DURATION_FORMAT.pattern()) ) {
      throw new IllegalArgumentException("Invalid 'duration' parameter. see icalendar duration expression.");
    }

    List<Object> objects = queryHistoryRepository.findByQueryCountPerElapsedTime(dataSourceId,
            TimeUtils.getDateTimeByDuration(DateTime.now(), duration));

    ChartSeriesResponse<String> result = new ChartSeriesResponse<>(1);
    for(Object obj : objects) {
      Object[] row = (Object[]) obj;
      if(row.length == 2) {
        result.add((String) row[0], ((Long) row[1]) * 1d);
      }
    }

    return ResponseEntity.ok(result);
  }

  @RequestMapping(value = "/datasources/{id}/histories/size/stats/hour", method = RequestMethod.GET, produces = "application/json")
  public ResponseEntity<?> findByAvgSizePerHour(@PathVariable("id") String dataSourceId,
                                                   @RequestParam(value = "duration", defaultValue = "-P1D") String duration) {
    if (dataSourceRepository.findById(dataSourceId) == null) {
      return ResponseEntity.notFound().build();
    }

    // http://www.kanzaki.com/docs/ical/duration-t.html
    if(!duration.matches(TimeUtils.PATTERN_DURATION_FORMAT.pattern()) ) {
      throw new IllegalArgumentException("Invalid 'duration' parameter. see icalendar duration expression.");
    }

    List<Object> objects = sizeHistoryRepository.findByAvgSizePerHour(dataSourceId,
            TimeUtils.getDateTimeByDuration(DateTime.now(), duration));

    ChartSeriesResponse<DateTime> result = new ChartSeriesResponse<>(1);
    for(Object obj : objects) {
      Object[] row = (Object[]) obj;
      if(row.length == 5) {
        DateTime time = new DateTime(DateTimeZone.UTC)
                .withDate((Integer) row[0], (Integer) row[1], (Integer) row[2])
                .withTime((Integer) row[3], 0, 0, 0);
        result.add(time, ((Double) row[4]));
      }
    }

    return ResponseEntity.ok(result);
  }

  public class ChartSeriesResponse<T> {
    List<T> x = Lists.newArrayList();
    List<List<Double>> series;

    public ChartSeriesResponse(int seriesCount) {
      series = Lists.newArrayList();

      for(int i = 0; i<seriesCount; i++) {
        series.add(Lists.newArrayList());
      }
    }

    public void add(T x, Double...seriesValues) {
      this.x.add(x);

      if(seriesValues == null || seriesValues.length != this.series.size()) {
        series.forEach((y) -> y.add(Double.NaN));
        return;
      }

      for(int i = 0; i<seriesValues.length; i++) {
        series.get(i).add(seriesValues[i]);
      }

    }

    public List<T> getX() {
      return x;
    }

    public void setX(List<T> x) {
      this.x = x;
    }

    public List<List<Double>> getSeries() {
      return series;
    }

    public void setSeries(List<List<Double>> series) {
      this.series = series;
    }
  }

}
