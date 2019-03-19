package com.sandu.search.initialize;

import com.sandu.search.common.constant.DesignPlanType;
import com.sandu.search.common.constant.IndexInfoQueryConfig;
import com.sandu.search.common.constant.SystemDictionaryType;
import com.sandu.search.common.constant.UserDefinedConstant;
import com.sandu.search.common.tools.EntityCopyUtils;
import com.sandu.search.common.tools.JsonUtil;
import com.sandu.search.common.tools.StringUtil;
import com.sandu.search.config.ElasticSearchConfig;
import com.sandu.search.datasync.task.RecommendationTask;
import com.sandu.search.entity.designplan.po.RecommendationPlanPo;
import com.sandu.search.entity.elasticsearch.constant.TypeConstant;
import com.sandu.search.entity.elasticsearch.dto.IndexRequestDTO;
import com.sandu.search.entity.elasticsearch.index.FullHouseDesignPlanIndexMappingData;
import com.sandu.search.entity.elasticsearch.index.RecommendationPlanIndexMappingData;
import com.sandu.search.entity.elasticsearch.po.CompanyShopInfoPo;
import com.sandu.search.entity.elasticsearch.po.metadate.*;
import com.sandu.search.entity.elasticsearch.response.BulkStatistics;
import com.sandu.search.entity.fullhouse.FullHouseDesignPlanPo;
import com.sandu.search.exception.ElasticSearchException;
import com.sandu.search.exception.FullHouseDesignPlanException;
import com.sandu.search.service.elasticsearch.ElasticSearchService;
import com.sandu.search.service.index.FullHouseDesignPlanIndexService;
import com.sandu.search.storage.company.BrandMetaDataStorage;
import com.sandu.search.storage.company.CompanyMetaDataStorage;
import com.sandu.search.storage.company.CompanyShopMetaDataStorage;
import com.sandu.search.storage.design.*;
import com.sandu.search.storage.resource.RenderPicMetaDataStorage;
import com.sandu.search.storage.resource.ResPicMetaDataStorage;
import com.sandu.search.storage.system.SysUserMetaDataStorage;
import com.sandu.search.storage.system.SystemDictionaryMetaDataStorage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * 全屋方案索引初始化
 *
 * @date 2018年9月12日 17:19
 * @auth zhangchengda
 */
@Slf4j
@Component
public class FullHouseIndex {
    private final static String CLASS_LOG_PREFIX = "初始化搜索引擎全屋方案索引:";

    private final ElasticSearchService elasticSearchService;
    private final BrandMetaDataStorage brandMetaDataStorage;
    private final RenderPicMetaDataStorage renderPicMetaDataStorage;
    private final CompanyMetaDataStorage companyMetaDataStorage;
    private final SysUserMetaDataStorage sysUserMetaDataStorage;
    private final DesignPlanPlatformMetaDataStorage designPlanPlatformMetaDataStorage;
    private final SystemDictionaryMetaDataStorage systemDictionaryMetaDataStorage;
    private final DesignPlanDecoratePriceMetaDataStorage designPlanDecoratePriceMetaDataStorage;
    private final CompanyShopMetaDataStorage companyShopMetaDataStorage;
    private final FullHouseDesignPlanIndexService fullHouseDesignPlanIndexService;
    private final ResPicMetaDataStorage resPicMetaDataStorage;
    private final ElasticSearchConfig elasticSearchConfig;
    private final RecommendationPlanIndex recommendationPlanIndex;
    private final CompanyShopPlanMetaDataStorage companyShopPlanMetaDataStorage;
    @Autowired
    public FullHouseIndex(BrandMetaDataStorage brandMetaDataStorage,
                          ElasticSearchService elasticSearchService,
                          RenderPicMetaDataStorage renderPicMetaDataStorage,
                          SystemDictionaryMetaDataStorage systemDictionaryMetaDataStorage,
                          CompanyMetaDataStorage companyMetaDataStorage,
                          DesignPlanPlatformMetaDataStorage designPlanPlatformMetaDataStorage,
                          SysUserMetaDataStorage sysUserMetaDataStorage,
                          DesignPlanDecoratePriceMetaDataStorage designPlanDecoratePriceMetaDataStorage,
                          CompanyShopMetaDataStorage companyShopMetaDataStorage,
                          FullHouseDesignPlanIndexService fullHouseDesignPlanIndexService,
                          ResPicMetaDataStorage resPicMetaDataStorage,
                          ElasticSearchConfig elasticSearchConfig,
                          RecommendationPlanIndex recommendationPlanIndex,
                          CompanyShopPlanMetaDataStorage companyShopPlanMetaDataStorage) {
        this.brandMetaDataStorage = brandMetaDataStorage;
        this.elasticSearchService = elasticSearchService;
        this.renderPicMetaDataStorage = renderPicMetaDataStorage;
        this.systemDictionaryMetaDataStorage = systemDictionaryMetaDataStorage;
        this.companyMetaDataStorage = companyMetaDataStorage;
        this.designPlanPlatformMetaDataStorage = designPlanPlatformMetaDataStorage;
        this.sysUserMetaDataStorage = sysUserMetaDataStorage;
        this.designPlanDecoratePriceMetaDataStorage = designPlanDecoratePriceMetaDataStorage;
        this.companyShopMetaDataStorage = companyShopMetaDataStorage;
        this.fullHouseDesignPlanIndexService = fullHouseDesignPlanIndexService;
        this.resPicMetaDataStorage = resPicMetaDataStorage;
        this.elasticSearchConfig = elasticSearchConfig;
        this.recommendationPlanIndex = recommendationPlanIndex;
        this.companyShopPlanMetaDataStorage =companyShopPlanMetaDataStorage;
    }

