package com.sandu.search.storage.product;

import com.google.gson.reflect.TypeToken;
import com.sandu.search.common.constant.RedisConstant;
import com.sandu.search.common.tools.JsonUtil;
import com.sandu.search.entity.elasticsearch.po.ProductCategoryPo;
import com.sandu.search.entity.product.vo.ProductCategoryVo;
import com.sandu.search.exception.MetaDataException;
import com.sandu.search.service.metadata.MetaDataService;
import com.sandu.search.service.redis.RedisService;
import com.sandu.search.storage.StorageComponent;
import com.sandu.search.storage.system.SystemDictionaryMetaDataStorage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 产品分类元数据存储
 *
 * @date 20171213
 * @auth pengxuangang
 */
@Slf4j
@Component
public class ProductCategoryMetaDataStorage {

    private final static String CLASS_LOG_PREFIX = "产品分类元数据存储:";

    //默认缓存模式
    private static Integer STORAGE_MODE = StorageComponent.CACHE_MODE;

    private final RedisService redisService;
    private final MetaDataService metaDataService;
    private final SystemDictionaryMetaDataStorage systemDictionaryMetaDataStorage;

    @Autowired
    public ProductCategoryMetaDataStorage(RedisService redisService, MetaDataService metaDataService, SystemDictionaryMetaDataStorage systemDictionaryMetaDataStorage) {
        this.redisService = redisService;
        this.metaDataService = metaDataService;
        this.systemDictionaryMetaDataStorage = systemDictionaryMetaDataStorage;
    }

    //产品分类Map元数据
    private static Map<String, String> productCategoryPoMap = null;
    //产品父子分类关系Map元数据<parentCategoryId, List<childrenCategoryId>>
    private static Map<String, String> parentChildrenCategoryMap = null;
    //产品分类编码找产品分类ID
    private static Map<String, String> categoryCodeMap = null;
    //产品分类长编码
    private static List<String> categoryLongCodeList = null;

    //切换存储模式
    public void changeStorageMode(Integer storageMode) {
        //缓存模式
        if (StorageComponent.CACHE_MODE == storageMode) {
            //清空内存占用
            productCategoryPoMap = null;
            parentChildrenCategoryMap = null;
            categoryCodeMap = null;
            categoryLongCodeList = null;
            //切换
            STORAGE_MODE = storageMode;
            //内存模式
        } else if(StorageComponent.MEMORY_MODE == storageMode) {
            //切换
            STORAGE_MODE = storageMode;
            //写入内存
            updateData();
            //productCategoryPoMap = redisService.getMap(RedisConstant.PRODUCT_CATEGORY_DATA);
            //parentChildrenCategoryMap = redisService.getMap(RedisConstant.PRODUCT_PARENT_CHILD_CATEGORY_DATA);
            //categoryCodeMap = redisService.getMap(RedisConstant.PRODUCT_CATEGORY_CODE_AND_ID_DATA);
            //categoryLongCodeList = redisService.getList(RedisConstant.PRODUCT_CATEGORY_LONG_CODE_DATA);
        }
        log.info(CLASS_LOG_PREFIX + "产品分类存储模式切换成功，当前存储:{}.", StorageComponent.CACHE_MODE == STORAGE_MODE ? "缓存" : "内存");
    }

    //获取Map数据方法兼容
    private String getMap(String mapName, String keyName) {
        //缓存模式
        if (StorageComponent.CACHE_MODE == STORAGE_MODE) {
            return redisService.getMap(mapName, keyName);
            //内存模式
        } else if (StorageComponent.MEMORY_MODE == STORAGE_MODE) {
            if (RedisConstant.PRODUCT_CATEGORY_DATA.equals(mapName)) {
                return productCategoryPoMap.get(keyName);
            } else if (RedisConstant.PRODUCT_PARENT_CHILD_CATEGORY_DATA.equals(mapName)) {
                return parentChildrenCategoryMap.get(keyName);
            } else if (RedisConstant.PRODUCT_CATEGORY_CODE_AND_ID_DATA.equals(mapName)) {
                return categoryCodeMap.get(keyName);
            }
        }
        return null;
    }

