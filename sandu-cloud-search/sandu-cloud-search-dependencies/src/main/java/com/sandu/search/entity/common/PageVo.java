package com.sandu.search.entity.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 分页对象
 *
 * @date 20180103
 * @auth pengxuangang
 */
@Data
public class PageVo implements Serializable {

    private static final long serialVersionUID = -1012575468651374016L;

    //数据起始条数
    private int start;
    //分页大小
    private int pageSize;
    //当前页数
    private int currentPage;
    //总页数
    private int totalCount;

}
