package com.sandu.search.service.index;

import com.sandu.search.entity.designplan.po.RecommendationPlanPo;
import com.sandu.search.entity.fullhouse.FullHouseDesignPlanPo;
import com.sandu.search.exception.FullHouseDesignPlanException;

import java.util.List;

/**
 * 全屋方案索引服务
 *
 * @date 2019/09/13
 * @auth zhangchengda
 */
public interface FullHouseDesignPlanIndexService {
    List<FullHouseDesignPlanPo> queryFullHouseDesignPlanList(int start, int limit) throws FullHouseDesignPlanException;

    /**
     * 获取一键全屋方案数据
     * @param start
     * @param limit
     * @return
     */
    List<RecommendationPlanPo> queryFullHousePlanList(int start, int limit) throws FullHouseDesignPlanException;

    /**
     * 根据Ids获取一键全屋方案数据
     * @return
     */
    List<RecommendationPlanPo> queryFullHousePlanListByIds(List<Integer> idList) throws FullHouseDesignPlanException;
}
