package com.sandu.search.service.design.impl;

import com.sandu.search.dao.DesignPlanIndexDao;
import com.sandu.search.entity.designplan.po.RecommendationPlanPo;
import com.sandu.search.service.design.DesignPlanIndexRecommendedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 设计方案 数据访问层
 *
 * @date 20180917
 * @auth chenqiang
 */
@Service("designPlanIndexRecommendedService")
public class DesignPlanIndexRecommendedServiceImpl implements DesignPlanIndexRecommendedService {

    @Autowired
    private DesignPlanIndexDao designPlanIndexDao;

    /**
     * 获取推荐方案列表
     *
     * @param recommendationPlanIdList 推荐方案ID列表
     * @return
     */
    @Override
    public List<Map<Integer,String>> queryRecommendationPlanMapByIdList(List<Integer> recommendationPlanIdList){
        List<Map<Integer,String>> listMap = new ArrayList<>(2);
        Map<Integer,String> chargeTypeMap = new HashMap<>(recommendationPlanIdList.size());
        Map<Integer,String> planPriceMap = new HashMap<>(recommendationPlanIdList.size());


        List<RecommendationPlanPo> recommendationPlanPos = designPlanIndexDao.queryRecommendationPlanListByRecommendationPlanIdList(recommendationPlanIdList);
        if(null != recommendationPlanPos && recommendationPlanPos.size() > 0){
            for (RecommendationPlanPo recommendationPlanPo : recommendationPlanPos) {
                chargeTypeMap.put(recommendationPlanPo.getId(),recommendationPlanPo.getChargeType() + "");
                planPriceMap.put(recommendationPlanPo.getId(),recommendationPlanPo.getPlanPrice() + "");
            }
        }

        listMap.add(chargeTypeMap);
        listMap.add(planPriceMap);
        return listMap;
    }
}