    /**
     * 同步全屋方案数据
     */
    public void syncFullHouseDesignPlanData() {

        log.info(CLASS_LOG_PREFIX + "开始索引全屋方案数据........");
        //开始时间
        long startTime = System.currentTimeMillis();
        //数据查询初始位
        int start = 0;
        //每次数据量
        int limit = IndexInfoQueryConfig.DEFAULT_QUERY_PRODUCTPOINFO_LIMIT;
        //是否继续处理
        boolean isContinueHandler = true;
        //总索引量
        int totalIndexCount = 0;
        //总数据量
        int totalRecommendedPlanCount = 0;
        //总数据量
        int totalExceptionCount = 0;

        while (isContinueHandler) {
            List<FullHouseDesignPlanPo> recommendationPlanList;
            /********************************** 查询全屋方案信息 *********************************/
            //更新全量数据
            try {
                log.info(CLASS_LOG_PREFIX + "第{}条开始查{}", start, limit);
                recommendationPlanList = fullHouseDesignPlanIndexService.queryFullHouseDesignPlanList(start, limit);
            } catch (FullHouseDesignPlanException e) {
                log.error(CLASS_LOG_PREFIX + "查询全屋方案信息失败:DesignPlanIndexException:{}", e);
                return;
            }

            //递增start下标
            start = start + limit;

            //无数据中断操作
            if (null == recommendationPlanList || 0 == recommendationPlanList.size()) {
                log.info(CLASS_LOG_PREFIX + "查询全屋方案信息数据为空：start:{},limit:{}.", start, limit);
                return;
            }
            //数据不足指定数据量表示已查询出最后一条数据,终止循环
            if (recommendationPlanList.size() < IndexInfoQueryConfig.DEFAULT_QUERY_PRODUCTPOINFO_LIMIT) {
                isContinueHandler = false;
            }
            //索引全屋方案数据
            int successIndexCount = indexFullHouseDesignPlanData(recommendationPlanList);

            totalIndexCount += successIndexCount;
            totalRecommendedPlanCount += recommendationPlanList.size();
            totalExceptionCount += recommendationPlanList.size() - successIndexCount;
        }

        log.info(CLASS_LOG_PREFIX + "索引所有全屋方案数据完成!!!方案数据量:{},索引数据量:{},失败数:{},共耗时:{}ms", new String[]{
                totalRecommendedPlanCount + "",
                totalIndexCount + "",
                totalExceptionCount + "",
                (System.currentTimeMillis() - startTime) + ""
        });

        RecommendationTask.isRuningFullTask = false;
    }

