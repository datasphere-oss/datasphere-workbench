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

package com.datasphere.datasource.connections.jdbc.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Singleton;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.datasphere.common.data.Column;
import com.datasphere.common.data.Dataset;
import com.datasphere.core.common.utils.UUIDUtils;
import com.datasphere.datasource.connections.BusinessDataType;
import com.datasphere.datasource.connections.constant.KeyWord;
import com.datasphere.datasource.connections.dao.DatasetDAO;
import com.datasphere.datasource.connections.dao.TableDAO;
import com.datasphere.datasource.connections.dao.TableNameGenerator;
import com.datasphere.datasource.connections.dao.impl.ConnectionFactoryImpl;
import com.datasphere.datasource.connections.dao.impl.DatasetDAOImpl;
import com.datasphere.datasource.connections.dao.impl.DefaultTableNameGenerator;
import com.datasphere.datasource.connections.dao.impl.TableDAOImpl;
import com.datasphere.datasource.connections.dbutils.ConnectionFactory;
import com.datasphere.datasource.connections.model.DatasetWrapper;
import com.datasphere.datasource.connections.model.Pager;
import com.datasphere.datasource.connections.model.TableMetaData;
import com.datasphere.datasource.connections.model.TableQuery;
import com.datasphere.datasource.connections.utils.Assert;
import com.datasphere.datasource.connections.utils.StringUtils;

/**
 * 1. KEY????????????????????????????????????????????????
 * 2. ????????? ?????????????????????????????????????????????
 */
@Singleton
public class DataAccessor {
	private static final Logger logger = LoggerFactory.getLogger(DataAccessor.class);

	public DataAccessor() {
	}
	ConnectionFactory connectionFactory;
	Boolean debugMode = false;
	public static String PG_URL = "jdbc:postgresql://app_datasource:5432/dmp";

	TableNameGenerator tableNameGenerator = new DefaultTableNameGenerator();
	DatasetDAO datasetDAO = new DatasetDAOImpl();
	TableDAO tableDAO = new TableDAOImpl();

	final List<String> dataTypeList = Arrays.asList(new String[]{
		BusinessDataType.BDT_BOOLEAN.toLowerCase(),
		BusinessDataType.BDT_DATETIME.toLowerCase(),
		BusinessDataType.BDT_DECIMAL.toLowerCase(),
		BusinessDataType.BDT_INTEGER.toLowerCase(),
		BusinessDataType.BDT_STRING.toLowerCase()
	});
	/**
	 * ???????????????????????????.
	 * ??????jdbc:snappydata:thrift://192.168.15.121[1527]
	 * @param url
	 * @throws Exception
	 */
	public DataAccessor(String url) throws Exception {
		this(PG_URL, "false");
	}

	public DataAccessor(DataSource dataSource) {
		this.connectionFactory = new ConnectionFactoryImpl(dataSource);
		datasetDAO.setConnectionFactory(connectionFactory);
		tableDAO.setConnectionFactory(connectionFactory);
	}

	public DataAccessor(String url, String singleton) throws Exception {
		this.connectionFactory = new ConnectionFactoryImpl(PG_URL, "true".equalsIgnoreCase(singleton));
		datasetDAO.setConnectionFactory(connectionFactory);
		tableDAO.setConnectionFactory(connectionFactory);
	}

	/**
	 * 2.2.1 ???????????????????????????????????????
	 * @param key??????????????????
	 * @param pageNumber?????????
	 * @param pageSize?????????????????????
	 * @return
	 * @throws SQLException
	 */
	public DatasetWrapper getDataset(String key, Integer pageNumber, Integer pageSize) throws Exception {
		ConnectionFactory connectionFactory = new ConnectionFactoryImpl(PG_URL,false);
		Assert.isTrue(!StringUtils.isBlank(key), "The key of dataset can't be null!");
		Assert.isTrue((pageNumber == null && pageSize == null) || (pageNumber != null && pageSize != null && pageNumber > 0 && pageSize > 0),
				"Wrong pageNumber or pageSize!");
		Connection conn = connectionFactory.getConnection();
		try {
			try(Statement st = conn.createStatement()) {
				// ???????????????metadata
				Dataset res = datasetDAO.get(key);
				if(res == null)	return null;
				if(res.getColumnsMeta() == null) return DatasetWrapper.from(res);

				String tableName = tableNameGenerator.generate(key);
				TableQuery query = new TableQuery();
				query.setTabelName(tableName);
				query.setRowBase(true);
				query.setColumnNames(null);

//				if(pageNumber != null && pageSize != null) {
//					Pager pager = new Pager(pageNumber, pageSize);
//					pager.setTotal(tableDAO.count(tableName));
//					query.setPager(pager);
//				}
				String[]columnNames=new String[res.getColumnsMeta().length];
				int index=0;
				for(Column col:res.getColumnsMeta() )
				{
					columnNames[index++]=col.getName();
				}
				query.setColumnNames(columnNames);
				res.setData(tableDAO.getData(query));
				return DatasetWrapper.from(res).setPager(query.getPager());
			}
		} finally {
			connectionFactory.returnConnection(conn);
		}
	}

