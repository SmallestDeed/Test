package com.sandu.search.storage.design;

import com.sandu.search.common.constant.DesignPlanType;
import com.sandu.search.common.constant.RedisConstant;
import com.sandu.search.common.constant.SystemDictionaryType;
import com.sandu.search.common.tools.JsonUtil;
import com.sandu.search.entity.elasticsearch.po.design.DecoratePriceData;
import com.sandu.search.entity.elasticsearch.po.metadate.DecoratePricePo;
import com.sandu.search.entity.elasticsearch.po.metadate.SystemDictionaryPo;
import com.sandu.search.exception.MetaDataException;
import com.sandu.search.service.metadata.MetaDataService;
import com.sandu.search.service.redis.RedisService;
import com.sandu.search.storage.StorageComponent;
import com.sandu.search.storage.system.SystemDictionaryMetaDataStorage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 装修报价元数据存储
 *
 * @auth xiaoxc
 * @date 2018-9-12
 */
@Slf4j
@Component
public class DesignPlanDecoratePriceMetaDataStorage {

    private final static String CLASS_LOG_PREFIX = "装修报价元数据存储:";

    //默认缓存模式
    private static Integer STORAGE_MODE = StorageComponent.CACHE_MODE;

    private final RedisService redisService;
    private final MetaDataService metaDataService;
    private final SystemDictionaryMetaDataStorage systemDictionaryMetaDataStorage;

    @Autowired
    public DesignPlanDecoratePriceMetaDataStorage(SystemDictionaryMetaDataStorage systemDictionaryMetaDataStorage, RedisService redisService, MetaDataService metaDataService) {
        this.redisService = redisService;
        this.metaDataService = metaDataService;
        this.systemDictionaryMetaDataStorage = systemDictionaryMetaDataStorage;
    }

    private static Map<String, String> recommenderDecoratePriceMap = null;
    private static Map<String, String> fullHousePlanDecoratePriceMap = null;

    //切换存储模式
    public void changeStorageMode(Integer storageMode) {
        //缓存模式
        if (StorageComponent.CACHE_MODE == storageMode) {
            //清空内存占用
            recommenderDecoratePriceMap = null;
            fullHousePlanDecoratePriceMap = null;
            //切换
            STORAGE_MODE = storageMode;
            //内存模式
        } else if(StorageComponent.MEMORY_MODE == storageMode) {
            //切换
            STORAGE_MODE = storageMode;
            //写入内存
            updateData();
        }
        log.info(CLASS_LOG_PREFIX + "装修报价存储模式切换成功，当前存储:{}.", StorageComponent.CACHE_MODE == STORAGE_MODE ? "缓存" : "内存");
    }

    //获取Map数据方法兼容
    private String getMap(String mapName, String keyName) {
        //缓存模式
        if (StorageComponent.CACHE_MODE == STORAGE_MODE) {
            return redisService.getMap(mapName, keyName);
            //内存模式
        } else if (StorageComponent.MEMORY_MODE == STORAGE_MODE) {
            if (RedisConstant.FULL_HOUSE_PLAN_DECORATE_PRICE_DATA.equals(mapName)){
                return fullHousePlanDecoratePriceMap.get(keyName);
            }
            if (RedisConstant.RECOMMENDED_PLAN_DECORATE_PRICE_DATA.equals(mapName)){
                return recommenderDecoratePriceMap.get(mapName);
            }
        }
        return null;
    }

