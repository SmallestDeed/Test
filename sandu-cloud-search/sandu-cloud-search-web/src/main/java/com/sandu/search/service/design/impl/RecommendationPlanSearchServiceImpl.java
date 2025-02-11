package com.sandu.search.service.design.impl;

import com.google.gson.reflect.TypeToken;
import com.sandu.node.constant.NodeInfoConstant;
import com.sandu.search.common.constant.*;
import com.sandu.search.common.tools.DomainUtil;
import com.sandu.search.common.tools.JsonUtil;
import com.sandu.search.common.tools.MessageUtil;
import com.sandu.search.common.tools.StringUtil;
import com.sandu.search.dao.DesignPlanIndexDao;
import com.sandu.search.entity.common.PageVo;
import com.sandu.search.entity.designplan.convert.RecommendationPlanConvert;
import com.sandu.search.entity.designplan.po.PlanTypeInfoPo;
import com.sandu.search.entity.designplan.po.RecommendationPlanPo;
import com.sandu.search.entity.designplan.po.TopDesignPlanRecommendPO;
import com.sandu.search.entity.designplan.vo.CollectRecommendedVo;
import com.sandu.search.entity.designplan.vo.RecommendationPlanSearchVo;
import com.sandu.search.entity.designplan.vo.RecommendationPlanVo;
import com.sandu.search.entity.elasticsearch.constant.IndexConstant;
import com.sandu.search.entity.elasticsearch.dco.MultiMatchField;
import com.sandu.search.entity.elasticsearch.index.RecommendationPlanIndexMappingData;
import com.sandu.search.entity.elasticsearch.po.metadate.CompanyPo;
import com.sandu.search.entity.elasticsearch.po.metadate.RecommendedPlanFavoritePo;
import com.sandu.search.entity.elasticsearch.po.metadate.SysUserPo;
import com.sandu.search.entity.elasticsearch.response.SearchObjectResponse;
import com.sandu.search.entity.elasticsearch.search.ShouldMatchSearch;
import com.sandu.search.entity.elasticsearch.search.SortOrderObject;
import com.sandu.search.entity.user.LoginUser;
import com.sandu.search.exception.ElasticSearchException;
import com.sandu.search.exception.MetaDataException;
import com.sandu.search.exception.RecommendationPlanSearchException;
import com.sandu.search.service.design.DesignPlanIndexRecommendedService;
import com.sandu.search.service.design.RecommendationPlanSearchService;
import com.sandu.search.service.design.UserDesignPlanPurchaseRecordService;
import com.sandu.search.service.elasticsearch.ElasticSearchService;
import com.sandu.search.service.metadata.MetaDataService;
import com.sandu.search.service.redis.RedisService;
import com.sandu.search.storage.company.BrandMetaDataStorage;
import com.sandu.search.storage.company.CompanyMetaDataStorage;
import com.sandu.search.storage.system.SysRoleFuncMetaDataStorage;
import com.sandu.search.storage.system.SysUserMetaDataStorage;
import com.sandu.search.storage.system.SystemDictionaryMetaDataStorage;
import com.sandu.system.service.NodeInfoBizService;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.index.query.*;
import org.elasticsearch.script.Script;
import org.elasticsearch.script.ScriptType;
import org.elasticsearch.search.sort.SortMode;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 设计方案搜索服务
 *
 * @date 20180103
 * @auth pengxuangang
 */
@Slf4j
@Service("recommendationPlanSearchService")
public class RecommendationPlanSearchServiceImpl implements RecommendationPlanSearchService {

    private final static String CLASS_LOG_PREFIX = "推荐方案搜索服务:";

    private final ElasticSearchService elasticSearchService;
    private final CompanyMetaDataStorage companyMetaDataStorage;
    private final MetaDataService metaDataService;
    private final SysUserMetaDataStorage sysUserMetaDataStorage;
    private final SystemDictionaryMetaDataStorage systemDictionaryMetaDataStorage;
    private final SysRoleFuncMetaDataStorage sysRoleFuncMetaDataStorage;
    private final RedisService redisService;
    private final HttpServletRequest request;
    private final DomainUtil domainUtil;
    private final BrandMetaDataStorage brandMetaDataStorage;
    private final DesignPlanIndexDao designPlanIndexDao;
    private final NodeInfoBizService nodeInfoBizService;

    @Autowired
    private UserDesignPlanPurchaseRecordService userDesignPlanPurchaseRecordService;
    @Autowired
    private DesignPlanIndexRecommendedService designPlanIndexRecommendedService;

    @Autowired
    public RecommendationPlanSearchServiceImpl(MetaDataService metaDataService,
                                               ElasticSearchService elasticSearchService,
                                               CompanyMetaDataStorage companyMetaDataStorage,
                                               SysRoleFuncMetaDataStorage sysRoleFuncMetaDataStorage,
                                               SystemDictionaryMetaDataStorage systemDictionaryMetaDataStorage,
                                               SysUserMetaDataStorage sysUserMetaDataStorage,
                                               RedisService redisService,
                                               HttpServletRequest request,
                                               DomainUtil domainUtil,
                                               BrandMetaDataStorage brandMetaDataStorage,
                                               DesignPlanIndexDao designPlanIndexDao,
                                               NodeInfoBizService nodeInfoBizService) {
        this.elasticSearchService = elasticSearchService;
        this.companyMetaDataStorage = companyMetaDataStorage;
        this.metaDataService = metaDataService;
        this.sysRoleFuncMetaDataStorage = sysRoleFuncMetaDataStorage;
        this.systemDictionaryMetaDataStorage = systemDictionaryMetaDataStorage;
        this.sysUserMetaDataStorage = sysUserMetaDataStorage;
        this.redisService = redisService;
        this.request = request;
        this.domainUtil = domainUtil;
        this.brandMetaDataStorage = brandMetaDataStorage;
        this.designPlanIndexDao = designPlanIndexDao;
        this.nodeInfoBizService = nodeInfoBizService;
    }

    @Override
    public SearchObjectResponse searchRecommendationPlan(RecommendationPlanSearchVo recommendationPlanSearchVo, PageVo pageVo) throws RecommendationPlanSearchException {

        if (null == recommendationPlanSearchVo || null == pageVo) {
            log.warn(CLASS_LOG_PREFIX + "通过条件搜索设计方案失败，必需参数为空.");
            throw new RecommendationPlanSearchException(CLASS_LOG_PREFIX + "通过条件搜索设计方案失败，必需参数为空.");
        }

        /********************************* Step 1: 组装搜索条件 *************************************************/
        //匹配条件List
        List<QueryBuilder> matchQueryList = new ArrayList<>();
        //非匹配条件List
        List<QueryBuilder> noMatchQueryList = new ArrayList<>(1);
        //或匹配条件List
        List<ShouldMatchSearch> shouldMatchSearchList = new ArrayList<>();
        //排序对象
        List<SortOrderObject> sortOrderObjectList = new ArrayList<>();
        //搜索全屋方案bool
        BoolQueryBuilder searchFullHouseBoolQueryBuilder = null;
        //搜索一键方案bool
        BoolQueryBuilder searchRecommendedPlanBooleQueryBuilder = null;

        //方案类型
        String displayType = recommendationPlanSearchVo.getDisplayType();
        Integer houseType = recommendationPlanSearchVo.getHouseType();
        String platformCode = recommendationPlanSearchVo.getPlatformCode();

        //一键方案查询全屋方案
        if (RecommendationPlanPo.FUNCTION_TYPE_DECORATE.equals(displayType)) {
            //空间类型搜索展示各自空间类型(全部和全屋查询)
            if(Objects.equals(PlatformConstant.PLATFORM_CODE_MINI_PROGRAM, platformCode)
                || Objects.equals(PlatformConstant.PLATFORM_CODE_SELECT_DECORATION, platformCode)
                || Objects.equals(PlatformConstant.PLATFORM_CODE_TOB_MOBILE, platformCode)) {

                if (null == houseType || 0 == houseType || SystemDictionaryType.SYSTEM_DICTIONARY_HOUSETYPE_FULLROOM_VALUE == houseType) {
                    searchFullHouseBoolQueryBuilder = this.searchFullHousePlan(recommendationPlanSearchVo);
                    sortOrderObjectList.add(new SortOrderObject(
                            QueryConditionField.QUERY_CONDITION_FIELD_RECOMMENDATION_PLANA_TABLE_TYPE,
                            SortOrder.DESC,
                            SortMode.MAX,
                            SortOrderObject.DEFAULT_SORT
                    ));
                }

                //适用空间面积排序
                Integer spaceAreas = recommendationPlanSearchVo.getSpaceArea();
                if (null != spaceAreas && spaceAreas > 0) {
                    Script scriptSort = new Script(ScriptType.INLINE,"painless","doc['"+QueryConditionField.QUERY_CONDITION_FIELD_RECOMMENDATION_PLAN_SPACE_AREAS+"'].value == " + spaceAreas + "?0:(" + spaceAreas + "-doc['"+QueryConditionField.QUERY_CONDITION_FIELD_RECOMMENDATION_PLAN_SPACE_AREAS+"'].value > 0 ? (" + spaceAreas + "-doc['"+QueryConditionField.QUERY_CONDITION_FIELD_RECOMMENDATION_PLAN_SPACE_AREAS+"'].value):(" + spaceAreas + " + doc['"+QueryConditionField.QUERY_CONDITION_FIELD_RECOMMENDATION_PLAN_SPACE_AREAS+"'].value))", new HashMap<>());
                    sortOrderObjectList.add(new SortOrderObject(scriptSort, SortOrder.ASC, SortOrderObject.SCRIPT_SORT));
                }

                sortOrderObjectList.add(new SortOrderObject(
                        QueryConditionField.QUERY_CONDITION_FIELD_RECOMMENDATION_PLATFORM_TOC_SELECT_DECORATION_PUTAWAT_STATUS
                                + "." +
                                QueryConditionField.QUERY_CONDITION_FIELD_RECOMMENDATION_PLAN_2C_PLATFORM_MODIFY_TIME,
                        SortOrder.DESC,
                        SortMode.MAX,
                        SortOrderObject.DEFAULT_SORT
                ));
            }
        }

        //过滤置顶方案
        if(recommendationPlanSearchVo.getPlatformCode().equals(PlatformConstant.PLATFORM_CODE_SELECT_DECORATION)){
            List<String> ids = recommendationPlanSearchVo.getIds();
            if(ids != null && ids.size() > 0){
                String[] idStr = ids.toArray(new String[ids.size()]);
                BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
                boolQueryBuilder.filter(QueryBuilders.boolQuery().mustNot(QueryBuilders.idsQuery().addIds(idStr)));
                matchQueryList.add(boolQueryBuilder);
            }
        }

        //空间、户型搜索一键方案替换列表
        if (RecommendationPlanPo.FUNCTION_TYPE_DRAGDECORATE.equals(displayType)) {
            searchRecommendedPlanBooleQueryBuilder = searchDragDecorateRecommendationPlan(recommendationPlanSearchVo, sortOrderObjectList);
        }

        //一键方案列表、公开方案和样板间方案列表
        if (RecommendationPlanPo.FUNCTION_TYPE_DECORATE.equals(displayType) ||
                RecommendationPlanPo.FUNCTION_TYPE_PUBLIC.equals(displayType) ||
                RecommendationPlanPo.FUNCTION_TYPE_PROTOTYPE.equals(displayType)) {
            //查询全屋类型不查推荐方案
            if (null == houseType || 0 == houseType || SystemDictionaryType.SYSTEM_DICTIONARY_HOUSETYPE_FULLROOM_VALUE != houseType) {
                searchRecommendedPlanBooleQueryBuilder = searchDecorateOrOpenOrPrototypeRecommendationPlan(recommendationPlanSearchVo, sortOrderObjectList);
            }
        }

        //测试方案和审核方案列表
        if (RecommendationPlanPo.FUNCTION_TYPE_TEST.equals(displayType) ||
                RecommendationPlanPo.FUNCTION_TYPE_PUBLISH.equals(displayType)) {
            searchRecommendedPlanBooleQueryBuilder = searchTestOrPublishypeRecommendationPlan(recommendationPlanSearchVo, sortOrderObjectList);
        }

        //组装条件
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        if (null != searchFullHouseBoolQueryBuilder && null != searchRecommendedPlanBooleQueryBuilder) {
            boolQueryBuilder.should(searchFullHouseBoolQueryBuilder);
            boolQueryBuilder.should(searchRecommendedPlanBooleQueryBuilder);
            matchQueryList.add(boolQueryBuilder);
        } else {
            matchQueryList.add(searchFullHouseBoolQueryBuilder == null ? searchRecommendedPlanBooleQueryBuilder : searchFullHouseBoolQueryBuilder);
        }

        /********************************* Step 2: 搜索数据 *************************************************/
        //搜索数据
        SearchObjectResponse searchObjectResponse;
        try {
            searchObjectResponse = elasticSearchService.search(matchQueryList, noMatchQueryList, shouldMatchSearchList, null, sortOrderObjectList, pageVo.getStart(), pageVo.getPageSize(), IndexConstant.RECOMMENDATION_PLAN_ALIASES);
        } catch (ElasticSearchException e) {
            log.error(CLASS_LOG_PREFIX + "通过条件搜索设计方案失败! ElasticSearchException:{}.", e);
            throw new RecommendationPlanSearchException(CLASS_LOG_PREFIX + "通过条件搜索设计方案失败! ElasticSearchException:{}.", e);
        }
        log.info(CLASS_LOG_PREFIX + "通过条件搜索设计方案列表成功!SearchObjectResponse:{}.", searchObjectResponse.getHitTotal());

        if (null == searchObjectResponse.getHitResult()) {
            log.info(CLASS_LOG_PREFIX + "通过条件搜索设计方案失败,查询结果为空。RecommendationPlanVo:{}.", recommendationPlanSearchVo.toString());
            throw new RecommendationPlanSearchException(CLASS_LOG_PREFIX + "通过条件搜索设计方案列表失败,查询结果为空。RecommendationPlanVo:" + recommendationPlanSearchVo.toString());
        }

        return searchObjectResponse;
    }

