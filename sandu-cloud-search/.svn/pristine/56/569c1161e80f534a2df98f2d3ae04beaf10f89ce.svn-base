package com.sandu.search.entity.elasticsearch.search.product;

import com.sandu.search.entity.elasticsearch.dco.MultiMatchField;
import com.sandu.search.entity.elasticsearch.dco.ValueOffset;
import com.sandu.search.entity.elasticsearch.dco.ValueRange;
import com.sandu.search.entity.elasticsearch.search.SortOrderObject;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 产品搜索匹配对象
 *
 * @date 20180120
 * @auth pengxuangang
 */
@Data
public class ProductSearchMatchVo implements Serializable {

    private static final long serialVersionUID = 1699938152505746747L;

    //无天花样板房区域信息
    public static final int NO_TEMPLATE_ZONE = 0;
    //仅墙面区域
    public static final int ONLY_WALL_FACE_ZONE = 1;
    //所有样板房区域
    public static final int ALL_TEMPLATE_ZONE = 2;

    //产品名
    private String productName;
    //产品大类
    private Integer productTypeValue;
    //产品小类列表
    private List<Integer> productTypeSmallValueList;
    //产品分类ID列表
    private List<Integer> productCategoryIdList;
    //产品二级分类ID
    private int secondLevelCategoryId;
    //产品三级分类ID
    private int thirdLevelCategoryId;
    //品牌ID列表(全局品牌)
    private List<Integer> brandIdList;
    //公司品牌ID列表(公司下的品牌)
    private List<Integer> companyBrandIdList;
    //公司产品可见范围小类ID列表
    private List<Integer> companyProductVisibilityRangeIdList;
    //发布状态列表
    private List<Integer> putawayStatusList;
    //产品五级分类长编码列表
    private List<String> productFiveCategoryLongCodeList;
    //产品三级分类长编码列表
    private List<String> productThreeCategoryLongCodeList;

    private List<String> productCategoryLongCodeList;

    private Integer productBrandId;


    //单值匹配多字段--MultiMatchField.matchFieldList--List中第一个字段优先级最高
    private List<MultiMatchField> multiMatchFieldList;
    //是否包含白膜产品(true:包含, false:不包含, default:false)
    private boolean includeWhiteMembraneProduct = false;
    //白膜产品全铺长度
    private ValueOffset fullPaveLengthValueOffset;
    //产品高度有效列表
    private ValueRange productHeightValueRange;
    //平台编码
    private String platformCode;
    //产品过滤属性类型集合
    private Map<String, String> productFilterAttributeMap;
    //产品排序属性类型集合
    private Map<String, String> productOrderAttributeMap;
    //用户ID(用以计算用户产品使用次数,以便排序)
    private Integer userId;
    //当前实际搜索小类(用户搜索的可能是白膜小类)
    private Integer nowProductTypeSmallValue;
    //是否为内部用户(内部用户可查看多个状态下的产品，外部用户只能查看已发布产品,默认:非内部用户)
    private boolean innerUser = false;
    //搜全部时公司用户可见分类过滤(仅没有大小类/分类时生效)(Format: Map<大类,List<小类>>)
    private Map<Integer, List<Integer>> companyAliveTypeMap;
    //是否有产品库权限
    private boolean productLibraryAuth;
    //排序字段
    private List<SortOrderObject> sortOrderObjectList;
    //样板房天花区域状态
    private int templateZoneStatus;
    //是否是三度云享家公司
    private boolean sanduCompany;
    //天花随机拼花标识(0:非随机拼花, 1:随机拼花)
    private int ceilingRandomPatchFlowerFlag;

    private int companyId; //公司ID
    
    /**
     * 排序类型; 1: 按上架时间排序
     */
    private Integer sortType;
    
    /**
     * add by huangsongbo 2018.9.15
	 * 应对背景墙缩放功能(缩放之后,要按缩放后的长度来搜索背景墙)
     */
    private Integer fullPaveLength;
    
    /**
     * add by huangsongbo 2018.9.15
	 * 点击一键调整后的背景墙, 过滤条件应选择当前产品的product_length,而不是其对应白膜的full_pave_length
     */
    private Integer productLength;
    
    /**
     * 当前产品是否是背景墙(不包括白膜背景墙)
     */
    private boolean isBeijing;


    private Integer bizProductTypeSmallValue; //业务产品小类筛选
    
}
