package com.year18.imooc_upload.http;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 作者：张玉辉 on 2018/5/1 15:09.
 * 全局单例，此处用的enum
 */
public enum RequestExecutor {
    INSTANCE;
    private ExecutorService mExecutorService;

    //只有在调用INSTANCE时，才会调用该构造方法
    RequestExecutor(){
        mExecutorService = Executors.newSingleThreadExecutor();
    }

    /**  供外部访问，执行请求 **/
    public void execute(Request request,HttpListener httpListener){
        mExecutorService.execute(new RequestTask(request,httpListener));
    }

}
