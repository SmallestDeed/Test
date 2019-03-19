package com.sandu.search.service.product;

import com.sandu.search.entity.elasticsearch.response.SearchObjectResponse;
import com.sandu.search.exception.BrandSearchException;

/**
 * 品牌搜索服务
 *
 * @date 20171215
 * @auth pengxuangang
 */
public interface ProductBrandSearchService {

    /**
     * 通过品牌名字查询产品列表
     *
     * @param brandName 品牌名
     * @param dataSize  数据大小
     * @return
     * @throws BrandSearchException
     */
    SearchObjectResponse queryProductListByBrandName(String brandName, int dataSize) throws BrandSearchException;
}
