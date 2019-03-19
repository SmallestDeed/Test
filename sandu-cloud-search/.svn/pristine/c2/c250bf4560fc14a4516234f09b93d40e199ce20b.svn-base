package com.sandu.search.entity.product.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 产品大小类编码对象
 *
 * @date 2018/3/22
 * @auth pengxuangang
 * @mail xuangangpeng@gmail.com
 */
@Data
public class ProductTypeCategory implements Serializable{

    public ProductTypeCategory(String productCategoryCode, List<Integer> productSmallTypeValueList) {
        this.productCategoryCode = productCategoryCode;
        this.productSmallTypeValueList = productSmallTypeValueList;
    }

    private static final long serialVersionUID = 6779031763605607217L;

    //产品分类编码
    private String productCategoryCode;
    //产品小类列表
    private List<Integer> productSmallTypeValueList;
}
