package com.year18.imooc_upload.http.error;

/**
 * 作者：张玉辉 on 2018/6/5 17:46.
 */
public class UnknownHostError extends Exception {
    public UnknownHostError() {
    }

    public UnknownHostError(String message) {
        super(message);
    }

    public UnknownHostError(String message, Throwable cause) {
        super(message, cause);
    }

    public UnknownHostError(Throwable cause) {
        super(cause);
    }
}
