package com.sandu.search.storage.system;

import com.google.gson.reflect.TypeToken;
import com.sandu.search.common.constant.RedisConstant;
import com.sandu.search.common.tools.JsonUtil;
import com.sandu.search.entity.elasticsearch.po.metadate.SystemDictionaryPo;
import com.sandu.search.exception.MetaDataException;
import com.sandu.search.service.metadata.MetaDataService;
import com.sandu.search.service.redis.RedisService;
import com.sandu.search.storage.StorageComponent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * 系统字典值元数据存储
 *
 * @date 20180104
 * @auth pengxuangang
 */
@Slf4j
@Component
public class SystemDictionaryMetaDataStorage {

    private final static String CLASS_LOG_PREFIX = "系统字典值元数据存储:";
    //默认缓存模式
    private static Integer STORAGE_MODE = StorageComponent.CACHE_MODE;

    private final RedisService redisService;
    private final MetaDataService metaDataService;

    @Autowired
    public SystemDictionaryMetaDataStorage(RedisService redisService, MetaDataService metaDataService) {
        this.redisService = redisService;
        this.metaDataService = metaDataService;
    }

    //系统字典值元数据Map<type,List<SystemDictionaryPo>>
    private static Map<String, String> systemDictionaryTypeMap = null;
    private static Map<String, String> systemDictionaryKeyValueMap = null;
    private static Map<String, String> systemDictionaryKeyTypeMap = null;

    //切换存储模式
    public void changeStorageMode(Integer storageMode) {
        //缓存模式
        if (StorageComponent.CACHE_MODE == storageMode) {
            //清空内存占用
            systemDictionaryTypeMap = null;
            systemDictionaryKeyValueMap = null;
            systemDictionaryKeyTypeMap = null;
            //切换
            STORAGE_MODE = storageMode;
            //内存模式
        } else if(StorageComponent.MEMORY_MODE == storageMode) {
            //切换
            STORAGE_MODE = storageMode;
            //写入内存
            updateData();
            //systemDictionaryTypeMap = redisService.getMap(RedisConstant.SYSTEM_DICTIONARY_TYPE_DATA);
            //systemDictionaryKeyValueMap = redisService.getMap(RedisConstant.SYSTEM_DICTIONARY_KEY_VALUE_DATA);
            //systemDictionaryKeyTypeMap = redisService.getMap(RedisConstant.SYSTEM_DICTIONARY_KEY_TYPE_DATA);
        }
        log.info(CLASS_LOG_PREFIX + "系统字典值存储模式切换成功，当前存储:{}.", StorageComponent.CACHE_MODE == STORAGE_MODE ? "缓存" : "内存");
    }

    //获取Map数据方法兼容
    private String getMap(String mapName, String keyName) {
        //缓存模式
        if (StorageComponent.CACHE_MODE == STORAGE_MODE) {
            return redisService.getMap(mapName, keyName);
            //内存模式
        } else if (StorageComponent.MEMORY_MODE == STORAGE_MODE) {
            if (RedisConstant.SYSTEM_DICTIONARY_TYPE_DATA.equals(mapName)) {
                return systemDictionaryTypeMap.get(keyName);
            } else if (RedisConstant.SYSTEM_DICTIONARY_KEY_VALUE_DATA.equals(mapName)) {
                return systemDictionaryKeyValueMap.get(keyName);
            } else if (RedisConstant.SYSTEM_DICTIONARY_KEY_TYPE_DATA.equals(mapName)) {
                return systemDictionaryKeyTypeMap.get(keyName);
            }
        }
        return null;
    }

