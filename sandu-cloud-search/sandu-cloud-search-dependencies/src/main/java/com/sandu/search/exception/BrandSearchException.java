package com.sandu.search.exception;

/**
 * 品牌搜索异常
 *
 * @date 20171215
 * @auth pengxuangang
 */
public class BrandSearchException extends Exception {
    public BrandSearchException() {
    }

    public BrandSearchException(String message) {
        super(message);
    }

    public BrandSearchException(String message, Throwable cause) {
        super(message, cause);
    }

    public BrandSearchException(Throwable cause) {
        super(cause);
    }

    public BrandSearchException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
