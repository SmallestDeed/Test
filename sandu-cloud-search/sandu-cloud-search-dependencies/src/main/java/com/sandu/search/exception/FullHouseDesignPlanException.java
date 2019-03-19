package com.sandu.search.exception;

/**
 * 全屋方案异常
 *
 * @date 2018/9/13
 * @auth zhangchengda
 */
public class FullHouseDesignPlanException extends Exception  {
    public FullHouseDesignPlanException() {
    }

    public FullHouseDesignPlanException(String message) {
        super(message);
    }

    public FullHouseDesignPlanException(String message, Throwable cause) {
        super(message, cause);
    }

    public FullHouseDesignPlanException(Throwable cause) {
        super(cause);
    }

    public FullHouseDesignPlanException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
