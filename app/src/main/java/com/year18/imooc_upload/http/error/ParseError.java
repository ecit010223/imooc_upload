package com.year18.imooc_upload.http.error;

/**
 * 作者：张玉辉 on 2018/6/5 17:32.
 */
public class ParseError extends Exception {
    public ParseError() {
    }

    public ParseError(String message) {
        super(message);
    }

    public ParseError(String message, Throwable cause) {
        super(message, cause);
    }

    public ParseError(Throwable cause) {
        super(cause);
    }

}
