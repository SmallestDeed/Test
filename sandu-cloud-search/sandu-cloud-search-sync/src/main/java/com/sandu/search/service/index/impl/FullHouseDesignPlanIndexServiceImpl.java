package com.sandu.search.service.index.impl;

import com.sandu.search.dao.FullHouseDesignPlanIndexDao;
import com.sandu.search.entity.designplan.po.RecommendationPlanPo;
import com.sandu.search.entity.fullhouse.FullHouseDesignPlanPo;
import com.sandu.search.exception.FullHouseDesignPlanException;
import com.sandu.search.service.index.FullHouseDesignPlanIndexService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 全屋方案索引服务
 *
 * @date 2019/09/13
 * @auth zhangchengda
 */
@Log4j2
@Service("fullHouseDesignPlanIndexService")
public class FullHouseDesignPlanIndexServiceImpl implements FullHouseDesignPlanIndexService {
    private static final String CLASS_LOG_PREFIX = "全屋方案索引服务:";

    private final FullHouseDesignPlanIndexDao fullHouseDesignPlanIndexDao;

    @Autowired
    public FullHouseDesignPlanIndexServiceImpl(FullHouseDesignPlanIndexDao fullHouseDesignPlanIndexDao) {
        this.fullHouseDesignPlanIndexDao = fullHouseDesignPlanIndexDao;
    }

    @Override
    public List<FullHouseDesignPlanPo> queryFullHouseDesignPlanList(int start, int limit) throws FullHouseDesignPlanException {
        //查询全屋方案信息
        List<FullHouseDesignPlanPo> fullHouseDesignPlanList;
        try {
            fullHouseDesignPlanList = fullHouseDesignPlanIndexDao.queryFullHouseDesignPlanList(start, limit);
        } catch (Exception e) {
            log.error(CLASS_LOG_PREFIX + "获取全屋方案数据失败,Exception:{}", e);
            throw new FullHouseDesignPlanException(CLASS_LOG_PREFIX + "获取全屋方案数据失败,Exception:" + e);
        }
        log.info(CLASS_LOG_PREFIX + "查询全屋方案信息完成,List<ProductPo>长度:{}.", fullHouseDesignPlanList.size());

        return fullHouseDesignPlanList;
    }

    @Override
    public List<RecommendationPlanPo> queryFullHousePlanList(int start, int limit) throws FullHouseDesignPlanException {
        //查询全屋方案信息
        List<RecommendationPlanPo> fullHousePlanList;
        try {
            fullHousePlanList = fullHouseDesignPlanIndexDao.queryFullHousePlanList(start, limit);
        } catch (Exception e) {
            log.error(CLASS_LOG_PREFIX + "获取全屋方案数据失败,Exception:{}", e);
            throw new FullHouseDesignPlanException(CLASS_LOG_PREFIX + "获取全屋方案数据失败,Exception:" + e);
        }
        log.info(CLASS_LOG_PREFIX + "查询全屋方案信息完成,List<RecommendationPlanPo>长度:{}.", fullHousePlanList.size());

        return fullHousePlanList;
    }


    @Override
    public List<RecommendationPlanPo> queryFullHousePlanListByIds(List<Integer> idList) throws FullHouseDesignPlanException {
        //查询全屋方案信息
        List<RecommendationPlanPo> fullHousePlanList;
        try {
            fullHousePlanList = fullHouseDesignPlanIndexDao.queryFullHousePlanListByIds(idList);
        } catch (Exception e) {
            log.error(CLASS_LOG_PREFIX + "获取全屋方案数据失败,Exception:{}", e);
            throw new FullHouseDesignPlanException(CLASS_LOG_PREFIX + "获取全屋方案数据失败,Exception:" + e);
        }
        log.info(CLASS_LOG_PREFIX + "查询全屋方案信息完成,List<RecommendationPlanPo>长度:{}.", fullHousePlanList.size());

        return fullHousePlanList;
    }
}
