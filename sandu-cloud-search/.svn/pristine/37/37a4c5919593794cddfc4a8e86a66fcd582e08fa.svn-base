package com.sandu.search.entity.designplan.vo;

import com.sandu.search.entity.elasticsearch.dco.MultiMatchField;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 推荐方案搜索业务对象
 *
 * @date 2018/6/1
 * @auth xiaoxc
 */
@Data
public class RecommendationPlanSearchVo implements Serializable {

    private static final long serialVersionUID = 4751804971686843788L;

    //发布时间正序
    public static final int RELEASE_TIME_SORT_ASC = 1;
    //发布时间逆序
    public static final int RELEASE_TIME_SORT_DESC = 2;

    //显示类型(样板间：public,一键方案:decorate,测试:test,公开:open)
    private String displayType;
    //一键方案类型(一键方案：2，样板间：1)
    private Integer recommendationPlanType;
    //方案类型
    private List<Integer> recommendationPlanTypeList;
    //发布状态(已发布:1)
    private Integer releaseStatus;
    //关键字一词多字段搜索(方案名称、品牌名称、小区名称、创建人)
    private String searchKeyword;
    //单值匹配多字段--MultiMatchField.matchFieldList--List中第一个字段优先级最高
    private List<MultiMatchField> multiMatchFieldList;
    //空间面积
    private Integer spaceArea;
    //房屋类型
    private Integer houseType;
    //风格ID
    private Integer designStyleId;
    //公司ID
    private Integer companyId;
    //公司ids(非经销商和厂商需包含三度公司ID+本公司ID)
    private List<Integer> companyIdList;
    //品牌ids(经销商品牌IDS) Map<companId, brandIds>
    private Map<String, String> brandIdMap;
    //排序类型(1:发布时间正序, 2:发布时间逆序)
    private int sortType;
    //方案来源
    private List<String> planSource;
    //商家后台上下架状态 ONEKEY 一键方案 OPEN 公开方案 多个用 , 隔开 ONEKEY,OPEN
    private String shelfStatus;
    //(卫生间)空间布局
    private String spaceLayoutType;
    //平台编码
    private String platformCode;
    //平台编码ID
    private Integer platformId;
    //u3d标识
    private Integer msgId;
    //用户ID
    private Integer userId;
    //设计师ID
    private Integer designerId;
    //店铺ID
    private Integer shopId;
    //该空间类型审核管理权限
    private boolean spaceTypeAuditPermission = false;
    //审核方案菜单权限
    private boolean auditPlanMenuPermission = false;
    //测试方案菜单权限
    private boolean testPlanMenuPermission = false;
    //装修方案类型
    private Integer decoratePriceType;
    //装修方案价格范围
    private Integer decoratePriceRange;
    //产品IDs
    private List<Integer> productIdList;
    //区分是从哪里调的接口
    private String enterType;
	//需要过滤的置顶方案集合
    private List<String> ids;
    //店铺平台Id (3同城联盟，2随选网，1企业小程序)
    private Integer shopPlatformType;
    //单空间方案ids
    private List<Integer> recommendedIds;
    //全屋方案ids
    private List<Integer> fullHouseIds;
}
