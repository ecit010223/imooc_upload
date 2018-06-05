package com.year18.imooc_upload.http;

import com.year18.imooc_upload.http.Way2.Binary;

import java.io.File;

/**
 * 作者：张玉辉 on 2018/5/1 15:19.
 * 发送请求的参数
 */
public class KeyValue {
    private String key;
    private Object value;

    public KeyValue(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public KeyValue(String key, File value) {
        this.key = key;
        this.value = value;
    }

    public KeyValue(String key, Binary value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public Object getValue() {
        return value;
    }
}
