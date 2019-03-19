package com.sandu.search.entity.product.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @version V1.0
 * @Title: ProductAttribute.java
 * @Description:产品模块-产品属性
 * @createAuthor pandajun
 * @CreateDate 2015-09-01 13:17:36
 */
@Data
public class ProductAttribute implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer id;
    /**
     * 产品小分类（产品属性父级）
     **/
    private String attributeTypeValue;
    /**
     * 属性值
     **/
    private Integer attributeId;
    /**
     * 属性key
     **/
    private String attributeKey;
    /**
     * 属性名称
     **/
    private String attributeName;
    /**
     * 属性值value
     **/
    private Integer attributeValueId;
    /**
     * 属性值key
     **/
    private String attributeValueKey;
    /**
     * 属性值名称
     **/
    private String attributeValueName;
    /**
     * 属性值图片
     **/
    private String attributeValuePicIds;
    /**
     * 产品ID
     **/
    private Integer productId;
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
     * 字符备用1
     **/
    private String productCode;
    /**
     * 字符备用2
     **/
    private String att2;
    /**
     * 整数备用1
     **/
    private Integer numa1;
    /**
     * 整数备用2
     **/
    private Integer numa2;
    /**
     * 备注
     **/
    private String remark;
    /**
     * 属性值
     **/
    private Integer propValue;

    /**
     * 对应product_props表的数据的父属性的filterOrder字段
     * product_attribute->找出对应的product_props->根据pid找出上级属性product_props->获得filterOrder字段
     */
    private String parentFilterOrder;

    private String productTypeName;

    private List<Integer> productIdList;

    public Integer state;


    /**
     * 获取对象的copy
     **/
    public ProductAttribute copy() {
        ProductAttribute obj = new ProductAttribute();
        obj.setAttributeTypeValue(this.attributeTypeValue);
        obj.setAttributeId(this.attributeId);
        obj.setAttributeKey(this.attributeKey);
        obj.setAttributeName(this.attributeName);
        obj.setAttributeValueId(this.attributeValueId);
        obj.setAttributeValueKey(this.attributeValueKey);
        obj.setAttributeValueName(this.attributeValueName);
        obj.setAttributeValuePicIds(this.attributeValuePicIds);
        obj.setProductId(this.productId);
        obj.setSysCode(this.sysCode);
        obj.setCreator(this.creator);
        obj.setGmtCreate(this.gmtCreate);
        obj.setModifier(this.modifier);
        obj.setGmtModified(this.gmtModified);
        obj.setIsDeleted(this.isDeleted);
        obj.setProductCode(this.productCode);
        obj.setAtt2(this.att2);
        obj.setNuma1(this.numa1);
        obj.setNuma2(this.numa2);
        obj.setRemark(this.remark);

        return obj;
    }

    /**
     * 获取对象的map
     **/
    public Map toMap() {
        Map map = new HashMap();
        map.put("attributeTypeValue", this.attributeTypeValue);
        map.put("attributeId", this.attributeId);
        map.put("attributeKey", this.attributeKey);
        map.put("attributeName", this.attributeName);
        map.put("attributeValueId", this.attributeValueId);
        map.put("attributeValueKey", this.attributeValueKey);
        map.put("attributeValueName", this.attributeValueName);
        map.put("attributeValuePicIds", this.attributeValuePicIds);
        map.put("productId", this.productId);
        map.put("sysCode", this.sysCode);
        map.put("creator", this.creator);
        map.put("gmtCreate", this.gmtCreate);
        map.put("modifier", this.modifier);
        map.put("gmtModified", this.gmtModified);
        map.put("isDeleted", this.isDeleted);
        map.put("productCode", this.productCode);
        map.put("att2", this.att2);
        map.put("numa1", this.numa1);
        map.put("numa2", this.numa2);
        map.put("remark", this.remark);

        return map;
    }


}
