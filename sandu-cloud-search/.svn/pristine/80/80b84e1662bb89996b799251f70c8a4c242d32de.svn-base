package com.sandu.search.entity.product.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 产品分类业务对象
 *
 * @date 2018/6/9
 * @auth pengxuangang
 * @mail xuangangpeng@gmail.com
 */
@Data
public class ProductCategoryVo implements Serializable {

    private static final long serialVersionUID = -523668200776843814L;

    //分类名
    private String categoryName;
    //分类编码
    private String categoryCode;
    //子分类列表
    private List<ProductCategoryVo> productCategoryVoList;
    //对应小类
    private Integer productSmallTypeValue;

    private Integer productType;
    //顺序
    private int categoryOrder;
    //sort
    private int sortOrder = 9;
}
