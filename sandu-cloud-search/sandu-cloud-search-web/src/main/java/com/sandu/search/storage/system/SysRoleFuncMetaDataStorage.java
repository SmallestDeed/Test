package com.sandu.search.storage.system;

import com.google.gson.reflect.TypeToken;
import com.sandu.search.common.constant.RedisConstant;
import com.sandu.search.common.tools.JsonUtil;
import com.sandu.search.service.redis.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * 角色菜单元数据存储
 *
 * @date 20180709
 * @auth xiaoxc
 */
@Slf4j
@Component
public class SysRoleFuncMetaDataStorage {

    private final RedisService redisService;

    @Autowired
    public SysRoleFuncMetaDataStorage(RedisService redisService) {
        this.redisService = redisService;
    }

    /**
     * 根据菜单编码查询角色编码集合
     *
     * @param funcCode 菜单Code
     * @return
     */
    public List<String> getRoleListByFuncCode(String funcCode) {
        if (StringUtils.isEmpty(funcCode)) {
            return null;
        }

        String roleCodeListStr = redisService.getMap(RedisConstant.SYS_ROLE_FUNC_DATA, funcCode);
        if (StringUtils.isEmpty(roleCodeListStr)) {
            return null;
        }

        return JsonUtil.fromJson(roleCodeListStr, new TypeToken<List<String>>() {}.getType());
    }
}
