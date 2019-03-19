package com.sandu.search.config;



import com.sandu.collect.KafkaProducer;
import com.sandu.search.aop.CollectUriAspect;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile({"local", "sit", "online"})
public class CollectConfig
{

    @Value("${spring.kafka.producer.topic}")
    private String topic ;

    @Value("${spring.kafka.producer.bootstrap-servers}")
    private String bootstrapServers;

    @Bean(name={"kafkaProducer"}, initMethod="init", destroyMethod="close")
    public KafkaProducer kafkaProducer()
    {
        KafkaProducer producer = new KafkaProducer();
        producer.setBootstrapServers(this.bootstrapServers);
        producer.setTopic(this.topic);
        return producer;
    }

    @Bean
    public CollectUriAspect collectUriAspect(KafkaProducer kafkaProducer) {
        CollectUriAspect collectUriAspect = new CollectUriAspect();
        collectUriAspect.setKafkaProducer(kafkaProducer);
        return collectUriAspect;
    }
}
