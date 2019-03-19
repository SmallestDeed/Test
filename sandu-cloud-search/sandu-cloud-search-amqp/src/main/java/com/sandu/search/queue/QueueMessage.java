package com.sandu.search.queue;

import lombok.Data;

import java.io.Serializable;

/**
 * 队列消息
 *
 * @date 2018/3/24
 * @auth pengxuangang
 * @mail xuangangpeng@gmail.com
 */
@Data
public class QueueMessage implements Serializable{

    private static final long serialVersionUID = -3199789111919225538L;

    public QueueMessage() {
    }

    public QueueMessage(int action, int module, Object object) {
        this.action = action;
        this.module = module;
        this.object = object;
    }

    public QueueMessage(int action, int module, Object object, String platformType) {
        this.action = action;
        this.module = module;
        this.object = object;
        this.platformType = platformType;
    }

    //操作
    private int action;
    //模块
    private int module;
    //传输对象
    private Object object;
    //平台类型
    private String platformType;
    //消息唯一标志(UUID)
    private String messageId;
}
