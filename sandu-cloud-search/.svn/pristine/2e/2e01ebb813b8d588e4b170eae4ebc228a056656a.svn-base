package com.sandu.search.controller;

import com.sandu.search.common.constant.DesignPlanType;
import com.sandu.search.common.tools.JsonUtil;
import com.sandu.search.common.tools.StringUtil;
import com.sandu.search.datasync.handler.RecommendationMessageHandler;
import com.sandu.search.enitiy.SyncRecommendedPlanVo;
import com.sandu.search.entity.elasticsearch.index.RecommendationPlanIndexMappingData;
import com.sandu.search.storage.StorageComponent;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * 推荐方案数据同步接口
 *
 * @date 2018/8/15
 * @auth xiaoxc
 */
@Slf4j
@RestController
@EnableAutoConfiguration
@RequestMapping("/sync/recommendedPlan")
public class RecommendedPlanDataSyncController {

    private final RecommendationMessageHandler recommendationMessageHandler;
    private final StorageComponent storageComponent;

    private final static String CLASS_LOG_PREFIX = "[数据同步]推荐方案信息同步接口:";

    @Autowired
    public RecommendedPlanDataSyncController(StorageComponent storageComponent, RecommendationMessageHandler recommendationMessageHandler) {
        this.recommendationMessageHandler = recommendationMessageHandler;
        this.storageComponent = storageComponent;
    }

    /**
     * 同步推荐方案信息
     *
     * @param recommendedPlanIdList
     * @return
     */
    @PostMapping("/ids")
    String syncRecommendedPlanInfoList(@RequestBody List<Integer> recommendedPlanIdList) {

        if (null == recommendedPlanIdList || 0 >= recommendedPlanIdList.size()) {
            log.info(CLASS_LOG_PREFIX + "同步推荐方案信息失败，推荐方案ID为空!");
            return CLASS_LOG_PREFIX + "同步推荐方案信息失败，推荐方案ID为空!";
        }

        //转换对象
        List<RecommendationPlanIndexMappingData> recommendationPlanIndexMappingDataList = new ArrayList<>(recommendedPlanIdList.size());
        recommendedPlanIdList.stream().distinct().forEach(planId -> {
            RecommendationPlanIndexMappingData recommendationPlanIndexMappingData = new RecommendationPlanIndexMappingData();
            recommendationPlanIndexMappingData.setId(planId);
            recommendationPlanIndexMappingDataList.add(recommendationPlanIndexMappingData);
        });

        log.info(CLASS_LOG_PREFIX + "Rest服务收到待同步方案，推荐方案ID:{}", JsonUtil.toJson(recommendedPlanIdList));

        //加入队列标识
        boolean addAndUpdateFlag = recommendationMessageHandler.addAndUpdate(recommendationPlanIndexMappingDataList);

        if (!addAndUpdateFlag) {
            log.error(CLASS_LOG_PREFIX + "同步推荐方案信息失败。加入待更新推荐方案队列失败!");
            return CLASS_LOG_PREFIX + "同步推荐方案信息失败。加入待更新推荐方案队列失败!";
        }

        log.info(CLASS_LOG_PREFIX + "推荐方案已加入待更新列表,推荐方案ID:{}.", JsonUtil.toJson(recommendedPlanIdList));
        return CLASS_LOG_PREFIX + "推荐方案已加入待更新列表,推荐方案ID:" + JsonUtil.toJson(recommendedPlanIdList);
    }


    /**
     * 查询待同步推荐方案列表
     *
     * @return
     */
    @GetMapping("/get")
    Map<String, Object> syncRecommendedPlanInfoList() {
        List<Integer> waitUpdateRecommendedPlanIdList = recommendationMessageHandler.queryWaitUpdateRecommendationPlanIdList();

        Map<String, Object> resultMap = new HashMap<>(2);
        resultMap.put("obj", waitUpdateRecommendedPlanIdList);
        resultMap.put("size", waitUpdateRecommendedPlanIdList.size());
        return resultMap;
    }

    /**
     * 执行方案元数据到缓存
     *
     * @return
     */
    @GetMapping("/runPlanMetaData")
    String runPlanMetaData() {
        log.info("----------开始手动执行元数据到缓存-----------");
        storageComponent.reloadAllRecommendationPlanStorageInCache();
        log.info("----------执行结束-----------");

        return "success";
    }

    /**
     * 强制更新某些推荐方案
     *
     * @param recommendedPlanIdList
     * @return
     * @author xiaoxc
     */
    @PostMapping("/syncRecommendedPlanInfo")
    public String syncRecommendedPlanInfo(@RequestBody List<Integer> recommendedPlanIdList, int planType) {
        if (recommendedPlanIdList == null || recommendedPlanIdList.size() < 1) {
            return "Param is empty!";
        }
        if (DesignPlanType.RECOMMENDED_TABLE_TYPE == planType) {
            recommendationMessageHandler.sycnRecommendationPlanData(recommendedPlanIdList, "");
        }
        if (DesignPlanType.FULLHOUSE_TABLE_TYPE == planType) {
            recommendationMessageHandler.sycnFullHousePlanData(recommendedPlanIdList);
        }
        return "success";
    }

    /**
     * 供远程调用
     *
     * @param syncPlanVo
     * @return
     * @author xiaoxc
     */
    @PostMapping("/syncRecommendedPlanById")
    public String syncRecommendedPlanInfo(@RequestBody SyncRecommendedPlanVo syncPlanVo) {
        if (null == syncPlanVo) {
            return "Param is empty!";
        }
        String recommendedPlanIds = syncPlanVo.getRecommendedPlanIds();
        String businessType = syncPlanVo.getBusinessType();

        log.info(CLASS_LOG_PREFIX + businessType + ",recommendedPlanIds:{}" + recommendedPlanIds);

        List<String> recommendedPlanIdList = null;
        if (!StringUtils.isEmpty(recommendedPlanIds)) {
            recommendedPlanIdList = new ArrayList<>(Arrays.asList(recommendedPlanIds.split(",")));
        }
        if (recommendedPlanIdList == null || recommendedPlanIdList.size() < 1) {
            return "RecommendedPlanIds is empty!";
        } else {
            List<Integer> idList = StringUtil.transformInteger(recommendedPlanIdList);
            recommendationMessageHandler.sycnRecommendationPlanData(idList, "");
        }
        log.info(CLASS_LOG_PREFIX + "远程调用更新完成！");
        return "success";
    }


}
