package com.sandu.search.storage.goods;

import com.google.gson.reflect.TypeToken;
import com.sandu.search.common.constant.RedisConstant;
import com.sandu.search.common.tools.JsonUtil;
import com.sandu.search.entity.elasticsearch.po.BaseGoodsSkuPo;
import com.sandu.search.entity.elasticsearch.po.metadate.GoodsSkuPo;
import com.sandu.search.exception.MetaDataException;
import com.sandu.search.service.metadata.MetaDataService;
import com.sandu.search.service.redis.RedisService;
import com.sandu.search.storage.StorageComponent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.*;

/**
 * 空间元数据存储
 *
 * @date 2018/5/31
 * @auth pengxuangang
 * @mail xuangangpeng@gmail.com
 */
@Slf4j
@Component
public class GoodsSkuMetaDataStorage {

    private final static String CLASS_LOG_PREFIX = "sku产品单元元数据存储:";
    //默认缓存模式
    private static Integer STORAGE_MODE = StorageComponent.CACHE_MODE;

    private final RedisService redisService;
    private final MetaDataService metaDataService;

    @Autowired
    public GoodsSkuMetaDataStorage(RedisService redisService, MetaDataService metaDataService) {
        this.redisService = redisService;
        this.metaDataService = metaDataService;
    }

    private static Map<String, String> goodsSkuMap = null;

    //切换存储模式
    public void changeStorageMode(Integer storageMode) {
        //缓存模式
        if (StorageComponent.CACHE_MODE == storageMode) {
            //清空内存占用
            goodsSkuMap = null;
            //切换
            STORAGE_MODE = storageMode;
            //内存模式
        } else if (StorageComponent.MEMORY_MODE == storageMode) {
            //切换
            STORAGE_MODE = storageMode;
            //写入内存
            updateData();
        }
        log.info(CLASS_LOG_PREFIX + "sku产品单元存储模式切换成功，当前存储:{}.", StorageComponent.CACHE_MODE == STORAGE_MODE ? "缓存" : "内存");
    }

    //获取Map数据方法兼容
    private String getMap(String mapName, String keyName) {
        //缓存模式
        if (StorageComponent.CACHE_MODE == STORAGE_MODE) {
            return redisService.getMap(mapName, keyName);
            //内存模式
        } else if (StorageComponent.MEMORY_MODE == STORAGE_MODE) {
            if (RedisConstant.BASE_GOODS_SKU_MAP.equals(mapName)) {
                return goodsSkuMap.get(keyName);
            }
        }
        return null;
    }


    //更新数据
    public void updateData() {
        //获取数据
        log.info(CLASS_LOG_PREFIX + "开始获取sku产品单元元数据存储....");

        //sku产品单元数据
        List<GoodsSkuPo> goodsSkuList;
        try {
            goodsSkuList = metaDataService.querygoodsSkuMetaData();
        } catch (MetaDataException e) {
            log.error(CLASS_LOG_PREFIX + "获取sku产品单元元数据失败: MetaDataException:{}", e);
            throw new NullPointerException(CLASS_LOG_PREFIX + "获取sku产品单元元数据失败, List<GoodsSkuPo> is null.MetaDataException:" + e);
        }

        log.info(CLASS_LOG_PREFIX + "获取sku产品单元元数据完成,总条数:{}", (null == goodsSkuList ? 0 : goodsSkuList.size()));

        //Map对象
        Map<Integer, List<GoodsSkuPo>> tempGoodsSkuMap = new HashMap<>(null == goodsSkuList ? 10 : goodsSkuList.size());
        //转换为Map元数据
        if (null != goodsSkuList && 0 != goodsSkuList.size()) {
            for(GoodsSkuPo goodsSkuPo : goodsSkuList){
                if(!tempGoodsSkuMap.containsKey(goodsSkuPo.getSpuId())){
                    List<GoodsSkuPo> goodsSkuPoList = new ArrayList<>();
                    goodsSkuPoList.add(goodsSkuPo);
                    tempGoodsSkuMap.put(goodsSkuPo.getSpuId(), goodsSkuPoList);
                }else{
                    List<GoodsSkuPo> goodsSkuPoList =  tempGoodsSkuMap.get(goodsSkuPo.getSpuId());
                    goodsSkuPoList.add(goodsSkuPo);
                    tempGoodsSkuMap.put(goodsSkuPo.getSpuId(), goodsSkuPoList);
                }

            }

        }
        log.info(CLASS_LOG_PREFIX + "格式化sku产品单元元数据完成.");
        //转换map元数据
        Map<String, String> newGoodsSkuMap = new HashMap<>(tempGoodsSkuMap.size());
        tempGoodsSkuMap.forEach((k, v) -> newGoodsSkuMap.put(k + "", JsonUtil.toJson(v)));

        //装回对象
        redisService.addMapCompatible(RedisConstant.BASE_GOODS_SKU_MAP, newGoodsSkuMap);
        log.info(CLASS_LOG_PREFIX + "缓存载入sku产品单元元数据完成.");

        //内存模式
        if (StorageComponent.MEMORY_MODE == STORAGE_MODE) {
            goodsSkuMap = newGoodsSkuMap;
            log.info(CLASS_LOG_PREFIX + "内存载入sku产品单元元数据完成....");
        }
    }

