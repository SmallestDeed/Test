package com.sandu.search.storage.system;

import com.sandu.search.common.constant.RedisConstant;
import com.sandu.search.common.tools.JsonUtil;
import com.sandu.search.entity.common.AreaZone;
import com.sandu.search.entity.elasticsearch.po.metadate.AreaPo;
import com.sandu.search.entity.elasticsearch.po.metadate.SysUserPo;
import com.sandu.search.exception.MetaDataException;
import com.sandu.search.service.metadata.MetaDataService;
import com.sandu.search.service.redis.RedisService;
import com.sandu.search.storage.StorageComponent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
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

    private final static String CLASS_LOG_PREFIX = "用户元数据存储:";
    //默认缓存模式
    private static Integer STORAGE_MODE = StorageComponent.CACHE_MODE;

    private final RedisService redisService;
    private final MetaDataService metaDataService;

    @Autowired
    public SysUserMetaDataStorage(RedisService redisService, MetaDataService metaDataService) {
        this.redisService = redisService;
        this.metaDataService = metaDataService;
    }

    //用户元数据Map
    private static Map<String, String> sysUserMap = null;

    //切换存储模式
    public void changeStorageMode(Integer storageMode) {
        //缓存模式
        if (StorageComponent.CACHE_MODE == storageMode) {
            //清空内存占用
            sysUserMap = null;
            //切换
            STORAGE_MODE = storageMode;
            //内存模式
        } else if (StorageComponent.MEMORY_MODE == storageMode) {
            //切换
            STORAGE_MODE = storageMode;
            //写入内存
            updateData();
            //areaMap = redisService.getMap(RedisConstant.AREA_DATA);
        }
        log.info(CLASS_LOG_PREFIX + "用户存储模式切换成功，当前存储:{}.", StorageComponent.CACHE_MODE == STORAGE_MODE ? "缓存" : "内存");
    }

    //获取Map数据方法兼容
    @SuppressWarnings("all")
    private String getMap(String mapName, String keyName) {
        //缓存模式
        if (StorageComponent.CACHE_MODE == STORAGE_MODE) {
            return redisService.getMap(mapName, keyName);
            //内存模式
        } else if (StorageComponent.MEMORY_MODE == STORAGE_MODE) {
            if (RedisConstant.SYS_USER_DATA.equals(mapName)) {
                return sysUserMap.get(keyName);
            }
        }
        return null;
    }

    //更新数据
    public void updateData() {
        log.info(CLASS_LOG_PREFIX + "开始获取用户元数据....");
        //用户元数据
        List<SysUserPo> userPoList;
        try {
            userPoList = metaDataService.queryUserMetaData();
        } catch (MetaDataException e) {
            log.error(CLASS_LOG_PREFIX + "获取用户元数据失败: MetaDataException:{}", e);
            throw new NullPointerException(CLASS_LOG_PREFIX + "获取用户元数据失败,List<SysUserPo> is null.MetaDataException:" + e);
        }
        log.info(CLASS_LOG_PREFIX + "获取用户元数据完成,总条数:{}", (null == userPoList ? 0 : userPoList.size()));

        Map<String, String> userPoMap = new HashMap<>(userPoList.size());
        //转换Map
        if (null != userPoList && 0 != userPoList.size()) {
            //遍历Map切换数据格式
            userPoList.forEach(userPo -> userPoMap.put(userPo.getUserId() + "", JsonUtil.toJson(userPo)));
        }
        log.info(CLASS_LOG_PREFIX + "格式化用户元数据完成....");

        //装载缓存
        redisService.addMapCompatible(RedisConstant.SYS_USER_DATA, userPoMap);
        log.info(CLASS_LOG_PREFIX + "用户元数据装载缓存完成....");

        //内存模式
        if (StorageComponent.MEMORY_MODE == STORAGE_MODE) {
            sysUserMap = userPoMap;
            log.info(CLASS_LOG_PREFIX + "用户元数据装载内存完成....");
        }
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

        String userStr = getMap(RedisConstant.SYS_USER_DATA, userId + "");
        if (StringUtils.isEmpty(userStr)) {
            return null;
        }

        return JsonUtil.fromJson(userStr, SysUserPo.class);
    }
}
