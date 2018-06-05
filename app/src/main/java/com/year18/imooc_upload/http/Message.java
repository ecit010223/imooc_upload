package com.year18.imooc_upload.http;

/**
 * 作者：张玉辉 on 2018/5/1 15:39.
 */
public class Message implements Runnable{
    private Response mResponse;
    private HttpListener mHttpListener;

    public Response getResponse() {
        return mResponse;
    }

    public Message(Response response,HttpListener httpListener) {
        this.mResponse = response;
        this.mHttpListener = httpListener;
    }

    @Override
    public void run() {
        //将在主线程中执行
        Exception exception = mResponse.getException();
        if(exception!=null){
            mHttpListener.onFailed(exception);
        }else{
            mHttpListener.onSuccessed(mResponse);
        }
    }
}
