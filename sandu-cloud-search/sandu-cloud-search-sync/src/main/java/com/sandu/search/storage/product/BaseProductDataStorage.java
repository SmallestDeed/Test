package com.sandu.search.storage.product;

import com.google.gson.reflect.TypeToken;
import com.sandu.search.common.constant.RedisConstant;
import com.sandu.search.common.tools.JsonUtil;
import com.sandu.search.entity.elasticsearch.po.metadate.BaseProductDataPo;
import com.sandu.search.exception.MetaDataException;
import com.sandu.search.service.metadata.MetaDataService;
import com.sandu.search.service.redis.RedisService;
import com.sandu.search.storage.StorageComponent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 产品属性元数据存储
 *
 * @date 20180301
 * @auth pengxuangang
 */
@Slf4j
@Component
public class BaseProductDataStorage {

    private final static String CLASS_LOG_PREFIX = "基础产品元数据存储:";
    //默认缓存模式
    private static Integer STORAGE_MODE = StorageComponent.CACHE_MODE;

    private final RedisService redisService;
    private final MetaDataService metaDataService;

    @Autowired
    public BaseProductDataStorage(RedisService redisService, MetaDataService metaDataService) {
        this.redisService = redisService;
        this.metaDataService = metaDataService;
    }

    //产品元数据map
    private static Map<String, String> baseProductDataMap = null;


    //切换存储模式
    public void changeStorageMode(Integer storageMode) {
        //缓存模式
        if (StorageComponent.CACHE_MODE == storageMode) {
            //清空内存占用
            baseProductDataMap = null;
            //切换
            STORAGE_MODE = storageMode;
            //内存模式
        } else if(StorageComponent.MEMORY_MODE == storageMode) {
            //切换
            STORAGE_MODE = storageMode;
            //写入内存
            updateData();
        }
        log.info(CLASS_LOG_PREFIX + "基础产品存储模式切换成功，当前存储:{}.", StorageComponent.CACHE_MODE == STORAGE_MODE ? "缓存" : "内存");
    }

    //获取Map数据方法兼容
    private String getMap(String mapName, String keyName) {
        //缓存模式
        if (StorageComponent.CACHE_MODE == STORAGE_MODE) {
            return redisService.getMap(mapName, keyName);
            //内存模式
        } else if (StorageComponent.MEMORY_MODE == STORAGE_MODE) {
            if (RedisConstant.BASE_PRODUCT_MAP.equals(mapName)) {
                return baseProductDataMap.get(keyName);
            } else if (RedisConstant.BASE_PRODUCT_MAP.equals(mapName)) {
                return baseProductDataMap.get(keyName);
            }
        }
        return null;
    }

    //更新数据
    public void updateData() {
        log.info(CLASS_LOG_PREFIX + "开始获取基础产品元数据....");
        //基础产品元数据
        List<BaseProductDataPo> baseProductDataList;
        try {
            baseProductDataList = metaDataService.queryBaseProductData();
        } catch (MetaDataException e) {
            log.error(CLASS_LOG_PREFIX + "获取基础产品元数据失败: MetaDataException:{}", e);
            throw new NullPointerException(CLASS_LOG_PREFIX + "获取基础产品元数据失败,List<ProductAttributePo> is null.MetaDataException:" + e);
        }
        log.info(CLASS_LOG_PREFIX + "获取基础产品元数据完成,总条数:{}", (null == baseProductDataList ? 0 : baseProductDataList.size()));

        //临时对象
        Map<Integer, List<BaseProductDataPo>> tempBaseProductDataListMap = new HashMap<>(null == baseProductDataList ? 10 : baseProductDataList.size());


        //转换为Map元数据
        if (null != baseProductDataList && 0 != baseProductDataList.size()) {
            for(BaseProductDataPo baseProductDataPo : baseProductDataList){
                if(!tempBaseProductDataListMap.containsKey(baseProductDataPo.getSpuId())){
                    List<BaseProductDataPo> baseProductList = new ArrayList<>();
                    baseProductList.add(baseProductDataPo);
                    tempBaseProductDataListMap.put(baseProductDataPo.getSpuId(), baseProductList);
                }else{
                    List<BaseProductDataPo> baseProductList = tempBaseProductDataListMap.get(baseProductDataPo.getSpuId());
                    baseProductList.add(baseProductDataPo);
                    tempBaseProductDataListMap.put(baseProductDataPo.getSpuId(), baseProductList);
                }
            }
        }
        Map<String, String> newBaseProductDataListMap = new HashMap<>(tempBaseProductDataListMap.size());
        tempBaseProductDataListMap.forEach((k, v) -> newBaseProductDataListMap.put(k + "", JsonUtil.toJson(v)));
        log.info(CLASS_LOG_PREFIX + "格式化基础产品元数据完成....");

        //装载缓存
        redisService.addMapCompatible(RedisConstant.BASE_PRODUCT_MAP, newBaseProductDataListMap);
        log.info(CLASS_LOG_PREFIX + "产品基础元数据装载缓存完成....");

        //内存模式
        if (StorageComponent.MEMORY_MODE == STORAGE_MODE) {
            baseProductDataMap = newBaseProductDataListMap;
            log.info(CLASS_LOG_PREFIX + "内存载入产品基础元数据完成....");
        }
    }


    public  List<BaseProductDataPo> getBaseProductListBySpuId(Integer id) {
        if( null == id || 0 == id ){
            return null;
        }

        String baseProductDataStr = getMap(RedisConstant.BASE_PRODUCT_MAP, id + "");

        if(StringUtils.isEmpty(baseProductDataStr)){
            return null;
        }
        List<BaseProductDataPo> baseProductDataPoList = JsonUtil.fromJson(baseProductDataStr, new TypeToken<List<BaseProductDataPo>>(){}.getType());

        return baseProductDataPoList;
    }



    public List<BaseProductDataPo> queryBaseProductDataListById(Integer spuId) {
        log.info(CLASS_LOG_PREFIX + "开始获取产品数据....");
        //基础产品元数据
        List<BaseProductDataPo> baseProductDataList;
        try {
            baseProductDataList = metaDataService.queryBaseProductDataById(spuId);
        } catch (MetaDataException e) {
            log.error(CLASS_LOG_PREFIX + "开始获取产品数据失败: MetaDataException:{}", e);
            throw new NullPointerException(CLASS_LOG_PREFIX + "开始获取产品数据失败,List<ProductAttributePo> is null.MetaDataException:" + e);
        }
        log.info(CLASS_LOG_PREFIX + "获取基础产品元数据完成,总条数:{}", (null == baseProductDataList ? 0 : baseProductDataList.size()));

        return baseProductDataList;
    }

}
