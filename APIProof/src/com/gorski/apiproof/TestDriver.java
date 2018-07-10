package com.gorski.apiproof;


import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.testng.IReporter;
import org.testng.TestNG;
import org.testng.reporters.JUnitXMLReporter;
import org.testng.xml.XmlClass;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;
import org.uncommons.reportng.HTMLReporter;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 */
public class TestDriver {

    public static String tableUsed;

    @SuppressWarnings("deprecation")
	static public void main(String[] args) {

        List<XmlSuite> testXmlSuites = new ArrayList<>();
        XmlSuite restTest = createSuite("RestTest");
        String pack = "com.gorski.apiproof.testcases.";
        addTest("First Rest Test", pack+"FirstRestTest", restTest);
        testXmlSuites.add(restTest);

        TestNG tng = new TestNG(false);
        IReporter r = new HTMLReporter();
        tng.addListener(r);
        tng.addListener(new JUnitXMLReporter());
        tng.setDefaultTestName("API Test");
        tng.setXmlSuites(testXmlSuites);
        tng.setOutputDirectory("testOutput" + System.getProperty("file.separator") + buildOutputDir(""));
        tng.run();
    }

    public static XmlSuite createSuite(String suiteName) {
        XmlSuite suite = new XmlSuite();
        suite.setName(suiteName);
        suite.setThreadCount(5);
        //suite.addListener(sl);
        return suite;
    }

    public static void addTest(String testName, String classString, XmlSuite xmlSuite) {
        ArrayList<XmlClass> classes = new ArrayList<XmlClass>();
        classes.add(new XmlClass(classString));
        XmlTest xmlTest = new XmlTest(xmlSuite);
        xmlTest.setXmlClasses(classes);
        xmlTest.setName(testName);
    }

    public static String buildOutputDir(String folderName) {
        SimpleDateFormat d = new SimpleDateFormat("yyyyMMddHHmm");
        Date date = new Date();
        String output = d.format(date);
        return folderName + output + ".result";
    }

    static void purgeDirectoryKeepSubdir(File dir) {
        for (File file : dir.listFiles()) {
            if (!file.isDirectory() && file.exists()) {
                file.delete();
            }
        }
    }
}
