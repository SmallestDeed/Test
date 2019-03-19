package com.sandu.search.storage.system;

import com.sandu.search.common.constant.RedisConstant;
import com.sandu.search.common.tools.JsonUtil;
import com.sandu.search.entity.elasticsearch.po.metadate.SysUserPo;
import com.sandu.search.exception.MetaDataException;
import com.sandu.search.service.metadata.MetaDataService;
import com.sandu.search.service.redis.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 用户元数据存储
 *
 * @date 20180709
 * @auth xiaoxc
 */
@Slf4j
@Component
public class SysUserMetaDataStorage {

    private final RedisService redisService;
    private final MetaDataService metaDataService;

    @Autowired
    public SysUserMetaDataStorage(RedisService redisService, MetaDataService metaDataService) {
        this.redisService = redisService;
        this.metaDataService = metaDataService;
    }

    /**
     * 根据用户ID查询用户对象
     *
     * @param userId 用户ID
     * @return
     */
    public SysUserPo getUserPoByUserId(Integer userId) {
        if (null == userId || 0 > userId) {
            return null;
        }

        String userStr = redisService.getMap(RedisConstant.SYS_USER_DATA, userId + "");
        if (StringUtils.isEmpty(userStr)) {
            SysUserPo sysUserPo = null;
            try {
                sysUserPo = metaDataService.getUserById(userId);
            } catch (MetaDataException e) {
                log.error("获取用户信息失败！userId:{}, exception:{}", userId, e);
            }
            return sysUserPo;
        }

        return JsonUtil.fromJson(userStr, SysUserPo.class);
    }
}
