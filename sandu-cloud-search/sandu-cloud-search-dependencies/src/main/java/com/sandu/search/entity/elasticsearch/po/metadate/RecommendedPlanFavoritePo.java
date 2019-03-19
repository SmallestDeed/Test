package com.sandu.search.entity.elasticsearch.po.metadate;

import lombok.Data;

import java.io.Serializable;

/**
 * 推荐方案收藏数据持久化对象
 *
 * @date 20180926
 * @auth xiaoxc
 */
@Data
public class RecommendedPlanFavoritePo implements Serializable {

    private static final long serialVersionUID = 5956433006228195979L;

    //收藏数
    private int collectCount;
    //点赞数
    private int likeCount;
    //浏览数
    private int viewCount;
    //单空间方案ID
    private int planRecommendedId;
    //全屋方案ID
    private int fullHouseId;
    //方案类型（1：全屋方案，0：推荐方案）
    private int planHouseType;

    //（0：未收藏，1：已收藏
    private int isFavorite;

    //（0：未点赞，1：已点赞
    private int isLike;
    //收藏标识
    private String bid;

}
