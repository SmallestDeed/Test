package com.sandu.search.entity.elasticsearch.index;

import com.sandu.search.entity.elasticsearch.po.CompanyShopInfoPo;
import com.sandu.search.entity.elasticsearch.po.DecoratePriceInfoPo;
import com.sandu.search.entity.elasticsearch.po.metadate.DesignPlanPlatformRelPo;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 全屋方案索引对象
 *
 * @date 2018年9月12日 15:29
 * @auth zhangchengda
 */
@Data
public class FullHouseDesignPlanIndexMappingData implements Serializable {
    /** 全屋方案ID **/
    private Integer id;
    /** 全屋方案UUID **/
    private String uuid;
    /** 全屋方案来源类型 **/
    private Integer sourceType;
    /** 来源方案ID **/
    private Integer sourcePlanId;
    /** 全屋方案名称 **/
    private String name;
    /** 全屋方案编码 **/
    private String code;
    /** 全屋方案描述 **/
    private String describe;
    /** 设计师ID **/
    private Integer userId;
    /** 设计师名称 **/
    private String userName;
    /** 封面图ID **/
    private Integer planPicId;
    /** 封面图 **/
    private String planPicPath;
    /** 风格ID **/
    private Integer planStyleId;
    /** 风格名称 **/
    private String planStyleName;
    /** 720UUID **/
    private String vrSourceUuid;
    /** 公开状态 **/
    private Integer openState;
    /** 公开时间 **/
    private Date openTime;
    /** 方案创建时间 **/
    private Date createTime;
    /** 方案最后一次修改时间 **/
    private Date modifyTime;
    /** 是否被删除 **/
    private Integer isDeleted;
    /** 公司ID **/
    private Integer companyId;
    /** 公司名称 **/
    private String companyName;
    /** 品牌ID列表 **/
    private List<Integer> brandIdList;
    /** 品名名称列表 **/
    private List<String> brandNameList;
    /** 设计师店铺列表 **/
    private List<CompanyShopInfoPo> userShopList;
    /** 装修报价信息列表 **/
    private List<DecoratePriceInfoPo> decoratePriceInfoList;
    /** 平台编码列表 **/
    private List<String> platformCodeList;
    /** 2B-移动端 **/
    private DesignPlanPlatformRelPo platformDesignPlanToBMobile;
    /** 2B-PC **/
    private DesignPlanPlatformRelPo platformDesignPlanToBPc;
    /** 品牌2C-网站 **/
    private DesignPlanPlatformRelPo platformDesignPlanToCSite;
    /** 2C-移动端 **/
    private DesignPlanPlatformRelPo platformDesignPlanToCMobile;
    /** 三度-后台管理 **/
    private DesignPlanPlatformRelPo platformDesignPlanSanduManager;
    /** 户型绘制工具 **/
    private DesignPlanPlatformRelPo platformDesignPlanHouseDraw;
    /** 商家管理后台 **/
    private DesignPlanPlatformRelPo platformDesignPlanMerchantsManager;
    /** 测试 **/
    private DesignPlanPlatformRelPo platformDesignPlanTest;
    /** U3D转换插件 **/
    private DesignPlanPlatformRelPo platformDesignPlanU3dPlugin;
    /** 小程序 **/
    private DesignPlanPlatformRelPo platformDesignPlanMiniProgram;
    /** 随选网 **/
    private DesignPlanPlatformRelPo platformDesignPlanSelectDecoration;
}