    /**
     * 搜索全屋方案
     * @param recommendationPlanSearchVo
     */
    private BoolQueryBuilder searchFullHousePlan(RecommendationPlanSearchVo recommendationPlanSearchVo) {

        //初始化
        BoolQueryBuilder searchFullHouseBoolQueryBuilder = QueryBuilders.boolQuery();

        //企业、 经销商品牌过滤
        BoolQueryBuilder companyInfoBoolQueryBuilder = getBoolQueryBuildersOfCompanyIdList(recommendationPlanSearchVo);
        if (null != companyInfoBoolQueryBuilder) {
            searchFullHouseBoolQueryBuilder.filter(companyInfoBoolQueryBuilder);
        }

        //风格
        Integer designStyleId = recommendationPlanSearchVo.getDesignStyleId();
        if (null != designStyleId && 0 < designStyleId) {
            searchFullHouseBoolQueryBuilder.filter(QueryBuilders.termQuery(QueryConditionField.QUERY_CONDITION_FIELD_RECOMMENDATION_PLAN_DESIGN_STYLEID, designStyleId));
        }

        //方案类型(2:全屋方案)
        searchFullHouseBoolQueryBuilder.filter(QueryBuilders.termQuery(QueryConditionField.QUERY_CONDITION_FIELD_RECOMMENDATION_PLANA_TABLE_TYPE, DesignPlanType.FULLHOUSE_TABLE_TYPE));

        //数据未被删除
        searchFullHouseBoolQueryBuilder.filter(QueryBuilders.termQuery(QueryConditionField.QUERY_CONDITION_FIELD_RECOMMENDATION_PLAN_STATUS, 0));

        //组合查询条件--单值匹配多字段--List中第一个字段优先级最高
        getMultiMatchQueryBuilder(recommendationPlanSearchVo, searchFullHouseBoolQueryBuilder);

        //平台类型
        if (!"shop".equals(recommendationPlanSearchVo.getEnterType())){
            BoolQueryBuilder boolQueryBuildersOfPlatformCode = getBoolQueryBuildersOfPlatformCode(recommendationPlanSearchVo.getPlatformCode());
            if (null != boolQueryBuildersOfPlatformCode) {
                searchFullHouseBoolQueryBuilder.filter(boolQueryBuildersOfPlatformCode);
            }
        }

        //装修报价范围过滤
        Integer decoratePriceType = recommendationPlanSearchVo.getDecoratePriceType();
        Integer decoratePriceRenge = recommendationPlanSearchVo.getDecoratePriceRange();
        BoolQueryBuilder boolQueryBuildersOfDecoratePrice = getBoolQueryBuildersOfDecoratePrice(decoratePriceType, decoratePriceRenge);
        if (null != boolQueryBuildersOfDecoratePrice) {
            searchFullHouseBoolQueryBuilder.filter(boolQueryBuildersOfDecoratePrice);
        }

        //店铺ID
        Integer shopId = recommendationPlanSearchVo.getShopId();
        if (null != shopId && 0 < shopId) {
            String planShopInfoList = QueryConditionField.QUERY_CONDITION_FIELD_RECOMMENDATION_PLANA_SHOP_INFO_LIST;
            QueryBuilder queryBuilder = QueryBuilders.nestedQuery(planShopInfoList,
                    QueryBuilders.boolQuery()
                            .must(QueryBuilders.matchQuery(planShopInfoList + "." + QueryConditionField.QUERY_CONDITION_FIELD_RECOMMENDATION_PLANA_SHOP_ID, shopId)),
                    ScoreMode.Max);
            searchFullHouseBoolQueryBuilder.must(queryBuilder);
        }

        return searchFullHouseBoolQueryBuilder;
    }

    /**
     * 搜索替换一键方案条件(空间搜索/户型搜索)
     *
     * @param recommendationPlanSearchVo
     */
    private BoolQueryBuilder searchDragDecorateRecommendationPlan(RecommendationPlanSearchVo recommendationPlanSearchVo, List<SortOrderObject> sortOrderObjectList) {

        //初始化
        BoolQueryBuilder searchRecommendedPlanBooleQueryBuilder = QueryBuilders.boolQuery();

        //本公司、用户、类型
        Integer companyId = recommendationPlanSearchVo.getCompanyId();
        Integer userId = recommendationPlanSearchVo.getUserId();

        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        BoolQueryBuilder onekeyBoole = QueryBuilders.boolQuery();
        onekeyBoole.must(QueryBuilders.termQuery(QueryConditionField.QUERY_CONDITION_FIELD_RECOMMENDATION_PLAN_RELEASE_STATUS, RecommendationPlanPo.DESIGN_PLAN_IS_PUBLISH));
        //企业id、 经销商品牌、三度公司分配标识过滤
        BoolQueryBuilder companyInfoBoolQueryBuilder = getBoolQueryBuildersOfCompanyIdList(recommendationPlanSearchVo);
        if (null != companyInfoBoolQueryBuilder) {
            onekeyBoole.must(companyInfoBoolQueryBuilder);
        }
        //平台类型
        if (!StringUtils.isEmpty(recommendationPlanSearchVo.getPlatformCode())){
            BoolQueryBuilder boolQueryBuildersOfPlatformCode = getBoolQueryBuildersOfPlatformCode(recommendationPlanSearchVo.getPlatformCode());
            if (null != boolQueryBuildersOfPlatformCode) {
                onekeyBoole.must(boolQueryBuildersOfPlatformCode);
            }
        }
        boolQueryBuilder.should(onekeyBoole);

        //查看审核方案权限
        if (recommendationPlanSearchVo.isAuditPlanMenuPermission()) {
            BoolQueryBuilder auditPlanBoole = QueryBuilders.boolQuery();
            auditPlanBoole.must(QueryBuilders.termQuery(QueryConditionField.QUERY_CONDITION_FIELD_RECOMMENDATION_PLAN_RELEASE_STATUS, RecommendationPlanPo.DESIGN_PLAN_AUDIT_IS_PUBLISH));
            if (null != companyId && 0 < companyId) {
                auditPlanBoole.must(QueryBuilders.termQuery(QueryConditionField.QUERY_CONDITION_FIELD_RECOMMENDATION_PLAN_COMPANY_ID_LIST, companyId));
            }
            if (null != auditPlanBoole) {
                boolQueryBuilder.should(auditPlanBoole);
            }
        }
        //查看测试方案权限
        if ( recommendationPlanSearchVo.isTestPlanMenuPermission()) {
            BoolQueryBuilder testPlanBoole = QueryBuilders.boolQuery();
            testPlanBoole.must(QueryBuilders.termQuery(QueryConditionField.QUERY_CONDITION_FIELD_RECOMMENDATION_PLAN_RELEASE_STATUS, RecommendationPlanPo.DESIGN_PLAN_TEST_IS_PUBLISH));
            if (null != userId && 0 < userId) {
                testPlanBoole.must(QueryBuilders.termQuery(QueryConditionField.QUERY_CONDITION_FIELD_RECOMMENDATION_PLATFORM_CREATE_USER_ID, userId));
            }
            if (null != testPlanBoole) {
                boolQueryBuilder.should(testPlanBoole);
            }
        }
        searchRecommendedPlanBooleQueryBuilder.filter(boolQueryBuilder);

        //状态排序，审核，测试，一键方案
        sortOrderObjectList.add(new SortOrderObject(
                QueryConditionField.QUERY_CONDITION_FIELD_RECOMMENDATION_PLAN_RELEASE_STATUS,
                SortOrder.DESC,
                SortMode.MEDIAN,
                SortOrderObject.DEFAULT_SORT
        ));

        //适用空间面积排序
        Integer spaceAreas = recommendationPlanSearchVo.getSpaceArea();
        if (null != spaceAreas && spaceAreas > 0) {
            Script scriptSort = new Script(ScriptType.INLINE,"painless","doc['"+QueryConditionField.QUERY_CONDITION_FIELD_RECOMMENDATION_PLAN_SPACE_AREAS+"'].value == " + spaceAreas + "?0:(" + spaceAreas + "-doc['"+QueryConditionField.QUERY_CONDITION_FIELD_RECOMMENDATION_PLAN_SPACE_AREAS+"'].value > 0 ? (" + spaceAreas + "-doc['"+QueryConditionField.QUERY_CONDITION_FIELD_RECOMMENDATION_PLAN_SPACE_AREAS+"'].value):(" + spaceAreas + " + doc['"+QueryConditionField.QUERY_CONDITION_FIELD_RECOMMENDATION_PLAN_SPACE_AREAS+"'].value))", new HashMap<>());
            sortOrderObjectList.add(new SortOrderObject(scriptSort, SortOrder.ASC, SortOrderObject.SCRIPT_SORT));
        }

        //方案公共过滤条件 有多个排序条件故放置后排
        searchRecommendedPlanCommonCondtion(recommendationPlanSearchVo, searchRecommendedPlanBooleQueryBuilder, sortOrderObjectList);

        return searchRecommendedPlanBooleQueryBuilder;
    }

    /**
     * 一键方案、公开方案、样板间方案搜索条件
     *
     * @param recommendationPlanSearchVo
     * @param sortOrderObjectList
     */
    private BoolQueryBuilder searchDecorateOrOpenOrPrototypeRecommendationPlan(RecommendationPlanSearchVo recommendationPlanSearchVo, List<SortOrderObject> sortOrderObjectList) {

        //初始化
        BoolQueryBuilder searchRecommendedPlanBooleQueryBuilder = QueryBuilders.boolQuery();

        //基本过滤条件
        searchRecommendedPlanCommonCondtion(recommendationPlanSearchVo, searchRecommendedPlanBooleQueryBuilder, sortOrderObjectList);

        //平台类型 //随选网不需要平台过滤，查询的都是店铺发布的方案
        if (!PlatformConstant.PLATFORM_CODE_SELECT_DECORATION.equals(recommendationPlanSearchVo.getPlatformCode())){
            BoolQueryBuilder boolQueryBuildersOfPlatformCode = getBoolQueryBuildersOfPlatformCode(recommendationPlanSearchVo.getPlatformCode());
            if (null != boolQueryBuildersOfPlatformCode) {
                searchRecommendedPlanBooleQueryBuilder.filter(boolQueryBuildersOfPlatformCode);
            }
        }

        //店铺ID
        Integer shopId = recommendationPlanSearchVo.getShopId();
        if (null != shopId && 0 < shopId) {
            String planShopInfoList = QueryConditionField.QUERY_CONDITION_FIELD_RECOMMENDATION_PLANA_SHOP_INFO_LIST;
            QueryBuilder queryBuilder = QueryBuilders.nestedQuery(planShopInfoList,
                    QueryBuilders.boolQuery()
                            .must(QueryBuilders.matchQuery(planShopInfoList + "." + QueryConditionField.QUERY_CONDITION_FIELD_RECOMMENDATION_PLANA_SHOP_ID, shopId)),
                    ScoreMode.Max);
            searchRecommendedPlanBooleQueryBuilder.must(queryBuilder);
        }

        return searchRecommendedPlanBooleQueryBuilder;
    }

    /**
     * 测试方案和审核方案搜索条件
     *
     * @param recommendationPlanSearchVo
     * @param sortOrderObjectList
     */
    private BoolQueryBuilder searchTestOrPublishypeRecommendationPlan(RecommendationPlanSearchVo recommendationPlanSearchVo, List<SortOrderObject> sortOrderObjectList) {

        //初始化
        BoolQueryBuilder searchRecommendedPlanBooleQueryBuilder = QueryBuilders.boolQuery();

        //基本过滤条件
        searchRecommendedPlanCommonCondtion(recommendationPlanSearchVo, searchRecommendedPlanBooleQueryBuilder, sortOrderObjectList);

        //test方案、审核方案不需平台过滤，
        // 测试方案只查看自己的测试方
        Integer userId = recommendationPlanSearchVo.getUserId();
        if (null != userId && 0 < userId) {
            searchRecommendedPlanBooleQueryBuilder.filter(QueryBuilders.termQuery(QueryConditionField.QUERY_CONDITION_FIELD_RECOMMENDATION_PLATFORM_CREATE_USER_ID, userId));
        }

        return searchRecommendedPlanBooleQueryBuilder;
    }

