package com.year18.imooc_upload.http.Way2;

import android.webkit.MimeTypeMap;

import com.year18.imooc_upload.http.Poster;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 作者：张玉辉 on 2018/6/5 19:02.
 */
public class FileBinary implements Binary {
    private File mFile;

    private int mWhat;
    private OnBinaryProgressListener mProgressListener;

    public FileBinary(File file) {
        if (file == null || !file.exists()) {
            throw new IllegalArgumentException("The file is not found");
        }
        this.mFile = file;
    }

    @Override
    public String getFileName() {
        return mFile.getName();
    }

    @Override
    public String getMimeType() {
        String mimeType = "application/octet-stream"; //默认当成流形式
        if (MimeTypeMap.getSingleton().hasExtension(mFile.getName())) {
            String extendsion = MimeTypeMap.getFileExtensionFromUrl(mFile.getName());
            mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extendsion);
        }
        return mimeType;
    }

    @Override
    public long getBinaryLength() {
        return mFile.length();
    }

    @Override
    public void onWriteBinary(OutputStream outputStream) throws IOException {
        long allLength = getBinaryLength();
        long hasUploadLength=0;
        InputStream inputStream = new FileInputStream(mFile);
        byte[] buffer = new byte[2048];
        int len;
        while ((len = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, len);
            hasUploadLength += len;
            int progress = (int)(hasUploadLength/allLength*100);
            Poster.getInstance().post(new ProgressMessage(mWhat,ProgressMessage.CMD_PROGRESS,
                    progress,mProgressListener));
        }
    }

    /**
     * 设置上传进度监听器
     * @param progressListener
     */
    public void setProgressListener(int what,OnBinaryProgressListener progressListener) {
        this.mWhat = what;
        this.mProgressListener = progressListener;
    }
}
