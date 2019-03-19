package com.sandu.search.storage.design;

import com.google.gson.reflect.TypeToken;
import com.sandu.search.common.constant.RedisConstant;
import com.sandu.search.common.tools.JsonUtil;
import com.sandu.search.common.tools.StringUtil;
import com.sandu.search.entity.elasticsearch.po.metadate.CompanyShopPlanPo;
import com.sandu.search.exception.MetaDataException;
import com.sandu.search.service.metadata.MetaDataService;
import com.sandu.search.service.redis.RedisService;
import com.sandu.search.storage.StorageComponent;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 店铺方案元数据存储
 *
 * @date 2018/9/10
 * @auth xiaoxc
 */
@Slf4j
@Component
public class CompanyShopPlanMetaDataStorage {

    private final static String CLASS_LOG_PREFIX = "店铺方案元数据存储:";
    //默认缓存模式
    private static Integer STORAGE_MODE = StorageComponent.CACHE_MODE;

    private final RedisService redisService;
    private final MetaDataService metaDataService;

    @Autowired
    public CompanyShopPlanMetaDataStorage(RedisService redisService, MetaDataService metaDataService) {
        this.redisService = redisService;
        this.metaDataService = metaDataService;
    }

    private static Map<String, String> companyShopSinglePlansMap = null;

    private static Map<String, String> companyShopFullPlansMap = null;

    //切换存储模式
    public void changeStorageMode(Integer storageMode) {
        //缓存模式
        if (StorageComponent.CACHE_MODE == storageMode) {
            //清空内存占用
            companyShopSinglePlansMap = null;
            companyShopFullPlansMap = null;
            //切换
            STORAGE_MODE = storageMode;
            //内存模式
        } else if (StorageComponent.MEMORY_MODE == storageMode) {
            //切换
            STORAGE_MODE = storageMode;
            //写入内存
            updateData();
        }
        log.info(CLASS_LOG_PREFIX + "店铺方案存储模式切换成功，当前存储:{}.", StorageComponent.CACHE_MODE == STORAGE_MODE ? "缓存" : "内存");
    }

    //获取Map数据方法兼容
    private String getMap(String mapName, String keyName, int planTableType) {
        //缓存模式
        if (StorageComponent.CACHE_MODE == STORAGE_MODE) {
            return redisService.getMap(mapName, keyName);
            //内存模式
        } else if (StorageComponent.MEMORY_MODE == STORAGE_MODE) {
            if (planTableType == 2) {
                return companyShopFullPlansMap.get(keyName);
            } else {
                return companyShopSinglePlansMap.get(keyName);
            }

        }
        return null;
    }


