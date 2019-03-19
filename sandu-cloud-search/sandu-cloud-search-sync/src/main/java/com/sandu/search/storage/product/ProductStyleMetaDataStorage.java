package com.sandu.search.storage.product;

import com.google.gson.reflect.TypeToken;
import com.sandu.search.common.constant.RedisConstant;
import com.sandu.search.common.tools.JsonUtil;
import com.sandu.search.entity.elasticsearch.po.ProductStylePo;
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
 * 产品风格元数据存储
 *
 * @date 20171213
 * @auth pengxuangang
 */
@Slf4j
@Component
public class ProductStyleMetaDataStorage {

    private final static String CLASS_LOG_PREFIX = "产品风格元数据存储:";
    //默认缓存模式
    private static Integer STORAGE_MODE = StorageComponent.CACHE_MODE;

    private final RedisService redisService;
    private final MetaDataService metaDataService;

    @Autowired
    public ProductStyleMetaDataStorage(RedisService redisService, MetaDataService metaDataService) {
        this.redisService = redisService;
        this.metaDataService = metaDataService;
    }

    //产品风格map元数据
    private static Map<String, String> productStyleIdMap = null;
    //产品父类风格map元数据
    private static Map<String, String> productParentStyleIdMap = null;

    //切换存储模式
    public void changeStorageMode(Integer storageMode) {
        //缓存模式
        if (StorageComponent.CACHE_MODE == storageMode) {
            //清空内存占用
            productStyleIdMap = null;
            productParentStyleIdMap = null;
            //切换
            STORAGE_MODE = storageMode;
            //内存模式
        } else if(StorageComponent.MEMORY_MODE == storageMode) {
            //切换
            STORAGE_MODE = storageMode;
            //写入内存
            updateData();
            //productStyleIdMap = redisService.getMap(RedisConstant.PRODUCT_STYLE_DATA);
            //productParentStyleIdMap = redisService.getMap(RedisConstant.PRODUCT_PARENT_STYLE_DATA);
        }
        log.info(CLASS_LOG_PREFIX + "产品风格存储模式切换成功，当前存储:{}.", StorageComponent.CACHE_MODE == STORAGE_MODE ? "缓存" : "内存");
    }

    //获取Map数据方法兼容
    private String getMap(String mapName, String keyName) {
        //缓存模式
        if (StorageComponent.CACHE_MODE == STORAGE_MODE) {
            return redisService.getMap(mapName, keyName);
            //内存模式
        } else if (StorageComponent.MEMORY_MODE == STORAGE_MODE) {
            if (RedisConstant.PRODUCT_STYLE_DATA.equals(mapName)) {
                return productStyleIdMap.get(keyName);
            } else if (RedisConstant.PRODUCT_PARENT_STYLE_DATA.equals(mapName)) {
                return productParentStyleIdMap.get(keyName);
            }
        }
        return null;
    }


    //更新数据
    public void updateData() {
        log.info(CLASS_LOG_PREFIX + "开始获取产品风格元数据....");
        List<ProductStylePo> productStylePoList;
        try {
            productStylePoList = metaDataService.queryProductStyleMetaData();
        } catch (MetaDataException e) {
            log.error(CLASS_LOG_PREFIX + "获取产品风格元数据失败: MetaDataException:{}", e);
            throw new NullPointerException(CLASS_LOG_PREFIX + "获取产品风格元数据失败,List<ProductStylePo> is null.MetaDataException:" + e);
        }
        log.info(CLASS_LOG_PREFIX + "获取产品风格元数据完成,总条数:{}", (null == productStylePoList ? 0 : productStylePoList.size()));

        Map<String, String> tempProductStylePoMap = new HashMap<>();
        Map<String, List<Integer>> tempParentStylePoMap = new HashMap<>();
        //转换为Map元数据
        if (null != productStylePoList && 0 != productStylePoList.size()) {
            productStylePoList.forEach(productStylePo -> {
                //风格ID
                tempProductStylePoMap.put(productStylePo.getId() + "", productStylePo.getStyleName());
                //父类风格ID
                List<Integer> styleIdList = new ArrayList<>();
                styleIdList.add(productStylePo.getId());
                if (tempParentStylePoMap.containsKey(productStylePo.getParentStyleId() + "")) {
                    styleIdList.addAll(tempParentStylePoMap.get(productStylePo.getParentStyleId() + ""));
                }
                tempParentStylePoMap.put(productStylePo.getParentStyleId() + "", styleIdList);

            });
        }
        log.info(CLASS_LOG_PREFIX + "格式化产品风格元数据完成....");

        //数据转换
        Map<String, String> productParentStyleJsonMap = new HashMap<>(tempParentStylePoMap.size());
        tempParentStylePoMap.forEach((k, v) -> productParentStyleJsonMap.put(k, JsonUtil.toJson(v)));
        log.info(CLASS_LOG_PREFIX + "格式化产品风格Json元数据完成....");

        //装载缓存
        redisService.addMapCompatible(RedisConstant.PRODUCT_STYLE_DATA, tempProductStylePoMap);
        redisService.addMapCompatible(RedisConstant.PRODUCT_PARENT_STYLE_DATA, productParentStyleJsonMap);
        log.info(CLASS_LOG_PREFIX + "产品风格元数据装载缓存完成....");

        //内存模式
        if (StorageComponent.MEMORY_MODE == STORAGE_MODE) {
            productStyleIdMap = tempProductStylePoMap;
            productParentStyleIdMap = productParentStyleJsonMap;
            log.info(CLASS_LOG_PREFIX + "产品风格元数据装载内存完成....");
        }
    }

    /**
     * 根据风格ID获取产品风格名
     *
     * @return
     */
    public String getProductStyleNameById(int styleId) {
        if (0 == styleId) {
            return null;
        }
        return getMap(RedisConstant.PRODUCT_STYLE_DATA, styleId + "");
    }

    /**
     * 根据父风格ID查询所有子节点风格ID
     *
     * @param parentId
     * @return
     */
    public List<Integer> getProductStyleChildrenIdByParentId(Integer parentId) {

        if (null == parentId || 0 >= parentId) {
            return null;
        }

        String productParentStyleIdListStr = getMap(RedisConstant.PRODUCT_PARENT_STYLE_DATA, parentId + "");
        if (StringUtils.isEmpty(productParentStyleIdListStr)) {
            return null;
        }
        return JsonUtil.fromJson(productParentStyleIdListStr, new TypeToken<List<Integer>>() {}.getType());
    }
}
