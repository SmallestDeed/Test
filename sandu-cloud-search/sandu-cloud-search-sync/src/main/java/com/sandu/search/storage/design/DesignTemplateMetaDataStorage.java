package com.sandu.search.storage.design;

import com.sandu.search.common.constant.RedisConstant;
import com.sandu.search.entity.elasticsearch.po.metadate.DesignTemplatePo;
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
 * 设计方案样板房元数据存储
 *
 * @date 20180131
 * @auth pengxuangang
 */
@Slf4j
@Component
public class DesignTemplateMetaDataStorage {

    private final static String CLASS_LOG_PREFIX = "设计方案样板房元数据存储:";
    //默认缓存模式
    private static Integer STORAGE_MODE = StorageComponent.CACHE_MODE;

    private final RedisService redisService;
    private final MetaDataService metaDataService;

    @Autowired
    public DesignTemplateMetaDataStorage(RedisService redisService, MetaDataService metaDataService) {
        this.redisService = redisService;
        this.metaDataService = metaDataService;
    }

    private static Map<String, String> designTemplateMap = null;

    //切换存储模式
    public void changeStorageMode(Integer storageMode) {
        //缓存模式
        if (StorageComponent.CACHE_MODE == storageMode) {
            //清空内存占用
            designTemplateMap = null;
            //切换
            STORAGE_MODE = storageMode;
            //内存模式
        } else if(StorageComponent.MEMORY_MODE == storageMode) {
            //切换
            STORAGE_MODE = storageMode;
            //写入内存
            updateData();
            //designTemplateMap = redisService.getMap(RedisConstant.DESIGN_TEMPLATE_DATA);
        }
        log.info(CLASS_LOG_PREFIX + "设计方案样板房存储模式切换成功，当前存储:{}.", StorageComponent.CACHE_MODE == STORAGE_MODE ? "缓存" : "内存");
    }

    //获取Map数据方法兼容
    private String getMap(String mapName, String keyName) {
        //缓存模式
        if (StorageComponent.CACHE_MODE == STORAGE_MODE) {
            return redisService.getMap(mapName, keyName);
            //内存模式
        } else if (StorageComponent.MEMORY_MODE == STORAGE_MODE) {
            if (RedisConstant.DESIGN_TEMPLATE_DATA.equals(mapName)) {
                return designTemplateMap.get(keyName);
            }
        }
        return null;
    }

    //更新数据
    public void updateData() {
        //获取数据
        log.info(CLASS_LOG_PREFIX + "开始获取设计方案样板房元数据....");

        //设计方案样板房元数据
        List<DesignTemplatePo> designTemplatePoList;
        try {
            designTemplatePoList = metaDataService.queryDesignTemplatePoMetaData();
        } catch (MetaDataException e) {
            log.error(CLASS_LOG_PREFIX + "获取设计方案样板房元数据失败: MetaDataException:{}", e);
            throw new NullPointerException(CLASS_LOG_PREFIX + "获取设计方案样板房元数据失败,List<DesignTemplatePo> is null.MetaDataException:" + e);
        }

        log.info(CLASS_LOG_PREFIX + "获取设计方案样板房元数据完成,总条数:{}", (null == designTemplatePoList ? 0 : designTemplatePoList.size()));

        //Map对象
        Map<String, String> tempDesignTemplateMap = new HashMap<>();
        //转换为Map元数据
        if (null != designTemplatePoList && 0 != designTemplatePoList.size()) {
            designTemplatePoList.forEach(designTemplatePo -> tempDesignTemplateMap.put(designTemplatePo.getId() + "", designTemplatePo.getSpaceCommonId() + ""));
        }
        log.info(CLASS_LOG_PREFIX + "格式化设计方案样板房元数据完成.");

        //装回对象
        redisService.addMapCompatible(RedisConstant.DESIGN_TEMPLATE_DATA, tempDesignTemplateMap);
        log.info(CLASS_LOG_PREFIX + "缓存载入设计方案样板房元数据完成.");

        //内存模式
        if (StorageComponent.MEMORY_MODE == STORAGE_MODE) {
            designTemplateMap = tempDesignTemplateMap;
            log.info(CLASS_LOG_PREFIX + "设计方案产品元数据装载内存完成....");
        }
    }

    /**
     * 根据设计方案样板房ID获取空间ID
     *
     * @param designPlanTemplateId  设计方案样板房ID
     * @return
     */
    public int getSpaceCommonIdByDesignPlanTemplateId (Integer designPlanTemplateId) {

        if (null == designPlanTemplateId || 0 > designPlanTemplateId ) {
            return 0;
        }

        String spaceCommonIdStr = getMap(RedisConstant.DESIGN_TEMPLATE_DATA, designPlanTemplateId + "");
        if (StringUtils.isEmpty(spaceCommonIdStr)) {
            return 0;
        }

        return Integer.parseInt(spaceCommonIdStr);
    }
}
