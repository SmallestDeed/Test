package com.sandu.search.entity.product.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @version V1.0
 * @Title: ResPic.java
 * @Description:系统-图片资源库
 * @createAuthor pandajun
 * @CreateDate 2015-05-19 16:06:59
 */
@Data
public class ResPic implements Serializable {
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
     * 图片编码
     **/
    private String picCode;
    /**
     * 图片名称
     **/
    private String picName;
    /**
     * 图片文件名称
     **/
    private String picFileName;
    /**
     * 图片类型
     **/
    private String picType;
    /**
     * 图片大小
     **/
    private Integer picSize;
    /**
     * 图片长
     **/
    private String picWeight;
    /**
     * 图片高
     **/
    private String picHigh;
    /**
     * 图片后缀
     **/
    private String picSuffix;
    /**
     * 图片级别
     **/
    private String picLevel;
    /**
     * 图片格式
     **/
    private String picFormat;
    /**
     * 图片路径
     **/
    private String picPath;
    /**
     * 图片描述
     **/
    private String picDesc;
    /**
     * 图片排序
     **/
    private String picOrdering;

    /**
     * key标识（多个）
     **/
    private String fileKeys;
    /**
     * 标识IDs
     **/
    private String businessIds;
    /**
     * 图片对应的缩略图id信息:small_pic_info
     **/
    private String smallPicInfo;
    /**
     * 渲染图视角
     **/
    private Integer viewPoint;
    /**
     * 渲染图场景
     **/
    private Integer scene;
    /**
     * 时间备用1
     **/
    private Date dateAtt1;
    /**
     * 时间备用2
     **/
    private Date dateAtt2;

    /**
     * 整数备用2
     **/
    private Integer numAtt2;
    /**
     * 数字备用1
     **/
    private Double numAtt3;
    /**
     * 归档标志
     **/
    private Double numAtt4;
    /**
     * 备注
     **/
    private String remark;

    /*添加三个字段:三个字段能获得file_type信息*/
    private String firstMenu;

    private String secondMenu;

    private String thirdMenuP;

    private String OriginalPicPath;
    //记录图片文件排序序号
    private Integer sequence;

    //ipad缩略图路径
    private String ipadThumbnailPath;

    //web缩略图路径
    private String webThumbnailPath;
    /**
     * 渲染图片类型
     **/
    private String renderingType;
    /**
     * 全景路径
     **/
    private String panoPath;
    /**
     * 原图ID。缩略图时使用
     **/
    private Integer baseRenderId;

    private Integer sysTaskPicId;
    //产品主键ID
    private Integer productId;

    private Integer businessId;

    private String fileKey;

    private String resIds;

    private List<Integer> resIdList;


    /**
     * 获取对象的copy
     **/
    public ResPic copy() {
        ResPic obj = new ResPic();
        obj.setSysCode(this.sysCode);
        obj.setCreator(this.creator);
        obj.setSysTaskPicId(this.sysTaskPicId);
        obj.setGmtCreate(this.gmtCreate);
        obj.setModifier(this.modifier);
        obj.setGmtModified(this.gmtModified);
        obj.setIsDeleted(this.isDeleted);
        obj.setPicCode(this.picCode);
        obj.setPicName(this.picName);
        obj.setPicFileName(this.picFileName);
        obj.setPicType(this.picType);
        obj.setPicSize(this.picSize);
        obj.setPicWeight(this.picWeight);
        obj.setPicHigh(this.picHigh);
        obj.setPicSuffix(this.picSuffix);
        obj.setPicLevel(this.picLevel);
        obj.setPicFormat(this.picFormat);
        obj.setPicPath(this.picPath);
        obj.setPicDesc(this.picDesc);
        obj.setPicOrdering(this.picOrdering);
        obj.setFileKey(this.fileKey);
        obj.setFileKeys(this.fileKeys);
        obj.setBusinessIds(this.businessIds);
        obj.setSmallPicInfo(this.smallPicInfo);
        obj.setViewPoint(this.viewPoint);
        obj.setScene(this.scene);
        obj.setDateAtt1(this.dateAtt1);
        obj.setDateAtt2(this.dateAtt2);
        obj.setBusinessId(this.businessId);
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
        map.put("sysTaskPicId", this.sysTaskPicId);
        map.put("gmtCreate", this.gmtCreate);
        map.put("modifier", this.modifier);
        map.put("gmtModified", this.gmtModified);
        map.put("isDeleted", this.isDeleted);
        map.put("picCode", this.picCode);
        map.put("picName", this.picName);
        map.put("picFileName", this.picFileName);
        map.put("picType", this.picType);
        map.put("picSize", this.picSize);
        map.put("picWeight", this.picWeight);
        map.put("picHigh", this.picHigh);
        map.put("picSuffix", this.picSuffix);
        map.put("picLevel", this.picLevel);
        map.put("picFormat", this.picFormat);
        map.put("picPath", this.picPath);
        map.put("picDesc", this.picDesc);
        map.put("picOrdering", this.picOrdering);
        map.put("fileKey", this.fileKey);
        map.put("fileKeys", this.fileKeys);
        map.put("businessIds", this.businessIds);
        map.put("smallPicInfo", this.smallPicInfo);
        map.put("viewPoint", this.viewPoint);
        map.put("scene", this.scene);
        map.put("dateAtt1", this.dateAtt1);
        map.put("dateAtt2", this.dateAtt2);
        map.put("businessId", this.businessId);
        map.put("numAtt2", this.numAtt2);
        map.put("numAtt3", this.numAtt3);
        map.put("numAtt4", this.numAtt4);
        map.put("remark", this.remark);

        return map;
    }


}