    /**
     * 索引全屋方案数据
     *
     * @param fullHouseDesignPlanList
     * @return
     */
    public int indexFullHouseDesignPlanData(List<FullHouseDesignPlanPo> fullHouseDesignPlanList) {

        log.info(CLASS_LOG_PREFIX + "开始解析全屋方案数据...");
        if (null == fullHouseDesignPlanList || 0 >= fullHouseDesignPlanList.size()) {
            return 0;
        }

        //批量提交数据对象
        List<Object> bulkIndexList = new ArrayList<>(IndexInfoQueryConfig.DEFAULT_QUERY_PRODUCTPOINFO_LIMIT);

        /********************************** 处理产品分类信息数据 *********************************/
        for (int i = 0; i < fullHouseDesignPlanList.size(); i++) {
            FullHouseDesignPlanPo fullHouseDesignPlanPo = fullHouseDesignPlanList.get(i);
            //定义索引数据对象
            FullHouseDesignPlanIndexMappingData fullHouseDesignPlanIndexMappingData = new FullHouseDesignPlanIndexMappingData();

            //复制基本对象
            EntityCopyUtils.copyData(fullHouseDesignPlanPo, fullHouseDesignPlanIndexMappingData);

            //封面图片
            Integer coverPicId = fullHouseDesignPlanPo.getPlanPicId();
            if (null != coverPicId && 0 < coverPicId) {
                fullHouseDesignPlanIndexMappingData.setPlanPicPath(renderPicMetaDataStorage.getPicPathByPicId(coverPicId));
            }

            //品牌ID
            String[] brandIds = fullHouseDesignPlanPo.getBrandIds().split(",");
            List<Integer> brandIdList = new ArrayList<>(brandIds.length);
            List<String> brandNameList = new ArrayList<>(brandIds.length);
            for (String brandId : brandIds) {
                brandIdList.add(Integer.parseInt(brandId));
                brandNameList.add(brandMetaDataStorage.getBrandNameById(Integer.parseInt(brandId)));
            }
            fullHouseDesignPlanIndexMappingData.setBrandIdList(brandIdList);
            fullHouseDesignPlanIndexMappingData.setBrandNameList(brandNameList);

            // 用户
            int userId = fullHouseDesignPlanPo.getUserId();
            SysUserPo userPo = sysUserMetaDataStorage.getUserPoByUserId(userId);
            if (userPo != null){
                fullHouseDesignPlanIndexMappingData.setUserName(userPo.getUserName());
            }

            //公司
            fullHouseDesignPlanIndexMappingData.setCompanyName(
                    companyMetaDataStorage.getCompanyNameByCompanyId(fullHouseDesignPlanPo.getCompanyId()));

            //设计师店铺
            List<CompanyShopPo> shopList = companyShopMetaDataStorage.getCompanyShopPoByUserId(fullHouseDesignPlanPo.getUserId());
            if (shopList != null && shopList.size() > 0){
                List<CompanyShopInfoPo> shops = new ArrayList<>(shopList.size());
                shopList.forEach(companyShopPo -> {
                    CompanyShopInfoPo infoPo = new CompanyShopInfoPo();
                    infoPo.setShopId(companyShopPo.getId());
                    infoPo.setShopLogoPicPath(resPicMetaDataStorage.getPicPathByPicId(companyShopPo.getLogoPicId()));
                    shops.add(infoPo);
                });
                fullHouseDesignPlanIndexMappingData.setUserShopList(shops);
            }

            //装修报价
            /*List<DecoratePricePo> decorateList = designPlanDecoratePriceMetaDataStorage.getDecoratePricePoByFullHouseId(fullHouseDesignPlanPo.getId());
            if (decorateList != null && decorateList.size() > 0){
                List<DecoratePriceInfoPo> decorateInfos = new ArrayList<>(decorateList.size());
                decorateList.forEach(decoratePricePo -> {
                    DecoratePriceInfoPo info = new DecoratePriceInfoPo();
                    SystemDictionaryPo sysDictionary = systemDictionaryMetaDataStorage.getSystemDictionaryByTypeAndValue(
                            SystemDictionaryType.SYSTEM_DICTIONARY_TYPE_DECORATETYPE, decoratePricePo.getDecoratePriceType());
                    info.setDecoratePriceId(decoratePricePo.getId());
                    info.setDecoratePriceRangeValue(decoratePricePo.getDecoratePriceRange());
                    info.setDecoratePriceRangeName(systemDictionaryMetaDataStorage.getSystemDictionaryNameByTypeAndValue(
                            sysDictionary.getDictionaryType(), decoratePricePo.getDecoratePriceRange()));
                    info.setDecoratePriceTypeValue(decoratePricePo.getDecoratePriceType());
                    info.setDecoratePriceTypeName(sysDictionary.getDictionaryName());
                    info.setDecoratePrice(decoratePricePo.getDecoratePrice());
                    decorateInfos.add(info);
                });
                fullHouseDesignPlanIndexMappingData.setDecoratePriceInfoList(decorateInfos);
            }*/

            //创建索引对象
            IndexRequestDTO indexRequestDTO = new IndexRequestDTO(
                    elasticSearchConfig.getRecommendedPlanDataIndexName(),
                    TypeConstant.FULL_HOUSE_DESIGN_PLAN_TYPE,
                    fullHouseDesignPlanIndexMappingData.getId() + "",
                    JsonUtil.toJson(fullHouseDesignPlanIndexMappingData)
            );

            //加入批量对象
            bulkIndexList.add(indexRequestDTO);

            //Count
            if (0 == i % 100) {
                log.info(CLASS_LOG_PREFIX + "已解析{}条全屋方案数据,剩余{}条", i, fullHouseDesignPlanList.size() - i);
            }
        }

        log.info(CLASS_LOG_PREFIX + "解析全屋方案数据完成，开始索引数据...");
        //索引数据
        BulkStatistics bulkStatistics = null;
        try {
            bulkStatistics = elasticSearchService.bulk(bulkIndexList, null);
        } catch (ElasticSearchException e) {
            log.error(CLASS_LOG_PREFIX + "索引全屋方案数据异常:ElasticSearchException:{}", e);
        }
        log.info(CLASS_LOG_PREFIX + "索引全屋方案数据成功:成功索引数:{},BulkStatistics:{}", new String[]{
                bulkIndexList.size() + "",
                null == bulkStatistics ? null : bulkStatistics.toString()
        });

        return bulkIndexList.size();
    }


