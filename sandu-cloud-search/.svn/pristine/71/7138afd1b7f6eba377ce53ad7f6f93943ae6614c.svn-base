package com.sandu.search.entity.elasticsearch.po.metadate;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 店铺方案持久化对象
 *
 * @date 2018/9/21
 * @auth xiaoxc
 */
@Data
public class CompanyShopPlanPo implements Serializable {

    private static final long serialVersionUID = -954561045653351536L;

    //推荐方案ID
    private Integer planId;
    //打组主键ID
    private Integer shopId;
    //发布时间
    private Date gmtCreate;
    //发布时间
    private Date gmtModified;
    //店铺发布平台
    private String shopPlatformValues;
    //店铺发布平台
    private List<Integer> releasePlatformList;
    //方案类型(1:样板方案 2:推荐方案 3:全屋方案)
    private Integer planRecommendedType;
    //删除状态
    private Integer dataIsDeleted;

}
