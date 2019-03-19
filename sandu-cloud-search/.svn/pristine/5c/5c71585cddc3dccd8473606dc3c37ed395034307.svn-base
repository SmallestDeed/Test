package com.sandu.search.service.index;/**
 * @ Author     ：weisheng.
 * @ Date       ：Created in PM 2:50 2018/8/1 0001
 * @ Description：${description}
 * @ Modified By：
 * @Version: $version$
 */

import com.sandu.search.entity.elasticsearch.po.BaseGoodsSpuPo;
import com.sandu.search.entity.elasticsearch.po.ProductPo;
import com.sandu.search.exception.GoodsIndexException;
import com.sandu.search.exception.ProductIndexException;

import java.util.List;

/**
 * @Title: 商品列表信息索引服务
 * @Package
 * @Description:
 * @author weisheng
 * @date 2018/8/1 0001PM 2:50
 */
public interface GoodsIndexService {
    /**
     * 获取商品数据
     *
     * @param start 起始数
     * @param limit 最大数
     * @return
     */
    List<BaseGoodsSpuPo> queryGoodsPoList(int start, int limit) throws GoodsIndexException;



    /*
    通过商品ID查询商品列表
     */
    List<BaseGoodsSpuPo> queryGoodsPoListByProductIdList(List<Integer> goodsIdList) throws GoodsIndexException;
}
