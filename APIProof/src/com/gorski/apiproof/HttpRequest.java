package com.gorski.apiproof;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author JLiang
 */
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.testng.Reporter;
import org.testng.asserts.SoftAssert;

public class HttpRequest {

    private static String token;
    private static String baseUrl = 
    		"https://jsonplaceholder.typicode.com/";
    private SoftAssert sa;
    private String testEndpointURL="posts/1";

    public static String getToken() {
        return token;
    }

    public static void setToken(String toke) {
        token = toke;
    }

    // http://localhost:8080/RESTfulExample/json/product/post
    public String post(String input, String endpointUrl) throws MalformedURLException, IOException {
//    	Properties props = Tools.loadProp();
//    	baseUrl=(String) props.get("apiURL");
    	//for(Object key:props.keySet()) {
    		//System.out.println(key.toString());
    	//}
        sa = new SoftAssert();
        String fullUrl = baseUrl + endpointUrl;
 		System.out.println(fullUrl);
//      Reporter.log(fullUrl,true);
        URL url = new URL(fullUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.setRequestMethod("GET");
        //conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        /*

        OutputStream os = conn.getOutputStream();
        os.write(input.getBytes());
        os.flush();
        */

        sa.assertTrue(conn.getResponseCode() == HttpURLConnection.HTTP_OK, "For URL:" + endpointUrl + " Response code was:" + conn.getResponseCode() + " Message:" + conn.getResponseMessage());
            System.out.println( "For URL:" + fullUrl + " Response code was:" 
        + conn.getResponseCode() + " Message:" + conn.getResponseMessage());

        BufferedReader reader;
        if (conn.getResponseCode() == 200) {
            reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            Reporter.log("Output from Server .... \n", true);
        } else {
            Reporter.log("Output from ERROR STREAM ....\n", true);
            Reporter.log( "For URL:" + fullUrl + " Response code was:" 
        + conn.getResponseCode() + " Message:" + conn.getResponseMessage());
            reader = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }
        String output;
        String jsonOut = "";
        while ((output = reader.readLine()) != null) {
            Reporter.log(output, true);
            jsonOut += output;
        }
        conn.disconnect();
//        sa.assertAll();
        return jsonOut;
    }

 
    public void urlToFile(String endpointUrl, String addParam, String fileName) throws Exception {
    	Properties props = Tools.loadProp();
    	baseUrl=(String) props.get("apiURL");
        sa = new SoftAssert();
        String url = baseUrl + endpointUrl;
        Reporter.log(" attempting to save " + fileName);
        Reporter.log("Get file at:" + url, true);
        URL obj = new URL(url);
        File f = new File(fileName);
        FileUtils.copyURLToFile(obj, f);
        Reporter.log("Saved file at:" + fileName, true);
    }

    public boolean saveFile(String endpointUrl, String addParam, String fileName) throws Exception {
    	Properties props = Tools.loadProp();
    	baseUrl=(String) props.get("apiURL");
        String url = baseUrl + endpointUrl;
        
//    	URL imgURL = new URL(url.replace("http", "https"));
    	URL imgURL = new URL(url);
    	
        Reporter.log(" attempting new saveFile " + fileName);
        Reporter.log("Get file at:" + imgURL.toString(), true);

        boolean isSucceed = true;

        CloseableHttpClient httpClient = HttpClients.createDefault();

        HttpGet httpGet = new HttpGet(imgURL.toString());
//        httpGet.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/50.0.2661.11 Safari/537.36");
 //       httpGet.addHeader("Referer", "https://www.google.com");

        try {
            CloseableHttpResponse httpResponse = httpClient.execute(httpGet);
            HttpEntity imageEntity = httpResponse.getEntity();

            if (imageEntity != null) {
                FileUtils.copyInputStreamToFile(imageEntity.getContent(), new File(fileName));
            }

        } catch (IOException e) {
            isSucceed = false;
        }

        httpGet.releaseConnection();

        return isSucceed;
    }
}
