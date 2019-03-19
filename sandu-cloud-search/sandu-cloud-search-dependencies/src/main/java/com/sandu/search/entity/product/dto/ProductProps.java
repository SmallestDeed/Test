package com.sandu.search.entity.product.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @version V1.0
 * @Title: ProductProps.java
 * @Description:产品属性-产品属性表
 * @createAuthor pandajun
 * @CreateDate 2015-09-01 10:40:03
 */
@Data
public class ProductProps implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer id;
    /**
     * 属性CODE
     **/
    private String code;
    /**
     * 属性长CODE
     **/
    private String longCode;
    /**
     * 属性名称
     **/
    private String name;
    /**
     * 属性值
     **/
    private String propValue;
    /**
     * 图片
     **/
    private Integer picPath;
    /**
     * 父级ID
     **/
    private Integer pid;
    /**
     * 类型
     **/
    private Integer type;
    /**
     * 等级
     **/
    private Integer level;
    /**
     * 是否子级
     **/
    private Integer isLeaf;
    /**
     * 排序
     **/
    private Integer ordering;
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
    private String att1;
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
     * 父级
     **/
    private ProductProps parentProps;

    /**
     * 图片路径
     **/
    private String picPathVal;

    /**
     * 颜色名称
     **/
    private String colorName;

    /**
     * 材质名称
     **/
    private String materialName;
    /**
     * （排序，过滤）
     **/
    private String filterOrder;
    //属性序号
    private Integer sequenceNumber;
    // 属性长序号
    private String longNumbers;

    private String parentCode;
    private Integer productId;


    /**
     * 获取对象的copy
     **/
    public ProductProps copy() {
        ProductProps obj = new ProductProps();
        obj.setCode(this.code);
        obj.setRemark(this.longCode);
        obj.setName(this.name);
        obj.setPropValue(this.propValue);
        obj.setPicPath(this.picPath);
        obj.setPid(this.pid);
        obj.setType(this.type);
        obj.setLevel(this.level);
        obj.setIsLeaf(this.isLeaf);
        obj.setOrdering(this.ordering);
        obj.setSysCode(this.sysCode);
        obj.setCreator(this.creator);
        obj.setGmtCreate(this.gmtCreate);
        obj.setModifier(this.modifier);
        obj.setGmtModified(this.gmtModified);
        obj.setIsDeleted(this.isDeleted);
        obj.setAtt1(this.att1);
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
        map.put("code", this.code);
        map.put("code", this.longCode);
        map.put("name", this.name);
        map.put("propValue", this.propValue);
        map.put("picPath", this.picPath);
        map.put("pid", this.pid);
        map.put("type", this.type);
        map.put("level", this.level);
        map.put("isLeaf", this.isLeaf);
        map.put("ordering", this.ordering);
        map.put("sysCode", this.sysCode);
        map.put("creator", this.creator);
        map.put("gmtCreate", this.gmtCreate);
        map.put("modifier", this.modifier);
        map.put("gmtModified", this.gmtModified);
        map.put("isDeleted", this.isDeleted);
        map.put("att1", this.att1);
        map.put("att2", this.att2);
        map.put("numa1", this.numa1);
        map.put("numa2", this.numa2);
        map.put("remark", this.remark);

        return map;
    }
}
