package com.sandu.search.entity.elasticsearch.dto;

import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.common.xcontent.XContentType;

/**
 * 索引数据传输对象
 *
 * @date 20171211
 * @auth pengxuangang
 */
public class IndexRequestDTO extends IndexRequest {

    public IndexRequestDTO() {
    }

    public IndexRequestDTO(String index) {
        super(index);
    }

    public IndexRequestDTO(String index, String type) {
        super(index, type);
    }

    public IndexRequestDTO(String index, String type, String id) {
        super(index, type, id);
    }

    public IndexRequestDTO(String index, String type, String id, String jsoStr) {
        super(index, type, id);
        super.source(jsoStr, XContentType.JSON);
    }
}
