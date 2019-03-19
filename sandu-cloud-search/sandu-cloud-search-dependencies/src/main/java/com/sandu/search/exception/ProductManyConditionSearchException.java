package com.sandu.search.exception;

/**
 * 产品多条件搜索异常
 *
 * @date 20171219
 * @auth pengxuangang
 */
public class ProductManyConditionSearchException extends Exception {
    public ProductManyConditionSearchException() {
    }

    public ProductManyConditionSearchException(String message) {
        super(message);
    }

    public ProductManyConditionSearchException(String message, Throwable cause) {
        super(message, cause);
    }

    public ProductManyConditionSearchException(Throwable cause) {
        super(cause);
    }

    public ProductManyConditionSearchException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
