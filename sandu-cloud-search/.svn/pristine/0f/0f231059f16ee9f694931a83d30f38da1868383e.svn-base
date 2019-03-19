package com.sandu.search.service.index.impl;/**
 * @ Author     ：weisheng.
 * @ Date       ：Created in PM 2:52 2018/8/1 0001
 * @ Description：${description}
 * @ Modified By：
 * @Version: $version$
 */

import com.sandu.search.common.constant.IndexInfoQueryConfig;
import com.sandu.search.common.tools.JsonUtil;
import com.sandu.search.dao.GoodsIndexDao;
import com.sandu.search.dao.ProductIndexDao;
import com.sandu.search.entity.elasticsearch.po.BaseGoodsSpuPo;
import com.sandu.search.entity.elasticsearch.po.ProductPo;
import com.sandu.search.exception.GoodsIndexException;
import com.sandu.search.exception.ProductIndexException;
import com.sandu.search.service.index.GoodsIndexService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Title: 商品列表信息索引服务实现类
 * @Package
 * @Description:
 * @author weisheng
 * @date 2018/8/1 0001PM 2:52
 */
@Slf4j
@Service("goodsIndexService")
public class GoodsIndexServiceImpl implements GoodsIndexService{
    private final static String CLASS_LOG_PREFIX = "商品列表索引服务:";

    private final GoodsIndexDao goodsIndexDao;

    @Autowired
    public GoodsIndexServiceImpl(GoodsIndexDao goodsIndexDao) {
        this.goodsIndexDao = goodsIndexDao;
    }


    @Override
    public List<BaseGoodsSpuPo> queryGoodsPoList(int start, int limit) throws GoodsIndexException {
        //初始化数据条数
        if (0 == limit) {
            limit = IndexInfoQueryConfig.DEFAULT_QUERY_PRODUCTPOINFO_LIMIT;
        }

        //查询产品信息
        log.info(CLASS_LOG_PREFIX + "正在查询产品信息第{}-{}条.", start, (start + limit));
        List<BaseGoodsSpuPo> goodsPoList;
        try {
            goodsPoList = goodsIndexDao.queryGoodsPoList(start,limit);
        } catch (Exception e) {
            log.error(CLASS_LOG_PREFIX + "获取商品列表数据第{}-{}条失败,Exception:" + e, start, (start + limit));
            throw new GoodsIndexException(CLASS_LOG_PREFIX + "获取商品列表数据第" + start + "-" + (start + limit) + "条失败,Exception:" + e);
        }
        log.info(CLASS_LOG_PREFIX + "查询商品列表数据完成,List<BaseGoodsSpuPo>长度:{}.", goodsPoList.size());

        return goodsPoList;
    }



    @Override
    public List<BaseGoodsSpuPo> queryGoodsPoListByProductIdList(List<Integer> goodsIdList) throws GoodsIndexException {
        if (null == goodsIdList || 0 >= goodsIdList.size()) {
            return null;
        }

        //查询产品信息
        log.info(CLASS_LOG_PREFIX + "正在查询产品信息,productIdList:{}.", JsonUtil.toJson(goodsIdList));
        List<BaseGoodsSpuPo> goodsPoList;
        try {
            goodsPoList = goodsIndexDao.queryGoodsPoListByProductIdList(goodsIdList);
        } catch (Exception e) {
            log.error(CLASS_LOG_PREFIX + "获取产品信息失败,goodsIdList:{}, Exception:{}.", JsonUtil.toJson(goodsIdList), e);
            throw new GoodsIndexException(CLASS_LOG_PREFIX + "获取产品信息失败,Exception:" + e);
        }
        log.info(CLASS_LOG_PREFIX + "查询产品信息完成,List<BaseGoodsSpuPo>长度:{}.", goodsPoList.size());

        return goodsPoList;
    }


}
