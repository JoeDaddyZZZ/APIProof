package com.gorski.ngTesting;

import java.util.HashMap;

import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.gorski.apiproof.Collections.CSVobject;
import com.gorski.apiproof.Collections.XLSobject;


public class XLStoCSVTest {

	@Test(description = "sysIdData")
	@Parameters({ "testName", "testGroup", "testParm1" })
	public void runTest(String testName, String testGroup, String testParm1) {
		String[] filesFound = testParm1.split(",");
		HashMap<String,String[][]> dataMaps = new HashMap<String,String[][]>();
		HashMap<String,String[]> stringMaps = new HashMap<String,String[]>();
		String[][] dataSet = null;
		/*
		 * collect data
		 */
		for(String fileFound:filesFound) {
			if(fileFound.contains("xls")) {
				String sheetName[] = fileFound.split(".");
				XLSobject xls = new XLSobject(fileFound);
				dataMaps.put(fileFound, xls.getData(sheetName[1]));
			} else if(fileFound.contains("csv")) {
				CSVobject csv = new CSVobject(fileFound);
				dataMaps.put(fileFound, csv.getData());
			}
		}
		/*
		 * configure data
		 */
		for(String fileFound:filesFound) {
			for(String[] dataRow:dataMaps.get(fileFound)) {
				String result = "";
				for(String dataCell:dataRow) {
					result=result.concat(dataCell+",");
				}
				System.out.println(fileFound+ " row " + result);
			}
		}
		SoftAssert softAssert = new SoftAssert();
		/*
		 * finalize
		 */
		softAssert.assertAll();
	}
}

