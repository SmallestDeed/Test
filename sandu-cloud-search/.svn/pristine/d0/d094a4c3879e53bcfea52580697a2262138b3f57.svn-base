package com.sandu.search.entity.designplan.po;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 推荐方案持久化对象
 *
 * @date 2018/5/30
 * @auth pengxuangang
 * @mail xuangangpeng@gmail.com
 */
@Data
public class RecommendationPlanPo implements Serializable {

    private static final long serialVersionUID = -1755077915064748546L;

    //推荐方案类型-分享/样板间
    public static final int SHARE_RECOMMENDATION_PLAN_TYPE = 1;
    //推荐方案类型-一键装修
    public static final int ONEKEY_RECOMMENDATION_PLAN_TYPE = 2;

    //推荐方案类型-普通
    public static final String RECOMMENDATION_PLAN_TYPE_COMMON = "普通";
    //推荐方案类型-智能
    public static final String RECOMMENDATION_PLAN_TYPE_CAPACITY = "智能";
    //推荐方案类型-未知
    public static final String RECOMMENDATION_PLAN_TYPE_UNKNOWN = "未知";

    //设计方案是否发布-否
    public static final int DESIGN_PLAN_NOT_IS_PUBLISH = 0;
    //设计方案是否发布-是
    public static final int DESIGN_PLAN_IS_PUBLISH = 1;
    //设计方案是否发布-测试
    public static final int DESIGN_PLAN_TEST_IS_PUBLISH = 2;
    //设计方案是否发布-审核
    public static final int DESIGN_PLAN_AUDIT_IS_PUBLISH = 3;

    //自己制作方案来源
    public static final String DIY_PLAN_SOURCE = "diy";
    //分享方案来源
    public static final String SHARE_PLAN_SOURCE = "share";
    //交付方案来源
    public static final String DELIVER_PLAN_SOURCE = "deliver";
    //一键方案标识
    public static final String ONEKEY_PLAN_SIGN = "ONEKEY";
    //公开方案标识
    public static final String OPEN_PLAN_SIGN = "OPEN";
    //样板间标识
    public static final String TEMPLATE_PLAN_SIGN = "TEMPLATE";

    /********************** 根据类型查询不同列表 ************************/
    // 一键方案类型
    public static final String FUNCTION_TYPE_DECORATE = "decorate";
    // 公开样板间方案
    public static final String FUNCTION_TYPE_PUBLIC = "public";
    // 样板方案
    public static final String FUNCTION_TYPE_PROTOTYPE = "prototype";
    // 样板房一键装修方案
    public static final String FUNCTION_TYPE_DRAGDECORATE = "dragDecorate";
    // 移动端一键方案替换
    public static final String FUNCTION_TYPE_MOBILE = "mobile";
    // 测试方案
    public static final String FUNCTION_TYPE_TEST = "test";
    // 审核方案
    public static final String FUNCTION_TYPE_PUBLISH = "publish";
    // 支持一键方案和样板间方案状态同时查询
    public static final String FUNCTION_TYPE_SUPPORTBOTH = "supportBoth";

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
    //空间ID
    private Integer spaceCommonId;
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
    //商家后台上下架状态 ONEKEY 一键方案 OPEN 公开方案 多个用 , 隔开 ONEKEY,OPEN
    private String shelfStatus;
    //适用空间面积
    private String applySpaceAreas;
    //空间布局标识
    private String spaceLayoutType;
    //是否发布(0:否,1:是)
    private Integer releaseStatus;
    //是否删除(0:否,1:是)
    private Integer dataIsDeleted;
    //公司ID
    private Integer companyId;
    //打包方案的主方案ID
    private Integer groupPrimaryId;
    //浏览次数
    private Integer visitCount;
    //收费类型(0:免费,1:收费)
    private Integer chargeType;
    //方案价格
    private double planPrice;

    /***************增量查询属性*****************/
    //空间功能类型
    private Integer spaceFunctionId;
    //空间编码
    private String spaceCode;
    //空间面积
    private Integer spaceAreas;
    //空间形状
    private Integer spaceShape;
    // 封面图
    private String coverPicPath;
    //渲染720图片Url
    private String resRenderPicPath;
    //风格名
    private String styleName;
    //创建者名称
    private String createUserName;
    //公司名称
    private String companyName;
    //用户头像地址
    private String userPicPath;
    //品牌名称
    private String brandNames;
    //关联方案公司IDS
    private String companyIds;
    //公司名称
    private String companyNames;

    /***************** 全屋方案属性 *****************/
    //全屋方案ID
    private Integer fullHouseId;
    //全屋方案UUID
    private String uuid;
    //720 UUID
    private String vrSourceUuid;
    //公开状态(1公开,0未公开)
    private Integer openState;
    //方案来源类型(1:内部制作,2:装进我家,3:交付,4:分享,5:复制)
    private Integer sourceType;
    //全屋品牌Ids
    private String brandIds;

    /**********************************/
    private Integer ordering = 0;
}
