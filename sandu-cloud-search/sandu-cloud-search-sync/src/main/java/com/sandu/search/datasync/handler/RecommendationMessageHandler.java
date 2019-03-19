package com.sandu.search.datasync.handler;

import com.sandu.search.common.constant.DesignPlanType;
import com.sandu.search.common.constant.UserDefinedConstant;
import com.sandu.search.common.tools.EntityUtil;
import com.sandu.search.common.tools.JsonUtil;
import com.sandu.search.config.ElasticSearchConfig;
import com.sandu.search.entity.designplan.po.RecommendationPlanPo;
import com.sandu.search.entity.elasticsearch.constant.IndexConstant;
import com.sandu.search.entity.elasticsearch.constant.TypeConstant;
import com.sandu.search.entity.elasticsearch.dto.DeleteRequestDTO;
import com.sandu.search.entity.elasticsearch.dto.UpdateRequestDTO;
import com.sandu.search.entity.elasticsearch.index.RecommendationPlanIndexMappingData;
import com.sandu.search.entity.elasticsearch.po.metadate.CompanyShopPlanPo;
import com.sandu.search.entity.elasticsearch.response.BulkStatistics;
import com.sandu.search.exception.DesignPlanIndexException;
import com.sandu.search.exception.ElasticSearchException;
import com.sandu.search.exception.FullHouseDesignPlanException;
import com.sandu.search.exception.MetaDataException;
import com.sandu.search.initialize.FullHouseIndex;
import com.sandu.search.initialize.RecommendationPlanIndex;
import com.sandu.search.service.elasticsearch.ElasticSearchService;
import com.sandu.search.service.index.DesignPlanIndexService;
import com.sandu.search.service.index.FullHouseDesignPlanIndexService;
import com.sandu.search.service.metadata.MetaDataService;
import com.sandu.search.storage.design.CompanyShopPlanMetaDataStorage;
import com.sandu.search.storage.design.DesignPlanDecoratePriceMetaDataStorage;
import com.sandu.search.storage.design.DesignPlanPlatformMetaDataStorage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 推荐方案消息处理
 *
 * @date 2018/6/9
 * @auth pengxuangang
 * @mail xuangangpeng@gmail.com
 */
@Slf4j
@Component
public class RecommendationMessageHandler {

    private final static String CLASS_LOG_PREFIX = "Rabbit推荐方案消息处理:";

    private final DesignPlanIndexService designPlanIndexService;
    private final RecommendationPlanIndex recommendationPlanIndex;
    private final DesignPlanPlatformMetaDataStorage designPlanPlatformMetaDataStorage;
    private final FullHouseDesignPlanIndexService fullHouseDesignPlanIndexService;
    private final FullHouseIndex fullHouseIndex;
    private final DesignPlanDecoratePriceMetaDataStorage designPlanDecoratePriceMetaDataStorage;
    private final CompanyShopPlanMetaDataStorage companyShopPlanMetaDataStorage;
    private final MetaDataService metaDataService;
    private final ElasticSearchConfig elasticSearchConfig;
    private final ElasticSearchService elasticSearchService;

    @Autowired
    public RecommendationMessageHandler(
            MetaDataService metaDataService,
            ElasticSearchConfig elasticSearchConfig,
            ElasticSearchService elasticSearchService,
            DesignPlanDecoratePriceMetaDataStorage designPlanDecoratePriceMetaDataStorage,
                                        FullHouseIndex fullHouseIndex,
                                        FullHouseDesignPlanIndexService fullHouseDesignPlanIndexService,
                                        DesignPlanPlatformMetaDataStorage designPlanPlatformMetaDataStorage,
                                        RecommendationPlanIndex recommendationPlanIndex,
                                        DesignPlanIndexService designPlanIndexService,
                                        CompanyShopPlanMetaDataStorage companyShopPlanMetaDataStorage) {
        this.metaDataService = metaDataService;
        this.elasticSearchConfig = elasticSearchConfig;
        this.elasticSearchService = elasticSearchService;
        this.designPlanIndexService = designPlanIndexService;
        this.recommendationPlanIndex = recommendationPlanIndex;
        this.designPlanPlatformMetaDataStorage = designPlanPlatformMetaDataStorage;
        this.fullHouseDesignPlanIndexService = fullHouseDesignPlanIndexService;
        this.fullHouseIndex = fullHouseIndex;
        this.designPlanDecoratePriceMetaDataStorage = designPlanDecoratePriceMetaDataStorage;
        this.companyShopPlanMetaDataStorage = companyShopPlanMetaDataStorage;
    }

