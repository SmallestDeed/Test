package com.sandu.search.storage.design;

import com.sandu.search.common.constant.RedisConstant;
import com.sandu.search.entity.elasticsearch.po.metadate.DesignPlanBrandPo;
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
 * 推荐方案品牌元数据存储
 *
 * @date 2018/5/31
 * @auth pengxuangang
 * @mail xuangangpeng@gmail.com
 */
@Slf4j
@Component
public class DesignPlanBrandMetaDataStorage {

    private final static String CLASS_LOG_PREFIX = "推荐方案品牌元数据存储:";
    //默认缓存模式
    private static Integer STORAGE_MODE = StorageComponent.CACHE_MODE;

    private final RedisService redisService;
    private final MetaDataService metaDataService;

    @Autowired
    public DesignPlanBrandMetaDataStorage(RedisService redisService, MetaDataService metaDataService) {
        this.redisService = redisService;
        this.metaDataService = metaDataService;
    }

    private static Map<String, String> recommendationPlanBrandMap = null;
    private static Map<String, String> recommendationPlanCompanyMap = null;

    //切换存储模式
    public void changeStorageMode(Integer storageMode) {
        //缓存模式
        if (StorageComponent.CACHE_MODE == storageMode) {
            //清空内存占用
            recommendationPlanBrandMap = null;
            //切换
            recommendationPlanCompanyMap = null;
            STORAGE_MODE = storageMode;
            //内存模式
        } else if(StorageComponent.MEMORY_MODE == storageMode) {
            //切换
            STORAGE_MODE = storageMode;
            //写入内存
            updateData();
        }
        log.info(CLASS_LOG_PREFIX + "推荐方案品牌存储模式切换成功，当前存储:{}.", StorageComponent.CACHE_MODE == STORAGE_MODE ? "缓存" : "内存");
    }

    //获取Map数据方法兼容
    private String getMap(String mapName, String keyName) {
        //缓存模式
        if (StorageComponent.CACHE_MODE == STORAGE_MODE) {
            return redisService.getMap(mapName, keyName);
            //内存模式
        } else if (StorageComponent.MEMORY_MODE == STORAGE_MODE) {
            if (RedisConstant.RECOMMENDATION_PLAN_BRAND.equals(mapName)) {
                return recommendationPlanBrandMap.get(keyName);
            } else if (RedisConstant.RECOMMENDATION_PLAN_COMPANY.equals(mapName)) {
                return recommendationPlanCompanyMap.get(keyName);
            }
        }
        return null;
    }


    //更新数据
    @SuppressWarnings("all")
    public void updateData() {
        //获取数据
        log.info(CLASS_LOG_PREFIX + "开始获取推荐方案品牌元数据存储....");

        //推荐方案品牌元数据
        List<DesignPlanBrandPo> designPlanBrandList;
        try {
            designPlanBrandList = metaDataService.queryDesignPlanBrandMetaData();
        } catch (MetaDataException e) {
            log.error(CLASS_LOG_PREFIX + "获取推荐方案品牌元数据失败: MetaDataException:{}", e);
            throw new NullPointerException(CLASS_LOG_PREFIX + "获取推荐方案品牌元数据失败,List<DesignPlanBrandPo> is null.MetaDataException:" + e);
        }
        log.info(CLASS_LOG_PREFIX + "获取推荐方案品牌元数据完成,总条数:{}", (null == designPlanBrandList ? 0 : designPlanBrandList.size()));

        if (null == designPlanBrandList || 0 >= designPlanBrandList.size()) {
            log.error(CLASS_LOG_PREFIX + "获取推荐方案品牌元数据为空，请检查数据...");
            return;
        }

        //Map对象
        Map<String, String> tempRecommendationPlanBrandMap = new HashMap<>(designPlanBrandList.size());
        Map<String, String> tempRecommendationPlanCompanyMap = new HashMap<>(designPlanBrandList.size());

        //转换为Map元数据
        designPlanBrandList.forEach(designPlanBrand -> {
            String recommendationPlanId = designPlanBrand.getDesignPlanId() + "";
            //推荐方案品牌
            Integer brandId = designPlanBrand.getBrandId();
            if (null == brandId) { //为空当做无品牌处理，前端搜索可以看到
                brandId = -1;
            }
            StringBuilder brandIdBuilder = new StringBuilder(brandId + "");
            if (tempRecommendationPlanBrandMap.containsKey(recommendationPlanId)) {
                brandIdBuilder.append(","+ tempRecommendationPlanBrandMap.get(recommendationPlanId));
            }
            tempRecommendationPlanBrandMap.put(recommendationPlanId, brandIdBuilder.toString());

            //推荐方案公司
            Integer companyId = designPlanBrand.getCompanyId();
            if (null != companyId && 0 < companyId) {
                StringBuilder companyIdBuilder = new StringBuilder(companyId + "");
                if (tempRecommendationPlanCompanyMap.containsKey(recommendationPlanId)) {
                    companyIdBuilder.append("," + tempRecommendationPlanCompanyMap.get(recommendationPlanId));
                }
                tempRecommendationPlanCompanyMap.put(recommendationPlanId, companyIdBuilder.toString());
            }
        });
        log.info(CLASS_LOG_PREFIX + "格式化推荐方案品牌元数据完成.");

        //装载缓存
        redisService.addMapCompatible(RedisConstant.RECOMMENDATION_PLAN_BRAND, tempRecommendationPlanBrandMap);
        redisService.addMapCompatible(RedisConstant.RECOMMENDATION_PLAN_COMPANY, tempRecommendationPlanCompanyMap);
        log.info(CLASS_LOG_PREFIX + "装载缓存推荐方案品牌元数据完成.");

        if (StorageComponent.MEMORY_MODE == STORAGE_MODE) {
            recommendationPlanBrandMap = tempRecommendationPlanBrandMap;
            recommendationPlanCompanyMap = tempRecommendationPlanCompanyMap;
            log.info(CLASS_LOG_PREFIX + "装载内存推荐方案品牌元数据完成.");
        }
    }

    /**
     * 根据推荐方案ID获取品牌ID
     *
     * @param recommendationPlanId 推荐方案ID
     * @return
     */
    public String getBrandIdByDesignPlanId(Integer recommendationPlanId) {

        if (null == recommendationPlanId || 0 == recommendationPlanId) {
            return null;
        }

        return getMap(RedisConstant.RECOMMENDATION_PLAN_BRAND, recommendationPlanId + "");
    }

    /**
     * 根据推荐方案ID获取公司ID
     *
     * @param recommendationPlanId 推荐方案ID
     * @return
     */
    public String getCompanyIdByRecommendationPlanId(Integer recommendationPlanId) {

        if (null == recommendationPlanId || 0 == recommendationPlanId) {
            return null;
        }

        return getMap(RedisConstant.RECOMMENDATION_PLAN_COMPANY, recommendationPlanId + "");
    }

}
