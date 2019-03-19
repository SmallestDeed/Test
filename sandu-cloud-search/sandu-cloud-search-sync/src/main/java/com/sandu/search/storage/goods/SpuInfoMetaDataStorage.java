package com.sandu.search.storage.goods;

import com.sandu.search.common.constant.RedisConstant;
import com.sandu.search.common.tools.JsonUtil;
import com.sandu.search.entity.elasticsearch.po.metadate.SpaceCommonPo;
import com.sandu.search.entity.elasticsearch.po.metadate.SpuSaleInfoPo;
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
 * 空间元数据存储
 *
 * @date 2018/5/31
 * @auth pengxuangang
 * @mail xuangangpeng@gmail.com
 */
@Slf4j
@Component
public class SpuInfoMetaDataStorage {

    private final static String CLASS_LOG_PREFIX = "商品详情元数据存储:";
    //默认缓存模式
    private static Integer STORAGE_MODE = StorageComponent.CACHE_MODE;

    private final RedisService redisService;
    private final MetaDataService metaDataService;

    @Autowired
    public SpuInfoMetaDataStorage(RedisService redisService, MetaDataService metaDataService) {
        this.redisService = redisService;
        this.metaDataService = metaDataService;
    }

    private static Map<String, String> spuInfoMap = null;

    //切换存储模式
    public void changeStorageMode(Integer storageMode) {
        //缓存模式
        if (StorageComponent.CACHE_MODE == storageMode) {
            //清空内存占用
            spuInfoMap = null;
            //切换
            STORAGE_MODE = storageMode;
            //内存模式
        } else if(StorageComponent.MEMORY_MODE == storageMode) {
            //切换
            STORAGE_MODE = storageMode;
            //写入内存
            updateData();
        }
        log.info(CLASS_LOG_PREFIX + "商品详情存储模式切换成功，当前存储:{}.", StorageComponent.CACHE_MODE == STORAGE_MODE ? "缓存" : "内存");
    }

    //获取Map数据方法兼容
    private String getMap(String mapName, String keyName) {
        //缓存模式
        if (StorageComponent.CACHE_MODE == STORAGE_MODE) {
            return redisService.getMap(mapName, keyName);
            //内存模式
        } else if (StorageComponent.MEMORY_MODE == STORAGE_MODE) {
            if (RedisConstant.BASE_GOODS_INFO_MAP.equals(mapName)) {
                return spuInfoMap.get(keyName);
            }
        }
        return null;
    }

    //更新数据
    public void updateData() {
        //获取数据
        log.info(CLASS_LOG_PREFIX + "开始获取商品详情元数据存储....");

        //商品详情元数据
        List<SpuSaleInfoPo> spuInfoList;
        try {
            spuInfoList = metaDataService.querySpuInfoMetaData();
        } catch (MetaDataException e) {
            log.error(CLASS_LOG_PREFIX + "获取商品详情元数据失败: MetaDataException:{}", e);
            throw new NullPointerException(CLASS_LOG_PREFIX + "获取商品详情元数据失败,List<SpuSaleInfoPo> is null.MetaDataException:" + e);
        }

        log.info(CLASS_LOG_PREFIX + "获取商品详情元数据完成,总条数:{}", (null == spuInfoList ? 0 : spuInfoList.size()));

        //Map对象
        Map<String, String> tempSpuInfoMap = new HashMap<>(null == spuInfoList ? 10 : spuInfoList.size());
        //转换为Map元数据
        if (null != spuInfoList && 0 != spuInfoList.size()) {
            spuInfoList.forEach(spuInfo -> tempSpuInfoMap.put(spuInfo.getSpuId() + "", JsonUtil.toJson(spuInfo)));
        }
        log.info(CLASS_LOG_PREFIX + "格式化商品详情元数据完成.");

        //装回对象
        redisService.addMapCompatible(RedisConstant.BASE_GOODS_INFO_MAP, tempSpuInfoMap);
        log.info(CLASS_LOG_PREFIX + "缓存载入商品详情元数据完成.");

        //内存模式
        if (StorageComponent.MEMORY_MODE == STORAGE_MODE) {
            spuInfoMap = tempSpuInfoMap;
            log.info(CLASS_LOG_PREFIX + "内存载入商品详情元数据完成....");
        }
    }

    /**
     * 根据spuID获取商品详情
     *
     * @param spuId  商品ID
     * @return
     */
    public SpuSaleInfoPo getSpuInfoBySpuId (Integer spuId) {

        if (null == spuId || 0 > spuId ) {
            return null;
        }

        String spuInfoStr = getMap(RedisConstant.BASE_GOODS_INFO_MAP, spuId + "");
        if (StringUtils.isEmpty(spuInfoStr)) {
            return null;
        }

        return JsonUtil.fromJson(spuInfoStr, SpuSaleInfoPo.class);
    }


    public SpuSaleInfoPo querySpuInfoBySpuId(Integer id) {
        if(null == id || id ==0){
            return null;
        }
        //查询单个商品详情
        SpuSaleInfoPo spuInfo;
        try {
            spuInfo = metaDataService.querySpuInfoById(id);
        } catch (Exception e) {
            log.error(CLASS_LOG_PREFIX + "获取单个商品详情数据失败: MetaDataException:{}", e);
            throw new NullPointerException(CLASS_LOG_PREFIX + "获取单个商品详情数据失败,spuInfo is null.MetaDataException:" + e);
        }


        return spuInfo;
    }
}
