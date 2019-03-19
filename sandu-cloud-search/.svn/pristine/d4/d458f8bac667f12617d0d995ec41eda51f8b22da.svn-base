package com.sandu.search.entity.response.universal;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * [通用版]搜索结果返回
 *
 * @date 20171225
 * @auth pengxuangang
 */
@Data
public class UniversalSearchResultResponse<T> implements Serializable {

    private static final long serialVersionUID = -341953922994376753L;
    //成功状态
    private boolean success;
    //信息
    private String message;
    //数据对象
    private Object obj;
    //返回数据条数
    private long totalCount;
    //消息ID[此字段仅通用版可用]
    private Integer msgId;
    //头部
    private List<String> header;
    //数据列表
    private Object datalist;

    private List <T> extInfoList;

    public UniversalSearchResultResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public UniversalSearchResultResponse(boolean success, String message, Integer msgId) {
        this.success = success;
        this.message = message;
        this.msgId = msgId;
    }
    public UniversalSearchResultResponse(boolean success, String message, Integer msgId, long totalCount, Object datalist) {
        this.success = success;
        this.message = message;
        this.totalCount = totalCount;
        this.msgId = msgId;
        this.datalist = datalist;
    }

    public UniversalSearchResultResponse(boolean success, String message, long totalCount, Object datalist) {
        this.success = success;
        this.message = message;
        this.totalCount = totalCount;
        this.datalist = datalist;
    }

    public UniversalSearchResultResponse(boolean success, String message, long totalCount, Integer msgId) {
        this.success = success;
        this.message = message;
        this.totalCount = totalCount;
        this.msgId = msgId;
    }

    public UniversalSearchResultResponse(boolean success, String message, long totalCount, Object datalist, List<T> extInfoList) {
        this.success = success;
        this.message = message;
        this.totalCount = totalCount;
        this.datalist = datalist;
        this.extInfoList = extInfoList;
    }
}
