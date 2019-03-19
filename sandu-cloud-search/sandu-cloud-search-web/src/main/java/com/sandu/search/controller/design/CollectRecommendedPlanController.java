package com.sandu.search.controller.design;

import com.sandu.common.LoginContext;
import com.sandu.node.constant.NodeInfoConstant;
import com.sandu.search.common.constant.DesignPlanType;
import com.sandu.search.common.constant.IndexInfoQueryConfig;
import com.sandu.search.common.constant.SystemDictionaryType;
import com.sandu.search.common.tools.JsonUtil;
import com.sandu.search.common.tools.MessageUtil;
import com.sandu.search.common.tools.RequestHeaderUtil;
import com.sandu.search.entity.common.PageVo;
import com.sandu.search.entity.designplan.vo.RecommendationPlanSearchVo;
import com.sandu.search.entity.designplan.vo.RecommendationPlanVo;
import com.sandu.search.entity.elasticsearch.index.RecommendationPlanIndexMappingData;
import com.sandu.search.entity.elasticsearch.response.SearchObjectResponse;
import com.sandu.search.entity.response.RecommendationPlanCondition;
import com.sandu.search.entity.response.universal.UniversalSearchResultResponse;
import com.sandu.search.entity.user.LoginUser;
import com.sandu.search.exception.RecommendationPlanSearchException;
import com.sandu.search.service.design.RecommendationPlanSearchService;
import com.sandu.search.service.design.RecommendedPlanService;
import com.sandu.system.service.NodeInfoBizService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;

/**
 * 收藏推荐方案 控制层
 *
 * @auth xiaoxc
 * @data 20180929
 */
@Slf4j
@RestController
@EnableAutoConfiguration
@RequestMapping("/collect/recommendationplan/search")
public class CollectRecommendedPlanController {

    private final static String CLASS_LOG_PREFIX = "收藏推荐方案列表:";
    private final HttpServletRequest request;
    private final RecommendationPlanSearchService recommendationPlanSearchService;
    private final RecommendedPlanService recommendedPlanService;
    private final NodeInfoBizService nodeInfoBizService;


    @Autowired
    public CollectRecommendedPlanController(RecommendedPlanService recommendedPlanService,
                                            HttpServletRequest request,
                                            RecommendationPlanSearchService recommendationPlanSearchService,
                                            NodeInfoBizService nodeInfoBizService) {
        this.request = request;
        this.recommendationPlanSearchService = recommendationPlanSearchService;
        this.recommendedPlanService = recommendedPlanService;
        this.nodeInfoBizService = nodeInfoBizService;
    }


