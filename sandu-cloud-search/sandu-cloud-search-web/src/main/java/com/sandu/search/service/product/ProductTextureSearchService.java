package com.sandu.search.service.product;

import com.sandu.search.entity.elasticsearch.response.SearchObjectResponse;
import com.sandu.search.exception.ProductNameSearchException;
import com.sandu.search.exception.ProductTextureSearchException;

/**
 * 产品材质搜索服务
 *
 * @date 20171215
 * @auth pengxuangang
 */
public interface ProductTextureSearchService {

    /**
     * 通过产品材质查询产品列表
     *
     * @param textureName 产品材质名
     * @param dataSize    数据大小
     * @return
     * @throws ProductNameSearchException
     */
    SearchObjectResponse queryProductListByProductTextureName(String textureName, int dataSize) throws ProductTextureSearchException;
}
