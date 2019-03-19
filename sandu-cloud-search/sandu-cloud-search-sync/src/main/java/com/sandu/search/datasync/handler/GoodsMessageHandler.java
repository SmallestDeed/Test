package com.sandu.search.datasync.handler;

import com.sandu.search.common.tools.EntityUtil;
import com.sandu.search.common.tools.JsonUtil;
import com.sandu.search.entity.elasticsearch.constant.IndexConstant;
import com.sandu.search.entity.elasticsearch.constant.TypeConstant;
import com.sandu.search.entity.elasticsearch.dto.UpdateRequestDTO;
import com.sandu.search.entity.elasticsearch.index.GoodsIndexMappingData;
import com.sandu.search.entity.elasticsearch.index.ProductIndexMappingData;
import com.sandu.search.entity.elasticsearch.po.BaseGoodsSpuPo;
import com.sandu.search.entity.elasticsearch.po.ProductPo;
import com.sandu.search.entity.elasticsearch.response.BulkStatistics;
import com.sandu.search.exception.ElasticSearchException;
import com.sandu.search.exception.GoodsIndexException;
import com.sandu.search.exception.ProductIndexException;
import com.sandu.search.initialize.GoodsIndex;
import com.sandu.search.initialize.ProductIndex;
import com.sandu.search.service.elasticsearch.ElasticSearchService;
import com.sandu.search.service.index.GoodsIndexService;
import com.sandu.search.service.index.ProductIndexService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 产品消息处理
 *
 * @date 2018/3/24
 * @auth pengxuangang
 * @mail xuangangpeng@gmail.com
 */
@Slf4j
@Component
public class GoodsMessageHandler {

    private final static String CLASS_LOG_PREFIX = "Rabbit商品消息处理器:";


    private final ElasticSearchService elasticSearchService;
    private final GoodsIndex goodsIndex;
    private final GoodsIndexService goodsIndexService;


    //待更新产品列表
    private static volatile List<Integer> waitUpdateGoodsIdList = new ArrayList<>();

    @Autowired
    public GoodsMessageHandler(ElasticSearchService elasticSearchService,GoodsIndexService goodsIndexService,GoodsIndex goodsIndex) {
        this.elasticSearchService = elasticSearchService;
        this.goodsIndexService = goodsIndexService;
        this.goodsIndex = goodsIndex;
    }


    /**
     * 更新产品数据
     *
     * @return
     */
    public boolean updateGoodsInfo() {

        //去空
        waitUpdateGoodsIdList = waitUpdateGoodsIdList.stream().filter(waitUpdateGoodsId -> null != waitUpdateGoodsId && 0 != waitUpdateGoodsId).collect(Collectors.toList());
        //去重
        waitUpdateGoodsIdList = waitUpdateGoodsIdList.stream().distinct().collect(Collectors.toList());

        if (null != waitUpdateGoodsIdList && 0 < waitUpdateGoodsIdList.size()) {

            //更新产品列表
            List<Integer> updateGoodsIdList = new ArrayList<>(waitUpdateGoodsIdList);
            //更新完成删除待更新区域产品数据
            waitUpdateGoodsIdList = new ArrayList<>();

            log.info(CLASS_LOG_PREFIX + "准备更新数据，查询产品信息--总条数:{}, ID列表:{}.", updateGoodsIdList.size(), updateGoodsIdList);
            //查询商品信息
            List<BaseGoodsSpuPo> goodsPoList;
            try {
                goodsPoList = goodsIndexService.queryGoodsPoListByProductIdList(updateGoodsIdList);
            } catch (GoodsIndexException e) {
                log.error(CLASS_LOG_PREFIX + "消费消息失败。查询产品信息失败,还原产品状态为待更新!ProductIndexException:{}.", e);
                //还原产品数据,留存下次更新
                waitUpdateGoodsIdList.addAll(updateGoodsIdList);
                return false;
            }

            //更新数据
            log.info(CLASS_LOG_PREFIX + "开始更新数据，数据总条数:{}.", updateGoodsIdList.size());
            int updateGoodsSuccessCount = goodsIndex.indexGoodsDataByGoodsIdList(goodsPoList);
            log.info(CLASS_LOG_PREFIX + "更新数据完成，成功{}条,失败{}条.", updateGoodsSuccessCount, goodsPoList.size() - updateGoodsSuccessCount);
        }

        return true;
    }


    /**
     * 新增/更新商品消息处理
     *
     * @param goodsIndexMappingDataList
     * @return
     */
    public boolean addAndUpdate(List<GoodsIndexMappingData> goodsIndexMappingDataList) {

        if (null == goodsIndexMappingDataList || 0 >= goodsIndexMappingDataList.size()) {
            log.info(CLASS_LOG_PREFIX + "消费消息失败，消息对象为空。");
            return false;
        }

        //根据产品ID更新
        List<Integer> goodsIdList = new ArrayList<>(goodsIndexMappingDataList.size());
        goodsIndexMappingDataList.forEach(goodsIndexMappingData -> {
                goodsIdList.add(goodsIndexMappingData.getId());
        });

        //ID加入待更新产品列表
        if (null != goodsIdList && 0 < goodsIdList.size()) {
            waitUpdateGoodsIdList.addAll(goodsIdList);
            this.updateGoodsInfo();
            log.info(CLASS_LOG_PREFIX + "消费消息成功，商品已加入待更新产品列表!goodsIdList:{},现有待更新产品数据条数:{}.", JsonUtil.toJson(goodsIdList), waitUpdateGoodsIdList.size());
        }


        return true;
    }
}
