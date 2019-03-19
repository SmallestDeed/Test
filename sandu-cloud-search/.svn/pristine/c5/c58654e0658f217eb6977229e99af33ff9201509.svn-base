package com.sandu.search.service.design.impl;

import com.sandu.search.common.constant.DesignPlanType;
import com.sandu.search.dao.UserDesignPlanPurchaseRecordDao;
import com.sandu.search.entity.designplan.po.PlanTypeInfoPo;
import com.sandu.search.service.design.UserDesignPlanPurchaseRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("userDesignPlanPurchaseRecordService")
public class UserDesignPlanPurchaseRecordServiceImpl implements UserDesignPlanPurchaseRecordService {

    @Autowired
    private UserDesignPlanPurchaseRecordDao userDesignPlanPurchaseRecordDao;

    /**
     * 查询方案列表中用户购买的推荐方案
     * @author chenqiang
     * @param idList            方案id集合
     * @param userId            用户id
     * @return
     * @date 2018/9/10 0010 11:55
     */
    public Map<Integer,Integer> getPlanZFPayMap(List<Integer> idList, Integer userId){
        Map<Integer,Integer> resultMap = new HashMap<>();

        //查询方案列表中用户购买的推荐方案
        List<Integer> planLIst = userDesignPlanPurchaseRecordDao.selectPlanZFPayList(idList,userId);

        //判断
        if(null != planLIst && planLIst.size() > 0){

            for (Integer paPlanId : idList) {
                boolean bool = false;

                for (Integer rePlanId : planLIst) {
                    if(paPlanId.equals(rePlanId))
                        bool = true;
                }

                if(bool){
                    resultMap.put(paPlanId, DesignPlanType.RECOMMENDED_PLAN_IS_HAVEPURCHASED_ONE);            //已购买
                }else{
                    resultMap.put(paPlanId, DesignPlanType.RECOMMENDED_PLAN_IS_HAVEPURCHASED_ZEOR);            //未购买
                }
            }

        }else{

            for (Integer paPlanId : idList) {
                    resultMap.put(paPlanId, DesignPlanType.RECOMMENDED_PLAN_IS_HAVEPURCHASED_ZEOR);            //未购买
            }

        }

        return resultMap;
    }

    /**
     * 根据用户获取企业id
     * @author chenqiang
     * @param userId
     * @return
     * @date 2018/9/13 0013 15:59
     */
    public Integer getUserCompanyId(Integer userId){
        return userDesignPlanPurchaseRecordDao.selectUserCompanyId(userId);
    }

    @Override
    public List<PlanTypeInfoPo> getPlanBuyInfo(List<PlanTypeInfoPo> idList, Integer userId) {
        return userDesignPlanPurchaseRecordDao.getPlanBuyInfo(idList, userId);
    }

}