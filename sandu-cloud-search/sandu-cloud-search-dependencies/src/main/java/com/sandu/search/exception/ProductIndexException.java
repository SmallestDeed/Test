package com.sandu.search.exception;

/**
 * 分类产品索引异常
 *
 * @date 20171213
 * @auth pengxuangang
 */
public class ProductIndexException extends Exception {
    public ProductIndexException() {
    }

    public ProductIndexException(String message) {
        super(message);
    }

    public ProductIndexException(String message, Throwable cause) {
        super(message, cause);
    }

    public ProductIndexException(Throwable cause) {
        super(cause);
    }

    public ProductIndexException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