    //更新数据
    public void updateData() {
        //获取数据
        log.info(CLASS_LOG_PREFIX + "开始获取装修报价元数据....");

        //公司元数据
        List<DecoratePricePo> decoratePricePoList;
        try {
            decoratePricePoList = metaDataService.queryPlanDecoratePriceMetaData();
        } catch (MetaDataException e) {
            log.error(CLASS_LOG_PREFIX + "获取方案报价元数据失败: MetaDataException:{}", e);
            throw new NullPointerException(CLASS_LOG_PREFIX + "获取方案报价元数据失败,List<CompanyShopPo> is null.MetaDataException:" + e);
        }
        log.info(CLASS_LOG_PREFIX + "获取方案报价元数据完成,总条数:{}", (null == decoratePricePoList ? 0 : decoratePricePoList.size()));

        if (null == decoratePricePoList || 0 >= decoratePricePoList.size()) {
            log.info(CLASS_LOG_PREFIX + "获取方案报价元数据为空，初始化获取方案报价元数据异常...");
            return;
        }

        //获取数据字典装修类型列表
        List<SystemDictionaryPo> dictionaryPoList = null;
        try {
            dictionaryPoList = systemDictionaryMetaDataStorage.querySystemDictionaryListByType(SystemDictionaryType.SYSTEM_DICTIONARY_TYPE_DECORATETYPE);
        } catch (Exception e) {
            log.error(CLASS_LOG_PREFIX + "获取缓存数据字典装修类型异常！e:{}", e);
            dictionaryPoList = null;
        }

        //公司对象
        Map<String, DecoratePriceData> tempFullHouseDecoratePriceMap = new HashMap<>();
        Map<String, DecoratePriceData> tempRecommendedPlanDecoratePriceMap = new HashMap<>();
        List<SystemDictionaryPo> finalDictionaryPoList = dictionaryPoList;
        decoratePricePoList.forEach(decoratePricePo -> {
            String fullHouseId = decoratePricePo.getFullHouseId() + "";
            String planRecommendId = decoratePricePo.getPlanRecommendId() + "";
            Integer decoratePriceType = decoratePricePo.getDecoratePriceType();
            Integer planType = decoratePricePo.getPlanType();
            //全屋报价
            if (!StringUtils.isEmpty(fullHouseId) && DesignPlanType.FULLHOUSE_TABLE_TYPE == planType && null != decoratePriceType) {
                //装修报价对象
                DecoratePriceData decoratePriceData = new DecoratePriceData();
                if (tempFullHouseDecoratePriceMap.containsKey(fullHouseId)){
                    decoratePriceData = tempFullHouseDecoratePriceMap.get(fullHouseId);
                }
                switch (decoratePricePo.getDecoratePriceType()) {
                    case SystemDictionaryType.SYSTEM_DICTIONARY_DECORATETYPE_VALUE_ONE :
                        decoratePriceData.setDecorateHalfPack(decoratePricePo);
                        decoratePriceData.getDecorateHalfPack().setDecorateTypeName(getDecorateTypeName(finalDictionaryPoList, decoratePriceData.getDecorateHalfPack().getDecoratePriceType()));
                        break;
                    case SystemDictionaryType.SYSTEM_DICTIONARY_DECORATETYPE_VALUE_TWO :
                        decoratePriceData.setDecorateAllPack(decoratePricePo);
                        decoratePriceData.getDecorateAllPack().setDecorateTypeName(getDecorateTypeName(finalDictionaryPoList, decoratePriceData.getDecorateAllPack().getDecoratePriceType()));
                        break;
                    case SystemDictionaryType.SYSTEM_DICTIONARY_DECORATETYPE_VALUE_THREE :
                        decoratePriceData.setDecoratePackSoft(decoratePricePo);
                        decoratePriceData.getDecoratePackSoft().setDecorateTypeName(getDecorateTypeName(finalDictionaryPoList, decoratePriceData.getDecoratePackSoft().getDecoratePriceType()));
                        break;
                    case SystemDictionaryType.SYSTEM_DICTIONARY_DECORATETYPE_VALUE_FOUR :
                        decoratePriceData.setDecorateWater(decoratePricePo);
                        decoratePriceData.getDecorateWater().setDecorateTypeName(getDecorateTypeName(finalDictionaryPoList, decoratePriceData.getDecorateWater().getDecoratePriceType()));
                        break;
                    default:
                        log.info(CLASS_LOG_PREFIX + "无法匹配装修报价类型, fullHouseId:{}, decoratePriceType:{}", fullHouseId, decoratePriceType);
                }
                tempFullHouseDecoratePriceMap.put(fullHouseId, decoratePriceData);
            }
            //推荐方案报价
            if (!StringUtils.isEmpty(planRecommendId) && DesignPlanType.RECOMMENDED_TABLE_TYPE == planType && null != decoratePriceType) {
                //装修报价对象
                DecoratePriceData decoratePriceData = new DecoratePriceData();
                if (tempRecommendedPlanDecoratePriceMap.containsKey(planRecommendId)){
                    decoratePriceData = tempRecommendedPlanDecoratePriceMap.get(planRecommendId);
                }
                switch (decoratePriceType) {
                    case SystemDictionaryType.SYSTEM_DICTIONARY_DECORATETYPE_VALUE_ONE :
                        decoratePriceData.setDecorateHalfPack(decoratePricePo);
                        decoratePriceData.getDecorateHalfPack().setDecorateTypeName(getDecorateTypeName(finalDictionaryPoList, decoratePriceData.getDecorateHalfPack().getDecoratePriceType()));
                        break;
                    case SystemDictionaryType.SYSTEM_DICTIONARY_DECORATETYPE_VALUE_TWO :
                        decoratePriceData.setDecorateAllPack(decoratePricePo);
                        decoratePriceData.getDecorateAllPack().setDecorateTypeName(getDecorateTypeName(finalDictionaryPoList, decoratePriceData.getDecorateAllPack().getDecoratePriceType()));
                        break;
                    case SystemDictionaryType.SYSTEM_DICTIONARY_DECORATETYPE_VALUE_THREE :
                        decoratePriceData.setDecoratePackSoft(decoratePricePo);
                        decoratePriceData.getDecoratePackSoft().setDecorateTypeName(getDecorateTypeName(finalDictionaryPoList, decoratePriceData.getDecoratePackSoft().getDecoratePriceType()));
                        break;
                    case SystemDictionaryType.SYSTEM_DICTIONARY_DECORATETYPE_VALUE_FOUR :
                        decoratePriceData.setDecorateWater(decoratePricePo);
                        decoratePriceData.getDecorateWater().setDecorateTypeName(getDecorateTypeName(finalDictionaryPoList, decoratePriceData.getDecorateWater().getDecoratePriceType()));
                        break;
                    default:
                        log.info(CLASS_LOG_PREFIX + "无法匹配装修报价类型, planRecommendId:{}, decoratePriceType:{}", planRecommendId, decoratePriceType);
                }
                tempRecommendedPlanDecoratePriceMap.put(planRecommendId, decoratePriceData);
            }
        });
        log.info(CLASS_LOG_PREFIX + "格式化方案报价元数据完成....");

        //转换为Json数据
        Map<String, String> fullHouseDecoratePriceJsonMap = new HashMap<>(tempFullHouseDecoratePriceMap.size());
        Map<String, String> recommendedPlanDecoratePriceJsonMap = new HashMap<>(tempRecommendedPlanDecoratePriceMap.size());
        tempFullHouseDecoratePriceMap.forEach((k, v) -> fullHouseDecoratePriceJsonMap.put(k, JsonUtil.toJson(v)));
        tempRecommendedPlanDecoratePriceMap.forEach((k, v) -> recommendedPlanDecoratePriceJsonMap.put(k, JsonUtil.toJson(v)));
        log.info(CLASS_LOG_PREFIX + "格式化Json方案报价元数据完成....");

        //装载缓存
        redisService.addMapCompatible(RedisConstant.FULL_HOUSE_PLAN_DECORATE_PRICE_DATA, fullHouseDecoratePriceJsonMap);
        redisService.addMapCompatible(RedisConstant.RECOMMENDED_PLAN_DECORATE_PRICE_DATA, recommendedPlanDecoratePriceJsonMap);
        log.info(CLASS_LOG_PREFIX + "方案报价元数据装载缓存完成....");

        //内存模式
        if (StorageComponent.MEMORY_MODE == STORAGE_MODE) {
            fullHousePlanDecoratePriceMap = fullHouseDecoratePriceJsonMap;
            recommenderDecoratePriceMap = recommendedPlanDecoratePriceJsonMap;
            log.info(CLASS_LOG_PREFIX + "方案报价元数据装载内存完成....");
        }
    }

