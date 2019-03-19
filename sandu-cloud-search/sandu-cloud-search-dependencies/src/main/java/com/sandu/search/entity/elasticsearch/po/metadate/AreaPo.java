package com.sandu.search.entity.elasticsearch.po.metadate;

import lombok.Data;

import java.io.Serializable;

/**
 * 区域数据持久化对象
 *
 * @date 20180104
 * @auth pengxuangang
 */
@Data
public class AreaPo implements Serializable {

    private static final long serialVersionUID = 2485108070755202145L;

    //ID
    private int areaId;
    //区域编码
    private String areaCode;
    //区域名
    private String areaName;
    //区域父ID
    private String areaParentId;
    //区域级别ID
    private int areaLevelId;
    //区域邮编
    private String areaZipCode;
}
