package com.sandu.search.storage.design;

import com.sandu.search.common.constant.RedisConstant;
import com.sandu.search.entity.elasticsearch.po.metadate.RecommendedPlanLivingPo;
import com.sandu.search.exception.MetaDataException;
import com.sandu.search.service.metadata.MetaDataService;
import com.sandu.search.service.redis.RedisService;
import com.sandu.search.storage.StorageComponent;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 推荐方案小区元数据存储
 *
 * @date 2018/07/11
 * @auth xiaoxc
 */
@Slf4j
@Component
public class RecommendedPlanLivingMetaDataStorage {

    private final static String CLASS_LOG_PREFIX = "推荐方案小区元数据存储:";
    //默认缓存模式
    private static Integer STORAGE_MODE = StorageComponent.CACHE_MODE;

    private final RedisService redisService;
    private final MetaDataService metaDataService;

    @Autowired
    public RecommendedPlanLivingMetaDataStorage(RedisService redisService, MetaDataService metaDataService) {
        this.redisService = redisService;
        this.metaDataService = metaDataService;
    }

    private static Map<String, String> recommendationPlanLivingNameMap = null;

    //切换存储模式
    public void changeStorageMode(Integer storageMode) {
        //缓存模式
        if (StorageComponent.CACHE_MODE == storageMode) {
            //清空内存占用
            recommendationPlanLivingNameMap = null;
            //切换
            STORAGE_MODE = storageMode;
            //内存模式
        } else if(StorageComponent.MEMORY_MODE == storageMode) {
            //切换
            STORAGE_MODE = storageMode;
            //写入内存
            updateData();
        }
        log.info(CLASS_LOG_PREFIX + "推荐方案小区存储模式切换成功，当前存储:{}.", StorageComponent.CACHE_MODE == STORAGE_MODE ? "缓存" : "内存");
    }

    //获取Map数据方法兼容
    private String getMap(String mapName, String keyName) {
        //缓存模式
        if (StorageComponent.CACHE_MODE == STORAGE_MODE) {
            return redisService.getMap(mapName, keyName);
            //内存模式
        } else if (StorageComponent.MEMORY_MODE == STORAGE_MODE) {
            return recommendationPlanLivingNameMap.get(keyName);
        }
        return null;
    }


    //更新数据
    @SuppressWarnings("all")
    public void updateData() {
        //获取数据
        log.info(CLASS_LOG_PREFIX + "开始获取推荐方案小区元数据存储....");

        //推荐方案小区元数据
        List<RecommendedPlanLivingPo> planLivingList;
        try {
            planLivingList = metaDataService.queryPlanLivingMetaData();
        } catch (MetaDataException e) {
            log.error(CLASS_LOG_PREFIX + "获取推荐方案小区元数据失败: MetaDataException:{}", e);
            throw new NullPointerException(CLASS_LOG_PREFIX + "获取推荐方案小区元数据失败,List<RecommendedPlanLivingPo> is null.MetaDataException:" + e);
        }
        log.info(CLASS_LOG_PREFIX + "获取推荐方案小区元数据完成,总条数:{}", (null == planLivingList ? 0 : planLivingList.size()));

        if (null == planLivingList || 0 >= planLivingList.size()) {
            log.error(CLASS_LOG_PREFIX + "获取推荐方案小区元数据为空，请检查数据...");
            return;
        }

        //Map对象
        Map<String, String> tempPlanLivingMap = new HashMap<>(planLivingList.size());

        //转换为Map元数据
        planLivingList.forEach(planLiving -> {
            Integer spaceId = planLiving.getSpaceId();
            String livingName = planLiving.getLivingName();
            //推荐方案小区名称
            if (null != spaceId && 0 < spaceId && !StringUtils.isEmpty(livingName)) {
                StringBuilder livingNameBuilder = new StringBuilder(planLiving.getLivingName());
                if (tempPlanLivingMap.containsKey(spaceId + "")) {
                    livingNameBuilder.append(","+ tempPlanLivingMap.get(spaceId + ""));
                }
                tempPlanLivingMap.put(spaceId + "", livingNameBuilder.toString());
            }
        });
        log.info(CLASS_LOG_PREFIX + "格式化推荐方案小区元数据完成.");

        //装载缓存
        redisService.addMapCompatible(RedisConstant.RECOMMENDATION_PLAN_LIVING_DATA, tempPlanLivingMap);
        log.info(CLASS_LOG_PREFIX + "装载缓存推荐方案小区元数据完成.");

        if (StorageComponent.MEMORY_MODE == STORAGE_MODE) {
            recommendationPlanLivingNameMap = tempPlanLivingMap;
            log.info(CLASS_LOG_PREFIX + "装载内存推荐方案小区元数据完成.");
        }
    }

    /**
     * 根据空间ID获取小区名称列表
     *
     * @param spaceId 推荐方案ID
     * @return
     */
    public String getLivingNameBySpaceId(Integer spaceId) {

        if (null == spaceId || 0 > spaceId) {
            return null;
        }

        return getMap(RedisConstant.RECOMMENDATION_PLAN_LIVING_DATA, spaceId + "");
    }

}