	/**
	 * 2.2.2 ?????????????????????????????????????????????
	 * @param key
	 * @return
	 * @throws SQLException
	 */
	public Dataset getDataset(String key) throws SQLException {
		Assert.isTrue(!StringUtils.isBlank(key), "The key of dataset can't be null!");
		Connection conn = connectionFactory.getConnection();
		try {
			try(Statement st = conn.createStatement()) {
				// ???????????????metadata
				Dataset res = datasetDAO.get(key);
				if(res == null)	return null;
				if(res.getColumnsMeta() == null) return res;
				String tableName = tableNameGenerator.generate(key);
				TableQuery query = new TableQuery();
				query.setTabelName(tableName);
				query.setRowBase(true);
				query.setColumnNames(null);
				String[]columnNames=new String[res.getColumnsMeta().length];
				int index=0;
				for(Column col:res.getColumnsMeta() )
				{
					columnNames[index++]=col.getName();
				}
				query.setColumnNames(columnNames);
				res.setData(tableDAO.getData(query));
				return res;
			}
		} finally {
			connectionFactory.returnConnection(conn);
		}
	}

	/**
	 * 2.2.3 ???????????????????????????
	 * @param key??????????????????
	 * @return????????????????????????????????????NULL???
	 * @throws SQLException
	 */
	public Dataset getDatasetMetadata(String key) throws SQLException {
		Assert.isTrue(!StringUtils.isBlank(key), "The key of dataset can't be null!");
		Connection conn = connectionFactory.getConnection();
		try {
			return datasetDAO.get(key);
		} finally {
			connectionFactory.returnConnection(conn);
		}
	}
	/**
	 * ??????????????????
	 * @param key??????????????????
	 * @return????????????????????????????????????NULL???
	 * @throws SQLException
	 */
	public Dataset getDataset(String key, String... columns) throws SQLException {
		return getDataset(key, true,false,null,null, columns);
	}
	public Dataset getDateSet(String key, Integer pageNumber, Integer pageSize, String... columns)throws SQLException
	{
		return getDataset(key, true,false,pageNumber,pageSize, columns);
	}
	/**
	 *  ???????????????????????????
	 * @param key??????????????????
	 * @return????????????????????????????????????NULL???
	 * @throws SQLException
	 */
	public Dataset getDatasetWithPkID(String key, String... columns) throws SQLException {
		return getDataset(key, true,false, null, null, columns);
	}

	protected Dataset getDataset(String key, Boolean rowBase,Boolean isWithPkid, Integer pageNumber, Integer pageSize, String... columns) throws SQLException {
		Assert.isTrue(!StringUtils.isBlank(key), "The key of dataset can't be null!");
		for(String name: columns) {
			Assert.isTrue(!StringUtils.isBlank(name), "Invalid name of column!");
		}
		Connection conn = connectionFactory.getConnection();
		try(Statement st = conn.createStatement()) {
			Dataset dataset = datasetDAO.get(key);
			if(dataset == null) return dataset;
			if(dataset.getColumnsMeta() == null) return dataset;

			String tableName = tableNameGenerator.generate(key);
			if(columns.length != 0) {
				Column[] selectCols = extract(dataset.getColumnsMeta(),isWithPkid, columns);
				dataset.setColumnsMeta(selectCols);
			}
			TableQuery query = new TableQuery();
			query.setTabelName(tableName);
			query.setRowBase(rowBase);

			String[] columnNames = new String[dataset.getColumnsMeta().length];

			for(int index = 0; index < dataset.getColumnsMeta().length; index++) {
				columnNames[index] = dataset.getColumnsMeta()[index].getName();
			}
			query.setColumnNames(columnNames);
			if(pageNumber != null && pageSize != null) {
				query.setPager(new Pager(pageNumber, pageSize));
			}
			dataset.setData(tableDAO.getData(query));
			return dataset;
		} finally {
			connectionFactory.returnConnection(conn);
		}
	}

