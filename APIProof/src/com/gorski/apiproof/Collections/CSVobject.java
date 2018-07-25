package com.gorski.apiproof.Collections;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import org.testng.Reporter;

public class CSVobject {
	private String[][] dataSet;
	private String fileName;

	public CSVobject(String fileName) {
		super();
		this.fileName = fileName;
		this.collectData();
	}
	public String[][] getData() {
		return dataSet;
	}

	private void collectData() {
		String line="";
        BufferedReader fileReader;
		try {
			fileReader = new BufferedReader(new FileReader(fileName));
        int rowNum = 0;
        while ((line = fileReader.readLine()) != null) {
            try {
                String[] tokens = line.split(",");
                int colNum=0;
                for(String token:tokens) {
                	dataSet[rowNum][colNum] = token;
                }
                rowNum++;
            } catch(Exception e) {
                Reporter.log("Test Failed at step: "+rowNum);
                try {
					fileReader.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
            }
        }
        fileReader.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

}
