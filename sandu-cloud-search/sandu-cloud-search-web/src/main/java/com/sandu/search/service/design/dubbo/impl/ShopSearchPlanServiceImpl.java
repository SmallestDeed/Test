package com.sandu.search.service.design.dubbo.impl;

import com.sandu.search.common.constant.QueryConditionField;
import com.sandu.search.entity.designplan.po.ShopPlanInfoPo;
import com.sandu.search.entity.elasticsearch.constant.IndexConstant;
import com.sandu.search.entity.elasticsearch.index.RecommendationPlanIndexMappingData;
import com.sandu.search.entity.response.ElasticSearchResponse;
import com.sandu.search.exception.ElasticSearchException;
import com.sandu.search.service.design.dubbo.ShopSearchPlanService;
import com.sandu.search.service.elasticsearch.ElasticSearchService;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortMode;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @desc 店铺方案信息服务
 *
 * @auth xiaoxc
 * @data 2018/10/30
 */
@Service("shopSearchPlanService")
@Slf4j
public class ShopSearchPlanServiceImpl implements ShopSearchPlanService {

    private final static String CLASS_LOG_PREFIX = "店铺搜索方案服务(consumer调用):";
    @Autowired
    private ElasticSearchService elasticSearchService;

    @Override
    public ShopPlanInfoPo getShopPlanInfo(Integer shopId) throws ElasticSearchException {
        if (null == shopId) {
            return null;
        }
        ShopPlanInfoPo shopPlanInfoPo = new ShopPlanInfoPo();
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        //过滤店铺
        String planShopInfoList = QueryConditionField.QUERY_CONDITION_FIELD_RECOMMENDATION_PLANA_SHOP_INFO_LIST;
        BoolQueryBuilder nestedBool = QueryBuilders.boolQuery()
                .must(QueryBuilders.matchQuery(planShopInfoList + "." + QueryConditionField.QUERY_CONDITION_FIELD_RECOMMENDATION_PLANA_SHOP_ID, shopId));
        QueryBuilder queryBuilder = QueryBuilders.nestedQuery(planShopInfoList,
                nestedBool,
                ScoreMode.Max);
        boolQueryBuilder.must(queryBuilder);
        //数据未被删除
        boolQueryBuilder.must(QueryBuilders.termQuery(QueryConditionField.QUERY_CONDITION_FIELD_RECOMMENDATION_PLAN_STATUS, 0));
        //elasticSearchService.search(sourceBuilder, IndexConstant.RECOMMENDATION_PLAN_ALIASES);
        //根据方案类型、店铺方案发布时间、Id排序 一键方案>样板房方案
        sourceBuilder.sort(SortBuilders.fieldSort(QueryConditionField.QUERY_CONDITION_FIELD_RECOMMENDATION_PLAN_RECOMMENDATIONPLAN_TYPE).order(SortOrder.DESC));
        sourceBuilder.sort(SortBuilders.fieldSort(planShopInfoList + "." + QueryConditionField.QUERY_CONDITION_FIELD_SHOP_RELEASE_TIME).order(SortOrder.DESC).sortMode(SortMode.MAX).setNestedPath(planShopInfoList).setNestedFilter(nestedBool));
        sourceBuilder.sort(SortBuilders.fieldSort(QueryConditionField.QUERY_CONDITION_FIELD_RECOMMENDATION_PLAN_ID).order(SortOrder.DESC));
        //查询三条
        sourceBuilder.from(0);
        sourceBuilder.size(3);
        sourceBuilder.postFilter(boolQueryBuilder);

        //执行查询获取数据
        ElasticSearchResponse searchResponse = elasticSearchService.searchData(sourceBuilder, IndexConstant.RECOMMENDATION_PLAN_ALIASES);
        if (null != searchResponse && null != searchResponse.getObj()) {
            List<RecommendationPlanIndexMappingData> list = (List<RecommendationPlanIndexMappingData>) searchResponse.getObj();
            List<ShopPlanInfoPo.PlanPicInfo> planPicInfos = new ArrayList<>(3);
            for (RecommendationPlanIndexMappingData planIndex : list) {
                ShopPlanInfoPo.PlanPicInfo planPicInfo = new ShopPlanInfoPo.PlanPicInfo();
                //全屋方案
                if (null == planIndex.getId()) {
                    planPicInfo.setPlanId(planIndex.getFullHouseId());
                }
                //推荐方案
                if (null == planIndex.getFullHouseId()) {
                    planPicInfo.setPlanId(planIndex.getId());
                }
                planPicInfo.setPicPath(planIndex.getCoverPicPath());
                planPicInfo.setPlanTableType(planIndex.getPlanTableType());
                planPicInfos.add(planPicInfo);
            }
            shopPlanInfoPo.setPlanPicList(planPicInfos);
            shopPlanInfoPo.setPlanCount((int)searchResponse.getTotal());
        }

        return shopPlanInfoPo;
    }
}