    //待更新推荐方案列表
    private static volatile List<Integer> waitUpdateRecommendationPlanIdList = new ArrayList<>();

    /**
     * 新增/更新推荐方案消息处理
     *
     * @param recommendationPlanIndexMappingDataList 推荐方案索引对象
     * @return
     */
    @SuppressWarnings("all")
    public boolean addAndUpdate(List<RecommendationPlanIndexMappingData> recommendationPlanIndexMappingDataList) {

        if (null == recommendationPlanIndexMappingDataList || 0 >= recommendationPlanIndexMappingDataList.size()) {
            log.info(CLASS_LOG_PREFIX + "消费消息失败，消息对象为空。");
            return false;
        }

        //推荐方案增量更新
        List<RecommendationPlanIndexMappingData> recommendationPlanList = new ArrayList<>(recommendationPlanIndexMappingDataList.size());
        //根据推荐方案ID更新
        List<Integer> recommendationPlanIdList = new ArrayList<>(recommendationPlanIndexMappingDataList.size());

        recommendationPlanIndexMappingDataList.forEach(recommendationPlanIndexMappingData -> {
            //判断更新类型
            List<String> fieldList = null;
            try {
                fieldList = EntityUtil.queryHasValueExcludeNameIsIdInField(recommendationPlanIndexMappingData);
            } catch (IllegalAccessException e) {
                log.warn(CLASS_LOG_PREFIX + "检查对象失败,IllegalAccessException:{}.", e);
            }

            if (null != fieldList && 0 < fieldList.size()) {
                //属性增量更新
                recommendationPlanList.add(recommendationPlanIndexMappingData);
            } else {
                if (recommendationPlanIndexMappingData.getId() != null) {
                    recommendationPlanIdList.add(recommendationPlanIndexMappingData.getId());
                }
            }
        });

        if (null != recommendationPlanIdList && 0 < recommendationPlanIdList.size()) {
            sycnRecommendationPlanData(recommendationPlanIdList, "");
            log.info(CLASS_LOG_PREFIX + "消费消息成功，推荐方案已更新方案列表!recommendationPlanIdList:{}", JsonUtil.toJson(recommendationPlanIdList));
        }

        //属性增量更新
        if (null != recommendationPlanList && 0 < recommendationPlanList.size()) {
            //加入属性增量更新列表
            updateIncrRecommendationPlanInfo(recommendationPlanList);
            log.info(CLASS_LOG_PREFIX + "消费消息成功，推荐方案已加入属性增量更新列表!recommendationPlanIndexMappingDataArrayList:{},现有待更新推荐方案数据条数:{}.", JsonUtil.toJsonExcludeEmpty(recommendationPlanList), recommendationPlanList.size());
        }

        return true;
    }

    /**
     * 获取待更新推荐方案ID列表
     *
     * @return
     */
    public List<Integer> queryWaitUpdateRecommendationPlanIdList() {

        if (null != waitUpdateRecommendationPlanIdList && waitUpdateRecommendationPlanIdList.size() > 0) {
            //去空
            List<Integer> recommendedPlanIdList = waitUpdateRecommendationPlanIdList.stream().filter(waitUpdatePlanId -> null != waitUpdatePlanId && 0 != waitUpdatePlanId).collect(Collectors.toList());
            //去重
            recommendedPlanIdList = waitUpdateRecommendationPlanIdList.stream().distinct().collect(Collectors.toList());

            waitUpdateRecommendationPlanIdList = new ArrayList<>();
            return  recommendedPlanIdList;
        }

        return waitUpdateRecommendationPlanIdList;
    }

