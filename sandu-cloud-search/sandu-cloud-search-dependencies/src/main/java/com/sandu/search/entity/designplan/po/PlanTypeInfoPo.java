package com.sandu.search.entity.designplan.po;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @desc 方案购买信息
 * @auth xiaoxc
 * @data 2018/11/15
 */
@Data
public class PlanTypeInfoPo implements Serializable {

    private static final long serialVersionUID = 4742157227272147395L;
    //方案Id
    private Integer planId;
    //方案类型(方案类型:0.全屋,1.普通)
    private Integer planType;

}
