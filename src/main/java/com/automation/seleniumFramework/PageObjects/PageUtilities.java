package com.automation.seleniumFramework.PageObjects;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import com.codoid.products.exception.FilloException;
import com.codoid.products.fillo.Connection;
import com.codoid.products.fillo.Fillo;
import com.codoid.products.fillo.Recordset;

public class PageUtilities {

	static Fillo fillo;
	static Connection con;

	public static String getProValue(String uriData) throws IOException {

		Properties prop ;
		FileInputStream fis = null;
		String uri = null;	
		try {
			fis = new FileInputStream("./data/Testdata.properties");
			prop = new Properties();
			prop.load(fis);
			uri = prop.getProperty(uriData);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return uri;	
	}
	/**
	 * 
	 * @param sheetname
	 * @param tc_id
	 * @param value
	 * @return
	 */
	public static HashMap<String, String> readDataFromExcel(String sheetName ,String keyColName , String keyValue) {
		HashMap<String, String> colValMap = new HashMap<String, String>();
		Recordset recordset;

		try {	
			Fillo fillo = new Fillo();
			 con = fillo.getConnection("./data/TestData.xlsx");
			String strQuery = "Select * from " + sheetName + " where " + keyColName + "='" + keyValue + "'";
			recordset = con.executeQuery(strQuery);			
			while(recordset.next()) {
				List<String> columns1=recordset.getFieldNames();
				for(String con1 : columns1) {	
					if(!con1.equalsIgnoreCase(recordset.getField(keyColName))) {		
						colValMap.put(con1, recordset.getField(con1));
					}		
				}		

			}
			recordset.close();
			con.close();
		} catch (FilloException e) {
			e.printStackTrace();
		}
		return colValMap;

	}
	/**
	 * 
	 * @param sheetName
	 * @param keyCol
	 * @param keyValue
	 * @param columnName
	 * @return
	 */
	public static String readDataFromExcel_data(String sheetName , String keyCol , String keyValue ,String columnName) {
		Recordset rs ;
		String data="";
		try {

			Connection con = fillo.getConnection("./data/KT_Videos_List.xlsx");
			String qry = "select * from "+sheetName+"where "+keyCol+"= '"+keyValue+"'";
			rs=con.executeQuery(qry);
			while(rs.next()) {
				data = rs.getField(columnName);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return data;
	}


}
