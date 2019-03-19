package com.sandu.search.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StringUtils;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * 属性资源文件配置
 *
 * @date 2018/5/7
 * @auth pengxuangang
 * @mail xuangangpeng@gmail.com
 */
@Slf4j
@Configuration
public class PropertySourceConfig {

    private final static String CLASS_LOG_PREFIX = "属性资源文件配置:";

    //当前激活文件
    private static String active;

    @Bean
    public PropertySourcesPlaceholderConfigurer properties() {

        //根据机器IP判断应加载哪个环境配置
        InetAddress localHost;
        try {
            localHost = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            log.error(CLASS_LOG_PREFIX + "获取机器IP失败,UnknownHostException:{}.", e);
            return null;
        }

        //当前IP
        String host = localHost.getHostAddress();
        if (StringUtils.isEmpty(host)) {
            log.error(CLASS_LOG_PREFIX + "获取机器IP失败,机器IP为空.");
            return null;
        }

        //检查IP是否正确
        String[] hostSplit = host.split("\\.");
        if (4 != hostSplit.length) {
            log.error(CLASS_LOG_PREFIX + "解析机器IP失败，当前IP:{}.", host);
        }

        //当前环境类型
        int envType = Integer.parseInt(hostSplit[2]);
        //正式环境
        if (20 == envType) {
            active = "online";
        //集成环境
        } else if (30 == envType) {
            active = "sit";
        //开发-本地环境(其他环境)
        } else {
            active = "local";
        }

        PropertySourcesPlaceholderConfigurer configurer = new PropertySourcesPlaceholderConfigurer();
        YamlPropertiesFactoryBean yaml = new YamlPropertiesFactoryBean();
        //File引入
        //yaml.setResources(new FileSystemResource("config.yml"));
        log.info(CLASS_LOG_PREFIX + "当前IP:{},激活配置:{}.", host, active);
        //class引入
        yaml.setResources(new ClassPathResource("searchsync-application-" + active + ".yml"));
        configurer.setProperties(yaml.getObject());
        return configurer;
    }
}
