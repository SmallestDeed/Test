package com.sandu.search.storage.product;

import com.google.gson.reflect.TypeToken;
import com.sandu.search.common.constant.ProductAttributeTypeConstant;
import com.sandu.search.common.constant.RedisConstant;
import com.sandu.search.common.tools.JsonUtil;
import com.sandu.search.entity.elasticsearch.po.metadate.ProductAttributePo;
import com.sandu.search.exception.MetaDataException;
import com.sandu.search.service.metadata.MetaDataService;
import com.sandu.search.service.redis.RedisService;
import com.sandu.search.storage.StorageComponent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * 产品属性元数据存储
 *
 * @date 20180301
 * @auth pengxuangang
 */
@Slf4j
@Component
public class ProductAttributeMetaDataStorage {

    private final static String CLASS_LOG_PREFIX = "产品属性元数据存储:";
    //默认缓存模式
    private static Integer STORAGE_MODE = StorageComponent.CACHE_MODE;

    private final RedisService redisService;
    private final MetaDataService metaDataService;

    @Autowired
    public ProductAttributeMetaDataStorage(RedisService redisService, MetaDataService metaDataService) {
        this.redisService = redisService;
        this.metaDataService = metaDataService;
    }

    //过滤属性
    private static Map<String, String> productAttributeFilterMap = null;
    //排序属性
    private static Map<String, String> productAttributeOrderMap = null;
    //所有的属性
    private static Map<String, String> productAttributeMap = null;

    //切换存储模式
    public void changeStorageMode_old(Integer storageMode) {
        //缓存模式
        if (StorageComponent.CACHE_MODE == storageMode) {
            //清空内存占用
            productAttributeFilterMap = null;
            productAttributeOrderMap = null;
            productAttributeMap = null;
            //切换
            STORAGE_MODE = storageMode;
            //内存模式
        } else if(StorageComponent.MEMORY_MODE == storageMode) {
            //切换
            STORAGE_MODE = storageMode;
            //写入内存
            updateData_old();
            //productAttributeFilterMap = redisService.getMap(RedisConstant.PRODUCT_FITER_ATTR_DATA);
            //productAttributeOrderMap = redisService.getMap(RedisConstant.PRODUCT_ORDER_ATTR_DATA);
        }
        log.info(CLASS_LOG_PREFIX + "产品属性存储模式切换成功，当前存储:{}.", StorageComponent.CACHE_MODE == STORAGE_MODE ? "缓存" : "内存");
    }

    //获取Map数据方法兼容
    private String getMap_old(String mapName, String keyName) {
        //缓存模式
        if (StorageComponent.CACHE_MODE == STORAGE_MODE) {
            return redisService.getMap(mapName, keyName);
            //内存模式
        } else if (StorageComponent.MEMORY_MODE == STORAGE_MODE) {
            if (RedisConstant.PRODUCT_FITER_ATTR_DATA.equals(mapName)) {
                return productAttributeFilterMap.get(keyName);
            } else if (RedisConstant.PRODUCT_ORDER_ATTR_DATA.equals(mapName)) {
                return productAttributeOrderMap.get(keyName);
            }
        }
        return null;
    }

