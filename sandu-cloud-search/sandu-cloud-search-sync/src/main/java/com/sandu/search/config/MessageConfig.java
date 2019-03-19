package com.sandu.search.config;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 返回码信息配置
 *
 * @date 20171222
 * @auth pengxuangang
 */
@Data
@Slf4j
@Component
@ConfigurationProperties(prefix = "message")
public class MessageConfig {

    private final static String CLASS_LOG_PREFIX = "初始化返回码信息配置:";
    private static Map<String, String> returnMessageMap = null;
    //返回信息Map
    private Map<String, String> messageMap = new HashMap<>();

    /**
     * 根据返回码查询返回提示语
     *
     * @param code 返回码
     * @return
     */
    public static String getMessageByCode(String code) {
        if (StringUtils.isEmpty(code) || !returnMessageMap.containsKey(code)) {
            return null;
        }
        return returnMessageMap.get(code);
    }

    @Bean
    public Map<String, String> setMessageMap() {
        if (null == returnMessageMap) {
            returnMessageMap = messageMap;
            log.info(CLASS_LOG_PREFIX + "加载返回码信息完成，返回码条数:{}!", returnMessageMap.size());
        }
        return returnMessageMap;
    }
}