    //获取List数据方法兼容
    private List<String> getList(String listName) {
        //缓存模式
        if (StorageComponent.CACHE_MODE == STORAGE_MODE) {
            return redisService.getList(listName);
            //内存模式
        } else if (StorageComponent.MEMORY_MODE == STORAGE_MODE) {
            if (RedisConstant.PRODUCT_CATEGORY_LONG_CODE_DATA.equals(listName)) {
                return categoryLongCodeList;
            }
        }
        return null;
    }

    //更新数据
    public void updateData() {
        //获取数据
        log.info(CLASS_LOG_PREFIX + "开始获取产品分类元数据....");
        List<ProductCategoryPo> productCategoryPoList;
        try {
            productCategoryPoList = metaDataService.queryProductCategoryMetaData();
        } catch (MetaDataException e) {
            log.error(CLASS_LOG_PREFIX + "获取产品分类元数据失败: MetaDataException:{}", e);
            throw new NullPointerException(CLASS_LOG_PREFIX + "获取产品分类元数据失败,List<ProductCategoryPo> is null.MetaDataException:" + e);
        }
        log.info(CLASS_LOG_PREFIX + "获取产品分类元数据完成,总条数:{}", (null == productCategoryPoList ? 0 : productCategoryPoList.size()));

        if (null == productCategoryPoList || 0 == productCategoryPoList.size()) {
            log.error(CLASS_LOG_PREFIX + "获取产品分类元数据失败,List<ProductCategoryPo> is null.");
            throw new NullPointerException(CLASS_LOG_PREFIX + "获取产品分类元数据失败,List<ProductCategoryPo> is null.");
        }

        Map<Integer, ProductCategoryPo> tempProductCategoryPoMap = new HashMap<>();
        Map<Integer, List<Integer>> tempParentChildrenCategoryMap = new HashMap<>();
        Map<String, String> tempCategoryCodeAndCategoryIdMap = new HashMap<>();
        List<String> tempCategoryLongCodeList = new ArrayList<>(productCategoryPoList.size());
        Map<String, String> productCategoryVoMap = new HashMap<>();

        //整理数据
        productCategoryPoList.forEach(productCategoryPo -> {
            //产品分类Map元数据
            tempProductCategoryPoMap.put(productCategoryPo.getId(), productCategoryPo);
            //产品父子分类关系Map元数据<parentCategoryId, List<childrenCategoryId>>
            if (tempParentChildrenCategoryMap.containsKey(productCategoryPo.getParentCategoryId())) {
                List<Integer> childrenCategoryIdList = new ArrayList<>();
                childrenCategoryIdList.add(productCategoryPo.getId());
                childrenCategoryIdList.addAll(tempParentChildrenCategoryMap.get(productCategoryPo.getParentCategoryId()));
                tempParentChildrenCategoryMap.put(productCategoryPo.getParentCategoryId(), childrenCategoryIdList);
            } else {
                tempParentChildrenCategoryMap.put(productCategoryPo.getParentCategoryId(), Collections.singletonList(productCategoryPo.getId()));
            }
            //产品分类编码和ID
            tempCategoryCodeAndCategoryIdMap.put(productCategoryPo.getProductCategoryCode(), productCategoryPo.getId() + "");
            //分类长编码
            tempCategoryLongCodeList.add(productCategoryPo.getProductCategoryLongCode());

            //分类对象
            ProductCategoryVo productCategoryVo = new ProductCategoryVo();
            productCategoryVo.setCategoryName(productCategoryPo.getCategoryName());
            productCategoryVo.setCategoryCode(productCategoryPo.getProductCategoryLongCode());
            //解析长编码根据3级分类获取小类信息
            String[] codeSplit = productCategoryPo.getProductCategoryLongCode().split("\\.");
            if (null != codeSplit && 4 < codeSplit.length) {
                String thirdCode = productCategoryPo.getProductCategoryLongCode().split("\\.")[4];
                if (!StringUtils.isEmpty(thirdCode)) {
                    productCategoryVo.setProductSmallTypeValue(systemDictionaryMetaDataStorage.getValueByKey(thirdCode));
                }
            }

            productCategoryVo.setCategoryOrder(productCategoryPo.getCategoryOrder());
            productCategoryVoMap.put(productCategoryPo.getProductCategoryLongCode(), JsonUtil.toJson(productCategoryVo));
        });
        //父子分类节点数据去重
        if (null != tempParentChildrenCategoryMap && 0 < tempParentChildrenCategoryMap.size()) {
            //去重
            tempParentChildrenCategoryMap.forEach((k, list) -> tempParentChildrenCategoryMap.put(k, list.stream().distinct().collect(Collectors.toList())));
        }
        log.info(CLASS_LOG_PREFIX + "格式化产品分类元数据完成....");

        //处理为Json数据
        Map<String, String> productCategoryPoJsonMap = new HashMap<>(tempProductCategoryPoMap.size());
        Map<String, String> parentChildrenCategoryJsonMap = new HashMap<>(tempParentChildrenCategoryMap.size());
        tempProductCategoryPoMap.forEach((k, v) -> productCategoryPoJsonMap.put(k + "", JsonUtil.toJson(v)));
        tempParentChildrenCategoryMap.forEach((k, v) -> parentChildrenCategoryJsonMap.put(k + "", JsonUtil.toJson(v)));
        log.info(CLASS_LOG_PREFIX + "格式化产品分类Json元数据完成....");

        //装入缓存
        redisService.addMapCompatible(RedisConstant.PRODUCT_CATEGORY_DATA, productCategoryPoJsonMap);
        redisService.addMapCompatible(RedisConstant.PRODUCT_PARENT_CHILD_CATEGORY_DATA, parentChildrenCategoryJsonMap);
        redisService.addMapCompatible(RedisConstant.PRODUCT_CATEGORY_CODE_AND_ID_DATA, tempCategoryCodeAndCategoryIdMap);
        redisService.addMapCompatible(RedisConstant.PRODUCT_CATEGORY_CODE_AND_VO_DATA, productCategoryVoMap);
        //List需要删除再提交
        redisService.del(RedisConstant.PRODUCT_CATEGORY_LONG_CODE_DATA);
        redisService.addList(RedisConstant.PRODUCT_CATEGORY_LONG_CODE_DATA, tempCategoryLongCodeList);
        log.info(CLASS_LOG_PREFIX + "产品分类元数据装载缓存完成....");

        //内存模式
        if (StorageComponent.MEMORY_MODE == STORAGE_MODE) {
            productCategoryPoMap = productCategoryPoJsonMap;
            parentChildrenCategoryMap = parentChildrenCategoryJsonMap;
            categoryCodeMap = tempCategoryCodeAndCategoryIdMap;
            categoryLongCodeList = tempCategoryLongCodeList;
            log.info(CLASS_LOG_PREFIX + "产品分类元数据装载内存完成....");
        }
    }

