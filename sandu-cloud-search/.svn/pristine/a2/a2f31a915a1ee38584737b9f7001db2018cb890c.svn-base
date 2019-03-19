package com.sandu.search.entity.product.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.*;

/**
 * @version V1.0
 * @Title: BaseBrand.java
 * @Description:产品-品牌表
 * @createAuthor pandajun
 * @CreateDate 2015-06-16 10:03:47
 */
@Data
public class BaseBrand implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer id;

    /**
     * 系统编码
     **/
    private String sysCode;
    /**
     * 创建者
     **/
    private String creator;
    /**
     * 创建时间
     **/
    private Date gmtCreate;
    /**
     * 修改人
     **/
    private String modifier;
    /**
     * 修改时间
     **/
    private Date gmtModified;
    /**
     * 是否删除
     **/
    private Integer isDeleted;
    /**
     * 品牌编码
     **/
    private String brandCode;
    /**
     * 品牌名称
     **/
    private String brandName;
    private String brandReferred;

    private List<Integer> brandIds = new ArrayList<Integer>();
    /**
     * 是否显示无品牌开关(0:默认显示无品牌,1:不显示无品牌)
     * add by huangsongbo 2017.11.4
     */
    private Integer statusShowWu;
    /**
     * companyIndustry 公司所属行业
     * companySmallType 公司细分行业
     * authorizedBigType 授权码大分类
     * authorizedSmallTypeIds 授权码小分类
     * authorizedProductIds 授权码产品ids
     * companyProductSmallValueKeys 公司细分行业产品小分类keys
     * add by xiaoxc 20171118
     */
    private Integer companyIndustry;
    private Integer companySmallType;
    private String authorizedBigType;
    private String authorizedSmallTypeIds;
    private String authorizedProductIds;
    private String companyProductSmallValueKeys;
    /**
     * 企业ID
     **/
    private Integer companyId;
    private Integer brandStyleId;
    private String brandStyleName;
    /**
     * 品牌logo
     **/
    private String brandLogo;

    private String brandLogoPath;
    /**
     * 品牌介绍
     **/
    private String brandDesc;
    /**
     * 字符备用1
     **/
    private String att1;
    /**
     * 字符备用2
     **/
    private String att2;
    /**
     * 字符备用3
     **/
    private String att3;
    /**
     * 字符备用4
     **/
    private String att4;
    /**
     * 字符备用5
     **/
    private String att5;
    /**
     * 字符备用6
     **/
    private String att6;
    /**
     * 时间备用1
     **/
    private Date dateAtt1;
    /**
     * 时间备用2
     **/
    private Date dateAtt2;
    /**
     * 整数备用1
     **/
    private Integer numAtt1;
    /**
     * 整数备用2
     **/
    private Integer numAtt2;
    /**
     * 数字备用1
     **/
    private Double numAtt3;
    /**
     * 数字备用2
     **/
    private Double numAtt4;
    /**
     * 备注
     **/
    private String remark;
    //公司名称
    private String companyName;
    /**
     * 方案推荐专用  Begin >>
     */
    /*design_plan_brand 表中的关联brand_id ,-1为所有品牌*/
    private Integer brandAssociatedId;
    /* 品牌id*/
    private Integer brandId;
    /*design_plan_brand 表主键Id*/
    private Integer designPlanBrandId;

    /**
     * 方案推荐专用  End  <<
     */


    /**
     * 获取对象的copy
     **/
    public BaseBrand copy() {
        BaseBrand obj = new BaseBrand();
        obj.setSysCode(this.sysCode);
        obj.setCreator(this.creator);
        obj.setGmtCreate(this.gmtCreate);
        obj.setModifier(this.modifier);
        obj.setGmtModified(this.gmtModified);
        obj.setIsDeleted(this.isDeleted);
        obj.setBrandCode(this.brandCode);
        obj.setBrandName(this.brandName);
        obj.setCompanyId(this.companyId);
        obj.setBrandLogo(this.brandLogo);
        obj.setBrandDesc(this.brandDesc);
        obj.setAtt1(this.att1);
        obj.setAtt2(this.att2);
        obj.setAtt3(this.att3);
        obj.setAtt4(this.att4);
        obj.setAtt5(this.att5);
        obj.setAtt6(this.att6);
        obj.setDateAtt1(this.dateAtt1);
        obj.setDateAtt2(this.dateAtt2);
        obj.setNumAtt1(this.numAtt1);
        obj.setNumAtt2(this.numAtt2);
        obj.setNumAtt3(this.numAtt3);
        obj.setNumAtt4(this.numAtt4);
        obj.setRemark(this.remark);

        return obj;
    }

    /**
     * 获取对象的map
     **/
    public Map toMap() {
        Map map = new HashMap();
        map.put("sysCode", this.sysCode);
        map.put("creator", this.creator);
        map.put("gmtCreate", this.gmtCreate);
        map.put("modifier", this.modifier);
        map.put("gmtModified", this.gmtModified);
        map.put("isDeleted", this.isDeleted);
        map.put("brandCode", this.brandCode);
        map.put("brandName", this.brandName);
        map.put("companyId", this.companyId);
        map.put("brandLogo", this.brandLogo);
        map.put("brandDesc", this.brandDesc);
        map.put("att1", this.att1);
        map.put("att2", this.att2);
        map.put("att3", this.att3);
        map.put("att4", this.att4);
        map.put("att5", this.att5);
        map.put("att6", this.att6);
        map.put("dateAtt1", this.dateAtt1);
        map.put("dateAtt2", this.dateAtt2);
        map.put("numAtt1", this.numAtt1);
        map.put("numAtt2", this.numAtt2);
        map.put("numAtt3", this.numAtt3);
        map.put("numAtt4", this.numAtt4);
        map.put("remark", this.remark);

        return map;
    }
}
