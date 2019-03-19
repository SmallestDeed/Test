package com.sandu.search.storage.product;

import com.google.gson.reflect.TypeToken;
import com.sandu.search.common.constant.RedisConstant;
import com.sandu.search.common.tools.JsonUtil;
import com.sandu.search.entity.elasticsearch.po.ProductCategoryPo;
import com.sandu.search.entity.elasticsearch.po.ProductCategoryRelPo;
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
 * 产品分类关联元数据存储
 *
 * @date 20171219
 * @auth pengxuangang
 */
@Slf4j
@Component
public class ProductCategoryRelMetaDataStorage {

    private final static String CLASS_LOG_PREFIX = "产品分类关联元数据存储:";
    //默认缓存模式
    private static Integer STORAGE_MODE = StorageComponent.CACHE_MODE;

    private final RedisService redisService;
    private final MetaDataService metaDataService;

    @Autowired
    public ProductCategoryRelMetaDataStorage(RedisService redisService, MetaDataService metaDataService) {
        this.redisService = redisService;
        this.metaDataService = metaDataService;
    }

    //产品分类map元数据<productId,List<CategoryId>>
    private static Map<String, String> productCategoryRelMap = null;

    //切换存储模式
    public void changeStorageMode(Integer storageMode) {
        //缓存模式
        if (StorageComponent.CACHE_MODE == storageMode) {
            //清空内存占用
            productCategoryRelMap = null;
            //切换
            STORAGE_MODE = storageMode;
            //内存模式
        } else if(StorageComponent.MEMORY_MODE == storageMode) {
            //切换
            STORAGE_MODE = storageMode;
            //写入内存
            updateData();
            //productCategoryRelMap = redisService.getMap(RedisConstant.PRODUCT_CATEGORY_REL_DATA);
        }
        log.info(CLASS_LOG_PREFIX + "产品分类关联存储模式切换成功，当前存储:{}.", StorageComponent.CACHE_MODE == STORAGE_MODE ? "缓存" : "内存");
    }

    //获取Map数据方法兼容
    private String getMap(String mapName, String keyName) {
        //缓存模式
        if (StorageComponent.CACHE_MODE == STORAGE_MODE) {
            return redisService.getMap(mapName, keyName);
            //内存模式
        } else if (StorageComponent.MEMORY_MODE == STORAGE_MODE) {
            if (RedisConstant.PRODUCT_CATEGORY_REL_DATA.equals(mapName)) {
                return productCategoryRelMap.get(keyName);
            }
        }
        return null;
    }
    
    /**
     * 个人觉得,没必要存到缓存里面,因为更新完了就可以删除了
     * 
     * @author huangsongbo
     * @param mapName
     * @param keyName
     * @return
     */
    /*private String getMap(String mapName, String keyName) {
    	if(productCategoryRelMap != null) {
    		return productCategoryRelMap.get(keyName);
    	}else {
    		log.error(CLASS_LOG_PREFIX + "productCategoryRelMap = null");
    		return null;
    	}
    }*/

    //更新数据
    public void updateData() {

        log.info(CLASS_LOG_PREFIX + "开始获取产品分类关联元数据....");
        //产品分类关联元数据
        List<ProductCategoryRelPo> productCategoryRelList;
        try {
            productCategoryRelList = metaDataService.queryProductCategoryRelMetaData();
        } catch (MetaDataException e) {
            log.error(CLASS_LOG_PREFIX + "获取产品分类关联元数据失败: MetaDataException:{}", e);
            throw new NullPointerException(CLASS_LOG_PREFIX + "获取产品分类关联元数据失败,List<ProductCategoryRelPo> is null.MetaDataException:" + e);
        }
        log.info(CLASS_LOG_PREFIX + "获取产品分类关联元数据完成,总条数:{}", (null == productCategoryRelList ? 0 : productCategoryRelList.size()));

        //临时对象
        Map<Integer, List<Integer>> tempProductCategoryRelMap = new HashMap<>();
        //转换为Map元数据
        if (null != productCategoryRelList && 0 != productCategoryRelList.size()) {
            productCategoryRelList.forEach(productCategoryRelPo -> {
                if (tempProductCategoryRelMap.containsKey(productCategoryRelPo.getProductId())) {
                    List<Integer> categoryList = new ArrayList<>();
                    categoryList.addAll(tempProductCategoryRelMap.get(productCategoryRelPo.getProductId()));
                    categoryList.add(productCategoryRelPo.getCategoryId());
                    tempProductCategoryRelMap.put(productCategoryRelPo.getProductId(), categoryList);
                } else {
                    tempProductCategoryRelMap.put(productCategoryRelPo.getProductId(), Collections.singletonList(productCategoryRelPo.getCategoryId()));
                }
            });
        }
        log.info(CLASS_LOG_PREFIX + "格式化产品分类关联元数据完成....");

        //数据转换
        Map<String, String> productCategoryRelJsonMap = new HashMap<>(tempProductCategoryRelMap.size());
        tempProductCategoryRelMap.forEach((k, v) -> productCategoryRelJsonMap.put(k + "", JsonUtil.toJson(v)));
        log.info(CLASS_LOG_PREFIX + "格式化产品分类关联Json元数据完成....");

        //装载缓存
        redisService.addMapCompatible(RedisConstant.PRODUCT_CATEGORY_REL_DATA, productCategoryRelJsonMap);
        log.info(CLASS_LOG_PREFIX + "产品分类关联元数据装载缓存完成....");

        //内存模式
        if (StorageComponent.MEMORY_MODE == STORAGE_MODE) {
            productCategoryRelMap = productCategoryRelJsonMap;
            log.info(CLASS_LOG_PREFIX + "产品分类关联元数据装载内存完成....");
        }
    }

    /**
     * 根据产品ID查询产品分类List
     *
     * @param productId 产品ID
     * @return
     */
    public List<Integer> getCategoryListByProductId(Integer productId) {

        if (null == productId || 0 >= productId) {
            return null;
        }

        String categoryIdListStr = getMap(RedisConstant.PRODUCT_CATEGORY_REL_DATA, productId + "");
        if (StringUtils.isEmpty(categoryIdListStr)) {
            return null;
        }

        return JsonUtil.fromJson(categoryIdListStr, new TypeToken<List<Integer>>() {}.getType());
    }

    public List<ProductCategoryPo> queryCategoryListByProductId(int productId) {
        if (0 ==  productId) {
            return null;
        }
        log.info(CLASS_LOG_PREFIX+"根据产品ID查询产品所有分类数据开始:");
        List<ProductCategoryPo> productCategoryPoList;
        try {
            productCategoryPoList = metaDataService.queryProductCategoryByProductId(productId);
        }catch (Exception e){
            log.info(CLASS_LOG_PREFIX+"根据产品ID查询产品所有分类数据失败,productId"+productId);
            throw new NullPointerException(CLASS_LOG_PREFIX+"获取产品分类关联元数据失败,List<ProductCategoryRelPo> is null.MetaDataException:" + e);
        }
        log.info(CLASS_LOG_PREFIX+"根据产品ID查询产品所有分类数据完成:"+(null==productCategoryPoList ? 0 : productCategoryPoList.size())) ;


        return productCategoryPoList;
    }
}
