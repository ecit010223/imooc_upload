package com.year18.imooc_upload.http.Way2;

/**
 * 作者：张玉辉 on 2018/6/6 08:05.
 */
public class ProgressMessage implements Runnable {
    public static final int CMD_ERROR = 1;
    public static final int CMD_PROGRESS = 2;
    private int mWhat;
    private int mCmd;
    private int mProgress;
    private OnBinaryProgressListener mProgressListener;

    public ProgressMessage(int what,int cmd, int progress, OnBinaryProgressListener progressListener) {
        this.mWhat = what;
        this.mCmd = cmd;
        this.mProgress = progress;
        this.mProgressListener = progressListener;
    }

    public ProgressMessage(int what,int cmd, OnBinaryProgressListener progressListener) {
        this.mWhat = what;
        this.mCmd = cmd;
        this.mProgressListener = progressListener;
    }

    @Override
    public void run() {
        //将在主线程中执行
        switch (mCmd){
            case CMD_ERROR:
                if(mProgressListener!=null) {
                    mProgressListener.onError(mWhat);
                }
                break;
            case CMD_PROGRESS:
                if(mProgressListener !=null) {
                    mProgressListener.onProgress(mWhat,mProgress);
                }
                break;
        }
    }
}
