package com.sandu.search.entity.elasticsearch.dto;

import org.elasticsearch.action.get.GetRequest;

/**
 * 获取数据传输对象
 *
 * @date 20171211
 * @auth pengxuangang
 */
public class GetRequestDTO extends GetRequest {

    public GetRequestDTO() {
    }

    public GetRequestDTO(String index) {
        super(index);
    }

    public GetRequestDTO(String index, String type, String id) {
        super(index, type, id);
    }
}
