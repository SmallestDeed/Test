package com.sandu.search.initialize;

import com.sandu.search.common.constant.*;
import com.sandu.search.common.tools.EntityCopyUtils;
import com.sandu.search.common.tools.JsonUtil;
import com.sandu.search.common.tools.StringUtil;
import com.sandu.search.config.ElasticSearchConfig;
import com.sandu.search.datasync.task.RecommendationTask;
import com.sandu.search.entity.designplan.po.RecommendationPlanPo;
import com.sandu.search.entity.elasticsearch.constant.TypeConstant;
import com.sandu.search.entity.elasticsearch.dto.IndexRequestDTO;
import com.sandu.search.entity.elasticsearch.index.RecommendationPlanIndexMappingData;
import com.sandu.search.entity.elasticsearch.po.design.DecoratePriceData;
import com.sandu.search.entity.elasticsearch.po.design.DesignPlanPlatformData;
import com.sandu.search.entity.elasticsearch.po.metadate.CompanyShopPlanPo;
import com.sandu.search.entity.elasticsearch.po.metadate.DesignPlanRecommendedPo;
import com.sandu.search.entity.elasticsearch.po.metadate.SpaceCommonPo;
import com.sandu.search.entity.elasticsearch.po.metadate.SysUserPo;
import com.sandu.search.entity.elasticsearch.response.BulkStatistics;
import com.sandu.search.exception.DesignPlanIndexException;
import com.sandu.search.exception.ElasticSearchException;
import com.sandu.search.service.elasticsearch.ElasticSearchService;
import com.sandu.search.service.index.DesignPlanIndexService;
import com.sandu.search.storage.company.BrandMetaDataStorage;
import com.sandu.search.storage.company.CompanyMetaDataStorage;
import com.sandu.search.storage.design.*;
import com.sandu.search.storage.product.ProductStyleMetaDataStorage;
import com.sandu.search.storage.resource.RenderPicMetaDataStorage;
import com.sandu.search.storage.resource.ResPicMetaDataStorage;
import com.sandu.search.storage.space.SpaceCommonMetaDataStorage;
import com.sandu.search.storage.system.SysUserMetaDataStorage;
import com.sandu.search.storage.system.SystemDictionaryMetaDataStorage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * 搜索引擎推荐方案索引初始化
 *
 * @date 20180531
 * @auth pengxuangang
 */
@Slf4j
@Component
public class RecommendationPlanIndex {

    private final static String CLASS_LOG_PREFIX = "搜索引擎推荐方案索引初始化:";

    private final BrandMetaDataStorage brandMetaDataStorage;
    private final ElasticSearchConfig elasticSearchConfig;
    private final ElasticSearchService elasticSearchService;
    private final DesignPlanIndexService designPlanIndexService;
    private final RenderPicMetaDataStorage renderPicMetaDataStorage;
    private final DesignPlanBrandMetaDataStorage designPlanBrandMetaDataStorage;
    private final SpaceCommonMetaDataStorage spaceCommonMetaDataStorage;
    private final ProductStyleMetaDataStorage productStyleMetaDataStorage;
    private final SystemDictionaryMetaDataStorage systemDictionaryMetaDataStorage;
    private final CompanyMetaDataStorage companyMetaDataStorage;
    private final DesignPlanPlatformMetaDataStorage designPlanPlatformMetaDataStorage;
    private final SysUserMetaDataStorage sysUserMetaDataStorage;
    private final RecommendedPlanLivingMetaDataStorage recommendedPlanLivingMetaDataStorage;
    private final DesignPlanRecommendationMetaDataStorage designPlanRecommendationMetaDataStorage;
    private final DesignPlanDecoratePriceMetaDataStorage designPlanDecoratePriceMetaDataStorage;
    private final ResPicMetaDataStorage resPicMetaDataStorage;
    private final CompanyShopPlanMetaDataStorage companyShopPlanMetaDataStorage;
    private final RecommendedPlanProductMetaDataStorage recommendedPlanProductMetaDataStorage;

