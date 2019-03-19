package com.sandu.search.service.product.impl;

import com.sandu.search.common.constant.IndexInfoQueryConfig;
import com.sandu.search.common.constant.QueryConditionField;
import com.sandu.search.entity.elasticsearch.constant.IndexConstant;
import com.sandu.search.entity.elasticsearch.response.SearchObjectResponse;
import com.sandu.search.exception.ElasticSearchException;
import com.sandu.search.exception.ProductTextureSearchException;
import com.sandu.search.service.elasticsearch.ElasticSearchService;
import com.sandu.search.service.product.ProductTextureSearchService;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Collections;

/**
 * 产品材质搜索服务
 *
 * @date 20171215
 * @auth pengxuangang
 */
@Slf4j
@Service("productTextureSearchService")
public class ProductTextureSearchServiceImpl implements ProductTextureSearchService {

    private final static String CLASS_LOG_PREFIX = "产品材质搜索服务:";

    private final ElasticSearchService elasticSearchService;

    @Autowired
    public ProductTextureSearchServiceImpl(ElasticSearchService elasticSearchService) {
        this.elasticSearchService = elasticSearchService;
    }

    @Override
    public SearchObjectResponse queryProductListByProductTextureName(String textureName, int dataSize) throws ProductTextureSearchException {

        if (StringUtils.isEmpty(textureName)) {
            log.warn(CLASS_LOG_PREFIX + "通过材质名查询产品列表失败，材质名为空!");
            return null;
        }

        //构造查询体
        QueryBuilder matchQueryBuilder = QueryBuilders.matchQuery(QueryConditionField.QUERY_CONDITION_FIELD_PRODUCT_TEXTURE_NAME, textureName);

        //查询数据
        SearchObjectResponse searchObjectResponse;
        try {
            searchObjectResponse = elasticSearchService.search(Collections.singletonList(matchQueryBuilder), null, null, null, null, IndexInfoQueryConfig.DEFAULT_SEARCH_DATA_START, dataSize, IndexConstant.INDEX_PRODUCT_INFO_ALIASES);
        } catch (ElasticSearchException e) {
            log.error(CLASS_LOG_PREFIX + "通过材质名查询产品列表失败，QueryBuilder:{}, ElasticSearchException:{}.", matchQueryBuilder.toString(), e);
            throw new ProductTextureSearchException(CLASS_LOG_PREFIX + "通过材质名查询产品列表失败，QueryBuilder:" + matchQueryBuilder.toString() + ", ElasticSearchException:" + e);
        }

        return searchObjectResponse;
    }
}
