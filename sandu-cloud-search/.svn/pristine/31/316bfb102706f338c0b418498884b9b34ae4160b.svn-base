package com.sandu.search.controller.design;

import com.sandu.common.LoginContext;
import com.sandu.search.common.constant.IndexInfoQueryConfig;
import com.sandu.search.common.constant.PlatformConstant;
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
@RequestMapping("/sxwmini/recommendationplan/search")
public class SXWRecommendedPlanController {

    private final static String CLASS_LOG_PREFIX = "随选网小程序方案列表:";
    private final HttpServletRequest request;
    private final RecommendationPlanSearchService recommendationPlanSearchService;
    private final RecommendedPlanService recommendedPlanService;

    @Autowired
    public SXWRecommendedPlanController(RecommendedPlanService recommendedPlanService, HttpServletRequest request, RecommendationPlanSearchService recommendationPlanSearchService) {
        this.request = request;
        this.recommendationPlanSearchService = recommendationPlanSearchService;
        this.recommendedPlanService = recommendedPlanService;
    }


    @RequestMapping("/list")
    UniversalSearchResultResponse getRecommendedPlanList(@RequestBody RecommendationPlanCondition planCondition) {

        RecommendationPlanSearchVo shopRecommendedPlanVo = planCondition.getRecommendationPlanSearchVo();
        // 参数校验
        if (null == shopRecommendedPlanVo
                || null == shopRecommendedPlanVo.getPlatformCode()) {
            return new UniversalSearchResultResponse(false, "Param is empty");
        }
        //店铺平台类型设置
        if (Objects.equals(PlatformConstant.PLATFORM_CODE_SELECT_DECORATION, shopRecommendedPlanVo.getPlatformCode())) {
            shopRecommendedPlanVo.setShopPlatformType(SystemDictionaryType.SHOP_SXW_PLATFORM_TYPE);
        }

        //接口平台限制
        /*String platformCode = RequestHeaderUtil.getPlatformIdByRequest(request);
        MessageUtil messageUtil = recommendationPlanSearchService.paramVerify(shopRecommendedPlanVo.getDisplayType(), platformCode);
        if (!messageUtil.isStauts()) {
            return new UniversalSearchResultResponse(false, messageUtil.getMessage());
        }*/

        //登录信息
        LoginUser loginUser = LoginContext.getLoginUser(LoginUser.class);
        if (null != loginUser) {
            shopRecommendedPlanVo.setUserId(loginUser.getId());
        } else {
            return new UniversalSearchResultResponse(false, "请登录！");
        }

        //获取企业相关信息
        Integer companyId = shopRecommendedPlanVo.getCompanyId();
        if (null == companyId || companyId == 0) {
            companyId = recommendationPlanSearchService.getCompanyInfo(shopRecommendedPlanVo.getPlatformCode(), null);
        }
        if (null == companyId || companyId == 0) {
            return new UniversalSearchResultResponse(false, "数据异常！");
        } else {
            shopRecommendedPlanVo.setCompanyId(companyId);
        }
//        shopRecommendedPlanVo.setUserId(31295);
//        shopRecommendedPlanVo.setCompanyId(2501);
        //分页对象
        PageVo pageVo = planCondition.getPageVo();
        /********************* 构造分页对象 *********************/
        if (null == pageVo) {
            pageVo = new PageVo();
        }
        if (0 == pageVo.getPageSize()) {
            pageVo.setPageSize(IndexInfoQueryConfig.DEFAULT_SEARCH_DATA_SIZE);
        }
        if (pageVo.getStart() == 1) {
            pageVo.setStart(pageVo.getStart()-1);
        }

        //手动同步缓存数据到数据库
        MessageUtil message = recommendedPlanService.syncRecommendedPlanFavorite();
        if (null != message && !message.isStauts()) {
            log.error(CLASS_LOG_PREFIX + message.getMessage());
        }

        /********************** 搜索推荐方案 *********************/
        SearchObjectResponse searchObjectResponse;
        try {
            searchObjectResponse = recommendationPlanSearchService.searchCompanyShopPlanRel(shopRecommendedPlanVo, pageVo);
        } catch (RecommendationPlanSearchException e) {
            log.error(CLASS_LOG_PREFIX + "搜索推荐方案失败:RecommendationPlanSearchException:{}.", e);
            return new UniversalSearchResultResponse(false, CLASS_LOG_PREFIX + "搜索推荐方案失败:RecommendationPlanSearchException:{}.");
        }

        if (null == searchObjectResponse || 0 == searchObjectResponse.getHitTotal() || null == searchObjectResponse.getHitResult()) {
            log.info(CLASS_LOG_PREFIX + "未搜索到数据.....recommendedPlanIdList:{}", JsonUtil.toJson(shopRecommendedPlanVo));
            return new UniversalSearchResultResponse(true, "ok");
        }

        /******************* 对象转换(RecommendationPlanIndexMappingData->RecommendationPlanVo) **************/
        List<RecommendationPlanIndexMappingData> recommendationPlanIndexMappingDataList = (List<RecommendationPlanIndexMappingData>) searchObjectResponse.getHitResult();
        //转换对象
        List<RecommendationPlanVo> recommendationPlanVoList = recommendationPlanSearchService.dispostRecommendPlanResultList(recommendationPlanIndexMappingDataList, shopRecommendedPlanVo.getUserId(), shopRecommendedPlanVo.getPlatformCode(), shopRecommendedPlanVo.getCompanyId());

        return new UniversalSearchResultResponse(true, "ok", searchObjectResponse.getHitTotal(), recommendationPlanVoList);
    }

}
