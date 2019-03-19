package com.sandu.search.storage.company;

import com.google.gson.reflect.TypeToken;
import com.sandu.search.common.constant.RedisConstant;
import com.sandu.search.common.tools.JsonUtil;
import com.sandu.search.entity.elasticsearch.po.metadate.CompanyShopPo;
import com.sandu.search.exception.MetaDataException;
import com.sandu.search.service.metadata.MetaDataService;
import com.sandu.search.service.redis.RedisService;
import com.sandu.search.storage.StorageComponent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 店铺元数据存储
 *
 * @date 2018年9月12日 20:48
 * @auth zhangchengda
 */
@Slf4j
@Component
public class CompanyShopMetaDataStorage {
    private final static String CLASS_LOG_PREFIX = "店铺元数据存储:";

    //默认缓存模式
    private static Integer STORAGE_MODE = StorageComponent.CACHE_MODE;

    private final RedisService redisService;
    private final MetaDataService metaDataService;

    @Autowired
    public CompanyShopMetaDataStorage(RedisService redisService, MetaDataService metaDataService) {
        this.redisService = redisService;
        this.metaDataService = metaDataService;
    }

    private static Map<String, String> shopMap = null;
    private static Map<String, String> userCompanyShopMap = null;
    private static Map<String, String> companyShopMap = null;

    //切换存储模式
    public void changeStorageMode(Integer storageMode) {
        //缓存模式
        if (StorageComponent.CACHE_MODE == storageMode) {
            //清空内存占用
            companyShopMap = null;
            shopMap = null;
            userCompanyShopMap = null;
            //切换
            STORAGE_MODE = storageMode;
            //内存模式
        } else if(StorageComponent.MEMORY_MODE == storageMode) {
            //切换
            STORAGE_MODE = storageMode;
            //写入内存
            updateData();
        }
        log.info(CLASS_LOG_PREFIX + "店铺存储模式切换成功，当前存储:{}.", StorageComponent.CACHE_MODE == STORAGE_MODE ? "缓存" : "内存");
    }

    //获取Map数据方法兼容
    private String getMap(String mapName, String keyName) {
        //缓存模式
        if (StorageComponent.CACHE_MODE == STORAGE_MODE) {
            return redisService.getMap(mapName, keyName);
            //内存模式
        } else if (StorageComponent.MEMORY_MODE == STORAGE_MODE) {
            if (RedisConstant.COMPANY_SHOP_DATA.equals(mapName)){
                return companyShopMap.get(keyName);
            }
            if (RedisConstant.SHOP_DATA.equals(mapName)){
                return shopMap.get(keyName);
            }
            if (RedisConstant.USER_COMPANY_SHOP_DATA.equals(mapName)){
                return userCompanyShopMap.get(keyName);
            }
        }
        return null;
    }

