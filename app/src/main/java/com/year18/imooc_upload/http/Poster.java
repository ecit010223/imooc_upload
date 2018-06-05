package com.year18.imooc_upload.http;


import android.os.Handler;
import android.os.Looper;

/**
 * 作者：张玉辉 on 2018/5/1 10:15.
 */
public class Poster extends Handler {
    private static Poster instance;

    public static Poster getInstance(){
        if(instance ==null) {
            synchronized (Poster.class) {
                if (instance == null) {
                    instance = new Poster();
                }
            }
        }
        return instance;
    }
    private Poster(){
        super(Looper.getMainLooper());
    }
}