    @RequestMapping("/list")
    UniversalSearchResultResponse getCollectRecommendedPlanList(@RequestBody RecommendationPlanCondition planCondition) {

        //条件对象
        RecommendationPlanSearchVo recommendationPlanSearchVo = planCondition.getRecommendationPlanSearchVo();
        if (null == recommendationPlanSearchVo) {
            log.warn(CLASS_LOG_PREFIX + "搜索收藏方案失败，必需参数为空,models is null.");
            return new UniversalSearchResultResponse(false, "Param is empty!");
        }

        //分页对象
        PageVo pageVo = planCondition.getPageVo();
        /********************* 构造分页对象 *********************/
        if (null == pageVo) {
            pageVo = new PageVo();
        }
        if (0 == pageVo.getPageSize()) {
            pageVo.setPageSize(IndexInfoQueryConfig.DEFAULT_SEARCH_DATA_SIZE);
        }

        if(pageVo.getStart()==1){
            pageVo.setStart(pageVo.getStart()-1);
        }
        //参数校验 这里只校验平台编码
        String platformCode = RequestHeaderUtil.getPlatformIdByRequest(request);
        MessageUtil messageUtil = recommendationPlanSearchService.paramVerify("a", platformCode);
        if (!messageUtil.isStauts()) {
            return new UniversalSearchResultResponse(false, messageUtil.getMessage());
        } else {
            recommendationPlanSearchVo.setPlatformCode(platformCode);
        }
        //登录信息
        LoginUser loginUser = LoginContext.getLoginUser(LoginUser.class);
        if (null != loginUser) {
            recommendationPlanSearchVo.setUserId(loginUser.getId());
        } else {
            return new UniversalSearchResultResponse(false, "请登录！");
        }
        //获取企业
        Integer companyId = recommendationPlanSearchService.getCompanyInfo(platformCode, loginUser);
        if (null == companyId || companyId == 0) {
            return new UniversalSearchResultResponse(false, "数据异常！");
        }
        recommendationPlanSearchVo.setCompanyId(companyId);

        //手动同步缓存数据到数据库update by weis 2018.10.31
        MessageUtil message = recommendedPlanService.syncRecommendedPlanFavorite();
        if (null != message && !message.isStauts()) {
            log.error(CLASS_LOG_PREFIX + message.getMessage());
        }

        //从数据库查询方案点赞收藏数据
        List<Integer> recommendedPlanIdList = null;
        List<Integer> fullHousePlanIdList = null;
        //收藏列表装修我家接口
        /*if (Objects.equals(DesignPlanType.COLLECT_PLAN_DECORATE_TYPE, recommendationPlanSearchVo.getEnterType())) {
            try {
                recommendedPlanIdList = recommendationPlanSearchService.queryCollectRecommendedPlanList(recommendationPlanSearchVo);
                fullHousePlanIdList = recommendationPlanSearchService.queryCollectFullHousePlanList(recommendationPlanSearchVo);
            } catch (RecommendationPlanSearchException e) {
                log.error(CLASS_LOG_PREFIX + "获取收藏推荐方案数据异常，Exception:{}", e);
                return new UniversalSearchResultResponse(false, "数据异常！");
            }
        } else {*/
        // 随选网走新的收藏逻辑
        if ("miniProgram".equals(platformCode) && companyId.equals(2501)) {
            if (null == recommendationPlanSearchVo.getHouseType() || 0 == recommendationPlanSearchVo.getHouseType()) {
                fullHousePlanIdList = nodeInfoBizService.getContentIdList(loginUser.getId(), NodeInfoConstant.SYSTEM_DICTIONARY_NODE_TYPE_FULL_HOUSE, NodeInfoConstant.SYSTEM_DICTIONARY_DETAIL_TYPE_FAVORITE);
                recommendedPlanIdList = nodeInfoBizService.getContentIdList(loginUser.getId(), NodeInfoConstant.SYSTEM_DICTIONARY_NODE_TYPE_RECOMMENDED, NodeInfoConstant.SYSTEM_DICTIONARY_DETAIL_TYPE_FAVORITE);
            } else if (null != recommendationPlanSearchVo.getHouseType()
                    && SystemDictionaryType.SYSTEM_DICTIONARY_HOUSETYPE_FULLROOM_VALUE == recommendationPlanSearchVo.getHouseType()) {
                fullHousePlanIdList = nodeInfoBizService.getContentIdList(loginUser.getId(), NodeInfoConstant.SYSTEM_DICTIONARY_NODE_TYPE_FULL_HOUSE, NodeInfoConstant.SYSTEM_DICTIONARY_DETAIL_TYPE_FAVORITE);
            } else if (null != recommendationPlanSearchVo.getHouseType() && 0 != recommendationPlanSearchVo.getHouseType()) {
                recommendedPlanIdList = nodeInfoBizService.getContentIdList(loginUser.getId(), NodeInfoConstant.SYSTEM_DICTIONARY_NODE_TYPE_RECOMMENDED, NodeInfoConstant.SYSTEM_DICTIONARY_DETAIL_TYPE_FAVORITE);
            } else {
                log.info(CLASS_LOG_PREFIX + "未传户型类型！");
            }
        } else {
            if (null != recommendationPlanSearchVo.getHouseType()
                    && SystemDictionaryType.SYSTEM_DICTIONARY_HOUSETYPE_FULLROOM_VALUE == recommendationPlanSearchVo.getHouseType()) {
                try {
                    fullHousePlanIdList = recommendationPlanSearchService.queryCollectFullHousePlanList(recommendationPlanSearchVo);
                } catch (RecommendationPlanSearchException e) {
                    log.error(CLASS_LOG_PREFIX + "获取收藏全屋方案数据异常，Exception:{}", e);
                    return new UniversalSearchResultResponse(false, "数据异常！");
                }
            } else {
                try {
                    recommendedPlanIdList = recommendationPlanSearchService.queryCollectRecommendedPlanList(recommendationPlanSearchVo);
                } catch (RecommendationPlanSearchException e) {
                    log.error(CLASS_LOG_PREFIX + "获取收藏推荐方案数据异常，Exception:{}", e);
                    return new UniversalSearchResultResponse(false, "数据异常！");
                }
            }
        }
        //}
        if ((null == recommendedPlanIdList || 0 == recommendedPlanIdList.size())
                && (null == fullHousePlanIdList || 0 == fullHousePlanIdList.size())) {
            return new UniversalSearchResultResponse(true, "ok");
        }

        /********************** 搜索推荐方案 *********************/
        SearchObjectResponse searchObjectResponse;
        try {
            searchObjectResponse = recommendationPlanSearchService.searchRecommendationPlanByIds(recommendedPlanIdList, fullHousePlanIdList, recommendationPlanSearchVo, pageVo);
        } catch (RecommendationPlanSearchException e) {
            log.error(CLASS_LOG_PREFIX + "搜索推荐方案失败:RecommendationPlanSearchException:{}.", e);
            return new UniversalSearchResultResponse(false, CLASS_LOG_PREFIX + "搜索推荐方案失败:RecommendationPlanSearchException:{}.");
        }

        if (null == searchObjectResponse || 0 == searchObjectResponse.getHitTotal() || null == searchObjectResponse.getHitResult()) {
            log.info(CLASS_LOG_PREFIX + "未搜索到数据.....recommendedPlanIdList:{}", JsonUtil.toJson(recommendedPlanIdList));
            return new UniversalSearchResultResponse(true, "ok");
        }

        /******************* 对象转换(RecommendationPlanIndexMappingData->RecommendationPlanVo) **************/
        List<RecommendationPlanIndexMappingData> recommendationPlanIndexMappingDataList = (List<RecommendationPlanIndexMappingData>) searchObjectResponse.getHitResult();
        //转换对象
        List<RecommendationPlanVo> recommendationPlanVoList = recommendationPlanSearchService.dispostRecommendPlanResultList(recommendationPlanIndexMappingDataList, recommendationPlanSearchVo.getUserId(), platformCode, recommendationPlanSearchVo.getCompanyId());

        return new UniversalSearchResultResponse(true, "ok", searchObjectResponse.getHitTotal(), recommendationPlanVoList);
    }


