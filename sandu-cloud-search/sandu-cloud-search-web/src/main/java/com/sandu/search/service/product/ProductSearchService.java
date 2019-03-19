package com.sandu.search.service.product;

import com.sandu.search.entity.common.PageVo;
import com.sandu.search.entity.elasticsearch.index.ProductIndexMappingData;
import com.sandu.search.entity.elasticsearch.response.SearchObjectResponse;
import com.sandu.search.entity.elasticsearch.search.product.ProductSearchAggregationVo;
import com.sandu.search.entity.elasticsearch.search.product.ProductSearchForOneKeyDTO;
import com.sandu.search.entity.elasticsearch.search.product.ProductSearchMatchVo;
import com.sandu.search.exception.ProductSearchException;

import java.util.List;

/**
 * 产品搜索服务
 *
 * @date 20180103
 * @auth pengxuangang
 */
public interface ProductSearchService {

    /**
     * 通过条件搜索产品
     *
     * @param productSearchMatchVo              产品搜索匹配对象
     * @param productSearchAggregationVoList    产品搜索聚合对象
     * @param pageVo                            分页对象
     * @return
     * @throws ProductSearchException           产品搜索异常
     */
    SearchObjectResponse searchProduct(ProductSearchMatchVo productSearchMatchVo, List<ProductSearchAggregationVo> productSearchAggregationVoList, PageVo pageVo) throws ProductSearchException;

    /**
     * 通过产品ID搜索产品
     *
     * @param productId                         产品ID
     * @return
     * @throws ProductSearchException           产品搜索异常
     */
    ProductIndexMappingData searchProductById(Integer productId) throws ProductSearchException;
    
}
