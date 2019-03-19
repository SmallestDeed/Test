package com.sandu.search.entity.designplan.po;

import lombok.Data;

import java.io.Serializable;

/**
 * Copyright (c) http://www.sanduspace.cn. All rights reserved.
 *
 * @author :  Steve
 * @date : 2018/12/6
 * @since : sandu_yun_1.0
 */
@Data
public class TopDesignPlanRecommendPO implements Serializable{
    private static final long serialVersionUID = 8118653087475219679L;

    private Integer id;

    private Integer ordering;

    private Integer spaceType;

    private String spaceName;

}
