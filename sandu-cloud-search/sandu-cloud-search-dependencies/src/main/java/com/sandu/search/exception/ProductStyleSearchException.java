package com.sandu.search.exception;

/**
 * 产品风格搜索异常
 *
 * @date 20171215
 * @auth pengxuangang
 */
public class ProductStyleSearchException extends Exception {
    public ProductStyleSearchException() {
    }

    public ProductStyleSearchException(String message) {
        super(message);
    }

    public ProductStyleSearchException(String message, Throwable cause) {
        super(message, cause);
    }

    public ProductStyleSearchException(Throwable cause) {
        super(cause);
    }

    public ProductStyleSearchException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
