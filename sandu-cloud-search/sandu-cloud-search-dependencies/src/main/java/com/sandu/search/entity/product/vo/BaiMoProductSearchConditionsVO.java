package com.sandu.search.entity.product.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 * Copyright (c) http://www.sanduspace.cn. All rights reserved.
 *
 * @author :  Steve
 * @date : 2018/7/28
 * @since : sandu_yun_1.0
 */
@Data
public class BaiMoProductSearchConditionsVO implements Serializable{
    private static final long serialVersionUID = 5283865217947601724L;

    private Integer id;

    private String productCode;

    //产品长度
    private Integer productLength;
    //产品宽度
    private Integer productWidth;
    //产品高度
    private Integer productHeight;

    //产品全铺长度(白膜)
    private Integer productFullPaveLength;

    //产品过滤属性类型集合
    private Map<String, String> productFilterAttributeMap;
    //产品排序属性类型集合
    private Map<String, String> productOrderAttributeMap;
    
	/**
	 * 产品大类value
	 */
    private Integer productTypeValue;
    
    /**
     * 产品小类value
     */
    private Integer productTypeSmallValue;
    
}
