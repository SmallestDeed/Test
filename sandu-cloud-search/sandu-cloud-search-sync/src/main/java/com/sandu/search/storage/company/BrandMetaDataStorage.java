package com.sandu.search.storage.company;

import com.sandu.search.common.constant.RedisConstant;
import com.sandu.search.common.tools.JsonUtil;
import com.sandu.search.entity.elasticsearch.po.BaseCompanyMiniProgramConfig;
import com.sandu.search.entity.elasticsearch.po.BrandPo;
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
 * 品牌元数据存储
 *
 * @date 20171218
 * @auth pengxuangang
 */
@Slf4j
@Component
public class BrandMetaDataStorage {

    private final static String CLASS_LOG_PREFIX = "品牌元数据存储:";
    //默认缓存模式
    private static Integer STORAGE_MODE = StorageComponent.CACHE_MODE;

    private final RedisService redisService;
    private final MetaDataService metaDataService;

    @Autowired
    public BrandMetaDataStorage(MetaDataService metaDataService, RedisService redisService) {
        this.metaDataService = metaDataService;
        this.redisService = redisService;
    }

    private static Map<String, String> brandMap = null;
    private static Map<String, String> companyBrandMap = null;
    private static Map<String, String> miniProAppIdBrandsMap = null;

    //切换存储模式
    public void changeStorageMode(Integer storageMode) {
        //缓存模式
        if (StorageComponent.CACHE_MODE == storageMode) {
            //清空内存占用
            brandMap = null;
            companyBrandMap = null;
            miniProAppIdBrandsMap = null;
            //切换
            STORAGE_MODE = storageMode;
        //内存模式
        } else if(StorageComponent.MEMORY_MODE == storageMode) {
            //切换
            STORAGE_MODE = storageMode;
            //写入内存
            updateData();
            //brandMap = redisService.getMap(RedisConstant.BRAND_META_DATA);
            //companyBrandMap = redisService.getMap(RedisConstant.COMPANY_BRAND_META_DATA);
        }
        log.info(CLASS_LOG_PREFIX + "品牌存储模式切换成功，当前存储:{}.", StorageComponent.CACHE_MODE == STORAGE_MODE ? "缓存" : "内存");
    }

    //获取Map数据方法兼容
    private String getMap(String mapName, String keyName) {
        //缓存模式
        if (StorageComponent.CACHE_MODE == STORAGE_MODE) {
            return redisService.getMap(mapName, keyName);
            //内存模式
        } else if (StorageComponent.MEMORY_MODE == STORAGE_MODE) {
            if (RedisConstant.BRAND_META_DATA.equals(mapName)) {
                return brandMap.get(keyName);
            } else if (RedisConstant.COMPANY_BRAND_META_DATA.equals(mapName)) {
                return companyBrandMap.get(keyName);
            }else if (RedisConstant.BASE_COMPANY_MINIPROGRAM_CONFIG_MAP.equals(mapName)) {
                return miniProAppIdBrandsMap.get(keyName);
            }
        }
        return null;
    }

