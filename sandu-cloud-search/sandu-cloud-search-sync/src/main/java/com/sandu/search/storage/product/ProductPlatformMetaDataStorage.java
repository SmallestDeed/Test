package com.sandu.search.storage.product;

import com.sandu.search.common.constant.PlatformConstant;
import com.sandu.search.common.constant.RedisConstant;
import com.sandu.search.common.tools.JsonUtil;
import com.sandu.search.entity.elasticsearch.po.metadate.ProductPlatformRelPo;
import com.sandu.search.entity.elasticsearch.po.product.ProductPlatformData;
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
 * 产品平台元数据存储(全平台数据过滤)
 *
 * @date 20180228
 * @auth pengxuangang
 */
@Slf4j
@Component
public class ProductPlatformMetaDataStorage {

    private final static String CLASS_LOG_PREFIX = "产品平台元数据存储(全平台数据过滤):";

    //默认缓存模式
    private static Integer STORAGE_MODE = StorageComponent.CACHE_MODE;

    private final RedisService redisService;
    private final MetaDataService metaDataService;

    @Autowired
    public ProductPlatformMetaDataStorage(RedisService redisService, MetaDataService metaDataService) {
        this.redisService = redisService;
        this.metaDataService = metaDataService;
    }

    //产品平台元数据Map<产品ID,产品平台数据>
    private static Map<String, String> productPlatformRelMap = null;

    //切换存储模式
    public void changeStorageMode(Integer storageMode) {
        //缓存模式
        if (StorageComponent.CACHE_MODE == storageMode) {
            //清空内存占用
            productPlatformRelMap = null;
            //切换
            STORAGE_MODE = storageMode;
            //内存模式
        } else if(StorageComponent.MEMORY_MODE == storageMode) {
            updateData();
            //切换
            STORAGE_MODE = storageMode;

            //productPlatformRelMap = redisService.getMap(RedisConstant.PRODUCT_PLATFORM_REL_MAP_DATA);
        }
        log.info(CLASS_LOG_PREFIX + "产品平台存储模式切换成功，当前存储:{}.", StorageComponent.CACHE_MODE == STORAGE_MODE ? "缓存" : "内存");
    }

    //获取Map数据方法兼容
    private String getMap(String mapName, String keyName) {
        //缓存模式
        if (StorageComponent.CACHE_MODE == STORAGE_MODE) {
            return redisService.getMap(mapName, keyName);
            //内存模式
        } else if (StorageComponent.MEMORY_MODE == STORAGE_MODE) {
            if (RedisConstant.PRODUCT_PLATFORM_REL_MAP_DATA.equals(mapName)) {
                //return productPlatformRelMap.get(keyName);
                return redisService.getMap(mapName, keyName);
            }
        }
        return null;
    }

    /**
     * 觉得更新没必要存在缓存里面,因为更新完了就可以清除了
     * 
     * @author huangsongbo
     * @return
     */
    /*private String getMap(String mapName, String keyName) {
    	if(productPlatformRelMap != null) {
    		return productPlatformRelMap.get(keyName);
    	}else {
    		log.error(CLASS_LOG_PREFIX + "productPlatformRelMap = null");
    		return null;
    	}
    }*/
    
