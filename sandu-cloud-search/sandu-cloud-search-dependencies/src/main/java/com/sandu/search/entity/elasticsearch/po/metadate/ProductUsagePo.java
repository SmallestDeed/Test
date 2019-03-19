package com.sandu.search.entity.elasticsearch.po.metadate;

import lombok.Data;

import java.io.Serializable;

/**
 * 产品使用统计持久化对象
 *
 * @date 20180302
 * @auth pengxuangang
 */
@Data
public class ProductUsagePo implements Serializable {

    private static final long serialVersionUID = -4630924734953946259L;

    //产品ID
    private Integer productId;
    //用户ID
    private int userId;
    //产品使用次数
    private int productUsageCount;
}