    //更新数据
    public void updateData() {

        //获取数据
        log.info(CLASS_LOG_PREFIX + "开始获取品牌元数据....");

        List<BrandPo> brandPoList;
        List<BaseCompanyMiniProgramConfig> baseCompanyMiniProgramConfigList;
        try {
            brandPoList = metaDataService.queryBrandMetaData();
        } catch (MetaDataException e) {
            log.error(CLASS_LOG_PREFIX + "获取品牌元数据失败: MetaDataException:{}", e);
            throw new NullPointerException(CLASS_LOG_PREFIX + "获取品牌元数据失败,List<BrandPo> is null.MetaDataException:" + e);
        }
        log.info(CLASS_LOG_PREFIX + "获取品牌元数据完成,总条数:{}", (null == brandPoList ? 0 : brandPoList.size()));
        try {
            baseCompanyMiniProgramConfigList = metaDataService.queryBaseCompanyMiniProgramConfig();
        } catch (MetaDataException e) {
            log.error(CLASS_LOG_PREFIX + "获取小程序品牌元数据失败: MetaDataException:{}", e);
            throw new NullPointerException(CLASS_LOG_PREFIX + "获取小程序品牌元数据失败,List<BaseCompanyMiniProgramConfig> is null.MetaDataException:" + e);
        }
        log.info(CLASS_LOG_PREFIX + "获取小程序品牌元数据完成,baseCompanyMiniProgramConfigList总条数:{}", (null == baseCompanyMiniProgramConfigList ? 0 : baseCompanyMiniProgramConfigList.size()));

        if (null == brandPoList || 0 >= brandPoList.size()) {
            log.info(CLASS_LOG_PREFIX + "品牌元数据为空，初始化品牌数据异常...");
            return;
        }

        //品牌对象
        Map<String, String> tempBrandPoMap = new HashMap<>();
        Map<Integer, List<Integer>> tempCompanyBrandMap = new HashMap<>();
        Map<String, String> baseCompanyMiniProgramConfigMap = new HashMap<>();
        brandPoList.forEach(brandPo -> {
            //品牌对象
            tempBrandPoMap.put(brandPo.getId() + "", JsonUtil.toJson(brandPo));
            //公司品牌对象
            List<Integer> brandIdList = new ArrayList<>();
            brandIdList.add(brandPo.getId());
            if (tempCompanyBrandMap.containsKey(brandPo.getCompanyId())) {
                brandIdList.addAll(tempCompanyBrandMap.get(brandPo.getCompanyId()));
            }
            tempCompanyBrandMap.put(brandPo.getCompanyId(), brandIdList);
        });
        log.info(CLASS_LOG_PREFIX + "格式化品牌元数据完成....");

        baseCompanyMiniProgramConfigList.forEach(companyMiniProgramConfig -> {
            //小程序可用品牌对象 ex : "bradId1,brandId2"
            String enableBrandIds = companyMiniProgramConfig.getEnableBrands();
            baseCompanyMiniProgramConfigMap.put(companyMiniProgramConfig.getAppId(), JsonUtil.toJson(enableBrandIds));
        });

        //数据转换
        Map<String, String> tempCompanyBrandJsonMap = new HashMap<>(tempCompanyBrandMap.size());
        tempCompanyBrandMap.forEach((k, v) -> tempCompanyBrandJsonMap.put(k + "", JsonUtil.toJson(v)));
        log.info(CLASS_LOG_PREFIX + "格式化公司品牌Json元数据完成....");

        //装载缓存
        redisService.addMapCompatible(RedisConstant.BASE_COMPANY_MINIPROGRAM_CONFIG_MAP, baseCompanyMiniProgramConfigMap);
        log.info(CLASS_LOG_PREFIX + "装配baseCompanyMiniProgramConfigMapJson元数据完成....");
        redisService.addMapCompatible(RedisConstant.BRAND_META_DATA, tempBrandPoMap);
        redisService.addMapCompatible(RedisConstant.COMPANY_BRAND_META_DATA, tempCompanyBrandJsonMap);
        log.info(CLASS_LOG_PREFIX + "品牌元数据装载缓存完成....");

        //内存模式
        if (StorageComponent.MEMORY_MODE == STORAGE_MODE) {
            brandMap = tempBrandPoMap;
            companyBrandMap = tempCompanyBrandJsonMap;
            log.info(CLASS_LOG_PREFIX + "品牌元数据装载内存完成....");
        }
    }

    /**
     * 根据品牌ID查询品牌名
     *
     * @param brandId 品牌ID
     * @return
     */
    public String getBrandNameById(Integer brandId) {

        BrandPo brandPo = getBrandPo(brandId);
        if (brandPo == null) {
            return null;
        }

        return brandPo.getBrandName();
    }

    /**
     * 根据品牌ID查询公司ID
     *
     * @param brandId 品牌ID
     * @return
     */
    public Integer getCompanyIdByBrandId(Integer brandId) {

        BrandPo brandPo = getBrandPo(brandId);
        if (brandPo == null) {
            return null;
        }
        return brandPo.getCompanyId();
    }

    /**
     * 获取品牌对象
     *
     * @date 2018/5/15
     * @auth pengxuangang
     * @mail xuangangpeng@gmail.com
     */
    private BrandPo getBrandPo(Integer brandId) {
        if (null == brandId || 0 >= brandId) {
            return null;
        }

        String brandPoStr = getMap(RedisConstant.BRAND_META_DATA, brandId + "");

        if (StringUtils.isEmpty(brandPoStr)) {
            return null;
        }

        return JsonUtil.fromJson(brandPoStr, BrandPo.class);
    }
}
