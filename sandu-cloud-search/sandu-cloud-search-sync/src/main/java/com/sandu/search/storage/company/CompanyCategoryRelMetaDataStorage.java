package com.sandu.search.storage.company;

import com.sandu.search.common.constant.RedisConstant;
import com.sandu.search.common.tools.JsonUtil;
import com.sandu.search.entity.elasticsearch.po.metadate.CompanyCategoryRelPo;
import com.sandu.search.exception.MetaDataException;
import com.sandu.search.service.metadata.MetaDataService;
import com.sandu.search.service.redis.RedisService;
import com.sandu.search.storage.StorageComponent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 公司分类关联元数据存储
 *
 * @date 20171218
 * @auth pengxuangang
 */
@Slf4j
@Component
public class CompanyCategoryRelMetaDataStorage {

    private final static String CLASS_LOG_PREFIX = "公司分类关联元数据存储:";

    //默认缓存模式
    private static Integer STORAGE_MODE = StorageComponent.CACHE_MODE;

    private final RedisService redisService;
    private final MetaDataService metaDataService;

    @Autowired
    public CompanyCategoryRelMetaDataStorage(RedisService redisService, MetaDataService metaDataService) {
        this.redisService = redisService;
        this.metaDataService = metaDataService;
    }

    private static Map<String, String> companyCategoryrelMap = null;

    //切换存储模式
    public void changeStorageMode(Integer storageMode) {
        //缓存模式
        if (StorageComponent.CACHE_MODE == storageMode) {
            //清空内存占用
            companyCategoryrelMap = null;
            //切换
            STORAGE_MODE = storageMode;
            //内存模式
        } else if(StorageComponent.MEMORY_MODE == storageMode) {
            //切换
            STORAGE_MODE = storageMode;
            //写入内存
            updateData();
            //companyCategoryrelMap = redisService.getMap(RedisConstant.COMPANY_CATEGORY_REL_DATA);
        }
        log.info(CLASS_LOG_PREFIX + "公司分类关联存储模式切换成功，当前存储:{}.", StorageComponent.CACHE_MODE == STORAGE_MODE ? "缓存" : "内存");
    }

    //获取Map数据方法兼容
    private String getMap(String mapName, String keyName) {
        //缓存模式
        if (StorageComponent.CACHE_MODE == STORAGE_MODE) {
            return redisService.getMap(mapName, keyName);
            //内存模式
        } else if (StorageComponent.MEMORY_MODE == STORAGE_MODE) {
            if (RedisConstant.COMPANY_CATEGORY_REL_DATA.equals(mapName)) {
                return companyCategoryrelMap.get(keyName);
            }
        }
        return null;
    }

    //更新数据
    public void updateData() {

        //获取数据
        log.info(CLASS_LOG_PREFIX + "开始获取公司分类关联元数据....");

        //公司分类关联元数据
        List<CompanyCategoryRelPo> companyCategoryRelPoList;
        try {
            companyCategoryRelPoList = metaDataService.queryCompanyCategoryRelMetaData();
        } catch (MetaDataException e) {
            log.error(CLASS_LOG_PREFIX + "获取公司分类关联元数据失败: MetaDataException:{}", e);
            throw new NullPointerException(CLASS_LOG_PREFIX + "获取公司分类关联元数据失败,List<CompanyCategoryRelPo> is null.MetaDataException:" + e);
        }
        log.info(CLASS_LOG_PREFIX + "获取公司分类关联元数据完成,总条数:{}", (null == companyCategoryRelPoList ? 0 : companyCategoryRelPoList.size()));

        if (null == companyCategoryRelPoList || 0 >= companyCategoryRelPoList.size()) {
            log.info(CLASS_LOG_PREFIX + "公司分类关联元数据为空，初始化公司分类关联数据异常...");
            return;
        }

        //公司分类关联对象
        Map<Integer, List<Integer>> companyCategoryRelMap = new HashMap<>();
        if (null != companyCategoryRelPoList && 0 != companyCategoryRelPoList.size()) {
            companyCategoryRelPoList.forEach(companyPo -> {
                int companyId = companyPo.getCompanyId();
                if (companyCategoryRelMap.containsKey(companyId)) {
                    List<Integer> categoryList = new ArrayList<>();
                    categoryList.add(companyPo.getCategoryId());
                    categoryList.addAll(companyCategoryRelMap.get(companyId));
                    companyCategoryRelMap.put(companyId, categoryList);
                } else {
                    companyCategoryRelMap.put(companyId, Collections.singletonList(companyPo.getCategoryId()));
                }
            });
        }
        log.info(CLASS_LOG_PREFIX + "格式化公司分类关联元数据完成....");

        //数据格式转换为json
        Map<String, String> companyCategoryRelJsonMap = new HashMap<>();
        companyCategoryRelMap.forEach((k, v) -> companyCategoryRelJsonMap.put(k + "", JsonUtil.toJson(v)));
        log.info(CLASS_LOG_PREFIX + "格式化Json公司分类关联元数据完成....");

        //装载缓存
        redisService.addMapCompatible(RedisConstant.COMPANY_CATEGORY_REL_DATA, companyCategoryRelJsonMap);
        log.info(CLASS_LOG_PREFIX + "公司分类关联元数据装载缓存完成....");

        //内存模式
        if (StorageComponent.MEMORY_MODE == STORAGE_MODE) {
            companyCategoryrelMap = companyCategoryRelJsonMap;
            log.info(CLASS_LOG_PREFIX + "公司分类关联元数据装载内存完成....");
        }
    }
}
