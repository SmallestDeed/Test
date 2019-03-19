package com.sandu.search.entity.elasticsearch.dco;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 单值多匹配数据转换对象
 *
 * @Date 20180120
 * @auth pengxuangang
 */
@Data
public class MultiMatchField implements Serializable{

    private static final long serialVersionUID = -6623649028934554842L;

    //搜索关键字
    private String searchKeyword;
    //匹配字段列表
    private List<String> matchFieldList;

    public MultiMatchField(String searchKeyword, List<String> matchFieldList) {
        this.searchKeyword = searchKeyword;
        this.matchFieldList = matchFieldList;
    }
}
