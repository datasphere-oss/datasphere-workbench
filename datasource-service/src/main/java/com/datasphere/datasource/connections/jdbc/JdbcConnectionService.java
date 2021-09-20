package com.datasphere.datasource.connections.jdbc;



import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.stereotype.Component;
import org.supercsv.prefs.CsvPreference;

import com.datasphere.datasource.connections.jdbc.accessor.JdbcAccessor;
import com.datasphere.datasource.connections.jdbc.dialect.JdbcDialect;
import com.datasphere.datasource.connections.jdbc.exception.JdbcDataConnectionErrorCodes;
import com.datasphere.datasource.connections.jdbc.exception.JdbcDataConnectionException;
import com.datasphere.server.common.datasource.DataType;
import com.datasphere.server.common.datasource.LogicalType;
import com.datasphere.server.common.exception.FunctionWithException;
import com.datasphere.datasource.connections.DataConnection;
import com.datasphere.datasource.connections.DataConnectionHelper;
import com.datasphere.datasource.connections.jdbc.dialect.HiveDialect;
import com.datasphere.datasource.connections.query.NativeCriteria;
import com.datasphere.datasource.connections.query.expression.NativeBetweenExp;
import com.datasphere.datasource.connections.query.expression.NativeCurrentDatetimeExp;
import com.datasphere.datasource.connections.query.expression.NativeDateFormatExp;
import com.datasphere.datasource.connections.query.expression.NativeDisjunctionExp;
import com.datasphere.datasource.connections.query.expression.NativeEqExp;
import com.datasphere.datasource.connections.query.expression.NativeOrderExp;
import com.datasphere.datasource.connections.query.expression.NativeProjection;
import com.datasphere.datasource.connections.query.utils.VarGenerator;
import com.datasphere.datasource.Field;
import com.datasphere.datasource.data.CandidateQueryRequest;
import com.datasphere.datasource.ingestion.jdbc.BatchIngestionInfo;
import com.datasphere.datasource.ingestion.jdbc.JdbcIngestionInfo;
import com.datasphere.datasource.ingestion.jdbc.LinkIngestionInfo;
import com.datasphere.datasource.ingestion.jdbc.SelectQueryBuilder;
import com.datasphere.server.domain.engine.EngineProperties;
import com.datasphere.server.domain.workbook.configurations.filter.Filter;
import com.datasphere.server.domain.workbook.configurations.filter.InclusionFilter;
import com.datasphere.server.domain.workbook.configurations.filter.IntervalFilter;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 *
 */
@Component
public class JdbcConnectionService {

  private static final Logger LOGGER = LoggerFactory.getLogger(JdbcConnectionService.class);

  private static final String RESULTSET_COLUMN_PREFIX = SelectQueryBuilder.TEMP_TABLE_NAME + ".";
  private static final String ANONYMOUS_COLUMN_PREFIX = "anonymous";

  @Autowired
  EngineProperties engineProperties;

  /**
   * Check JDBC connection.
 * @throws JdbcDataConnectionException 
   */
  public Map<String, Object> checkConnection(JdbcConnectInformation connectInformation) throws JdbcDataConnectionException {
    return DataConnectionHelper.getAccessor(connectInformation).checkConnection();
  }

  /**
   * Find list of JDBC database from connection
 * @throws SQLException 
 * @throws JdbcDataConnectionException 
   */
  public Map<String, Object> getDatabases(JdbcConnectInformation connectInformation, String databaseNamePattern, Pageable pageable) throws SQLException, JdbcDataConnectionException {
    return getDatabases(connectInformation, null, databaseNamePattern, pageable);
  }
  // 通过连接获得数据库
  public Map<String, Object> getDatabases(JdbcConnectInformation connectInformation,
                                          Connection connection,
                                          String databaseNamePattern,
                                          Pageable pageable) throws SQLException, JdbcDataConnectionException {
    JdbcAccessor jdbcDataAccessor = DataConnectionHelper.getAccessor(connectInformation);
    jdbcDataAccessor.setConnection(connection);
    return jdbcDataAccessor.getDatabases(connectInformation.getCatalog(), databaseNamePattern,
                                         pageable == null ? null : pageable.getPageSize(),
                                         pageable == null ? null : pageable.getPageNumber());
  }
  // 更改数据库
  public void changeDatabase(JdbcConnectInformation connectInformation, String databaseName) throws JdbcDataConnectionException {
    changeDatabase(connectInformation, databaseName, null);
  }

  public void changeDatabase(JdbcConnectInformation connectInformation, String databaseName, Connection connection) throws JdbcDataConnectionException {
    JdbcAccessor jdbcDataAccessor = DataConnectionHelper.getAccessor(connectInformation);
    jdbcDataAccessor.setConnection(connection);
    jdbcDataAccessor.useDatabase(connectInformation.getCatalog(), databaseName);
  }

