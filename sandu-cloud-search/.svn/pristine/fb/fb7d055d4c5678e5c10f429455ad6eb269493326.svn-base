package com.sandu.search.entity.designplan.vo;

import com.sandu.search.entity.elasticsearch.po.metadate.DecoratePricePo;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 推荐方案VO对象
 *
 * @date 2018/6/1
 * @auth pengxuangang
 * @mail xuangangpeng@gmail.com
 */
@Data
public class RecommendationPlanVo implements Serializable {

    private static final long serialVersionUID = 4544210795687880753L;

    // ID
    private Integer id;
    //推荐方案ID
    private Integer planRecommendedId;
    //设计方案ID
    private Integer planId;
    //方案名称
    private String planName;
    //推荐方案类型(1:分享,2:一键装修)
    private Integer recommendedType;
    //发布状态(1:发布,2:测试中,3:审核中)
    private Integer isRelease;
    //是否支持一件装修
    private Integer isDefaultDecorate;
    //发布时间
    private String releaseTime;
    //空间编码
    private String spaceCode;
    //空间功能类型
    private Integer spaceFunctionId;
    //空间功能类型
    private String houseTypeName;
    //空间面积
    private Integer spaceAreas;
    //空间适用面积
    private String applySpaceAreas;
    //空间形状
    private Integer spaceShape;
    //封面图片Url
    private String coverPath;
    //720Url
    private String resRenderPicPath;
    //设计样板房ID
    private Integer designTemplateId;
    //小区ID
    private Integer livingId;
    //设计风格ID
    private Integer designStyleId;
    //风格名
    private String styleName;
    //创建者BaseCompanyControllerTwoBaseCompanyControllerTwo
    private String creator;
    //来源
    private String planSource;
    //方案类型名称(智能，普通，未知)
    private String recommendedTypeName;
    //打组主键ID
    private Integer groupPrimaryId;
    //装修类型名称
    private String decorateType;
    //装修报价值
    private Integer decoratePrice;
    //收藏标识 默认为0
    private String bid = new String("0");
    //pc2b、同城联盟收藏(0未收藏，1已收藏)
    private int collectStatus;
    //小程序、移动端收藏(0未收藏，1已收藏)
    private int isFavorite;
    //是否点赞(0:否,1:已点赞)
    private int isLike;
    //收藏数
    private int collectNum;
    //点赞数
    private int likeNum;
    //浏览数量
    private int visitCount;
    //备注
    private String remark;
    //收费类型(0:免费,1:收费)
    private Integer chargeType;
    //方案价格
    private Integer planPrice;
    //用户是否已购买该方案:0.未购买 1.已购买
    private Integer havePurchased;
    //720 UUID
    private String fullHousePlanUUID;
    //设计师头像
    private String userPicPath;
    //方案表类型（1：推荐方案 2：全屋方案）
    private Integer planTableType;
    //用户是否需要购买方案版权:0.不需要;1.需要
    private Integer copyRightPermission;
    //方案装修类型集合
    private List<DecoratePricePo> planDecoratePriceList;
    // 虚拟点赞数
    private Integer virtualFavoriteNum;
    // 虚拟收藏数
    private Integer virtualLikeNum;
    // 收藏数
    private Integer favoriteNum;
}
