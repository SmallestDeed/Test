package com.sandu.search.entity.elasticsearch.response;

import lombok.Data;
import org.elasticsearch.search.aggregations.Aggregations;

import java.io.Serializable;

/**
 * 搜索对象返回
 *
 * @date 20171222
 * @auth pengxuangang
 */
@Data
public class SearchObjectResponse implements Serializable {

    private static final long serialVersionUID = 3457364002164644564L;

    //搜索结果列表
    private Object hitResult;
    //搜索条数
    private long hitTotal;
    //搜索聚合
    private Aggregations searchAggs;

    public SearchObjectResponse(Object hitResultList, long hitTotal) {
        this.hitResult = hitResultList;
        this.hitTotal = hitTotal;
    }

    public SearchObjectResponse(Object hitResult, long hitTotal, Aggregations searchAggs) {
        this.hitResult = hitResult;
        this.hitTotal = hitTotal;
        this.searchAggs = searchAggs;
    }
}
