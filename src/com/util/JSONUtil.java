package com.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * Created by forvoid on 4/18/2017.
 */
public class JSONUtil {
    public static JSONObject getJSONObject(int code, String message){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code",code);
        jsonObject.put("message",message);
        return jsonObject;
    }
    public static JSONObject getJSONObject(int code, String message,Object date){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code",code);
        jsonObject.put("message",message);
        jsonObject.put("data",date);
        return jsonObject;
    }
    public static JSONObject getJSONObject(int code, String message,Object date,int size){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code",code);
        jsonObject.put("message",message);
        jsonObject.put("data",date);
        jsonObject.put("size",size);
        return jsonObject;
    }
    public static JSONObject getSocketJSONObject(int code, String message,Object date,String option){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code",code);
        jsonObject.put("message",message);
        jsonObject.put("data",date);

        jsonObject.put("option",option);
        return jsonObject;
    }
    public static JSONObject getSocketJSONObject(int code, String message,String to,String from,String option){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code",code);
        jsonObject.put("to",to);
        jsonObject.put("from",from);
        jsonObject.put("message",message);
        jsonObject.put("option",option);
        return jsonObject;
    }
    public static JSONObject getSocketJSONObject(int code, String message,String to,String from,Object data,String option){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code",code);
        jsonObject.put("to",to);
        jsonObject.put("data",data);
        jsonObject.put("from",from);
        jsonObject.put("message",message);
        jsonObject.put("option",option);
        return jsonObject;
    }
}
