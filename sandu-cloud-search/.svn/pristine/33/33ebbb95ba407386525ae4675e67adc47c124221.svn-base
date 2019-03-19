package com.sandu.search.dao;

import com.sandu.search.entity.designplan.po.RecommendationPlanPo;
import com.sandu.search.entity.designplan.po.TopDesignPlanRecommendPO;
import com.sandu.search.entity.designplan.vo.CollectRecommendedVo;
import com.sandu.search.entity.designplan.vo.RecommendationPlanSearchVo;
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
    List<RecommendationPlanPo> queryRecommendationPlanList();

    /**
     * 获取推荐方案列表
     *
     * @param recommendationPlanIdList 推荐方案ID列表
     * @return
     */
    List<RecommendationPlanPo> queryRecommendationPlanListByRecommendationPlanIdList(@Param("recommendationPlanIdList") List<Integer> recommendationPlanIdList);

    /**
     * 获取收藏推荐方案列表
     * @param collectRecommendedVo
     * @return
     */
    List<Integer> queryCollectRecommendedPlanList(RecommendationPlanSearchVo collectRecommendedVo);

    /**
     * 获取收藏全屋方案列表
     * @param collectRecommendedVo
     * @return
     */
    List<Integer> queryCollectFullHousePlanList(RecommendationPlanSearchVo collectRecommendedVo);


    int getFavoriteCount(RecommendationPlanSearchVo collectRecommendedVo);

    List<String> selectAllSuperiorPlanIds();


    List<TopDesignPlanRecommendPO> getTopDesignPlanRecommendPOList();

}
