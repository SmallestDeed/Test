package com.sandu.search.storage.design;

import com.sandu.search.common.constant.RedisConstant;
import com.sandu.search.entity.elasticsearch.po.metadate.DesignPlanBrandPo;
import com.sandu.search.entity.elasticsearch.po.metadate.DesignPlanRecommendedPo;
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
 * 推荐方案元数据存储
 *
 * @date 2018/9/10
 * @auth xiaoxc
 */
@Slf4j
@Component
public class DesignPlanRecommendationMetaDataStorage {

    private final static String CLASS_LOG_PREFIX = "推荐方案元数据存储:";
    //默认缓存模式
    private static Integer STORAGE_MODE = StorageComponent.CACHE_MODE;

    private final RedisService redisService;
    private final MetaDataService metaDataService;

    @Autowired
    public DesignPlanRecommendationMetaDataStorage(RedisService redisService, MetaDataService metaDataService) {
        this.redisService = redisService;
        this.metaDataService = metaDataService;
    }

    private static Map<String, String> recommendationGroupPlanApplySpaceAreasMap = null;

    //切换存储模式
    public void changeStorageMode(Integer storageMode) {
        //缓存模式
        if (StorageComponent.CACHE_MODE == storageMode) {
            //清空内存占用
            recommendationGroupPlanApplySpaceAreasMap = null;
            //切换
            STORAGE_MODE = storageMode;
            //内存模式
        } else if(StorageComponent.MEMORY_MODE == storageMode) {
            //切换
            STORAGE_MODE = storageMode;
            //写入内存
            updateData();
        }
        log.info(CLASS_LOG_PREFIX + "推荐方案存储模式切换成功，当前存储:{}.", StorageComponent.CACHE_MODE == STORAGE_MODE ? "缓存" : "内存");
    }

    //获取Map数据方法兼容
    private String getMap(String mapName, String keyName) {
        //缓存模式
        if (StorageComponent.CACHE_MODE == STORAGE_MODE) {
            return redisService.getMap(mapName, keyName);
            //内存模式
        } else if (StorageComponent.MEMORY_MODE == STORAGE_MODE) {
            return recommendationGroupPlanApplySpaceAreasMap.get(keyName);
        }
        return null;
    }


    //更新数据
    public void updateData() {
        //获取数据
        log.info(CLASS_LOG_PREFIX + "开始获取推荐方案元数据存储....");

        //推荐方案元数据
        List<DesignPlanRecommendedPo> designPlanRecommendedList;
        try {
            designPlanRecommendedList = metaDataService.queryDesignPlanRecommendedMetaData();
        } catch (MetaDataException e) {
            log.error(CLASS_LOG_PREFIX + "获取推荐方案元数据失败: MetaDataException:{}", e);
            throw new NullPointerException(CLASS_LOG_PREFIX + "获取推荐方案元数据失败,List<DesignPlanBrandPo> is null.MetaDataException:" + e);
        }
        log.info(CLASS_LOG_PREFIX + "获取推荐方案元数据完成,总条数:{}", (null == designPlanRecommendedList ? 0 : designPlanRecommendedList.size()));

        if (null == designPlanRecommendedList || 0 >= designPlanRecommendedList.size()) {
            log.error(CLASS_LOG_PREFIX + "获取推荐方案元数据为空，请检查数据...");
            return;
        }

        //Map对象
        Map<String, String> groupRecommendationPlanAreasMap = new HashMap<>(designPlanRecommendedList.size());

        //转换为Map元数据
        designPlanRecommendedList.forEach(recommendedPo -> {
            //推荐方案打组主键ID
            String groupPrimaryId = recommendedPo.getGroupPrimaryId() + "";
            //适用面积
            String applySpaceAreas= recommendedPo.getApplySpaceAreas();
            if (!StringUtils.isEmpty(groupPrimaryId) && !StringUtils.isEmpty(applySpaceAreas)) {
                StringBuilder builder = new StringBuilder(applySpaceAreas);
                if (groupRecommendationPlanAreasMap.containsKey(groupPrimaryId)) {
                    builder.append(","+ groupRecommendationPlanAreasMap.get(groupPrimaryId));
                }
                groupRecommendationPlanAreasMap.put(groupPrimaryId, builder.toString());
            }
        });
        log.info(CLASS_LOG_PREFIX + "格式化推荐方案元数据完成.");

        //装载缓存
        redisService.addMapCompatible(RedisConstant.GROUP_RECOMMENDATION_PLAN_AREAS_DATA, groupRecommendationPlanAreasMap);
        log.info(CLASS_LOG_PREFIX + "装载缓存推荐方案元数据完成.");

        if (StorageComponent.MEMORY_MODE == STORAGE_MODE) {
            recommendationGroupPlanApplySpaceAreasMap = groupRecommendationPlanAreasMap;
            log.info(CLASS_LOG_PREFIX + "装载内存推荐方案元数据完成.");
        }
    }

    /**
     * 根据推荐方案ID获取子方案所有的适用面积
     *
     * @param recommendationPlanId 推荐方案ID
     * @return
     */
    public String getGroupRecommendedAreasById(Integer recommendationPlanId) {

        if (null == recommendationPlanId || 0 == recommendationPlanId) {
            return null;
        }

        return getMap(RedisConstant.GROUP_RECOMMENDATION_PLAN_AREAS_DATA, recommendationPlanId + "");
    }

}
