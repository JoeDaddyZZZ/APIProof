package com.gorski.apiproof;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.testng.TestNG;
import org.testng.xml.XmlClass;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;
import org.uncommons.reportng.HTMLReporter;
import org.uncommons.reportng.JUnitXMLReporter;

public class RestTestDriver {
	static String apiClassPath = "com.gorski.apiproof.TestCases.";

	@SuppressWarnings("deprecation")
	public static void main(String[] args) throws FileNotFoundException,
			IOException {
		
		/*
		 * read arguments
		 */
		String[] tests;
		String testGroupName = "First";
		String testFile = "";
		if(args.length>0) {
			if(args[0].contains(".txt")) {
		        FileReader fileReader = new FileReader(args[0]);
		        testGroupName = new File(args[0]).getName();
		        BufferedReader bufferedReader = new BufferedReader(fileReader);
		        List<String> lines = new ArrayList<String>();
		        String line = null;
		        while ((line = bufferedReader.readLine()) != null) {
		            lines.add(line);
		        }
		        bufferedReader.close();
		        tests =lines.toArray(new String[lines.size()]);
			} else {
				tests = args;
			}
		} else if(testFile.length()>0) {
		        FileReader fileReader = new FileReader(testFile);
		        testGroupName = new File(testFile).getName();
		        BufferedReader bufferedReader = new BufferedReader(fileReader);
		        List<String> lines = new ArrayList<String>();
		        String line = null;
		        while ((line = bufferedReader.readLine()) != null) {
		            lines.add(line);
		        }
		        bufferedReader.close();
		        tests =lines.toArray(new String[lines.size()]);
		} else {
			tests = new String[] {
					"FirstRestTest,posts,1",
				};
		}
		String suiteName = "";
		suiteName = testGroupName;
		/*
		 * create suite
		 */
		List<XmlSuite> suites = new ArrayList<>();
		XmlSuite suite = new XmlSuite();
		suite.setName("API Testing " + suiteName);
		System.out.println("Running test suite  " + suiteName);
		int count = 0;
		/*
		 * for each item in the tests array add a test.
		 */
		for (String test : tests) {
			List<XmlClass> testClasses = new ArrayList<>();
			System.out.println(test);
			String[] vals = test.split(",");
			/*
			 * add test
			 */
			XmlTest newTest = new XmlTest(suite);
			String testClass = vals[0];
			String testName =testClass+"."+vals[1]+"."+vals[2];
			for(int i=3;i<vals.length;i++) {
					testName = testName.concat("."+vals[i]);
			}
			newTest.setName(testName);
			Map<String, String> parameters = new HashMap<String, String>();
			parameters.put("testName", vals[0]);
			parameters.put("endPoint", vals[1]);
			String parm1 = vals[2];
			if(vals.length>3) {
				for(int i=3;i<vals.length;i++) {
					parm1=parm1+","+vals[i];
				}
			}
			parameters.put("testParm1", parm1);
			newTest.setParameters(parameters);
			testClasses.add(new XmlClass(apiClassPath + testClass));
			System.out.println(" adding test " + apiClassPath+testClass);
			newTest.setXmlClasses(testClasses);
			count++;
		}
		suites.add(suite);
			System.out.println(" tests found " + count);
		/*
		 * run suites
		 */
		TestNG tng = new TestNG();
		tng.setUseDefaultListeners(false);
		tng.addListener(new HTMLReporter());
		tng.addListener(new JUnitXMLReporter());
		tng.setXmlSuites(suites);
		tng.setOutputDirectory("output/results/"
				+ new SimpleDateFormat("yyyy-MM-dd.HH.mm.ss")
						.format(new Date()) + suiteName);
		tng.run();
	}
}
