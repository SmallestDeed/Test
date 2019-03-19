package com.sandu.search.common.tools;

import lombok.Data;

import java.io.Serializable;

/**
 * 状态消息工具类
 * @auth xiaoxc
 * @data 2018-08-15
 */
@Data
public class MessageUtil implements Serializable {

    private static final long serialVersionUID = 3178512985163406046L;

    /**
     * 状态码
     */
    private boolean stauts;
    /**
     * 消息
     */
    private String message;

    public MessageUtil() {}

    public MessageUtil(boolean stauts) {
        this.stauts = stauts;
    }

    public MessageUtil(boolean stauts, String message) {
        this.stauts = stauts;
        this.message = message;
    }
}