  /**
   * Find list of table names from database
 * @throws SQLException 
 * @throws JdbcDataConnectionException 
   */
  public Map<String, Object> getTables(JdbcConnectInformation connectInformation, String databaseName,
                                       Pageable pageable) throws SQLException, JdbcDataConnectionException {
    return getTables(connectInformation, databaseName, null, null, pageable);
  }

  /**
   * Find list of table names from database
 * @throws SQLException 
 * @throws JdbcDataConnectionException 
   */
  public Map<String, Object> getTables(JdbcConnectInformation connectInformation, String databaseName,
                                       String tableNamePattern, Pageable pageable) throws SQLException, JdbcDataConnectionException {
    return getTables(connectInformation, databaseName, tableNamePattern, null, pageable);
  }

  /**
   * Find list of table names from database
 * @throws SQLException 
 * @throws JdbcDataConnectionException 
   */
  public Map<String, Object> getTables(JdbcConnectInformation connectInformation, String databaseName,
                                       String tableName, Connection connection, Pageable pageable) throws SQLException, JdbcDataConnectionException {
    JdbcAccessor jdbcDataAccessor = DataConnectionHelper.getAccessor(connectInformation);
    jdbcDataAccessor.setConnection(connection);
    Map<String, Object> searchedTableMap = jdbcDataAccessor.getTables(connectInformation.getCatalog(), databaseName, tableName,
                                                                      pageable == null ? null : pageable.getPageSize(),
                                                                      pageable == null ? null : pageable.getPageNumber());
    return searchedTableMap;
  }

  /**
   * Find list of table names from database
 * @throws SQLException 
 * @throws JdbcDataConnectionException 
   */
  public Map<String, Object> getTableNames(JdbcConnectInformation connectInformation, String databaseName,
                                           Pageable pageable) throws SQLException, JdbcDataConnectionException {
    return getTableNames(connectInformation, databaseName, null, null, pageable);
  }

  /**
   * Find list of table names from database
 * @throws SQLException 
 * @throws JdbcDataConnectionException 
   */
  public Map<String, Object> getTableNames(JdbcConnectInformation connectInformation, String databaseName,
                                           String tableNamePattern, Pageable pageable) throws SQLException, JdbcDataConnectionException {
    return getTableNames(connectInformation, databaseName, tableNamePattern, null, pageable);
  }

  /**
   * Find list of table names from database
 * @throws SQLException 
 * @throws JdbcDataConnectionException 
   */
  public Map<String, Object> getTableNames(JdbcConnectInformation connectInformation, String databaseName,
                                           String tableName, Connection connection, Pageable pageable) throws SQLException, JdbcDataConnectionException {
    Map<String, Object> searchedTableMap = getTables(connectInformation, databaseName, tableName, connection, pageable);
    List<Map<String, Object>> tableMapList = (List<Map<String, Object>>) searchedTableMap.get("tables");
    List<String> tableNameList = tableMapList.stream()
                                             .map(tableMap -> (String) tableMap.get("name"))
                                             .collect(Collectors.toList());
    searchedTableMap.put("tables", tableNameList);
    return searchedTableMap;
  }

  public Map<String, Object> getTableColumns(JdbcConnectInformation connectInformation, String schema,
                                             String tableName, String columnNamePattern, Pageable pageable) throws JdbcDataConnectionException {
    return getTableColumns(connectInformation, null, schema, tableName, columnNamePattern, pageable);
  }

  public Map<String, Object> getTableColumns(JdbcConnectInformation connectInformation, Connection connection, String schema,
                                             String tableName, String columnNamePattern, Pageable pageable) throws JdbcDataConnectionException {
    Map<String, Object> columnMap = new LinkedHashMap<>();
    List<Map<String, Object>> columnNames = getTableColumnNames(connectInformation, connection, schema, tableName,
                                                                columnNamePattern, pageable);

    //Just dummy paging information
    Map<String, Integer> pageInfoMap = new HashMap<>();
    pageInfoMap.put("size", columnNames.size());
    pageInfoMap.put("totalElements", columnNames.size());
    pageInfoMap.put("totalPages", (int) Math.ceil((double) columnNames.size() / (double) columnNames.size()));
    pageInfoMap.put("number", 0);

    columnMap.put("columns", columnNames);
    columnMap.put("page", pageInfoMap);
    return columnMap;
  }
  // 获得表列名称
  public List<Map<String, Object>> getTableColumnNames(JdbcConnectInformation connectInformation, Connection connection, String schema,
                                                       String tableName, String columnNamePattern, Pageable pageable) throws JdbcDataConnectionException {
    JdbcAccessor jdbcDataAccessor = DataConnectionHelper.getAccessor(connectInformation);
    jdbcDataAccessor.setConnection(connection);
    return jdbcDataAccessor.getColumns(connectInformation.getCatalog(), schema, tableName, columnNamePattern);
  }