    /**
     * 根据Id类型查询数据库装修报价
     * @param planId
     * @param type
     * @return
     */
    public DecoratePriceData selectDecoratePriceByIdData(Integer planId, int type) {

        List<DecoratePricePo> decoratePricePoList;
        try {
            decoratePricePoList = metaDataService.queryPlanDecoratePriceByIdsMetaData(planId, type);
        } catch (MetaDataException e) {
            log.error(CLASS_LOG_PREFIX + "获取方案报价元数据失败: planId:{}, MetaDataException:{}", planId, e);
            throw new NullPointerException(CLASS_LOG_PREFIX + "获取方案报价元数据失败,List<CompanyShopPo> is null.MetaDataException:" + e);
        }

        if (null == decoratePricePoList || 0 >= decoratePricePoList.size()) {
            //log.info(CLASS_LOG_PREFIX + "获取方案报价元数据为空, planId = " + planId + ",type = " + type);
            return null;
        }

        //获取数据字典装修类型列表
        List<SystemDictionaryPo> dictionaryPoList = null;
        try {
            dictionaryPoList = systemDictionaryMetaDataStorage.querySystemDictionaryListByType(SystemDictionaryType.SYSTEM_DICTIONARY_TYPE_DECORATETYPE);
        } catch (Exception e) {
            log.error(CLASS_LOG_PREFIX + "获取缓存数据字典装修类型异常！e:{}", e);
            dictionaryPoList = null;
        }

        //装修报价对象
        DecoratePriceData decoratePriceData = new DecoratePriceData();
        List<SystemDictionaryPo> finalDictionaryPoList = dictionaryPoList;
        decoratePricePoList.forEach(decoratePricePo -> {

            switch (decoratePricePo.getDecoratePriceType()) {
                case SystemDictionaryType.SYSTEM_DICTIONARY_DECORATETYPE_VALUE_ONE :
                    decoratePriceData.setDecorateHalfPack(decoratePricePo);
                    decoratePriceData.getDecorateHalfPack().setDecorateTypeName(getDecorateTypeName(finalDictionaryPoList, decoratePriceData.getDecorateHalfPack().getDecoratePriceType()));
                    break;
                case SystemDictionaryType.SYSTEM_DICTIONARY_DECORATETYPE_VALUE_TWO :
                    decoratePriceData.setDecorateAllPack(decoratePricePo);
                    decoratePriceData.getDecorateAllPack().setDecorateTypeName(getDecorateTypeName(finalDictionaryPoList, decoratePriceData.getDecorateAllPack().getDecoratePriceType()));
                    break;
                case SystemDictionaryType.SYSTEM_DICTIONARY_DECORATETYPE_VALUE_THREE :
                    decoratePriceData.setDecoratePackSoft(decoratePricePo);
                    decoratePriceData.getDecoratePackSoft().setDecorateTypeName(getDecorateTypeName(finalDictionaryPoList, decoratePriceData.getDecoratePackSoft().getDecoratePriceType()));
                    break;
                case SystemDictionaryType.SYSTEM_DICTIONARY_DECORATETYPE_VALUE_FOUR :
                    decoratePriceData.setDecorateWater(decoratePricePo);
                    decoratePriceData.getDecorateWater().setDecorateTypeName(getDecorateTypeName(finalDictionaryPoList, decoratePriceData.getDecorateWater().getDecoratePriceType()));
                    break;
                default:
                    log.info(CLASS_LOG_PREFIX + "无法匹配装修报价类型, planId:{}, type:{}", planId, type);
            }
        });

        return decoratePriceData;
    }


