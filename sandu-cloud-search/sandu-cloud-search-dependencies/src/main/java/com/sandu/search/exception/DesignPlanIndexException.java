package com.sandu.search.exception;

/**
 * 设计方案索引异常
 *
 * @date 20180531
 * @auth pengxuangang
 */
public class DesignPlanIndexException extends Exception {
    public DesignPlanIndexException() {
    }

    public DesignPlanIndexException(String message) {
        super(message);
    }

    public DesignPlanIndexException(String message, Throwable cause) {
        super(message, cause);
    }

    public DesignPlanIndexException(Throwable cause) {
        super(cause);
    }

    public DesignPlanIndexException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
