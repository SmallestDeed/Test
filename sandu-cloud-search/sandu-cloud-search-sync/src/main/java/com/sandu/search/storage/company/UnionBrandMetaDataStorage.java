package com.sandu.search.storage.company;

import com.google.gson.reflect.TypeToken;
import com.sandu.search.common.constant.RedisConstant;
import com.sandu.search.common.tools.JsonUtil;
import com.sandu.search.entity.elasticsearch.po.UnionBrandPo;
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
 * 联盟品牌元数据存储
 *
 * @date 20171218
 * @auth pengxuangang
 */
@Slf4j
@Component
public class UnionBrandMetaDataStorage {

    private final static String CLASS_LOG_PREFIX = "联盟品牌元数据存储:";
    //默认缓存模式
    private static Integer STORAGE_MODE = StorageComponent.CACHE_MODE;

    private final RedisService redisService;
    private final MetaDataService metaDataService;

    @Autowired
    public UnionBrandMetaDataStorage(RedisService redisService, MetaDataService metaDataService) {
        this.redisService = redisService;
        this.metaDataService = metaDataService;
    }

    //联盟品牌
    private static Map<String, String> unionBrandMap = null;
    //品牌联盟
    private static Map<String, String> brandUnionMap = null;

    //切换存储模式
    public void changeStorageMode(Integer storageMode) {
        //缓存模式
        if (StorageComponent.CACHE_MODE == storageMode) {
            //清空内存占用
            unionBrandMap = null;
            brandUnionMap = null;
            //切换
            STORAGE_MODE = storageMode;
            //内存模式
        } else if(StorageComponent.MEMORY_MODE == storageMode) {
            //切换
            STORAGE_MODE = storageMode;
            //写入内存
            updateData();
            //unionBrandMap = redisService.getMap(RedisConstant.UNION_BRAND_DATA);
            //brandUnionMap = redisService.getMap(RedisConstant.BRAND_UNION_DATA);
        }
        log.info(CLASS_LOG_PREFIX + "联盟|品牌存储模式切换成功，当前存储:{}.", StorageComponent.CACHE_MODE == STORAGE_MODE ? "缓存" : "内存");
    }

    //获取Map数据方法兼容
    private String getMap(String mapName, String keyName) {
        //缓存模式
        if (StorageComponent.CACHE_MODE == STORAGE_MODE) {
            return redisService.getMap(mapName, keyName);
            //内存模式
        } else if (StorageComponent.MEMORY_MODE == STORAGE_MODE) {
            if (RedisConstant.UNION_BRAND_DATA.equals(mapName)) {
                return unionBrandMap.get(keyName);
            } else if (RedisConstant.BRAND_UNION_DATA.equals(mapName)) {
                return brandUnionMap.get(keyName);
            }
        }
        return null;
    }


    //更新数据
    public void updateData() {
        //获取数据
        log.info(CLASS_LOG_PREFIX + "开始获取联盟品牌元数据....");
        List<UnionBrandPo> unionBrandPoList;
        try {
            unionBrandPoList = metaDataService.queryUnionBrandMetaData();
        } catch (MetaDataException e) {
            log.error(CLASS_LOG_PREFIX + "获取联盟品牌元数据失败: MetaDataException:{}", e);
            throw new NullPointerException(CLASS_LOG_PREFIX + "获取联盟品牌元数据失败,List<UnionBrandPo> is null.MetaDataException:" + e);
        }
        log.info(CLASS_LOG_PREFIX + "获取联盟品牌元数据完成,总条数:{}", (null == unionBrandPoList ? 0 : unionBrandPoList.size()));

        //临时联盟品牌数据<unionId, List<brandId>>
        Map<Integer, List<Integer>> tempUnionBrandMap = new HashMap<>();
        //临时品牌联盟数据<brandId, List<unionId>>
        Map<Integer, List<Integer>> tempBrandUnionMap = new HashMap<>();

        //转换为Map元数据
        if (null != unionBrandPoList && 0 != unionBrandPoList.size()) {
            for (UnionBrandPo unionBrandPo : unionBrandPoList) {
                //联盟ID
                Integer unionId = unionBrandPo.getUnionId();
                //品牌ID
                Integer brandId = unionBrandPo.getBrandId();
                //联盟品牌
                if (tempUnionBrandMap.containsKey(unionId)) {
                    List<Integer> brandList = new ArrayList<>();
                    brandList.add(brandId);
                    brandList.addAll(tempUnionBrandMap.get(unionId));
                    tempUnionBrandMap.put(unionId, brandList);
                } else {
                    tempUnionBrandMap.put(unionId, Collections.singletonList(brandId));
                }

                //品牌联盟
                if (tempBrandUnionMap.containsKey(brandId)) {
                    List<Integer> unionList = new ArrayList<>();
                    unionList.add(unionId);
                    unionList.addAll(tempBrandUnionMap.get(brandId));
                    tempBrandUnionMap.put(brandId, unionList);
                } else {
                    tempBrandUnionMap.put(brandId, Collections.singletonList(unionId));
                }
            }
        }
        log.info(CLASS_LOG_PREFIX + "格式化联盟|品牌元数据完成....");

        //转Json数据
        Map<String, String> unionBrandJsonMap = new HashMap<>(tempUnionBrandMap.size());
        Map<String, String> brandUnionJsonMap = new HashMap<>(tempBrandUnionMap.size());
        tempUnionBrandMap.forEach((k, v) -> unionBrandJsonMap.put(k + "", JsonUtil.toJson(v)));
        tempBrandUnionMap.forEach((k, v) -> brandUnionJsonMap.put(k + "", JsonUtil.toJson(v)));
        log.info(CLASS_LOG_PREFIX + "格式化联盟|品牌Json元数据完成....");

        //装入缓存
        redisService.addMapCompatible(RedisConstant.UNION_BRAND_DATA, unionBrandJsonMap);
        redisService.addMapCompatible(RedisConstant.BRAND_UNION_DATA, brandUnionJsonMap);
        log.info(CLASS_LOG_PREFIX + "联盟|品牌元数据装载缓存完成....");

        //内存模式
        if (StorageComponent.MEMORY_MODE == STORAGE_MODE) {
            unionBrandMap = unionBrandJsonMap;
            brandUnionMap = brandUnionJsonMap;
            log.info(CLASS_LOG_PREFIX + "联盟|品牌元数据装载内存完成....");
        }    }

    /**
     * 获取联盟ID列表
     *
     * @date 2018/6/14
     * @auth pengxuangang
     * @mail xuangangpeng@gmail.com
     */
    private List<Integer> queryUnionIdListByBrandId(Integer brandId) {
        if (null == brandId || 0 >= brandId) {
            return null;
        }

        String unionIdListStr = getMap(RedisConstant.BRAND_UNION_DATA, brandId + "");

        if (StringUtils.isEmpty(unionIdListStr)) {
            return null;
        }

        return JsonUtil.fromJson(unionIdListStr, new TypeToken<List<Integer>>() {}.getType());
    }

    /**
     * 获取品牌ID列表
     *
     * @date 2018/6/14
     * @auth pengxuangang
     * @mail xuangangpeng@gmail.com
     */
    private List<Integer> queryBrandIdListByUnionId(Integer unionId) {
        if (null == unionId || 0 >= unionId) {
            return null;
        }

        String brandIdListStr = getMap(RedisConstant.UNION_BRAND_DATA, unionId + "");

        if (StringUtils.isEmpty(brandIdListStr)) {
            return null;
        }

        return JsonUtil.fromJson(brandIdListStr, new TypeToken<List<Integer>>() {}.getType());
    }
}
