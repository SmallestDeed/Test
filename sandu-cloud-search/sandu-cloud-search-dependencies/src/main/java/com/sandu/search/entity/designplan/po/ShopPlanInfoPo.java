package com.sandu.search.entity.designplan.po;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @desc 店铺方案信息
 * @auth xiaoxc
 * @data 2018/10/31
 */
@Data
public class ShopPlanInfoPo implements Serializable {

    private static final long serialVersionUID = -1093779543773859603L;
    //店铺方案数量
    private int planCount;
    //店铺方案图片
    private List<PlanPicInfo> planPicList;

    @Data
    public static class PlanPicInfo implements Serializable{

        private static final long serialVersionUID = 998530346058269808L;
        public PlanPicInfo(){}
        public PlanPicInfo(Integer planId, String picPath, Integer planTableType){
            this.planId = planId;
            this.picPath = picPath;
            this.planTableType = planTableType;
        }
        //方案ID
        private Integer planId;
        //方案封面图
        private String picPath;
        //方案表类型（1：推荐方案 2：全屋方案）
        private Integer planTableType;
    }

}