    //更新增量推荐方案数据
    public boolean updateIncrRecommendationPlanInfo(List<RecommendationPlanIndexMappingData> recommendationPlanList) {

        if (null == recommendationPlanList || 0 == recommendationPlanList.size()) {
            return true;
        }

        //构造更新对象
        for (RecommendationPlanIndexMappingData recommendationPlan : recommendationPlanList) {

            if (null == recommendationPlan || null == recommendationPlan.getId() || 0 == recommendationPlan.getId()) {
                continue;
            }

            log.info(" planType: " + recommendationPlan.getPlanTableType() + ", planId: " +recommendationPlan.getId());

            if (null != recommendationPlan.getPlanTableType()) {
                //推荐方案
                if (DesignPlanType.RECOMMENDED_TABLE_TYPE == recommendationPlan.getPlanTableType()) {
                    //加入待更新推荐方案列表
                    //waitUpdateRecommendationPlanIdList.add(recommendationPlan.getId());
                    sycnRecommendationPlanData(Arrays.asList(recommendationPlan.getId()), "");
                }
                //全屋方案
                if (DesignPlanType.FULLHOUSE_TABLE_TYPE == recommendationPlan.getPlanTableType()) {
                    sycnFullHousePlanData(Arrays.asList(recommendationPlan.getId()));
                }
            }
        }

        return true;
    }

    /**
     * 删除推荐方案消息处理
     *
     * @param planIndexMappingDataList 推荐方案索引对象
     * @return
     */
    public boolean delete(List<RecommendationPlanIndexMappingData> planIndexMappingDataList) throws ElasticSearchException {

        if (null == planIndexMappingDataList || 0 >= planIndexMappingDataList.size()) {
            log.info(CLASS_LOG_PREFIX + "消费推荐方案消息失败，消息对象为空。");
            return false;
        }

        return true;
    }

    /**
     * 同步删除推荐方案处理
     *
     * @param idList 推荐方案索引对象
     * @return
     */
    public boolean deleteByIds(List<Integer> idList) throws ElasticSearchException {

        if (null == idList || 0 >= idList.size()) {
            log.info(CLASS_LOG_PREFIX + "同步删除推荐方案失败，同步对象为空。");
            return false;
        }
        String indexName = StringUtils.isEmpty(elasticSearchConfig.getRecommendedPlanDataIndexName())? IndexConstant.RECOMMENDATION_PLAN_ALIASES:elasticSearchConfig.getRecommendedPlanDataIndexName();
        for (Integer id : idList) {
            DeleteRequestDTO deleteRequestDTO = new DeleteRequestDTO(
                    indexName,
                    TypeConstant.RECOMMENDATION_PLAN_TYPE,
                    id.toString());
            boolean falg = elasticSearchService.delete(deleteRequestDTO);
            if (!falg) {
                log.error(CLASS_LOG_PREFIX + "同步删除推荐方案失败,id:{}", id);
            }
        }

        return true;
    }

