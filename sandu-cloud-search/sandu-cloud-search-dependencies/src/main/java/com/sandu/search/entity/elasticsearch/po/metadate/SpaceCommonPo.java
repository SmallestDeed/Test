package com.sandu.search.entity.elasticsearch.po.metadate;

import lombok.Data;

import java.io.Serializable;

/**
 * 空间持久化对象
 *
 * @date 2018/5/31
 * @auth pengxuangang
 * @mail xuangangpeng@gmail.com
 */
@Data
public class SpaceCommonPo implements Serializable{

    private static final long serialVersionUID = -4050632843071862614L;

    private int id;
    //空间功能类型
    private int spaceFunctionId;
    //空间面积
    private Integer spaceAreas;
    //空间形状
    private Integer spaceShape;
    //空间编码
    private String spaceCode;
}
