package com.sandu.search.entity.elasticsearch.search;

import lombok.Data;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.script.Script;
import org.elasticsearch.search.sort.SortMode;
import org.elasticsearch.search.sort.SortOrder;

import java.io.Serializable;

/**
 * 排序对象
 *
 * @date 2018/4/18
 * @auth pengxuangang
 * @mail xuangangpeng@gmail.com
 */
@Data
public class SortOrderObject implements Serializable {

    private static final long serialVersionUID = 2660091976498568656L;

    public static final String DEFAULT_SORT = "defSort"; //默认排序
    public static final String SCRIPT_SORT = "script"; //script排序
    public static final String NESTED_SORT = "nested"; //nested排序
    public static final String SCRIPT_SORT_NUMBER = "number"; //script排序类型


    public SortOrderObject(String fieldName, SortOrder sortOrder, SortMode sortMode, String sortType) {
        this.fieldName = fieldName;
        this.sortOrder = sortOrder;
        this.sortMode = sortMode;
        this.sortType = sortType;
    }

    public SortOrderObject(Script script, SortOrder sortOrder, String sortType) {
        this.script = script;
        this.sortOrder = sortOrder;
        this.sortType = sortType;
    }

    public SortOrderObject(String fieldName, SortOrder sortOrder, SortMode sortMode, String sortType, String nestedPath, BoolQueryBuilder nestedFilter) {
        this.fieldName = fieldName;
        this.sortOrder = sortOrder;
        this.sortMode = sortMode;
        this.sortType = sortType;
        this.nestedPath = nestedPath;
        this.nestedFilter = nestedFilter;
    }

    //字段名
    private String fieldName;
    //排序类型
    private SortOrder sortOrder;
    //排序模式
    private SortMode sortMode;
    //script排序
    private Script script;
    //排序类型
    private String sortType;
    //nested嵌套对象
    private String nestedPath;
    //nested过滤字段
    private BoolQueryBuilder nestedFilter;


}
