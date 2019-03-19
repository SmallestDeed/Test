package com.sandu.search.datasync.amqp;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.stereotype.Component;

/**
 * Rabbit发送消息被消费后确认回调
 *
 * @date 20180104
 * @auth pengxuangang
 */
@Component
public class RabbitProducerCallBack implements RabbitTemplate.ConfirmCallback {

    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        System.out.println("Rabbit发送消息被消费后确认回调 回调id:" + correlationData);
        if (ack) {
            System.out.println("Rabbit发送消息被消费后确认回调 消息成功消费");
        } else {
            System.out.println("Rabbit发送消息被消费后确认回调 消息消费失败:" + cause);
        }
    }

}
