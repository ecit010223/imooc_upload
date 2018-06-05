package com.year18.imooc_upload.http;

/**
 * 作者：张玉辉 on 2018/5/1 15:15.
 * 使用枚举的原因是，限制用户能使用的请求方式
 */
public enum RequestMethod {
    GET("GET"),
    POST("POST"),
    HEAD("HEAD"),
    DELETE("DELETE");

    private String value;

    RequestMethod(String value){
        this.value = value;
    }

    /** 该方法的意思与toString()一样，方便不同用户的使用习惯 **/
    public String value() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }

    /** 是否可以获取OutputStream **/
    public boolean isOutputMethod(){
        switch (this){
            case POST:
            case DELETE:
                return true;
            default:
                return false;
        }
    }
}
