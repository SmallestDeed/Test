package com.sandu.search.storage.design;

import com.sandu.search.common.constant.RedisConstant;
import com.sandu.search.entity.designplan.po.DesignPlanProductPo;
import com.sandu.search.entity.designplan.po.RecommendedPlanProductPo;
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
 * 推荐方案产品元数据存储
 *
 * @date 20180926
 * @auth xiaoxc
 */
@Slf4j
@Component
public class RecommendedPlanProductMetaDataStorage {

    private final static String CLASS_LOG_PREFIX = "推荐方案产品元数据存储:";

    //默认缓存模式
    private static Integer STORAGE_MODE = StorageComponent.CACHE_MODE;

    private final RedisService redisService;
    private final MetaDataService metaDataService;

    @Autowired
    public RecommendedPlanProductMetaDataStorage(RedisService redisService, MetaDataService metaDataService) {
        this.redisService = redisService;
        this.metaDataService = metaDataService;
    }

    //推荐推荐方案产品
    private static Map<String, String> recommendedPlanProductMap = null;

    //切换存储模式
    @SuppressWarnings("all")
    public void changeStorageMode(Integer storageMode) {
        //缓存模式
        if (StorageComponent.CACHE_MODE == storageMode) {
            //清空内存占用
            recommendedPlanProductMap = null;
            //切换
            STORAGE_MODE = storageMode;
            //内存模式
        } else if(StorageComponent.MEMORY_MODE == storageMode) {
            //切换
            STORAGE_MODE = storageMode;
            //写入内存
            updateData();
        }
        log.info(CLASS_LOG_PREFIX + "推荐方案产品存储模式切换成功，当前存储:{}.", StorageComponent.CACHE_MODE == STORAGE_MODE ? "缓存" : "内存");
    }

    //获取Map数据方法兼容
    private String getMap(String mapName, String keyName) {
        //缓存模式
        if (StorageComponent.CACHE_MODE == STORAGE_MODE) {
            return redisService.getMap(mapName, keyName);
            //内存模式
        } else if (StorageComponent.MEMORY_MODE == STORAGE_MODE) {
            return recommendedPlanProductMap.get(keyName);
        }
        return null;
    }

    //更新数据
    public void updateData() {

        Map<String, String> originRecommendedPlanProductMap = new HashMap<>();

        //获取数据
        log.info(CLASS_LOG_PREFIX + "开始获取推荐推荐方案产品元数据....");
        List<RecommendedPlanProductPo> designPlanProductList;
        try {
            designPlanProductList = metaDataService.queryRecommendedPlanProductMetaData();
        } catch (MetaDataException e) {
            log.error(CLASS_LOG_PREFIX + "获取推荐推荐方案产品元数据失败: MetaDataException:{}", e);
            throw new NullPointerException(CLASS_LOG_PREFIX + "获取推荐推荐方案产品元数据失败,List<DesignPlanProductPo> is null.MetaDataException:" + e);
        }
        log.info(CLASS_LOG_PREFIX + "获取推荐推荐方案产品元数据完成,总条数:{}", (null == designPlanProductList ? 0 : designPlanProductList.size()));

        if (null == designPlanProductList || 0 >= designPlanProductList.size()) {
            log.error(CLASS_LOG_PREFIX + "初始化推荐推荐方案产品元数据失败,数据为空，请检查数据库design_plan_recommende_product数据。");
            throw new RuntimeException(CLASS_LOG_PREFIX + "初始化推荐推荐方案产品元数据失败,数据为空，请检查数据库design_plan_recommende_product数据。");
        }
        //格式化数据
        for (RecommendedPlanProductPo designPlanProductPo : designPlanProductList) {
            String recommendedPlanId = designPlanProductPo.getRecommendedPlanId() + "";
            String productId = designPlanProductPo.getProductId() + "";
            if (StringUtils.isEmpty(recommendedPlanId) || StringUtils.isEmpty(productId)) {
                continue;
            }
            StringBuilder builder = new StringBuilder(200);
            builder.append(productId);
            if (originRecommendedPlanProductMap.containsKey(recommendedPlanId)) {
                builder.append("," + originRecommendedPlanProductMap.get(recommendedPlanId));
            }
            originRecommendedPlanProductMap.put(recommendedPlanId, builder.toString());
        }


        //载入缓存
        redisService.addMapCompatible(RedisConstant.RECOMMENDATION_PLAN_PRODUCT_DATA, originRecommendedPlanProductMap);
        log.info(CLASS_LOG_PREFIX + "推荐方案产品元数据装载缓存完成....");

        if (StorageComponent.MEMORY_MODE == STORAGE_MODE) {
            recommendedPlanProductMap = originRecommendedPlanProductMap;
            log.info(CLASS_LOG_PREFIX + "推荐方案产品元数据装载内存完成....");
        }
    }

    /**
     * 根据推荐方案ID获取方案产品ids
     *
     * @param recommendationPlanId 推荐方案ID
     * @return
     */
    public String getProductIdsByRecommendationPlanId(Integer recommendationPlanId) {

        if (null == recommendationPlanId || 0 == recommendationPlanId) {
            return null;
        }

        return getMap(RedisConstant.RECOMMENDATION_PLAN_PRODUCT_DATA, recommendationPlanId + "");
    }

    /**
     * 根据方案ID获取数据方案产品IDs
     * @param planId
     * @return
     */
    public List<Integer> selectProductIdsByPlanId(Integer planId) {

        List<Integer> productIdList;
        try {
            productIdList = metaDataService.queryRecommendedPlanProductByPlanIdMetaData(planId);
        } catch (MetaDataException e) {
            log.error(CLASS_LOG_PREFIX + "获取推荐推荐方案产品元数据失败:planId:{}, MetaDataException:{}", planId, e);
            throw new NullPointerException(CLASS_LOG_PREFIX + "获取推荐推荐方案产品元数据失败,List<DesignPlanProductPo> is null.MetaDataException:" + e);
        }

//        if (null == productIdList || 0 >= productIdList.size()) {
//            log.error(CLASS_LOG_PREFIX + "初始化推荐推荐方案产品元数据失败,数据为空 planId =" + planId);
//            throw new RuntimeException(CLASS_LOG_PREFIX + "初始化推荐推荐方案产品元数据失败,数据为空 planId =" + planId);
//        }

        return productIdList;
    }
}