  /**
   * Show table description map.
   *
   * @param connectInformation the connectInformation
   * @param schema             the schema
   * @param tableName          the table name
   * @return the map
 * @throws SQLException 
 * @throws JdbcDataConnectionException 
   */
  public Map<String, Object> showTableDescription(JdbcConnectInformation connectInformation, String schema, String tableName) throws SQLException, JdbcDataConnectionException {
    return showTableDescription(connectInformation, null, schema, tableName);
  }

  public Map<String, Object> showTableDescription(JdbcConnectInformation connectInformation,
                                                  Connection connection,
                                                  String schema, String tableName) throws SQLException, JdbcDataConnectionException {
    JdbcAccessor jdbcDataAccessor = DataConnectionHelper.getAccessor(connectInformation);
    jdbcDataAccessor.setConnection(connection);
    return jdbcDataAccessor.showTableDescription(connectInformation.getCatalog(), schema, tableName);
  }

  public int executeUpdate(JdbcConnectInformation connectInformation, String query) throws JdbcDataConnectionException {
    return executeUpdate(connectInformation, null, query);
  }

  public int executeUpdate(JdbcConnectInformation connectInformation, Connection connection, String query) throws JdbcDataConnectionException {
    LOGGER.debug("executeUpdate : {} ", query);
    JdbcAccessor jdbcDataAccessor = DataConnectionHelper.getAccessor(connectInformation);
    jdbcDataAccessor.setConnection(connection);
    try {
      return jdbcDataAccessor.executeUpdate(jdbcDataAccessor.getConnection(), query);
    } catch (SQLException e) {
      LOGGER.error("Fail to executeUpdate query : {}", e.getMessage());
      throw new JdbcDataConnectionException(JdbcDataConnectionErrorCodes.INVALID_QUERY_ERROR_CODE,
                                            "Fail to executeUpdate query : " + e.getMessage());
    }
  }


  public JdbcQueryResultResponse selectQuery(JdbcConnectInformation connectInformation, Connection conn, String query) throws JdbcDataConnectionException {
    return selectQuery(connectInformation, conn, query, -1, false);
  }
  // 执行Query查询
  public JdbcQueryResultResponse selectQuery(JdbcConnectInformation connectInformation, Connection conn, String query,
                                             int limit, boolean extractColumnName) throws JdbcDataConnectionException {

    JdbcDialect dialect = DataConnectionHelper.lookupDialect(connectInformation);
    // int totalRows = countOfSelectQuery(connection, ingestion);
    JdbcQueryResultResponse queryResultSet = null;

    LOGGER.debug("selectQuery : {} ", query);

    Statement stmt = null;
    ResultSet rs = null;
    try {
      stmt = conn.createStatement();

      if (limit > 0)
        stmt.setMaxRows(limit);

      rs = stmt.executeQuery(query);

      queryResultSet = getJdbcQueryResult(rs, dialect, extractColumnName);
      // queryResultSet.setTotalRows(totalRows);
    } catch (SQLException e) {
      LOGGER.error("Fail to query for select : SQLState({}), ErrorCode({}), Message : {}"
          , e.getSQLState(), e.getErrorCode(), e.getMessage());
      throw new JdbcDataConnectionException(JdbcDataConnectionErrorCodes.PREVIEW_TABLE_SQL_ERROR,
                                            "Fail to query : " + e.getSQLState() + ", " + e.getErrorCode() + ", " + e.getMessage());
    } catch (Exception e) {
      LOGGER.error("Fail to query for select :  {}", e.getMessage());
      throw new JdbcDataConnectionException(JdbcDataConnectionErrorCodes.INVALID_QUERY_ERROR_CODE,
                                            "Fail to query : " + e.getMessage());
    } finally {
      closeConnection(conn, stmt, rs);
    }

    return queryResultSet;
  }

  public JdbcQueryResultResponse selectQueryForIngestion(JdbcConnectInformation connectInformation,
                                                         String schema,
                                                         JdbcIngestionInfo.DataType type,
                                                         String query,
                                                         List<Map<String, Object>> partitionList,
                                                         int limit,
                                                         boolean extractColumnName) throws JdbcDataConnectionException {
    JdbcAccessor jdbcDataAccessor = DataConnectionHelper.getAccessor(connectInformation);
    Connection conn = jdbcDataAccessor.getConnection(schema, true);
    String queryString = generateSelectQuery(connectInformation, schema, type, query, partitionList);
    LOGGER.debug("selectQueryForIngestion SQL : {} ", queryString);
    return selectQuery(connectInformation, conn, queryString, limit, extractColumnName);
  }

