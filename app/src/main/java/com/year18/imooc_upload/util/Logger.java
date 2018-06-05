package com.year18.imooc_upload.util;

import android.util.Log;

/**
 * 作者：张玉辉 on 2018/5/1 10:10.
 */
public class Logger {
    public static final String TAG = "tag";
    public static final boolean debug=true;

    public static String getMsg(Object o){
        return o==null?"null":o.toString();
    }

    public static void i(Object msg){
        if(debug){
            Log.i(TAG,getMsg(msg));
        }
    }

    public static void d(Object msg){
        if(debug){
            Log.d(TAG,getMsg(msg));
        }
    }

    /** 打印警告信息 **/
    public static void w(Object msg){
        if(debug){
            Log.w(TAG,getMsg(msg));
        }
    }

    /** 打印错误信息 **/
    public static void e(Object msg){
        if(debug){
            Log.e(TAG,getMsg(msg));
        }
    }
}
