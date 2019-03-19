package com.sandu.search.entity.elasticsearch.index;/**
 * @ Author     ：weisheng.
 * @ Date       ：Created in AM 11:06 2018/8/1 0001
 * @ Description：${description}
 * @ Modified By：
 * @Version: $version$
 */

import com.sandu.search.entity.elasticsearch.po.BaseGoodsSkuPo;
import com.sandu.search.entity.elasticsearch.po.metadate.ProductPlatformRelPo;
import lombok.Data;

import java.awt.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @Title: 商品索引数据对象
 * @Package
 * @Description:
 * @author weisheng
 * @date 2018/8/1 0001AM 11:06
 */
@Data
public class GoodsIndexMappingData implements Serializable{
    private static final long serialVersionUID = -145421629964803606L;

    /******************************** 商品属性字段 **********************************/
    //商品的spuId
    private Integer id;

    //商品的spu名称(即商品名称)
    private String goodsSpuName;

    //商品的spu缩略图地址
    private String goodsSpuPicPath;

    //商品的spu缩略图地址列表
    private List<String> goodsSpuPicPathList;

    //商品的spu图片列表ID
    private String goodsSpuPicIds;

    //商品的spu缩略图Id
    private Integer goodsSpuPicId;

    //获得时间
    private Date goodsGetTime;

    //商品默认价格
    private BigDecimal goodsDefaultPrice;

    //创建时间
    private Date goodsGmtCreatetime;

    //修改时间
    private Date goodsGmtModified;

    //商品上架情况
    private Integer goodsIsPutaway;

    //商品公司ID
    private Integer goodsCompanyId;

    //是否预售新品
    private Integer goodsisPreSell;

    //是否特卖商品
    private Integer goodsIsSpecialOffer;

    //商品sku列表(包含每一个商品的平台信息,分类信息,售卖价格)
    private List<BaseGoodsSkuPo> goodsSkuPoList;

    //数据是否删除(0:未删除, 1:已删除)
    private Integer dataIsDelete;

}