  public String generateSelectQuery(JdbcConnectInformation connectInformation,
                                    String schema,
                                    JdbcIngestionInfo.DataType type,
                                    String query,
                                    List<Map<String, Object>> partitionList) throws JdbcDataConnectionException{
    String queryString;
    if (type == JdbcIngestionInfo.DataType.TABLE) {
      JdbcDialect dialect = DataConnectionHelper.lookupDialect(connectInformation);
      NativeCriteria nativeCriteria = new NativeCriteria(connectInformation.getImplementor());
      String tableName = dialect.getTableName(connectInformation, connectInformation.getCatalog(), schema, query);
      String tableAlias = dialect.getTableName(connectInformation, connectInformation.getCatalog(), "", query);
      nativeCriteria.addTable(tableName, tableAlias);

      //add projection for partition
      if (partitionList != null && !partitionList.isEmpty()) {

        for (Map<String, Object> partitionMap : partitionList) {
          for (String keyStr : partitionMap.keySet()) {
            nativeCriteria.add(new NativeEqExp(keyStr, partitionMap.get(keyStr)));
          }
        }
      }

      queryString = nativeCriteria.toSQL();
    } else {
      queryString = query;
    }
    return queryString;
  }

  public JdbcQueryResultResponse selectQueryForIngestion(JdbcConnectInformation connectInformation,
                                                         String schema,
                                                         JdbcIngestionInfo.DataType type,
                                                         String query,
                                                         int limit,
                                                         boolean extractColumnName) throws JdbcDataConnectionException {
    return selectQueryForIngestion(connectInformation, schema, type, query, null, limit, extractColumnName);
  }

  public List<String> selectQueryToCsv(JdbcConnectInformation connectInformation,
                                       JdbcIngestionInfo ingestionInfo,
                                       String dataSourceName,
                                       List<Field> fields,
                                       Integer limit) throws JdbcDataConnectionException {

    return selectQueryToCsv(connectInformation, ingestionInfo, null, dataSourceName, fields, null, limit);
  }

  public List<String> selectQueryToCsv(JdbcConnectInformation connectInformation,
                                       JdbcIngestionInfo ingestionInfo,
                                       String baseDir,
                                       String dataSourceName,
                                       List<Field> fields,
                                       List<Filter> filters,
                                       Integer limit) throws JdbcDataConnectionException {

    int fetchSize = ingestionInfo.getFetchSize();
    int maxLimit = limit == null ? ingestionInfo.getMaxLimit() : limit;

    // Get JDBC Connection and set database
    JdbcConnectInformation realConnection = connectInformation == null ? ingestionInfo.getConnection() : connectInformation;
    Preconditions.checkNotNull(realConnection, "connection info. required.");

    JdbcAccessor jdbcDataAccessor = DataConnectionHelper.getAccessor(realConnection);
    JdbcDialect jdbcDialect = jdbcDataAccessor.getDialect();
    Connection connection = jdbcDataAccessor.getConnection();

    List<String> tempCsvFiles = Lists.newArrayList();

    String queryString = makeSelectQuery(jdbcDialect, realConnection, ingestionInfo, fields, filters, maxLimit);

    // make file name to save result of select query
    String tempFileName = getTempFileName(baseDir, EngineProperties.TEMP_CSV_PREFIX + "_"
        + dataSourceName + "_" + System.currentTimeMillis());

    JdbcCSVWriter jdbcCSVWriter = null;
    try {
      jdbcCSVWriter = new JdbcCSVWriter(new FileWriter(tempFileName), CsvPreference.STANDARD_PREFERENCE);
      jdbcCSVWriter.setJdbcDialect(jdbcDialect);
      jdbcCSVWriter.setConnection(connection);
      jdbcCSVWriter.setQuery(queryString);
      jdbcCSVWriter.setFileName(tempFileName);
      jdbcCSVWriter.setFetchSize(fetchSize);
      jdbcCSVWriter.setWithHeader(false);
    } catch (IOException e) {
    }

    String resultFileName = jdbcCSVWriter.write();

    // Handle if no result set
    File file = new File(resultFileName);
    if (!file.exists() && file.length() == 0) {
      return null;
    }

    LOGGER.debug("Created result file : {} ", resultFileName);

    tempCsvFiles.add(tempFileName);

    return tempCsvFiles;

  }