    //更新数据
    public void updateData() {
        //获取数据
        log.info(CLASS_LOG_PREFIX + "开始获取店铺方案元数据存储....");

        //店铺方案元数据
        List<CompanyShopPlanPo> shopPlanList;
        try {
            shopPlanList = metaDataService.queryCompanyShopPlanMetaData();
        } catch (MetaDataException e) {
            log.error(CLASS_LOG_PREFIX + "获取店铺方案元数据失败: MetaDataException:{}", e);
            throw new NullPointerException(CLASS_LOG_PREFIX + "获取店铺方案元数据失败,List<CompanyShopPlanPo> is null.MetaDataException:" + e);
        }
        log.info(CLASS_LOG_PREFIX + "获取店铺方案元数据完成,总条数:{}", (null == shopPlanList ? 0 : shopPlanList.size()));

        if (null == shopPlanList || 0 >= shopPlanList.size()) {
            log.error(CLASS_LOG_PREFIX + "获取店铺方案元数据为空，请检查数据...");
            return;
        }

        //Map对象
        Map<String, List<CompanyShopPlanPo>> shopSinglePlanMap = new HashMap<>();
        Map<String, List<CompanyShopPlanPo>> shopFullPlanMap = new HashMap<>();
        //转换为Map元数据
        for (CompanyShopPlanPo companyShopPlanPo : shopPlanList) {
            if (null != companyShopPlanPo) {
                //店铺方案ID
                String planId = null != companyShopPlanPo.getPlanId() ? (companyShopPlanPo.getPlanId() + "") : null;
                List<CompanyShopPlanPo> shopPlanPos = new ArrayList<CompanyShopPlanPo>();
                shopPlanPos.add(companyShopPlanPo);
                if (!StringUtils.isEmpty(planId)) {
                    if (companyShopPlanPo.getPlanRecommendedType().intValue() == 3) {
                        if (shopFullPlanMap.containsKey(planId)) {
                            shopPlanPos.addAll(shopFullPlanMap.get(planId));
                        }
                        shopFullPlanMap.put(planId, shopPlanPos);
                    } else {
                        if (shopSinglePlanMap.containsKey(planId)) {
                            shopPlanPos.addAll(shopSinglePlanMap.get(planId));
                        }
                        shopSinglePlanMap.put(planId, shopPlanPos);
                    }
                }
            }
        }
        log.info(CLASS_LOG_PREFIX + "格式化店铺方案元数据完成.");

        //转换map
        Map<String, String> shopFullPlanJsonMap = new HashMap<>(shopFullPlanMap.size());
        Map<String, String> shopSinglePlanJsonMap = new HashMap<>(shopSinglePlanMap.size());
        shopFullPlanMap.forEach((k,v) -> shopFullPlanJsonMap.put(k, JsonUtil.toJson(v)));
        shopSinglePlanMap.forEach((k,v) -> shopSinglePlanJsonMap.put(k, JsonUtil.toJson(v)));
        //装载缓存
        redisService.del(RedisConstant.COMPANY_SHOP_RECOMMENDATION_FULL_PLAN_DATA);
        redisService.addMapCompatible(RedisConstant.COMPANY_SHOP_RECOMMENDATION_FULL_PLAN_DATA, shopFullPlanJsonMap);

        redisService.del(RedisConstant.COMPANY_SHOP_RECOMMENDATION_RECOMMEND_PLAN_DATA);
        redisService.addMapCompatible(RedisConstant.COMPANY_SHOP_RECOMMENDATION_RECOMMEND_PLAN_DATA, shopSinglePlanJsonMap);
        log.info(CLASS_LOG_PREFIX + "装载缓存店铺方案元数据完成.");

        if (StorageComponent.MEMORY_MODE == STORAGE_MODE) {
            companyShopSinglePlansMap = shopSinglePlanJsonMap;
            companyShopFullPlansMap = shopFullPlanJsonMap;
            log.info(CLASS_LOG_PREFIX + "装载内存店铺方案元数据完成.");
        }
    }

    /**
     * 根据方案ID获取子店铺Ids
     *
     * @param planId        店铺方案ID
     * @param planTableType 方案类型
     * @return
     */
    public List<CompanyShopPlanPo> getCompanyShopPlanPoById(Integer planId, int planTableType) {

        if (null == planId || 0 == planId) {
            return null;
        }
        List<CompanyShopPlanPo> shopPlanPoList = null;
        if (planTableType == 2) {
            String shopfullPlanJson = getMap(RedisConstant.COMPANY_SHOP_RECOMMENDATION_FULL_PLAN_DATA, planId.toString(), planTableType);
            if (StringUtils.isEmpty(shopfullPlanJson)) {
                return null;
            }
            shopPlanPoList = JsonUtil.fromJson(shopfullPlanJson, new TypeToken<List<CompanyShopPlanPo>>() {}.getType());
        } else {
            String shopSinglePlanJson = getMap(RedisConstant.COMPANY_SHOP_RECOMMENDATION_RECOMMEND_PLAN_DATA, planId.toString() + "", planTableType);
            if (StringUtils.isEmpty(shopSinglePlanJson)) {
                return null;
            }
            shopPlanPoList = JsonUtil.fromJson(shopSinglePlanJson, new TypeToken<List<CompanyShopPlanPo>>() {}.getType());
        }

        if (null == shopPlanPoList || shopPlanPoList.size() == 0) {
            return null;
        }

        for (CompanyShopPlanPo shopPlanPo : shopPlanPoList) {
            if (StringUtils.isEmpty(shopPlanPo.getShopPlatformValues())) {
                continue;
            }
            List<Integer> shopIdList = StringUtil.transformInteger(Arrays.asList(shopPlanPo.getShopPlatformValues().split(",")));
            //店铺平台类型转换list
            if (null != shopIdList && shopIdList.size() > 0) {
                shopPlanPo.setReleasePlatformList(StringUtil.transformInteger(Arrays.asList(shopPlanPo.getShopPlatformValues().split(","))));
            }
        }

        return shopPlanPoList;
    }

