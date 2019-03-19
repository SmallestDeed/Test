package com.sandu.search.config;

import com.sandu.search.datasync.handler.RabbitMqHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.MessageListenerContainer;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * 消息队列配置(RabbitMq)
 *
 * @date 2018/3/24
 * @auth pengxuangang
 * @mail xuangangpeng@gmail.com
 */
@Slf4j
@Configuration
public class AmqpConfig {

    //交换器
    public static final String EXCHANGE = "sync.exchange";
    //路由KEY
    public static final String ROUTINGKEY = "sync.route";
    //队列
    public static final String QUEUE = "sync.queue";

    private final static String CLASS_LOG_PREFIX = "消息队列配置:";

    private final ConnectionFactory connectionFactory;
    private final RabbitMqHandler rabbitMqHandler;

    @Autowired
    public AmqpConfig(ConnectionFactory connectionFactory, RabbitMqHandler rabbitMqHandler) {
        this.connectionFactory = connectionFactory;
        this.rabbitMqHandler = rabbitMqHandler;
    }

    //调用模板配置
    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public RabbitTemplate rabbitTemplate() {
        return new RabbitTemplate(connectionFactory);
    }

    /**
     * 针对消费者配置
     * FanoutExchange: 将消息分发到所有的绑定队列，无routingkey的概念
     * HeadersExchange ：通过添加属性key-value匹配
     * DirectExchange:按照routingkey分发到指定队列
     * TopicExchange:多关键字匹配
     */
    @Bean
    public DirectExchange defaultExchange() {
        return new DirectExchange(EXCHANGE);
    }

    //队列配置
    @Bean
    public Queue queue() {
        //队列持久配置true
        return new Queue(QUEUE, true);

    }

    //绑定配置
    @Bean
    public Binding binding() {
        return BindingBuilder.bind(queue()).to(defaultExchange()).with(AmqpConfig.ROUTINGKEY);
    }

    //消息监听配置
    @Bean
    public MessageListenerContainer messageListenerContainer(ConnectionFactory connectionFactory) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(connectionFactory);
        //队列
        container.setQueues(queue());
        //链接工厂
        container.setConnectionFactory(connectionFactory);
        //通道监听
        container.setExposeListenerChannel(true);
        //消费者数
        container.setMaxConcurrentConsumers(1);
        //消息并发数
        container.setConcurrentConsumers(1);
        //消息消费预取数为1
        container.setPrefetchCount(1);
        //确认模式为手工确认
        container.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        //自动确认
        //container.setAcknowledgeMode(AcknowledgeMode.NONE);
        //container.setChannelTransacted(false);
        //LAMBDA监听
        container.setMessageListener((ChannelAwareMessageListener) (message, channel) -> {
            byte[] bytes;
            try {
                bytes = message.getBody();
                //接收消息
                String receiveMessageStr = new String(bytes, "UTF-8");
                log.info(CLASS_LOG_PREFIX + "收到消息:{}.", receiveMessageStr);
                //处理消息
                rabbitMqHandler.consumerMessage(receiveMessageStr);
            } catch (UnsupportedEncodingException e) {
                log.info(CLASS_LOG_PREFIX + "解析消息失败.UnsupportedEncodingException:{}.", e);
            }finally {
                //确认消息
//                try {
//                    channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
//                } catch (IOException e) {
//                    log.error(CLASS_LOG_PREFIX + "确认消息失败!IOException:{}.", e);
//                }
                try {
                    if(message.getMessageProperties().getHeaders().get("error") == null) {
                        channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
                    }else {
                        log.info(CLASS_LOG_PREFIX + "消息拒绝：" + new String(message.getBody()));
                        //channel.basicNack(message.getMessageProperties().getDeliveryTag(),false,false);
                        channel.basicReject(message.getMessageProperties().getDeliveryTag(),false);
                    }
                } catch (IOException e) {
                    log.error(CLASS_LOG_PREFIX + "确认消息失败!IOException:{}.", e);
                }
            }
        });
        return container;
    }
}
