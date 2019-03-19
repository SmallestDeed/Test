package com.sandu.search.entity.elasticsearch.dco;

import lombok.Data;

import java.io.Serializable;

/**
 * 值范围对象
 *
 * @date 2018/3/10
 * @auth pengxuangang
 * @mail xuangangpeng@gmail.com
 */
@Data
public class ValueRange implements Serializable {

    private static final long serialVersionUID = 1322018922539571802L;

    public ValueRange(int rangeType, int startValue, int endValue) {
        this.rangeType = rangeType;
        this.startValue = startValue;
        this.endValue = endValue;
    }

    //仅起始相等
    public static final int RANGE_TYPE_ONLY_START_EQUAL = 1;
    //仅结束相等
    public static final int RANGE_TYPE_ONLY_END_EQUAL = 2;
    //起始结束都不等
    public static final int RANGE_TYPE_START_AND_END_NO_EQUAL = 3;
    //起始结束都相等
    public static final int RANGE_TYPE_ALL_EQUAL = 4;

    //范围类型(1:仅起始相等，2:仅结束相等，3:起始结束都不等，4:起始结束都相等)
    private int rangeType;
    //起始值
    private int startValue;
    //结束值
    private int endValue;

}
