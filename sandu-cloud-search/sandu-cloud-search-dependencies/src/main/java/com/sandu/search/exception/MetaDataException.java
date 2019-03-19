package com.sandu.search.exception;

/**
 * 元数据异常
 *
 * @date 20171213
 * @auth pengxuangang
 */
public class MetaDataException extends Exception {
    public MetaDataException() {
    }

    public MetaDataException(String message) {
        super(message);
    }

    public MetaDataException(String message, Throwable cause) {
        super(message, cause);
    }

    public MetaDataException(Throwable cause) {
        super(cause);
    }

    public MetaDataException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