    /**
     * 搜索推荐方案公共条件
     * @param recommendationPlanSearchVo
     * @param searchRecommendedPlanBooleQueryBuilder
     */
    private void searchRecommendedPlanCommonCondtion(RecommendationPlanSearchVo recommendationPlanSearchVo, BoolQueryBuilder searchRecommendedPlanBooleQueryBuilder, List<SortOrderObject> sortOrderObjectList) {

        String disPlayType = recommendationPlanSearchVo.getDisplayType();
        Integer companyId = recommendationPlanSearchVo.getCompanyId();
        //dragDecorate类型方案已处理公司信息过滤
        if (!RecommendationPlanPo.FUNCTION_TYPE_DRAGDECORATE.equals(disPlayType)) {
            //非厂商查询一键方案 本公司+三度公司 ; 其他类型只查本公司方案（设置条件时已判断）
            if (null != companyId && 0 < companyId) {
                BoolQueryBuilder companyInfoBoolQueryBuilder = getBoolQueryBuildersOfCompanyIdList(recommendationPlanSearchVo);
                if (null != companyInfoBoolQueryBuilder) {
                    searchRecommendedPlanBooleQueryBuilder.filter(companyInfoBoolQueryBuilder);
                }
            }

            //发布状态
            Integer releaseStatus = recommendationPlanSearchVo.getReleaseStatus();
            if (null != releaseStatus) {
                searchRecommendedPlanBooleQueryBuilder.filter(QueryBuilders.termQuery(QueryConditionField.QUERY_CONDITION_FIELD_RECOMMENDATION_PLAN_RELEASE_STATUS, releaseStatus));
            }
        }

        //空间类型
        Integer houseType = recommendationPlanSearchVo.getHouseType();
        if (null != houseType && 0 < houseType) {
            searchRecommendedPlanBooleQueryBuilder.filter(QueryBuilders.termQuery(QueryConditionField.QUERY_CONDITION_FIELD_RECOMMENDATION_PLAN_HOUSE_TYPE, houseType));
        }

        //风格
        Integer designStyleId = recommendationPlanSearchVo.getDesignStyleId();
        if (null != designStyleId && 0 < designStyleId) {
            searchRecommendedPlanBooleQueryBuilder.filter(QueryBuilders.termQuery(QueryConditionField.QUERY_CONDITION_FIELD_RECOMMENDATION_PLAN_DESIGN_STYLEID, designStyleId));
        }

        //推荐方案类型
        Integer recommendationPlanType = recommendationPlanSearchVo.getRecommendationPlanType();
        if (null != recommendationPlanType && 0 < recommendationPlanType) {
            searchRecommendedPlanBooleQueryBuilder.filter(QueryBuilders.termQuery(QueryConditionField.QUERY_CONDITION_FIELD_RECOMMENDATION_PLAN_RECOMMENDATIONPLAN_TYPE, recommendationPlanType));
        }

        //分配方案类型标识
        String shelfStatus = recommendationPlanSearchVo.getShelfStatus();
        Integer shopId = recommendationPlanSearchVo.getShopId();
        if (!StringUtils.isEmpty(shelfStatus) && (null == shopId || 0 == shopId)) {
            if (shelfStatus.contains(",")){
                String[] shelfStatusList = shelfStatus.split(",");
                searchRecommendedPlanBooleQueryBuilder.filter(QueryBuilders.termsQuery(
                        QueryConditionField.QUERY_CONDITION_FIELD_RECOMMENDATION_PLAN_SHELF_STATUS_LIST,
                        shelfStatusList));
            }else {
                searchRecommendedPlanBooleQueryBuilder.filter(QueryBuilders.termQuery(QueryConditionField.QUERY_CONDITION_FIELD_RECOMMENDATION_PLAN_SHELF_STATUS_LIST, shelfStatus));
            }
        }

        //空间面积
        Integer spaceAreas = recommendationPlanSearchVo.getSpaceArea();
        String platformCode = recommendationPlanSearchVo.getPlatformCode();
        String displayType = recommendationPlanSearchVo.getDisplayType();
        if (null != spaceAreas && spaceAreas > 0) {
            //样板房搜索一键方案 和 非pc2b一键方案需要使用面积过滤
            //推荐方案关联多个适用面积以后，在移动2B、微信小程序端搜索时，选着关联的面积，但是结果搜索不到相应的方案（大面积方案特别突出）
            if (RecommendationPlanPo.FUNCTION_TYPE_DRAGDECORATE.equals(displayType)
                    || (RecommendationPlanPo.FUNCTION_TYPE_DECORATE.equals(displayType)
                    && !PlatformConstant.PLATFORM_CODE_TOB_PC.equals(platformCode))) {
                //适用面积匹配
                BoolQueryBuilder spaceAreasBool = QueryBuilders.boolQuery();
                spaceAreasBool.should(QueryBuilders.termQuery(QueryConditionField.QUERY_CONDITION_FIELD_RECOMMENDATION_PLAN_APPLY_ALL_SPACE_AREAS_LIST, spaceAreas));
                //测试方案还没选面积，适用面积为空
                spaceAreasBool.should(QueryBuilders.boolQuery().mustNot(QueryBuilders.existsQuery(QueryConditionField.QUERY_CONDITION_FIELD_RECOMMENDATION_PLAN_APPLY_ALL_SPACE_AREAS_LIST)));
                searchRecommendedPlanBooleQueryBuilder.filter(spaceAreasBool);
            } else {
                searchRecommendedPlanBooleQueryBuilder.filter(QueryBuilders.termQuery(QueryConditionField.QUERY_CONDITION_FIELD_RECOMMENDATION_PLAN_SPACE_AREAS, spaceAreas));
            }
        }

        //方案来源
        List<String> planSourceList = recommendationPlanSearchVo.getPlanSource();
        if (null != planSourceList && 0 < planSourceList.size()) {
            searchRecommendedPlanBooleQueryBuilder.filter(QueryBuilders.termsQuery(QueryConditionField.QUERY_CONDITION_FIELD_RECOMMENDATION_PLAN_SOURCE, planSourceList));
        }

        //数据未被删除
        searchRecommendedPlanBooleQueryBuilder.filter(QueryBuilders.termQuery(QueryConditionField.QUERY_CONDITION_FIELD_RECOMMENDATION_PLAN_STATUS, 0));

        //组合查询条件--单值匹配多字段--List中第一个字段优先级最高
        getMultiMatchQueryBuilder(recommendationPlanSearchVo, searchRecommendedPlanBooleQueryBuilder);

        //方案打组后只查未打组和主方案(打组 把不同面积相似空间打成一组，groupPrimaryId记录组主键，存的推荐方案ID)
        //通过户型和样板房替换搜索一键方案（如果是打组方案匹配适用的子方案显示主方案，替换找适配的子方案）
        //sql ：group_primary_id = 0 or group_primary_id = dpr.id
        BoolQueryBuilder groupPrimaryBool = QueryBuilders.boolQuery();
        Script script = new Script(ScriptType.INLINE,"painless","doc['"+QueryConditionField.QUERY_CONDITION_FIELD_RECOMMENDATION_PLAN_ID+"'].value - doc['"+QueryConditionField.QUERY_CONDITION_FIELD_RECOMMENDATION_PLATFORM_GROUP_PRIMARY_ID+"'].value == 0", new HashMap<>());
        groupPrimaryBool.should(new ScriptQueryBuilder(script));
        groupPrimaryBool.should(QueryBuilders.termQuery(QueryConditionField.QUERY_CONDITION_FIELD_RECOMMENDATION_PLATFORM_GROUP_PRIMARY_ID, 0));
        searchRecommendedPlanBooleQueryBuilder.filter(groupPrimaryBool);

        //装修报价范围过滤
        Integer decoratePriceType = recommendationPlanSearchVo.getDecoratePriceType();
        Integer decoratePriceRenge = recommendationPlanSearchVo.getDecoratePriceRange();
        BoolQueryBuilder boolQueryBuildersOfDecoratePrice = getBoolQueryBuildersOfDecoratePrice(decoratePriceType, decoratePriceRenge);
        if (null != boolQueryBuildersOfDecoratePrice) {
            searchRecommendedPlanBooleQueryBuilder.filter(boolQueryBuildersOfDecoratePrice);
        }

        //发布时间排序
        int sortType = recommendationPlanSearchVo.getSortType();
        if (RecommendationPlanSearchVo.RELEASE_TIME_SORT_ASC == sortType) {
            //ASC
            sortOrderObjectList.add(new SortOrderObject(
                    QueryConditionField.QUERY_CONDITION_FIELD_RECOMMENDATION_PLAN_PUBLISH_TIME,
                    SortOrder.ASC,
                    SortMode.MEDIAN,
                    SortOrderObject.DEFAULT_SORT
            ));
        }  else {
            //DESC
            sortOrderObjectList.add(new SortOrderObject(
                    QueryConditionField.QUERY_CONDITION_FIELD_RECOMMENDATION_PLAN_PUBLISH_TIME,
                    SortOrder.DESC,
                    SortMode.MEDIAN,
                    SortOrderObject.DEFAULT_SORT
            ));
        }
    }

    /**
     * 一值多字段匹配
     * @param recommendationPlanSearchVo
     * @param boolQueryBuilder
     */
    private void getMultiMatchQueryBuilder(RecommendationPlanSearchVo recommendationPlanSearchVo, BoolQueryBuilder boolQueryBuilder) {
        List<MultiMatchField> multiMatchFieldList = recommendationPlanSearchVo.getMultiMatchFieldList();
        if (null != multiMatchFieldList && 0 < multiMatchFieldList.size()) {
            //遍历条件
            multiMatchFieldList.forEach(multiMatchField -> {
                //匹配字段
                List<String> matchFieldList = multiMatchField.getMatchFieldList();

                //设置查询条件
                MultiMatchQueryBuilder multiMatchQueryBuilder = QueryBuilders.multiMatchQuery(multiMatchField.getSearchKeyword(), matchFieldList.toArray(new String[matchFieldList.size()]));
                //设置字段优先级
                for (int i = 0; i < matchFieldList.size(); i++) {
                    //字段名
                    String filedName = matchFieldList.get(i);
                    //权重
                    float boost = (matchFieldList.size() - i);
                    //方案名称由于是强匹配,防止其他分值大于方案编码.故上调为5倍权重
                    if (QueryConditionField.QUERY_CONDITION_FIELD_RECOMMENDATION_PLAN_NAME.equals(filedName)) {
                        boost = boost * 10;
                    }
                    multiMatchQueryBuilder.field(filedName, boost);
                }
                //加入查询
                boolQueryBuilder.filter(multiMatchQueryBuilder);
            });
        }
    }

    @Override
    public RecommendationPlanIndexMappingData searchRecommendationPlanById(Integer recommendationPlanId) throws RecommendationPlanSearchException {

        if (null == recommendationPlanId || 0 >= recommendationPlanId) {
            return null;
        }
        //构造查询体
        QueryBuilder matchQueryBuilder = QueryBuilders.matchQuery(QueryConditionField.QUERY_CONDITION_FIELD_RECOMMENDATION_PLAN_ID, recommendationPlanId);

        //查询数据
        SearchObjectResponse searchObjectResponse;
        try {
            searchObjectResponse = elasticSearchService.search(Collections.singletonList(matchQueryBuilder), null, null, null, null, IndexInfoQueryConfig.DEFAULT_SEARCH_DATA_START, 1, IndexConstant.RECOMMENDATION_PLAN_ALIASES);
        } catch (ElasticSearchException e) {
            log.error(CLASS_LOG_PREFIX + "通过设计方案ID查询产设计方案失败，QueryBuilder:{}, ElasticSearchException:{}.", matchQueryBuilder.toString(), e);
            throw new RecommendationPlanSearchException(CLASS_LOG_PREFIX + "通过设计方案ID查询设计方案失败，QueryBuilder:" + matchQueryBuilder.toString() + ", ElasticSearchException:" + e);
        }

        if (null != searchObjectResponse) {
            List<RecommendationPlanIndexMappingData> recommendationPlanIndexMappingDataList = (List<RecommendationPlanIndexMappingData>) searchObjectResponse.getHitResult();
            if (null != recommendationPlanIndexMappingDataList && 0 < recommendationPlanIndexMappingDataList.size()) {
                return recommendationPlanIndexMappingDataList.get(0);
            }
        }

        return null;
    }

    /**
     * 获取平台编码布尔查询
     *
     * @param platformCode 平台编码
     * @return
     */
    private BoolQueryBuilder getBoolQueryBuildersOfPlatformCode(String platformCode) {

        BoolQueryBuilder boolQueryBuilder = null;

        if (!StringUtils.isEmpty(platformCode)) {
            //初始化查询
            boolQueryBuilder = new BoolQueryBuilder();
            //新增条件
            boolQueryBuilder.must(QueryBuilders.termQuery(QueryConditionField.QUERY_CONDITION_FIELD_RECOMMENDATION_PLATFORM_CODE_LIST, platformCode));

            //检查方案平台状态必须为已发布且已分配
            switch (platformCode) {
                //2B-移动端
                case PlatformConstant.PLATFORM_CODE_TOB_MOBILE:
                    boolQueryBuilder.must(QueryBuilders.termQuery(QueryConditionField.QUERY_CONDITION_FIELD_RECOMMENDATION_PLATFORM_TOB_MOBILE_PUTAWAT_STATUS
                                    + "." +
                                    QueryConditionField.QUERY_CONDITION_FIELD_RECOMMENDATION_PLATFORM_PUTAWAT_STATUS,
                            1
                    ));
                    boolQueryBuilder.must(QueryBuilders.termQuery(QueryConditionField.QUERY_CONDITION_FIELD_RECOMMENDATION_PLATFORM_TOB_MOBILE_PUTAWAT_STATUS
                                    + "." +
                                    QueryConditionField.QUERY_CONDITION_FIELD_RECOMMENDATION_PLATFORM_ALLOT_STATUS,
                            1
                    ));
                    break;
                //2B-PC
                case PlatformConstant.PLATFORM_CODE_TOB_PC:
                    boolQueryBuilder.must(QueryBuilders.termQuery(QueryConditionField.QUERY_CONDITION_FIELD_RECOMMENDATION_PLATFORM_TOB_PC_PUTAWAT_STATUS
                                    + "." +
                                    QueryConditionField.QUERY_CONDITION_FIELD_RECOMMENDATION_PLATFORM_PUTAWAT_STATUS,
                            1
                    ));
                    boolQueryBuilder.must(QueryBuilders.termQuery(QueryConditionField.QUERY_CONDITION_FIELD_RECOMMENDATION_PLATFORM_TOB_PC_PUTAWAT_STATUS
                                    + "." +
                                    QueryConditionField.QUERY_CONDITION_FIELD_RECOMMENDATION_PLATFORM_ALLOT_STATUS,
                            1
                    ));
                    break;
                //品牌2C-网站
                case PlatformConstant.PLATFORM_CODE_TOC_SITE:
                    boolQueryBuilder.must(QueryBuilders.termQuery(QueryConditionField.QUERY_CONDITION_FIELD_RECOMMENDATION_PLATFORM_TOC_SITE_PUTAWAT_STATUS
                                    + "." +
                                    QueryConditionField.QUERY_CONDITION_FIELD_RECOMMENDATION_PLATFORM_PUTAWAT_STATUS,
                            1
                    ));
                    boolQueryBuilder.must(QueryBuilders.termQuery(QueryConditionField.QUERY_CONDITION_FIELD_RECOMMENDATION_PLATFORM_TOC_SITE_PUTAWAT_STATUS
                                    + "." +
                                    QueryConditionField.QUERY_CONDITION_FIELD_RECOMMENDATION_PLATFORM_ALLOT_STATUS,
                            1
                    ));
                    break;
                //2C-移动端
                case PlatformConstant.PLATFORM_CODE_TOC_MOBILE:
                    boolQueryBuilder.must(QueryBuilders.termQuery(QueryConditionField.QUERY_CONDITION_FIELD_RECOMMENDATION_PLATFORM_TOC_MOBILE_PUTAWAT_STATUS
                                    + "." +
                                    QueryConditionField.QUERY_CONDITION_FIELD_RECOMMENDATION_PLATFORM_PUTAWAT_STATUS,
                            1
                    ));
                    boolQueryBuilder.must(QueryBuilders.termQuery(QueryConditionField.QUERY_CONDITION_FIELD_RECOMMENDATION_PLATFORM_TOC_MOBILE_PUTAWAT_STATUS
                                    + "." +
                                    QueryConditionField.QUERY_CONDITION_FIELD_RECOMMENDATION_PLATFORM_ALLOT_STATUS,
                            1
                    ));
                    break;
                //2C-小程序
                case PlatformConstant.PLATFORM_CODE_MINI_PROGRAM:
                    boolQueryBuilder.must(QueryBuilders.termQuery(QueryConditionField.QUERY_CONDITION_FIELD_RECOMMENDATION_PLATFORM_TOC_MINIPROGRAM_PUTAWAT_STATUS
                                    + "." +
                                    QueryConditionField.QUERY_CONDITION_FIELD_RECOMMENDATION_PLATFORM_PUTAWAT_STATUS,
                            1
                    ));
                    boolQueryBuilder.must(QueryBuilders.termQuery(QueryConditionField.QUERY_CONDITION_FIELD_RECOMMENDATION_PLATFORM_TOC_MINIPROGRAM_PUTAWAT_STATUS
                                    + "." +
                                    QueryConditionField.QUERY_CONDITION_FIELD_RECOMMENDATION_PLATFORM_ALLOT_STATUS,
                            1
                    ));
                    break;
                //2C-随选网
                case PlatformConstant.PLATFORM_CODE_SELECT_DECORATION:
                    boolQueryBuilder.must(QueryBuilders.termQuery(QueryConditionField.QUERY_CONDITION_FIELD_RECOMMENDATION_PLATFORM_TOC_SELECT_DECORATION_PUTAWAT_STATUS
                                    + "." +
                                    QueryConditionField.QUERY_CONDITION_FIELD_RECOMMENDATION_PLATFORM_PUTAWAT_STATUS,
                            1
                    ));
                    boolQueryBuilder.must(QueryBuilders.termQuery(QueryConditionField.QUERY_CONDITION_FIELD_RECOMMENDATION_PLATFORM_TOC_SELECT_DECORATION_PUTAWAT_STATUS
                                    + "." +
                                    QueryConditionField.QUERY_CONDITION_FIELD_RECOMMENDATION_PLATFORM_ALLOT_STATUS,
                            1
                    ));
                    break;
            }
        }

        return boolQueryBuilder;
    }

