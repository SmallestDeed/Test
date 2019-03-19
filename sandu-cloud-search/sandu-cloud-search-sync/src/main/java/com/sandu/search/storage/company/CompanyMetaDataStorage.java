package com.sandu.search.storage.company;

import com.sandu.search.common.constant.RedisConstant;
import com.sandu.search.common.tools.JsonUtil;
import com.sandu.search.entity.elasticsearch.po.metadate.CompanyPo;
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
 * 公司元数据存储
 *
 * @date 20171218
 * @auth pengxuangang
 */
@Slf4j
@Component
public class CompanyMetaDataStorage {

    private final static String CLASS_LOG_PREFIX = "公司元数据存储:";

    //默认缓存模式
    private static Integer STORAGE_MODE = StorageComponent.CACHE_MODE;

    private final RedisService redisService;
    private final MetaDataService metaDataService;

    @Autowired
    public CompanyMetaDataStorage(RedisService redisService, MetaDataService metaDataService) {
        this.redisService = redisService;
        this.metaDataService = metaDataService;
    }

    private static Map<String, String> companyMap = null;
    private static Map<String, String> companyDomainMap = null;
    private static Map<String, String> companyMiniProgramMap = null;

    //切换存储模式
    public void changeStorageMode(Integer storageMode) {
        //缓存模式
        if (StorageComponent.CACHE_MODE == storageMode) {
            //清空内存占用
            companyMap = null;
            companyDomainMap = null;
            companyMiniProgramMap = null;
            //切换
            STORAGE_MODE = storageMode;
            //内存模式
        } else if(StorageComponent.MEMORY_MODE == storageMode) {
            //切换
            STORAGE_MODE = storageMode;
            //写入内存
            updateData();
        }
        log.info(CLASS_LOG_PREFIX + "公司存储模式切换成功，当前存储:{}.", StorageComponent.CACHE_MODE == STORAGE_MODE ? "缓存" : "内存");
    }

    //获取Map数据方法兼容
    private String getMap(String mapName, String keyName) {
        //缓存模式
        if (StorageComponent.CACHE_MODE == STORAGE_MODE) {
            return redisService.getMap(mapName, keyName);
            //内存模式
        } else if (StorageComponent.MEMORY_MODE == STORAGE_MODE) {
            if (RedisConstant.COMPANY_DATA.equals(mapName)) {
                return companyMap.get(keyName);
            } else if (RedisConstant.COMPANY_DOMAIN_DATA.equals(mapName)) {
                return companyDomainMap.get(keyName);
            } else if (RedisConstant.COMPANY_MINIPROGRAM_DATA.equals(mapName)) {
                return companyMiniProgramMap.get(keyName);
            }
        }
        return null;
    }

    //更新数据
    public void updateData() {
        //获取数据
        log.info(CLASS_LOG_PREFIX + "开始获取公司元数据....");

        //公司元数据
        List<CompanyPo> companyPoList;
        try {
            companyPoList = metaDataService.queryCompanyMetaData();
        } catch (MetaDataException e) {
            log.error(CLASS_LOG_PREFIX + "获取公司元数据失败: MetaDataException:{}", e);
            throw new NullPointerException(CLASS_LOG_PREFIX + "获取公司元数据失败,List<CompanyPo> is null.MetaDataException:" + e);
        }
        log.info(CLASS_LOG_PREFIX + "获取公司元数据完成,总条数:{}", (null == companyPoList ? 0 : companyPoList.size()));

        if (null == companyPoList || 0 >= companyPoList.size()) {
            log.info(CLASS_LOG_PREFIX + "公司获取公司元数据为空，初始化获取公司元数据异常...");
            return;
        }

        //公司对象
        Map<String, CompanyPo> tempCompanyMap = new HashMap<>();
        Map<String, String> tempCompanyDomainMap = new HashMap<>();
        Map<String, CompanyPo> tempCompanyMiniProgramMap = new HashMap<>();
        companyPoList.forEach(companyPo -> {
            //三度云享家公司ID
            if ("C0000071".equals(companyPo.getCompanyCode())) {
                tempCompanyMap.put(RedisConstant.COMPANY_SANDU, companyPo);
            }

            tempCompanyMap.put(companyPo.getCompanyId() + "", companyPo);
            tempCompanyDomainMap.put(companyPo.getCompanyDomainName(), companyPo.getCompanyId() + "");
            String miniProgramAppId = companyPo.getMiniProgramAppId();
            if (!StringUtils.isEmpty(miniProgramAppId)) {
                tempCompanyMiniProgramMap.put(miniProgramAppId, companyPo);
            }
        });
        log.info(CLASS_LOG_PREFIX + "格式化公司元数据完成....");

        //转换为Json数据
        Map<String, String> companyJsonMap = new HashMap<>(tempCompanyMap.size());
        Map<String, String> companyMiniProgramJsonMap = new HashMap<>(tempCompanyMiniProgramMap.size());
        tempCompanyMap.forEach((k, v) -> companyJsonMap.put(k, JsonUtil.toJson(v)));
        tempCompanyMiniProgramMap.forEach((k, v) -> companyMiniProgramJsonMap.put(k, JsonUtil.toJson(v)));
        log.info(CLASS_LOG_PREFIX + "格式化Json公司元数据完成....");

        //装载缓存
        redisService.addMapCompatible(RedisConstant.COMPANY_DATA, companyJsonMap);
        redisService.addMapCompatible(RedisConstant.COMPANY_DOMAIN_DATA, tempCompanyDomainMap);
        redisService.addMapCompatible(RedisConstant.COMPANY_MINIPROGRAM_DATA, companyMiniProgramJsonMap);
        log.info(CLASS_LOG_PREFIX + "公司元数据装载缓存完成....");

        //内存模式
        if (StorageComponent.MEMORY_MODE == STORAGE_MODE) {
            companyMap = companyJsonMap;
            companyDomainMap = tempCompanyDomainMap;
            companyMiniProgramMap = companyMiniProgramJsonMap;
            log.info(CLASS_LOG_PREFIX + "公司元数据装载内存完成....");
        }
    }

    /**
     * 根据公司ID获取公司名
     *
     * @date 2018/6/4
     * @auth pengxuangang
     * @mail xuangangpeng@gmail.com
     */
    public String getCompanyNameByCompanyId(Integer companyId) {
        if (null == companyId || 0 >= companyId) {
            return null;
        }

        CompanyPo companyPo = getCompanyPoByCompanyId(companyId);

        if (null == companyPo) {
            return null;
        }

        return companyPo.getCompanyName();
    }


    /**
     * 根据公司ID获取公司对象
     *
     * @date 2018/6/14
     * @auth pengxuangang
     * @mail xuangangpeng@gmail.com
     */
    public CompanyPo getCompanyPoByCompanyId(Integer companyId) {
        if (null == companyId || 0 >= companyId) {
            return null;
        }

        String companyPoStr = getMap(RedisConstant.COMPANY_DATA, companyId + "");

        if (StringUtils.isEmpty(companyPoStr)) {
            return null;
        }

        return JsonUtil.fromJson(companyPoStr, CompanyPo.class);
    }
}
