package com.sandu.search.entity.elasticsearch.po.metadate;

import lombok.Data;

import java.io.Serializable;

/**
 * 公司分类关联持久化对象
 *
 * @date 20180111
 * @auth pengxuangang
 */
@Data
public class CompanyCategoryRelPo implements Serializable {

    private static final long serialVersionUID = 7199736115983589952L;

    //公司ID
    private int companyId;
    //分类ID
    private int categoryId;

}