    /**
     * 增量同步推荐方案
     * @param recommendedPlanIdList
     */
    public void sycnRecommendationPlanData(List<Integer> recommendedPlanIdList, String type) {

        //去空
        recommendedPlanIdList = recommendedPlanIdList.stream().filter(waitUpdateProductId -> null != waitUpdateProductId && 0 != waitUpdateProductId).collect(Collectors.toList());
        //去重
        recommendedPlanIdList = recommendedPlanIdList.stream().distinct().collect(Collectors.toList());

        /*if ("Increment".equals(type)) {
            //更新完成删除待更新区域产品数据
            waitUpdateRecommendationPlanIdList = new ArrayList<>();
        }*/
        if (null == recommendedPlanIdList || recommendedPlanIdList.size() == 0) {
            return;
        }

        List<RecommendationPlanPo> recommendationPlanList;
        /********************************** 查询推荐方案信息 *********************************/
        //更新增量数据
        try {
            recommendationPlanList = designPlanIndexService.queryRecommendationPlanDataList(recommendedPlanIdList,0, recommendedPlanIdList.size());
        } catch (DesignPlanIndexException e) {
            log.error(CLASS_LOG_PREFIX + "查询推荐方案信息失败:DesignPlanIndexException:{}", e);
            if ("Increment".equals(type)) {
                waitUpdateRecommendationPlanIdList.addAll(recommendedPlanIdList);
            }
            return;
        }

        if (null == recommendationPlanList || 0 >= recommendationPlanList.size()) {
            return;
        }

        //切换推荐方案存储模式
        log.info(CLASS_LOG_PREFIX + "开始增量更新推荐方案平台元数据...");
        designPlanPlatformMetaDataStorage.updateDataByRecommendedPlanIds(recommendedPlanIdList, DesignPlanType.RECOMMENDED_TABLE_TYPE);
        log.info(CLASS_LOG_PREFIX + "完成增量更新推荐方案平台元数据...");

        log.info(CLASS_LOG_PREFIX + "开始增量更新方案装修报价元数据...");
        designPlanDecoratePriceMetaDataStorage.updateData();
        log.info(CLASS_LOG_PREFIX + "完成增量更新方案装修报价元数据...");

        //更新数据
        log.info(CLASS_LOG_PREFIX + "开始更新数据，数据总条数:{}.", recommendedPlanIdList.size());
        int indexSuccessCount = recommendationPlanIndex.indexRecommendationPlanData(recommendationPlanList, UserDefinedConstant.INCREMENT_UPDATE_TYPE);
        log.info(CLASS_LOG_PREFIX + "更新数据完成，成功{}条,失败{}条.", indexSuccessCount, recommendedPlanIdList.size() - indexSuccessCount);
    }

    /**
     * 增量同步全屋推荐方案
     * @param fullHousePlanIdList
     */
    public void sycnFullHousePlanData(List<Integer> fullHousePlanIdList) {

        //去空
        fullHousePlanIdList = fullHousePlanIdList.stream().filter(waitUpdateProductId -> null != waitUpdateProductId && 0 != waitUpdateProductId).collect(Collectors.toList());
        //去重
        fullHousePlanIdList = fullHousePlanIdList.stream().distinct().collect(Collectors.toList());


        List<RecommendationPlanPo> fullHousePlanList;
        /********************************** 查询增量方案信息 *********************************/
        //更新增量数据
        try {
            fullHousePlanList = fullHouseDesignPlanIndexService.queryFullHousePlanListByIds(fullHousePlanIdList);
        } catch (FullHouseDesignPlanException e) {
            log.error(CLASS_LOG_PREFIX + "查询增量方案信息失败:fullHousePlanIdList:{}, DesignPlanIndexException:{}", fullHousePlanIdList.toString(), e);
            return;
        }
        if (null == fullHousePlanList || fullHousePlanList.size() < 1) {
            return;
        }

        //切换推荐方案存储模式
        log.info(CLASS_LOG_PREFIX + "开始增量更新全屋方案平台元数据...");
        designPlanPlatformMetaDataStorage.updateDataByRecommendedPlanIds(fullHousePlanIdList, DesignPlanType.FULLHOUSE_TABLE_TYPE);
        log.info(CLASS_LOG_PREFIX + "完成增量更新全屋方案平台元数据...");

        log.info(CLASS_LOG_PREFIX + "开始增量更新全屋方案装修报价元数据...");
        designPlanDecoratePriceMetaDataStorage.updateData();
        log.info(CLASS_LOG_PREFIX + "完成增量更新全屋方案装修报价元数据...");

        //增量更新店铺方案元数据...
        /*log.info(CLASS_LOG_PREFIX + "开始增量更新店铺全屋方案元数据...");
        companyShopPlanMetaDataStorage.updateDataByFullhousePlanIds(fullHousePlanIdList);
        log.info(CLASS_LOG_PREFIX + "完成增量更新店铺全屋方案元数据...");*/
        //更新数据
        log.info(CLASS_LOG_PREFIX + "开始更新数据，数据总条数:{}.", fullHousePlanIdList.size());
        int indexSuccessCount = fullHouseIndex.indexFullHousePlanData(fullHousePlanList, UserDefinedConstant.INCREMENT_UPDATE_TYPE);
        log.info(CLASS_LOG_PREFIX + "更新数据完成，成功{}条,失败{}条.", indexSuccessCount, fullHousePlanIdList.size() - indexSuccessCount);
    }

