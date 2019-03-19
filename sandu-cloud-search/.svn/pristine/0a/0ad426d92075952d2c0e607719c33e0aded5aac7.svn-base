package com.sandu.search.dao;/**
 * @ Author     ：weisheng.
 * @ Date       ：Created in PM 3:16 2018/8/1 0001
 * @ Description：${description}
 * @ Modified By：
 * @Version: $version$
 */

import com.sandu.search.entity.elasticsearch.po.BaseGoodsSpuPo;
import com.sandu.search.entity.elasticsearch.po.ProductPo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Title: 商品列表基本信息
 * @Package
 * @Description:
 * @author weisheng
 * @date 2018/8/1 0001PM 3:16
 */
@Repository
public interface GoodsIndexDao {
    /**
     * 获取产品数据
     *
     * @param start 起始数
     * @param limit 最大数
     * @return
     */
    List<BaseGoodsSpuPo> queryGoodsPoList(@Param("start") int start, @Param("limit") int limit);

    List<BaseGoodsSpuPo> queryGoodsPoListByProductIdList(@Param("goodsIdList") List<Integer> goodsIdList);
}