    /**
     * 根据分类ID获取产品分类数据
     *
     * @return
     */
    public ProductCategoryPo getProductCategory(Integer productCategoryId) {
        if (null == productCategoryId || 0 == productCategoryId) {
            return null;
        }

        String productCategoryPoStr = getMap(RedisConstant.PRODUCT_CATEGORY_DATA, productCategoryId + "");
        if (StringUtils.isEmpty(productCategoryPoStr)) {
            return null;
        }
        return JsonUtil.fromJson(productCategoryPoStr, ProductCategoryPo.class);
    }

    /**
     * 根据分类Code获取分类ID
     *
     * @return
     */
    private String getProductCategoryCodeById(String productCategoryCode) {
        if (StringUtils.isEmpty(productCategoryCode)) {
            return null;
        }

        String productCategoryPoStr = getMap(RedisConstant.PRODUCT_CATEGORY_CODE_AND_ID_DATA, productCategoryCode);
        if (StringUtils.isEmpty(productCategoryPoStr)) {
            return null;
        }
        return productCategoryPoStr;
    }

    /**
     * 根据父分类ID获取子分类ID列表
     *
     * @return
     */
    private List<Integer> queryChildCategoryIdListByParentCategoryId(Integer parentCategoryId) {
        if (null == parentCategoryId || 0 == parentCategoryId) {
            return null;
        }

        String parentCategoryIdListStr = getMap(RedisConstant.PRODUCT_PARENT_CHILD_CATEGORY_DATA, parentCategoryId + "");
        if (StringUtils.isEmpty(parentCategoryIdListStr)) {
            return null;
        }
        return JsonUtil.fromJson(parentCategoryIdListStr, new TypeToken<List<Integer>>() {}.getType());
    }

