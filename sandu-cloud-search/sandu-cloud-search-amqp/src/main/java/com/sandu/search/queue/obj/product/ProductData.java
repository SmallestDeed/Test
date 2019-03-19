package com.sandu.search.queue.obj.product;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 分类产品索引对象
 *
 * @date 20171212
 * @auth pengxuangang
 */
@Data
public class ProductData implements Serializable {

    private static final long serialVersionUID = -209375085002659492L;

    /******************************** 产品基本属性字段 **********************************/
    //产品ID
    private Integer id;
    //产品类型ID
    private Integer productCategoryId;
    //产品编码
    private String productCode;
    //产品名称
    private String productName;
    //产品品牌ID
    private Integer productBrandId;
    //产品品牌名
    private String productBrandName;
    //产品风格ID
    private List<Integer> productStyleList;
    //产品规格
    private String productSpecification;
    //产品颜色
    private Integer productColorId;
    //产品长度
    private Integer productLength;
    //产品宽度
    private Integer productWidth;
    //产品高度
    private Integer productHeight;
    //销售价格
    private Double productSalePrice;
    //销售价格值
    private Integer productSalePriceValue;
    //产品图片ID
    private Integer productPicId;
    //产品图片路径
    private String productPicPath;
    //产品图片列表
    private List<Integer> productPicList;
    //产品图片路径列表
    private List<String> productPicPathList;
    //产品模型ID
    private Integer productModelId;
    //产品描述
    private String productDesc;
    //系统编码
    private String productSystemCode;
    //创建时间
    private Date productCreateDate;
    //修改时间
    private Date productModifyDate;
    //上架时间
    private Date productPutawayDate;
    //产品材质ID列表
    private List<String> productMaterialList;
    //产品大类
    private Integer productTypeValue;
    //产品小类
    private Integer productTypeSmallValue;
    //发布状态(0:未上架,1:已上架,2:测试中,3:已发布,4:已下架)
    private Integer productPutawayState;
    //产品设计者ID
    private Integer productDesignerId;
    //产品系列ID
    private Integer productSeriesId;
    //产品型号
    private String productModelNumber;
    //产品尺寸
    private String productMeasurementCode;
    //产品天花布局标识
    private List<String> productCeilingLayoutIdenList;
    //产品地面布局标识
    private String productGroundLayoutIden;
    //产品全铺长度(白膜)
    private Integer productFullPaveLength;
    //产品是否保密(0不保密，1保密',default:0)
    private Integer secrecyFlag;
    //天花随机拼花标识(0:非随机拼花, 1:随机拼花)
    private Integer ceilingRandomPatchFlowerFlag;
    //数据是否删除(0:未删除, 1:已删除)
    private Integer dataIsDelete;

    /******************************** 产品分类字段 **********************************/
    //一级分类ID
    private Integer firstLevelCategoryId;
    //一级分类名
    private String firstLevelCategoryName;
    //二级分类ID
    private Integer secondLevelCategoryId;
    //二级分类名
    private String secondLevelCategoryName;
    //三级分类ID
    private Integer thirdLevelCategoryId;
    //三级分类名
    private String thirdLevelCategoryName;
    //四级分类ID
    private Integer fourthLevelCategoryId;
    //四级分类名
    private String fourthLevelCategoryName;
    //五级分类ID
    private Integer fifthLevelCategoryId;
    //五级分类名
    private String fifthLevelCategoryName;
    //每级对象
    private List<String> allLevelCategoryName;
    //产品分类编码
    private String productCategoryCode;
    //产品分类长编码列表
    private List<String> productCategoryLongCodeList;
    //分类父节点
    private Integer parentCategoryId;
    //分类名称
    private String categoryName;
    //分类级别
    private Integer categoryLevel;
    //分类排序
    private Integer categoryOrder;
    //分类系统编码
    private String categorySystemCode;
    //产品所有分类ID
    private List<Integer> allProductCategoryId;
    //产品所有分类名
    private List<String> allProductCategoryName;

    /******************************** 产品材质字段 **********************************/
    private List<String> productMaterialNameList;


    /******************************** 产品风格质字段 **********************************/
    //风格名
    private List<String> styleNameList;

    /******************************** 产品组合字段 **********************************/

    //产品组合ID列表
    private List<Integer> productGroupIdList;
    //产品组合名字列表
    private List<Integer> productGroupNameList;
    //产品组合Code列表
    private List<Integer> productGroupCodeList;

    /******************************** 产品公司字段 **********************************/
    //公司ID
    private Integer companyId;
    //公司编码
    private String companyCode;
    //公司行业
    private Integer companyIndustry;

    /******************************** 产品全平台过滤字段 **********************************/
    //平台编码列表
    private List<String> platformCodeList;
    //2B-移动端
    private ProductPlatform platformProductToBMobile;
    //2B-PC
    private ProductPlatform platformProductToBPc;
    //品牌2C-网站
    private ProductPlatform platformProductToCSite;
    //2C-移动端
    private ProductPlatform platformProductToCMobile;
    //三度-后台管理
    private ProductPlatform platformProductSanduManager;
    //户型绘制工具
    private ProductPlatform platformProductHouseDraw;
    //商家管理后台
    private ProductPlatform platformProductMerchantsManager;
    //测试
    private ProductPlatform platformProductTest;
    //U3D转换插件
    private ProductPlatform platformProductU3dPlugin;
    //小程序
    private ProductPlatform platformProductMiniProgram;

    /******************************** 产品属性字段 **********************************/
    //产品过滤属性类型集合
    private Map<String, String> productFilterAttributeMap;
    //产品排序属性类型集合
    private Map<String, String> productOrderAttributeMap;

    /******************************** 产品使用次数字段 **********************************/
    //产品使用次数字段Map<userId, usageCount>
    private Map<Integer, Integer> productUsageCount;
}