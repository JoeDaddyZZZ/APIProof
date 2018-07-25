package com.gorski.apiproof.Collections;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import com.gorski.apiproof.DBSQL;


public class SQLDataObject {

	public SQLDataObject(String schemaName, String sqlToRun) {
		super();
		this.schemaName = schemaName;
		this.sqlToRun = sqlToRun;
		this.collectData();
	}

	public SQLDataObject(String dBHost, String dBUser, String dBPassword,
			String schemaName, String sqlToRun) {
		super();
		DBHost = dBHost;
		DBUser = dBUser;
		DBPassword = dBPassword;
		this.schemaName = schemaName;
		this.sqlToRun = sqlToRun;
		this.collectData();
	}
	public static void main(String args[]){
		SQLDataObject sqldata = new SQLDataObject("Movies","select * from actors");
		sqldata.printData();
		
	}

	private HashMap<Integer,ArrayList<SQLCell>> rowMap 
		= new HashMap<Integer,ArrayList<SQLCell>>();
	private String DBHost = "localhost";
	private String DBUser = "joedaddy";
	private String DBPassword = "3foot40";
	private String schemaName = null;
	private String sqlToRun = null;

	public void collectData() {
		DBSQL db = new DBSQL(schemaName);
		ResultSet rs = db.getDBRow(sqlToRun);
		ArrayList<SQLCell> cellList = new ArrayList<SQLCell>();
		try {
			int rowCount=0;
			/*
			 * each row
			 */
			while (rs.next()) {
				
				/*
				 * each column
				 */
				ResultSetMetaData rsmd = rs.getMetaData();
					for(int i = 1; i<=rsmd.getColumnCount();i++) {
						SQLCell cell = new SQLCell();
						cell.value=rs.getObject(i);
						cell.colName=rsmd.getColumnName(i);
						cell.rowNum=rowCount;
						cell.dataType=rsmd.getColumnTypeName(i);
						cell.returnString=cell.value.toString();
						cellList.add(cell);
					}
				rowMap.put(rowCount, cellList);
				rowCount++;
				}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	public void printData() {
		for(int i=0;i<rowMap.size();i++) {
			System.out.println("  row "+i);
			for( SQLCell cell:rowMap.get(i)) {
				System.out.println(" Cell Column Name "+ cell.colName);
				System.out.println(" Cell Value "+ cell.value);
				System.out.println(" Cell String Value "+ cell.returnString);
				System.out.println(" Cell Data Type "+ cell.dataType+ "\n");
				
			}
		}
		
		
	}

	public String getSchemaName() {
		return schemaName;
	}

	public void setSchemaName(String schemaName) {
		this.schemaName = schemaName;
	}

	public String getSqlToRun() {
		return sqlToRun;
	}

	public void setSqlToRun(String sqlToRun) {
		this.sqlToRun = sqlToRun;
	}

	public void setDBHost(String dBHost) {
		DBHost = dBHost;
	}

	public void setDBUser(String dBUser) {
		DBUser = dBUser;
	}

	public void setDBPassword(String dBPassword) {
		DBPassword = dBPassword;
	}
}
final class SQLCell {
	String dataType = "String";
	String colName = null;
	Integer rowNum =0;
	Object value = null;
	String returnString = null;
}
