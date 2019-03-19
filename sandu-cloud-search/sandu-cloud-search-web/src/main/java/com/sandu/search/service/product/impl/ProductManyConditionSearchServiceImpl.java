package com.sandu.search.service.product.impl;

import com.sandu.search.common.constant.IndexInfoQueryConfig;
import com.sandu.search.common.constant.QueryConditionField;
import com.sandu.search.common.tools.JsonUtil;
import com.sandu.search.entity.elasticsearch.constant.IndexConstant;
import com.sandu.search.entity.elasticsearch.index.ProductIndexMappingData;
import com.sandu.search.entity.elasticsearch.response.SearchObjectResponse;
import com.sandu.search.exception.ElasticSearchException;
import com.sandu.search.exception.ProductManyConditionSearchException;
import com.sandu.search.service.elasticsearch.ElasticSearchService;
import com.sandu.search.service.product.ProductManyConditionSearchService;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 产品多条件搜索服务
 *
 * @date 20171219
 * @auth pengxuangang
 */
@Slf4j
@Service("productManyConditionSearchService")
public class ProductManyConditionSearchServiceImpl implements ProductManyConditionSearchService {

    private final static String CLASS_LOG_PREFIX = "产品多条件搜索服务:";

    private final ElasticSearchService elasticSearchService;

    @Autowired
    public ProductManyConditionSearchServiceImpl(ElasticSearchService elasticSearchService) {
        this.elasticSearchService = elasticSearchService;
    }

    @Override
    public SearchObjectResponse queryProductListByCondition(ProductIndexMappingData productIndexMappingData, int dataSize) throws ProductManyConditionSearchException {

        //参数判空
        if (null == productIndexMappingData) {
            log.warn(CLASS_LOG_PREFIX + "根据条件搜索产品列表失败，必需参数为空. ProductIndexMappingData is null.");
            throw new ProductManyConditionSearchException(CLASS_LOG_PREFIX + "根据条件搜索产品列表失败，必需参数为空. ProductIndexMappingData is null.");
        }

        //组装搜索条件
        List<QueryBuilder> matchQueryBuilderList = new ArrayList<>();

        //检查条件参数---产品ID
        if (!StringUtils.isEmpty(productIndexMappingData.getId())) {
            matchQueryBuilderList.add(QueryBuilders.matchQuery(QueryConditionField.QUERY_CONDITION_FIELD_PRODUCT_ID, productIndexMappingData.getId()));
        }

        //检查条件参数---产品名
        if (!StringUtils.isEmpty(productIndexMappingData.getProductName())) {
            matchQueryBuilderList.add(QueryBuilders.matchQuery(QueryConditionField.QUERY_CONDITION_FIELD_PRODUCT_NAME, productIndexMappingData.getProductName()));
        }

        //检查条件参数---品牌名
        if (!StringUtils.isEmpty(productIndexMappingData.getProductBrandName())) {
            matchQueryBuilderList.add(QueryBuilders.matchQuery(QueryConditionField.QUERY_CONDITION_FIELD_PRODUCT_BRAND_NAME, productIndexMappingData.getProductBrandName()));
        }

        //检查条件参数---风格名
        if (null != productIndexMappingData.getStyleNameList() && 0 < productIndexMappingData.getStyleNameList().size()) {
            StringBuffer styleStringBuffer = new StringBuffer();
            productIndexMappingData.getStyleNameList().forEach(styleName -> styleStringBuffer.append(styleName).append(" "));
            matchQueryBuilderList.add(QueryBuilders.matchQuery(QueryConditionField.QUERY_CONDITION_FIELD_PRODUCT_STYLE_NAME, styleStringBuffer.toString()));
        }

        //检查条件参数---材质名
        if (null != productIndexMappingData.getProductMaterialNameList() && 0 < productIndexMappingData.getProductMaterialNameList().size()) {
            StringBuffer textureStringBuffer = new StringBuffer();
            productIndexMappingData.getProductMaterialNameList().forEach(textureName -> textureStringBuffer.append(textureName).append(" "));
            matchQueryBuilderList.add(QueryBuilders.matchQuery(QueryConditionField.QUERY_CONDITION_FIELD_PRODUCT_TEXTURE_NAME, textureStringBuffer.toString()));
        }

        //检查条件参数---分类名
        if (null != productIndexMappingData.getAllProductCategoryName() && 0 < productIndexMappingData.getAllProductCategoryName().size()) {
            StringBuffer categoryStringBuffer = new StringBuffer();
            productIndexMappingData.getAllProductCategoryName().forEach(categoryName -> categoryStringBuffer.append(categoryName).append(" "));
            matchQueryBuilderList.add(QueryBuilders.matchQuery(QueryConditionField.QUERY_CONDITION_FIELD_PRODUCT_CATEGORY_NAME, categoryStringBuffer.toString()));
        }

        //查询数据
        SearchObjectResponse searchObjectResponse;
        try {
            searchObjectResponse = elasticSearchService.search(matchQueryBuilderList, null, null, null, null, IndexInfoQueryConfig.DEFAULT_SEARCH_DATA_START, dataSize, IndexConstant.INDEX_PRODUCT_INFO_ALIASES);
        } catch (ElasticSearchException e) {
            log.error(CLASS_LOG_PREFIX + "根据条件搜索产品列表失败，List<QueryBuilder>:{}, ElasticSearchException:{}.", JsonUtil.toJson(matchQueryBuilderList), e);
            throw new ProductManyConditionSearchException(CLASS_LOG_PREFIX + "根据条件搜索产品列表失败，List<QueryBuilder>:" + JsonUtil.toJson(matchQueryBuilderList) + ", ElasticSearchException:" + e);
        }

        return searchObjectResponse;
    }
}
