package com.gorski.apiproof.Collections;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class XLSobject {
	private HashMap<String, String[][]> dataMap;
	private String[][] dataSet;
	private String fileName;

	public XLSobject(String fileName) {
		super();
		this.fileName = fileName;
		this.collectData();
	}
	public String[][] getData(String sheetName) {
		return dataMap.get(sheetName);
	}

	private void collectData() {
		/*
		 * open workbook file save data in hashmap sheetname, String double
		 * array
		 */
		XSSFWorkbook workbook = null;
		try {
			workbook = new XSSFWorkbook(new FileInputStream(new File(fileName)));
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		/*
		 * for each sheet
		 */
		for (Sheet sheet : workbook) {
			String sheetName = sheet.getSheetName();
			int rowNum = 0;
			/*
			 * for each row
			 */
			for (Row row : sheet) {
				try {
					int colNum = 0;
					for (Cell cell : row) {
						if (cell == null)
							dataSet[rowNum][colNum] = null;
						else {
							cell.setCellType(Cell.CELL_TYPE_STRING);
							dataSet[rowNum][colNum] = cell.getStringCellValue();
						}
						colNum++;
					}
					rowNum++;
				} catch (Exception e) {
					try {
						workbook.close();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
			/*
			 * save data array in map by sheet name
			 */
			dataMap.put(sheetName, dataSet);
		}
		try {
			workbook.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