    @RequestMapping("/count")
    UniversalSearchResultResponse getCollectRecommendedPlanCount() {

        RecommendationPlanSearchVo recommendationPlanSearchVo = new RecommendationPlanSearchVo();
        //平台校验 这里只校验平台编码
        String platformCode = RequestHeaderUtil.getPlatformIdByRequest(request);
        MessageUtil messageUtil = recommendationPlanSearchService.paramVerify("a", platformCode);
        if (!messageUtil.isStauts()) {
            return new UniversalSearchResultResponse(false, messageUtil.getMessage());
        } else {
            recommendationPlanSearchVo.setPlatformId(RequestHeaderUtil.getPlatformIdByCode(platformCode));
            recommendationPlanSearchVo.setPlatformCode(platformCode);
        }

        //登录信息
        LoginUser loginUser = LoginContext.getLoginUser(LoginUser.class);
        if (null != loginUser) {
            recommendationPlanSearchVo.setUserId(loginUser.getId());
        } else {
            return new UniversalSearchResultResponse(false, "请登录！");
        }
        //获取企业
        Integer companyId = recommendationPlanSearchService.getCompanyInfo(platformCode, loginUser);
        if (null == companyId || companyId == 0) {
            return new UniversalSearchResultResponse(false, "数据异常！");
        }
        recommendationPlanSearchVo.setCompanyId(companyId);

        //从数据库查询方案收藏数量
        int count;
        if ("miniProgram".equals(platformCode) && companyId.equals(2501)) {
            List<Integer> recommendedPlanIdList = nodeInfoBizService.getContentIdList(loginUser.getId(), NodeInfoConstant.SYSTEM_DICTIONARY_NODE_TYPE_RECOMMENDED, NodeInfoConstant.SYSTEM_DICTIONARY_DETAIL_TYPE_FAVORITE);
            List<Integer> fullHousePlanIdList = nodeInfoBizService.getContentIdList(loginUser.getId(), NodeInfoConstant.SYSTEM_DICTIONARY_NODE_TYPE_FULL_HOUSE, NodeInfoConstant.SYSTEM_DICTIONARY_DETAIL_TYPE_FAVORITE);
            count = (recommendedPlanIdList == null ? 0 : recommendedPlanIdList.size()) +
                    (fullHousePlanIdList == null ? 0 : fullHousePlanIdList.size());
        }else {
            //手动同步缓存数据到数据库update by weis 2018.10.31
            MessageUtil message = recommendedPlanService.syncRecommendedPlanFavorite();
            if (null != message && !message.isStauts()) {
                log.error(CLASS_LOG_PREFIX + message.getMessage());
            }
            try {
                count = recommendationPlanSearchService.queryCollectRecommendedPlanCount(recommendationPlanSearchVo);
            } catch (Exception e) {
                log.error(CLASS_LOG_PREFIX + "获取收藏推荐方案列表数据异常，Exception:{}", e);
                return new UniversalSearchResultResponse(false, "获取收藏推荐方案列表数据异常！");
            }
        }
        if (count==0) {
            log.warn(CLASS_LOG_PREFIX + "还没有收藏任何方案，" , recommendationPlanSearchVo.getUserId());
            return new UniversalSearchResultResponse(true, "还没有收藏任何方案",count);
        }
        return new UniversalSearchResultResponse(true, "ok",count,"");

    }

}
