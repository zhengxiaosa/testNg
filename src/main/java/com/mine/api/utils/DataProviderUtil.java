package com.mine.api.utils;


import com.alibaba.fastjson.JSONException;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.apache.commons.lang3.StringUtils;
import org.skyscreamer.jsonassert.JSONAssert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.testng.Assert;

import java.io.*;
import java.net.URL;
import java.util.Map;


/**
 * @author: liwanlu
 * @date: 2021/5/8
 */
public class DataProviderUtil {
    private final static Logger LOGGER = LoggerFactory.getLogger(DataProviderUtil.class);

    private DataProviderUtil() {
    }

    public static Object[][] getTestData(String filename) {
        return getTestData(filename, null);
    }

    public static Object[][] getTestOneData(String filename) {
        return getTestOneData(filename, null);
    }

    public static Object[][] getTestDataMap(String filename) {
        return getTestDataMap(filename, null);
    }

    /**
     * 不需要修改json格式,根据comments方法名称获取case
     * @param filename
     * @param comments
     * @return
     */
    public static Object[][] getTestOneData(String filename, String comments) {
        JsonArray testArray = null;
        Object[][] objects = null;
        try {
//            String testJsonFileName=Thread.currentThread().getContextClassLoader().getResource(filename).getFile();
            URL testJsonFileUrl=new ClassPathResource(filename).getURL();
            LOGGER.info("文件路径："+testJsonFileUrl);
            String testJsonFileName = testJsonFileUrl.getFile();
            String jsonPath= decodeUrl(testJsonFileName);
            String contentString = readToString(jsonPath);

            testArray = GsonUtil.strToJsonArray(contentString);
            if (StringUtils.isNotBlank(comments)) {
                objects=new Object[1][3];
                for (int i = 0; i < testArray.size(); ++i) {
                    if (GsonUtil.strToJsonObject(testArray.get(i).toString()).get("comments").equals(comments)) {
                        objects[i][0] = GsonUtil.strToJsonObject(testArray.get(i).toString()).get("comments").toString();
                        objects[0][1] = GsonUtil.strToJsonObject(testArray.get(i).toString()).getAsJsonObject("request");
                        objects[0][2] = GsonUtil.strToJsonObject(testArray.get(i).toString()).getAsJsonObject("expResult");
                    }
                }
            } else {
                objects=new Object[testArray.size()][3];
                for (int i = 0; i < testArray.size(); ++i) {
                    objects[i][0] = GsonUtil.strToJsonObject(testArray.get(i).toString()).get("comments").toString();
                    objects[i][1] = GsonUtil.strToJsonObject(testArray.get(i).toString()).getAsJsonObject("request");
                    objects[i][2] = GsonUtil.strToJsonObject(testArray.get(i).toString()).getAsJsonObject("expResult");
                }
            }
        } catch (Exception e) {
            LOGGER.error("获取测试数据文件错误",e);
            Assert.fail("获取测试数据文件错误，文件："+filename);
        }
        return objects;
    }



    /**
     * 不需要修改json格式,根据case数方法名称获取case
     * @param filename
     * @param num
     * @return
     */
    public static Object[][] getTestOneData(String filename, int num) {

        JsonArray testArray = null;
        Object[][] objects_result =new Object[1][3];
        try {
//            String testJsonFileName=Thread.currentThread().getContextClassLoader().getResource(filename).getFile();
            URL testJsonFileUrl=new ClassPathResource(filename).getURL();
            LOGGER.info("文件路径："+testJsonFileUrl);
            String testJsonFileName = testJsonFileUrl.getFile();
            String jsonPath= decodeUrl(testJsonFileName);
            String contentString = readToString(jsonPath);
            testArray = GsonUtil.strToJsonArray(contentString);
            Object[][] objects = new Object[testArray.size()][3];

            if (num >=0 && num<=testArray.size()) {
                for (int i = 0; i < testArray.size(); ++i) {
                    objects[i][0] = GsonUtil.strToJsonObject(testArray.get(i).toString()).get("comments").toString();
                    objects[i][1] = GsonUtil.strToJsonObject(testArray.get(i).toString()).getAsJsonObject("request");
                    objects[i][2] = GsonUtil.strToJsonObject(testArray.get(i).toString()).getAsJsonObject("expResult");
                }
            } else {
                LOGGER.error(num + "不能小于0");
            }
            objects_result[0][0]= objects[num][0];
            objects_result[0][1]= objects[num][1];
            objects_result[0][2]= objects[num][2];
        } catch (Exception e) {
            LOGGER.error("获取测试数据文件错误",e);
            Assert.fail("获取测试数据文件错误，文件："+filename);
        }

        return objects_result;
    }

    public static Object[][] getTestDataMap(String filename, String methodName) {
        JsonArray testArray = null;
        try {
            URL testJsonFileUrl=new ClassPathResource(filename).getURL();
            LOGGER.info("文件路径："+testJsonFileUrl);
            String testJsonFileName = testJsonFileUrl.getFile();
            String jsonPath= decodeUrl(testJsonFileName);
            String contentString = readToString(jsonPath);
            if (methodName != null) {
                testArray = GsonUtil.strToJsonObject(contentString).getAsJsonArray(methodName);
            } else {
                testArray = GsonUtil.strToJsonArray(contentString);
            }
        } catch (Exception e) {
            LOGGER.error("获取测试数据文件错误",e);
            Assert.fail("获取测试数据文件错误，文件："+filename);
        }
        int len = testArray.size();
        Object[][] objects = new Object[len][3];
        for (int i = 0; i < len; ++i) {
            objects[i][0] = GsonUtil.strToJsonObject(testArray.get(i).toString()).get("comments").toString();
//            objects[i][1] = GsonUtil.strToJsonObject(testArray.get(i).toString()).get("request").getInnerMap();
            objects[i][2] = GsonUtil.strToJsonObject(testArray.get(i).toString()).getAsJsonObject("expResult");
        }
        return objects;
    }

