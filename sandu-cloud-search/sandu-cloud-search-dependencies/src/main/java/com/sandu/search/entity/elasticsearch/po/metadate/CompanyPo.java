package com.sandu.search.entity.elasticsearch.po.metadate;

import lombok.Data;

import java.io.Serializable;

/**
 * 公司信息持久化对象
 *
 * @date 20180111
 * @auth pengxuangang
 */
@Data
public class CompanyPo implements Serializable {

    private static final long serialVersionUID = 5549328194846133636L;

    //公司ID
    private int companyId;
    //经销商公司ID
    private int companyPid;
    //公司编码
    private String companyCode;
    //公司名
    private String companyName;
    //公司类型
    private String businessType;
    //公司描述
    private String companyDesc;
    //公司地址
    private String companyAddress;
    //公司品牌网站名
    private String companyDomainName;
    //公司行业
    private int companyIndustry;
    //公司产品可见范围
    private String companyProductVisibilityRange;
    //经销商品牌(仅经销商类型可用)
    private String dealerBrands;
    //小程序应用ID
    private String miniProgramAppId;
    //小程序可用品牌IDS
    private String enableBrandIds;
}

