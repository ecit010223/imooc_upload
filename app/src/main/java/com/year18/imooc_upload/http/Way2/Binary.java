package com.year18.imooc_upload.http.Way2;

import java.io.IOException;
import java.io.OutputStream;

/**
 * 作者：张玉辉 on 2018/6/5 18:08.
 */
public interface Binary {

    String getFileName();
    
    String getMimeType();

    long getBinaryLength();

    void onWriteBinary(OutputStream outputStream) throws IOException;
}