	public String setDataset(Dataset dataset) throws Exception {
		return setDataset(dataset, true);
	}

	/**
	 * ??????????????????????????????
	 * @param dataset??????????????????????????????????????????[columns??????]?????????[data??????]???
	 * @param autoCommit?????????????????????
	 * @throws Exception
	 */
	public String setDataset(Dataset dataset, boolean autoCommit) throws Exception {
		// ??????
		// 1.??????ID
		// 2.??????columns
		Assert.isTrue(dataset != null);
		if(StringUtils.isBlank(dataset.getDataKey())) {
			dataset.setDataKey(UUIDUtils.random());
		}
		// ?????????????????????????????????????????????
		assertIsValid(dataset.getColumnsMeta());

		Connection conn = connectionFactory.getConnection();
		try {
			if(!autoCommit) {
				//conn.setAutoCommit(false);
			}
			String tableName = tableNameGenerator.generate(dataset.getDataKey());
			// ???????????????????????????
			Boolean exists = datasetDAO.exists(dataset.getDataKey());
			// ????????????????????????????????????
			if(exists) {
				try {
					tableDAO.deleteTable(tableName);
				} catch(Throwable t) {
					// ????????????????????????
				}
			}
			// ???????????????
			if(dataset.getColumnsMeta() != null) {
				TableMetaData metadata = new TableMetaData();
				metadata.setTableName(tableName);
				metadata.setColumns(dataset.getColumnsMeta());
				metadata.setData(dataset.getData());
				tableDAO.createTable(metadata);
			}
			if(exists) { 	// ????????????????????????
				datasetDAO.update(dataset);
			} else { 		// ??????????????????
				datasetDAO.insert(dataset);
			}
		} finally {
			connectionFactory.returnConnection(conn);
		}
		return dataset.getDataKey();
	}
	//??????data[][]???????????????????????????????????????????????????
	public void updateDataset(Dataset dataset) throws Exception {
		Connection conn = connectionFactory.getConnection();
		try {
			try {
				String tableName=tableNameGenerator.generate(dataset.getDataKey());
				if(dataset.getColumnsMeta() != null) {
					TableMetaData metadata = new TableMetaData();
					metadata.setTableName(tableName);
					metadata.setColumns(dataset.getColumnsMeta());
					metadata.setData(dataset.getData());
					tableDAO.update(metadata);
				}
			} catch(Throwable t) { // ???????????????????????????????????????????????????????????????
				t.printStackTrace();
			}
		} finally {
			connectionFactory.returnConnection(conn);
		}
		return;
	}
	//?????????
	public boolean dropColumn(String key,String columnName)throws Exception{
		Connection conn = connectionFactory.getConnection();
		try {
			Dataset dataset = datasetDAO.get(key);
			if(dataset==null) return false;
			Column[] columns=dataset.getColumnsMeta();
			//Column[] afterDropColumns=new Column[columns.length-1];
			List<Column>list=new ArrayList<Column>();
			int index=0;
			boolean ishave=false;
			for(Column col : columns){
				if(col.getName().equalsIgnoreCase(columnName)){
					ishave=true;
				} else {
					list.add(col);
				}
			}
			Column[] afterDropColumns=new Column[list.size()];
			for(Column col : list){
				afterDropColumns[index++]=col;
			}
			if(ishave){
				dataset.setColumnsMeta(afterDropColumns);
				datasetDAO.update(dataset);
				try{
				tableDAO.dropColumn(tableNameGenerator.generate(key), columnName);
				}catch(SQLException exp){
					dataset.setColumnsMeta(afterDropColumns);
					return false;
				}
			} else {
				return false;
			}
		} finally {
			connectionFactory.returnConnection(conn);
		}
		return true;
	}