    /**
     * 同步全屋方案数据
     */
    public void syncFullHousePlanData() {

        log.info(CLASS_LOG_PREFIX + "开始索引全屋方案数据........");
        //开始时间
        long startTime = System.currentTimeMillis();
        //数据查询初始位
        int start = 0;
        //每次数据量
        int limit = IndexInfoQueryConfig.DEFAULT_QUERY_PRODUCTPOINFO_LIMIT;
        //是否继续处理
        boolean isContinueHandler = true;
        //总索引量
        int totalIndexCount = 0;
        //总数据量
        int totalRecommendedPlanCount = 0;
        //总数据量
        int totalExceptionCount = 0;

        while (isContinueHandler) {
            List<RecommendationPlanPo> fullHousePlanList;
            /********************************** 查询全屋方案信息 *********************************/
            //更新全量数据
            try {
                log.info(CLASS_LOG_PREFIX + "第{}条开始查{}", start, limit);
                fullHousePlanList = fullHouseDesignPlanIndexService.queryFullHousePlanList(start, limit);
            } catch (FullHouseDesignPlanException e) {
                log.error(CLASS_LOG_PREFIX + "查询全屋方案信息失败:FullHouseDesignPlanException:{}", e);
                return;
            }

            //递增start下标
            start = start + limit;

            //无数据中断操作
            if (null == fullHousePlanList || 0 == fullHousePlanList.size()) {
                log.info(CLASS_LOG_PREFIX + "查询全屋方案信息数据为空：start:{},limit:{}.", start, limit);
                return;
            }
            //数据不足指定数据量表示已查询出最后一条数据,终止循环
            if (fullHousePlanList.size() < IndexInfoQueryConfig.DEFAULT_QUERY_PRODUCTPOINFO_LIMIT) {
                isContinueHandler = false;
            }
            //索引全屋方案数据
            int successIndexCount = indexFullHousePlanData(fullHousePlanList, UserDefinedConstant.FULL_UPDATE_TYPE);

            totalIndexCount += successIndexCount;
            totalRecommendedPlanCount += fullHousePlanList.size();
            totalExceptionCount += fullHousePlanList.size() - successIndexCount;
        }

        log.info(CLASS_LOG_PREFIX + "索引所有全屋方案数据完成!!!方案数据量:{},索引数据量:{},失败数:{},共耗时:{}ms", new String[]{
                totalRecommendedPlanCount + "",
                totalIndexCount + "",
                totalExceptionCount + "",
                (System.currentTimeMillis() - startTime) + ""
        });
    }

