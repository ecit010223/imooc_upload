package com.year18.imooc_upload.http.error;

/**
 * 作者：张玉辉 on 2018/6/5 17:41.
 */
public class TimeoutError extends Exception {
    public TimeoutError() {
    }

    public TimeoutError(String message) {
        super(message);
    }

    public TimeoutError(String message, Throwable cause) {
        super(message, cause);
    }

    public TimeoutError(Throwable cause) {
        super(cause);
    }
}
