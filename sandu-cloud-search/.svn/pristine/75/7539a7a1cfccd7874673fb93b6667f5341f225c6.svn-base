package com.sandu.search.entity.elasticsearch.index;

import com.sandu.search.entity.elasticsearch.po.metadate.CompanyShopPlanPo;
import com.sandu.search.entity.elasticsearch.po.metadate.DecoratePricePo;
import com.sandu.search.entity.elasticsearch.po.metadate.DesignPlanPlatformRelPo;
import lombok.Builder;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 推荐方案索引对象
 *
 * @date 2018/5/31
 * @auth pengxuangang
 * @mail xuangangpeng@gmail.com
 */
@Data
public class RecommendationPlanIndexMappingData implements Serializable {

    private static final long serialVersionUID = -6137889115088779980L;

    /******************************** 推荐方案字段 **********************************/
    private Integer id;
    //推荐方案类型(1:分享,2:一键装修)
    private Integer type;
    //设计方案ID
    private Integer designPlanId;
    //名称
    private String name;
    //编码
    private String code;
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
    //方案编号
    private String planNumber;
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
    //商家后台上下架状态 ONEKEY 一键方案 OPEN 公开方案 多个用,隔开 ONEKEY,OPEN
    private List<String> shelfStatusList;
    //适用空间面积
    private List<String> applySpaceAreaList;
    //适用空间面积
    private List<String> applyAllSpaceAreaList;
    //空间布局标识
    private String spaceLayoutType;
    //是否发布(0:否,1:是)
    private Integer releaseStatus;
    //是否删除(0:否,1:是)
    private Integer dataIsDeleted;
    //打包方案的主方案ID
    private Integer groupPrimaryId;
    //浏览次数
    private Integer visitCount;
    //收费类型(0:免费,1:收费)
    private Integer chargeType;
    //方案价格
    private Integer planPrice;
    //方案表类型（1：推荐方案 2：全屋方案）
    private Integer planTableType;

    /******************************** 空间字段 **********************************/
    //空间ID
    private Integer spaceCommonId;
    //空间功能类型
    private Integer spaceFunctionId;
    //空间编码
    private String spaceCode;
    //空间面积
    private Integer spaceAreas;
    //空间形状
    private Integer spaceShape;

    /******************************** 扩展字段 **********************************/
    //非pc端封面图片Url 封面图太大取720缩略图
    private String coverPicPath;
    //pc端封面图片Url
    private String pcCoverPicPath;
    //渲染720图片Url
    private String resRenderPicPath;
    //品牌ID
    private List<Integer> brandIdList;
    //公司ID 和 发布关联品牌公司ID
    private List<Integer> companyIdList;
    //品牌名
    private List<String> brandNameList;
    //公司名
    private List<String> companyNameList;
    //风格名
    private String styleName;
    //房间类型名
    private String houseTypeName;
    //小区名称
    private List<String> livingNameList;
    //创建者名称
    private String createUserName;
    //用户头像地址
    private String userPicPath;
    //店铺ID
    private List<CompanyShopPlanPo> planShopInfoList;
    //方案产品IDs
    private List<Integer> productIdList;


    /******************************** 方案全平台数据 **********************************/
    //平台编码列表
    private List<String> platformCodeList;
    //2B-移动端
    private DesignPlanPlatformRelPo platformDesignPlanToBMobile;
    //2B-PC
    private DesignPlanPlatformRelPo platformDesignPlanToBPc;
    //品牌2C-网站
    private DesignPlanPlatformRelPo platformDesignPlanToCSite;
    //2C-移动端
    private DesignPlanPlatformRelPo platformDesignPlanToCMobile;
    //三度-后台管理
    private DesignPlanPlatformRelPo platformDesignPlanSanduManager;
    //户型绘制工具
    private DesignPlanPlatformRelPo platformDesignPlanHouseDraw;
    //商家管理后台
    private DesignPlanPlatformRelPo platformDesignPlanMerchantsManager;
    //测试
    private DesignPlanPlatformRelPo platformDesignPlanTest;
    //U3D转换插件
    private DesignPlanPlatformRelPo platformDesignPlanU3dPlugin;
    //小程序
    private DesignPlanPlatformRelPo platformDesignPlanMiniProgram;
    //随选网
    private DesignPlanPlatformRelPo platformDesignPlanSelectDecoration;

    /***************** 全屋方案属性 *****************/
    //全屋方案ID
    private Integer fullHouseId;
    //全屋方案UUID
    private String uuid;
    //720 UUID
    private String vrSourceUuid;
    //公开状态(1公开,0未公开)
    private Integer openState;

    /*******************装修报价************************/
    //装修报价类型列表
    private List<Integer> decoratePriceTypeList;
    //半包
    private DecoratePricePo decorateHalfPack;
    //全包
    private DecoratePricePo decorateAllPack;
    //包软装
    private DecoratePricePo decoratePackSoft;
    //清水
    private DecoratePricePo decorateWater;

    /*************The top plan sort priority******************/
    // the default value is 0, That mean this not belong to top plan list.
    private Integer ordering = 0;

}