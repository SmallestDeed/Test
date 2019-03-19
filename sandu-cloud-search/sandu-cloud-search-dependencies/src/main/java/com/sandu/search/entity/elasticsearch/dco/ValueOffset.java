package com.sandu.search.entity.elasticsearch.dco;

import lombok.Data;

import java.io.Serializable;

/**
 * 白膜产品全铺对象
 *
 * @date 20180207
 * @auth pengxuangang
 */
@Data
public class ValueOffset implements Serializable {

    private static final long serialVersionUID = -6324632176487104940L;

    public ValueOffset(int fullPaveLength, int fullPaveOffsetValue, int fullPaveOffsetType) {
        this.fullPaveOffsetType = fullPaveOffsetType;
        this.fullPaveOffsetValue = fullPaveOffsetValue;
        this.fullPaveLength = fullPaveLength;
    }

    public ValueOffset(int fullPaveLength, int fullPaveOffsetValue, int fullPaveOffsetType, int valueEqualType) {
        this.fullPaveOffsetType = fullPaveOffsetType;
        this.fullPaveOffsetValue = fullPaveOffsetValue;
        this.fullPaveLength = fullPaveLength;
        this.valueEqualType = valueEqualType;
    }

    //正常偏移(左右偏移)
    public static final int NORMAL_FULL_PAVE_OFFSET_TYPE = 0;
    //左偏移
    public static final int LEFT_FULL_PAVE_OFFSET_TYPE = 1;
    //右偏移
    public static final int RIGHT_FULL_PAVE_OFFSET_TYPE = 2;
    //不偏移(指代一个具体值,如:5<=x<=5)
    public static final int NO_FULL_PAVE_OFFSET_TYPE = 3;

    //仅包含小值
    public static final int ONLY_INCLUDE_LOWER = 1;
    //仅包含大值
    public static final int ONLY_INCLUDE_UPPER = 2;
    //包含所有值(默认)
    public static final int INCLUDE_ALL = 3;
    //都不包含
    public static final int NOT_INCLUDE = 4;

    //全铺偏移类型
    private int fullPaveOffsetType;
    //偏移量(单位:厘米)
    private int fullPaveOffsetValue;
    //全铺长度
    private int fullPaveLength;
    //值匹配类型(1:仅包含小值, 2:仅包含大值, 3:包含所有值(默认), 4:都不包含)
    private int valueEqualType;

}
