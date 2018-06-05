package com.year18.imooc_upload.http.error;

/**
 * 作者：张玉辉 on 2018/6/5 17:43.
 */
public class URLError extends Exception {
    public URLError() {
    }

    public URLError(String message) {
        super(message);
    }

    public URLError(String message, Throwable cause) {
        super(message, cause);
    }

    public URLError(Throwable cause) {
        super(cause);
    }
}
