package com.sandu.search.service.product;

import com.sandu.search.entity.elasticsearch.index.ProductIndexMappingData;
import com.sandu.search.entity.elasticsearch.response.SearchObjectResponse;
import com.sandu.search.exception.ProductManyConditionSearchException;

/**
 * 产品多条件搜索服务
 *
 * @date 20171219
 * @auth pengxuangang
 */
public interface ProductManyConditionSearchService {

    /**
     * 根据条件搜索产品列表
     *
     * @param productIndexMappingData 条件对象
     * @param dataSize                   数据大小
     * @return
     * @throws ProductManyConditionSearchException
     */
    SearchObjectResponse queryProductListByCondition(ProductIndexMappingData productIndexMappingData, int dataSize) throws ProductManyConditionSearchException;
}
