package com.sandu.search.entity.designplan.convert;

import com.sandu.search.common.tools.DateTool;
import com.sandu.search.entity.designplan.po.RecommendationPlanPo;
import com.sandu.search.entity.designplan.vo.RecommendationPlanVo;
import com.sandu.search.entity.elasticsearch.index.RecommendationPlanIndexMappingData;
import com.sandu.search.entity.elasticsearch.po.metadate.DecoratePricePo;

import java.util.ArrayList;
import java.util.List;

/**
 * 转换推荐方案VO对象
 *
 * @atuh xiaoxc
 * @data 2018/8/2
 */
public class RecommendationPlanConvert {

    /**
     * 推荐方案ES结果集转换VO对象
     *
     * @param planIndexMappingData
     * @param planVo
     */
    public static void parseToPlanVoByPlanIndexMappingData(RecommendationPlanIndexMappingData planIndexMappingData, RecommendationPlanVo planVo) {
        if (null != planIndexMappingData) {
            //推荐方案
            if (null != planIndexMappingData.getId() && 0 < planIndexMappingData.getId()) {
                planVo.setPlanRecommendedId(planIndexMappingData.getId());
                planVo.setId(planIndexMappingData.getId());
            }
            //全屋方案
            if (null != planIndexMappingData.getFullHouseId() && 0 < planIndexMappingData.getFullHouseId()) {
                planVo.setPlanRecommendedId(planIndexMappingData.getFullHouseId());
                planVo.setId(planIndexMappingData.getFullHouseId());
            }
            planVo.setOrdering(planIndexMappingData.getOrdering());
            planVo.setPlanId(planIndexMappingData.getDesignPlanId());
            planVo.setPlanName(planIndexMappingData.getName());
            planVo.setRecommendedType(planIndexMappingData.getType());
            planVo.setIsRelease(planIndexMappingData.getReleaseStatus());
            planVo.setIsDefaultDecorate(planIndexMappingData.getIsSupportOneKeyDecorate());
            planVo.setReleaseTime(DateTool.getDateStrByDate(planIndexMappingData.getPublishTime()));
            planVo.setSpaceCode(planIndexMappingData.getSpaceCode());
            planVo.setSpaceFunctionId(planIndexMappingData.getSpaceFunctionId());
            planVo.setHouseTypeName(planIndexMappingData.getHouseTypeName());
            planVo.setSpaceAreas(planIndexMappingData.getSpaceAreas());
            planVo.setOrdering(planIndexMappingData.getOrdering());
            //使用空间面积
            List<String> applySpaceAreaList = planIndexMappingData.getApplySpaceAreaList();
            if (null != applySpaceAreaList && 0 < applySpaceAreaList.size()) {
                StringBuffer applySpaceAreaSB = new StringBuffer();
                applySpaceAreaList.forEach(area -> applySpaceAreaSB.append(area).append(","));
                //移除最后一个逗号
                planVo.setApplySpaceAreas(applySpaceAreaSB.substring(0, applySpaceAreaSB.length()-1).toString());
            }
            planVo.setSpaceShape(planIndexMappingData.getSpaceShape());
            planVo.setCoverPath(planIndexMappingData.getCoverPicPath());
            planVo.setResRenderPicPath(planIndexMappingData.getResRenderPicPath());
            planVo.setDesignTemplateId(planIndexMappingData.getDesignTemplateId());
            planVo.setLivingId(planIndexMappingData.getLivingId());
            planVo.setDesignStyleId(planIndexMappingData.getDesignStyleId());
            planVo.setStyleName(planIndexMappingData.getStyleName());
            planVo.setCreator(planIndexMappingData.getCreateUserName());
            planVo.setPlanSource(planIndexMappingData.getPlanSource());
            //方案类型名称
            Integer recommendationType = planIndexMappingData.getType();
            if (null != recommendationType) {
                if(RecommendationPlanPo.SHARE_RECOMMENDATION_PLAN_TYPE == recommendationType) {
                    planVo.setRecommendedTypeName(RecommendationPlanPo.RECOMMENDATION_PLAN_TYPE_COMMON);
                }else if(RecommendationPlanPo.ONEKEY_RECOMMENDATION_PLAN_TYPE == recommendationType) {
                    planVo.setRecommendedTypeName(RecommendationPlanPo.RECOMMENDATION_PLAN_TYPE_CAPACITY);
                }else {
                    planVo.setRecommendedTypeName(RecommendationPlanPo.RECOMMENDATION_PLAN_TYPE_UNKNOWN);
                }
            } else {
                planVo.setRecommendedTypeName(RecommendationPlanPo.RECOMMENDATION_PLAN_TYPE_UNKNOWN);
            }
            planVo.setGroupPrimaryId(planIndexMappingData.getGroupPrimaryId());
            planVo.setRemark(planIndexMappingData.getRemark());
            planVo.setChargeType(planIndexMappingData.getChargeType());
            planVo.setPlanPrice(planIndexMappingData.getPlanPrice());
            planVo.setFullHousePlanUUID(planIndexMappingData.getVrSourceUuid());
            planVo.setUserPicPath(planIndexMappingData.getUserPicPath());
            planVo.setPlanTableType(planIndexMappingData.getPlanTableType());
            //方案装修类型报价信息
            List<DecoratePricePo> decoratePricePoList = new ArrayList<>(4);
            if (null != planIndexMappingData.getDecorateHalfPack()) {
                decoratePricePoList.add(planIndexMappingData.getDecorateHalfPack());
            }
            if (null != planIndexMappingData.getDecorateAllPack()) {
                decoratePricePoList.add(planIndexMappingData.getDecorateAllPack());
            }
            if (null != planIndexMappingData.getDecoratePackSoft()) {
                decoratePricePoList.add(planIndexMappingData.getDecoratePackSoft());
            }
            if (null != planIndexMappingData.getDecorateWater()) {
                decoratePricePoList.add(planIndexMappingData.getDecorateWater());
            }
            planVo.setPlanDecoratePriceList(decoratePricePoList);
        }
    }

}
