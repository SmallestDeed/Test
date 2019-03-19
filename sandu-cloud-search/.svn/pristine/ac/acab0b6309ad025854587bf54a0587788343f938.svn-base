package com.sandu.search.common.tools;

import com.sandu.search.common.constant.HeaderConstant;
import com.sandu.search.storage.company.CompanyMetaDataStorage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * 品牌网站品牌名处理类
 *
 * @date 20171220
 * @auth pengxuangang
 */
@Component
public class DomainUtil {

    private final CompanyMetaDataStorage companyMetaDataStorage;

    @Autowired
    public DomainUtil(CompanyMetaDataStorage companyMetaDataStorage) {
        this.companyMetaDataStorage = companyMetaDataStorage;
    }

    /**
     * 根据品牌网站HOST获取品牌网站名
     *
     * @param request 请求
     * @return
     */
    public static String getDomainNameByHost(HttpServletRequest request) throws NullPointerException {

        //第一种Referer方式
        String domainName = getDomainString(request.getHeader(HeaderConstant.REFERER));

        if (null == domainName || "".equals(domainName) || "null".equals(domainName) || 0 < domainName.indexOf("\\.")) {
            //第二种Custom_Referer方式
            domainName = getDomainString(request.getHeader(HeaderConstant.CUSTOM_REFERER));
        }

        return domainName;
    }

    /**
     * 根据请求获取小程序公司ID
     *
     * @param request 请求
     * @return
     */
    public int getCompanyIdInMiniProgramByRequest(HttpServletRequest request) throws NullPointerException {

        //第一种Referer方式
        int companyId = getCompanyIdFromMiniProgramUrl(request.getHeader(HeaderConstant.REFERER));
        if (0 == companyId) {
            companyId = getCompanyIdFromMiniProgramUrl(request.getHeader(HeaderConstant.CUSTOM_REFERER));
        }

        return companyId;
    }


    /**
     * 获取域名数据
     *
     * @param referer 字段
     * @return
     */
    public static String getDomainString(String referer) {
        String domainName = null;
        if (null != referer && !"".equals(referer)) {
            try {
                domainName = referer.substring(referer.indexOf("//") + 2, referer.indexOf("."));
            } catch (Exception ex) {
                throw new NullPointerException("Can't read referer the param in the head. please check request is complate.");
            }
        }
        return domainName;
    }

    /**
     * 获取小程序公司ID
     *
     * @param referer   字段
     * @return
     */
    public int getCompanyIdFromMiniProgramUrl(String referer) {

        int companyId = 0;
        if (null != referer && !"".equals(referer) && (
                referer.startsWith("http://servicewechat.com/")
                        || referer.startsWith("https://servicewechat.com/")
            )) {
            //去掉前缀
            referer = referer.substring(referer.indexOf("//servicewechat.com/") + 20, referer.length());
            //去掉后缀
            referer = referer.substring(0, referer.indexOf("/"));

            if (!"".equals(referer) && 0 > referer.indexOf("\\.")) {
                companyId = companyMetaDataStorage.getCompanyIdByMiniProgramAppId(referer);
            }
        }

        return companyId;
    }


    public String getAppIdFromMiniProgramByRequest(HttpServletRequest request) {
        String appId = null;
        String customerRefer = request.getHeader(HeaderConstant.REFERER);
        if (null == customerRefer && "".equals(customerRefer)) {
            customerRefer = request.getHeader(HeaderConstant.CUSTOM_REFERER);
        }
        if (null != customerRefer && !"".equals(customerRefer) && (
                customerRefer.startsWith("http://servicewechat.com/")
                        || customerRefer.startsWith("https://servicewechat.com/")
        )) {
            //去掉前缀
            customerRefer = customerRefer.substring(customerRefer.indexOf("//servicewechat.com/") + 20, customerRefer.length());
            //去掉后缀
            appId = customerRefer.substring(0, customerRefer.indexOf("/"));

            if (!"".equals(appId) && 0 > appId.indexOf("\\.")) {
                return appId;
            }
        }
        return appId;
    }
}