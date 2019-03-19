package com.sandu.search.entity.product.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @version V1.0
 * @Title: ResModel.java
 * @Description:系统-模型资源库
 * @createAuthor pandajun
 * @CreateDate 2015-05-19 16:05:22
 */
@Data
public class ResModel implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

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
     * 模型编码
     **/
    private String modelCode;
    /**
     * 模型名称
     **/
    private String modelName;
    /**
     * 模型文件名称
     **/
    private String modelFileName;
    /**
     * 模型文件类型
     **/
    private String modelType;
    /**
     * 模型大小
     **/
    private String modelSize;
    /**
     * 模型后缀
     **/
    private String modelSuffix;
    /**
     * 模型级别
     **/
    private String modelLevel;
    /**
     * 模型路径
     **/
    private String modelPath;
    /**
     * 模型描述
     **/
    private String modelDesc;
    /**
     * 模型排序
     **/
    private Integer modelOrdering;

    /**
     * key标识（多个）
     **/
    private String fileKeys;
    /**
     * 业务IDS
     **/
    private String businessIds;
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
     * 整数备用2
     **/
    private Integer numAtt2;
    /**
     * 数字备用1
     **/
    private Double numAtt3;
    /**
     * 归档状态
     **/
    private Double numAtt4;
    /**
     * 备注
     **/
    private String remark;
    /*添加三个字段:三个字段能获得file_type信息*/
    private String firstMenu;

    private String secondMenu;

    private String thirdMenuM;

    private int length;

    private int width;

    private int height;

    private int minHeight;
    /*是否解压,Integer,0:false;1:true*/
    private Integer isDecompress;

    private Integer businessId;

    private String fileKey;

    private String resIds;

    private List<Integer> resIdList;


    /**
     * 获取对象的copy
     **/
    public ResModel copy() {
        ResModel obj = new ResModel();
        obj.setSysCode(this.sysCode);
        obj.setCreator(this.creator);
        obj.setGmtCreate(this.gmtCreate);
        obj.setModifier(this.modifier);
        obj.setGmtModified(this.gmtModified);
        obj.setIsDeleted(this.isDeleted);
        obj.setModelCode(this.modelCode);
        obj.setModelName(this.modelName);
        obj.setModelFileName(this.modelFileName);
        obj.setModelType(this.modelType);
        obj.setModelSize(this.modelSize);
        obj.setModelSuffix(this.modelSuffix);
        obj.setModelLevel(this.modelLevel);
        obj.setModelPath(this.modelPath);
        obj.setModelDesc(this.modelDesc);
        obj.setModelOrdering(this.modelOrdering);
        obj.setFileKey(this.fileKey);
        obj.setFileKeys(this.fileKeys);
        obj.setBusinessIds(this.businessIds);
        obj.setAtt4(this.att4);
        obj.setAtt5(this.att5);
        obj.setAtt6(this.att6);
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
        map.put("gmtCreate", this.gmtCreate);
        map.put("modifier", this.modifier);
        map.put("gmtModified", this.gmtModified);
        map.put("isDeleted", this.isDeleted);
        map.put("modelCode", this.modelCode);
        map.put("modelName", this.modelName);
        map.put("modelFileName", this.modelFileName);
        map.put("modelType", this.modelType);
        map.put("modelSize", this.modelSize);
        map.put("modelSuffix", this.modelSuffix);
        map.put("modelLevel", this.modelLevel);
        map.put("modelPath", this.modelPath);
        map.put("modelDesc", this.modelDesc);
        map.put("modelOrdering", this.modelOrdering);
        map.put("fileKey", this.fileKey);
        map.put("fileKeys", this.fileKeys);
        map.put("businessIds", this.businessIds);
        map.put("att4", this.att4);
        map.put("att5", this.att5);
        map.put("att6", this.att6);
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
