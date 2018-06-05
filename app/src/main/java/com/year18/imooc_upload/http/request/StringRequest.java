package com.year18.imooc_upload.http.request;

import com.year18.imooc_upload.http.Request;
import com.year18.imooc_upload.http.RequestMethod;

/**
 * 作者：张玉辉 on 2018/6/5 17:12.
 */
public class StringRequest extends Request<String> {

    public StringRequest(String url) {
        //默认为GET请求方式
        this(url, RequestMethod.GET);
    }

    public StringRequest(String url, RequestMethod method) {
        super(url, method);
        //接收json类型的数据
//        setRequestHeader("Accept","application/json");
        //接收xml类型的数据
//        setRequestHeader("Accept","application/xml");
        //接收所有类型的数据
        setRequestHeader("Accept","*");
    }

    @Override
    public String parseResponse(byte[] responseBody) throws Exception {
        if(responseBody!=null&&responseBody.length>0){
            return new String(responseBody);
        }
        return " ";
    }
}
