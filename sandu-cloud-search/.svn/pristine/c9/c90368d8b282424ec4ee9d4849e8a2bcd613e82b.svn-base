package com.sandu.search.service.index;

import com.sandu.search.entity.elasticsearch.po.ProductPo;
import com.sandu.search.exception.ProductIndexException;

import java.util.List;
import java.util.Map;

/**
 * 分类产品索引服务
 *
 * @date 20171212
 * @auth pengxuangang
 */
public interface ProductIndexService {

    /**
     * 获取产品数据
     *
     * @param start 起始数
     * @param limit 最大数
     * @return
     */
    List<ProductPo> queryProductPoList(int start, int limit) throws ProductIndexException;

    /**
     * 获取产品数据列表
     *
     * @param productIdList 产品ID列表
     * @return
     */
    List<ProductPo> queryProductPoListByProductIdList(List<Integer> productIdList) throws ProductIndexException;

    /**
     * 处理拆分材质信息
     * @author huangsongbo
     * @param splitTexturesInfo
     * @param type
     * @return
     */
    Map<String, Object> dealWithSplitTextureInfo(Integer baseProductId, String splitTexturesInfo, String type);
}
