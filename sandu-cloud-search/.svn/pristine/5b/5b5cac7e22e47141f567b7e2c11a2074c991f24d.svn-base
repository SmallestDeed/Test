package com.sandu.search.entity.response;

import lombok.Data;

import java.io.Serializable;

/**
 * 搜索DSL结果返回
 *
 * @date 20181031
 * @auth xiaoxc
 */
@Data
public class ElasticSearchResponse implements Serializable {

    private static final long serialVersionUID = -8172269490110693777L;

    //返回对象
    private Object obj;
    //返回数据总条数
    private long total;

    public ElasticSearchResponse(Object obj, long total) {
        this.obj = obj;
        this.total = total;
    }
}
