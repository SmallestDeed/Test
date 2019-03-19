package com.sandu.search.dao;

import com.sandu.search.entity.designplan.po.RecommendationPlanPo;
import com.sandu.search.entity.designplan.vo.CollectRecommendedVo;
import com.sandu.search.entity.elasticsearch.po.metadate.DesignPlanRecommendedPo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 设计方案索引数据访问层
 *
 * @date 20180531
 * @auth pengxuangang
 */
@Repository
public interface DesignPlanIndexDao {

    /**
     * 获取推荐方案数据
     *
     * @return
     */
    List<RecommendationPlanPo> queryRecommendationPlanList(@Param("start") int start, @Param("limit") int limit);

    /**
     * 获取推荐方案数据
     *
     * @return
     */
    List<RecommendationPlanPo> queryRecommendationPlanDataList(@Param("recommendationPlanIdList") List<Integer> recommendationPlanIdList, @Param("start") int start, @Param("limit") int limit);

    /**
     * 获取推荐方案列表
     *
     * @param recommendationPlanIdList 推荐方案ID列表
     * @return
     */
    //List<RecommendationPlanPo> queryRecommendationPlanListByRecommendationPlanIdList(@Param("recommendationPlanIdList") List<Integer> recommendationPlanIdList);

    /**
     * 获取推荐方案打组信息列表
     *
     * @param recommendedPlanId 推荐方案ID
     * @return
     */
    List<DesignPlanRecommendedPo> queryRecommendationPlanPoListByRecommendationPlanId(@Param("recommendedPlanId") Integer recommendedPlanId);

}