    //更新数据
    public void updateData_old() {
        log.info(CLASS_LOG_PREFIX + "开始获取产品属性元数据....");
        //产品属性元数据
        List<ProductAttributePo> productAttributePoList;
        try {
            productAttributePoList = metaDataService.queryProductAttrMetaData();
        } catch (MetaDataException e) {
            log.error(CLASS_LOG_PREFIX + "获取产品属性元数据失败: MetaDataException:{}", e);
            throw new NullPointerException(CLASS_LOG_PREFIX + "获取产品属性元数据失败,List<ProductAttributePo> is null.MetaDataException:" + e);
        }
        log.info(CLASS_LOG_PREFIX + "获取产品属性元数据完成,总条数:{}", (null == productAttributePoList ? 0 : productAttributePoList.size()));

        //临时对象
        Map<Integer, Map<String, String>> tempProductAttributeFilterMap = new HashMap<>();
        Map<Integer, Map<String, String>> tempProductAttributeOrderMap = new HashMap<>();
        Map<Integer, List<ProductAttributePo>> tempProductAttributeMap = new HashMap<>();

        //转换Map
        if (null != productAttributePoList && 0 != productAttributePoList.size()) {
            productAttributePoList.forEach(productAttributePo -> {
                if (null != productAttributePo) {
                    //产品ID
                    Integer productId = productAttributePo.getProductId();
                    //属性Map
                    Map<String, String> attributeMap = new HashMap<>();
                    //插入值
                    attributeMap.put(productAttributePo.getAttributeCode(), productAttributePo.getAttributeValue());
                    //过滤属性
                    if (ProductAttributeTypeConstant.PRODUCT_ATTRIBUTE_TYPE_FILTER.equals(productAttributePo.getAttributeType())) {
                        //更新属性
                        if (tempProductAttributeFilterMap.containsKey(productId)) {
                            //加入原对象
                            attributeMap.putAll(tempProductAttributeFilterMap.get(productId));
                        }
                        //装回对象
                        tempProductAttributeFilterMap.put(productId, attributeMap);
                    } else {
                        //除过滤属性类型的其他均为排序属性
                        //更新属性
                        if (tempProductAttributeOrderMap.containsKey(productId)) {
                            //加入原对象
                            attributeMap.putAll(tempProductAttributeOrderMap.get(productId));
                        }
                        //装回对象
                        tempProductAttributeOrderMap.put(productId, attributeMap);
                    }

                    if (tempProductAttributeMap.containsKey(productId)) {
                        List<ProductAttributePo> productAttributePos = tempProductAttributeMap.get(productId);
                        List<ProductAttributePo> tempProductAttributePoList = new ArrayList<>();
                        tempProductAttributePoList.addAll(productAttributePos);
                        tempProductAttributePoList.add(productAttributePo);
                        tempProductAttributeMap.put(productId, tempProductAttributePoList);
                    } else {
                        tempProductAttributeMap.put(productId, Collections.singletonList(productAttributePo));
                    }
                }

            });
        }
        log.info(CLASS_LOG_PREFIX + "格式化属性元数据完成....");

        //转换Json对象
        Map<String, String> productAttributeFilterJsonMap = new HashMap<>(tempProductAttributeFilterMap.size());
        Map<String, String> productAttributeOrderJsonMap = new HashMap<>(tempProductAttributeOrderMap.size());
        Map<String, String> productAttributeJsonMap = new HashMap<>();
        tempProductAttributeFilterMap.forEach((k, v) -> productAttributeFilterJsonMap.put(k + "", JsonUtil.toJson(v)));
        tempProductAttributeOrderMap.forEach((k, v) -> productAttributeOrderJsonMap.put(k + "", JsonUtil.toJson(v)));
        tempProductAttributeMap.forEach((k,v) -> productAttributeJsonMap.put(k+"",JsonUtil.toJson(v)));
        log.info(CLASS_LOG_PREFIX + "格式化属性Json元数据完成....");

        //装载缓存
        redisService.addMapCompatible(RedisConstant.PRODUCT_FITER_ATTR_DATA, productAttributeFilterJsonMap);
        redisService.addMapCompatible(RedisConstant.PRODUCT_ORDER_ATTR_DATA, productAttributeOrderJsonMap);
        redisService.addMapCompatible(RedisConstant.GROUP_PRODUCT_ATTRIBUTE_DATA, productAttributeJsonMap);
        log.info(CLASS_LOG_PREFIX + "属性元数据装载缓存完成....");

        //内存模式
        if (StorageComponent.MEMORY_MODE == STORAGE_MODE) {
            productAttributeFilterMap = productAttributeFilterJsonMap;
            productAttributeOrderMap = productAttributeOrderJsonMap;
            productAttributeMap = productAttributeJsonMap;
            log.info(CLASS_LOG_PREFIX + "属性元数据装载内存完成....");
        }
    }

    /**
     * 获取产品过滤属性集合
     *
     * @param productId 产品ID
     * @return
     */
    public Map<String, String> getProductFilterAttributeMap_old(Integer productId) {
        if (null == productId || 0 >= productId) {
            return null;
        }

        String filterAttrMap = getMap_old(RedisConstant.PRODUCT_FITER_ATTR_DATA, productId + "");

        if (StringUtils.isEmpty(filterAttrMap)) {
            return null;
        }

        return JsonUtil.fromJson(filterAttrMap, new TypeToken<Map<String, String>>() {}.getType());
    }

    /**
     * 获取产品排序属性集合
     *
     * @param productId 产品ID
     * @return
     */
    public Map<String, String> getProductOrderAttributeMap_old(Integer productId) {
        if (null == productId || 0 >= productId) {
            return null;
        }

        String orderAttrMap = getMap_old(RedisConstant.PRODUCT_ORDER_ATTR_DATA, productId + "");

        if (StringUtils.isEmpty(orderAttrMap)) {
            return null;
        }

        return JsonUtil.fromJson(orderAttrMap, new TypeToken<Map<String, String>>() {}.getType());
    }

    /**
     * 根据产品id获取属性集合
     * @param productId
     * @return
     */
    public List<ProductAttributePo> getByProductId(Integer productId) {
        if (null == productId || productId <= 0) {
            return null;
        }

        String json = redisService.getMap(RedisConstant.GROUP_PRODUCT_ATTRIBUTE_DATA, productId + "");
        if (StringUtils.isEmpty(json)) {
            return null;
        }

        return JsonUtil.fromJson(json, new TypeToken<List<ProductAttributePo>>(){}.getType());
    }
}