    /**
     * 索引全屋方案数据
     *
     * @param recommendationPlanList 全屋方案对象
     * @return 索引成功数
     */
    public int indexFullHousePlanData(List<RecommendationPlanPo> recommendationPlanList, String type) {

        log.info(CLASS_LOG_PREFIX + "开始解析全屋方案数据...");
        if (null == recommendationPlanList || 0 >= recommendationPlanList.size()) {
            return 0;
        }

        //批量提交数据对象
        List<Object> bulkIndexList = new ArrayList<>(IndexInfoQueryConfig.DEFAULT_QUERY_PRODUCTPOINFO_LIMIT);

        /********************************** 处理产品分类信息数据 *********************************/
        for (int i = 0; i < recommendationPlanList.size(); i++) {
            RecommendationPlanPo recommendationPlanPo = recommendationPlanList.get(i);
            //定义索引数据对象
            RecommendationPlanIndexMappingData recommendationPlanIndexMappingData = new RecommendationPlanIndexMappingData();

            recommendationPlanIndexMappingData.setDataIsDeleted(recommendationPlanPo.getDataIsDeleted());
            //复制基本对象
            EntityCopyUtils.copyData(recommendationPlanPo, recommendationPlanIndexMappingData);
            //方案版本费转换INT
            Double planPrice = recommendationPlanPo.getPlanPrice();
            if (null != planPrice && planPrice > 0) {
                if (planPrice < 1) {
                    recommendationPlanIndexMappingData.setPlanPrice(1);
                } else {
                    recommendationPlanIndexMappingData.setPlanPrice(Integer.valueOf(new java.text.DecimalFormat("0").format(planPrice)));
                }
            }
            //全屋方案来源转换
            Integer sourceType = recommendationPlanPo.getSourceType();
            switch (sourceType) {
                case DesignPlanType.DIY_FULLHOUSEPLAN_SOURCE:
                    recommendationPlanIndexMappingData.setPlanSource(RecommendationPlanPo.DIY_PLAN_SOURCE);
                    break;
                case DesignPlanType.DELIVER_FULLHOUSEPLAN_SOURCE:
                    recommendationPlanIndexMappingData.setPlanSource(RecommendationPlanPo.DELIVER_PLAN_SOURCE);
                    break;
                case DesignPlanType.SHARE_FULLHOUSEPLAN_SOURCE:
                    recommendationPlanIndexMappingData.setPlanSource(RecommendationPlanPo.SHARE_PLAN_SOURCE);
                    break;
                default:
                    break;
            }

            //自定义方案来源表类型
            recommendationPlanIndexMappingData.setPlanTableType(DesignPlanType.FULLHOUSE_TABLE_TYPE);

            //渲染720图片
            /*Integer recommendationPlanId = recommendationPlanPo.getId();
            if (null != recommendationPlanId && 0 < recommendationPlanId) {
                recommendationPlanIndexMappingData.setResRenderPicPath(renderPicMetaDataStorage.getPicPathByPlanId(recommendationPlanId));
            }*/

            //品牌信息
            String brandIds = recommendationPlanPo.getBrandIds();
            if (!StringUtils.isEmpty(brandIds)) {
                List<Integer> brandIdList = StringUtil.transformInteger(Arrays.asList(brandIds.split(",")));
                //品牌ID
                recommendationPlanIndexMappingData.setBrandIdList(brandIdList);
                //品牌名称
                List<String> brandNameList = new ArrayList<>(brandIdList.size());
                for (Integer brandId : brandIdList) {
                    String brandName = brandMetaDataStorage.getBrandNameById(brandId);
                    if (!StringUtils.isEmpty(brandName)) {
                        brandNameList.add(brandName);
                    }
                }
                recommendationPlanIndexMappingData.setBrandNameList(brandNameList);
            }

            //公司信息
            Integer companyId = recommendationPlanPo.getCompanyId();
            if (null != companyId && 0 < companyId.intValue()) {
                recommendationPlanIndexMappingData.setCompanyIdList(Arrays.asList(companyId));
                recommendationPlanIndexMappingData.setCompanyNameList(Arrays.asList(companyMetaDataStorage.getCompanyNameByCompanyId(companyId)));
            }

            //全屋类型
            SystemDictionaryPo dictionaryPo = systemDictionaryMetaDataStorage.getSystemDictionaryByTypeAndKey(SystemDictionaryType.SYSTEM_DICTIONARY_TYPE_HOUSETYPE, SystemDictionaryType.SYSTEM_DICTIONARY_HOUSETYPE_KEY_FULLROOM);
            if (null != dictionaryPo) {
                recommendationPlanIndexMappingData.setSpaceFunctionId(dictionaryPo.getDictionaryValue());
                recommendationPlanIndexMappingData.setHouseTypeName(dictionaryPo.getDictionaryName());
            }
            //全量更新
            if (UserDefinedConstant.FULL_UPDATE_TYPE.equals(type)) {
                //创建人
                Integer createUserId = recommendationPlanPo.getCreateUserId();
                if (null != createUserId && 0 < createUserId) {
                    SysUserPo userPo = sysUserMetaDataStorage.getUserPoByUserId(createUserId);
                    if (null != userPo) {
                        recommendationPlanIndexMappingData.setCreateUserName(userPo.getUserName());
                    }
                }

                //全屋平台数据
                recommendationPlanIndex.getRecommendedPlanPlatformInfoById(recommendationPlanIndexMappingData, UserDefinedConstant.FULL_UPDATE_TYPE);

                //全屋装修报价
                recommendationPlanIndex.getDecoratePriceById(recommendationPlanIndexMappingData, DesignPlanType.FULLHOUSE_TABLE_TYPE);

                //方案关联的店铺
                List<CompanyShopPlanPo> shopPlanPoList = companyShopPlanMetaDataStorage.getCompanyShopPlanPoById(recommendationPlanPo.getFullHouseId(), DesignPlanType.FULLHOUSE_TABLE_TYPE);
                if (null != shopPlanPoList && shopPlanPoList.size() > 0) {
                    List<CompanyShopPlanPo> shopPlanList = new ArrayList<>(shopPlanPoList.size());
                    for (CompanyShopPlanPo shopPlanPo : shopPlanPoList) {
                        //过滤删除的店铺
                        if (UserDefinedConstant.DATA_DELETED_NO == shopPlanPo.getDataIsDeleted()) {
                            shopPlanList.add(shopPlanPo);
                        }
                    }
                    recommendationPlanIndexMappingData.setPlanShopInfoList(shopPlanList);
                }
            } else if (UserDefinedConstant.INCREMENT_UPDATE_TYPE.equals(type)) {  //增量更新
                //全屋平台数据
                recommendationPlanIndex.getRecommendedPlanPlatformInfoById(recommendationPlanIndexMappingData, type);

                //全屋装修报价
                recommendationPlanIndex.selectDecoratePriceById(recommendationPlanIndexMappingData, DesignPlanType.FULLHOUSE_TABLE_TYPE);

                //店铺信息
                List<CompanyShopPlanPo> shopPlanPoList = companyShopPlanMetaDataStorage.selectCompanyShopPoByPlanId(recommendationPlanIndexMappingData.getFullHouseId());
                if (null != shopPlanPoList && 0 < shopPlanPoList.size()) {
                    recommendationPlanIndexMappingData.setPlanShopInfoList(shopPlanPoList);
                }
            }
            //创建索引对象 //全屋用uuid
            IndexRequestDTO indexRequestDTO = new IndexRequestDTO(
                    elasticSearchConfig.getRecommendedPlanDataIndexName(),
                    TypeConstant.RECOMMENDATION_PLAN_TYPE,
                    recommendationPlanIndexMappingData.getUuid(),
                    JsonUtil.toJson(recommendationPlanIndexMappingData)
            );

            //加入批量对象
            bulkIndexList.add(indexRequestDTO);

            //Count
            if (0 == i % 100) {
                log.info(CLASS_LOG_PREFIX + "已解析{}条全屋方案数据,剩余{}条", i, recommendationPlanList.size() - i);
            }
        }

        log.info(CLASS_LOG_PREFIX + "解析全屋方案数据完成，开始索引数据...");
        //索引数据
        BulkStatistics bulkStatistics = null;
        try {
            bulkStatistics = elasticSearchService.bulk(bulkIndexList, null);
        } catch (ElasticSearchException e) {
            log.error(CLASS_LOG_PREFIX + "索引全屋方案数据异常:ElasticSearchException:{}", e);
        }
        log.info(CLASS_LOG_PREFIX + "索引全屋方案数据成功:成功索引数:{},BulkStatistics:{}", new String[]{
                bulkIndexList.size() + "",
                null == bulkStatistics ? null : bulkStatistics.toString()
        });

        return bulkIndexList.size();
    }
}
