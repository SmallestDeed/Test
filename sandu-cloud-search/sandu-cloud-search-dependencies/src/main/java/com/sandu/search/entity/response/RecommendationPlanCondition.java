package com.sandu.search.entity.response;

import com.sandu.search.entity.common.PageVo;
import com.sandu.search.entity.designplan.vo.RecommendationPlanSearchVo;
import lombok.Data;

import java.io.Serializable;

/**
 * 推荐方案条件对象
 *
 * @date 2018/6/1
 * @auth pengxuangang
 * @mail xuangangpeng@gmail.com
 */
@Data
public class RecommendationPlanCondition implements Serializable {

    private static final long serialVersionUID = -1568892079643845484L;

    //条件对象
    private RecommendationPlanSearchVo recommendationPlanSearchVo;
    //分页对象
    private PageVo pageVo;
}
