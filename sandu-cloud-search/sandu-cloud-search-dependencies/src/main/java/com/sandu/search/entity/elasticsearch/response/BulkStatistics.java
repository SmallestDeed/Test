package com.sandu.search.entity.elasticsearch.response;

import lombok.Data;

import java.io.Serializable;

/**
 * 批量统计(用于批量操作Elasticsearch后统计结果)
 *
 * @date 20171211
 * @auth pengxuangang
 */
@Data
public class BulkStatistics implements Serializable {

    private static final long serialVersionUID = 4313072843575826072L;

    //总记录数
    private int totalCount;
    //总成功数
    private int totalSuccessCount;
    //总失败数
    private int totalFailCount;

    //索引数
    private int indexCount;
    //索引成功数
    private int indexSuccessCount;
    //索引失败数
    private int indexFailCount;

    //更新数
    private int updateCount;
    //更新成功数
    private int updateSuccessCount;
    //更新失败数
    private int updateFailCount;

    //删除数
    private int deleteCount;
    //删除成功数
    private int deleteSuccessCount;
    //删除失败数
    private int deleteFailCount;
}