    /**
     * 根据分类编码获取产品分类数据
     *
     * @return
     */
    public ProductCategoryPo getProductCategoryByCategoryCode(String productCategoryCode) {
        if (StringUtils.isEmpty(productCategoryCode)) {
            return null;
        }

        //获取分类ID
        String productCategoryId = getProductCategoryCodeById(productCategoryCode);
        if (StringUtils.isEmpty(productCategoryId)) {
            return null;
        }

        //获取分类对象
        return getProductCategory(Integer.parseInt(productCategoryId));
    }

    /**
     * 根据产品分类ID列表获取产品分类长编码列表
     *
     * @param categoryIdList 产品分类ID列表
     * @return
     */
    public List<String> queryProductCategoryLongCodeByCategoryIdList(List<Integer> categoryIdList) {

        if (null == categoryIdList || 0 == categoryIdList.size()) {
            return null;
        }

        List<String> categoryLongCodeList = new ArrayList<>(categoryIdList.size());
        categoryIdList.forEach(categoryId -> {
            if (null != categoryId && 0 != categoryId) {
                ProductCategoryPo productCategory = getProductCategory(categoryId);
                if (null != productCategory) {
                    String productCategoryLongCode = productCategory.getProductCategoryLongCode();
                    if (!StringUtils.isEmpty(productCategoryLongCode)) {
                        categoryLongCodeList.add(productCategoryLongCode);
                    }
                }
            }
        });

        return categoryLongCodeList;
    }

    /**
     * 根据产品分类ID获取所有分类名
     *
     * @param categoryIdList 分类IDList
     * @return
     */
    public List<String> queryProductCategoryIdByProductId(List<Integer> categoryIdList) {
        if (null == categoryIdList || 0 == categoryIdList.size()) {
            return null;
        }

        List<String> categoryNameList = new ArrayList<>(categoryIdList.size());
        categoryIdList.forEach(categoryId -> {
            if (null != categoryId && 0 != categoryId) {
                ProductCategoryPo productCategory = getProductCategory(categoryId);
                if (null != productCategory) {
                    categoryNameList.add(productCategory.getCategoryName());
                }
            }
        });

        return categoryNameList;
    }

    /**
     * 根据父分类ID查询子分类IDList
     *
     * @param parentCategoryIdList
     * @return
     */
    private List<Integer> queryCategoryIdListByParentCategoryIdList(List<Integer> parentCategoryIdList) {

        if (null == parentCategoryIdList || 0 >= parentCategoryIdList.size()) {
            return null;
        }

        List<Integer> childrenCategoryIdList = new ArrayList<>();
        parentCategoryIdList.forEach(parentCategoryId -> {
            List<Integer> childCategoryIdList = queryChildCategoryIdListByParentCategoryId(parentCategoryId);
            if (null != childCategoryIdList && 0 < childCategoryIdList.size()) {
                childrenCategoryIdList.addAll(childCategoryIdList);
            }
        });

        return childrenCategoryIdList;
    }
}