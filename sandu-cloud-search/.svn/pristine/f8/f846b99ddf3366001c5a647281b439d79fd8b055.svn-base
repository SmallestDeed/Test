package com.sandu.search.entity.designplan.po;

import lombok.Data;

import java.io.Serializable;

/**
 * 设计方案产品持久化对象
 *
 * @date 20171227
 * @auth pengxuangang
 */
@Data
public class DesignPlanProductPo implements Serializable {

    //组合产品类型(非主产品)
    public final static int PRODUCT_TYPE_NO_MAIN_PRODUCT = 0;
    //组合产品类型(主产品)
    public final static int PRODUCT_TYPE_IS_MAIN_PRODUCT = 1;
    private static final long serialVersionUID = -3008520243498053300L;
    //ID
    private int id;
    //初始化产品ID
    private int initProductId;
    
    /**
     * 当前使用的产品id
     */
    private int productId;

    /*去掉多余属性，优化查询条件
    //系统编码
    private String systemCode;
    //设计方案ID
    private int designPlanId;
    
    //产品顺序
    private String productOrder;
    //方案产品ID
    private int designProductId;
    //是否隐藏
    private int isHide;
    //位置索引路径
    private String positionIndexPath;
    //产品组合ID
    private int productGroupId;
    //是否是主产品(0:不是主产品, 1:主产品)
    private int isMainProduct;
    //位置名称
    private String positionName;
    //设计方案组合ID
    private String designPlanGroupId;
    //模型产品ID
    private int modelProductId;
    //是组合还是结构,0:组合,1:结构
    private int groupType;
    //标准(1标准 0 非标准)
    private int isStandard;
    //中心点
    private String centerPoint;*/
}
