package com.sandu.search.entity.product.universal;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 产品基本条件视图对象
 *
 * @date 20171222
 * @auth pengxuangang
 */
@Data
public class ProductBaseConditionVo implements Serializable {

    private static final long serialVersionUID = 831558733088604823L;

    //搜索关键字
    private String searchKeyword;
    //产品名
    private String productName;
    //产品大类
    private String productTypeValue;
    //是否包含白膜产品(true:包含, false:不包含, default:false)
    private boolean includeWhiteMembraneProduct = false;
    //产品分类ID列表
    private List<Integer> productCategoryIdList;
    //产品二级分类ID
    private int secondLevelCategoryId;
    //产品三级分类ID
    private int thirdLevelCategoryId;
    //产品五级分类ID列表
    private List<Integer> fifthLevelCategoryIdList;
    //品牌ID列表
    private List<Integer> brandIdList;
}

