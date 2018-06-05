package com.year18.imooc_upload.http.url_connection;

import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;

import okhttp3.OkHttpClient;
import okhttp3.internal.huc.OkHttpURLConnection;
import okhttp3.internal.huc.OkHttpsURLConnection;

/**
 * 把OkHttp转成UrlConnection
 * 作者：张玉辉 on 2018/5/1 16:52.
 */
public class UrlConnectionFactory {
    private OkHttpClient mOkHttpClient;

    private static class Holder{
        private static final UrlConnectionFactory instance = new UrlConnectionFactory();
    }

    public static UrlConnectionFactory getInstance(){
        return Holder.instance;
    }

    private UrlConnectionFactory(){
        mOkHttpClient = new OkHttpClient();
    }

    public HttpURLConnection openUrl(URL url){
        return openUrl(url,null);
    }

    public HttpURLConnection openUrl(URL url,Proxy proxy){
        String protocol = url.getProtocol();
        OkHttpClient copy = mOkHttpClient.newBuilder()
                .proxy(proxy)
                .build();
        if(protocol.equals("http")){
            return new OkHttpURLConnection(url,copy);
        }else if(protocol.equals("https")){
            return new OkHttpsURLConnection(url,copy);
        }
        throw new IllegalArgumentException("unexpected protocol:"+protocol);
    }
}
