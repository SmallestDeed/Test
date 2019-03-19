package com.sandu.search.dao;

import com.sandu.search.entity.designplan.po.RecommendationPlanPo;
import com.sandu.search.entity.fullhouse.FullHouseDesignPlanPo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FullHouseDesignPlanIndexDao {

    List<FullHouseDesignPlanPo> queryFullHouseDesignPlanList(int start, int limit);

    /**
     * 获取一键全屋方案数据
     * @param start
     * @param limit
     * @return
     */
    List<RecommendationPlanPo> queryFullHousePlanList(@Param("start") int start, @Param("limit") int limit);

    /**
     * 根据Ids获取一键全屋方案数据
     * @return
     */
    List<RecommendationPlanPo> queryFullHousePlanListByIds(@Param("idList") List<Integer> idList);
}
