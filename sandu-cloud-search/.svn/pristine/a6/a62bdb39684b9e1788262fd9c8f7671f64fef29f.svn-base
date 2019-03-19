package com.sandu.search.service.design.impl;

import com.sandu.search.common.tools.StringUtil;
import com.sandu.search.datasync.handler.RecommendationMessageHandler;
import com.sandu.search.entity.amqp.QueueMessage;
import com.sandu.search.exception.ElasticSearchException;
import com.sandu.search.service.design.RecommendedPlanService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Desc 同步推荐方案实现服务
 * @auth xiaoxc
 * @data 20181107
 */
@Slf4j
@Service("recommendedPlanService")
public class RecommendedPlanServiceImpl implements RecommendedPlanService {

    private final String CLASS_LOG_PREFIX = "同步推荐方案实现服务:";
    @Autowired
    private RecommendationMessageHandler recommendationMessageHandler;

    @Override
    public boolean syncRecommendedPlan(String recommendedPlanIds, int actionType) throws ElasticSearchException {

        log.info(CLASS_LOG_PREFIX + "recommendedPlanIds:{},actionType:{}", recommendedPlanIds, actionType);

        List<String> recommendedPlanIdList = null;
        if (!StringUtils.isEmpty(recommendedPlanIds)) {
            recommendedPlanIdList = new ArrayList<>(Arrays.asList(recommendedPlanIds.split(",")));
        }
        if (recommendedPlanIdList == null || recommendedPlanIdList.size() < 1) {
            log.error(CLASS_LOG_PREFIX + "参数recommendedPlanIds为空！");
            return false;
        }
        if (QueueMessage.UPDATE_ACTION == actionType) {
            //更新
            recommendationMessageHandler.sycnRecommendationPlanData(StringUtil.transformInteger(recommendedPlanIdList), "");

        } else if (QueueMessage.DELETE_ACTION == actionType) {
            //删除
            recommendationMessageHandler.deleteByIds(StringUtil.transformInteger(recommendedPlanIdList));
        } else {
            log.info(CLASS_LOG_PREFIX + "操作类型错误,actionType:{}", actionType);
        }

        return true;
    }
}
