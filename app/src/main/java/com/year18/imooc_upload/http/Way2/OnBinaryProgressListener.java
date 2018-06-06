package com.year18.imooc_upload.http.Way2;

/**
 * 作者：张玉辉 on 2018/6/6 07:51.
 */
public interface OnBinaryProgressListener {
    void onProgress(int what,int progress);
    void onError(int what);
}
