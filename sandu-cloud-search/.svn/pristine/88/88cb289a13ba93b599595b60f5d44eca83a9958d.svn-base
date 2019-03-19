package com.sandu.search.dao;


import com.sandu.search.entity.designplan.po.PlanTypeInfoPo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Map;

@Repository
public interface UserDesignPlanPurchaseRecordDao {

    /**
     * 查询方案列表中用户购买的推荐方案
     * @author chenqiang
     * @param userId            用户id
     * @param idList            方案id集合
     * @return
     * @date 2018/9/10 0010 11:55
     */
    List<Integer> selectPlanZFPayList(@Param("idList") List<Integer> idList,@Param("userId") Integer userId);

    /**
     * 根据用户获取企业id
     * @author chenqiang
     * @param userId
     * @return
     * @date 2018/9/13 0013 15:59
     */
    Integer selectUserCompanyId(@Param("userId")Integer userId);

    /**
     * 获取用户方案购买信息
     * @auth xiaoxc
     * @param idList
     * @return
     * @data 20181115
     */
    List<PlanTypeInfoPo> getPlanBuyInfo(@Param("idList") List<PlanTypeInfoPo> idList, @Param("userId") Integer userId);
}