package com.sandu.search.storage.company;

import com.sandu.search.common.constant.RedisConstant;
import com.sandu.search.common.tools.JsonUtil;
import com.sandu.search.entity.elasticsearch.po.metadate.CompanyPo;
import com.sandu.search.service.metadata.MetaDataService;
import com.sandu.search.service.redis.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 公司元数据存储
 *
 * @date 20171218
 * @auth pengxuangang
 */
@Component
public class CompanyMetaDataStorage {

    private final RedisService redisService;

    private final MetaDataService metaDataService;

    @Autowired
    public CompanyMetaDataStorage(RedisService redisService,
                                  MetaDataService metaDataService) {
        this.redisService = redisService;
        this.metaDataService = metaDataService;
    }

    /**
     * 根据公司品牌网站域名查询公司ID
     *
     * @param domainName 品牌网站域名
     * @return
     */
    public int getCompanyIdByDomainName(String domainName) {

        if (StringUtils.isEmpty(domainName)) {
            return 0;
        }

        String companyId = redisService.getMap(RedisConstant.COMPANY_DOMAIN_DATA, domainName);

        if (StringUtils.isEmpty(companyId)) {
            return 0;
        }

        return Integer.parseInt(companyId);
    }

    /**
     * 根据经销商ID查询经销商品牌列表
     *
     * @param dealerId 经销商ID
     * @return
     */
    public List<Integer> queryDealerBranIdListByDealerId(int dealerId) {
        if (0 == dealerId) {
            return null;
        }

        CompanyPo companyPo = getCompanyPoByCompanyId(dealerId);
        if (null == companyPo) {
            return null;
        }

        //品牌
        String dealerBrands = companyPo.getDealerBrands();
        if (StringUtils.isEmpty(dealerBrands)) {
            return null;
        }

        //经销商品牌字符串ID列表
        List<String> brandIdStringList = Arrays.asList(dealerBrands.split(","));
        //经销商品牌ID列表
        List<Integer> brandIdList = new ArrayList<>(brandIdStringList.size());
        //转换为整形
        brandIdStringList.forEach(brandIdString -> brandIdList.add(Integer.parseInt(brandIdString)));
        return brandIdList;
    }

    /**
     * 根据公司ID查询公司产品可见范围
     *
     * @param companyId 公司ID
     * @return
     */
    public String getCompanyProductVisibilityRangeByCompanyId(Integer companyId) {

        if (null == companyId || 0 >= companyId) {
            return null;
        }

        CompanyPo companyPo = getCompanyPoByCompanyId(companyId);

        if (null == companyPo) {
            return null;
        }

        return companyPo.getCompanyProductVisibilityRange();
    }

    /**
     * 根据小程序APPID获取公司品牌网站名
     *
     * @param appId 小程序APPID
     * @return
     */
    public String getCompanyDomanNameByMiniProgramAppId(String appId) {

        if (StringUtils.isEmpty(appId)) {
            return null;
        }

        CompanyPo companyPo = getCompanyPoByMiniProgramAppId(appId);
        if (null == companyPo) {
            return null;
        }

        return companyPo.getCompanyDomainName();
    }

    /**
     * 根据小程序APPID获取公司ID
     *
     * @param appId 小程序APPID
     * @return
     */
    public int getCompanyIdByMiniProgramAppId(String appId) {

        if (StringUtils.isEmpty(appId)) {
            return 0;
        }

        CompanyPo companyPo = getCompanyPoByMiniProgramAppId(appId);
        if (null == companyPo) {
            return 0;
        }

        return companyPo.getCompanyId();
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

        String companyPoStr = redisService.getMap(RedisConstant.COMPANY_DATA, companyId + "");

        if (StringUtils.isEmpty(companyPoStr)) {
            return null;
        }

        return JsonUtil.fromJson(companyPoStr, CompanyPo.class);
    }

    /**
     * 根据小程序ID获取公司对象
     *
     * @date 2018/6/14
     * @auth pengxuangang
     * @mail xuangangpeng@gmail.com
     */
    public CompanyPo getCompanyPoByMiniProgramAppId(String appId) {
        if (StringUtils.isEmpty(appId)) {
            return null;
        }

        String companyPoStr = redisService.getMap(RedisConstant.COMPANY_MINIPROGRAM_DATA, appId + "");

        if (StringUtils.isEmpty(companyPoStr)) {
            CompanyPo companyPo = metaDataService.getCompanyByAppId(appId);
            if (companyPo == null) {
                return null;
            }
            redisService.addMap(RedisConstant.COMPANY_MINIPROGRAM_DATA, appId + "", JsonUtil.toJson(companyPo));
            return companyPo;
        }

        return JsonUtil.fromJson(companyPoStr, CompanyPo.class);
    }

    //获取三度公司ID
    public int getSanduCompanyId() {
        String companyPoStr = redisService.getMap(RedisConstant.COMPANY_DATA, RedisConstant.COMPANY_SANDU);
        if (StringUtils.isEmpty(companyPoStr)) {
            return 0;
        }

        return JsonUtil.fromJson(companyPoStr, CompanyPo.class).getCompanyId();
    }
}