	/**
	 * ????????????????????????KB
	 * @param key??????????????????
	 * @throws SQLException
	 */
	public long getDatasetVolume(String key) throws SQLException {
		Assert.isTrue(!StringUtils.isBlank(key), "The key of dataset can't be null!");
		Connection conn = connectionFactory.getConnection();
		try {
			return tableDAO.getTableVolume(tableNameGenerator.generate(key));
		} finally {
			connectionFactory.returnConnection(conn);
		}
	}

	/**
	 * ???????????????
	 * @param key??????????????????
	 * @return
	 * @throws SQLException
	 */
	public boolean removeDataset(String key) throws SQLException {
		Assert.isTrue(!StringUtils.isBlank(key), "The key of dataset can't be null!");
		Connection conn = connectionFactory.getConnection();
		try {
			logger.info("removeDataset is delete", key);
			System.out.println("removeDataset is delete" + key);
			int rows = datasetDAO.delete(key);
			try {
				tableDAO.deleteTable(tableNameGenerator.generate(key));
			} catch(Throwable t) {
				// ???????????????????????????????????????????????????????????????
			}
			return rows == 1;
		} finally {
			connectionFactory.returnConnection(conn);
		}
	}

	/**
	 * ???????????????????????????????????????
	 * @param key??????????????????
	 * @param columns?????????????????????????????????????????????????????????
	 * @throws SQLException
	 */
	public Dataset getColumnBasedDataset(String key, String... columns) throws SQLException {
		return getDataset(key, false,false,null,null, columns);
	}
	/**
	 * ?????????????????????????????????????????????????????????
	 * @param key??????????????????
	 * @param column?????????????????????????????????????????????????????????
	 * @throws SQLException
	 */
	public Dataset getColumnBasedDataset(String key, Integer pageNumber, Integer pageSize, String... column) throws SQLException {
		return getDataset(key, false,true, pageNumber, pageSize, column);
	}

	public Dataset getColumnBasedDatasetWithPkID(String key, String... columns) throws SQLException {
		return getDataset(key, true,true,null,null, columns);
	}

	public long rowCount(String key) throws SQLException {
		Assert.isTrue(!StringUtils.isBlank(key), "The key of dataset can't be null!");
		Connection conn = connectionFactory.getConnection();
		try {
//			return tableDAO.count(tableNameGenerator.generate(key));
			return tableDAO.count(key);
		} finally {
			connectionFactory.returnConnection(conn);
		}
	}
	/**
	 * ??????????????????????????????
	 * @param key??????????????????
	 * @param data???????????????????????????????????????
	 * @throws SQLException
	 */
	public void append(String key, String[][] data) throws SQLException {
		Connection conn = connectionFactory.getConnection();
		try {
			Dataset dataset = datasetDAO.get(key);
			Assert.isTrue(dataset != null, "Dataset[" + key + "] does not exist!");
			Assert.isTrue(dataset.getColumnsMeta() != null, "Dataset does not contain table!");

			TableMetaData metadata = new TableMetaData();
			metadata.setTableName(tableNameGenerator.generate(key));
			metadata.setColumns(dataset.getColumnsMeta());
			metadata.setData(data);
			tableDAO.append(metadata);
		} finally {
			connectionFactory.returnConnection(conn);
		}
	}

	/**
	 * ?????????????????????????????????????????????
	 * @param key
	 * @return
	 * @throws SQLException
	 */
	public String copy(String key) throws SQLException {
		Connection conn = connectionFactory.getConnection();
		try(Statement st = conn.createStatement()) {
			String newKey = UUIDUtils.random();
			Dataset dataset = datasetDAO.get(key);
			if(dataset!=null) {
				String oldTableName=tableNameGenerator.generate(key);
				String  newTableName=tableNameGenerator.generate(newKey);
				tableDAO.copy(oldTableName, newTableName, dataset.getColumnsMeta());
				dataset.setDataKey(newKey);//????????????datakey
				datasetDAO.insert(dataset);
			} else {
				throw new IllegalArgumentException("Dataset[" + key + "] does not exist!");
			}
			return newKey;
		} finally {
			connectionFactory.returnConnection(conn);
		}
	}

	/**
	 * ???????????????????????????????????????????????????
	 * @param words
	 * @return
	 */
	public Map<String, Integer> check(String[] words) {
		Map<String, Integer> res = new HashMap<String, Integer>();
		for(String word: words) {
			if(!word.matches("[A-Za-z][A-Za-z0-9_]*")) {
				res.put(word, 2);
				continue;
			}
			if(KeyWord.ARRAY.contains(word.toLowerCase())) {
				res.put(word, 1);
			}
		}
		return res;
	}