    /**
     * 获取装修报价类型范围值
     *
     * @param decoratePriceType  装修类型
     * @param decoratePriceRenge 装修报价范围
     * @return
     */
    private BoolQueryBuilder getBoolQueryBuildersOfDecoratePrice(Integer decoratePriceType, Integer decoratePriceRenge) {

        BoolQueryBuilder boolQueryBuilder = null;

        if (!StringUtils.isEmpty(decoratePriceType)) {
            //初始化查询
            boolQueryBuilder = new BoolQueryBuilder();
            //新增条件
            boolQueryBuilder.must(QueryBuilders.termQuery(QueryConditionField.QUERY_CONDITION_FIELD_RECOMMENDATION_DECORATE_PRICE_VALUE_LIST, decoratePriceType));

            //检查方案报价类型及价格范围
            if (!StringUtils.isEmpty(decoratePriceRenge)){
                switch (decoratePriceType) {
                    //半包
                    case SystemDictionaryType.SYSTEM_DICTIONARY_DECORATETYPE_VALUE_ONE:
                        boolQueryBuilder.must(QueryBuilders.termQuery(QueryConditionField.QUERY_CONDITION_FIELD_RECOMMENDATION_DECORATE_HALFPACK
                                + "." +
                                QueryConditionField.QUERY_CONDITION_FIELD_RECOMMENDATION_DECORATE_PRICE_RANGE_VALUE, decoratePriceRenge
                        ));
                        break;
                    //全包
                    case SystemDictionaryType.SYSTEM_DICTIONARY_DECORATETYPE_VALUE_TWO:
                        boolQueryBuilder.must(QueryBuilders.termQuery(QueryConditionField.QUERY_CONDITION_FIELD_RECOMMENDATION_DECORATE_ALLPACK
                                + "." +
                                QueryConditionField.QUERY_CONDITION_FIELD_RECOMMENDATION_DECORATE_PRICE_RANGE_VALUE, decoratePriceRenge
                        ));
                        break;
                    //包软装
                    case SystemDictionaryType.SYSTEM_DICTIONARY_DECORATETYPE_VALUE_THREE:
                        boolQueryBuilder.must(QueryBuilders.termQuery(QueryConditionField.QUERY_CONDITION_FIELD_RECOMMENDATION_DECORATE_PACKSOFT
                                + "." +
                                QueryConditionField.QUERY_CONDITION_FIELD_RECOMMENDATION_DECORATE_PRICE_RANGE_VALUE, decoratePriceRenge
                        ));
                        break;
                    //清水
                    case SystemDictionaryType.SYSTEM_DICTIONARY_DECORATETYPE_VALUE_FOUR:
                        boolQueryBuilder.must(QueryBuilders.termQuery(QueryConditionField.QUERY_CONDITION_FIELD_RECOMMENDATION_DECORATE_WATER
                                + "." +
                                QueryConditionField.QUERY_CONDITION_FIELD_RECOMMENDATION_DECORATE_PRICE_RANGE_VALUE, decoratePriceRenge
                        ));
                        break;
                }
            }
        }

        return boolQueryBuilder;
    }

    /**
     * 获取推荐方案企业相关信息查询条件
     * @param recommendationPlanSearchVo
     * @return
     */
    private BoolQueryBuilder getBoolQueryBuildersOfCompanyIdList(RecommendationPlanSearchVo recommendationPlanSearchVo) {

        //本公司+三度公司
        List<Integer> companyIdList = recommendationPlanSearchVo.getCompanyIdList();
        //本公司
        Integer companyId = recommendationPlanSearchVo.getCompanyId();
        //经销商品牌
        Map<String, String> brandIdMap = recommendationPlanSearchVo.getBrandIdMap();
        //当前companyBool
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

        if (null != companyIdList && 0 < companyIdList.size()) {
            companyIdList.forEach(cId -> {
                //当前companyBool
                BoolQueryBuilder companyBool = QueryBuilders.boolQuery();
                //公司过滤
                companyBool.must(QueryBuilders.termQuery(QueryConditionField.QUERY_CONDITION_FIELD_RECOMMENDATION_PLAN_COMPANY_ID_LIST, cId));
                //本公司显示前面,权重+大
                if (cId.equals(companyId)) {
                    companyBool.boost(2.0f);
                }

                //经销商品牌过滤
                if (null != brandIdMap && 0 < brandIdMap.size()) {
                    String brandIdStr = brandIdMap.get(cId + "");
                    if (!StringUtils.isEmpty(brandIdStr)) {
                        List<Integer> brandIdList = JsonUtil.fromJson(brandIdStr, new TypeToken<List<Integer>>() {}.getType());
                        if (null != brandIdList && 0 < brandIdList.size()) {
                            //当前brandBool
                            BoolQueryBuilder brandBool = QueryBuilders.boolQuery();
                            brandIdList.forEach(brandId -> brandBool.should(QueryBuilders.termQuery(QueryConditionField.QUERY_CONDITION_FIELD_RECOMMENDATION_PLAN_BRAND_ID_LIST, brandId)));
                            if (null != brandBool) {
                                companyBool.must(brandBool);
                            }
                        }
                    }
                }

                //三度公司过滤方案来源信息 注释原因：外部设计师可以交付给三度云享家
                /*if (companyMetaDataStorage.getSanduCompanyId() == cId) {
                    companyBool.mustNot(QueryBuilders.termsQuery(QueryConditionField.QUERY_CONDITION_FIELD_RECOMMENDATION_PLAN_SOURCE,
                            Arrays.asList(
                                    RecommendationPlanPo.SHARE_PLAN_SOURCE,
                                    RecommendationPlanPo.DELIVER_PLAN_SOURCE
                            )));

                }*/

                boolQueryBuilder.should(companyBool);
            });
        }

        return boolQueryBuilder;
    }


    @Override
    public MessageUtil paramVerify(String displayType, String platformCode) {

        String msg = "";
        //方案显示类型必填参数验证
        if (StringUtils.isEmpty(displayType)) {
            msg = CLASS_LOG_PREFIX + "搜索列表失败，必需参数为空, displayType is null.";
            log.warn(msg);
            return new MessageUtil(false, msg);
        }
        //平台正确性验证
        if (StringUtils.isEmpty(platformCode)
                || (!PlatformConstant.PLATFORM_CODE_TOB_PC.equals(platformCode)
                && !PlatformConstant.PLATFORM_CODE_TOB_MOBILE.equals(platformCode))
                && !PlatformConstant.PLATFORM_CODE_TOC_SITE.equals(platformCode)
                && !PlatformConstant.PLATFORM_CODE_MINI_PROGRAM.equals(platformCode)
                && !PlatformConstant.PLATFORM_CODE_SELECT_DECORATION.equals(platformCode)) {
            msg = CLASS_LOG_PREFIX + "平台标识无效:{} ";
            log.warn(msg, platformCode);
            return new MessageUtil(false, msg);
        }

        return new MessageUtil(true);
    }

    @Override
    public MessageUtil paramVerify(String platformCode, Integer spuId) {

        String msg = "";
        //方案显示类型必填参数验证
        if (null == spuId) {
            msg = CLASS_LOG_PREFIX + "Param is empty!";
            log.warn(msg);
            return new MessageUtil(false, msg);
        }
        //平台正确性验证
        if (StringUtils.isEmpty(platformCode)
                || (!PlatformConstant.PLATFORM_CODE_TOB_PC.equals(platformCode)
                && !PlatformConstant.PLATFORM_CODE_TOB_MOBILE.equals(platformCode))
                && !PlatformConstant.PLATFORM_CODE_TOC_SITE.equals(platformCode)
                && !PlatformConstant.PLATFORM_CODE_MINI_PROGRAM.equals(platformCode)
                && !PlatformConstant.PLATFORM_CODE_SELECT_DECORATION.equals(platformCode)) {
            msg = CLASS_LOG_PREFIX + "平台标识无效:{} ";
            log.warn(msg, platformCode);
            return new MessageUtil(false, msg);
        }

        return new MessageUtil(true);
    }


    @Override
    public Integer getCompanyInfo(String platformCode, LoginUser loginUser) {

        /*********************************** 公司信息 ******************************/
        Integer companyId = 0;
        if (PlatformConstant.PLATFORM_CODE_TOC_SITE.equals(platformCode)) {
            //品牌网站HOST解析
            String domainName = DomainUtil.getDomainNameByHost(request);
            log.info(CLASS_LOG_PREFIX + "公司域名-domainName:{}", domainName);

            if (StringUtils.isEmpty(domainName)) {
                log.info(CLASS_LOG_PREFIX + "品牌网站HOST解析失败 domainname is null!");
                return companyId;
            }
            //获取为空则直接获取公司--(小程序有公司但没有公司域名)
            companyId = companyMetaDataStorage.getCompanyIdByDomainName(domainName);
            log.info(CLASS_LOG_PREFIX + "Brand2C公司ID获取完成! DomainName:{}, CompanyId:{}.", domainName, companyId);

        } else if (PlatformConstant.PLATFORM_CODE_MINI_PROGRAM.equals(platformCode) ||
                PlatformConstant.PLATFORM_CODE_SELECT_DECORATION.equals(platformCode)) {
            //小程序获取公司ID
            companyId = domainUtil.getCompanyIdInMiniProgramByRequest(request);
            log.info(CLASS_LOG_PREFIX + "小程序获取公司ID为:{}.", companyId);

        } else if (PlatformConstant.PLATFORM_CODE_TOB_PC.equals(platformCode) ||
                PlatformConstant.PLATFORM_CODE_TOB_MOBILE.equals(platformCode)) {
            //获取公司ID
            int businessAdministrationId = loginUser.getBusinessAdministrationId();
            if (0 == businessAdministrationId) {
                SysUserPo userPo = sysUserMetaDataStorage.getUserPoByUserId(loginUser.getId());
                if (userPo != null) {
                    Integer franchiser = userPo.getBusinessAdministrationId();
                    if (franchiser == null || franchiser == 0) {
                        companyId = userPo.getCompanyId() == null ? 0 : userPo.getCompanyId();
                    } else {
                        companyId = franchiser;
                    }
                }
            } else {
                companyId = businessAdministrationId;
            }
            log.info(CLASS_LOG_PREFIX + "Pc2b获取公司ID为:{}.", companyId);
        }

        return companyId;
    }

