package com.sandu.search.service.index;

import com.sandu.search.entity.designplan.po.RecommendationPlanPo;
import com.sandu.search.entity.designplan.po.TopDesignPlanRecommendPO;
import com.sandu.search.entity.elasticsearch.po.metadate.DesignPlanRecommendedPo;
import com.sandu.search.exception.DesignPlanIndexException;

import java.util.List;

/**
 * 分类产品索引服务
 *
 * @date 20180531
 * @auth pengxuangang
 */
public interface DesignPlanIndexService {

    /**
     * 获取推荐方案数据
     *
     * @return
     */
    List<RecommendationPlanPo> queryRecommendationPlanList(int start, int limit) throws DesignPlanIndexException;

    /**
     * 获取推荐方案数据
     *
     * @return
     */
    List<RecommendationPlanPo> queryRecommendationPlanDataList(List<Integer> recommendationPlanIdList, int start, int limit) throws DesignPlanIndexException;

    /**
     * 获取推荐方案列表
     *
     * @param recommendationPlanIdList 推荐方案ID列表
     * @return
     */
    //List<RecommendationPlanPo> queryRecommendationPlanListByRecommendationPlanIdList(List<Integer> recommendationPlanIdList) throws DesignPlanIndexException;

    /**
     * 获取推荐方案打组信息列表
     *
     * @param recommendedPlanId 推荐方案ID
     * @return
     */
    List<DesignPlanRecommendedPo> queryRecommendationPlanPoListByRecommendationPlanId(Integer recommendedPlanId) throws DesignPlanIndexException;

    List<TopDesignPlanRecommendPO> getTopDesignPlanRecommendList();
}