  public String makeSelectQuery(JdbcDialect jdbcDialect,
                                JdbcConnectInformation connectionInfo,
                                JdbcIngestionInfo ingestionInfo,
                                List<Field> fields,
                                List<Filter> filters,
                                int limit) {

    if (ingestionInfo.getDataType() == JdbcIngestionInfo.DataType.QUERY
        && CollectionUtils.isEmpty(filters)) {
      // FixMe: How can I set fetch limitation?
      return ingestionInfo.getQuery();
    }

    NativeCriteria nativeCriteria = new NativeCriteria(connectionInfo.getImplementor());

    if (ingestionInfo.getDataType() == JdbcIngestionInfo.DataType.TABLE) {
      String database = ingestionInfo.getDatabase();
      String table = ingestionInfo.getQuery();
      String tableName = jdbcDialect.getTableName(connectionInfo, connectionInfo.getCatalog(), database, table);
      String tableAlias = jdbcDialect.getTableName(connectionInfo, connectionInfo.getCatalog(), null, table);
      nativeCriteria.addTable(tableName, tableAlias);
    } else {
      nativeCriteria.addSubQuery(StringUtils.replaceAll(ingestionInfo.getQuery(), ";", ""));
    }

    if (CollectionUtils.isEmpty(filters)) {
      if (fields != null && !fields.isEmpty()) {
        NativeProjection nativeProjection = new NativeProjection();

        for (Field field : fields) {
          if (field.isNotPhysicalField()) {
            continue;
          }
          String fieldAlias = field.getName();
          String fieldName = field.getSqlName();
          if (StringUtils.contains(fieldAlias, ".")) {
            String[] splicedFieldAlias = StringUtils.split(fieldAlias, ".");
            fieldAlias = splicedFieldAlias[splicedFieldAlias.length - 1];
          }

          if (Field.COLUMN_NAME_CURRENT_DATETIME.equals(field.getName()) && field.getRole() == Field.FieldRole.TIMESTAMP) {
            nativeProjection.addProjection(new NativeCurrentDatetimeExp(fieldName));
          } else if (StringUtils.isEmpty(field.getTimeFormat()) &&
              (field.getRole() == Field.FieldRole.TIMESTAMP ||
                  (field.getRole() == Field.FieldRole.DIMENSION && field.getType() == DataType.TIMESTAMP))) {
            nativeProjection.addProjection(new NativeDateFormatExp(fieldName, null));
            field.setFormat(NativeDateFormatExp.COMMON_DEFAULT_DATEFORMAT);
          } else {
            nativeProjection.addProjection(fieldName, fieldAlias);
          }
        }

        nativeCriteria.setProjection(nativeProjection);
      }

      if (filters != null && !filters.isEmpty()) {
        for (Filter filter : filters) {
          //Inclusion Filter
          if (filter instanceof InclusionFilter) {
            List<String> valueList = ((InclusionFilter) filter).getValueList();
            if (valueList != null) {
              NativeDisjunctionExp disjunctionExp = new NativeDisjunctionExp();
              for (String value : valueList) {
                disjunctionExp.add(new NativeEqExp(filter.getColumn(), value));
              }
              nativeCriteria.add(disjunctionExp);
            }

            // Interval Filter
          } else if (filter instanceof IntervalFilter) {
            IntervalFilter.SelectorType selectorType = ((IntervalFilter) filter).getSelector();

            //If it's the latest type
            if (selectorType == IntervalFilter.SelectorType.RELATIVE) {
              DateTime startDateTime = ((IntervalFilter) filter).getRelativeStartDate();
              DateTime endDateTime = ((IntervalFilter) filter).utcFakeNow();
              nativeCriteria.add(new NativeBetweenExp(filter.getColumn(), startDateTime, endDateTime));
              //If period is specified
            } else if (selectorType == IntervalFilter.SelectorType.RANGE) {
              List<String> intervals = ((IntervalFilter) filter).getEngineIntervals();
              if (intervals != null && !intervals.isEmpty()) {
                NativeDisjunctionExp disjunctionExp = new NativeDisjunctionExp();
                for (String interval : intervals) {
                  DateTime startDateTime = new DateTime(interval.split("/")[0]);
                  DateTime endDateTime = new DateTime(interval.split("/")[1]);
                  disjunctionExp.add(new NativeBetweenExp(filter.getColumn(), startDateTime, endDateTime));
                }
                nativeCriteria.add(disjunctionExp);
              }
            }
          }
        }
      }

    }

    nativeCriteria.setLimit(limit);

    String queryString = nativeCriteria.toSQL();

    LOGGER.info("Generated SQL query from datasource : {}", queryString);

    return queryString;

  }

