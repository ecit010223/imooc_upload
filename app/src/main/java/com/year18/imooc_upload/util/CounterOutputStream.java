package com.year18.imooc_upload.util;

import android.support.annotation.NonNull;

import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.atomic.AtomicLong;


/**
 * 作者：张玉辉 on 2018/5/1 20:22.
 */
public class CounterOutputStream extends OutputStream {
    /** 线程安全的统计类型 **/
    private AtomicLong mAtomicLong = new AtomicLong();

    /**
     * @return 长度值
     */
    public Long get(){
        return mAtomicLong.get();
    }

    public void write(long length) throws IOException {
        mAtomicLong.addAndGet(length);
    }

    @Override
    public void write(@NonNull byte[] b) throws IOException {
        mAtomicLong.addAndGet(b.length);
    }

    @Override
    public void write(@NonNull byte[] b, int off, int len) throws IOException {
       mAtomicLong.addAndGet(len);
    }

    @Override
    public void flush() throws IOException {
    }

    @Override
    public void close() throws IOException {
    }

    @Override
    public void write(int b) throws IOException {
        mAtomicLong.addAndGet(1);  //无论b为什么都是一个字节的长度
    }
}
