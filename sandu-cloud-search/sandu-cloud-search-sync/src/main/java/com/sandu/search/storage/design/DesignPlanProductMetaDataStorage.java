package com.sandu.search.storage.design;

import com.sandu.search.common.constant.RedisConstant;
import com.sandu.search.entity.designplan.po.DesignPlanProductPo;
import com.sandu.search.exception.MetaDataException;
import com.sandu.search.service.metadata.MetaDataService;
import com.sandu.search.service.redis.RedisService;
import com.sandu.search.storage.StorageComponent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 设计方案产品元数据存储
 *
 * @date 20180131
 * @auth pengxuangang
 */
@Slf4j
@Component
public class DesignPlanProductMetaDataStorage {

    private final static String CLASS_LOG_PREFIX = "设计方案产品元数据存储:";

    //默认缓存模式
    private static Integer STORAGE_MODE = StorageComponent.CACHE_MODE;

    private final RedisService redisService;
    private final MetaDataService metaDataService;

    @Autowired
    public DesignPlanProductMetaDataStorage(RedisService redisService, MetaDataService metaDataService) {
        this.redisService = redisService;
        this.metaDataService = metaDataService;
    }

    //草稿设计方案产品
    private static Map<String, String> tempDesignPlanProductMap = null;
    //推荐设计方案产品
    private static Map<String, String> recommendDesignPlanProductMap = null;
    //自定义设计方案产品
    private static Map<String, String> diyDesignPlanProductMap = null;

    //切换存储模式
    @SuppressWarnings("all")
    public void changeStorageMode_old(Integer storageMode) {
        //缓存模式
        if (StorageComponent.CACHE_MODE == storageMode) {
            //清空内存占用
            tempDesignPlanProductMap = null;
            recommendDesignPlanProductMap = null;
            diyDesignPlanProductMap = null;
            //切换
            STORAGE_MODE = storageMode;
            //内存模式
        } else if(StorageComponent.MEMORY_MODE == storageMode) {
            //切换
            STORAGE_MODE = storageMode;
            //写入内存
            updateData_old();
            //tempDesignPlanProductMap = redisService.getMap(RedisConstant.TEMP_DESIGNPLAN_PRODUCT_DATA);
            //recommendDesignPlanProductMap = redisService.getMap(RedisConstant.RECOMMEND_DESIGNPLAN_PRODUCT_DATA);
            //diyDesignPlanProductMap = redisService.getMap(RedisConstant.DIY_DESIGNPLAN_PRODUCT_DATA);
        }
        log.info(CLASS_LOG_PREFIX + "设计方案产品存储模式切换成功，当前存储:{}.", StorageComponent.CACHE_MODE == STORAGE_MODE ? "缓存" : "内存");
    }

    /**
     * 获取Map数据方法兼容
     * update by huangsongbo 2018.9.11 getMap -> getMap_old
     * 
     * @param mapName
     * @param keyName
     * @return
     */
    private String getMap_old(String mapName, String keyName) {
        //缓存模式
        if (StorageComponent.CACHE_MODE == STORAGE_MODE) {
            return redisService.getMap(mapName, keyName);
            //内存模式
        } else if (StorageComponent.MEMORY_MODE == STORAGE_MODE) {
            if (RedisConstant.TEMP_DESIGNPLAN_PRODUCT_DATA.equals(mapName)) {
                return tempDesignPlanProductMap.get(keyName);
            } else if (RedisConstant.RECOMMEND_DESIGNPLAN_PRODUCT_DATA.equals(mapName)) {
                return recommendDesignPlanProductMap.get(keyName);
            } else if (RedisConstant.DIY_DESIGNPLAN_PRODUCT_DATA.equals(mapName)) {
                return diyDesignPlanProductMap.get(keyName);
            }
        }
        return null;
    }

