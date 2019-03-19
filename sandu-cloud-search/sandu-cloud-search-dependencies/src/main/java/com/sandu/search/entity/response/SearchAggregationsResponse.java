package com.sandu.search.entity.response;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 * 搜索聚合结果
 *
 * @date 20180116
 * @auth pengxuangang
 */
@Data
public class SearchAggregationsResponse implements Serializable {

    private static final long serialVersionUID = -6077902050380369889L;

    //聚合名
    private String aggregationName;
    //聚合结果<聚合结果，聚合对象>
    private Map<String, Long> aggregationMap;

    public SearchAggregationsResponse(String aggregationName, Map<String, Long> aggregationMap) {
        this.aggregationName = aggregationName;
        this.aggregationMap = aggregationMap;
    }
}
