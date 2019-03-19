package com.sandu.search.entity.elasticsearch.search.product;

import lombok.Data;

import java.io.Serializable;

/**
 * 产品搜索聚合对象
 *
 * @date 20180122
 * @auth pengxuangang
 */
@Data
public class ProductSearchAggregationVo implements Serializable{

    private static final long serialVersionUID = 4819470588214500935L;

    public ProductSearchAggregationVo() {
    }

    //字段聚合
    public static final int FIELD_AGGREGATION_TYPE = 1;
    //最小值聚合
    public static final int MIN_VALUE_AGGREGATION_TYPE = 2;
    //最大值聚合
    public static final int MAX_VALUE_AGGREGATION_TYPE = 3;

    //聚合名
    private String aggregationName;
    //聚合字段名
    private String aggregationFieldName;
    //聚合数据大小
    private int aggregationSize;

    //聚合类型(1.字段聚合,2.最小值聚合,3.最大值聚合)
    private int aggregationType;
    //聚合值
    private int aggregationValue;

    public ProductSearchAggregationVo(String aggregationName, String aggregationFieldName, int aggregationSize) {
        this.aggregationName = aggregationName;
        this.aggregationFieldName = aggregationFieldName;
        this.aggregationType = FIELD_AGGREGATION_TYPE;
        this.aggregationSize = aggregationSize;
    }
}