    /**
     * 根据spuId获取sku列表
     *
     * @param spuId 商品ID
     * @return
     */
    public List<GoodsSkuPo> getGoodsSkuListBySpuId(Integer spuId) {

        if (null == spuId || 0 > spuId) {
            return null;
        }


        String goodsSkuInfoStr = getMap(RedisConstant.BASE_GOODS_SKU_MAP, spuId + "");
        if (StringUtils.isEmpty(goodsSkuInfoStr)) {
            return null;
        }

        List<GoodsSkuPo> goodsSkuPoList = JsonUtil.fromJson(goodsSkuInfoStr, new TypeToken<List<GoodsSkuPo>>(){}.getType());


        return goodsSkuPoList;
    }


    public BigDecimal getGoodsSkuPriceBySpuId(Integer id, int productId) {
        if (null == id || 0 > id ||productId == id || 0 > productId) {
            return new BigDecimal(0);
        }
        String goodsSkuInfoStr = getMap(RedisConstant.BASE_GOODS_SKU_MAP, id + "");
        if (StringUtils.isEmpty(goodsSkuInfoStr)) {
            return new BigDecimal(0);
        }
        List<GoodsSkuPo> goodsSkuPoList = JsonUtil.fromJson(goodsSkuInfoStr, new TypeToken<List<GoodsSkuPo>>(){}.getType());
        for(GoodsSkuPo goodsSkuPo : goodsSkuPoList){
            if(productId == goodsSkuPo.getProductId()){
                return goodsSkuPo.getPrice();
            }
        }

        return new BigDecimal(0);

    }

    public BigDecimal getGoodsDefaultPrice(List<BaseGoodsSkuPo> baseGoodsSkuPoList) {
        //处理商品默认价格
        if(null!=baseGoodsSkuPoList && baseGoodsSkuPoList.size()>0){
            Collections.sort(baseGoodsSkuPoList, new Comparator<BaseGoodsSkuPo>() {
                @Override
                public int compare(BaseGoodsSkuPo o1, BaseGoodsSkuPo o2) {
                    int i = o1.getPrice().intValue();
                    int j = o2.getPrice().intValue();
                    if (i == 0 || j == 0) {
                        return -1;
                    }
                    return i - j;
                }
            });
        }
        return baseGoodsSkuPoList.get(0).getPrice();
    }


    public GoodsSkuPo getGoodsSkuPriceById(int spuId, int productId) {
        //sku产品单元数据
        GoodsSkuPo goodsSkuPo;
        try {
            goodsSkuPo = metaDataService.querygoodsSkuMetaDataByspuId(spuId,productId);
        } catch (MetaDataException e) {
            log.error(CLASS_LOG_PREFIX + "获取sku产品单元元数据失败: MetaDataException:{}", e);
            throw new NullPointerException(CLASS_LOG_PREFIX + "获取sku产品单元元数据失败, goodsSkuPo is null.MetaDataException:" + e);
        }

        log.info(CLASS_LOG_PREFIX + "获取sku产品单元元数据完成",goodsSkuPo);
        return goodsSkuPo;
    }
}