    @Autowired
    public RecommendationPlanIndex(RecommendedPlanProductMetaDataStorage recommendedPlanProductMetaDataStorage, CompanyShopPlanMetaDataStorage companyShopPlanMetaDataStorage, ResPicMetaDataStorage resPicMetaDataStorage, DesignPlanDecoratePriceMetaDataStorage designPlanDecoratePriceMetaDataStorage, DesignPlanRecommendationMetaDataStorage designPlanRecommendationMetaDataStorage, BrandMetaDataStorage brandMetaDataStorage, ElasticSearchConfig elasticSearchConfig, ElasticSearchService elasticSearchService, DesignPlanIndexService designPlanIndexService, RenderPicMetaDataStorage renderPicMetaDataStorage, DesignPlanBrandMetaDataStorage designPlanBrandMetaDataStorage, SpaceCommonMetaDataStorage spaceCommonMetaDataStorage, ProductStyleMetaDataStorage productStyleMetaDataStorage, SystemDictionaryMetaDataStorage systemDictionaryMetaDataStorage, CompanyMetaDataStorage companyMetaDataStorage, DesignPlanPlatformMetaDataStorage designPlanPlatformMetaDataStorage, SysUserMetaDataStorage sysUserMetaDataStorage, RecommendedPlanLivingMetaDataStorage recommendedPlanLivingMetaDataStorage) {
        this.brandMetaDataStorage = brandMetaDataStorage;
        this.elasticSearchConfig = elasticSearchConfig;
        this.elasticSearchService = elasticSearchService;
        this.designPlanIndexService = designPlanIndexService;
        this.renderPicMetaDataStorage = renderPicMetaDataStorage;
        this.designPlanBrandMetaDataStorage = designPlanBrandMetaDataStorage;
        this.spaceCommonMetaDataStorage = spaceCommonMetaDataStorage;
        this.productStyleMetaDataStorage = productStyleMetaDataStorage;
        this.systemDictionaryMetaDataStorage = systemDictionaryMetaDataStorage;
        this.companyMetaDataStorage = companyMetaDataStorage;
        this.designPlanPlatformMetaDataStorage = designPlanPlatformMetaDataStorage;
        this.sysUserMetaDataStorage = sysUserMetaDataStorage;
        this.recommendedPlanLivingMetaDataStorage = recommendedPlanLivingMetaDataStorage;
        this.designPlanRecommendationMetaDataStorage = designPlanRecommendationMetaDataStorage;
        this.designPlanDecoratePriceMetaDataStorage = designPlanDecoratePriceMetaDataStorage;
        this.resPicMetaDataStorage = resPicMetaDataStorage;
        this.companyShopPlanMetaDataStorage = companyShopPlanMetaDataStorage;
        this.recommendedPlanProductMetaDataStorage = recommendedPlanProductMetaDataStorage;
    }

    /**
     * 同步推荐方案数据
     */
    public void syncRecommendationPlanData() {

        log.info(CLASS_LOG_PREFIX + "开始索引推荐方案数据........");
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
            List<RecommendationPlanPo> recommendationPlanList;
            /********************************** 查询推荐方案信息 *********************************/
            //更新全量数据
            try {
                log.info(CLASS_LOG_PREFIX + "第{}条开始查{}", start, limit);
                recommendationPlanList = designPlanIndexService.queryRecommendationPlanDataList(null, start, limit);
            } catch (DesignPlanIndexException e) {
                log.error(CLASS_LOG_PREFIX + "查询推荐方案信息失败:DesignPlanIndexException:{}", e);
                return;
            }

            //递增start下标
            start = start + limit;

            //无数据中断操作
            if (null == recommendationPlanList || 0 == recommendationPlanList.size()) {
                log.info(CLASS_LOG_PREFIX + "查询推荐方案信息数据为空：start:{},limit:{}.", start, limit);
                return;
            }
            //数据不足指定数据量表示已查询出最后一条数据,终止循环
            if (recommendationPlanList.size() < IndexInfoQueryConfig.DEFAULT_QUERY_PRODUCTPOINFO_LIMIT) {
                isContinueHandler = false;
            }
            //索引推荐方案数据
            int successIndexCount = indexRecommendationPlanData(recommendationPlanList, UserDefinedConstant.FULL_UPDATE_TYPE);

            totalIndexCount += successIndexCount;
            totalRecommendedPlanCount += recommendationPlanList.size();
            totalExceptionCount += recommendationPlanList.size() - successIndexCount;
        }

        log.info(CLASS_LOG_PREFIX + "索引所有推荐方案数据完成!!!方案数据量:{},索引数据量:{},失败数:{},共耗时:{}ms", new String[]{
                totalRecommendedPlanCount + "",
                totalIndexCount + "",
                totalExceptionCount + "",
                (System.currentTimeMillis() - startTime) + ""
        });

