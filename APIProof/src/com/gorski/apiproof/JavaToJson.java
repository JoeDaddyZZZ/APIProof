package com.gorski.apiproof;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.testng.Reporter;

public class JavaToJson<T> {

    private Class<T> responseClass;
    private List<T> classList;
    private boolean isArray;

    public JavaToJson(Class<T> genericClass){
        responseClass = genericClass;
        classList = new ArrayList<T>();
    }
    
    public void setIsArray(boolean boo){
        isArray=boo;
    }
    public boolean getIsArray(){
        return isArray;
    }

    public Class<T> getResponseClass() {
        return responseClass;
    }
    
    public void setResponseClass(Class<T> genericClass){
        responseClass = genericClass;
        classList = new ArrayList<T>();
    }
    
    public List<T> getResponseList(){
        return classList;
    }


    public static void main(String[] args) {
//        JavaToJson obj = new JavaToJson(ProductUrlTypeDTO.class);
//
//        obj.getResponseList();
//		obj.run();
//        obj.testBlocks();
    }

    public ObjectMapper buildMapper() {
        ObjectMapper mapper = new ObjectMapper();
        //setDateFormat
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        mapper.setDateFormat(df);

        //setIgnorable
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper;
    }

    //serialization layer
    //maps passed in object to json then posts to the passed in endpointurl
    public Object genericPost(Object o, String baseUrl) throws JsonProcessingException {
        ObjectMapper mapper = buildMapper();

        // Convert object to JSON string
        String jsonInString;
//                                = mapper.writeValueAsString(o);
//			System.out.println(jsonInString);

        // Convert object to JSON string and pretty print
        jsonInString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(o);
        Reporter.log("Pretty printing request for baseURL:" + baseUrl, true);
        Reporter.log(jsonInString, true);

        HttpRequest http = new HttpRequest();
        String jsonOutString;
        Object outObj = null;
        //Object[] objects = null;
        try {
            jsonOutString = http.post(jsonInString, baseUrl);

            if(isArray){
                JavaType type = mapper.getTypeFactory().constructCollectionLikeType(List.class, responseClass);
                classList = mapper.readValue(jsonOutString, type);
            }
            else{
                outObj = mapper.readValue(jsonOutString, responseClass);
            }

        } catch (IOException ex) {
            Logger.getLogger(JavaToJson.class.getName()).log(Level.SEVERE, null, ex);
        }
        return outObj;
    }

}
