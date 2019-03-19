package com.sandu.search.storage.company;

import com.alibaba.dubbo.common.json.JSON;
import com.google.gson.reflect.TypeToken;
import com.sandu.search.common.constant.RedisConstant;
import com.sandu.search.common.tools.JsonUtil;
import com.sandu.search.dao.MetaDataDao;
import com.sandu.search.entity.elasticsearch.po.BaseCompanyMiniProgramConfig;
import com.sandu.search.entity.elasticsearch.po.BrandPo;
import com.sandu.search.entity.elasticsearch.po.metadate.CompanyPo;
import com.sandu.search.exception.MetaDataException;
import com.sandu.search.service.metadata.MetaDataService;
import com.sandu.search.service.redis.RedisService;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 品牌元数据存储
 *
 * @date 20171218
 * @auth pengxuangang
 */
@Component
public class BrandMetaDataStorage {

    private final RedisService redisService;

    @Autowired
    private MetaDataService metaDataService;

    @Autowired
    public BrandMetaDataStorage(RedisService redisService) {
        this.redisService = redisService;
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
     * 根据公司ID列表查询品牌ID列表
     *
     * @param companyIdList 公司ID列表
     * @return
     */
    public List<Integer> queryBrandIdListByCompanyIdList(List<Integer> companyIdList) {

        if (null == companyIdList || 0 >= companyIdList.size()) {
            return null;
        }

        //品牌ID列表
        List<Integer> brandIdList = new ArrayList<>();
        companyIdList.forEach(companyId -> {
            if (null != companyId && 0 < companyId) {
                String brandIdListStr = redisService.getMap(RedisConstant.COMPANY_BRAND_META_DATA, companyId + "");
                List<Integer> companyBrandIdList = null;
                if (!StringUtils.isEmpty(brandIdListStr)) {
                    companyBrandIdList = JsonUtil.fromJson(brandIdListStr, new TypeToken<List<Integer>>() {}.getType());
                }
                if (null != companyBrandIdList && 0 < companyBrandIdList.size()) {
                    brandIdList.addAll(companyBrandIdList);
                }
            }
        });

        return brandIdList;
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

        String brandPoStr = redisService.getMap(RedisConstant.BRAND_META_DATA, brandId + "");

        if (StringUtils.isEmpty(brandPoStr)) {
            return null;
        }

        return JsonUtil.fromJson(brandPoStr, BrandPo.class);
    }

    public List <Integer> getEnableBrandIdsByAppId(String appId) {
        List <Integer> brandIdList = null;
        String companyPoStr = redisService.getMap(RedisConstant.COMPANY_MINIPROGRAM_DATA, appId + "");
        if (StringUtils.isEmpty(companyPoStr)) {
            String brandIds = metaDataService.getEnableBrandIdsByAppId(appId);
            if (brandIds == null || "".equals(brandIds)) {
                return null;
            }
            String[] array = brandIds.trim().split(",");
            brandIdList = new ArrayList <Integer>();
            for (String idStr : array) {
                if (StringUtils.isNotEmpty(idStr) && NumberUtils.isDigits(idStr)) {
                    brandIdList.add(Integer.valueOf(idStr));
                }
            }
            return brandIdList;
        }
        CompanyPo companyPo = JsonUtil.fromJson(companyPoStr, CompanyPo.class);
//        String enableBrandIdStrs = redisService.getMap(RedisConstant.BASE_COMPANY_MINIPROGRAM_CONFIG_MAP, appId + "");
        String enableBrandIdStrs = companyPo.getEnableBrandIds();
        if (StringUtils.isNotEmpty(enableBrandIdStrs)) {
            String[] array = enableBrandIdStrs.trim().split(",");
            brandIdList = new ArrayList <Integer>();
            for (String idStr : array) {
                if (StringUtils.isNotEmpty(idStr) && NumberUtils.isDigits(idStr)) {
                    brandIdList.add(Integer.valueOf(idStr));
                }
            }
        }
        return brandIdList;
    }
}
