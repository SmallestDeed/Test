package com.sandu.search.exception;

/**
 * 推荐方案搜索异常
 *
 * @date 2018/6/1
 * @auth pengxuangang
 * @mail xuangangpeng@gmail.com
 */
public class RecommendationPlanSearchException extends Exception {
    public RecommendationPlanSearchException() {
    }

    public RecommendationPlanSearchException(String message) {
        super(message);
    }

    public RecommendationPlanSearchException(String message, Throwable cause) {
        super(message, cause);
    }

    public RecommendationPlanSearchException(Throwable cause) {
        super(cause);
    }

    public RecommendationPlanSearchException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