  public List<Map<String, Object>> selectCandidateQuery(CandidateQueryRequest queryRequest) throws JdbcDataConnectionException {

    com.datasphere.server.domain.workbook.configurations.field.Field targetField = queryRequest.getTargetField();

    //Required value check target field
    Preconditions.checkNotNull(targetField, "target field. required.");

    //MetaDataSource
    com.datasphere.datasource.DataSource metaDataSource = queryRequest.getDataSource().getMetaDataSource();
    com.datasphere.datasource.Field metaField = metaDataSource.getMetaFieldMap(false, "")
                                                                             .get(targetField.getName());

    //Jdbc Connection
    DataConnection jdbcDataConnection = metaDataSource.getConnection();

    //Ingestion Info (Link)
    LinkIngestionInfo ingestionInfo = (LinkIngestionInfo) metaDataSource.getIngestionInfo();

    // FixMe: Need to duplicated code
    DataConnection realConnection = metaDataSource.getJdbcConnectionForIngestion();
    Preconditions.checkNotNull(realConnection, "connection info. required.");

    JdbcAccessor jdbcDataAccessor = DataConnectionHelper.getAccessor(realConnection);
    JdbcDialect jdbcDialect = jdbcDataAccessor.getDialect();
    Connection connection = jdbcDataAccessor.getConnection(ingestionInfo.getDatabase(), true);

    NativeCriteria nativeCriteria = new NativeCriteria(jdbcDataConnection.getImplementor());
    NativeProjection nativeProjection = new NativeProjection();

    String targetFieldName = targetField.getName();
    if (StringUtils.contains(targetFieldName, ".")) {
      targetFieldName = StringUtils.split(targetFieldName, ".")[1];
    }

    if (metaField.getLogicalType() == LogicalType.TIMESTAMP) {
      nativeProjection.addAggregateProjection(targetFieldName, "minTime", NativeProjection.AggregateProjection.MIN);
      nativeProjection.addAggregateProjection(targetFieldName, "maxTime", NativeProjection.AggregateProjection.MAX);
      nativeCriteria.setProjection(nativeProjection);
      if (ingestionInfo.getDataType() == JdbcIngestionInfo.DataType.TABLE) {
        String database = ingestionInfo.getDatabase();
        String table = ingestionInfo.getQuery();
        String tableName = jdbcDialect.getTableName(realConnection, realConnection.getCatalog(), database, table);
        String tableAlias = jdbcDialect.getTableName(realConnection, realConnection.getCatalog(), null, table);
        nativeCriteria.addTable(tableName, tableAlias);
      } else {
        nativeCriteria.addSubQuery(ingestionInfo.getQuery());
      }
    } else {
      nativeProjection.addProjection(targetFieldName, "field");
      nativeProjection.addAggregateProjection(targetFieldName, "count", NativeProjection.AggregateProjection.COUNT);
      nativeCriteria.setProjection(nativeProjection);
      if (ingestionInfo.getDataType() == JdbcIngestionInfo.DataType.TABLE) {
        String database = ingestionInfo.getDatabase();
        String table = ingestionInfo.getQuery();
        String tableName = jdbcDialect.getTableName(realConnection, realConnection.getCatalog(), database, table);
        String tableAlias = jdbcDialect.getTableName(realConnection, realConnection.getCatalog(), null, table);
        nativeCriteria.addTable(tableName, tableAlias);
      } else {
        nativeCriteria.addSubQuery(ingestionInfo.getQuery());
      }
      nativeCriteria.setOrder((new NativeOrderExp()).add("count", NativeOrderExp.OrderType.DESC));
    }
    nativeCriteria.setLimit(10000);

    String query = nativeCriteria.toSQL();

    LOGGER.debug("Candidate Query : {} ", query);

    JdbcQueryResultResponse queryResult = selectQuery(jdbcDataConnection, connection, query);
    return queryResult.getData();
  }

  private String getTempFileName(String fileName) {
    return getTempFileName(null, fileName);
  }
  // 得到临时文件
  private String getTempFileName(String baseDir, String fileName) {
    if (StringUtils.isEmpty(baseDir)) {
      baseDir = engineProperties.getIngestion().getLocalBaseDir();
    }

    return baseDir + File.separator + fileName + ".csv";
  }
  // 增量查询到CSV
  public List<String> selectIncrementalQueryToCsv(JdbcConnectInformation connectInformation,
                                                  JdbcIngestionInfo ingestionInfo,
                                                  String dataSourceName,
                                                  DateTime maxTime,
                                                  List<Field> fields) throws JdbcDataConnectionException {

    Preconditions.checkArgument(ingestionInfo instanceof BatchIngestionInfo,
                                "Required Batch type Jdbc ingestion information.");

    int fetchSize = ingestionInfo.getFetchSize();
    int maxLimit = ingestionInfo.getMaxLimit();

    Field timestampField = fields.stream()
                                 .filter(field -> field.getRole() == Field.FieldRole.TIMESTAMP)
                                 .findFirst().orElseThrow(() -> new RuntimeException("Timestamp field required."));

    BatchIngestionInfo batchIngestionInfo = (BatchIngestionInfo) ingestionInfo;

    // Get JDBC Connection
    JdbcConnectInformation realConnection = connectInformation == null ? ingestionInfo.getConnection() : connectInformation;
    Preconditions.checkNotNull(realConnection, "connection info. required.");

    JdbcAccessor jdbcDataAccessor = DataConnectionHelper.getAccessor(realConnection);
    JdbcDialect jdbcDialect = jdbcDataAccessor.getDialect();
    Connection connection = jdbcDataAccessor.getConnection(ingestionInfo.getDatabase(), true);

    // Max time Consider if you don't have
    DateTime incrementalTime = maxTime == null ? new DateTime(0L) : maxTime;

    List<String> tempCsvFiles = Lists.newArrayList();

    // Create incremental query
    String queryString = new SelectQueryBuilder(realConnection, jdbcDataAccessor.getDialect())
        .projection(fields)
        .query(batchIngestionInfo, connectInformation)
        .incremental(timestampField, incrementalTime.toString(JdbcDialect.CURRENT_DATE_FORMAT))
        .limit(0, maxLimit)
        .build();

    LOGGER.debug("Generated incremental query : {} ", queryString);

    // Save query results
    String tempFileName = getTempFileName(dataSourceName + "_" + incrementalTime.toString());
    JdbcCSVWriter jdbcCSVWriter = null;
    try {
      jdbcCSVWriter = new JdbcCSVWriter(new FileWriter(tempFileName), CsvPreference.STANDARD_PREFERENCE);
      jdbcCSVWriter.setJdbcDialect(jdbcDialect);
      jdbcCSVWriter.setConnection(connection);
      jdbcCSVWriter.setQuery(queryString);
      jdbcCSVWriter.setFetchSize(fetchSize);
      jdbcCSVWriter.setFileName(tempFileName);
      jdbcCSVWriter.setWithHeader(false);
    } catch (IOException e) {
    }

    String resultFileName = jdbcCSVWriter.write();

    // Handle if no result set
    File file = new File(resultFileName);
    if (!file.exists() || file.length() == 0) {
      return null;
    }

    LOGGER.debug("Created result file : {} ", resultFileName);

    tempCsvFiles.add(tempFileName);

    return tempCsvFiles;
  }

