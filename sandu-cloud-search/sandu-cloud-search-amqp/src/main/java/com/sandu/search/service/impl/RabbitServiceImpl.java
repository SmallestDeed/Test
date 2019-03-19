package com.sandu.search.service.impl;

import com.sandu.search.common.constant.RabbitExchange;
import com.sandu.search.common.constant.RabbitRoutingKey;
import com.sandu.search.common.tool.JsonUtil;
import com.sandu.search.queue.QueueMessage;
import com.sandu.search.queue.obj.designplan.RecommendationPlanData;
import com.sandu.search.queue.obj.product.ProductData;
import com.sandu.search.exception.RabbitException;
import com.sandu.search.service.RabbitService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.UUID;

/**
 * Rabbit发送消息
 *
 * @date 20180104
 * @auth pengxuangang
 */
@Slf4j
@Data
@Service("rabbitService")
@ConfigurationProperties(prefix = "rabbit.message")
public class RabbitServiceImpl implements RabbitService {

    private final static String CLASS_LOG_PREFIX = "Rabbit发送消息:";

    private String platform;

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Override
    public void sendMsg(Object obj) throws RabbitException {

        if (null == obj) {
            log.error(CLASS_LOG_PREFIX + "发送失败,对象为空.");
        }

        if (!(obj instanceof QueueMessage)) {
            log.error(CLASS_LOG_PREFIX + "不支持的传输对象!obj:{}.", obj.toString());
            throw new RabbitException(CLASS_LOG_PREFIX + "不支持的传输对象!obj:" + obj.toString() + ".");
        }

        QueueMessage queueMessage = (QueueMessage) obj;


        //补全消息ID
        if (StringUtils.isEmpty(queueMessage.getMessageId())) {
            queueMessage.setMessageId(UUID.randomUUID().toString());
        }

        //校验参数
        if (0 == queueMessage.getAction()) {
            log.error(CLASS_LOG_PREFIX + "发送消息失败:校验参数失败-ActionType must not be null!");
            throw new RabbitException(CLASS_LOG_PREFIX + "发送消息失败:校验参数失败-ActionType must not be null!");
        }
        if (0 == queueMessage.getModule()) {
            log.error(CLASS_LOG_PREFIX + "发送消息失败:校验参数失败-ModuleType must not be null!");
            throw new RabbitException(CLASS_LOG_PREFIX + "发送消息失败:校验参数失败-ModuleType must not be null!");
        }
        if (StringUtils.isEmpty(queueMessage.getPlatformType())) {
            //检查是否有公共配置
            if (StringUtils.isEmpty(platform)) {
                log.error(CLASS_LOG_PREFIX + "发送消息失败:校验参数失败-PlatformType must not be null!");
                throw new RabbitException(CLASS_LOG_PREFIX + "发送消息失败:校验参数失败-PlatformType must not be null!");
            } else {
                //使用系统配置
                queueMessage.setPlatformType(platform);
            }

        }

        Object object = queueMessage.getObject();
        if (null == object) {
            log.error(CLASS_LOG_PREFIX + "发送消息失败:校验参数失败-obj must not be null!");
            throw new RabbitException(CLASS_LOG_PREFIX + "发送消息失败:校验参数失败-obj must not be null!");
        }

        //单对象转List
        if (object instanceof ProductData) {
            //产品
            queueMessage.setObject(Collections.singleton((ProductData) object));
        } else if (object instanceof RecommendationPlanData) {
            //推荐方案
            queueMessage.setObject(Collections.singleton((RecommendationPlanData) object));
        }

        rabbitTemplate.convertAndSend(RabbitExchange.SANDU_CLOUD_SEARCH, RabbitRoutingKey.SANDU_CLOUD_SEARCH, JsonUtil.toJson(queueMessage), new CorrelationData(UUID.randomUUID().toString()));
    }


}
