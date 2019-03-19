package com.sandu.search.entity.elasticsearch.po;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 产品持久化对象
 *
 * @date 20171212
 * @auth pengxuangang
 */
@Data
public class ProductPo implements Serializable {

    //产品状态:未上架
    public final static int PUTAWAYSTATE_NO_SHELVES = 0;
    //产品状态:已上架
    public final static int PUTAWAYSTATE_UP_SHELVES = 1;
    //产品状态:测试中
    public final static int PUTAWAYSTATE_TESTING = 2;
    //产品状态:已发布
    public final static int PUTAWAYSTATE_ALEARY_PUBLISH = 3;
    //产品状态:已下架
    public final static int PUTAWAYSTATE_DOWN_SHELVES = 4;
    //产品状态:发布中
    public final static int PUTAWAYSTATE_PUBLICHING = 7;

    //保密标识(0不保密，1保密')
    //不保密
    public final static int NO_SECRECY = 0;
    //保密
    public final static int SECRECY = 1;

    //无天花随机拼花
    public final static int NO_CEILING_RANDOM_PATCH_FLOWER = 0;
    //有花随机拼花
    public final static int HAVE_CEILING_RANDOM_PATCH_FLOWER = 1;

    private static final long serialVersionUID = 1947663178318930925L;

    //产品ID
    private Integer id;
    //产品类型ID
    private int productCategoryId;
    //产品编码
    private String productCode;
    //产品名称
    private String productName;
    //产品品牌ID
    private Integer productBrandId;
    //产品品牌名
    private String productBrandName;
    //产品风格ID
    private Integer productStyleId;
    //产品风格数组
    private String productStyleIdArr;
    //产品规格
    private String productSpecification;
    //产品颜色
    private Integer productColorId;
    //产品长度
    private int productLength;
    //产品宽度
    private int productWidth;
    //产品高度
    private int productHeight;
    //销售价格
    private double productSalePrice;
    //销售价格值
    private int productSalePriceValue;
    //产品图片ID
    private Integer productPicId;
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
    //产品图片列表
    private String productPicArr;
    //产品材质ID
    private String productMaterialId;
    //产品材质ID列表
    private String productMaterialIdArr;
    //产品大类
    private Integer productTypeValue;
    //产品小类
    private Integer productTypeSmallValue;
    //发布状态(0:未上架,1:已上架,2:测试中,3:已发布,4:已下架)
    private int productPutawayState;
    //产品设计者ID
    private int productDesignerId;
    //产品系列ID
    private int productSeriesId;
    //产品型号
    private String productModelNumber;
    //产品尺寸
    private String productMeasurementCode;
    //产品天花布局标识
    private String productCeilingLayoutIden;
    //产品地面布局标识
    private String productGroundLayoutIden;
    //产品全铺长度(白膜)
    private int productFullPaveLength;
    //产品是否保密(0不保密，1保密',default:0)
    private int secrecyFlag;
    //天花随机拼花标识(0:非随机拼花, 1:随机拼花)
    private int ceilingRandomPatchFlowerFlag;
    //数据是否删除(0:未删除, 1:已删除)
    private int dataIsDelete;
    // 产品款式id
    private Integer styleModelId;
    // 硬装产品新增存的白模id
    private String bmIds;
    // 墙体分类
    /*private String wallType;*/
    // 多材质信息
    private String splitTexturesInfo;
    //商品spuid
    private Integer goodsSpuId;
    
    /**
     * 是否是多模型产品;
     * 0:多模型副产品;
     * 1:单产品;
     * 2:多模型主产品;
     */
    private Integer productBatchType;
    
    /**
     * 产品缩略图路径
     */
    private String productPicPath;
    
    /**
     * 产品属性信息
     * eg: CabinetShape,,3;CabinetType,,1;category,,2
     */
    private String propInfo;
 
    /**
     * 产品图片列表idList
     */
    private List<Integer> picIdList;
    
}
