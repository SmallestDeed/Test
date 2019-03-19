package com.sandu.search.entity.amqp;

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

    //操作-新增
    public static final int ADD_ACTION = 1;
    //操作-删除
    public static final int DELETE_ACTION = 2;
    //操作-更新
    public static final int UPDATE_ACTION = 3;

    //模块-产品
    public static final int PRODUCT_MODULE = 1;
    //模块-设计方案
    public static final int DESIGNPLAN_MODULE = 2;
    //模块-户型
    public static final int HOUSE_MODULE = 3;
    //模块-推荐方案
    public static final int RECOMMENDATION_MODULE = 4;
    //模块-推荐方案
    public static final int GOODS_MODULE = 5;
    //模块-店铺方案
    public static final int SHOPPLAN_MODULE = 6;


    //操作
    private int action;
    //模块
    private int module;
    //传输对象
    private Object object;
    //平台类型(Excmple: com.sandu.search.common.constant.PlatformConstant)
    private String platformType;
    //消息唯一标志(UUID)
    private String messageId;
}
