package com.sandu.search.common.constant;

/**
 * 枚举企业类型
 *
 * @auth xiaoxc
 * @data 2018/07/12
 */
public enum CompanyTypeEnum {

    //厂商
    MANUFACTURER(1),
    //经销商
    FRANCHISER(2),
    //门店
    STORE(3),
    //设计公司
    DESIGN_COMPANY(4),
    //装修公司
    DECORATION_COMPANY(5),
    //设计师
    DESIGNER(6),
    //工长（施工单位）
    CONSTRUCTION_UNIT(7),
    //中介
    MEDIATION(8),
    //独立经销商
    INDEPENDENT_DEALERS(9);

    private Integer value;

    CompanyTypeEnum(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }
}
