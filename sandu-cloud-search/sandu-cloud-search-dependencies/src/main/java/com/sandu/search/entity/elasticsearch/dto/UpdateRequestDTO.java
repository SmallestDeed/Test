package com.sandu.search.entity.elasticsearch.dto;

import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.common.xcontent.XContentType;

/**
 * 更新数据传输对象
 *
 * @date 2018/6/7
 * @auth pengxuangang
 * @mail xuangangpeng@gmail.com
 */
public class UpdateRequestDTO extends UpdateRequest {

    public UpdateRequestDTO() {
    }

    public UpdateRequestDTO(String index, String type, String id) {
        super(index, type, id);
    }

    public UpdateRequestDTO(String index, String type, String id, String jsonStr) {
        super(index, type, id);
        super.doc(jsonStr, XContentType.JSON);
    }
}
