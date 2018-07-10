package com.gorski.apiproof.TestCases;

import java.io.IOException;
import java.text.ParseException;

import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gorski.apiproof.HttpRequest;
import com.gorski.apiproof.JavaToJson;
import com.gorski.apiproof.Endpoints.posts;

public class FirstRestTest {
	
	@Test(description = "testName")
	@Parameters({ "testName", "endPoint", "testParm1" })
	public void runTest(String testName, String endPoint, String testParm1)
			throws JsonProcessingException, ParseException {
		SoftAssert softAssert = new SoftAssert();
		JavaToJson jtj = new JavaToJson(posts.class);
		System.out.println("jtj.genericPost(testParm1, endPoint");
//        posts postResults = (posts) jtj.genericPost(testParm1, endPoint);
		System.out.println("endPoint = " + endPoint+"/"+testParm1);
        posts postResults = (posts) jtj.genericPost("", endPoint+"/"+testParm1);
		/*
		HttpRequest hr = new HttpRequest();
		try {
			String jsonOut = hr.post("",endPoint+"/"+testParm1);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
        softAssert.assertAll();

	}


}