    @Override
    public MessageUtil searchRecommendedCondition(RecommendationPlanSearchVo recommendationPlanSearchVo, LoginUser loginUser) {

        String platformCode = recommendationPlanSearchVo.getPlatformCode();
        Integer companyId = recommendationPlanSearchVo.getCompanyId();
        Integer dealersId = recommendationPlanSearchVo.getCompanyId();
        Integer designerId = recommendationPlanSearchVo.getUserId();
        String searchKeyword = recommendationPlanSearchVo.getSearchKeyword();
        String displayType = recommendationPlanSearchVo.getDisplayType();
        Integer houseType = recommendationPlanSearchVo.getHouseType();

        //TODO 设置企业 (非厂商经销商类型设置本公司Id和三度公司Id)

        if (null != companyId && 0 < companyId) {
            //缓存获取经销商获取厂商Id
            CompanyPo companyPo = companyMetaDataStorage.getCompanyPoByCompanyId(new Integer(companyId));
            //缓存为空从数据库获取
            if (null == companyPo || 0 == companyPo.getCompanyId()) {
                try {
                    companyPo = metaDataService.queryCompanyById(companyId);
                } catch (MetaDataException e) {
                    log.error(CLASS_LOG_PREFIX + "获取企业异常~ e:{}" + e);
                    return new MessageUtil(false, "获取企业异常！");
                }
            }
            //一键方案有此需求(非厂商经销商类型设置本公司Id和三度公司Id)
            List<Integer> companyIdList = new ArrayList<>(2);
            if (RecommendationPlanPo.FUNCTION_TYPE_DECORATE.equals(displayType)
                    || RecommendationPlanPo.FUNCTION_TYPE_DRAGDECORATE.equals(displayType)) {
                if (null != companyPo) {
                    if (CompanyTypeEnum.MANUFACTURER.getValue().toString().equals(companyPo.getBusinessType())
                            || CompanyTypeEnum.INDEPENDENT_DEALERS.getValue().toString().equals(companyPo.getBusinessType())) {
                        //厂商/独立经销商类型用户为试用账号、显示三度方案设置本公司Id和三度公司Id
                        companyIdList.add(companyId);
                        SysUserPo userPo = sysUserMetaDataStorage.getUserPoByUserId(designerId);
                        if (null != userPo && null != userPo.getUseType() && UserTypeConstant.USER_USE_TYPE_TRIAL == userPo.getUseType()
                                && null != userPo.getShowSanduPlan() && UserTypeConstant.USER_SHOW_SANDU_PLAN == userPo.getShowSanduPlan()) {
                            companyIdList.add(companyMetaDataStorage.getSanduCompanyId());
                        }
                    } else if (CompanyTypeEnum.FRANCHISER.getValue().toString().equals(companyPo.getBusinessType())) {
                        //经销商类型用户为试用账号、显示三度方案设置本公司Id和三度公司Id
                        companyId = companyPo.getCompanyPid();
                        companyIdList.add(companyPo.getCompanyPid());
                        SysUserPo userPo = sysUserMetaDataStorage.getUserPoByUserId(designerId);
                        if (null != userPo && null != userPo.getUseType() && UserTypeConstant.USER_USE_TYPE_TRIAL == userPo.getUseType()
                                && null != userPo.getShowSanduPlan() && UserTypeConstant.USER_SHOW_SANDU_PLAN == userPo.getShowSanduPlan()) {
                            companyIdList.add(companyMetaDataStorage.getSanduCompanyId());
                        }
                    } else {
                        companyIdList.add(companyId);
                        if (companyMetaDataStorage.getSanduCompanyId() != companyId) {
                            //非厂商经销商需查三度公司+本公司方案
                            companyIdList.add(companyMetaDataStorage.getSanduCompanyId());
                        }
                    }
                } else {
                    companyIdList.add(companyId);
                }
            } else {
                if (null != companyPo) {
                    if (CompanyTypeEnum.FRANCHISER.getValue().toString().equals(companyPo.getBusinessType())) {
                        companyId = companyPo.getCompanyPid();
                    }
                }
                companyIdList.add(companyId);
            }
            recommendationPlanSearchVo.setCompanyId(companyId);
            recommendationPlanSearchVo.setCompanyIdList(companyIdList);

        } else {
            log.info(CLASS_LOG_PREFIX + platformCode +"端用户企业为空! companyId={},userId={}", companyId, designerId);
        }

        //TODO 独立经销商、经销商用户方案需品牌品牌
        if (loginUser != null && (UserTypeConstant.USER_ROLE_TYPE_DEALER == loginUser.getUserType()
                || UserTypeConstant.USER_ROLE_TYPE_INDEPENDENT_DEALERS == loginUser.getUserType()) && null != companyId) {
            List<Integer> brandIdList = new ArrayList<>();
            CompanyPo companyPo = companyMetaDataStorage.getCompanyPoByCompanyId(dealersId);
            if (null != companyPo) {
                if (!StringUtils.isEmpty(companyPo.getDealerBrands())) {
                    brandIdList = StringUtil.transformInteger(Arrays.asList(companyPo.getDealerBrands().split(",")));
                }
                log.info(CLASS_LOG_PREFIX + "当前用户经销商{},品牌:{}.", dealersId, companyPo.getDealerBrands());
                if (null != brandIdList && 0 < brandIdList.size()) {
                    brandIdList.add(-1); //可以查看无品牌数据
                    Map<String, String> brandIdMap = new HashMap<>(1);
                    if (0 != companyPo.getCompanyPid()) {
                        brandIdMap.put(companyPo.getCompanyPid() + "", JsonUtil.toJson(brandIdList));
                    } else {
                        brandIdMap.put(dealersId + "", JsonUtil.toJson(brandIdList));
                    }
                    recommendationPlanSearchVo.setBrandIdMap(brandIdMap);
                }
            }
        }

        //小程序可用品牌方案过滤
        if (PlatformConstant.PLATFORM_CODE_MINI_PROGRAM.equals(platformCode) || PlatformConstant.PLATFORM_CODE_SELECT_DECORATION.equals(platformCode)) {
            Map<String, String> brandIdMap = new HashMap<>(1);
            String appId = domainUtil.getAppIdFromMiniProgramByRequest(request);
            //处理小程序逻辑(WEIXIN-121,Update by steve);
            List<Integer> brandIdList = brandMetaDataStorage.getEnableBrandIdsByAppId(appId);
            if (null != brandIdList && 0 < brandIdList.size()) {
                brandIdMap.put(companyId + "", JsonUtil.toJson(brandIdList));
            } else {
                // modified by zhangchengda  没有设置品牌时不需要这段逻辑
                /*brandIdList = brandMetaDataStorage.queryBrandIdListByCompanyIdList(Arrays.asList(companyId));
                if (null != brandIdList && 0 < brandIdList.size()) {
                    brandIdMap.put(companyId + "", JsonUtil.toJson(brandIdList));
                }*/
            }
            recommendationPlanSearchVo.setBrandIdMap(brandIdMap);
        }

        //TODO 获取不同方案类型列表条件设置
        //样板方案
        if (RecommendationPlanPo.FUNCTION_TYPE_PROTOTYPE.equals(displayType)) {
            //推荐方案类型//已发布状态方案
            recommendationPlanSearchVo.setRecommendationPlanType(RecommendationPlanPo.SHARE_RECOMMENDATION_PLAN_TYPE);
            recommendationPlanSearchVo.setReleaseStatus(RecommendationPlanPo.DESIGN_PLAN_IS_PUBLISH);
            recommendationPlanSearchVo.setShelfStatus(RecommendationPlanPo.TEMPLATE_PLAN_SIGN);
        }
        // 一键方案列表
        if (RecommendationPlanPo.FUNCTION_TYPE_DECORATE.equals(displayType)) {
            //推荐方案类型//已发布状态方案
            recommendationPlanSearchVo.setRecommendationPlanType(RecommendationPlanPo.ONEKEY_RECOMMENDATION_PLAN_TYPE);
            recommendationPlanSearchVo.setReleaseStatus(RecommendationPlanPo.DESIGN_PLAN_IS_PUBLISH);
            //非PC端匹配
            if (Objects.equals(PlatformConstant.PLATFORM_CODE_TOB_PC, platformCode)) {
                recommendationPlanSearchVo.setShelfStatus(RecommendationPlanPo.ONEKEY_PLAN_SIGN);
            }
        }
        //公开方案
        if (RecommendationPlanPo.FUNCTION_TYPE_PUBLIC.equals(displayType)) {
            //推荐方案类型//已发布状态方案
            recommendationPlanSearchVo.setRecommendationPlanType(RecommendationPlanPo.ONEKEY_RECOMMENDATION_PLAN_TYPE);
            recommendationPlanSearchVo.setReleaseStatus(RecommendationPlanPo.DESIGN_PLAN_IS_PUBLISH);
            recommendationPlanSearchVo.setShelfStatus(RecommendationPlanPo.OPEN_PLAN_SIGN);
        }
        // 测试方案列表
        if (RecommendationPlanPo.FUNCTION_TYPE_TEST.equals(displayType)) {
            //测试发布状态方案
            recommendationPlanSearchVo.setReleaseStatus(RecommendationPlanPo.DESIGN_PLAN_TEST_IS_PUBLISH);
        }
        // 审核方案列表
        if (RecommendationPlanPo.FUNCTION_TYPE_PUBLISH.equals(displayType)) {
            //测试发布状态方案
            recommendationPlanSearchVo.setReleaseStatus(RecommendationPlanPo.DESIGN_PLAN_AUDIT_IS_PUBLISH);
        }
        // 样板房搜索一键方案列表
        if (RecommendationPlanPo.FUNCTION_TYPE_DRAGDECORATE.equals(displayType)) {
            //已发布状态方案
            recommendationPlanSearchVo.setRecommendationPlanType(RecommendationPlanPo.ONEKEY_RECOMMENDATION_PLAN_TYPE);
            recommendationPlanSearchVo.setReleaseStatus(RecommendationPlanPo.DESIGN_PLAN_IS_PUBLISH);
            //用户菜单权限
            List<String> roleCodeList = loginUser.getRoleCodeList();
            //获取空间类型key
            String dicValuekey = systemDictionaryMetaDataStorage.getSystemDictionaryValueKeyByTypeAndValue(SystemDictionaryType.SYSTEM_DICTIONARY_TYPE_HOUSETYPE, houseType).toUpperCase();
            if (null != roleCodeList && 0 < roleCodeList.size()) {
                //该空间类型的审核管理员权限
                if (roleCodeList.contains(MenuAuthorityCode.RECOMMENDEDCHECK_PREFIX + dicValuekey)) {
                    recommendationPlanSearchVo.setSpaceTypeAuditPermission(true);
                    //是否有查看“审核方案”菜单项的权限
                    List<String> auditPlanRoleList = sysRoleFuncMetaDataStorage.getRoleListByFuncCode(MenuAuthorityCode.PLANRECOMMENDEDLIST_CHECK);
                    if (null != auditPlanRoleList && 0 < auditPlanRoleList.size()) {
                        for (String auditPlanRole : auditPlanRoleList) {
                            if (roleCodeList.contains(auditPlanRole)) {
                                log.info(CLASS_LOG_PREFIX + "{}有审核管理员权限", loginUser.getId());
                                recommendationPlanSearchVo.setAuditPlanMenuPermission(true);
                                break;
                            }
                        }
                    }
                    //是否有查看“测试方案”菜单项的权限
                    List<String> testPlankRoleList = sysRoleFuncMetaDataStorage.getRoleListByFuncCode(MenuAuthorityCode.PLANRECOMMENDEDLIST_TEST);
                    if (null != testPlankRoleList && 0 < testPlankRoleList.size()) {
                        for (String testPlanRole : testPlankRoleList) {
                            if (roleCodeList.contains(testPlanRole)) {
                                log.info(CLASS_LOG_PREFIX + "{}有测试方案权限", loginUser.getId());
                                recommendationPlanSearchVo.setTestPlanMenuPermission(true);
                                break;
                            }
                        }
                    }
                }
            }
        }

        //小程序单独处理逻辑,公司为三度云享家展示平台所有diy的公开和一键方案
        if (PlatformConstant.PLATFORM_CODE_MINI_PROGRAM.equals(platformCode) || PlatformConstant.PLATFORM_CODE_SELECT_DECORATION.equals(platformCode)) {
            CompanyPo companyPo = companyMetaDataStorage.getCompanyPoByCompanyId(new Integer(companyId));
            if (null != companyPo && companyMetaDataStorage.getSanduCompanyId() == companyId) {
                recommendationPlanSearchVo.setPlanSource(
                        Arrays.asList(
                                RecommendationPlanPo.DIY_PLAN_SOURCE
                        ));
                recommendationPlanSearchVo.setShelfStatus(null);
                recommendationPlanSearchVo.setBrandIdMap(null);
                recommendationPlanSearchVo.setCompanyIdList(null);
                recommendationPlanSearchVo.setCompanyId(null);
            }
            if ("shop".equals(recommendationPlanSearchVo.getEnterType())){
                recommendationPlanSearchVo.setBrandIdMap(null);
            }
        }

        //TODO 单值多字段匹配
        List<MultiMatchField> multiMatchFieldList = new ArrayList<>();

        //构造方案搜索对象--搜索关键字(方案名称>方案编码>创建人>品牌名称>小区名称)
        if (!StringUtils.isEmpty(searchKeyword)) {
            searchKeyword = searchKeyword.toLowerCase();
            List<String> fidleNameList = Arrays.asList(
                    QueryConditionField.QUERY_CONDITION_FIELD_RECOMMENDATION_PLAN_CODE,
                    QueryConditionField.QUERY_CONDITION_FIELD_RECOMMENDATION_PLAN_NAME,
                    QueryConditionField.QUERY_CONDITION_FIELD_RECOMMENDATION_PLAN_CREATE_USER_NAME,
                    QueryConditionField.QUERY_CONDITION_FIELD_RECOMMENDATION_PLAN_BRAND_NAME,
                    QueryConditionField.QUERY_CONDITION_FIELD_RECOMMENDATION_PLAN_LIVING_NAME);
            multiMatchFieldList.add(new MultiMatchField(searchKeyword, fidleNameList));
            //装回对象
            recommendationPlanSearchVo.setMultiMatchFieldList(multiMatchFieldList);
        }

        return new MessageUtil(true);
    }