    //更新数据
    public void updateData() {

        log.info(CLASS_LOG_PREFIX + "开始获取产品平台元数据....");
        //产品平台元数据
        List<ProductPlatformRelPo> productPlatformRelPoList;
        try {
            productPlatformRelPoList = metaDataService.queryAllPlatformProductMetaData();
        } catch (MetaDataException e) {
            log.error(CLASS_LOG_PREFIX + "获取产品平台元数据失败: MetaDataException:{}", e);
            throw new NullPointerException(CLASS_LOG_PREFIX + "获取产品平台元数据失败,List<ProductPlatformRelPo> is null.MetaDataException:" + e);
        }
        log.info(CLASS_LOG_PREFIX + "获取产品平台元数据完成,总条数:{}", (null == productPlatformRelPoList ? 0 : productPlatformRelPoList.size()));

        //临时对象
        Map<Integer, ProductPlatformData> tempProductPlatformRelMap = new HashMap<>();

        //转换Map
        if (null != productPlatformRelPoList && 0 != productPlatformRelPoList.size()) {
            productPlatformRelPoList.forEach(productPlatformRelPo -> {
                //检查对象/产品ID/平台ID
                if (null != productPlatformRelPo && 0 < productPlatformRelPo.getProductId() && !StringUtils.isEmpty(productPlatformRelPo.getPlatformCode())) {
                    //产品ID
                    int productId = productPlatformRelPo.getProductId();
                    //产品平台对象
                    ProductPlatformData productPlatformData = new ProductPlatformData();
                    //更新数据
                    if (tempProductPlatformRelMap.containsKey(productId)) {
                        productPlatformData = tempProductPlatformRelMap.get(productId);
                    }
                    switch (productPlatformRelPo.getPlatformCode()) {
                        //2B-移动端
                        case PlatformConstant.PLATFORM_CODE_TOB_MOBILE:
                            productPlatformData.setPlatformProductToBMobile(productPlatformRelPo);
                            break;
                        //2B-PC
                        case PlatformConstant.PLATFORM_CODE_TOB_PC:
                            productPlatformData.setPlatformProductToBPc(productPlatformRelPo);
                            break;
                        //品牌2C-网站
                        case PlatformConstant.PLATFORM_CODE_TOC_SITE:
                            productPlatformData.setPlatformProductToCSite(productPlatformRelPo);
                            break;
                        //2C-移动端
                        case PlatformConstant.PLATFORM_CODE_TOC_MOBILE:
                            productPlatformData.setPlatformProductToCMobile(productPlatformRelPo);
                            break;
                        //三度-后台管理
                        case PlatformConstant.PLATFORM_CODE_SANDU_MANAGER:
                            productPlatformData.setPlatformProductSanduManager(productPlatformRelPo);
                            break;
                        //户型绘制工具
                        case PlatformConstant.PLATFORM_CODE_HOUSE_DRAW:
                            productPlatformData.setPlatformProductHouseDraw(productPlatformRelPo);
                            break;
                        //商家管理后台
                        case PlatformConstant.PLATFORM_CODE_MERCHANTS_MANAGER:
                            productPlatformData.setPlatformProductMerchantsManager(productPlatformRelPo);
                            break;
                        //测试
                        case PlatformConstant.PLATFORM_CODE_TEST:
                            productPlatformData.setPlatformProductTest(productPlatformRelPo);
                            break;
                        //U3D转换插件
                        case PlatformConstant.PLATFORM_CODE_U3D_PLUGIN:
                            productPlatformData.setPlatformProductU3dPlugin(productPlatformRelPo);
                            break;
                        //小程序
                        case PlatformConstant.PLATFORM_CODE_MINI_PROGRAM:
                            productPlatformData.setPlatformProductMiniProgram(productPlatformRelPo);
                            break;
                        // 随选网
                        case PlatformConstant.PLATFORM_CODE_SELECT_DECORATION:
                            productPlatformData.setPlatformProductSelectDecoration(productPlatformRelPo);
                            break;
                        default:
                            log.warn(CLASS_LOG_PREFIX + "无法识别平台编码,productPlatformRelPo:{}", productPlatformRelPo.toString());
                    }
                    //装入Map
                    tempProductPlatformRelMap.put(productId, productPlatformData);
                }
            });
        }
        log.info(CLASS_LOG_PREFIX + "格式化产品平台元数据完成....");

        //数据转换
        Map<String, String> productPlatformRelJsonMap = new HashMap<>(tempProductPlatformRelMap.size());
        tempProductPlatformRelMap.forEach((k, v) -> productPlatformRelJsonMap.put(k + "", JsonUtil.toJson(v)));
        log.info(CLASS_LOG_PREFIX + "格式化产品平台Json元数据完成....");

        //装载缓存
        // remove 缓存,应对一种情况,取消上架后(取消所有平台),产品平台数据更新失败(缓存数据还在并且没有更新)
        redisService.del(RedisConstant.PRODUCT_PLATFORM_REL_MAP_DATA);
        redisService.addMapCompatible(RedisConstant.PRODUCT_PLATFORM_REL_MAP_DATA, productPlatformRelJsonMap);
        log.info(CLASS_LOG_PREFIX + "产品平台元数据装载缓存完成....");

        //内存模式
        if (StorageComponent.MEMORY_MODE == STORAGE_MODE) {
            productPlatformRelMap = productPlatformRelJsonMap;
            log.info(CLASS_LOG_PREFIX + "产品平台元数据装载内存完成....");
        }
        
        //productPlatformRelMap = productPlatformRelJsonMap;
    }

    /**
     * 根据产品ID获取产品平台数据
     *
     * @param productId 产品ID
     * @return
     */
    public ProductPlatformData queryProductPlatformByProductId(Integer productId) {
        if (null == productId || 0 == productId) {
            return null;
        }

        String productPlatformRelStr = getMap(RedisConstant.PRODUCT_PLATFORM_REL_MAP_DATA, productId + "");
        if (StringUtils.isEmpty(productPlatformRelStr)) {
            return null;
        }

        if (productId == 39945592) {
            log.info("39945592平台数据 : {}", productPlatformRelStr);
        }

        return JsonUtil.fromJson(productPlatformRelStr, ProductPlatformData.class);
    }



    /**
     * 根据产品ID从数据库获取
     *
     * @param productId 产品ID
     * @return
     */
    public ProductPlatformRelPo queryProductPlatformById(Integer productId) {
        if (null == productId || 0 == productId) {
            return null;
        }

        ProductPlatformRelPo productPlatformData;
        try {
            productPlatformData = metaDataService.queryAllPlatformProductById(productId);
        } catch (MetaDataException e) {
            log.error(CLASS_LOG_PREFIX + "获取产品平台元数据失败: MetaDataException:{}", e);
            throw new NullPointerException(CLASS_LOG_PREFIX + "获取产品平台元数据失败,productPlatformData is null.MetaDataException:" + e);
        }
        log.info(CLASS_LOG_PREFIX + "获取产品平台元数据完成:{}", productPlatformData);

        return productPlatformData;
    }













}