  public int countOfSelectQuery(JdbcConnectInformation connectInformation, JdbcIngestionInfo jdbcInfo) throws JdbcDataConnectionException {
    JdbcAccessor jdbcDataAccessor = DataConnectionHelper.getAccessor(connectInformation);
    Connection connection = jdbcDataAccessor.getConnection();
    return countOfSelectQuery(connectInformation, connection, jdbcInfo);
  }

  public int countOfSelectQuery(JdbcConnectInformation connectInformation, Connection conn, JdbcIngestionInfo jdbcInfo) throws JdbcDataConnectionException {
    JdbcAccessor jdbcDataAccessor = DataConnectionHelper.getAccessor(connectInformation);
    JdbcDialect jdbcDialect = DataConnectionHelper.lookupDialect(connectInformation);
    String queryString = new SelectQueryBuilder(connectInformation, jdbcDialect)
        .countProjection()
        .query(jdbcInfo, connectInformation)
        .build();

    int count = 0;
    try {
      // Is there anything more than 2 billion?
      count = jdbcDataAccessor.executeQueryForObject(conn, queryString, Integer.class);
    } catch (Exception e) {
      LOGGER.error("Fail to get count of query : {}", e.getMessage());
      throw new JdbcDataConnectionException(JdbcDataConnectionErrorCodes.INVALID_QUERY_ERROR_CODE,
                                            "Fail to get count of query : " + e.getMessage());
    }

    return count;
  }

  public JdbcQueryResultResponse getJdbcQueryResult(ResultSet rs, JdbcDialect dialect) throws SQLException {
    return getJdbcQueryResult(rs, dialect, false);
  }

  public JdbcQueryResultResponse getJdbcQueryResult(ResultSet rs, JdbcDialect dialect, boolean extractColumnName) throws SQLException {
    List<Field> fields = getFieldList(rs, extractColumnName);
    List<Map<String, Object>> data = getDataList(rs, fields, dialect.resultObjectConverter());
    return new JdbcQueryResultResponse(fields, data);
  }

  /**
   * Remove prefix for dummy table
   */
  private String removeDummyPrefixColumnName(String name) {
    return StringUtils.removeStartIgnoreCase(name, RESULTSET_COLUMN_PREFIX);
  }

  /**
   * Remove table name
   */
  private String extractColumnName(String name) {
    if (StringUtils.contains(name, ".")) {
      return StringUtils.substring(name, StringUtils.lastIndexOf(name, ".") + 1, name.length());
    }
    return name;
  }

  private String generateUniqueColumnName(String fieldName, List<Field> fieldList) {
    //Add Random Number When Duplicate Field Names
    long duplicated = fieldList.stream()
                               .filter(field -> field.getName().equals(fieldName))
                               .count();
    if (duplicated > 0) {
      if (StringUtils.contains(fieldName, ".")) {
        return StringUtils.split(fieldName, ".")[0] + "." + VarGenerator.gen(fieldName + "_");
      } else {
        return VarGenerator.gen(fieldName + "_");
      }
    }
    return fieldName;
  }

  public void closeConnection(Connection connection, Statement stmt, ResultSet rs) {
    JdbcUtils.closeResultSet(rs);
    JdbcUtils.closeStatement(stmt);
    JdbcUtils.closeConnection(connection);
  }

