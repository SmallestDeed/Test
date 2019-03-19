package com.sandu.search.entity.designplan.po;

import lombok.Data;

import java.io.Serializable;

/**
 * 推荐方案产品持久化对象
 *
 * @date 20180926
 * @auth xiaoxc
 */
@Data
public class RecommendedPlanProductPo implements Serializable {

    private static final long serialVersionUID = 3353754798787284570L;
    //推荐方案ID
    private int recommendedPlanId;
    //初始化产品ID
    private int productId;

}
