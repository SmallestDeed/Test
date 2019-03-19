package com.sandu.search.entity.elasticsearch.po.metadate;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 推荐方案小区数据持久化对象
 *
 * @date 20180709
 * @auth xiaoxc
 */
@Data
public class RecommendedPlanLivingPo implements Serializable {

    private static final long serialVersionUID = -4826686991694299721L;

    //空间ID
    private int spaceId;
    //小区名称
    private String livingName;

}
