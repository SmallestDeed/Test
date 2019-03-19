package com.sandu.search.service.product;

import com.sandu.search.entity.elasticsearch.response.SearchObjectResponse;
import com.sandu.search.exception.ProductNameSearchException;

/**
 * 产品名搜索服务
 *
 * @date 20171215
 * @auth pengxuangang
 */
public interface ProductNameSearchService {

    /**
     * 通过产品名查询产品列表
     *
     * @param productName 产品名
     * @param dataSize    数据大小
     * @return
     * @throws ProductNameSearchException
     */
    SearchObjectResponse queryProductListByProductName(String productName, int dataSize) throws ProductNameSearchException;
}