    /**
     * 获取装修类型名称
     * @param dictionaryPoList
     * @param decorateTypeValue
     * @return
     */
    private String getDecorateTypeName(List<SystemDictionaryPo> dictionaryPoList, Integer decorateTypeValue) {
        if (null == decorateTypeValue) {
            return "";
        }
        if (null != dictionaryPoList && 0 < dictionaryPoList.size()) {
            for (SystemDictionaryPo dictionaryPo : dictionaryPoList) {
                if (dictionaryPo.getDictionaryValue() == decorateTypeValue) {
                    return dictionaryPo.getDictionaryName();
                }
            }
        } else {
            switch (decorateTypeValue) {
                case SystemDictionaryType.SYSTEM_DICTIONARY_DECORATETYPE_VALUE_ONE :
                    return SystemDictionaryType.SYSTEM_DICTIONARY_DECORATETYPE_NAME_HALFPACK;
                case SystemDictionaryType.SYSTEM_DICTIONARY_DECORATETYPE_VALUE_TWO :
                    return SystemDictionaryType.SYSTEM_DICTIONARY_DECORATETYPE_NAME_ALLPACK;
                case SystemDictionaryType.SYSTEM_DICTIONARY_DECORATETYPE_VALUE_THREE :
                    return SystemDictionaryType.SYSTEM_DICTIONARY_DECORATETYPE_NAME_PACKSOFT;
                case SystemDictionaryType.SYSTEM_DICTIONARY_DECORATETYPE_VALUE_FOUR :
                    return SystemDictionaryType.SYSTEM_DICTIONARY_DECORATETYPE_NAME_WATER;
                default:
                    log.info(CLASS_LOG_PREFIX + "无法匹配装修报价类型, decorateTypeValue:{}", decorateTypeValue);
            }
        }

        return "";
    }

    /**
     * 根据全屋方案ID获取方案报价
     *
     */
    public DecoratePriceData getDecoratePriceDataByFullHouseId(Integer fullHouseId) {
        if (null == fullHouseId || 0 >= fullHouseId) {
            return null;
        }
        String decoratePriceStr = getMap(RedisConstant.FULL_HOUSE_PLAN_DECORATE_PRICE_DATA, fullHouseId + "");

        if (StringUtils.isEmpty(decoratePriceStr)) {
            return null;
        }

        return JsonUtil.fromJson(decoratePriceStr, DecoratePriceData.class);
    }

    /**
     * 根据推荐方案ID获取方案报价
     *
     */
    public DecoratePriceData getDecoratePriceDataByRecommendedPlanId(Integer recommendedPlanId) {
        if (null == recommendedPlanId || 0 >= recommendedPlanId) {
            return null;
        }
        String decoratePriceStr = getMap(RedisConstant.RECOMMENDED_PLAN_DECORATE_PRICE_DATA, recommendedPlanId + "");

        if (StringUtils.isEmpty(decoratePriceStr)) {
            return null;
        }

        return JsonUtil.fromJson(decoratePriceStr, DecoratePriceData.class);
    }
}
