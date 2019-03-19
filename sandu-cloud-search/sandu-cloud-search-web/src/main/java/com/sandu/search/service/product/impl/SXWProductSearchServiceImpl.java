package com.sandu.search.service.product.impl;

import com.sandu.search.common.constant.*;
import com.sandu.search.common.tools.JsonUtil;
import com.sandu.search.common.tools.StringUtil;
import com.sandu.search.entity.common.PageVo;
import com.sandu.search.entity.elasticsearch.constant.IndexConstant;
import com.sandu.search.entity.elasticsearch.dco.MultiMatchField;
import com.sandu.search.entity.elasticsearch.response.SearchObjectResponse;
import com.sandu.search.entity.elasticsearch.search.ShouldMatchSearch;
import com.sandu.search.entity.elasticsearch.search.SortOrderObject;
import com.sandu.search.entity.elasticsearch.search.product.ProductSearchAggregationVo;
import com.sandu.search.entity.elasticsearch.search.product.ProductSearchMatchVo;
import com.sandu.search.exception.ElasticSearchException;
import com.sandu.search.exception.ProductSearchException;
import com.sandu.search.service.elasticsearch.ElasticSearchService;
import com.sandu.search.service.product.SXWProductSearchService;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.max.MaxAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.min.MinAggregationBuilder;
import org.elasticsearch.search.sort.SortMode;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * 随选网产品搜索服务
 *
 * @date 20180801
 * @auth zhangchengda
 */
@Slf4j
@Service("sXWProductSearchService")
public class SXWProductSearchServiceImpl implements SXWProductSearchService {
    private final static String CLASS_LOG_PREFIX = "随选网产品搜索服务:";

    private final ElasticSearchService elasticSearchService;

    @Autowired
    public SXWProductSearchServiceImpl(ElasticSearchService elasticSearchService) {
        this.elasticSearchService = elasticSearchService;
    }