    @Override
    public List<RecommendationPlanVo> dispostRecommendPlanResultList(List<RecommendationPlanIndexMappingData> recommendationPlanIndexMappingDataList, Integer userId, String platformCode, Integer companyId) {

        Long startTime = System.currentTimeMillis();

        List<RecommendationPlanVo> recommendationPlanVoList = null;
        if (null != recommendationPlanIndexMappingDataList && 0 < recommendationPlanIndexMappingDataList.size()) {
            //初始化
            recommendationPlanVoList = new ArrayList<>(recommendationPlanIndexMappingDataList.size());
            List<Integer> singleSpaceIdList = new ArrayList<>();
            List<Integer> fullHouseIdList = new ArrayList<>();
            Map<Integer,String> planCompanyMap = new HashMap<>();
            List<PlanTypeInfoPo> planIdList = new ArrayList<>(recommendationPlanIndexMappingDataList.size());

            //转换对象
            for (RecommendationPlanIndexMappingData planIndexMappingData : recommendationPlanIndexMappingDataList) {

                RecommendationPlanVo recommendationPlanVo = new RecommendationPlanVo();
                //复制对象
                //EntityCopyUtils.copyData(recommendationPlanIndexMappingData, recommendationPlanVo);
                RecommendationPlanConvert.parseToPlanVoByPlanIndexMappingData(planIndexMappingData, recommendationPlanVo);
                //装入对象
                recommendationPlanVoList.add(recommendationPlanVo);

                PlanTypeInfoPo planBuyInfoPo = new PlanTypeInfoPo();
                //id集合
                if(null != planIndexMappingData.getFullHouseId() && DesignPlanType.FULLHOUSE_TABLE_TYPE == planIndexMappingData.getPlanTableType().intValue()){
                    //全屋id集合
                    fullHouseIdList.add(planIndexMappingData.getFullHouseId());
                    planBuyInfoPo.setPlanId(planIndexMappingData.getFullHouseId());
                    planBuyInfoPo.setPlanType(planIndexMappingData.getPlanTableType());
                    // 随选网走新的点赞收藏
                    if ((PlatformConstant.PLATFORM_CODE_SELECT_DECORATION.equals(platformCode)) ||
                            (PlatformConstant.PLATFORM_CODE_MINI_PROGRAM.equals(platformCode) &&
                            companyId.equals(2501))) {
                        Map<String, Object> map = nodeInfoBizService.getNodeData(planIndexMappingData.getFullHouseId(),
                                NodeInfoConstant.SYSTEM_DICTIONARY_NODE_TYPE_FULL_HOUSE,
                                userId);

                        recommendationPlanVo.setIsLike(map.get("setIsLike") == null ? 0 : (Integer)map.get("setIsLike"));
                        recommendationPlanVo.setIsFavorite(map.get("setIsFavorite") == null ? 0 : (Integer)map.get("setIsFavorite"));
                        recommendationPlanVo.setCollectNum((map.get("setFavoriteNum") == null ? 0 : (Integer)map.get("setFavoriteNum"))
                                + (map.get("setVirtualFavoriteNum") == null ? 0 : (Integer)map.get("setVirtualFavoriteNum")));
                        recommendationPlanVo.setLikeNum((map.get("setLikeNum") == null ? 0 : (Integer)map.get("setLikeNum"))
                                + (map.get("setVirtualLikeNum") == null ? 0 : (Integer)map.get("setVirtualLikeNum")));
                        recommendationPlanVo.setVisitCount((map.get("setViewNum") == null ? 0 : (Integer)map.get("setViewNum"))
                                + (map.get("setVirtualViewNum") == null ? 0 : (Integer)map.get("setVirtualViewNum")));
                    }
                }else if (null != planIndexMappingData.getId() && DesignPlanType.RECOMMENDED_TABLE_TYPE == planIndexMappingData.getPlanTableType().intValue()){
                    //推荐方案Id集合
                    singleSpaceIdList.add(planIndexMappingData.getId());
                    planBuyInfoPo.setPlanId(planIndexMappingData.getId());
                    planBuyInfoPo.setPlanType(planIndexMappingData.getPlanTableType());
                    // 随选网走新的点赞收藏
                    if ((PlatformConstant.PLATFORM_CODE_SELECT_DECORATION.equals(platformCode)) ||
                            (PlatformConstant.PLATFORM_CODE_MINI_PROGRAM.equals(platformCode) &&
                                    companyId.equals(2501))) {
                        Map<String, Object> map = nodeInfoBizService.getNodeData(planIndexMappingData.getId(),
                                NodeInfoConstant.SYSTEM_DICTIONARY_NODE_TYPE_RECOMMENDED,
                                userId);

                        recommendationPlanVo.setIsLike(map.get("setIsLike") == null ? 0 : (Integer)map.get("setIsLike"));
                        recommendationPlanVo.setIsFavorite(map.get("setIsFavorite") == null ? 0 : (Integer)map.get("setIsFavorite"));
                        recommendationPlanVo.setCollectNum((map.get("setFavoriteNum") == null ? 0 : (Integer)map.get("setFavoriteNum"))
                                + (map.get("setVirtualFavoriteNum") == null ? 0 : (Integer)map.get("setVirtualFavoriteNum")));
                        recommendationPlanVo.setLikeNum((map.get("setLikeNum") == null ? 0 : (Integer)map.get("setLikeNum"))
                                + (map.get("setVirtualLikeNum") == null ? 0 : (Integer)map.get("setVirtualLikeNum")));
                        recommendationPlanVo.setVisitCount((map.get("setViewNum") == null ? 0 : (Integer)map.get("setViewNum"))
                                + (map.get("setVirtualViewNum") == null ? 0 : (Integer)map.get("setVirtualViewNum")));
                    }
                }
                if (null != planBuyInfoPo && null != planBuyInfoPo.getPlanId()) {
                    planIdList.add(planBuyInfoPo);
                }

                //企业集合
                List<Integer> companyIdList = planIndexMappingData.getCompanyIdList();
                StringBuffer companyIds = new StringBuffer();
                if (null != companyIdList && companyIdList.size() > 0) {
                    for (Integer integer : companyIdList) {
                        companyIds.append(integer+",");
                    }
                    if (DesignPlanType.RECOMMENDED_TABLE_TYPE == planIndexMappingData.getPlanTableType()) {
                        planCompanyMap.put(planIndexMappingData.getId(), companyIds.toString());
                    } else if (DesignPlanType.FULLHOUSE_TABLE_TYPE == planIndexMappingData.getPlanTableType()) {
                        planCompanyMap.put(planIndexMappingData.getFullHouseId(), companyIds.toString());
                    }
                }
            }

            //收藏方案，如果不是随选网的话走旧的点赞收藏
            List<RecommendedPlanFavoritePo> favoritePoList = new ArrayList<>(10);
            if (!PlatformConstant.PLATFORM_CODE_SELECT_DECORATION.equals(platformCode)) {
                try {
                    favoritePoList = metaDataService.getRecommendationPlanCollectBidMetaDataByIds(singleSpaceIdList, fullHouseIdList, userId);
                } catch (Exception e) {
                    log.error(CLASS_LOG_PREFIX + "查询方案收藏数据异常!");

                }
            }

            //TODO 方案收费二期优化 planBuyMap已购买数据
            Map<String, PlanTypeInfoPo> planBuyMap = null;
            List<PlanTypeInfoPo> planBuyInfoList = userDesignPlanPurchaseRecordService.getPlanBuyInfo(planIdList, userId);
            if(null != planBuyInfoList && 0 < planBuyInfoList.size()) {
                planBuyMap = new HashMap<>(planBuyInfoList.size());
               for (PlanTypeInfoPo buyInfo : planBuyInfoList) {
                    planBuyMap.put(buyInfo.getPlanId() + "_"+buyInfo.getPlanType(), buyInfo);
                }
            }

            //获取用户公司ID
            //String userCompanyId = userDesignPlanPurchaseRecordService.getUserCompanyId(userId) + "";

            for (RecommendationPlanVo recommendationPlanVo : recommendationPlanVoList) {
                //方案版权和购买(update by xiaoxc-20181113 共享方案在商家后台使用购买)
                if (null != planCompanyMap && planCompanyMap.size() > 0) {
                    String planCompanyIds = planCompanyMap.get(recommendationPlanVo.getPlanRecommendedId());
                    //B端：收费、非本公司需要版权，其他免费 C端：收费显示版权
                    if (null != recommendationPlanVo.getChargeType()
                            && DesignPlanType.RECOMMENDED_PLAN_PRICE_TYPE_ONE == recommendationPlanVo.getChargeType()) {
                        //企业小程序和随选网
                        if (Objects.equals(PlatformConstant.PLATFORM_CODE_SELECT_DECORATION, platformCode)
                            || Objects.equals(PlatformConstant.PLATFORM_CODE_MINI_PROGRAM, platformCode)) {
                            recommendationPlanVo.setCopyRightPermission(DesignPlanType.RECOMMENDED_PLAN_NEED_COPYRIGHT);
                        } else {
                            if (!StringUtils.isEmpty(planCompanyIds) && !planCompanyIds.contains(String.valueOf(companyId))
                                    && !planCompanyIds.contains(String.valueOf(companyMetaDataStorage.getSanduCompanyId()))) {
                                recommendationPlanVo.setCopyRightPermission(DesignPlanType.RECOMMENDED_PLAN_NEED_COPYRIGHT);
                            } else {
                                recommendationPlanVo.setCopyRightPermission(DesignPlanType.RECOMMENDED_PLAN_NO_NEED_COPYRIGHT);
                            }
                        }
                    } else {
                        recommendationPlanVo.setCopyRightPermission(DesignPlanType.RECOMMENDED_PLAN_NO_NEED_COPYRIGHT);
                    }
                }

                //方案是否购买：收费状态0：未购买、1：已购买、
                if (null != planBuyMap && 0 < planBuyMap.size()) {
                    if (planBuyMap.containsKey(recommendationPlanVo.getPlanRecommendedId() + "_" + recommendationPlanVo.getPlanTableType())) {
                        recommendationPlanVo.setHavePurchased(DesignPlanType.RECOMMENDED_PLAN_IS_HAVEPURCHASED_ONE);
                    } else {
                        recommendationPlanVo.setHavePurchased(DesignPlanType.RECOMMENDED_PLAN_IS_HAVEPURCHASED_ZEOR);
                    }
                } else {
                    recommendationPlanVo.setHavePurchased(DesignPlanType.RECOMMENDED_PLAN_IS_HAVEPURCHASED_ZEOR);
                }

                //封装点赞收藏数，如果不是随选网的话走旧的点赞收藏
                if (!(PlatformConstant.PLATFORM_CODE_SELECT_DECORATION.equals(platformCode)) &&
                        !(PlatformConstant.PLATFORM_CODE_MINI_PROGRAM.equals(platformCode) &&
                                companyId.equals(2501))) {
                    if (null != favoritePoList && favoritePoList.size() > 0) {
                        for (RecommendedPlanFavoritePo recommendedPlanFavoritePo : favoritePoList) {
                            //单空间方案
                            if (DesignPlanType.RECOMMENDED_TABLE_TYPE == recommendedPlanFavoritePo.getPlanHouseType()) {
                                if (recommendationPlanVo.getPlanRecommendedId().intValue() == recommendedPlanFavoritePo.getPlanRecommendedId()) {
                                    recommendationPlanVo.setIsFavorite(recommendedPlanFavoritePo.getIsFavorite());
                                    recommendationPlanVo.setCollectStatus(recommendedPlanFavoritePo.getIsFavorite());
                                    recommendationPlanVo.setIsLike(recommendedPlanFavoritePo.getIsLike());
                                    recommendationPlanVo.setLikeNum(recommendedPlanFavoritePo.getLikeCount());
                                    recommendationPlanVo.setCollectNum(recommendedPlanFavoritePo.getCollectCount());
                                    recommendationPlanVo.setVisitCount(recommendedPlanFavoritePo.getViewCount());
                                    recommendationPlanVo.setBid(recommendedPlanFavoritePo.getBid() == null ? "0" : recommendedPlanFavoritePo.getBid());
                                }
                                //
                            } else if (DesignPlanType.FULLHOUSE_TABLE_TYPE == recommendedPlanFavoritePo.getPlanHouseType()) {
                                if (recommendationPlanVo.getPlanRecommendedId().intValue() == recommendedPlanFavoritePo.getFullHouseId()) {
                                    recommendationPlanVo.setCollectStatus(recommendedPlanFavoritePo.getIsFavorite());
                                    recommendationPlanVo.setIsFavorite(recommendedPlanFavoritePo.getIsFavorite());
                                    recommendationPlanVo.setIsLike(recommendedPlanFavoritePo.getIsLike());
                                    recommendationPlanVo.setLikeNum(recommendedPlanFavoritePo.getLikeCount());
                                    recommendationPlanVo.setCollectNum(recommendedPlanFavoritePo.getCollectCount());
                                    recommendationPlanVo.setVisitCount(recommendedPlanFavoritePo.getViewCount());
                                    recommendationPlanVo.setBid(recommendedPlanFavoritePo.getBid() == null ? "0" : recommendedPlanFavoritePo.getBid());
                                }
                            }
                        }
                    }
                }
            }
        }

        log.info("响应结果集处理耗时：{}", System.currentTimeMillis() - startTime);

        return recommendationPlanVoList;
    }