        RecommendationTask.isRuningFullTask = false;
    }


    /**
     * 索引推荐方案数据
     *
     * @param recommendationPlanList 推荐方案对象
     * @return 索引成功数
     */
    public int indexRecommendationPlanData(List<RecommendationPlanPo> recommendationPlanList) {

        log.info(CLASS_LOG_PREFIX + "开始解析推荐方案数据...");
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

            //自定义方案来源表类型
            recommendationPlanIndexMappingData.setPlanTableType(DesignPlanType.RECOMMENDED_TABLE_TYPE);

            //封面图片
            Integer planId = recommendationPlanPo.getId();
            if (null != planId && 0 < planId) {
                recommendationPlanIndexMappingData.setCoverPicPath(renderPicMetaDataStorage.getPicPathByPicId(planId));
            }

            //渲染720图片
            Integer recommendationPlanId = recommendationPlanPo.getId();
            if (null != recommendationPlanId && 0 < recommendationPlanId) {
                recommendationPlanIndexMappingData.setResRenderPicPath(renderPicMetaDataStorage.getPicPathByPlanId(recommendationPlanId));
            }

            //品牌ID
            String brandIdStr = designPlanBrandMetaDataStorage.getBrandIdByDesignPlanId(recommendationPlanPo.getId());
            if (!StringUtils.isEmpty(brandIdStr) && !"null".equals(brandIdStr) && !"-1".equals(brandIdStr)) {
                List<Integer> brandIdList = StringUtil.transformInteger(Arrays.asList(brandIdStr.split(",")));
                //ID
                recommendationPlanIndexMappingData.setBrandIdList(brandIdList);
                //名
                List<String> brandNameList = new ArrayList<>(brandIdList.size());
                for (Integer brandId : brandIdList) {
                    brandNameList.add(brandMetaDataStorage.getBrandNameById(brandId));
                }
                recommendationPlanIndexMappingData.setBrandNameList(brandNameList);
            }

            //公司ID 和 发布关联品牌公司ID
            Integer companyId = recommendationPlanPo.getCompanyId();
            String companyIdStr = designPlanBrandMetaDataStorage.getCompanyIdByRecommendationPlanId(recommendationPlanPo.getId());
            if (!StringUtils.isEmpty(companyIdStr) && !"null".equals(companyIdStr) && !"-1".equals(companyIdStr)) {
                List<String> companyIds = Arrays.asList(companyIdStr.split(","));
                //去重加入
                Set companyIdSet = new HashSet(companyIds);
                if (null != companyId && 0 < companyId.intValue()) {
                    if (companyIdSet != null && 0 < companyIdSet.size()) {
                        companyIdSet.add(companyId+"");
                    } else {
                        companyIdSet = new HashSet();
                        companyIdSet.add(companyId+"");
                    }
                }
                //转换list
                List<Integer> companyIdList = StringUtil.transformInteger(new ArrayList<>(companyIdSet));
                //ID
                recommendationPlanIndexMappingData.setCompanyIdList(companyIdList);
                //名
                List<String> companyNameList = new ArrayList<>(companyIdList.size());
                for (Integer id : companyIdList) {
                    companyNameList.add(companyMetaDataStorage.getCompanyNameByCompanyId(id));
                }
                recommendationPlanIndexMappingData.setCompanyNameList(companyNameList);
            } else {
                if (null != companyId && 0 < companyId.intValue()) {
                    recommendationPlanIndexMappingData.setCompanyIdList(Arrays.asList(companyId));
                    recommendationPlanIndexMappingData.setCompanyNameList(Arrays.asList(companyMetaDataStorage.getCompanyNameByCompanyId(companyId)));
                }
            }

            //空间数据
            Integer spaceCommonId = recommendationPlanIndexMappingData.getSpaceCommonId();
            if (null != spaceCommonId && 0 < spaceCommonId) {
                //获取空间数据
                SpaceCommonPo spaceCommon = spaceCommonMetaDataStorage.getSpaceCommonBySpaceCommonId(spaceCommonId);
                if (null != spaceCommon) {
                    //类型-面积-形状
                    recommendationPlanIndexMappingData.setSpaceFunctionId(spaceCommon.getSpaceFunctionId());
                    recommendationPlanIndexMappingData.setSpaceAreas(spaceCommon.getSpaceAreas());
                    recommendationPlanIndexMappingData.setSpaceShape(spaceCommon.getSpaceShape());
                    recommendationPlanIndexMappingData.setSpaceCode(spaceCommon.getSpaceCode());
                }

                //小区名称
                String livingNames = recommendedPlanLivingMetaDataStorage.getLivingNameBySpaceId(spaceCommonId);
                if (!StringUtils.isEmpty(livingNames)) {
                    recommendationPlanIndexMappingData.setLivingNameList(Arrays.asList(livingNames.split(",")));
                }
            }

            //风格名
            Integer designStyleId = recommendationPlanIndexMappingData.getDesignStyleId();
            if (null != designStyleId && 0 < designStyleId) {
                String styleName = productStyleMetaDataStorage.getProductStyleNameById(designStyleId);
                if (!StringUtils.isEmpty(styleName)) {
                    recommendationPlanIndexMappingData.setStyleName(styleName);
                }
            }

            //房间类型
            Integer spaceFunctionId = recommendationPlanIndexMappingData.getSpaceFunctionId();
            if (null != spaceFunctionId && 0 < spaceFunctionId) {
                String houseTypeName = systemDictionaryMetaDataStorage.getSystemDictionaryNameByTypeAndValue(SystemDictionaryType.SYSTEM_DICTIONARY_TYPE_HOUSETYPE, spaceFunctionId);
                if (!StringUtils.isEmpty(houseTypeName)) {
                    recommendationPlanIndexMappingData.setHouseTypeName(houseTypeName);
                }
            }

            //商家后台上下架状态 ONEKEY 一键方案 OPEN 公开方案 多个用,隔开 ONEKEY,OPEN
            String shelfStatus = recommendationPlanPo.getShelfStatus();
            if (!StringUtils.isEmpty(shelfStatus)) {
                recommendationPlanIndexMappingData.setShelfStatusList(Arrays.asList(shelfStatus.split(",")));
            }

            //适用空间面积
            String applySpaceAreaStr = recommendationPlanPo.getApplySpaceAreas();
            if (!StringUtils.isEmpty(applySpaceAreaStr)) {
                //recommendationPlanIndexMappingData.setApplySpaceAreaList(Arrays.asList(applySpaceAreaStr.split(",")));
                //打组主方案存储子方案适用面积
                Integer groupPrimaryId = recommendationPlanPo.getGroupPrimaryId();
                Integer recommendedPlanId = recommendationPlanIndexMappingData.getId();
                if (null != groupPrimaryId && null != recommendedPlanId && groupPrimaryId.equals(recommendedPlanId)) {
                    String groupRecommendedPlanAreas = designPlanRecommendationMetaDataStorage.getGroupRecommendedAreasById(groupPrimaryId);
                    if (!StringUtils.isEmpty(groupRecommendedPlanAreas)) {
                        recommendationPlanIndexMappingData.setApplyAllSpaceAreaList(Arrays.asList(groupRecommendedPlanAreas.split(",")));
                    } else {
                        recommendationPlanIndexMappingData.setApplyAllSpaceAreaList(Arrays.asList(applySpaceAreaStr.split(",")));
                    }
                } else {
                    recommendationPlanIndexMappingData.setApplyAllSpaceAreaList(Arrays.asList(applySpaceAreaStr.split(",")));
                }
            }

            //数据状态
            Integer dataIsDeleted = recommendationPlanPo.getDataIsDeleted();
            if (null != dataIsDeleted) {
                recommendationPlanIndexMappingData.setDataIsDeleted(dataIsDeleted);
            }

            //创建人、用户头像(方案列表展示设计师头像)
            Integer createUserId = recommendationPlanPo.getCreateUserId();
            if (null != createUserId && 0 < createUserId) {
                SysUserPo userPo = sysUserMetaDataStorage.getUserPoByUserId(createUserId);
                if (null != userPo) {
                    recommendationPlanIndexMappingData.setCreateUserName(userPo.getUserName());
                    recommendationPlanIndexMappingData.setUserPicPath(userPo.getPicPath());
                }
            }

            //方案全平台数据
            getRecommendedPlanPlatformInfoById(recommendationPlanIndexMappingData, UserDefinedConstant.FULL_UPDATE_TYPE);

            //全屋装修报价
            getDecoratePriceById(recommendationPlanIndexMappingData, DesignPlanType.RECOMMENDED_TABLE_TYPE);

            //方案关联的店铺
            /*String shopIds = companyShopPlanMetaDataStorage.getCompanyShopIdById(recommendationPlanId, DesignPlanType.RECOMMENDED_TABLE_TYPE);
            if (!StringUtils.isEmpty(shopIds)) {
                List<Integer> shopIdList = StringUtil.transformInteger(new ArrayList<>(Arrays.asList(shopIds.split(","))));
                recommendationPlanIndexMappingData.setShopIdList(shopIdList);
            }*/

            //推荐方案产品
            String productIds = recommendedPlanProductMetaDataStorage.getProductIdsByRecommendationPlanId(recommendationPlanIndexMappingData.getId());
            if (!StringUtils.isEmpty(productIds)) {
                List<String> productIdList = Arrays.asList(productIds.split(","));
                recommendationPlanIndexMappingData.setProductIdList(StringUtil.transformInteger(productIdList));
            }

            //创建索引对象
            IndexRequestDTO indexRequestDTO = new IndexRequestDTO(
                    elasticSearchConfig.getRecommendedPlanDataIndexName(),
                    TypeConstant.RECOMMENDATION_PLAN_TYPE,
                    recommendationPlanIndexMappingData.getId() + "",
                    JsonUtil.toJson(recommendationPlanIndexMappingData)
            );

            //加入批量对象
            bulkIndexList.add(indexRequestDTO);

            //Count
            if (0 == i % 100) {
                log.info(CLASS_LOG_PREFIX + "已解析{}条推荐方案数据,剩余{}条", i, recommendationPlanList.size() - i);
            }
        }

        log.info(CLASS_LOG_PREFIX + "解析推荐方案数据完成，开始索引数据...");
        //索引数据
        BulkStatistics bulkStatistics = null;
        try {
            bulkStatistics = elasticSearchService.bulk(bulkIndexList, null);
        } catch (ElasticSearchException e) {
            log.error(CLASS_LOG_PREFIX + "索引推荐方案数据异常:ElasticSearchException:{}", e);
        }
        log.info(CLASS_LOG_PREFIX + "索引推荐方案数据成功:成功索引数:{},BulkStatistics:{}", new String[]{
                bulkIndexList.size() + "",
                null == bulkStatistics ? null : bulkStatistics.toString()
        });

        return bulkIndexList.size();
    }

    /**
     * 索引增量推荐方案数据 （从数据库去数据）
     *
     * @param recommendationPlanList 推荐方案对象
     * @return 索引成功数
     */
    public int indexRecommendationPlanData(List<RecommendationPlanPo> recommendationPlanList, String updateType) {

        if (Objects.equals(UserDefinedConstant.FULL_UPDATE_TYPE, updateType)) {
            log.info(CLASS_LOG_PREFIX + "开始全量更新推荐方案数据...");
        } else if(Objects.equals(UserDefinedConstant.INCREMENT_UPDATE_TYPE, updateType)) {
            log.info(CLASS_LOG_PREFIX + "开始增量更新推荐方案数据...");
        } else {
            log.error(CLASS_LOG_PREFIX + "非法入侵推荐方案更新方法");
            return 0;
        }
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

            //复制基本对象
            EntityCopyUtils.copyData(recommendationPlanPo, recommendationPlanIndexMappingData);

            //自定义方案来源表类型
            recommendationPlanIndexMappingData.setPlanTableType(DesignPlanType.RECOMMENDED_TABLE_TYPE);

            //方案版本费转换INT
            Double planPrice = recommendationPlanPo.getPlanPrice();
            if (null != planPrice && planPrice > 0) {
                if (planPrice < 1) {
                    recommendationPlanIndexMappingData.setPlanPrice(1);
                } else {
                    recommendationPlanIndexMappingData.setPlanPrice(Integer.valueOf(new java.text.DecimalFormat("0").format(planPrice)));
                }
            }

            //公司ID/名称
            Integer companyId = recommendationPlanPo.getCompanyId();
            String companyIds = recommendationPlanPo.getCompanyIds();
            String companyName = recommendationPlanPo.getCompanyName();
            String companyNames = recommendationPlanPo.getCompanyNames();
            if (null != companyId && 0 < companyId.intValue()) {
                if (!StringUtils.isEmpty(companyIds)) {
                    recommendationPlanIndexMappingData.setCompanyIdList(StringUtil.transformInteger(Arrays.asList(companyIds.split(","))));
                    recommendationPlanIndexMappingData.setCompanyNameList(Arrays.asList(companyNames.split(",")));
                } else {
                    recommendationPlanIndexMappingData.setCompanyIdList(Arrays.asList(companyId));
                    recommendationPlanIndexMappingData.setCompanyNameList(Arrays.asList(companyName));
                }
            } else {
                if (!StringUtils.isEmpty(companyIds)) {
                    recommendationPlanIndexMappingData.setCompanyIdList(StringUtil.transformInteger(Arrays.asList(companyIds.split(","))));
                    recommendationPlanIndexMappingData.setCompanyNameList(Arrays.asList(companyNames.split(",")));
                }
            }

            //品牌信息
            String brandIds = recommendationPlanPo.getBrandIds();
            String brandNames = recommendationPlanPo.getBrandNames();
            if (!StringUtils.isEmpty(brandIds)) {
                List<String> brandIdList = Arrays.asList(brandIds.split(","));
                recommendationPlanIndexMappingData.setBrandIdList(StringUtil.transformInteger(brandIdList));
                recommendationPlanIndexMappingData.setBrandNameList(Arrays.asList(brandNames.split(",")));
            }

            //空间数据
            /*Integer spaceCommonId = recommendationPlanIndexMappingData.getSpaceCommonId();
            if (null != spaceCommonId && 0 < spaceCommonId) {
                //小区名称
                String livingNames = recommendedPlanLivingMetaDataStorage.getLivingNameBySpaceId(spaceCommonId);
                if (!StringUtils.isEmpty(livingNames)) {
                    recommendationPlanIndexMappingData.setLivingNameList(Arrays.asList(livingNames.split(",")));
                }
            }*/

            //房间类型
            Integer spaceFunctionId = recommendationPlanIndexMappingData.getSpaceFunctionId();
            if (null != spaceFunctionId && 0 < spaceFunctionId) {
                String houseTypeName = systemDictionaryMetaDataStorage.getSystemDictionaryNameByTypeAndValue(SystemDictionaryType.SYSTEM_DICTIONARY_TYPE_HOUSETYPE, spaceFunctionId);
                if (!StringUtils.isEmpty(houseTypeName)) {
                    recommendationPlanIndexMappingData.setHouseTypeName(houseTypeName);
                }
            }

            //商家后台上下架状态 ONEKEY 一键方案 OPEN 公开方案 多个用,隔开 ONEKEY,OPEN
            String shelfStatus = recommendationPlanPo.getShelfStatus();
            if (!StringUtils.isEmpty(shelfStatus)) {
                recommendationPlanIndexMappingData.setShelfStatusList(Arrays.asList(shelfStatus.split(",")));
            }

            //适用空间面积
            String applySpaceAreaStr = recommendationPlanPo.getApplySpaceAreas();
            if (!StringUtils.isEmpty(applySpaceAreaStr)) {
                //打组主方案存储子方案所有适用面积集合
                Integer groupPrimaryId = recommendationPlanPo.getGroupPrimaryId();
                Integer recommendedPlanId = recommendationPlanPo.getId();
                if (groupPrimaryId.equals(recommendedPlanId)) {
                    if (Objects.equals(UserDefinedConstant.INCREMENT_UPDATE_TYPE, updateType)) {
                        try {
                            StringBuilder builder = new StringBuilder();
                            List<DesignPlanRecommendedPo> recommendedPoList = designPlanIndexService.queryRecommendationPlanPoListByRecommendationPlanId(recommendedPlanId);
                            if (null != recommendedPoList && 0 < recommendedPoList.size()) {
                                recommendedPoList.forEach(recommendationPo ->
                                    builder.append(recommendationPo.getApplySpaceAreas() + ",")
                                );
                                String applyAllSpaceAreas = builder != null ? builder.substring(0,builder.length()-1) : "";
                                if (!StringUtils.isEmpty(applyAllSpaceAreas)) {
                                    recommendationPlanIndexMappingData.setApplyAllSpaceAreaList(Arrays.asList(applyAllSpaceAreas.split(",")));
                                } else {
                                    recommendationPlanIndexMappingData.setApplyAllSpaceAreaList(Arrays.asList(applySpaceAreaStr.split(",")));
                                }
                            } else {
                                recommendationPlanIndexMappingData.setApplyAllSpaceAreaList(Arrays.asList(applySpaceAreaStr.split(",")));
                            }
                        } catch (DesignPlanIndexException e) {
                            log.error(CLASS_LOG_PREFIX + "获取打组推荐方案数据异常 recommendedPlanId：{}, exception:{}" + recommendedPlanId, e);
                            recommendationPlanIndexMappingData.setApplyAllSpaceAreaList(Arrays.asList(applySpaceAreaStr.split(",")));
                        }
                    } else if (Objects.equals(UserDefinedConstant.FULL_UPDATE_TYPE, updateType)) {
                        String groupRecommendedPlanAreas = designPlanRecommendationMetaDataStorage.getGroupRecommendedAreasById(groupPrimaryId);
                        if (!StringUtils.isEmpty(groupRecommendedPlanAreas)) {
                            recommendationPlanIndexMappingData.setApplyAllSpaceAreaList(Arrays.asList(groupRecommendedPlanAreas.split(",")));
                        } else {
                            recommendationPlanIndexMappingData.setApplyAllSpaceAreaList(Arrays.asList(applySpaceAreaStr.split(",")));
                        }
                    }
                } else {
                    recommendationPlanIndexMappingData.setApplyAllSpaceAreaList(Arrays.asList(applySpaceAreaStr.split(",")));
                }
            }

            //方案全平台数据
            getRecommendedPlanPlatformInfoById(recommendationPlanIndexMappingData, updateType);

            if (Objects.equals(UserDefinedConstant.FULL_UPDATE_TYPE, updateType)) {
                //装修报价
                getDecoratePriceById(recommendationPlanIndexMappingData, DesignPlanType.RECOMMENDED_TABLE_TYPE);
                //方案关联的店铺
                List<CompanyShopPlanPo> shopPlanPoList = companyShopPlanMetaDataStorage.getCompanyShopPlanPoById(recommendationPlanIndexMappingData.getId(), DesignPlanType.RECOMMENDED_TABLE_TYPE);
                if (null != shopPlanPoList && shopPlanPoList.size() > 0) {
                    List<CompanyShopPlanPo> shopPlanList = new ArrayList<>(shopPlanPoList.size());
                    for (CompanyShopPlanPo shopPlanPo : shopPlanPoList) {
                        //判断非删除的店铺
                        if (UserDefinedConstant.DATA_DELETED_NO == shopPlanPo.getDataIsDeleted()) {
                            shopPlanList.add(shopPlanPo);
                        }
                    }
                    recommendationPlanIndexMappingData.setPlanShopInfoList(shopPlanList);
                }
            } else if (Objects.equals(UserDefinedConstant.INCREMENT_UPDATE_TYPE, updateType)) {
                //装修报价
                selectDecoratePriceById(recommendationPlanIndexMappingData, DesignPlanType.RECOMMENDED_TABLE_TYPE);
                //店铺信息
                List<CompanyShopPlanPo> shopPlanPoList = companyShopPlanMetaDataStorage.selectCompanyShopPoByPlanId(recommendationPlanIndexMappingData.getId());
                if (null != shopPlanPoList && 0 < shopPlanPoList.size()) {
                    recommendationPlanIndexMappingData.setPlanShopInfoList(shopPlanPoList);
                }
            }

            //方案产品列表
            List<Integer> productIdList = recommendedPlanProductMetaDataStorage.selectProductIdsByPlanId(recommendationPlanIndexMappingData.getId());
            if (null != productIdList && 0 < productIdList.size()) {
                recommendationPlanIndexMappingData.setProductIdList(productIdList);
            }

            //创建索引对象
            IndexRequestDTO indexRequestDTO = new IndexRequestDTO(
                    elasticSearchConfig.getRecommendedPlanDataIndexName(),
                    TypeConstant.RECOMMENDATION_PLAN_TYPE,
                    recommendationPlanIndexMappingData.getId() + "",
                    JsonUtil.toJson(recommendationPlanIndexMappingData)
            );

            //加入批量对象
            bulkIndexList.add(indexRequestDTO);

            //Count
            if (0 == i % 100) {
                log.info(CLASS_LOG_PREFIX + "已解析{}条推荐方案数据,剩余{}条", i, recommendationPlanList.size() - i);
            }
        }

        log.info(CLASS_LOG_PREFIX + "解析推荐方案数据完成，开始索引数据...");
        //索引数据
        BulkStatistics bulkStatistics = null;
        try {
            bulkStatistics = elasticSearchService.bulk(bulkIndexList, null);
        } catch (ElasticSearchException e) {
            log.error(CLASS_LOG_PREFIX + "索引推荐方案数据异常:ElasticSearchException:{}", e);
        }
        log.info(CLASS_LOG_PREFIX + "索引推荐方案数据成功:成功索引数:{},BulkStatistics:{}", new String[]{
                bulkIndexList.size() + "",
                null == bulkStatistics ? null : bulkStatistics.toString()
        });

        return bulkIndexList.size();
    }

    /**
     * 获取推荐方案平台信息
     * @param recommendationPlanIndexMappingData
     */
    public void getRecommendedPlanPlatformInfoById(RecommendationPlanIndexMappingData recommendationPlanIndexMappingData, String logicType) {

        DesignPlanPlatformData designPlanPlatformData = null;
        //获取缓存全屋或推荐方案平台对象  通过fullHouseId不为空判断为全屋
        if (null != recommendationPlanIndexMappingData && null != recommendationPlanIndexMappingData.getFullHouseId() && 0 < recommendationPlanIndexMappingData.getFullHouseId().intValue()) {
            designPlanPlatformData = designPlanPlatformMetaDataStorage.queryPlanPlatformByPlanId(recommendationPlanIndexMappingData.getFullHouseId(), logicType, PlatformConstant.PLANFORM_DESING_PLAN_TYPE_FULL_HOUSE);
        } else {
            designPlanPlatformData = designPlanPlatformMetaDataStorage.queryPlanPlatformByPlanId(recommendationPlanIndexMappingData.getId(), logicType, PlatformConstant.PLANFORM_DESING_PLAN_TYPE_ONEKEY);
        }

        if (null != designPlanPlatformData) {
            EntityCopyUtils.copyData(designPlanPlatformData, recommendationPlanIndexMappingData);
            //平台编码列表
            List<String> platformCodeList = new ArrayList<>(9);
            if (null != designPlanPlatformData.getPlatformDesignPlanToBMobile()) {
                platformCodeList.add(designPlanPlatformData.getPlatformDesignPlanToBMobile().getPlatformCode());
            }
            if (null != designPlanPlatformData.getPlatformDesignPlanToBPc()) {
                platformCodeList.add(designPlanPlatformData.getPlatformDesignPlanToBPc().getPlatformCode());
            }
            if (null != designPlanPlatformData.getPlatformDesignPlanToCSite()) {
                platformCodeList.add(designPlanPlatformData.getPlatformDesignPlanToCSite().getPlatformCode());
            }
            if (null != designPlanPlatformData.getPlatformDesignPlanToCMobile()) {
                platformCodeList.add(designPlanPlatformData.getPlatformDesignPlanToCMobile().getPlatformCode());
            }
            if (null != designPlanPlatformData.getPlatformDesignPlanSanduManager()) {
                platformCodeList.add(designPlanPlatformData.getPlatformDesignPlanSanduManager().getPlatformCode());
            }
            if (null != designPlanPlatformData.getPlatformDesignPlanHouseDraw()) {
                platformCodeList.add(designPlanPlatformData.getPlatformDesignPlanHouseDraw().getPlatformCode());
            }
            if (null != designPlanPlatformData.getPlatformDesignPlanMerchantsManager()) {
                platformCodeList.add(designPlanPlatformData.getPlatformDesignPlanMerchantsManager().getPlatformCode());
            }
            if (null != designPlanPlatformData.getPlatformDesignPlanTest()) {
                platformCodeList.add(designPlanPlatformData.getPlatformDesignPlanTest().getPlatformCode());
            }
            if (null != designPlanPlatformData.getPlatformDesignPlanU3dPlugin()) {
                platformCodeList.add(designPlanPlatformData.getPlatformDesignPlanU3dPlugin().getPlatformCode());
            }
            if (null != designPlanPlatformData.getPlatformDesignPlanMiniProgram()) {
                platformCodeList.add(designPlanPlatformData.getPlatformDesignPlanMiniProgram().getPlatformCode());
            }
            if (null != designPlanPlatformData.getPlatformDesignPlanSelectDecoration()) {
                platformCodeList.add(designPlanPlatformData.getPlatformDesignPlanSelectDecoration().getPlatformCode());
            }
            recommendationPlanIndexMappingData.setPlatformCodeList(platformCodeList);
        } else {
            //log.info(CLASS_LOG_PREFIX + "recommendationPlanId：{}获取方案平台数据为空！", recommendationPlanIndexMappingData.getId());
        }
    }


    /**
     * 获取方案装修报价信息
     * @param recommendationPlanIndexMappingData
     */
    public void getDecoratePriceById(RecommendationPlanIndexMappingData recommendationPlanIndexMappingData, int planType) {

        DecoratePriceData decoratePriceData = null;
        if (DesignPlanType.RECOMMENDED_TABLE_TYPE == planType) {
            decoratePriceData = designPlanDecoratePriceMetaDataStorage.getDecoratePriceDataByRecommendedPlanId(recommendationPlanIndexMappingData.getId());
        }
        if (DesignPlanType.FULLHOUSE_TABLE_TYPE == planType) {
            decoratePriceData = designPlanDecoratePriceMetaDataStorage.getDecoratePriceDataByFullHouseId(recommendationPlanIndexMappingData.getFullHouseId());
        }

        //获取装修报价对象
        getDecoratePricePo(recommendationPlanIndexMappingData, decoratePriceData);
    }

    /**
     * 获取方案装修报价信息
     * @param recommendationPlanIndexMappingData
     */
    public void selectDecoratePriceById(RecommendationPlanIndexMappingData recommendationPlanIndexMappingData, int planType) {

        DecoratePriceData decoratePriceData = null;
        if (DesignPlanType.RECOMMENDED_TABLE_TYPE == planType) {
            decoratePriceData = designPlanDecoratePriceMetaDataStorage.selectDecoratePriceByIdData(recommendationPlanIndexMappingData.getId(), planType);
        }
        if (DesignPlanType.FULLHOUSE_TABLE_TYPE == planType) {
            decoratePriceData = designPlanDecoratePriceMetaDataStorage.selectDecoratePriceByIdData(recommendationPlanIndexMappingData.getFullHouseId(), planType);
        }

        //获取装修报价对象
        getDecoratePricePo(recommendationPlanIndexMappingData, decoratePriceData);
    }

    /**
     * 获取装修报价对象
     * @param recommendationPlanIndexMappingData
     * @param decoratePriceData
     */
    private void getDecoratePricePo(RecommendationPlanIndexMappingData recommendationPlanIndexMappingData, DecoratePriceData decoratePriceData) {

        if (null != decoratePriceData) {
            EntityCopyUtils.copyData(decoratePriceData, recommendationPlanIndexMappingData);
            //平台编码列表
            List<Integer> decoratePriceTypeList = new ArrayList<>(4);
            if (null != decoratePriceData.getDecorateHalfPack()) {
                decoratePriceTypeList.add(decoratePriceData.getDecorateHalfPack().getDecoratePriceType());
            }
            if (null != decoratePriceData.getDecorateAllPack()) {
                decoratePriceTypeList.add(decoratePriceData.getDecorateAllPack().getDecoratePriceType());
            }
            if (null != decoratePriceData.getDecoratePackSoft()) {
                decoratePriceTypeList.add(decoratePriceData.getDecoratePackSoft().getDecoratePriceType());
            }
            if (null != decoratePriceData.getDecorateWater()) {
                decoratePriceTypeList.add(decoratePriceData.getDecorateWater().getDecoratePriceType());
            }
            recommendationPlanIndexMappingData.setDecoratePriceTypeList(decoratePriceTypeList);
        } else {

        }
    }
}