    //更新数据
    public void updateData() {

        log.info(CLASS_LOG_PREFIX + "开始获取系统字典值元数据....");
        List<SystemDictionaryPo> systemDictionaryPoList;
        try {
            systemDictionaryPoList = metaDataService.querySystemDictionaryMetaData();
        } catch (MetaDataException e) {
            log.error(CLASS_LOG_PREFIX + "获取系统字典值元数据失败: MetaDataException:{}", e);
            throw new NullPointerException(CLASS_LOG_PREFIX + "获取系统字典值元数据失败,List<SystemDictionaryPo> is null.MetaDataException:" + e);
        }
        log.info(CLASS_LOG_PREFIX + "获取系统字典值元数据完成,总条数:{}", (null == systemDictionaryPoList ? 0 : systemDictionaryPoList.size()));

        //临时对象
        Map<String, List<SystemDictionaryPo>> tempSystemDictionaryTypeMap = new HashMap<>();
        Map<String, String> tempSystemDictionaryKeyValueMap = new HashMap<>(null == systemDictionaryPoList ? 10 : systemDictionaryPoList.size());
        Map<String, String> tempSystemDictionaryKeyTypeMap = new HashMap<>(null == systemDictionaryPoList ? 10 : systemDictionaryPoList.size());

        //转换Map
        if (null != systemDictionaryPoList && 0 != systemDictionaryPoList.size()) {
            systemDictionaryPoList.forEach(systemDictionaryPo -> {
                //字典类型
                String dictionaryType = systemDictionaryPo.getDictionaryType();
                if (tempSystemDictionaryTypeMap.containsKey(dictionaryType)) {
                    List<SystemDictionaryPo> systemDictionaryList = new ArrayList<>();
                    systemDictionaryList.add(systemDictionaryPo);
                    systemDictionaryList.addAll(tempSystemDictionaryTypeMap.get(dictionaryType));
                    tempSystemDictionaryTypeMap.put(dictionaryType, systemDictionaryList);
                } else {
                    tempSystemDictionaryTypeMap.put(dictionaryType, Collections.singletonList(systemDictionaryPo));
                }
                //字典键值
                tempSystemDictionaryKeyValueMap.put(systemDictionaryPo.getDictionaryKey(), systemDictionaryPo.getDictionaryValue() + "");
                //字典键类型
                tempSystemDictionaryKeyTypeMap.put(systemDictionaryPo.getDictionaryKey(), systemDictionaryPo.getDictionaryType());
            });
        }
        log.info(CLASS_LOG_PREFIX + "格式化系统字典值元数据完成....");

        //数据转换
        Map<String, String> systemDictionaryTypeJsonMap = new HashMap<>(tempSystemDictionaryTypeMap.size());
        tempSystemDictionaryTypeMap.forEach((k, v) -> systemDictionaryTypeJsonMap.put(k, JsonUtil.toJson(v)));
        log.info(CLASS_LOG_PREFIX + "格式化系统字典值Json元数据完成....");

        //装载缓存
        redisService.addMapCompatible(RedisConstant.SYSTEM_DICTIONARY_TYPE_DATA, systemDictionaryTypeJsonMap);
        redisService.addMapCompatible(RedisConstant.SYSTEM_DICTIONARY_KEY_VALUE_DATA, tempSystemDictionaryKeyValueMap);
        redisService.addMapCompatible(RedisConstant.SYSTEM_DICTIONARY_KEY_TYPE_DATA, tempSystemDictionaryKeyTypeMap);
        log.info(CLASS_LOG_PREFIX + "系统字典值元数据装载缓存完成....");

        //内存模式
        if (StorageComponent.MEMORY_MODE == STORAGE_MODE) {
            systemDictionaryTypeMap = systemDictionaryTypeJsonMap;
            systemDictionaryKeyValueMap = tempSystemDictionaryKeyValueMap;
            systemDictionaryKeyTypeMap = tempSystemDictionaryKeyTypeMap;
            log.info(CLASS_LOG_PREFIX + "系统字典值元数据装载内存完成....");
        }
    }

    /**
     * 根据Key值获取字典值
     *
     * @param key
     * @return
     */
    public int getValueByKey(String key) {

        if (StringUtils.isEmpty(key)) {
            return 0;
        }

        String productSmallType = redisService.getMap(RedisConstant.SYSTEM_DICTIONARY_KEY_VALUE_DATA, key);
        if (StringUtils.isEmpty(productSmallType)) {
            return 0;
        }

        return Integer.parseInt(productSmallType);
    }

    /**
     * 通过类型查询系统字典数据
     *
     * @param dictionaryType 字典类型
     * @return
     */
    public List<SystemDictionaryPo> querySystemDictionaryListByType(String dictionaryType) {
        if (StringUtils.isEmpty(dictionaryType)) {
            return null;
        }

        String systemDictionaryPoListStr = getMap(RedisConstant.SYSTEM_DICTIONARY_TYPE_DATA, dictionaryType);
        if (StringUtils.isEmpty(systemDictionaryPoListStr)) {
            return null;
        }

        return JsonUtil.fromJson(systemDictionaryPoListStr, new TypeToken<List<SystemDictionaryPo>>() {}.getType());
    }

    /**
     * 通过类型和值查询系统字典名
     *
     * @param dictionaryType 字典类型
     * @return
     */
    public String getSystemDictionaryNameByTypeAndValue(String dictionaryType, Integer dictionaryValue) {

        List<SystemDictionaryPo> systemDictionaryList = querySystemDictionaryListByType(dictionaryType);
        if (null == systemDictionaryList || 0 == systemDictionaryList.size()) {
            return null;
        }

        String dictionaryName = null;
        for (SystemDictionaryPo systemDictionaryPo : systemDictionaryList) {
            if (systemDictionaryPo.getDictionaryValue() == dictionaryValue) {
                dictionaryName = systemDictionaryPo.getDictionaryName();
                break;
            }
        }

        return dictionaryName;
    }

    /**
     * 通过类型和值查询系统字典
     *
     * @param dictionaryType 字典类型
     * @return
     */
    public SystemDictionaryPo getSystemDictionaryByTypeAndValue(String dictionaryType, Integer dictionaryValue) {

        List<SystemDictionaryPo> systemDictionaryList = querySystemDictionaryListByType(dictionaryType);
        if (null == systemDictionaryList || 0 == systemDictionaryList.size()) {
            return null;
        }
        for (SystemDictionaryPo systemDictionaryPo : systemDictionaryList) {
            if (systemDictionaryPo.getDictionaryValue() == dictionaryValue) {
                return systemDictionaryPo;
            }
        }
        return null;
    }

    /**
     * 通过类型和值查询系统字典
     *
     * @param dictionaryType 字典类型
     * @return
     */
    public SystemDictionaryPo getSystemDictionaryByTypeAndKey(String dictionaryType, String dictionarykey) {

        List<SystemDictionaryPo> systemDictionaryList = querySystemDictionaryListByType(dictionaryType);
        if (null == systemDictionaryList || 0 == systemDictionaryList.size()) {
            return null;
        }
        for (SystemDictionaryPo systemDictionaryPo : systemDictionaryList) {
            if (systemDictionaryPo.getDictionaryKey().trim().equals(dictionarykey.trim())) {
                return systemDictionaryPo;
            }
        }
        return null;
    }
}
