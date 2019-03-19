package com.sandu.search.datasync.handler;

import com.google.gson.reflect.TypeToken;
import com.sandu.search.common.constant.PlatformConstant;
import com.sandu.search.common.tools.JsonUtil;
import com.sandu.search.entity.amqp.QueueMessage;
import com.sandu.search.entity.elasticsearch.index.GoodsIndexMappingData;
import com.sandu.search.entity.elasticsearch.index.ProductIndexMappingData;
import com.sandu.search.entity.elasticsearch.index.RecommendationPlanIndexMappingData;
import com.sandu.search.entity.elasticsearch.po.metadate.CompanyShopPlanPo;
import com.sandu.search.exception.ElasticSearchException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * Rabbit消息消费处理
 *
 * @date 20180104
 * @auth pengxuangang
 */
@Slf4j
@Component
public class RabbitMqHandler {

    private final ProductMessageHandler productMessageHandler;
    private final RecommendationMessageHandler recommendationMessageHandler;
    private final GoodsMessageHandler goodsMessageHandler;

    private final static String CLASS_LOG_PREFIX = "Rabbit消息消费处理:";

    @Autowired
    public RabbitMqHandler(ProductMessageHandler productMessageHandler, RecommendationMessageHandler recommendationMessageHandler
            , GoodsMessageHandler goodsMessageHandler) {
        this.productMessageHandler = productMessageHandler;
        this.recommendationMessageHandler = recommendationMessageHandler;
        this.goodsMessageHandler = goodsMessageHandler;
    }

    public void consumerMessage(String message) {

        if (StringUtils.isEmpty(message)) {
            log.info(CLASS_LOG_PREFIX + "消息消费失败，消息为空!");
            return;
        }

        //转换消息
        QueueMessage queueMessage = JsonUtil.fromJson(message, QueueMessage.class);

        if (null == queueMessage) {
            log.info(CLASS_LOG_PREFIX + "消息消费反序列化失败. message:{}.", message);
            return;
        }

        //模块类型
        int moduleType = queueMessage.getModule();
        //平台类型
        String platformType = queueMessage.getPlatformType();
        //操作类型
        int actionType = queueMessage.getAction();
        //Don't handle the baimo product message.
        if (PlatformConstant.PLATFORM_CODE_HOUSE_DRAW.equals(platformType)) {
            return;
        }
        if (StringUtils.isEmpty(platformType) || (
                !PlatformConstant.PLATFORM_CODE_TOB_MOBILE.equals(platformType)
                        && !PlatformConstant.PLATFORM_CODE_TOB_PC.equals(platformType)
                        && !PlatformConstant.PLATFORM_CODE_TOC_SITE.equals(platformType)
                        && !PlatformConstant.PLATFORM_CODE_TOC_MOBILE.equals(platformType)
                        && !PlatformConstant.PLATFORM_CODE_SANDU_MANAGER.equals(platformType)
                        && !PlatformConstant.PLATFORM_CODE_HOUSE_DRAW.equals(platformType)
                        && !PlatformConstant.PLATFORM_CODE_MERCHANTS_MANAGER.equals(platformType)
                        && !PlatformConstant.PLATFORM_CODE_TEST.equals(platformType)
                        && !PlatformConstant.PLATFORM_CODE_U3D_PLUGIN.equals(platformType)
                        && !PlatformConstant.PLATFORM_CODE_MINI_PROGRAM.equals(platformType)
                        && !PlatformConstant.PLATFORM_CODE_SELECT_DECORATION.equals(platformType))
        ) {
            log.info(CLASS_LOG_PREFIX + "消息消费失败，平台类型错误! PlatformType:{}.", platformType);
            return;
        }

        //消息消费状态
        boolean messageConsumerStatus = false;
        //消息分发
        switch (moduleType) {
            //产品模块
            case QueueMessage.PRODUCT_MODULE:
                //操作类型
                if (QueueMessage.ADD_ACTION == actionType || QueueMessage.UPDATE_ACTION == actionType) {
                    //新增|更新
                    messageConsumerStatus = productMessageHandler.addAndUpdate(JsonUtil.fromJson(queueMessage.getObject().toString(), new TypeToken<List<ProductIndexMappingData>>() {
                    }.getType()));
                } else if (QueueMessage.DELETE_ACTION == actionType) {
                    //删除
                    try {
                        messageConsumerStatus = productMessageHandler.delete(JsonUtil.fromJson(queueMessage.getObject().toString(), new TypeToken<List<ProductIndexMappingData>>() {
                        }.getType()));
                    } catch (ElasticSearchException e) {
                        log.error(CLASS_LOG_PREFIX + "删除产品失败，object：{},e:{}", JsonUtil.toJson(queueMessage.getObject()), e);
                    }
                }
                break;
            //设计方案模块
            case QueueMessage.DESIGNPLAN_MODULE:
                break;
            //户型
            case QueueMessage.HOUSE_MODULE:
                break;
            //推荐方案
            case QueueMessage.RECOMMENDATION_MODULE:
                //操作类型
                if (QueueMessage.ADD_ACTION == actionType || QueueMessage.UPDATE_ACTION == actionType || QueueMessage.DELETE_ACTION == actionType) {
                    //新增|更新
                    messageConsumerStatus = recommendationMessageHandler.addAndUpdate(JsonUtil.fromJson(queueMessage.getObject().toString(), new TypeToken<List<RecommendationPlanIndexMappingData>>() {
                    }.getType()));
                } else if (QueueMessage.DELETE_ACTION == actionType) {
                    //删除
                    try {
                        messageConsumerStatus = recommendationMessageHandler.delete(JsonUtil.fromJson(queueMessage.getObject().toString(), new TypeToken<List<ProductIndexMappingData>>() {
                        }.getType()));
                    } catch (ElasticSearchException e) {
                        log.error(CLASS_LOG_PREFIX + "删除推荐方案失败，object：{},e:{}", JsonUtil.toJson(queueMessage.getObject()), e);
                    }
                }
                break;
            //商品模块
            case QueueMessage.GOODS_MODULE:
                String goodsStr = null;
                List goodsList = (List)queueMessage.getObject();
                if(null != goodsList && 0 < goodsList.size()){
                    if(!(goodsList instanceof GoodsIndexMappingData)){
                        goodsStr = JsonUtil.toJson(queueMessage.getObject());
                    }
                }
                //操作类型
                if (QueueMessage.ADD_ACTION == actionType || QueueMessage.UPDATE_ACTION == actionType) {
                    //新增|更新
                    messageConsumerStatus = goodsMessageHandler.addAndUpdate(JsonUtil.fromJson(null == goodsStr ? queueMessage.getObject().toString() : goodsStr, new TypeToken<List<GoodsIndexMappingData>>() {
                    }.getType()));
                }

                break;
            //店铺方案模块
            case QueueMessage.SHOPPLAN_MODULE:
                //操作类型-删除店铺
                if (QueueMessage.DELETE_ACTION == actionType || QueueMessage.UPDATE_ACTION == actionType) {
                    messageConsumerStatus = recommendationMessageHandler.updatePlanShopInfo(JsonUtil.fromJson(queueMessage.getObject().toString(), new TypeToken<List<CompanyShopPlanPo>>() {}.getType()));
                }
                break;
            default:
                log.info(CLASS_LOG_PREFIX + "消息消费失败，消息数据模块类型错误,ModuleType:{}.", moduleType);
        }

        log.info(CLASS_LOG_PREFIX + "消息消费完成，消息消费状态:{},消息UUID:{}.", messageConsumerStatus, queueMessage.getMessageId());
    }
}
