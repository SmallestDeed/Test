package com.sandu.search.storage.groupproduct;

import com.google.gson.reflect.TypeToken;
import com.sandu.search.common.constant.RedisConstant;
import com.sandu.search.common.tools.JsonUtil;
import com.sandu.search.entity.elasticsearch.po.metadate.ProductGroupRelPo;
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
 * 产品组合关系元数据存储
 *
 * @date 20171225
 * @auth pengxuangang
 */
@Slf4j
@Component
public class GroupProductRelMetaDataStorage {

    private final static String CLASS_LOG_PREFIX = "产品组合关系元数据存储:";

    // 默认缓存模式
    private static Integer STORAGE_MODE = StorageComponent.CACHE_MODE;

    //产品组合关系元数据map<groupId, List<ProductGroupRelPo>>>
    private static Map<Integer, List<ProductGroupRelPo>> productGroupRelMap = new HashMap<>();
    //元数据服务
    private final MetaDataService metaDataService;
    private final RedisService redisService;

    @Autowired
    public GroupProductRelMetaDataStorage(MetaDataService metaDataService,
                                          RedisService redisService) {
        this.metaDataService = metaDataService;
        this.redisService = redisService;
    }

    // 切换存储模式
    public void changeStorageMode(Integer storageMode) {
        if (StorageComponent.CACHE_MODE == storageMode) {
            // 清空内存占用
            productGroupRelMap = null;
            // 切换存储模式
            STORAGE_MODE = storageMode;
        } else if (StorageComponent.MEMORY_MODE == storageMode) {
            // 切换存储模式
            STORAGE_MODE = storageMode;
            // 更新内存数据
            updateData();
        }
    }

    /**
     * 更新数据
     * @param
     */
    public void updateData() {

        //若无强制更新则更新前判断是否已有数据
        // if (!isEnforceLoad) {
        //     if (null != productGroupRelMap && 0 < productGroupRelMap.size()) {
        //         return;
        //     }
        // }

        //产品组合关系元数据
        List<ProductGroupRelPo> productGroupRelList;
        try {
            //获取数据
            log.info(CLASS_LOG_PREFIX + "开始获取产品组合关系元数据....");
            productGroupRelList = metaDataService.queryProductGroupRelMetaData();
            log.info(CLASS_LOG_PREFIX + "获取产品组合关系元数据完成,总条数:{}", (null == productGroupRelList ? 0 : productGroupRelList.size()));
        } catch (MetaDataException e) {
            log.error(CLASS_LOG_PREFIX + "获取产品组合关系元数据失败: MetaDataException:{}", e);
            throw new NullPointerException(CLASS_LOG_PREFIX + "获取产品组合关系元数据失败,List<ProductStylePo> is null.MetaDataException:" + e);
        }

        //临时对象
        Map<Integer, List<ProductGroupRelPo>> tempProductGroupRelMap = new HashMap<>();

        //转换为Map元数据
        if (null != productGroupRelList && 0 != productGroupRelList.size()) {
            productGroupRelList.forEach(productGroupRel -> {
                //产品组合ID
                int productGroupId = productGroupRel.getProductGroupId();
                if (0 != productGroupId) {
                    if (tempProductGroupRelMap.containsKey(productGroupId)) {
                        // 这里必须先创建arrayList对象才能使用add方法，list的add方法会抛异常
                        ArrayList<ProductGroupRelPo> productGroupRelPoList = new ArrayList<>();
                        List<ProductGroupRelPo> productGroupRelPos = tempProductGroupRelMap.get(productGroupId);
                        productGroupRelPoList.addAll(productGroupRelPos);
                        productGroupRelPoList.add(productGroupRel);
                        tempProductGroupRelMap.put(productGroupId, productGroupRelPoList);
                    } else {
                        tempProductGroupRelMap.put(productGroupRel.getProductGroupId(), Collections.singletonList(productGroupRel));
                    }
                }
            });

            //装回对象
            productGroupRelMap = null;
            productGroupRelMap = tempProductGroupRelMap;
            log.info(CLASS_LOG_PREFIX + "产品组合关联元数据装载内存完成....");

            // 转换为json数据
            Map<String,String> productGroupRelJsonMap = new HashMap<>();
            tempProductGroupRelMap.forEach((k,v) -> productGroupRelJsonMap.put(k+"", JsonUtil.toJson(v)));
            // 存入缓存
            redisService.addMapCompatible(RedisConstant.GROUP_PRODUCT_REL_DATA,productGroupRelJsonMap);
            log.info(CLASS_LOG_PREFIX + "产品组合关联元数据装载缓存完成....");
        }

    }

    /**
     * 根据组合产品id获取主产品id
     * @param groupId
     * @return
     */
    public Integer getMainProductIdByGroupId(Integer groupId) {
        if (groupId == null || groupId == 0) {
            return null;
        }

        List<ProductGroupRelPo> groupProductRelPoList = getGroupProductRelPoList(groupId);
        if (groupProductRelPoList == null || groupProductRelPoList.size() <= 0) {
            return null;
        }

        for (ProductGroupRelPo productGroupRelPo : groupProductRelPoList) {
            if (productGroupRelPo.getProductIsMain() == 1) {
                return productGroupRelPo.getProductId();
            }
        }
        return null;
    }

    /**
     * 根据组合id获取组合产品关联数据
     * @param groupId
     * @return
     */
    public List<ProductGroupRelPo> getGroupProductRelPoList(Integer groupId) {
        if (groupId == null || groupId == 0) {
            return null;
        }

        if (StorageComponent.MEMORY_MODE == STORAGE_MODE && null != productGroupRelMap && productGroupRelMap.size() > 0) {
            return productGroupRelMap.get(groupId);
        } else {
            String listStr = redisService.getMap(RedisConstant.GROUP_PRODUCT_REL_DATA, groupId + "");

            if (StringUtils.isEmpty(listStr)) {
                return null;
            }
            return JsonUtil.fromJson(listStr, new TypeToken<List<ProductGroupRelPo>>() {
            }.getType());
        }
    }

    /**
     * 根据组合id获取关联产品id集合
     * @param groupId
     * @return
     */
    public List<Integer> getProductIdListByGroupId(Integer groupId) {
        if (groupId == null || groupId == 0) {
            return null;
        }

        List<ProductGroupRelPo> groupProductRelPoList = getGroupProductRelPoList(groupId);
        if (groupProductRelPoList == null || groupProductRelPoList.size() <= 0) {
            return null;
        }

        List<Integer> productIdList = new ArrayList<>();
        for (ProductGroupRelPo productGroupRelPo : groupProductRelPoList) {
            productIdList.add(productGroupRelPo.getProductId());
        }
        return productIdList;
    }
}