    //更新数据
    public void updateData() {
        //获取数据
        log.info(CLASS_LOG_PREFIX + "开始获取店铺元数据....");

        //公司元数据
        List<CompanyShopPo> companyShopPoList;
        try {
            companyShopPoList = metaDataService.queryCompanyShopMetaData();
        } catch (MetaDataException e) {
            log.error(CLASS_LOG_PREFIX + "获取店铺元数据失败: MetaDataException:{}", e);
            throw new NullPointerException(CLASS_LOG_PREFIX + "获取店铺元数据失败,List<CompanyShopPo> is null.MetaDataException:" + e);
        }
        log.info(CLASS_LOG_PREFIX + "获取店铺元数据完成,总条数:{}", (null == companyShopPoList ? 0 : companyShopPoList.size()));

        if (null == companyShopPoList || 0 >= companyShopPoList.size()) {
            log.info(CLASS_LOG_PREFIX + "获取店铺元数据为空，初始化获取店铺元数据异常...");
            return;
        }

        //公司对象
        Map<String, List<CompanyShopPo>> tempCompanyShopMap = new HashMap<>();
        Map<String, List<CompanyShopPo>> tempUserCompanyShopMap = new HashMap<>();
        Map<String, CompanyShopPo> tempShopMap = new HashMap<>();
        companyShopPoList.forEach(companyShopPo -> {
            tempShopMap.put(companyShopPo.getId() + "", companyShopPo);
            if (tempUserCompanyShopMap.get(companyShopPo.getUserId()) == null){
                List<CompanyShopPo> shopList = new ArrayList<>();
                shopList.add(companyShopPo);
                tempUserCompanyShopMap.put(companyShopPo.getUserId() + "", shopList);
            }else {
                tempUserCompanyShopMap.get(companyShopPo.getUserId()).add(companyShopPo);
            }
            if (tempCompanyShopMap.get(companyShopPo.getCompanyId()) != null){
                List<CompanyShopPo> shopList = new ArrayList<>();
                shopList.add(companyShopPo);
                tempCompanyShopMap.put(companyShopPo.getCompanyId() + "", shopList);
            }else {
                tempCompanyShopMap.get(companyShopPo.getCompanyId()).add(companyShopPo);
            }
        });
        log.info(CLASS_LOG_PREFIX + "格式化店铺元数据完成....");

        //转换为Json数据
        Map<String, String> companyShopJsonMap = new HashMap<>(tempCompanyShopMap.size());
        Map<String, String> userCompanyShopJsonMap = new HashMap<>(tempUserCompanyShopMap.size());
        Map<String, String> shopJsonMap = new HashMap<>(tempShopMap.size());
        tempCompanyShopMap.forEach((k, v) -> companyShopJsonMap.put(k, JsonUtil.toJson(v)));
        tempUserCompanyShopMap.forEach((k, v) -> userCompanyShopJsonMap.put(k, JsonUtil.toJson(v)));
        tempShopMap.forEach((k, v) -> shopJsonMap.put(k, JsonUtil.toJson(v)));
        log.info(CLASS_LOG_PREFIX + "格式化Json店铺元数据完成....");

        //装载缓存
        redisService.addMapCompatible(RedisConstant.COMPANY_SHOP_DATA, companyShopJsonMap);
        redisService.addMapCompatible(RedisConstant.SHOP_DATA, shopJsonMap);
        redisService.addMapCompatible(RedisConstant.USER_COMPANY_SHOP_DATA, userCompanyShopJsonMap);
        log.info(CLASS_LOG_PREFIX + "店铺元数据装载缓存完成....");

        //内存模式
        if (StorageComponent.MEMORY_MODE == STORAGE_MODE) {
            companyShopMap = companyShopJsonMap;
            shopMap = shopJsonMap;
            userCompanyShopMap = userCompanyShopJsonMap;
            log.info(CLASS_LOG_PREFIX + "店铺元数据装载内存完成....");
        }
    }

    /**
     * 根据公司ID获取公司店铺
     *
     * @date 2018/6/4
     * @auth pengxuangang
     * @mail xuangangpeng@gmail.com
     */
    public List<CompanyShopPo> getCompanyShopPoByCompanyId(Integer companyId) {
        if (null == companyId || 0 >= companyId) {
            return null;
        }
        List<CompanyShopPo> shopList = null;
        if (STORAGE_MODE == StorageComponent.CACHE_MODE){
            shopList = JsonUtil.fromJson(getMap(RedisConstant.COMPANY_SHOP_DATA, companyId + ""),
                    new TypeToken<List<CompanyShopPo>>(){}.getType());
        }else if (STORAGE_MODE == StorageComponent.MEMORY_MODE){
            shopList = JsonUtil.fromJson(companyShopMap.get(companyId),
                    new TypeToken<List<CompanyShopPo>>(){}.getType());
        }
        return shopList;
    }

    /**
     * 根据设计师ID获取店铺
     *
     * @date 2018/6/4
     * @auth pengxuangang
     * @mail xuangangpeng@gmail.com
     */
    public List<CompanyShopPo> getCompanyShopPoByUserId(Integer userId) {
        if (null == userId || 0 >= userId) {
            return null;
        }
        List<CompanyShopPo> shopList = null;
        if (STORAGE_MODE == StorageComponent.CACHE_MODE){
            shopList = JsonUtil.fromJson(getMap(RedisConstant.USER_COMPANY_SHOP_DATA, userId + ""),
                    new TypeToken<List<CompanyShopPo>>(){}.getType());
        }else if (STORAGE_MODE == StorageComponent.MEMORY_MODE){
            shopList = JsonUtil.fromJson(userCompanyShopMap.get(userId),
                    new TypeToken<List<CompanyShopPo>>(){}.getType());
        }
        return shopList;
    }
}
