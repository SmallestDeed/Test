package com.sandu.search.service.index.impl;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.sandu.designplan.model.DesignPlanRecommendedResult;
import com.sandu.search.common.tools.JsonUtil;
import com.sandu.search.dao.DesignPlanIndexDao;
import com.sandu.search.entity.designplan.po.RecommendationPlanPo;
import com.sandu.search.entity.designplan.po.TopDesignPlanRecommendPO;
import com.sandu.search.entity.designplan.vo.CollectRecommendedVo;
import com.sandu.search.entity.elasticsearch.po.metadate.DesignPlanRecommendedPo;
import com.sandu.search.exception.DesignPlanIndexException;
import com.sandu.search.service.index.DesignPlanIndexService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * 设计方案索引服务
 *
 * @date 20180531
 * @auth pengxuangang
 */
@Slf4j
@Service("designPlanIndexService")
public class DesignPlanIndexServiceImpl implements DesignPlanIndexService {
    private final static Cache<String, List<TopDesignPlanRecommendPO>> LOCAL_CACHE_PREFER_PLAN = CacheBuilder
            .newBuilder()
            .expireAfterWrite(2, TimeUnit.SECONDS)
            .build();

    private final static String CLASS_LOG_PREFIX = "设计方案索引服务:";

    private final static String LOCAL_CACHE_PREFER_PLAN_KEY = "LOCAL_CACHE_PREFER_PLAN_KEY";

    private final DesignPlanIndexDao designPlanIndexDao;

    @Autowired
    public DesignPlanIndexServiceImpl(DesignPlanIndexDao designPlanIndexDao) {
        this.designPlanIndexDao = designPlanIndexDao;
    }

    @Override
    public List<RecommendationPlanPo> queryRecommendationPlanList(int start, int limit) throws DesignPlanIndexException {

        //查询设计方案信息
        List<RecommendationPlanPo> recommendationPlanList;
        try {
            recommendationPlanList = designPlanIndexDao.queryRecommendationPlanList(start, limit);
        } catch (Exception e) {
            log.error(CLASS_LOG_PREFIX + "获取设计方案数据失败,Exception:{}", e);
            throw new DesignPlanIndexException(CLASS_LOG_PREFIX + "获取设计方案数据失败,Exception:" + e);
        }
        log.info(CLASS_LOG_PREFIX + "查询设计方案信息完成,List<ProductPo>长度:{}.", recommendationPlanList.size());

        return recommendationPlanList;
    }

    @Override
    public List<RecommendationPlanPo> queryRecommendationPlanDataList(List<Integer> recommendationPlanIdList, int start, int limit) throws DesignPlanIndexException {

        //查询设计方案信息
        List<RecommendationPlanPo> recommendationPlanList;
        try {
            recommendationPlanList = designPlanIndexDao.queryRecommendationPlanDataList(recommendationPlanIdList, start, limit);
        } catch (Exception e) {
            log.error(CLASS_LOG_PREFIX + "获取设计方案数据失败,Exception:{}", e);
            throw new DesignPlanIndexException(CLASS_LOG_PREFIX + "获取设计方案数据失败,Exception:" + e);
        }
        log.info(CLASS_LOG_PREFIX + "查询设计方案信息完成,List<RecommendationPlanPo>长度:{}.", recommendationPlanList.size());

        return recommendationPlanList;
    }


//    @Override
//    public List<RecommendationPlanPo> queryRecommendationPlanListByRecommendationPlanIdList(List<Integer> recommendationPlanIdList) throws DesignPlanIndexException {
//        if (null == recommendationPlanIdList || 0 >= recommendationPlanIdList.size()) {
//            return null;
//        }
//
//        //查询设计方案信息
//        log.info(CLASS_LOG_PREFIX + "正在查询设计方案信息,recommendationPlanIdList:{}.", JsonUtil.toJson(recommendationPlanIdList));
//        List<RecommendationPlanPo> recommendationPlanList;
//        try {
//            recommendationPlanList = designPlanIndexDao.queryRecommendationPlanListByRecommendationPlanIdList(recommendationPlanIdList);
//        } catch (Exception e) {
//            log.error(CLASS_LOG_PREFIX + "获取设计方案信息失败,recommendationPlanIdList:{}, Exception:{}.", JsonUtil.toJson(recommendationPlanIdList), e);
//            throw new DesignPlanIndexException(CLASS_LOG_PREFIX + "获取设计方案信息失败,Exception:" + e);
//        }
//        log.info(CLASS_LOG_PREFIX + "查询设计方案信息完成,List<RecommendationPlanPo>长度:{}.", recommendationPlanList.size());
//
//        return recommendationPlanList;
//    }

    @Override
    public List<DesignPlanRecommendedPo> queryRecommendationPlanPoListByRecommendationPlanId(Integer recommendationPlanId) throws DesignPlanIndexException {
        if (null == recommendationPlanId || 0 >= recommendationPlanId) {
            return null;
        }

        //查询推荐方案信息
        log.info(CLASS_LOG_PREFIX + "正在查询推荐方案信息,recommendationPlanId:{}.", recommendationPlanId);
        List<DesignPlanRecommendedPo> recommendationPlanList;
        try {
            recommendationPlanList = designPlanIndexDao.queryRecommendationPlanPoListByRecommendationPlanId(recommendationPlanId);
        } catch (Exception e) {
            log.error(CLASS_LOG_PREFIX + "获取推荐方案信息失败,recommendationPlanId:{}, Exception:{}.", recommendationPlanId, e);
            throw new DesignPlanIndexException(CLASS_LOG_PREFIX + "获取推荐方案信息失败,Exception:" + e);
        }
        log.info(CLASS_LOG_PREFIX + "查询推荐方案信息完成,List<DesignPlanRecommendedPo>长度:{}.", recommendationPlanList.size());

        return recommendationPlanList;
    }

    private List <TopDesignPlanRecommendPO> getTopDesignPlanRecommendFromDB(){
        return designPlanIndexDao.getTopDesignPlanRecommendPOList();
    }

    @Override
    public List <TopDesignPlanRecommendPO> getTopDesignPlanRecommendList() {
//        List <TopDesignPlanRecommendPO> TopDesignPlanRecommendList = new ArrayList<>();
//         try {
//             TopDesignPlanRecommendList = LOCAL_CACHE_PREFER_PLAN.get(LOCAL_CACHE_PREFER_PLAN_KEY, new Callable<List<TopDesignPlanRecommendPO>>() {
//                 @Override
//                 public List<TopDesignPlanRecommendPO> call() throws Exception {
//                     return getTopDesignPlanRecommendFromDB();
//                 }
//             });
//
//         } catch (ExecutionException e) {
//             e.printStackTrace();
//         }
        return getTopDesignPlanRecommendFromDB();
    }

}
