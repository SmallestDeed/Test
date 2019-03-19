package com.sandu.search.config;

import com.sandu.search.common.constant.RabbitExchange;
import com.sandu.search.common.constant.RabbitQueue;
import com.sandu.search.common.constant.RabbitRoutingKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

/**
 * 消息队列配置(RabbitMq)
 *
 * @date 2018/3/24
 * @auth pengxuangang
 * @mail xuangangpeng@gmail.com
 */
@Slf4j
@Configuration
public class AmqpRabbitConfig {

    private ConnectionFactory connectionFactory;

    @Autowired
    public AmqpRabbitConfig(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    //调用模板配置
    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public RabbitTemplate rabbitTemplate() {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        return template;
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
        return new DirectExchange(RabbitExchange.SANDU_CLOUD_SEARCH);
    }

    //队列配置
    @Bean
    public Queue queue() {
        //队列持久配置true
        return new Queue(RabbitQueue.SANDU_CLOUD_SEARCH, true);

    }

    //绑定同城联盟交换器
    @Bean
    public Binding binding() {
        return BindingBuilder.bind(queue()).to(defaultExchange()).with(RabbitRoutingKey.SANDU_CLOUD_SEARCH);
    }
}
