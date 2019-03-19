package com.sandu.search.entity.elasticsearch.po.metadate;

import lombok.Data;

import java.io.Serializable;

/**
 * 产品组合关联持久化对象
 *
 * @date 20171225
 * @auth pengxuangang
 */
@Data
public class ProductGroupRelPo implements Serializable {

    private static final long serialVersionUID = 8190188099378871989L;

    //产品组合主产品
    public static final int PRODUCT_GROUP_MAIN_PRODUCT = 1;
    //产品组合非主产品
    public static final int PRODUCT_GROUP_NOT_MAIN_PRODUCT = 0;
    //产品组合关联ID
    private int id;
    //产品组合ID
    private int productGroupId;
    //产品ID
    private int productId;
    //是否是主要产品(0：不是, 1:是)
    private int productIsMain;
    //系统编码
    private String systemCode;

}
