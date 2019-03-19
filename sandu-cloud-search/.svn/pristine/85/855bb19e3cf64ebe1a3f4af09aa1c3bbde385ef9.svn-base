package com.sandu.search.dao;

import com.sandu.search.entity.elasticsearch.po.ProductPo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 分类产品索引数据访问层
 *
 * @date 20171212
 * @auth pengxuangang
 */
@Repository
public interface ProductIndexDao {

    /**
     * 获取产品数据
     *
     * @param start 起始数
     * @param limit 最大数
     * @return
     */
    List<ProductPo> queryProductPoList(@Param("start") int start, @Param("limit") int limit);

    /**
     * 获取产品数据列表
     *
     * @param productIdList 产品ID列表
     * @return
     */
    List<ProductPo> queryProductPoListByProductIdList(@Param("productIdList") List<Integer> productIdList);
}
