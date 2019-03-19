package com.sandu.search.entity.product.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.*;

/**
 * @version V1.0
 * @Title: ResTexture.java
 * @Description:系统模块-材质库
 * @createAuthor pandajun
 * @CreateDate 2016-06-30 14:10:42
 */
@Data
public class ResTexture implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;
    /*id集合*/
    List<String> resTextureIds = new ArrayList<String>();
    /**
     * 文件手动命名
     **/
    private String name;
    /**
     * 文件名
     **/
    private String fileName;
    /**
     * 类型
     **/
    private String type;
    /**
     * 编码
     **/
    private String fileCode;
    /**
     * 文件大小
     **/
    private Integer fileSize;
    /**
     * 图片宽
     **/
    private Integer fileWidth;
    /**
     * 图片高
     **/
    private Integer fileHeight;
    /**
     * 图片格式
     **/
    private String fileSuffix;
    /**
     * 材质图片路径
     **/
    private String filePath;
    /**
     * 材质描述
     **/
    private String fileDesc;
    /**
     * 风格
     **/
    private String style;
    /**
     * 材质(材料)
     **/
    private String texture;
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
    /*关联图片id*/
    private Integer picId;
    /*材质属性*/
    private Integer textureAttrValue;
    /*铺设方式*/
    private String laymodes;
    /**
     * 缩略图
     **/
    private Integer thumbnailId;
    //法线贴图
    private Integer normalPicId;
    //法线参数
    private String normalParam;

    private String normalPath;
    //材质球文件Id
    private Integer textureBallFileId;
    //材质球文件名称
    private String textureBallFileName;
    //材质球文件路径
    private String materialPath;
    //品牌id
    private Integer brandId;
    /**
     * 品牌名称
     */
    private String brandName;
    //材质编号
    private String textureCode;


    /**
     * 获取对象的copy
     **/
    public ResTexture copy() {
        ResTexture obj = new ResTexture();
        obj.setName(this.name);
        obj.setFileName(this.fileName);
        obj.setType(this.type);
        obj.setFileCode(this.fileCode);
        obj.setFileSize(this.fileSize);
        obj.setFileWidth(this.fileWidth);
        obj.setFileHeight(this.fileHeight);
        obj.setFileSuffix(this.fileSuffix);
        obj.setFilePath(this.filePath);
        obj.setFileDesc(this.fileDesc);
        obj.setStyle(this.style);
        obj.setTexture(this.texture);
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
        map.put("name", this.name);
        map.put("fileName", this.fileName);
        map.put("type", this.type);
        map.put("fileCode", this.fileCode);
        map.put("fileSize", this.fileSize);
        map.put("fileWidth", this.fileWidth);
        map.put("fileHeight", this.fileHeight);
        map.put("fileSuffix", this.fileSuffix);
        map.put("filePath", this.filePath);
        map.put("fileDesc", this.fileDesc);
        map.put("style", this.style);
        map.put("texture", this.texture);
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
