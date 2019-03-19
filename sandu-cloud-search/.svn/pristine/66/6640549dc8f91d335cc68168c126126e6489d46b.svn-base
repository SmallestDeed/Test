package com.sandu.search.storage.product;

import com.google.gson.reflect.TypeToken;
import com.sandu.search.common.constant.RedisConstant;
import com.sandu.search.common.tools.JsonUtil;
import com.sandu.search.entity.elasticsearch.po.metadate.ProductUsagePo;
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
 * 产品使用次数元数据存储
 *
 * @date 20171214
 * @auth pengxuangang
 */
@Slf4j
@Component
public class ProductUsageCountMetaDataStorage {

    private final static String CLASS_LOG_PREFIX = "产品使用次数元数据存储:";
    //默认缓存模式
    private static Integer STORAGE_MODE = StorageComponent.CACHE_MODE;

    private final RedisService redisService;
    private final MetaDataService metaDataService;

    @Autowired
    public ProductUsageCountMetaDataStorage(RedisService redisService, MetaDataService metaDataService) {
        this.redisService = redisService;
        this.metaDataService = metaDataService;
    }

    //产品使用次数元数据Map<productId,Map<userId, usageCount>>
    private static Map<String, String> productUsageCountMap = null;

    //切换存储模式
    public void changeStorageMode(Integer storageMode) {
        //缓存模式
        if (StorageComponent.CACHE_MODE == storageMode) {
            //清空内存占用
            productUsageCountMap = null;
            //切换
            STORAGE_MODE = storageMode;
            //内存模式
        } else if(StorageComponent.MEMORY_MODE == storageMode) {
            //切换
            STORAGE_MODE = storageMode;
            //写入内存
            updateData();
            //productUsageCountMap = redisService.getMap(RedisConstant.PRODUCT_USAGE_COUNT_DATA);
        }
        log.info(CLASS_LOG_PREFIX + "产品使用次数存储模式切换成功，当前存储:{}.", StorageComponent.CACHE_MODE == STORAGE_MODE ? "缓存" : "内存");
    }

    //获取Map数据方法兼容
    private String getMap(String mapName, String keyName) {
        //缓存模式
        if (StorageComponent.CACHE_MODE == STORAGE_MODE) {
            return redisService.getMap(mapName, keyName);
            //内存模式
        } else if (StorageComponent.MEMORY_MODE == STORAGE_MODE) {
            if (RedisConstant.PRODUCT_USAGE_COUNT_DATA.equals(mapName)) {
                return productUsageCountMap.get(keyName);
            }
        }
        return null;
    }

    /**
     * 改为只从内存中取数据
     * 
     * @author huangsongbo
     * @param mapName
     * @param keyName
     * @return
     */
    /*private String getMap(String mapName, String keyName) {
    	if(productUsageCountMap != null) {
    		return productUsageCountMap.get(keyName);
    	}else {
    		log.error(CLASS_LOG_PREFIX + "productUsageCountMap == null");
    		return null;
    	}
    }*/
    
    //更新数据
    public void updateData() {
        //产品使用次数元数据
        List<ProductUsagePo> productUsagePoList;
        try {
            //获取数据
            log.info(CLASS_LOG_PREFIX + "开始获取产品使用次数元数据....");
            productUsagePoList = metaDataService.queryProductUsageCountSatatistics();
            log.info(CLASS_LOG_PREFIX + "获取产品使用次数元数据完成,总条数:{}", (null == productUsagePoList ? 0 : productUsagePoList.size()));
        } catch (MetaDataException e) {
            log.error(CLASS_LOG_PREFIX + "获取产品使用次数元数据失败: MetaDataException:{}", e);
            throw new NullPointerException(CLASS_LOG_PREFIX + "获取产品使用次数元数据失败,List<ProductTexturePo> is null.MetaDataException:" + e);
        }

        //临时对象
        Map<Integer, Map<Integer, Integer>> tempProductUsageCountMap = new HashMap<>();

        //转换Map
        if (null != productUsagePoList && 0 != productUsagePoList.size()) {
            productUsagePoList.forEach(productUsagePo -> {
                //产品ID
                Integer productId = productUsagePo.getProductId();
                //用户ID
                int userId = productUsagePo.getUserId();
                //使用次数
                int productUsageCount = productUsagePo.getProductUsageCount();

                //用户产品使用次数集合
                Map<Integer, Integer> userUsageCountMap = new HashMap<>();
                userUsageCountMap.put(userId, productUsageCount);
                if (tempProductUsageCountMap.containsKey(productId)) {
                    userUsageCountMap.putAll(tempProductUsageCountMap.get(productId));
                }

                tempProductUsageCountMap.put(productId, userUsageCountMap);
            });
        }
        log.info(CLASS_LOG_PREFIX + "格式化产品使用次数元数据完成....");

        //数据转换
        Map<String, String> productUsageCountJsonMap = new HashMap<>(tempProductUsageCountMap.size());
        tempProductUsageCountMap.forEach((k, v) -> productUsageCountJsonMap.put(k + "", JsonUtil.toJson(v)));
        log.info(CLASS_LOG_PREFIX + "格式化产品使用次数Json元数据完成....");

        //装载缓存
        redisService.addMapCompatible(RedisConstant.PRODUCT_USAGE_COUNT_DATA, productUsageCountJsonMap);
        log.info(CLASS_LOG_PREFIX + "产品使用次数元数据装载缓存完成....");

        //内存模式
        if (StorageComponent.MEMORY_MODE == STORAGE_MODE) {
            productUsageCountMap = productUsageCountJsonMap;
            log.info(CLASS_LOG_PREFIX + "产品使用次数元数据装载内存完成....");
        }
    }

    /**
     * 获取产品使用次数元数据Map
     *
     * @return
     */
    public Map<Integer, Integer> getProductUsageCountMetaDataMap(Integer productId) {

        if (null == productId || 0 >= productId) {
            return null;
        }

        String mapStr = getMap(RedisConstant.PRODUCT_USAGE_COUNT_DATA, productId + "");
        if (StringUtils.isEmpty(mapStr)) {
            return null;
        }

        return JsonUtil.fromJson(mapStr, new TypeToken<Map<Integer, Integer>>() {}.getType());
    }
}
