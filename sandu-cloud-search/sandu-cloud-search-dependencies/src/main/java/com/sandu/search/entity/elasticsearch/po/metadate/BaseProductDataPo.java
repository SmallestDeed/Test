package com.sandu.search.entity.elasticsearch.po.metadate;/**
 * @ Author     ：weisheng.
 * @ Date       ：Created in AM 10:39 2018/8/7 0007
 * @ Description：${description}
 * @ Modified By：
 * @Version: $version$
 */

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Title: 基本产品元数据
 * @Package
 * @Description:
 * @author weisheng
 * @date 2018/8/7 0007AM 10:39
 */
@Data
public class BaseProductDataPo implements Serializable{
    private static final long serialVersionUID = -4015357918336965863L;

    private int productId; //产品ID
    private int spuId; //商品ID
    private int productIsDeleted;//产品是否被删除
    private BigDecimal price;//产品优惠价
    private String productCode;//'产品编码'
    private int brandId; //产品品牌
    private String productStyleIdInfo; //'产品风格'
    private String productSpec;//产品规格
    private int productTypeValue;//产品大类
    private int productSmallTypeValue;//产品小类
    private String productModelNumber;//产品型号
    private int putawayState;//产品状态('0-未上架;1-已上架;2-测试中;3-已发布;4-已下架;5-发布中')
}
