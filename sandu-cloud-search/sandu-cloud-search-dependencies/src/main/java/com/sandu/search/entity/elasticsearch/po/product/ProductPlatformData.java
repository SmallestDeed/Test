package com.sandu.search.entity.elasticsearch.po.product;

import com.sandu.search.entity.elasticsearch.po.metadate.ProductPlatformRelPo;
import lombok.Data;

import java.io.Serializable;

/**
 * 产品平台数据对象
 *
 * @date 20180228
 * @auth pengxuangang
 */
@Data
public class ProductPlatformData implements Serializable{

    private static final long serialVersionUID = -7730871994002217068L;

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
    //随选网
    private ProductPlatformRelPo platformProductSelectDecoration;
}
