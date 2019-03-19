package com.sandu.search.entity.elasticsearch.index;

import com.sandu.search.entity.elasticsearch.po.metadate.ProductGroupDetailPo;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class GroupProductIndexMappingData implements Serializable {

    /************************** 组合产品属性 ************************/
    private Integer groupId;
    //组合编码
    private String groupCode;
    //组合名
    private String groupName;
    //类型
    private Integer houseType;
    //组合状态
    private Integer state;
    //创建时间
    private Date createDate;
    //修改时间
    private Date modifyDate;
    //样板房ID
    private Integer designTempletId;
    //组合位置
    private String groupLocation;
    //组合价格
    private Double groupPrice;
    //组合标识
    private String groupFlag;
    //组合宽度
    private Integer groupWidth;
    //组合长度
    private Integer groupLength;
    //组合高度
    private Integer groupHight;
    //组合空间类型
    private Integer groupSpaceType;
    //空间面积
    private Integer groupSpaceArea;
    //品牌ID
    private Integer brandId;
    //公司ID
    private Integer companyId;
    //组合风格
    // private Integer groupStyle;
    //处理选中的风格
    private String productStyleIdInfo;
    //产品型号
    // private String productModelNumber;
    //组合风格列表
    // private String groupStyleIds;
    //组合类型(0:普通组合;1:一件装修组合)
    private Integer groupType;
    //合成类型
    private Integer compositeType;
    //是否为标准，null或1：否；2是
    private Integer isStandard;
    //备注
    private String remark;
    //是否删除
    private String isDeleted;

    //组合空间类型名称
    private String groupSpaceTypeName;
    //组合空间适用面积
    private String spaceAreaStr;
    //组合类型名称
    private String groupTypeName;
    //组合空间类型名称
    private String houseTypeName;
    //组合封面图地址
    private String picPath;
    //配置文件路径
    private String filePath;

    /********************************* 组合主产品相关属性 ***********************************/
    //组合中主产品的ID，方便U3D处理逻辑
    private Integer productId;
    //组合名,和参数name一样,方便前端
    private String productName;
    //组合产品主产品code
    private String productCode;


    /******************************* 组合产品子产品详细信息 **********************************/
    //产品组详情(产品列表)
    private List<ProductGroupDetailPo> groupProductDetails;

}
