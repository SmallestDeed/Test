package com.sandu.search.entity.elasticsearch.po.metadate;

import lombok.Data;

import java.io.Serializable;

/**
 * 产品组合持久化对象
 *
 * @date 20171225
 * @auth pengxuangang
 */
@Data
public class ProductGroupPo implements Serializable {

    private static final long serialVersionUID = 3979391867084356304L;

    //普通组合
    public final static int GROUP_TYPE_NORMAL_GROUP = 0;
    //一键装修组合
    public final static int GROUP_TYPE_ONEKEY_GROUP = 1;

    //产品组合ID
    private int id;
    //产品组合编码
    private String productGroupCode;
    //产品组合名
    private String productGroupName;
    //组合类型---这里数据库有3个组合类型，不确定用的是哪个
    private int productGroupType;
    //业务组合类型(0:普通组合, 1:一键装修组合)
    private int productBusinessGroupType;
    //合成组合类型
    private int productCompositeType;
    //组合图片ID
    private String productGroupPicId;
    //系统编码
    private String systemCode;
    //产品组合样板房ID
    private int productGroupTmelateId;
    //产品位置关系
    private String productGroupLocationData;
    //组合产品总价
    private double productGroupTotalPrice;
    //结构ID
    private int productGroupStructureId;
    //组合长度
    private String productGroupLength;
    //组合宽度
    private String productGroupWidth;
    //组合高度
    private String productGroupHeight;
    //空间类型
    private int spaceFunctionValue;
    //空间面积
    private int spaceAreaValue;
    //品牌ID
    private int brandId;
}
