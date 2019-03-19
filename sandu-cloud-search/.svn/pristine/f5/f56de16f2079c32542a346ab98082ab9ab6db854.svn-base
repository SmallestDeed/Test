package com.sandu.search.storage.space;

import com.sandu.search.common.constant.RedisConstant;
import com.sandu.search.common.tools.JsonUtil;
import com.sandu.search.entity.elasticsearch.po.metadate.SpaceCommonPo;
import com.sandu.search.exception.MetaDataException;
import com.sandu.search.service.metadata.MetaDataService;
import com.sandu.search.service.redis.RedisService;
import com.sandu.search.storage.StorageComponent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 空间元数据存储
 *
 * @date 2018/5/31
 * @auth pengxuangang
 * @mail xuangangpeng@gmail.com
 */
@Slf4j
@Component
public class SpaceCommonMetaDataStorage {

    private final static String CLASS_LOG_PREFIX = "空间元数据存储:";
    //默认缓存模式
    private static Integer STORAGE_MODE = StorageComponent.CACHE_MODE;

    private final RedisService redisService;
    private final MetaDataService metaDataService;

    @Autowired
    public SpaceCommonMetaDataStorage(RedisService redisService, MetaDataService metaDataService) {
        this.redisService = redisService;
        this.metaDataService = metaDataService;
    }

    private static Map<String, String> spaceCommonMap = null;

    //切换存储模式
    public void changeStorageMode(Integer storageMode) {
        //缓存模式
        if (StorageComponent.CACHE_MODE == storageMode) {
            //清空内存占用
            spaceCommonMap = null;
            //切换
            STORAGE_MODE = storageMode;
            //内存模式
        } else if(StorageComponent.MEMORY_MODE == storageMode) {
            //切换
            STORAGE_MODE = storageMode;
            //写入内存
            updateData();
            //spaceCommonMap = redisService.getMap(RedisConstant.SPACE_COMMON_DATA);
        }
        log.info(CLASS_LOG_PREFIX + "空间存储模式切换成功，当前存储:{}.", StorageComponent.CACHE_MODE == STORAGE_MODE ? "缓存" : "内存");
    }

    //获取Map数据方法兼容
    private String getMap(String mapName, String keyName) {
        //缓存模式
        if (StorageComponent.CACHE_MODE == STORAGE_MODE) {
            return redisService.getMap(mapName, keyName);
            //内存模式
        } else if (StorageComponent.MEMORY_MODE == STORAGE_MODE) {
            if (RedisConstant.SPACE_COMMON_DATA.equals(mapName)) {
                return spaceCommonMap.get(keyName);
            }
        }
        return null;
    }

    //更新数据
    public void updateData() {
        //获取数据
        log.info(CLASS_LOG_PREFIX + "开始获取空间元数据存储....");

        //空间元数据
        List<SpaceCommonPo> spaceCommonList;
        try {
            spaceCommonList = metaDataService.querySpaceCommonMetaData();
        } catch (MetaDataException e) {
            log.error(CLASS_LOG_PREFIX + "获取空间元数据失败: MetaDataException:{}", e);
            throw new NullPointerException(CLASS_LOG_PREFIX + "获取空间元数据失败,List<SpaceCommonPo> is null.MetaDataException:" + e);
        }

        log.info(CLASS_LOG_PREFIX + "获取空间元数据完成,总条数:{}", (null == spaceCommonList ? 0 : spaceCommonList.size()));

        //Map对象
        Map<String, String> tempSpaceCommonMap = new HashMap<>(null == spaceCommonList ? 10 : spaceCommonList.size());
        //转换为Map元数据
        if (null != spaceCommonList && 0 != spaceCommonList.size()) {
            spaceCommonList.forEach(spaceCommon -> tempSpaceCommonMap.put(spaceCommon.getId() + "", JsonUtil.toJson(spaceCommon)));
        }
        log.info(CLASS_LOG_PREFIX + "格式化空间元数据完成.");

        //装回对象
        redisService.addMapCompatible(RedisConstant.SPACE_COMMON_DATA, tempSpaceCommonMap);
        log.info(CLASS_LOG_PREFIX + "缓存载入空间元数据完成.");

        //内存模式
        if (StorageComponent.MEMORY_MODE == STORAGE_MODE) {
            spaceCommonMap = tempSpaceCommonMap;
            log.info(CLASS_LOG_PREFIX + "内存载入空间元数据完成....");
        }
    }

    /**
     * 根据空间ID获取空间对象
     *
     * @param spaceCommonId  空间ID
     * @return
     */
    public SpaceCommonPo getSpaceCommonBySpaceCommonId (Integer spaceCommonId) {

        if (null == spaceCommonId || 0 > spaceCommonId ) {
            return null;
        }

        String spaceCommonStr = getMap(RedisConstant.SPACE_COMMON_DATA, spaceCommonId + "");
        if (StringUtils.isEmpty(spaceCommonStr)) {
            return null;
        }

        return JsonUtil.fromJson(spaceCommonStr, SpaceCommonPo.class);
    }
}
