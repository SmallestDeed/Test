package com.sandu.search.storage.groupproduct;

import com.google.gson.reflect.TypeToken;
import com.sandu.search.common.constant.RedisConstant;
import com.sandu.search.common.tools.JsonUtil;
import com.sandu.search.entity.elasticsearch.po.metadate.ProductGroupDetailPo;
import com.sandu.search.exception.MetaDataException;
import com.sandu.search.service.metadata.MetaDataService;
import com.sandu.search.service.redis.RedisService;
import com.sandu.search.storage.StorageComponent;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.*;

@Slf4j
@Component
public class GroupProductDetailsMetaDataStorage {

    private final String CLASS_LOG_PREFIX = "组合产品详细信息元数据";
    // 默认缓存模式
    private Integer STORAGE_MODE = StorageComponent.CACHE_MODE;

    private Map<String, String> groupProductDetailMap = new HashMap<>();
    // 元数据服务
    private final MetaDataService metaDataService;
    private final RedisService redisService;

    public GroupProductDetailsMetaDataStorage(MetaDataService metaDataService,
                                              RedisService redisService) {
        this.metaDataService = metaDataService;
        this.redisService = redisService;
    }

    // 切换存储模式
    public void changeStorageMode(Integer storageMode) {
        if (StorageComponent.CACHE_MODE == storageMode) {
            // 如果为缓存模式，切换存储模式，清空内存占用
            groupProductDetailMap = null;
            STORAGE_MODE = storageMode;
        } else if (StorageComponent.MEMORY_MODE == storageMode) {
            // 如果为内存模式，切换存储模式，更新内存数据
            STORAGE_MODE = storageMode;
            updateData();
        }
    }

    // 更新数据
    public void updateData() {
        List<ProductGroupDetailPo> productGroupDetailPoList;

        try {
            log.info(CLASS_LOG_PREFIX + "获取产品组合详细信息元数据");
            productGroupDetailPoList = metaDataService.queryProductGroupDetail();
        } catch (MetaDataException e) {
            log.error(CLASS_LOG_PREFIX + "获取产品组合详细信息元数据失败，exception:{}", e);
            throw new NullPointerException(CLASS_LOG_PREFIX + "获取产品组合详细信息元数据失败，exception:{}" + e);
        }

        if (null == productGroupDetailPoList || productGroupDetailPoList.size() <= 0) {
            log.error(CLASS_LOG_PREFIX + "获取产品组合详细信息元数据为空，请检查数据。。。");
            return;
        }
        log.info(CLASS_LOG_PREFIX + "获取产品组合详细信息元数据，总条数：{}", productGroupDetailPoList.size());

        Map<Integer, List<ProductGroupDetailPo>> tempProductGroupDetailPoMap = new HashMap<>();

        productGroupDetailPoList.forEach(productGroupDetailPo -> {
            Integer productGroupId = productGroupDetailPo.getProductGroupId();
            if (null != productGroupId && 0 < productGroupId) {
                if (tempProductGroupDetailPoMap.containsKey(productGroupId)) {
                    List<ProductGroupDetailPo> groupDetailPoList = tempProductGroupDetailPoMap.get(productGroupId);
                    List<ProductGroupDetailPo> groupDetailPos = new ArrayList<>();
                    groupDetailPos.addAll(groupDetailPoList);
                    groupDetailPos.add(productGroupDetailPo);
                    tempProductGroupDetailPoMap.put(productGroupId, groupDetailPos);
                } else {
                    tempProductGroupDetailPoMap.put(productGroupId, Collections.singletonList(productGroupDetailPo));
                }
            }
        });


        HashMap<String, String> jsonMap = new HashMap<>();
        tempProductGroupDetailPoMap.forEach((k, v) -> {
            jsonMap.put(k + "", JsonUtil.toJson(v));
        });

        // 装回对象
        groupProductDetailMap = null;
        groupProductDetailMap = jsonMap;
        log.info(CLASS_LOG_PREFIX + "产品组合详细信息元数据,内存装载完成。。。");

        redisService.addMapCompatible(RedisConstant.GROUP_PRODUCT_DETAIL_DATA, jsonMap);
        log.info(CLASS_LOG_PREFIX + "产品组合详细信息元数据,缓存装载完成。。。");
    }


    /**
     * 根据组合id查询组合产品详细信息
     *
     * @param groupId
     * @return
     */
    public List<ProductGroupDetailPo> getGroupProductDetailsList(Integer groupId) {
        if (null == groupId || 0 >= groupId) {
            return null;
        }

        String json;
        if (StorageComponent.MEMORY_MODE == STORAGE_MODE && null != groupProductDetailMap && 0 < groupProductDetailMap.size()) {
            json = groupProductDetailMap.get(groupId);
        } else {
            json = redisService.getMap(RedisConstant.GROUP_PRODUCT_DETAIL_DATA, groupId + "");
        }
        if (StringUtils.isEmpty(json)) {
            return null;
        }

        return JsonUtil.fromJson(json, new TypeToken<List<ProductGroupDetailPo>>() {
        }.getType());
    }
}