    /**
     * 更新数据
     * update by huangsongbo rename: updateData -> updateData_old
     * 
     */
    public void updateData_old() {

        Map<String, String> originTempDesignPlanProductMap = new HashMap<>();
        Map<String, String> originRecommendDesignPlanProductMap = new HashMap<>();
        Map<String, String> originDiyDesignPlanProductMap = new HashMap<>();

        /***************************** 草稿设计方案 *********************************/
        log.info(CLASS_LOG_PREFIX + "开始获取草稿设计方案产品元数据....");
        //设计方案产品元数据
        List<DesignPlanProductPo> designPlanProductList;
        try {
            designPlanProductList = metaDataService.queryTempDesignPlanProductMetaData();
        } catch (MetaDataException e) {
            log.error(CLASS_LOG_PREFIX + "获取草稿设计方案产品元数据失败: MetaDataException:{}", e);
            throw new NullPointerException(CLASS_LOG_PREFIX + "获取草稿设计方案产品元数据失败,List<DesignPlanProductPo> is null.MetaDataException:" + e);
        }
        log.info(CLASS_LOG_PREFIX + "获取草稿设计方案产品元数据完成,总条数:{}", (null == designPlanProductList ? 0 : designPlanProductList.size()));

        if (null == designPlanProductList || 0 >= designPlanProductList.size()) {
            log.error(CLASS_LOG_PREFIX + "初始化草稿设计方案产品元数据失败,数据为空，请检查数据库design_plan_product数据。");
            throw new RuntimeException(CLASS_LOG_PREFIX + "初始化草稿设计方案产品元数据失败,数据为空，请检查数据库design_plan_product数据。");
        }
        //格式化数据
        designPlanProductList.forEach(designPlanProductPo -> originTempDesignPlanProductMap.put(designPlanProductPo.getId() + "", designPlanProductPo.getInitProductId() + ""));

        /***************************** 推荐设计方案 *********************************/
        //获取数据
        log.info(CLASS_LOG_PREFIX + "开始获取推荐设计方案产品元数据....");
        try {
            designPlanProductList = metaDataService.queryRecommendDesignPlanProductMetaData();
        } catch (MetaDataException e) {
            log.error(CLASS_LOG_PREFIX + "获取推荐设计方案产品元数据失败: MetaDataException:{}", e);
            throw new NullPointerException(CLASS_LOG_PREFIX + "获取推荐设计方案产品元数据失败,List<DesignPlanProductPo> is null.MetaDataException:" + e);
        }
        log.info(CLASS_LOG_PREFIX + "获取推荐设计方案产品元数据完成,总条数:{}", (null == designPlanProductList ? 0 : designPlanProductList.size()));

        if (null == designPlanProductList || 0 >= designPlanProductList.size()) {
            log.error(CLASS_LOG_PREFIX + "初始化推荐设计方案产品元数据失败,数据为空，请检查数据库design_plan_product数据。");
            throw new RuntimeException(CLASS_LOG_PREFIX + "初始化推荐设计方案产品元数据失败,数据为空，请检查数据库design_plan_product数据。");
        }
        //格式化数据
        designPlanProductList.forEach(designPlanProductPo -> originRecommendDesignPlanProductMap.put(designPlanProductPo.getId() + "", designPlanProductPo.getInitProductId() + ""));

        /***************************** 自定义设计方案 *********************************/
        //获取数据
        log.info(CLASS_LOG_PREFIX + "开始获取自定义设计方案产品元数据....");
        try {
            designPlanProductList = metaDataService.queryDiyDesignPlanProductMetaData();
        } catch (MetaDataException e) {
            log.error(CLASS_LOG_PREFIX + "获取自定义设计方案产品元数据失败: MetaDataException:{}", e);
            throw new NullPointerException(CLASS_LOG_PREFIX + "获取自定义设计方案产品元数据失败,List<DesignPlanProductPo> is null.MetaDataException:" + e);
        }
        log.info(CLASS_LOG_PREFIX + "获取自定义设计方案产品元数据完成,总条数:{}", (null == designPlanProductList ? 0 : designPlanProductList.size()));

        if (null == designPlanProductList || 0 >= designPlanProductList.size()) {
            log.error(CLASS_LOG_PREFIX + "初始化自定义设计方案产品元数据失败,数据为空，请检查数据库design_plan_product数据。");
            throw new RuntimeException(CLASS_LOG_PREFIX + "初始化自定义设计方案产品元数据失败,数据为空，请检查数据库design_plan_product数据。");
        }
        //格式化数据
        designPlanProductList.forEach(designPlanProductPo -> originDiyDesignPlanProductMap.put(designPlanProductPo.getId() + "", designPlanProductPo.getInitProductId() + ""));

        //载入缓存
        redisService.addMapCompatible(RedisConstant.TEMP_DESIGNPLAN_PRODUCT_DATA, originTempDesignPlanProductMap);
        redisService.addMapCompatible(RedisConstant.RECOMMEND_DESIGNPLAN_PRODUCT_DATA, originRecommendDesignPlanProductMap);
        redisService.addMapCompatible(RedisConstant.DIY_DESIGNPLAN_PRODUCT_DATA, originDiyDesignPlanProductMap);
        log.info(CLASS_LOG_PREFIX + "设计方案产品元数据装载缓存完成....");

        if (StorageComponent.MEMORY_MODE == STORAGE_MODE) {
            tempDesignPlanProductMap = originTempDesignPlanProductMap;
            recommendDesignPlanProductMap = originRecommendDesignPlanProductMap;
            diyDesignPlanProductMap = originDiyDesignPlanProductMap;
            log.info(CLASS_LOG_PREFIX + "设计方案产品元数据装载内存完成....");
        }
    }
}