    @Override
    public SearchObjectResponse searchGoodDesign(RecommendationPlanSearchVo recommendationPlanSearchVo, PageVo pageVo) throws RecommendationPlanSearchException {

        if (null == recommendationPlanSearchVo) {
            log.warn(CLASS_LOG_PREFIX + "通过条件搜索设计方案失败，必需参数为空.");
            throw new RecommendationPlanSearchException(CLASS_LOG_PREFIX + "通过条件搜索设计方案失败，必需参数为空.");
        }

        /********************************* Step 1: 组装搜索条件 *************************************************/
        //匹配条件List
        List<QueryBuilder> matchQueryList = new ArrayList<>();
        //非匹配条件List
        List<QueryBuilder> noMatchQueryList = new ArrayList<>(1);
        //或匹配条件List
        List<ShouldMatchSearch> shouldMatchSearchList = new ArrayList<>();
        //排序对象
        List<SortOrderObject> sortOrderObjectList = new ArrayList<>();
        //
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();

        //产品
        List<Integer> productIdList = recommendationPlanSearchVo.getProductIdList();
        if (null != productIdList && 0 < productIdList.size()) {
            boolQueryBuilder.filter(QueryBuilders.termsQuery(QueryConditionField.QUERY_CONDITION_FIELD_RECOMMENDATION_PLANA_PRODUCT_ID_LIST, productIdList));
        }

        //发布状态
        Integer releaseStatus = recommendationPlanSearchVo.getReleaseStatus();
        if (null != releaseStatus) {
            boolQueryBuilder.filter(QueryBuilders.termQuery(QueryConditionField.QUERY_CONDITION_FIELD_RECOMMENDATION_PLAN_RELEASE_STATUS, releaseStatus));
        }

        //方案类型
        List<Integer> typeList = recommendationPlanSearchVo.getRecommendationPlanTypeList();
        if (null != typeList && 0 < typeList.size()) {
            boolQueryBuilder.filter(QueryBuilders.termsQuery(QueryConditionField.QUERY_CONDITION_FIELD_RECOMMENDATION_PLAN_RECOMMENDATIONPLAN_TYPE, typeList));
        }

        //企业信息
        Integer companyId = recommendationPlanSearchVo.getCompanyId();
        if (null != companyId && 0 < companyId) {
            BoolQueryBuilder companyInfoBoolQueryBuilder = getBoolQueryBuildersOfCompanyIdList(recommendationPlanSearchVo);
            if (null != companyInfoBoolQueryBuilder) {
                boolQueryBuilder.filter(companyInfoBoolQueryBuilder);
            }
        }

        // 随选网专用
        if (recommendationPlanSearchVo.getPlatformCode() != null && PlatformConstant.PLATFORM_CODE_SELECT_DECORATION.equals(recommendationPlanSearchVo.getPlatformCode())){
            // 方案来源
            boolQueryBuilder.filter(QueryBuilders.termQuery(QueryConditionField.QUERY_CONDITION_FIELD_RECOMMENDATION_PLAN_SOURCE, RecommendationPlanPo.DIY_PLAN_SOURCE));
        }

        //方案打组后只查未打组和主方案(打组 把不同面积相似空间打成一组，groupPrimaryId记录组主键，存的推荐方案ID)
        //通过户型和样板房替换搜索一键方案（如果是打组方案匹配适用的子方案显示主方案，替换找适配的子方案）
        //sql ：group_primary_id = 0 or group_primary_id = dpr.id
        BoolQueryBuilder groupPrimaryBool = QueryBuilders.boolQuery();
        Script script = new Script(ScriptType.INLINE,"painless","doc['"+QueryConditionField.QUERY_CONDITION_FIELD_RECOMMENDATION_PLAN_ID+"'].value - doc['"+QueryConditionField.QUERY_CONDITION_FIELD_RECOMMENDATION_PLATFORM_GROUP_PRIMARY_ID+"'].value == 0", new HashMap<>());
        groupPrimaryBool.should(new ScriptQueryBuilder(script));
        groupPrimaryBool.should(QueryBuilders.termQuery(QueryConditionField.QUERY_CONDITION_FIELD_RECOMMENDATION_PLATFORM_GROUP_PRIMARY_ID, 0));
        boolQueryBuilder.filter(groupPrimaryBool);

        //平台过滤
        BoolQueryBuilder boolQueryBuildersOfPlatformCode = getBoolQueryBuildersOfPlatformCode(recommendationPlanSearchVo.getPlatformCode());
        if (null != boolQueryBuildersOfPlatformCode) {
            boolQueryBuilder.filter(boolQueryBuildersOfPlatformCode);
        }

        //数据未被删除
        boolQueryBuilder.filter(QueryBuilders.termQuery(QueryConditionField.QUERY_CONDITION_FIELD_RECOMMENDATION_PLAN_STATUS, 0));

        //发布时间排序DESC
        sortOrderObjectList.add(new SortOrderObject(
                QueryConditionField.QUERY_CONDITION_FIELD_RECOMMENDATION_PLAN_PUBLISH_TIME,
                SortOrder.DESC,
                SortMode.MEDIAN,
                SortOrderObject.DEFAULT_SORT
        ));

        matchQueryList.add(boolQueryBuilder);

        /********************************* Step 2: 搜索数据 *************************************************/
        //搜索数据
        SearchObjectResponse searchObjectResponse;
        try {
            searchObjectResponse = elasticSearchService.search(matchQueryList, noMatchQueryList, shouldMatchSearchList, null, sortOrderObjectList, pageVo.getStart(), pageVo.getPageSize(), IndexConstant.RECOMMENDATION_PLAN_ALIASES);
        } catch (ElasticSearchException e) {
            log.error(CLASS_LOG_PREFIX + "通过条件搜索设计方案失败! ElasticSearchException:{}.", e);
            throw new RecommendationPlanSearchException(CLASS_LOG_PREFIX + "通过条件搜索设计方案失败! ElasticSearchException:{}.", e);
        }
        log.info(CLASS_LOG_PREFIX + "通过条件搜索设计方案列表成功!SearchObjectResponse:{}.", searchObjectResponse.getHitTotal());

        if (null == searchObjectResponse.getHitResult()) {
            log.info(CLASS_LOG_PREFIX + "通过条件搜索设计方案失败,查询结果为空。RecommendationPlanVo:{}.", recommendationPlanSearchVo.toString());
            throw new RecommendationPlanSearchException(CLASS_LOG_PREFIX + "通过条件搜索设计方案列表失败,查询结果为空。RecommendationPlanVo:" + recommendationPlanSearchVo.toString());
        }

        return searchObjectResponse;
    }


    @Override
    public SearchObjectResponse searchRecommendationPlanByIds(List<Integer> recommendedPlanIdList, List<Integer> fullHousePlanIdList, RecommendationPlanSearchVo recommendedPlanVo, PageVo pageVo) throws RecommendationPlanSearchException {

        if ((null == recommendedPlanIdList || 0 == recommendedPlanIdList.size())
                && (null == fullHousePlanIdList || 0 == fullHousePlanIdList.size())) {
            log.warn(CLASS_LOG_PREFIX + "通过条件搜索推荐方案失败，必需参数为空.");
            throw new RecommendationPlanSearchException(CLASS_LOG_PREFIX + "通过条件搜索设计方案失败，必需参数为空.");
        }

        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
        /********************************* Step 1: 组装搜索条件 *************************************************/
        //匹配条件List
        List<QueryBuilder> matchQueryList = new ArrayList<>();
        Integer houseType = recommendedPlanVo.getHouseType();

        BoolQueryBuilder recommendedPlanQueryBuilder = null;
        if (null != recommendedPlanIdList && 0 < recommendedPlanIdList.size()) {
            recommendedPlanQueryBuilder = QueryBuilders.boolQuery();
            recommendedPlanQueryBuilder.filter(QueryBuilders.termsQuery(QueryConditionField.QUERY_CONDITION_FIELD_RECOMMENDATION_PLAN_ID, recommendedPlanIdList));
            //空间面积
            Integer spaceAreas = recommendedPlanVo.getSpaceArea();
            if (null != spaceAreas && spaceAreas > 0) {
                // 适用面积匹配
                BoolQueryBuilder spaceAreasBool = QueryBuilders.boolQuery();
                spaceAreasBool.should(QueryBuilders.termQuery(QueryConditionField.QUERY_CONDITION_FIELD_RECOMMENDATION_PLAN_APPLY_ALL_SPACE_AREAS_LIST, spaceAreas));
                //方案还没选面积，适用面积为空
                spaceAreasBool.should(QueryBuilders.boolQuery().mustNot(QueryBuilders.existsQuery(QueryConditionField.QUERY_CONDITION_FIELD_RECOMMENDATION_PLAN_APPLY_ALL_SPACE_AREAS_LIST)));
                recommendedPlanQueryBuilder.filter(spaceAreasBool);
            }
            //空间类型
            if (null != houseType && houseType > 0) {
                recommendedPlanQueryBuilder.filter(QueryBuilders.termQuery(QueryConditionField.QUERY_CONDITION_FIELD_RECOMMENDATION_PLAN_HOUSE_TYPE, houseType));
            }
        }

        BoolQueryBuilder fullHouseQueryBuilder = null;
        if (null != fullHousePlanIdList && 0 < fullHousePlanIdList.size()) {
            fullHouseQueryBuilder = QueryBuilders.boolQuery();
            fullHouseQueryBuilder.filter(QueryBuilders.termsQuery(QueryConditionField.QUERY_CONDITION_FIELD_FULL_HOUSE_ID, fullHousePlanIdList));
            //空间类型
            fullHouseQueryBuilder.filter(QueryBuilders.termQuery(QueryConditionField.QUERY_CONDITION_FIELD_RECOMMENDATION_PLAN_HOUSE_TYPE, SystemDictionaryType.SYSTEM_DICTIONARY_HOUSETYPE_FULLROOM_VALUE));
        }

        BoolQueryBuilder planIdQueryBuilder = new BoolQueryBuilder();
        if (null != recommendedPlanQueryBuilder && null != fullHouseQueryBuilder) {
            planIdQueryBuilder.should(recommendedPlanQueryBuilder);
            planIdQueryBuilder.should(fullHouseQueryBuilder);
            boolQueryBuilder.filter(planIdQueryBuilder);
        } else if (null != recommendedPlanQueryBuilder) {
            boolQueryBuilder.filter(recommendedPlanQueryBuilder);
        } else if (null != fullHouseQueryBuilder) {
            boolQueryBuilder.filter(fullHouseQueryBuilder);
        } else {
            log.error(CLASS_LOG_PREFIX + "组装DSL语句失败！");
            throw new RecommendationPlanSearchException(CLASS_LOG_PREFIX + "组装DSL语句失败！");
        }

        //风格
        Integer designStyleId = recommendedPlanVo.getDesignStyleId();
        if (null != designStyleId && 0 < designStyleId) {
            boolQueryBuilder.filter(QueryBuilders.termQuery(QueryConditionField.QUERY_CONDITION_FIELD_RECOMMENDATION_PLAN_DESIGN_STYLEID, designStyleId));
        }

        //方案是否被删除
        boolQueryBuilder.filter(QueryBuilders.termQuery(QueryConditionField.QUERY_CONDITION_FIELD_RECOMMENDATION_PLAN_STATUS, 0));

        //装修报价范围过滤
        Integer decoratePriceType = recommendedPlanVo.getDecoratePriceType();
        Integer decoratePriceRenge = recommendedPlanVo.getDecoratePriceRange();
        BoolQueryBuilder boolQueryBuildersOfDecoratePrice = getBoolQueryBuildersOfDecoratePrice(decoratePriceType, decoratePriceRenge);
        if (null != boolQueryBuildersOfDecoratePrice) {
            boolQueryBuilder.filter(boolQueryBuildersOfDecoratePrice);
        }

        matchQueryList.add(boolQueryBuilder);
        /********************************* Step 2: 搜索数据 *************************************************/
        //搜索数据
        SearchObjectResponse searchObjectResponse;
        try {
            searchObjectResponse = elasticSearchService.search(matchQueryList, null, null, null, null, pageVo.getStart(), pageVo.getPageSize(), IndexConstant.RECOMMENDATION_PLAN_ALIASES);
        } catch (ElasticSearchException e) {
            log.error(CLASS_LOG_PREFIX + "通过条件搜索推荐方案失败! ElasticSearchException:{}.", e);
            throw new RecommendationPlanSearchException(CLASS_LOG_PREFIX + "通过条件搜索设计方案失败! ElasticSearchException:{}.", e);
        }
        log.info(CLASS_LOG_PREFIX + "通过条件搜索推荐方案列表成功!SearchObjectResponse:{}.", searchObjectResponse.getHitTotal());

        if (null == searchObjectResponse.getHitResult()) {
            log.info(CLASS_LOG_PREFIX + "通过条件搜索推荐方案失败,查询结果为空。recommendedPlanIdList:{}, fullHousePlanIdList.", JsonUtil.toJson(recommendedPlanIdList), JsonUtil.toJson(fullHousePlanIdList));
            throw new RecommendationPlanSearchException(CLASS_LOG_PREFIX + "通过条件搜索推荐方案列表失败,查询结果为空。recommendedPlanIdList:{}." + JsonUtil.toJson(recommendedPlanIdList) + ",fullHousePlanIdList:{}" + JsonUtil.toJson(fullHousePlanIdList));
        }

        return searchObjectResponse;
    }

