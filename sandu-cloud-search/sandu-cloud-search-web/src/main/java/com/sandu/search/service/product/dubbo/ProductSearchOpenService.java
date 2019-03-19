package com.sandu.search.service.product.dubbo;

import java.util.List;

import com.sandu.search.entity.elasticsearch.index.ProductIndexMappingData;
import com.sandu.search.entity.elasticsearch.search.product.ProductSearchForOneKeyDTO;
import com.sandu.search.exception.ElasticSearchException;

/**
 * 对外提供服务的ProductSearchService
 * 
 * @author huangsongbo
 * @date 2018.7.2
 */
public interface ProductSearchOpenService {

    /**
     * 一键装修搜索单品接口
     * 
     * @author huangsongbo
     * @param productSearchForOneKeyDTO
     * @return
     * @throws ElasticSearchException 
     */
	List<ProductIndexMappingData> searchProduct(ProductSearchForOneKeyDTO productSearchForOneKeyDTO) throws ElasticSearchException;
	
}