  public int writeResultSetToCSV(JdbcDialect jdbcDialect, ResultSet resultSet, String tempCsvFilePath, List<String> headers) throws SQLException {
    JdbcCSVWriter jdbcCSVWriter = null;
    int rowNumber = 0;
    try {
      jdbcCSVWriter = new JdbcCSVWriter(new FileWriter(tempCsvFilePath), CsvPreference.STANDARD_PREFERENCE);
      jdbcCSVWriter.setJdbcDialect(jdbcDialect);
      //write header from list if exist
      if (headers != null && !headers.isEmpty()) {
        jdbcCSVWriter.setWithHeader(false);
        jdbcCSVWriter.writeHeaders(headers);
      }
      jdbcCSVWriter.write(resultSet, false);
      rowNumber = jdbcCSVWriter.getRowNumber();
    } catch (IOException e) {
      LOGGER.error("writeResultSetToCSV error", e);
    } finally {
      try {
        if (jdbcCSVWriter != null)
          jdbcCSVWriter.close();
      } catch (IOException e) {
      }
    }
    return rowNumber;
  }

  public List<Field> getFieldList(ResultSet rs, boolean extractColumnName) throws SQLException {
    ResultSetMetaData metaData = rs.getMetaData();

    int colNum = metaData.getColumnCount();

    List<Field> fields = Lists.newArrayList();
    for (int i = 1; i <= colNum; i++) {

      String columnLabel = metaData.getColumnLabel(i);

      if (StringUtils.isEmpty(columnLabel)) {
        columnLabel = ANONYMOUS_COLUMN_PREFIX + i;
      }

      String fieldName;

      if (extractColumnName) {
        fieldName = extractColumnName(columnLabel);
      } else {
        fieldName = removeDummyPrefixColumnName(columnLabel);
      }

      //String uniqueFieldName = generateUniqueColumnName(fieldName, fields);

      Field field = new Field();
      field.setName(fieldName);
      field.setOriginalName(fieldName);
      field.setSqlName(columnLabel);
      field.setType(DataType.jdbcToFieldType((metaData.getColumnType(i))));
      field.setRole(field.getType().toRole());
      fields.add(field);
    }

    Field.checkDuplicatedField(fields, true);

    return fields;
  }


  public List<Map<String, Object>> getDataList(ResultSet rs,
                                               List<Field> fields,
                                               FunctionWithException<Object, Object, SQLException> objectConverter)
      throws SQLException {
    ResultSetMetaData metaData = rs.getMetaData();
    int colNum = metaData.getColumnCount();

    List<Map<String, Object>> dataList = Lists.newArrayList();
    while (rs.next()) {
      Map<String, Object> rowMap = Maps.newLinkedHashMap();
      for (int i = 1; i <= colNum; i++) {
        String fieldName = fields.get(i - 1).getName();
        Object resultObject = rs.getObject(i);
        if (objectConverter != null) {
          rowMap.put(fieldName, objectConverter.apply(resultObject));
        } else {
          rowMap.put(fieldName, resultObject);
        }
      }
      dataList.add(rowMap);
    }
    return dataList;
  }

  public boolean isSupportSaveAsHiveTable(DataConnection jdbcDataConnection){
    try{
      JdbcDialect dialect = DataConnectionHelper.lookupDialect(jdbcDataConnection);
      if(dialect instanceof HiveDialect){
        return HiveDialect.isSupportSaveAsHiveTable(jdbcDataConnection);
      }
    } catch (JdbcDataConnectionException e){
      LOGGER.error("cannot get isSupportSaveAsHiveTable for {}", jdbcDataConnection.getImplementor(), e);
    }
    return false;
  }

  public Object getConnectionInformation(DataConnection jdbcDataConnection){
    Map<String, Object> extensionInfo = new HashMap<>();
    extensionInfo.put("implementor", jdbcDataConnection.getImplementor());

    try{
      JdbcDialect dialect = DataConnectionHelper.lookupDialect(jdbcDataConnection);
      extensionInfo.put("name", dialect.getName());
      extensionInfo.put("scope", dialect.getScope());
      extensionInfo.put("inputSpec", dialect.getInputSpec());
//      extensionInfo.put("iconResource1", dialect.getIconResource1());
//      extensionInfo.put("iconResource2", dialect.getIconResource2());
//      extensionInfo.put("iconResource3", dialect.getIconResource3());
//      extensionInfo.put("iconResource4", dialect.getIconResource4());
      extensionInfo.put("iconResource1", null);
      extensionInfo.put("iconResource2", null);
      extensionInfo.put("iconResource3", null);
      extensionInfo.put("iconResource4", null);
    } catch (JdbcDataConnectionException e){
      LOGGER.error("cannot get ConnectionInformation for {}", jdbcDataConnection.getImplementor(), e);
    }

    return extensionInfo;
  }

}