    /**
     * 更新店铺方案（删除、更新店铺平台操作）
     * @param companyShopPlanPos
     * @return
     */
    public boolean updatePlanShopInfo(List<CompanyShopPlanPo> companyShopPlanPos) {

        log.info(CLASS_LOG_PREFIX + "companyShopPlanPos:{}", JsonUtil.toJson(companyShopPlanPos));
        if (null == companyShopPlanPos || companyShopPlanPos.size() < 1) {
            log.info(CLASS_LOG_PREFIX + "companyShop:{}", JsonUtil.toJson(companyShopPlanPos));
            return true;
        }
        for (CompanyShopPlanPo companyShopPlanPo : companyShopPlanPos) {
            if (null == companyShopPlanPo.getShopId() && null == companyShopPlanPo.getShopId()) {
                log.info(CLASS_LOG_PREFIX + "companyShopPlanPo：{}", JsonUtil.toJson(companyShopPlanPo));
                continue;
            }
            List<CompanyShopPlanPo> planIdList = null;
            try {
                planIdList = metaDataService.queryPlanInfoByShopId(companyShopPlanPo.getShopId());
            } catch (MetaDataException e) {
                log.error(CLASS_LOG_PREFIX + "查询方案店铺失败！shopId:{},exception:{}", companyShopPlanPo.getShopId(), e);
                continue;
            }
            if (null != planIdList && planIdList.size() > 0) {
                List<Integer> fullHousePlanIds = new ArrayList<>(planIdList.size());
                List<Integer> recommendedPlanIds = new ArrayList<>(planIdList.size());
                planIdList.forEach(shopPlanInfo ->{
                    if (DesignPlanType.SHOP_PLAN_FULLHOUSE_TYPE == shopPlanInfo.getPlanRecommendedType()) {
                        fullHousePlanIds.add(shopPlanInfo.getPlanId());
                    } else {
                        recommendedPlanIds.add(shopPlanInfo.getPlanId());
                    }
                });
                if (fullHousePlanIds.size() > 0) {
                    sycnFullHousePlanData(fullHousePlanIds);
                }
                if (recommendedPlanIds.size() > 0) {
                    sycnRecommendationPlanData(recommendedPlanIds, "");
                }
            }
        }

        return true;
    }

    /**
     * 更新增量推荐方案数据
     *
     * @return
     */
    public boolean updateIncrRecommendedPlanInfo(List<RecommendationPlanIndexMappingData> planIndexMappingDataList) {

        if (null == planIndexMappingDataList || 0 == planIndexMappingDataList.size()) {
            return true;
        }

        //bulk操作列表
        List<Object> bulkUpdateList = new ArrayList<>();

        //构造更新对象
        for (RecommendationPlanIndexMappingData recommendedPlan : planIndexMappingDataList) {

            if (null == recommendedPlan || null == recommendedPlan.getId() || 0 == recommendedPlan.getId()) {
                continue;
            }

            //创建索引对象
            UpdateRequestDTO updateRequestDTO = new UpdateRequestDTO(
                    IndexConstant.RECOMMENDATION_PLAN_ALIASES,
                    TypeConstant.RECOMMENDATION_PLAN_TYPE,
                    recommendedPlan.getId() + "",
                    JsonUtil.toJsonExcludeEmpty(recommendedPlan)
            );

            //加入待更新列表
            bulkUpdateList.add(updateRequestDTO);
        }

        //提交数据
        BulkStatistics bulkStatistics = null;
        try {
            bulkStatistics = elasticSearchService.bulk(bulkUpdateList, null);
        } catch (ElasticSearchException e) {
            log.error(CLASS_LOG_PREFIX + "索引推荐方案数据异常:ElasticSearchException:{}", e);
        }
        log.info(CLASS_LOG_PREFIX + "索引推荐方案数据成功:成功索引数:{},BulkStatistics:{}", new String[]{
                bulkUpdateList.size() + "",
                null == bulkStatistics ? null : bulkStatistics.toString()
        });

        return true;
    }

}
