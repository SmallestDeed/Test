package com.sandu.search.queue.obj.designplan;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 推荐方案对象
 *
 * @date 2018/5/31
 * @auth pengxuangang
 * @mail xuangangpeng@gmail.com
 */
@Data
public class RecommendationPlanData implements Serializable {

    private static final long serialVersionUID = 4699102792957604178L;

    /******************************** 推荐方案字段 **********************************/
    private Integer id;
    //推荐方案类型(1:分享,2:一键装修)
    private Integer type;
    //设计方案ID
    private Integer designPlanId;
    //名称
    private String name;
    //设计者ID
    private Integer createUserId;
    //小区ID
    private Integer livingId;
    //户型ID
    private Integer houseId;
    //备注
    private String remark;
    //是否支持一键装修
    private Integer isSupportOneKeyDecorate;
    //封面图片ID
    private Integer coverPicId;
    //设计样板房ID
    private Integer designTemplateId;
    //设计风格ID
    private Integer designStyleId;
    //创建时间
    private Date createTime;
    //修改时间
    private Date modifyTime;
    //发布时间
    private Date publishTime;
    //方案来源
    private String planSource;
    //是否发布(0:否,1:是)
    private Integer releaseStatus;
    //是否删除(0:否,1:是)
    private Integer dataIsDeleted;

    /******************************** 空间字段 **********************************/
    //空间ID
    private Integer spaceCommonId;
    //空间功能类型
    private Integer spaceFunctionId;
    //空间面积
    private String spaceAreas;
    //空间形状
    private String spaceShape;

    /******************************** 扩展字段 **********************************/
    //封面图片Url
    private String coverPicPath;
    //品牌ID
    private Integer brandId;
    //公司ID
    private Integer companyId;
    //品牌名
    private String brandName;
    //公司名
    private String companyName;
    //风格名
    private String styleName;
    //房间类型名
    private String houseTypeName;

}