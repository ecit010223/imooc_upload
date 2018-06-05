package com.year18.imooc_upload.http.request;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.year18.imooc_upload.http.Request;
import com.year18.imooc_upload.http.RequestMethod;

/**
 * 作者：张玉辉 on 2018/6/5 17:18.
 */
public class FastJsonRequest extends Request<JSONObject> {
    public FastJsonRequest(String url) {
        this(url, RequestMethod.GET);
    }

    public FastJsonRequest(String url, RequestMethod method) {
        super(url, method);
    }

    @Override
    public JSONObject parseResponse(byte[] responseBody) throws Exception{
        String stringResult = null;
        if(responseBody!=null&&responseBody.length>0){
            stringResult = new String(responseBody);
        }
        return JSON.parseObject(stringResult);
    }
}
