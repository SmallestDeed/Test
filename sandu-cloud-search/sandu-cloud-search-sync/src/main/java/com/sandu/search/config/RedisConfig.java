package com.sandu.search.config;

import com.sandu.search.common.tools.ShardedNormalJedisPool;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;

import java.util.Collections;

/**
 * Redis缓存配置
 *
 * @date 2018/3/13
 * @auth pengxuangang
 * @mail xuangangpeng@gmail.com
 */
@Data
@Configuration
public class RedisConfig {

    @Value("${redis.uri}")
    private String uri;
    @Value("${redis.pool.maxActive}")
    private Integer maxActive;
    @Value("${redis.pool.maxIdle}")
    private Integer maxIdle;
    @Value("${redis.pool.minIdle}")
    private Integer minIdle;
    @Value("${redis.pool.maxWaitMillis}")
    private Long maxWaitMills;
    @Value("${redis.pool.maxWait}")
    private Integer maxWait;
    @Value("${redis.pool.maxTotal}")
    private Integer maxTotal;

    @Bean
    public synchronized ShardedNormalJedisPool shardedJedisPool() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();

        jedisPoolConfig.setMaxTotal(this.maxActive);
        jedisPoolConfig.setMaxIdle(this.maxIdle);
        jedisPoolConfig.setMinIdle(this.minIdle);
        jedisPoolConfig.setMaxTotal(this.maxTotal);
        jedisPoolConfig.setMaxWaitMillis(this.maxWaitMills);
        jedisPoolConfig.setTestOnBorrow(true);
        jedisPoolConfig.setTestOnReturn(true);

        return ShardedNormalJedisPool.getSingleton(jedisPoolConfig,
                Collections.singletonList(new JedisShardInfo(this.uri)));
    }
}