    @Override
    public SearchObjectResponse searchProduct(ProductSearchMatchVo productSearchMatchVo,
                                              List<ProductSearchAggregationVo> productSearchAggregationVoList,
                                              PageVo pageVo) throws ProductSearchException {

        if (null == productSearchMatchVo || null == pageVo) {
            log.warn(CLASS_LOG_PREFIX + "通过条件搜索产品失败，必需参数为空.");
            throw new ProductSearchException(CLASS_LOG_PREFIX + "通过条件搜索产品失败，必需参数为空.");
        }

        /********************************* Step 1: 组装搜索条件 *************************************************/

        //匹配条件List
        List<QueryBuilder> matchQueryList = new ArrayList<>();
        //非匹配条件List
        List<QueryBuilder> noMatchQueryList = new ArrayList<>(1);
        //或匹配条件List
        List<ShouldMatchSearch> shouldMatchSearchList = new ArrayList<>();
        //聚合条件
        List<AggregationBuilder> aggregationBuilderList = new ArrayList<>(null == productSearchAggregationVoList ? 0 : productSearchAggregationVoList.size());

        //组合查询条件--产品名
        if (!StringUtils.isEmpty(productSearchMatchVo.getProductName())) {
            matchQueryList.add(QueryBuilders.matchQuery(QueryConditionField.QUERY_CONDITION_FIELD_GOODS_SPU_NAME, productSearchMatchVo.getProductName()));
        }

        // 平台bool查询
        BoolQueryBuilder putawayStateBoolQueryBuilder = QueryBuilders.boolQuery();
        // 分类bool查询
        BoolQueryBuilder categoryBoolQueryBuilder = QueryBuilders.boolQuery();

        /********************** 1.上架状态和平台过滤 **********************/
        // 添加平台过滤
        matchQueryList.add(QueryBuilders.matchQuery(
                QueryConditionField.QUERY_CONDITION_FIELD_GOODS_SKU_PO_LIST
                        + "." +
                        QueryConditionField.QUERY_CONDITION_FIELD_PRODUCT_PLATFORM_CODE_LIST,
                        productSearchMatchVo.getPlatformCode()
        ));

        // 添加公司过滤
        if( 0!= productSearchMatchVo.getCompanyId()){
            matchQueryList.add(QueryBuilders.termQuery(
                    QueryConditionField.QUERY_CONDITION_FIELD_GOODS_COMPANY_ID,
                    productSearchMatchVo.getCompanyId()
            ));
        }




        // 添加平台上架状态过滤
        putawayStateBoolQueryBuilder.must(QueryBuilders.termQuery(QueryConditionField.QUERY_CONDITION_FIELD_GOODS_SKU_PO_LIST
                        + "." +
                        QueryConditionField.QUERY_CONDITION_FIELD_PRODUCT_PLATFORM_MINI_PROGRAM_PUTAWAT_STATUS
                        + "." +
                        QueryConditionField.QUERY_CONDITION_FIELD_PRODUCT_PLATFORM_PUTAWAT_STATUS,
                1
        ));
        // 添加平台发布状态过滤
        putawayStateBoolQueryBuilder.must(QueryBuilders.termQuery(QueryConditionField.QUERY_CONDITION_FIELD_GOODS_SKU_PO_LIST
                        + "." +
                        QueryConditionField.QUERY_CONDITION_FIELD_PRODUCT_PLATFORM_MINI_PROGRAM_PUTAWAT_STATUS
                        + "." +
                        QueryConditionField.QUERY_CONDITION_FIELD_PRODUCT_PLATFORM__STATUS,
                1
        ));
        // 添加商品上架状态过滤
        putawayStateBoolQueryBuilder.must(QueryBuilders.termQuery(QueryConditionField.QUERY_CONDITION_FIELD_GOODS_IS_PUTAWAY,
                1
        ));
        // 添加基本产品发布状态过滤
        putawayStateBoolQueryBuilder.must(QueryBuilders.termQuery(QueryConditionField.QUERY_CONDITION_FIELD_GOODS_SKU_PO_LIST
                        + "." +
                        QueryConditionField.QUERY_CONDITION_FIELD_PUTAWAY_STATUS,
                3
        ));
        // 添加基本产品删除状态
        putawayStateBoolQueryBuilder.must(QueryBuilders.termQuery(QueryConditionField.QUERY_CONDITION_FIELD_GOODS_SKU_PO_LIST
                        + "." +
                        QueryConditionField.QUERY_CONDITION_FIELD_PRODUCT_IS_DELETED
                , 0
        ));
        matchQueryList.add(putawayStateBoolQueryBuilder);

        /********************** 上架状态和平台过滤 end **********************/

        /********************** 2.企业和品牌过滤 **********************/

       /* //组合查询条件--产品品牌ID列表
        List<Integer> brandIdList = productSearchMatchVo.getBrandIdList();
        if (null != brandIdList && 0 < brandIdList.size()) {
            matchQueryList.add(QueryBuilders.termsQuery(QueryConditionField.QUERY_CONDITION_FIELD_PRODUCT_BRAND_ID, brandIdList));
        }*/

        /********************** 企业和品牌过滤 end **********************/

        /********************** 3.分类 **********************/
        //组合查询条件--产品分类长编码
        List<String> productFiveCategoryLongCodeList = productSearchMatchVo.getProductFiveCategoryLongCodeList();
        //五级分类中所有条件为MUST
            if (productFiveCategoryLongCodeList != null && productFiveCategoryLongCodeList.size() > 0) {
                //组装bool查询(结构图)
                /**
                 * bool
                 *   |
                 *   |---must---terms(productCategoryLongCode)
                 *   |
                 *   |---must---terms(productCategoryLongCode)
                 *   .
                 */
                productFiveCategoryLongCodeList.forEach(productCategoryLongCode ->
                        categoryBoolQueryBuilder.must(
                                QueryBuilders.termQuery(
                                        QueryConditionField.QUERY_CONDITION_FIELD_GOODS_SKU_PO_LIST
                                                + "." +
                                                QueryConditionField.QUERY_CONDITION_FIELD_PRODUCT_CATEGORY_LONG_CODE_LIST,
                                                productCategoryLongCode)));
                matchQueryList.add(categoryBoolQueryBuilder);
            }

        //组合三级分类查询条件
        List<String> productThreeCategoryLongCodeList = productSearchMatchVo.getProductThreeCategoryLongCodeList();
        if (productThreeCategoryLongCodeList != null && productThreeCategoryLongCodeList.size() > 0) {
            productThreeCategoryLongCodeList.forEach(longCode -> {
                MatchPhrasePrefixQueryBuilder matchPhrasePrefixQueryBuilder = QueryBuilders.matchPhrasePrefixQuery(
                        QueryConditionField.QUERY_CONDITION_FIELD_GOODS_SKU_PO_LIST
                                + "." +
                                QueryConditionField.QUERY_CONDITION_FIELD_PRODUCT_CATEGORY_LONG_CODE_LIST2,
                        longCode);
                matchQueryList.add(matchPhrasePrefixQueryBuilder);
            });
        }
        /********************** 分类 end **********************/

        //组合查询条件--单值匹配多字段--List中第一个字段优先级最高
        List<MultiMatchField> multiMatchFieldList = productSearchMatchVo.getMultiMatchFieldList();
        if (null != multiMatchFieldList && 0 < multiMatchFieldList.size()) {
            //遍历条件
            multiMatchFieldList.forEach(multiMatchField -> {
                //匹配字段
                List<String> matchFieldList = multiMatchField.getMatchFieldList();

                //设置查询条件
                MultiMatchQueryBuilder multiMatchQueryBuilder = QueryBuilders.multiMatchQuery(multiMatchField.getSearchKeyword(), matchFieldList.toArray(new String[matchFieldList.size()]));
                //设置字段优先级
                for (int i = 0; i < matchFieldList.size(); i++) {
                    //字段名
                    String filedName = matchFieldList.get(i);
                    //权重
                    float boost = (matchFieldList.size() - i);
                    //产品编码由于是强匹配,防止其他分值大于产品编码.故上调为5倍权重
                    if (QueryConditionField.QUERY_CONDITION_FIELD_PRODUCT_CODE.equals(filedName)) {
                        boost = boost * 10;
                    }
                    multiMatchQueryBuilder.field(filedName, boost);
                }
                //加入查询
                matchQueryList.add(multiMatchQueryBuilder);
            });
        }
        // 排序字段
        // 如果查询的产品名称为空，根据修改时间排序，否则根据相关性排序
        List<SortOrderObject> sortOrderObjectList = null;
        if (StringUtils.isEmpty(productSearchMatchVo.getProductName())) {
            sortOrderObjectList = new ArrayList<>();
            sortOrderObjectList.add(new SortOrderObject("goodsGmtModified", SortOrder.DESC, SortMode.MAX, SortOrderObject.DEFAULT_SORT));
        }

        /********************************* Step 2: 组装聚合条件 *************************************************/
        if (null != productSearchAggregationVoList && 0 < productSearchAggregationVoList.size()) {
            productSearchAggregationVoList.forEach(productSearchAggregation -> {
                if (null != productSearchAggregation) {
                    //字段聚合
                    if (ProductSearchAggregationVo.FIELD_AGGREGATION_TYPE == productSearchAggregation.getAggregationType()) {
                        TermsAggregationBuilder termsAggregationBuilder = AggregationBuilders.terms(productSearchAggregation.getAggregationName());
                        termsAggregationBuilder.field(productSearchAggregation.getAggregationFieldName());
                        termsAggregationBuilder.size(productSearchAggregation.getAggregationSize());
                        aggregationBuilderList.add(termsAggregationBuilder);
                        //最小值聚合
                    } else if (ProductSearchAggregationVo.MIN_VALUE_AGGREGATION_TYPE == productSearchAggregation.getAggregationType()) {
                        MinAggregationBuilder minAggregationBuilder = AggregationBuilders.min(productSearchAggregation.getAggregationName());
                        minAggregationBuilder.field(productSearchAggregation.getAggregationFieldName());
                        aggregationBuilderList.add(minAggregationBuilder);
                        //最大值聚合
                    } else if (ProductSearchAggregationVo.MAX_VALUE_AGGREGATION_TYPE == productSearchAggregation.getAggregationType()) {
                        MaxAggregationBuilder maxAggregationBuilder = AggregationBuilders.max(productSearchAggregation.getAggregationName());
                        maxAggregationBuilder.field(productSearchAggregation.getAggregationFieldName());
                        aggregationBuilderList.add(maxAggregationBuilder);
                    }
                }
            });
        }

        /********************************* Step 3: 搜索数据 *************************************************/
        //搜索数据
        SearchObjectResponse searchObjectResponse;

        log.info(CLASS_LOG_PREFIX + "通过条件搜索产品-根据产品名或产品品牌搜索产品列表:ProductSearchMatchVo:{},ProductSearchAggregationVoList:{}", productSearchMatchVo.toString(), JsonUtil.toJson(productSearchAggregationVoList));
        try {
            searchObjectResponse = elasticSearchService.search(matchQueryList, noMatchQueryList, shouldMatchSearchList, aggregationBuilderList, sortOrderObjectList, pageVo.getStart(), pageVo.getPageSize(), IndexConstant.SXW_PRODUCT_INFO);
        } catch (ElasticSearchException e) {
            log.error(CLASS_LOG_PREFIX + "通过条件搜索产品-根据产品名或产品品牌搜索产品列表失败! ElasticSearchException:{}.", e);
            throw new ProductSearchException(CLASS_LOG_PREFIX + "通过条件搜索产品-根据产品名或产品品牌搜索产品列表失败! ElasticSearchException:{}.", e);
        }
        log.info(CLASS_LOG_PREFIX + "通过条件搜索产品-根据产品名或产品品牌搜索产品列表成功!SearchObjectResponse:{}.", searchObjectResponse.getHitTotal());

        if (null == searchObjectResponse.getHitResult()) {
            log.info(CLASS_LOG_PREFIX + "通过条件搜索产品-根据产品名或产品品牌搜索产品列表失败,查询结果为空。ProductSearchMatchVo:{}.", productSearchMatchVo.toString());
            throw new ProductSearchException(CLASS_LOG_PREFIX + "通过条件搜索产品-根据产品名或产品品牌搜索产品列表失败,查询结果为空。ProductSearchMatchVo:" + productSearchMatchVo.toString());
        }

        return searchObjectResponse;
    }
}
