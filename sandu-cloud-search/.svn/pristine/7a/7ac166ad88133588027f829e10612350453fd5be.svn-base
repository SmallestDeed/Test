package com.sandu.search.service.design;

import com.sandu.search.entity.designplan.po.PlanTypeInfoPo;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Map;

@Component
public interface UserDesignPlanPurchaseRecordService {

    /**
     * 查询方案列表中用户购买的推荐方案
     * @author chenqiang
     * @param idList            方案id集合
     * @param userId            用户id
     * @return
     * @date 2018/9/10 0010 11:55
     */
    Map<Integer,Integer> getPlanZFPayMap(List<Integer> idList, Integer userId);

    /**
     * 根据用户获取企业id
     * @author chenqiang
     * @param userId
     * @return
     * @date 2018/9/13 0013 15:59
     */
    Integer getUserCompanyId(Integer userId);

    /**
     * 获取用户购买方案信息
     * @auth xiaoxc
     * @param idList
     * @return
     * @data 20181115
     */
    List<PlanTypeInfoPo> getPlanBuyInfo(List<PlanTypeInfoPo> idList, Integer userId);
}