	protected boolean isValidType(String type) {
		if(StringUtils.isBlank(type)) return false;
		return dataTypeList.contains(type.trim().toLowerCase());
	}

	/**
	 * ??????????????????????????????????????????
	 * @param cols
	 * @param names
	 * @return
	 */
	protected Column[] extract(Column[] cols,boolean isWithPkid, String... names) {
		List<Column> res = new ArrayList<Column>();
		for(String name: names) {
			Assert.isTrue(!StringUtils.isBlank(name), "Invalid name of column!"); 	// ??????????????????
			boolean find = false;													// ???????????????????????????????????????
			for(Column col: cols) {
				if(col.getName().equalsIgnoreCase(name)) {
					find = true;
					if(!res.contains(col))	res.add(col);
					break;
				}
			}
			Assert.isTrue(find, "The column of name[" + name + "] does not exist!");
		}
		if(isWithPkid){
			Column pkCol=new Column();
			pkCol.setName("id___system");
			pkCol.setType("String");
			res.add(pkCol);
		}
		return toArray(res);
	}

	protected Column[] toArray(Collection<Column> list) {
		return list.toArray(new Column[list.size()]);
	}

	protected void assertIsValid(Column[] cols) {
		// ???????????????
		if(cols == null) return ;
		// ????????????????????????
		for(Column col: cols) {
			Assert.isTrue(col != null && !StringUtils.isBlank(col.getName()) && isValidType(col.getType()), "Invalid columns!");
		}
		// ???????????????
		for(int outer = 0; outer < cols.length - 1; outer++) {
			String colName1 = cols[outer].getName().trim().toLowerCase();
			for(int inner = outer + 1; inner < cols.length; inner++) {
				String colName2 = cols[inner].getName().trim().toLowerCase();
				Assert.isTrue(!colName1.equals(colName2), "Repeated columns!");
			}
		}
	}

	public void setDebugMode(Boolean debugMode) {
		this.debugMode = debugMode;
	}

	//???????????????
	public boolean changeColumnType(String key,String columnName,String type)throws SQLException {
		Connection conn = connectionFactory.getConnection();
		try {
			Dataset dataset = datasetDAO.get(key);
			if(dataset==null) {
				return false;
			}
			Column[] columns=dataset.getColumnsMeta();
			for(Column col : columns) {
				if(columnName.equalsIgnoreCase(col.getName())){
					col.setType(type);
				}
			}
			datasetDAO.update(dataset);
		} finally {
			connectionFactory.returnConnection(conn);
		}
		return true;
	}
	//????????????
	public boolean saveasDataset(String key,String newKey, String... columns) throws SQLException {
		Connection conn = connectionFactory.getConnection();
		try(Statement st = conn.createStatement()) {
			//String newKey = UUIDUtils.random();
			Dataset dataset = datasetDAO.get(key);
			if(dataset!=null){
				String oldTableName=tableNameGenerator.generate(key);
				String  newTableName=tableNameGenerator.generate(newKey);
				Boolean exists = datasetDAO.exists(newKey);
				// ????????????????????????????????????
				if(exists) {
					try {
						tableDAO.deleteTable(newTableName);
					} catch(Throwable t) {
						// ????????????????????????
					}
				}
				Column[]columnsMeta=new Column[columns.length];
				System.out.println();
				for(int index=0;index<columns.length;index++) {
					for(Column cMen:dataset.getColumnsMeta()) {
						if(cMen.getName().toLowerCase().equals(columns[index].toLowerCase())) {
							Column c=new Column();
							c.setName(cMen.getName());
							c.setType(cMen.getType());
							columnsMeta[index]=c;
						}
					}
				}
				tableDAO.copy(oldTableName, newTableName,columnsMeta );
				dataset.setDataKey(newKey);//????????????datakey
				dataset.setColumnsMeta(columnsMeta);
				if(exists) { 	// ????????????????????????
					datasetDAO.update(dataset);
				} else { 		// ??????????????????
					datasetDAO.insert(dataset);
				}
			} else {
				throw new IllegalArgumentException("Dataset[" + key + "] does not exist!");
			}
			//return newKey;
		} finally {
			connectionFactory.returnConnection(conn);
		}
		return true;
	}
}
