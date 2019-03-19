package com.sandu.search.entity.elasticsearch.po.metadate;

import com.sandu.search.entity.product.dto.SplitTextureDTO;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Data
public class ProductGroupDetailPo implements Serializable {

    /**
     * 产品ID
     **/
    private Integer productId;
    /**
     * 产品CODE
     **/
    private String productCode;
    /**
     * 产品模型地址
     **/
    private String u3dModelPath;//无法从元数据处获取，和平台相关,需后期获取
    /**
     * 是否为主产品
     **/
    private int isMainProduct;
    /**
     * 产品品牌id
     **/
    private Integer brandId;
    /**
     * 硬装还是软装
     **/
    private String rootType;
    /**
     * 产品小类
     **/
    private String productSmallTypeValue;
    /**
     * 产品小类code
     **/
    private String productSmallTypeCode;
    /**
     * 产品小分类标示
     **/
    private String productSmallTypeMark;
    /**
     * 产品小类name
     **/
    private String productSmallTypeName;
    /**
     * 产品大类code
     **/
    private String productTypeCode;
    /**
     * 产品大分类标示
     **/
    private String productTypeMark;
    /**
     * 产品大类
     **/
    private Integer productTypeValue;
    /**
     * 产品大类name
     **/
    private String productTypeName;
    /**
     * 产品宽度
     **/
    private String productWidth;
    /**
     * 产品长度
     **/
    private String productLength;
    /**
     * 产品高度
     **/
    private String productHeight;
    /**
     * 模型最小高度
     **/
    private int modelMinHeight;////无法从元数据处获取，和平台相关，需后期获取
    /**
     * 产品材质图片列表
     **/
    private String[] materialPicPaths;
    /**
     * 产品组ID
     **/
    private Integer productGroupId;
    /**
     * 规则
     **/
    private Map<String, String> rulesMap;
    /**
     * 结构id
     **/
    private Integer productStructureId;
    /**
     * 产品材质
     **/
    private String materialPicIds;
    /**
     * 贴图产品的模型id
     **/
    private Integer textureProductModelId;
    /**
     * 判断是否是可拆分材质产品:0:普通产品;1:可拆分材质产品(橱柜)
     **/
    private Integer isSplit = new Integer(0);
    /**
     * 可拆分材质信息
     **/
    private List<SplitTextureDTO> splitTexturesChoose;
    /**
     * 是否是背景墙
     **/
    private Integer bgWall;
    /**
     * 材质信息
     **/
    private String splitTexturesInfo;
    /**
     * 可拆分材质信息
     **/
    private String splitTexturesChooseInfo;
    /**
     * U3D模型Id
     **/
    private Integer windowsU3dModelId;
    /**
     * 产品属性
     **/
    private Map<String, String> propertyMap;

    /**
     * 产品属性key
     **/
    private String productAttributeKey;
    /**
     * 产品属性值Id
     **/
    private Integer productAttributeValueId;
    /**
     * 产品属性值value
     **/
    private Integer productPropValue;
}
