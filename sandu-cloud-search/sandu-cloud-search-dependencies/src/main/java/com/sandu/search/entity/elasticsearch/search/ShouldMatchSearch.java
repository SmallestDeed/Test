package com.sandu.search.entity.elasticsearch.search;

import lombok.Data;
import org.elasticsearch.index.query.QueryBuilder;

import java.io.Serializable;

/**
 * 或匹配搜索对象
 *
 * @date 20180302
 * @auth pengxuangang
 */
@Data
public class ShouldMatchSearch implements Serializable{

    private static final long serialVersionUID = -5006283250676629379L;

    //查询对象
    private QueryBuilder queryBuilder;
    //最小匹配数
    private int minimumShouldMatch;

    public ShouldMatchSearch(QueryBuilder queryBuilder, int minimumShouldMatch) {
        this.queryBuilder = queryBuilder;
        this.minimumShouldMatch = minimumShouldMatch;
    }
}