    @Override
    public SearchObjectResponse searchCompanyShopPlanRel(RecommendationPlanSearchVo shopRecommendedPlanVo, PageVo pageVo) throws RecommendationPlanSearchException {

        if (null == shopRecommendedPlanVo) {
            log.warn(CLASS_LOG_PREFIX + "通过条件搜索店铺方案关联方案失败，必需参数为空.");
            throw new RecommendationPlanSearchException(CLASS_LOG_PREFIX + "通过条件搜索设计方案失败，必需参数为空.");
        }

        /********************************* Step 1: 组装搜索条件 *************************************************/
        //匹配条件List
        List<QueryBuilder> matchQueryList = new ArrayList<>();
        //排序对象
        List<SortOrderObject> sortOrderObjectList = new ArrayList<>();
        //
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();

        //方案打组后只查未打组和主方案(打组 把不同面积相似空间打成一组，groupPrimaryId记录组主键，存的推荐方案ID)
        //通过户型和样板房替换搜索一键方案（如果是打组方案匹配适用的子方案显示主方案，替换找适配的子方案）
        //sql ：group_primary_id = 0 or group_primary_id = dpr.id
        BoolQueryBuilder groupPrimaryBool = QueryBuilders.boolQuery();
        Script script = new Script(ScriptType.INLINE,"painless","doc['"+QueryConditionField.QUERY_CONDITION_FIELD_RECOMMENDATION_PLAN_ID+"'].value - doc['"+QueryConditionField.QUERY_CONDITION_FIELD_RECOMMENDATION_PLATFORM_GROUP_PRIMARY_ID+"'].value == 0", new HashMap<>());
        groupPrimaryBool.should(new ScriptQueryBuilder(script));
        groupPrimaryBool.should(QueryBuilders.termQuery(QueryConditionField.QUERY_CONDITION_FIELD_RECOMMENDATION_PLATFORM_GROUP_PRIMARY_ID, 0));
        boolQueryBuilder.filter(groupPrimaryBool);

//        BoolQueryBuilder orderingBool = QueryBuilders.boolQuery()
//                                    .should(QueryBuilders
//                                            .rangeQuery(QueryConditionField.QUERY_CONDITION_FIELD_ORDERING).gt(Integer.valueOf(0)));
//        orderingBool.boost(10.0f);
//
//        boolQueryBuilder.filter(orderingBool);
        //空间类型
        Integer houseType = shopRecommendedPlanVo.getHouseType();
        if (null != houseType && 0 < houseType) {
            boolQueryBuilder.filter(QueryBuilders.termQuery(QueryConditionField.QUERY_CONDITION_FIELD_RECOMMENDATION_PLAN_HOUSE_TYPE, houseType));
        }

        //风格
        Integer designStyleId = shopRecommendedPlanVo.getDesignStyleId();
        if (null != designStyleId && 0 < designStyleId) {
            boolQueryBuilder.filter(QueryBuilders.termQuery(QueryConditionField.QUERY_CONDITION_FIELD_RECOMMENDATION_PLAN_DESIGN_STYLEID, designStyleId));
        }

        //空间面积
        Integer spaceAreas = shopRecommendedPlanVo.getSpaceArea();
        if (null != spaceAreas && spaceAreas > 0) {
            //适用面积匹配
            BoolQueryBuilder spaceAreasBool = QueryBuilders.boolQuery();
            spaceAreasBool.should(QueryBuilders.termQuery(QueryConditionField.QUERY_CONDITION_FIELD_RECOMMENDATION_PLAN_APPLY_ALL_SPACE_AREAS_LIST, spaceAreas));
            //测试方案还没选面积，适用面积为空
            spaceAreasBool.should(QueryBuilders.boolQuery().mustNot(QueryBuilders.existsQuery(QueryConditionField.QUERY_CONDITION_FIELD_RECOMMENDATION_PLAN_APPLY_ALL_SPACE_AREAS_LIST)));
            boolQueryBuilder.filter(spaceAreasBool);
        }

        //组合查询条件--单值匹配多字段--List中第一个字段优先级最高
        getMultiMatchQueryBuilder(shopRecommendedPlanVo, boolQueryBuilder);


        //装修报价范围过滤
        Integer decoratePriceType = shopRecommendedPlanVo.getDecoratePriceType();
        Integer decoratePriceRenge = shopRecommendedPlanVo.getDecoratePriceRange();
        BoolQueryBuilder boolQueryBuildersOfDecoratePrice = getBoolQueryBuildersOfDecoratePrice(decoratePriceType, decoratePriceRenge);
        if (null != boolQueryBuildersOfDecoratePrice) {
            boolQueryBuilder.filter(boolQueryBuildersOfDecoratePrice);
        }

        //数据未被删除
        boolQueryBuilder.filter(QueryBuilders.termQuery(QueryConditionField.QUERY_CONDITION_FIELD_RECOMMENDATION_PLAN_STATUS, 0));

        //随选网专用，店铺详情里方案不过滤
        Integer shopId = shopRecommendedPlanVo.getShopId();
        if ((null == shopId || shopId == 0) && PlatformConstant.PLATFORM_CODE_SELECT_DECORATION.equals(shopRecommendedPlanVo.getPlatformCode())){
            // 方案来源
            boolQueryBuilder.filter(QueryBuilders.termQuery(QueryConditionField.QUERY_CONDITION_FIELD_RECOMMENDATION_PLAN_SOURCE, RecommendationPlanPo.DIY_PLAN_SOURCE));
        }

        //嵌套查询 方案店铺信息(存储结构为多个对象)
        String planShopInfoList = QueryConditionField.QUERY_CONDITION_FIELD_RECOMMENDATION_PLANA_SHOP_INFO_LIST;
        Integer shopPlatformType = shopRecommendedPlanVo.getShopPlatformType();
        BoolQueryBuilder nestedBool = QueryBuilders.boolQuery();
        //随选网方案列表查询所有店铺方案
        if (null != shopPlatformType && shopPlatformType > 0) {
            nestedBool.must(QueryBuilders.matchQuery(planShopInfoList + "." + QueryConditionField.QUERY_CONDITION_FIELD_SHOP_RELEASE_PLATFORM_LIST, shopPlatformType));
        }
        //店铺详情用到
        if (null != shopId && shopId > 0) {
            nestedBool.must(QueryBuilders.matchQuery(planShopInfoList + "." + QueryConditionField.QUERY_CONDITION_FIELD_RECOMMENDATION_PLANA_SHOP_ID, shopId));
        }

        if (RecommendationPlanPo.FUNCTION_TYPE_DECORATE.equals(shopRecommendedPlanVo.getDisplayType())) {
            //一键方案
            if (null != shopRecommendedPlanVo.getHouseType() && 0 < shopRecommendedPlanVo.getHouseType()) {
                //全屋方案
                if (SystemDictionaryType.SYSTEM_DICTIONARY_HOUSETYPE_FULLROOM_VALUE == shopRecommendedPlanVo.getHouseType()) {
                    nestedBool.must(QueryBuilders.matchQuery(planShopInfoList + "." + QueryConditionField.QUERY_CONDITION_FIELD_SHOP_PLANA_TYPE, DesignPlanType.SHOP_PLAN_FULLHOUSE_TYPE));
                } else {
                    //推荐方案
                    nestedBool.must(QueryBuilders.matchQuery(planShopInfoList + "." + QueryConditionField.QUERY_CONDITION_FIELD_SHOP_PLANA_TYPE, DesignPlanType.SHOP_PLAN_RECOMMENDED_TYPE));
                }
            } else {
                //推荐方案和全屋方案
                BoolQueryBuilder planTypeQueryBuilder = new BoolQueryBuilder();
                planTypeQueryBuilder.should(QueryBuilders.termQuery(planShopInfoList + "." + QueryConditionField.QUERY_CONDITION_FIELD_SHOP_PLANA_TYPE, DesignPlanType.SHOP_PLAN_FULLHOUSE_TYPE));
                planTypeQueryBuilder.should(QueryBuilders.termQuery(planShopInfoList + "." + QueryConditionField.QUERY_CONDITION_FIELD_SHOP_PLANA_TYPE, DesignPlanType.SHOP_PLAN_RECOMMENDED_TYPE));
                nestedBool.must(planTypeQueryBuilder);
            }

        }
        if (RecommendationPlanPo.FUNCTION_TYPE_PROTOTYPE.equals(shopRecommendedPlanVo.getDisplayType())) {
            nestedBool.must(QueryBuilders.matchQuery(planShopInfoList + "." + QueryConditionField.QUERY_CONDITION_FIELD_SHOP_PLANA_TYPE, DesignPlanType.SHOP_PLAN_TEMPLET_TYPE));
        }
        QueryBuilder queryBuilder = QueryBuilders.nestedQuery(planShopInfoList,
                nestedBool,
                ScoreMode.Max);
        boolQueryBuilder.must(queryBuilder);

        //店铺发布方案时间排序DESC
        sortOrderObjectList.add(
                new SortOrderObject(
                        QueryConditionField.QUERY_CONDITION_FIELD_ORDERING,
                        SortOrder.DESC,
                        SortMode.MAX,
                        SortOrderObject.DEFAULT_SORT
                )
        );
        sortOrderObjectList.add(new SortOrderObject(
                planShopInfoList + "." + QueryConditionField.QUERY_CONDITION_FIELD_SHOP_RELEASE_TIME,
                SortOrder.DESC,
                SortMode.MAX,
                SortOrderObject.NESTED_SORT,
                planShopInfoList,
                nestedBool
        ));

        matchQueryList.add(boolQueryBuilder);

        /********************************* Step 2: 搜索数据 *************************************************/
        //搜索数据
        SearchObjectResponse searchObjectResponse;
        try {
            searchObjectResponse = elasticSearchService.search(matchQueryList, null, null, null, sortOrderObjectList, pageVo.getStart(), pageVo.getPageSize(), IndexConstant.RECOMMENDATION_PLAN_ALIASES);
        } catch (ElasticSearchException e) {
            log.error(CLASS_LOG_PREFIX + "通过条件搜索设计方案失败! ElasticSearchException:{}.", e);
            throw new RecommendationPlanSearchException(CLASS_LOG_PREFIX + "通过条件搜索设计方案失败! ElasticSearchException:{}.", e);
        }
        log.info(CLASS_LOG_PREFIX + "通过条件搜索设计方案列表成功!SearchObjectResponse:{}.", searchObjectResponse.getHitTotal());

        if (null == searchObjectResponse.getHitResult()) {
            log.info(CLASS_LOG_PREFIX + "通过条件搜索设计方案失败,查询结果为空。RecommendationPlanVo:{}.", shopRecommendedPlanVo.toString());
            throw new RecommendationPlanSearchException(CLASS_LOG_PREFIX + "通过条件搜索设计方案列表失败,查询结果为空。shopRecommendedPlanVo:" + shopRecommendedPlanVo.toString());
        }

        return searchObjectResponse;
    }


    @Override
    public List<Integer> queryCollectRecommendedPlanList(RecommendationPlanSearchVo collectRecommendedVo) throws RecommendationPlanSearchException {
        if (null == collectRecommendedVo
                || null == collectRecommendedVo.getUserId()
                || null == collectRecommendedVo.getCompanyId()
                || null == collectRecommendedVo.getPlatformCode()) {
            return null;
        }

        //企业小程序查本公司+三度公司 随选网不过滤
        /*if (PlatformConstant.PLATFORM_CODE_MINI_PROGRAM.equals(collectRecommendedVo.getPlatformCode())) {
            collectRecommendedVo.setCompanyIdList(Arrays.asList(collectRecommendedVo.getCompanyId()));
            collectRecommendedVo.setCompanyIdList(Arrays.asList(companyMetaDataStorage.getSanduCompanyId()));
        }*/
        //查询推荐方案信息
        log.info(CLASS_LOG_PREFIX + "正在查询收藏推荐方案列表,collectRecommendedVo:{}.", collectRecommendedVo.toString());
        List<Integer> recommendationPlanIdList;
        try {
            recommendationPlanIdList = designPlanIndexDao.queryCollectRecommendedPlanList(collectRecommendedVo);
        } catch (Exception e) {
            log.error(CLASS_LOG_PREFIX + "获取收藏推荐方案ID失败,collectRecommendedVo:{}, Exception:{}.", collectRecommendedVo.toString(), e);
            throw new RecommendationPlanSearchException(CLASS_LOG_PREFIX + "获取推荐方案信息失败,Exception:" + e);
        }

        log.info(CLASS_LOG_PREFIX + "查询收藏推荐方案Id完成,List<Integer>长度:{}.", recommendationPlanIdList.size());

        return recommendationPlanIdList;
    }

    @Override
    public List<Integer> queryCollectFullHousePlanList(RecommendationPlanSearchVo collectRecommendedVo) throws RecommendationPlanSearchException {
        if (null == collectRecommendedVo
                || null == collectRecommendedVo.getUserId()
                || null == collectRecommendedVo.getCompanyId()) {
            return null;
        }

        //查询推荐方案信息
        log.info(CLASS_LOG_PREFIX + "正在查询收藏全屋方案列表,collectRecommendedVo:{}.", collectRecommendedVo.toString());
        List<Integer> fullHousePlanIdList;
        try {
            fullHousePlanIdList = designPlanIndexDao.queryCollectFullHousePlanList(collectRecommendedVo);
        } catch (Exception e) {
            log.error(CLASS_LOG_PREFIX + "获取收藏全屋方案UUID失败,collectRecommendedVo:{}, Exception:{}.", collectRecommendedVo.toString(), e);
            throw new RecommendationPlanSearchException(CLASS_LOG_PREFIX + "获取全屋方案信息失败,Exception:" + e);
        }
        log.info(CLASS_LOG_PREFIX + "查询收藏全屋方案Id完成,List<Integer>长度:{}.", fullHousePlanIdList.size());

        return fullHousePlanIdList;
    }

    @Override
    public int queryCollectRecommendedPlanCount(RecommendationPlanSearchVo collectRecommendedVo) {

        return designPlanIndexDao.getFavoriteCount(collectRecommendedVo);
    }

    @Override
    public List<String> getAllSuperiorPlan() {
        return designPlanIndexDao.selectAllSuperiorPlanIds();
    }

    @Override
    public SearchObjectResponse getTopDesignPlanRecommendInfo(RecommendationPlanSearchVo recommendationPlanVo) {
        List<QueryBuilder> matchQueryList = new ArrayList<>();
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();

        boolQueryBuilder.filter(QueryBuilders.rangeQuery(QueryConditionField.QUERY_CONDITION_FIELD_ORDERING).gt(Integer.valueOf(0)));

//        if(recommendationPlanVo != null ) {
//            Integer spaceType = recommendationPlanVo.getHouseType();
//            if(spaceType != null) {
//                boolQueryBuilder.filter(QueryBuilders.termQuery(QueryConditionField.QUERY_CONDITION_FIELD_RECOMMENDATION_PLAN_HOUSE_TYPE, spaceType));
//            }
//        }
        matchQueryList.add(boolQueryBuilder);
        List<SortOrderObject> sortOrderObjectList = new ArrayList<>();
        sortOrderObjectList.add(new SortOrderObject(
                QueryConditionField.QUERY_CONDITION_FIELD_RECOMMENDATION_PLAN_HOUSE_TYPE,
                SortOrder.DESC,
                SortMode.MAX,
                SortOrderObject.DEFAULT_SORT
        ));

        sortOrderObjectList.add(new SortOrderObject(
                QueryConditionField.QUERY_CONDITION_FIELD_ORDERING,
                SortOrder.DESC,
                SortMode.MAX,
                SortOrderObject.DEFAULT_SORT
        ));
        SearchObjectResponse searchObjectResponse = null;
        try {
            searchObjectResponse = elasticSearchService.search(matchQueryList, null, null, null, sortOrderObjectList, 0, 500, IndexConstant.RECOMMENDATION_PLAN_ALIASES);
        } catch (ElasticSearchException e) {
            e.printStackTrace();
        }
        return searchObjectResponse;
    }

}

