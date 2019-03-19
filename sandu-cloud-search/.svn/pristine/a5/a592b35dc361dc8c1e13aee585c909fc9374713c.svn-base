package com.sandu.search.service.product;

import com.sandu.search.entity.elasticsearch.response.SearchObjectResponse;
import com.sandu.search.exception.ProductNameSearchException;
import com.sandu.search.exception.ProductStyleSearchException;

/**
 * 产品风格搜索服务
 *
 * @date 20171215
 * @auth pengxuangang
 */
public interface ProductStyleSearchService {

    /**
     * 通过产品风格查询产品列表
     *
     * @param styleName 产品风格名
     * @param dataSize  数据大小
     * @return
     * @throws ProductNameSearchException
     */
    SearchObjectResponse queryProductListByProductStyleName(String styleName, int dataSize) throws ProductStyleSearchException;
}