    //使用json文件的每个json对象作为一条测试用例
    public static Object[][] getTestData(String filename, String methodName) {
        JsonArray testArray = null;
        try {
            URL testJsonFileUrl=new ClassPathResource(filename).getURL();
            LOGGER.info("文件路径："+testJsonFileUrl);
            String testJsonFileName = testJsonFileUrl.getFile();
            String jsonPath= decodeUrl(testJsonFileName);
            String contentString = readToString(jsonPath);
            if (methodName != null) {
                testArray = GsonUtil.strToJsonObject(contentString).getAsJsonArray(methodName);

            } else {
                testArray = GsonUtil.strToJsonArray(contentString);
            }
        } catch (Exception e) {
            LOGGER.error("获取测试数据文件错误",e);
            Assert.fail("获取测试数据文件错误，文件："+ filename);
        }
        int len = testArray.size();
        Object[][] objects = new Object[len][3];
        for (int i = 0; i < len; ++i) {
            objects[i][0] = GsonUtil.strToJsonObject(testArray.get(i).toString()).get("comments").toString();
            objects[i][1] = GsonUtil.strToJsonObject(testArray.get(i).toString()).getAsJsonObject("request");
            objects[i][2] = GsonUtil.strToJsonObject(testArray.get(i).toString()).getAsJsonObject("expResult");
        }
        return objects;
    }

    private static String readToString(String fileName) {
        String encoding = "utf-8";
        File file = new File(fileName);
        Long filelength = file.length();
        byte[] filecontent = new byte[filelength.intValue()];
        try {
            FileInputStream in = new FileInputStream(file);
            in.read(filecontent);
            in.close();
        } catch (FileNotFoundException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
        try {
            return new String(filecontent, encoding);
        } catch (UnsupportedEncodingException e) {
            LOGGER.error("The OS does not support " + encoding + "; " + e.getMessage(), e);
            return null;
        }
    }

    /**
     * 解码
     * @param path 路径
     */
    private static String decodeUrl(String path){
        String jsonPath= null;
        try {
            jsonPath = java.net.URLDecoder.decode(path,"utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return jsonPath;
    }

    public static JsonObject getApiReq(JsonObject request, String methodName){
        return getApiReq(request, methodName, null);
    }

//    public static Map<String, Object> getApiReqMap(JsonObject request, String methodName, JsonObject commonreq){
//        return getApiReq(request, methodName, commonreq).getInnerMap();
//    }

    public static JsonObject getApiReq(JsonObject request, String methodName, JsonObject commonreq){
        JsonObject apireq = (JsonObject) request.get(methodName);
        if(commonreq != null){
            for(Map.Entry<String, JsonElement> entry : commonreq.entrySet()){
                apireq.add(entry.getKey(), entry.getValue());
            }
        }
        return apireq;
    }

    public static JsonObject transferParams(JsonObject from, JsonObject to){
        for(Map.Entry<String, JsonElement> entry : from.entrySet()){
            to.add(entry.getKey(), entry.getValue());
        }
        return to;
    }

    public static Map<String, Object> transferParams(Map<String, Object> from, Map<String, Object> to){
        for(Map.Entry<String,Object> entry : from.entrySet()){
            to.put(entry.getKey(), entry.getValue());
        }
        return to;
    }

    public static JsonObject removeParams(JsonObject params, JsonObject toBeRemove){
        for(Map.Entry<String, JsonElement> entry : toBeRemove.entrySet()){
            params.remove(entry.getKey());
        }
        return params;
    }

    public static Map<String, Object> removeParams(Map<String, Object> params, Map<String, Object> toBeRemove){
        for(Map.Entry<String,Object> entry : toBeRemove.entrySet()){
            params.remove(entry.getKey());
        }
        return params;
    }



    /**
     * result只要包含expResult结果就为true
     * @param expResult
     * @param result
     * @throws JSONException
     */
    public static void  AssertJson(JsonObject expResult,String result) throws JSONException, org.json.JSONException {
        //用户中心不稳定或者还款服务不可用，不作校验
        if (result.contains("11000001")||result.contains("09040114")){
            return;
        }
        JsonObject result_data = GsonUtil.strToJsonObject(result);
        //code码不等于0,校验code,等于0 断言返回字符串
        if("0".equals(result_data.get("code"))){
            String Response =GsonUtil.beanToJsonStr(result);
            JSONAssert.assertEquals(expResult.toString(),Response, false );
        }else {
            Assert.assertEquals(result_data.get("code"),expResult.get("code"));
        }
    }

    public static void  AssertJsonThrift(JsonObject expResult,Object result) throws JSONException, org.json.JSONException {
        // JsonObject result_data = GsonUtil.jsonStrToBean(result.toString());

        String Response = new Gson().toJson(result.toString());
        JSONAssert.assertEquals(expResult.toString(),Response, false );

    }

}