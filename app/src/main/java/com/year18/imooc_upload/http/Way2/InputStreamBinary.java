package com.year18.imooc_upload.http.Way2;

import android.text.TextUtils;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 作者：张玉辉 on 2018/6/6 07:25.
 */
public class InputStreamBinary implements Binary{
    private String mFileName;
    private String mMimeType;
    private InputStream mInputStream;

    public InputStreamBinary(String fileName,String mimeType,InputStream inputStream) {
        if(!(inputStream instanceof ByteArrayInputStream)&&!(inputStream instanceof FileInputStream)){
            throw new IllegalArgumentException("流格式不正确");
        }else if(TextUtils.isEmpty(fileName)||TextUtils.isEmpty(mimeType)){
            throw new IllegalArgumentException("fileName or mimeType is not empty");
        }
        this.mFileName = fileName;
        this.mMimeType = mimeType;
        this.mInputStream = inputStream;

        //可以获取长度的流数据：
        // 1.字节流：ByteArrayInputStream
        // 2.文件流：FileInputStream
    }

    @Override
    public String getFileName() {
        return mFileName;
    }

    @Override
    public String getMimeType() {
        return mMimeType;
    }

    @Override
    public long getBinaryLength() {
        try {
            return mInputStream.available();
        } catch (IOException e) {
            return 0;
        }
    }

    @Override
    public void onWriteBinary(OutputStream outputStream) throws IOException {
        byte[] buffer = new byte[2048];
        int len;
        while ((len = mInputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, len);
        }
    }
}
