package com.year18.imooc_upload.http;

import java.util.List;
import java.util.Map;

/**
 * 作者：张玉辉 on 2018/5/1 15:38.
 */
public class Response<T> {
    /** 服务器响应头 **/
    private Map<String,List<String>> mResponseHeaders;
    private int responseCode;
    private T responseBody;
    private Exception exception;
    private Request mRequest;

    Response(int responseCode, Map<String,List<String>> responseHeaders,
             Exception exception, Request request) {
        this.responseCode = responseCode;
        this.mResponseHeaders = responseHeaders;
        this.exception = exception;
        mRequest = request;
    }

    public Map<String, List<String>> getResponseHeaders() {
        return mResponseHeaders;
    }

    public int getResponseCode() {
        return responseCode;
    }

//    public byte[] getResponseBody() {
//        return responseBody;
//    }

    /**
     * 设置响应
     * @param responseResult
     */
    void setResponseResult(T responseResult){
        responseBody = responseResult;
    }

    public T get() {
        return responseBody;
    }

    Exception getException() {
        return exception;
    }

    public Request getRequest() {
        return mRequest;
    }

    public void setRequest(Request request) {
        mRequest = request;
    }
}
