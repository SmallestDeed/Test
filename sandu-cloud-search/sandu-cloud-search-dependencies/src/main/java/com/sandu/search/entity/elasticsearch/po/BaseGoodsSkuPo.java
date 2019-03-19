package com.sandu.search.entity.elasticsearch.po;/**
 * @ Author     ：weisheng.
 * @ Date       ：Created in PM 4:26 2018/7/31 0031
 * @ Description：${description}
 * @ Modified By：
 * @Version: $version$
 */

import com.sandu.search.entity.elasticsearch.po.metadate.ProductPlatformRelPo;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @Title: 商品sku
 * @Package
 * @Description:
 * @author weisheng
 * @date 2018/7/31 0031PM 4:26
 */
@Data
public class BaseGoodsSkuPo implements Serializable{
    private static final long serialVersionUID = -6661414569742732104L;
    private BigDecimal price;//商品售卖价格
    private int productId;//产品ID
    private int productIsDeleted;//产品是否被删除
    private String productCode;//'产品编码'
    private int brandId; //产品品牌
    private String productStyleIdInfo; //'产品风格'
    private String productSpec;//产品规格
    private int productTypeValue;//产品大类
    private int productSmallTypeValue;//产品小类
    private String productModelNumber;//产品型号
    private int putawayState;//产品状态('0-未上架;1-已上架;2-测试中;3-已发布;4-已下架;5-发布中')

    /******************************** 产品全平台过滤字段 **********************************/
    //平台编码列表
    private List<String> platformCodeList;
    //2B-移动端
    private ProductPlatformRelPo platformProductToBMobile;
    //2B-PC
    private ProductPlatformRelPo platformProductToBPc;
    //品牌2C-网站
    private ProductPlatformRelPo platformProductToCSite;
    //2C-移动端
    private ProductPlatformRelPo platformProductToCMobile;
    //三度-后台管理
    private ProductPlatformRelPo platformProductSanduManager;
    //户型绘制工具
    private ProductPlatformRelPo platformProductHouseDraw;
    //商家管理后台
    private ProductPlatformRelPo platformProductMerchantsManager;
    //测试
    private ProductPlatformRelPo platformProductTest;
    //U3D转换插件
    private ProductPlatformRelPo platformProductU3dPlugin;
    //小程序
    private ProductPlatformRelPo platformProductMiniProgram;


    /******************************** 产品分类字段 **********************************/
    //产品分类ID
    private Integer productCategoryId;
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

    //产品分类长编码列表2
    private List<String> productCategoryLongCodeList2;

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


}
