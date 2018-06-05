package com.year18.imooc_upload.http;

/**
 * 作者：张玉辉 on 2018/5/1 15:43.
 */
public interface HttpListener<T> {
    void onSuccessed(Response<T> response);
    void onFailed(Exception e);
}
