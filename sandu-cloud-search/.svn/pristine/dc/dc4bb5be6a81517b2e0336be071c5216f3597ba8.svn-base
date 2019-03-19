package com.sandu.search.service.index;

import com.sandu.search.entity.elasticsearch.po.ProductTexturePo;
import com.sandu.search.entity.product.dto.BaseBrand;
import com.sandu.search.entity.product.dto.ResModel;
import com.sandu.search.entity.product.dto.ResPic;
import com.sandu.search.entity.product.dto.SplitTextureDTO;

import java.util.Map;

/**
 * @version V1.0
 * @Title: IndexGroupProductService.java
 * @Description:组合产品建立索引的时候所用到的Service
 * @createAuthor
 */
public interface IndexGroupProductService {


    /**
     * ResTexture转化为ResTextureDTO
     *
     * @return
     */
    SplitTextureDTO.ResTextureDTO fromResTexture_old(ProductTexturePo groupProductTexture);


    /**
     * 获取模型资源详情
     *
     * @param id
     * @return ResModel
     */
    ResModel getResModel(Integer id);


    /**
     * 获取图片资源详情
     *
     * @param id
     * @return ResPic
     */
    ResPic getResPic(Integer id);

    /**
     * 获取品牌详情
     *
     * @param id
     * @return BaseBrand
     */
    BaseBrand getBaseBrand(Integer id);

    /**
     * getPropertyMap
     * 通过产品id  获取属性值 的map
     * map的结构（ 键是product_attribute表中attribute_key  值是product_props表中的prop_value）
     *
     * @param productId
     * @return
     */
    Map<String, String> getPropertyMap_old(Integer productId);

    /**
     * 获取规则
     *
     * @param productId
     * @param productCode
     * @param productTypeCode
     * @param productSmallTypeCode
     * @param productAttributeMap
     * @return
     */
    Map<String, String> getRulesSecondaryList(Integer productId, String productCode, String productTypeCode,
                                              String productSmallTypeCode, Map<String, String> productAttributeMap);
}
