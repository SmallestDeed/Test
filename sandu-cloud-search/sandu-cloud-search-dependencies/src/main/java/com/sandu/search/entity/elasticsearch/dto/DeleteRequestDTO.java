package com.sandu.search.entity.elasticsearch.dto;

import org.elasticsearch.action.delete.DeleteRequest;

/**
 * 删除数据传输对象
 *
 * @date 20171211
 * @auth pengxuangang
 */
public class DeleteRequestDTO extends DeleteRequest {

    public DeleteRequestDTO() {
    }

    public DeleteRequestDTO(String index) {
        super(index);
    }

    public DeleteRequestDTO(String index, String type, String id) {
        super(index, type, id);
    }
}
