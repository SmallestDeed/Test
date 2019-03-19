package com.sandu.search.entity.elasticsearch.po;

import lombok.Data;

import java.io.Serializable;

/**
 * 全屋方案装修报价信息对象
 *
 * @date 2018年9月12日 15:54
 * @auth zhangchengda
 */
@Data
public class DecoratePriceInfoPo implements Serializable {
    /** 方案装修报价信息表ID **/
    private Integer decoratePriceId;
    /** 装修报价类型value（对应数据字典type = decorateType的数据） **/
    private Integer decoratePriceTypeValue;
    /** 装修报价类型名称 **/
    private String decoratePriceTypeName;
    /** 装修报价范围value（对应数据字典） **/
    private Integer decoratePriceRangeValue;
    /** 装修报价范围名称 **/
    private String decoratePriceRangeName;
    /** 装修报价值 **/
    private Integer decoratePrice;
}
