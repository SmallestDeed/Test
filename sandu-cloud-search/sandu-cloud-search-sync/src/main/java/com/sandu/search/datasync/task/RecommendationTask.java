package com.sandu.search.datasync.task;

import com.sandu.search.datasync.handler.RecommendationMessageHandler;
import com.sandu.search.initialize.FullHouseIndex;
import com.sandu.search.initialize.RecommendationPlanIndex;
import com.sandu.search.storage.StorageComponent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 同步推荐方案数据任务
 *
 * @date 20171229
 * @auth pengxuangang
 */
@Slf4j
@Component
public class RecommendationTask {

    private final static String CLASS_LOG_PREFIX = "同步推荐方案数据任务:";

    private final StorageComponent storageComponent;
    private final RecommendationPlanIndex recommendationPlanIndex;
    private final RecommendationMessageHandler recommendationMessageHandler;
    private final FullHouseIndex fullHouseIndex;

    //全量任务执行次数
    private static int fullTaskExcuteCount = 0;
    //增量任务执行次数
    private static int incrementTaskExcuteCount = 0;
    // 是否正在执行全量更新(作用:1.避免同时执行两个全量更新;2.避免执行全量更新的同时执行增量更新)
    public static boolean isRuningFullTask = false;


    @Autowired
    public RecommendationTask(RecommendationMessageHandler recommendationMessageHandler, StorageComponent storageComponent, RecommendationPlanIndex recommendationPlanIndex,FullHouseIndex fullHouseIndex) {
        this.storageComponent = storageComponent;
        this.recommendationPlanIndex = recommendationPlanIndex;
        this.recommendationMessageHandler = recommendationMessageHandler;
        this.fullHouseIndex =fullHouseIndex;
    }

    /**
     * 推荐方案数据全量同步
     *
     * @date 2018/8/15
     * @auth xiaoxc
     * @strategy 每天凌晨3点执行
     */
    @Scheduled(cron = "${task.recommendationplan.all.cron}")
    public void recommendationPlanDataSyncTask() {
        //任务开始时间
        long startTime = System.currentTimeMillis();

        log.info(CLASS_LOG_PREFIX + "准备执行推荐方案全量更新...");

        //第一次任务跳过
        /*if (0 == fullTaskExcuteCount) {
            ++fullTaskExcuteCount;
            log.info(CLASS_LOG_PREFIX + "推荐方案数据全量同步,第一次任务跳过...");
            return;
        }
        // 如果正在执行全量更新,则不需要同时执行第二个全量更新任务
        if(isRuningFullTask) {
            log.info(CLASS_LOG_PREFIX + "全量更新停止(原因:有任务正在进行全量更新)");
            return;
        }*/

        //推荐方案全量更新标识
        RecommendationTask.isRuningFullTask = true;
        //更新缓存数据
        storageComponent.reloadAllRecommendationPlanStorageInCache();
        //索引全屋方案到推荐方案表
        log.info(CLASS_LOG_PREFIX + "开始同步全屋方案数据...");
        fullHouseIndex.syncFullHousePlanData();
        log.info(CLASS_LOG_PREFIX + "开始全量索引推荐方案数据....");
        recommendationPlanIndex.syncRecommendationPlanData();
        log.info(CLASS_LOG_PREFIX + "开始全量索引推荐方案数据完成...耗时:{}ms", (System.currentTimeMillis() - startTime));
    }


    /**
     * 推荐方案数据增量更新
     *
     * @date 20180815
     * @auth xiaoxc
     * @strategy 每半小时一次
     */
    //@Scheduled(cron = "${task.recommendationplan.incr.cron}")
    public void recommendationPlanIncrementDataSyncTask() {

        long startTime = System.currentTimeMillis();

        log.info(CLASS_LOG_PREFIX + "准备执行推荐方案增量更新...");

        //第一次任务跳过
        if (0 == incrementTaskExcuteCount) {
            ++incrementTaskExcuteCount;
            log.info(CLASS_LOG_PREFIX + "推荐方案数据增量同步,第一次任务跳过...");
            return;
        }

        if(isRuningFullTask) {
            log.info(CLASS_LOG_PREFIX + "推荐方案增量更新停止(原因:正在进行推荐方案全量更新)");
            return;
        }

        log.info(CLASS_LOG_PREFIX + "推荐方案数据增量更新.....");
        List<Integer> waitUpdateRecommendedPlanIdList = recommendationMessageHandler.queryWaitUpdateRecommendationPlanIdList();
        if (null != waitUpdateRecommendedPlanIdList && 0 <waitUpdateRecommendedPlanIdList.size()) {
            recommendationMessageHandler.sycnRecommendationPlanData(waitUpdateRecommendedPlanIdList, "Increment");
        } else {
            log.info(CLASS_LOG_PREFIX + "推荐方案无增量更新数据!");
        }
        log.info(CLASS_LOG_PREFIX + "推荐方案数据增量更新完成...耗时:{}ms", (System.currentTimeMillis() - startTime));
    }
}