    /**
     * 根据方案ID查询店铺id列表
     *
     * @param planId
     * @return
     */
    public List<CompanyShopPlanPo> selectCompanyShopPoByPlanId(Integer planId) {

        //店铺方案元数据
        List<CompanyShopPlanPo> shopPlanPoList;
        try {
            shopPlanPoList = metaDataService.queryCompanyShopPlanByPlanIdMetaData(planId);
        } catch (MetaDataException e) {
            log.error(CLASS_LOG_PREFIX + "获取店铺方案元数据失败: planId:{},MetaDataException:{}", planId, e);
            throw new NullPointerException(CLASS_LOG_PREFIX + "获取店铺方案元数据失败,List<Integer> is null.MetaDataException:" + e);
        }

        if (null == shopPlanPoList || 0 >= shopPlanPoList.size()) {
            log.error(CLASS_LOG_PREFIX + "获取店铺方案元数据为空,planId = " + planId);
            return null;
        }

        for (CompanyShopPlanPo shopPlanPo : shopPlanPoList) {
            if (StringUtils.isEmpty(shopPlanPo.getShopPlatformValues())) {
                continue;
            }
            List<Integer> shopIdList = StringUtil.transformInteger(Arrays.asList(shopPlanPo.getShopPlatformValues().split(",")));
            //店铺平台类型转换list
            if (null != shopIdList && shopIdList.size() > 0) {
                shopPlanPo.setReleasePlatformList(StringUtil.transformInteger(Arrays.asList(shopPlanPo.getShopPlatformValues().split(","))));
            }
        }

        return shopPlanPoList;
    }

    public void updateDataByFullhousePlanIds(List<Integer> fullHousePlanIdList) {
        log.info(CLASS_LOG_PREFIX + "开始获取增量店铺方案元数据....");
        if (null == fullHousePlanIdList || fullHousePlanIdList.size() == 0) {
            return ;
        }
        //map对象
        Map<String, String> shopFullPlanMap = new HashMap<>();
        //方案平台元数据
        List<CompanyShopPlanPo> companyShopPlanPoList = null;
        try {
            companyShopPlanPoList = metaDataService.queryCompanyShopPlanByFullHouseIds(fullHousePlanIdList);
        } catch (MetaDataException e) {
            log.error(CLASS_LOG_PREFIX + "获取店铺全屋方案数据失败: MetaDataException:{}", e);
            throw new NullPointerException(CLASS_LOG_PREFIX + "获取店铺全屋方案数据失败,List<CompanyShopPlanPo> is null.MetaDataException:" + e);
        }
        log.info(CLASS_LOG_PREFIX + "获取店铺全屋方案元数据完成,总条数:{}", (null == companyShopPlanPoList ? 0 : companyShopPlanPoList.size()));

        //处理对象更新到缓存
        for (Integer fullhouseId : fullHousePlanIdList) {
            //是否删除KEY
            boolean deleted = true;
            for (CompanyShopPlanPo companyShopPlanPo : companyShopPlanPoList) {
                if (fullhouseId.equals(companyShopPlanPo.getPlanId())) {
                    //全屋方案ID
                    Integer planId = companyShopPlanPo.getPlanId();
                    //店铺ID
                    Integer shopId = companyShopPlanPo.getShopId();
                    if (planId != null && shopId != null) {
                        StringBuilder builder = new StringBuilder(shopId + "");
                        if (shopFullPlanMap.containsKey(planId)) {
                            builder.append("," + shopFullPlanMap.get(planId));
                        }
                        shopFullPlanMap.put(planId + "", builder.toString());
                    }
                    deleted = false;
                }
            }
            if (deleted) {
                redisService.delMap(RedisConstant.COMPANY_SHOP_RECOMMENDATION_FULL_PLAN_DATA, fullhouseId + "");
                if (STORAGE_MODE == StorageComponent.MEMORY_MODE) {
                    if (companyShopFullPlansMap != null) {
                        companyShopFullPlansMap.remove(fullhouseId);
                    }
                }
            }
        }
        redisService.addMap(RedisConstant.COMPANY_SHOP_RECOMMENDATION_FULL_PLAN_DATA, shopFullPlanMap);
        if (STORAGE_MODE == StorageComponent.MEMORY_MODE) {
            if (companyShopFullPlansMap == null) {
                companyShopFullPlansMap = shopFullPlanMap;
            } else {
                companyShopFullPlansMap.putAll(shopFullPlanMap);
            }
        }
        log.info(CLASS_LOG_PREFIX + "增量更新店铺方案元数据完成....，shopFullPlanMap:{}", JsonUtil.toJson(shopFullPlanMap));
    }
}
