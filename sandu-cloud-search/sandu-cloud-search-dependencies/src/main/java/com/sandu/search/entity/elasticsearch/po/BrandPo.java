package com.sandu.search.entity.elasticsearch.po;

import lombok.Data;

import java.io.Serializable;

/**
 * 品牌持久化对象
 *
 * @date 20171218
 * @auth pengxuangang
 */
@Data
public class BrandPo implements Serializable {

    private static final long serialVersionUID = -6764847582430931805L;

    //不显示'无品牌'
    public static final int SHOW_NO_BRAND_NO = 0;
    //显示'无品牌'
    public static final int SHOW_NO_BRAND_YES = 1;

    //品牌ID
    private int id;
    //品牌名
    private String brandName;
    //公司ID
    private int companyId;
    //公司Logo图片ID
    private String brandLogoPicId;
    //品牌描述
    private String brandDesc;
    //是否显示‘无品牌’
    private int showNoBrand;

}
