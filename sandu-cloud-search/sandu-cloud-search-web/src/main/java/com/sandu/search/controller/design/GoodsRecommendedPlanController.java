package com.sandu.search.controller.design;

import com.sandu.common.LoginContext;
import com.sandu.gson.GsonUtil;
import com.sandu.search.common.constant.IndexInfoQueryConfig;
import com.sandu.search.common.tools.MessageUtil;
import com.sandu.search.common.tools.RequestHeaderUtil;
import com.sandu.search.common.util.ResponseEnvelope;
import com.sandu.search.common.util.Utils;
import com.sandu.search.entity.common.PageVo;
import com.sandu.search.entity.designplan.po.RecommendationPlanPo;
import com.sandu.search.entity.designplan.po.ShopPlanInfoPo;
import com.sandu.search.entity.designplan.vo.RecommendationPlanSearchVo;
import com.sandu.search.entity.designplan.vo.RecommendationPlanVo;
import com.sandu.search.entity.elasticsearch.index.RecommendationPlanIndexMappingData;
import com.sandu.search.entity.elasticsearch.response.SearchObjectResponse;
import com.sandu.search.entity.response.universal.UniversalSearchResultResponse;
import com.sandu.search.entity.user.LoginUser;
import com.sandu.search.exception.ElasticSearchException;
import com.sandu.search.exception.MetaDataException;
import com.sandu.search.exception.RecommendationPlanSearchException;
import com.sandu.search.service.design.RecommendationPlanSearchService;
import com.sandu.search.service.design.RecommendedPlanService;
import com.sandu.search.service.design.dubbo.ShopSearchPlanService;
import com.sandu.search.service.metadata.MetaDataService;
import com.sandu.search.storage.company.CompanyMetaDataStorage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 商品推荐方案 控制层
 *
 * @auth xiaoxc
 * @data 20180929
 */
@Slf4j
@RestController
@EnableAutoConfiguration
@RequestMapping("/goods/recommendationplan/search")
public class GoodsRecommendedPlanController {

    private final static String CLASS_LOG_PREFIX = "商品推荐方案列表:";

    private final HttpServletRequest request;
    private final CompanyMetaDataStorage companyMetaDataStorage;
    private final MetaDataService metaDataService;
    private final RecommendationPlanSearchService recommendationPlanSearchService;
    private final RecommendedPlanService recommendedPlanService;

    @Autowired
    public GoodsRecommendedPlanController(RecommendedPlanService recommendedPlanService, HttpServletRequest request, CompanyMetaDataStorage companyMetaDataStorage, MetaDataService metaDataService, RecommendationPlanSearchService recommendationPlanSearchService) {
        this.request = request;
        this.companyMetaDataStorage = companyMetaDataStorage;
        this.metaDataService = metaDataService;
        this.recommendationPlanSearchService = recommendationPlanSearchService;
        this.recommendedPlanService = recommendedPlanService;
    }


    @RequestMapping("/list")
    UniversalSearchResultResponse getGoodsRecommendedPlanList(@RequestParam(value = "spuId", required = true) Integer spuId,
                                                              @RequestParam(value = "searchKeyword", required = false) String searchKeyword,
                                                              @RequestParam(value = "start", required = false) Integer start,
                                                              @RequestParam(value = "dataSize", required = false) Integer dataSize,
                                                              @RequestParam(value = "platformCode", required = false) String platformCode) {

        /************* 登录信息 ***********/
        LoginUser loginUser = LoginContext.getLoginUser(LoginUser.class);
        Integer userId = 0;
        if (null != loginUser) {
            userId = loginUser.getId();
        } else {
            return new UniversalSearchResultResponse(false, "请登录！");
        }

        /*********** 参数校验 ***********/
        if (StringUtils.isEmpty(platformCode)){
            platformCode = RequestHeaderUtil.getPlatformIdByRequest(request);
        }
        MessageUtil messageUtil = recommendationPlanSearchService.paramVerify(platformCode, spuId);
        if (!messageUtil.isStauts()) {
            return new UniversalSearchResultResponse(false, messageUtil.getMessage());
        }

        /********************** 分页条件 *********************/
        PageVo pageVo = new PageVo();
        if (null == start) {
            start = IndexInfoQueryConfig.DEFAULT_SEARCH_DATA_START;
        }
        if (null == dataSize) {
            dataSize = IndexInfoQueryConfig.DEFAULT_SEARCH_DATA_SIZE;
        }
        pageVo.setStart(start);
        pageVo.setPageSize(dataSize);

        //手动同步缓存数据到数据库update by weis 2018.10.31
        MessageUtil message = recommendedPlanService.syncRecommendedPlanFavorite();
        if (null != message && !message.isStauts()) {
            log.error(CLASS_LOG_PREFIX + message.getMessage());
        }

        /********************** 设置查询条件 *********************/
        List<Integer> productIds;
        try {
            productIds = metaDataService.getProductIdsBySpuId(spuId);
        } catch (MetaDataException e) {
            log.error(CLASS_LOG_PREFIX + "通过spuId=" + spuId + "查询产品ID异常！Exception:{}" + e);
            return new UniversalSearchResultResponse(false, "通过spuId=" + spuId + "查询产品ID异常！");
        }
        if (null == productIds || 0 == productIds.size()) {
            return new UniversalSearchResultResponse(true, "OK");
        }

        RecommendationPlanSearchVo recommendationPlanSearchVo = new RecommendationPlanSearchVo();
        recommendationPlanSearchVo.setProductIdList(productIds);
        recommendationPlanSearchVo.setPlatformCode(platformCode);
        recommendationPlanSearchVo.setUserId(userId);
        if (!StringUtils.isEmpty(searchKeyword)) {
            recommendationPlanSearchVo.setSearchKeyword(searchKeyword);
        }
        //获取企业相关信息
        Integer companyId = recommendationPlanSearchVo.getCompanyId();
        if (null == companyId || companyId == 0) {
            companyId = recommendationPlanSearchService.getCompanyInfo(platformCode, loginUser);
        }
        if (null == companyId || companyId == 0) {
            return new UniversalSearchResultResponse(false, "数据异常！");
        }
        recommendationPlanSearchVo.setCompanyId(companyId);
        recommendationPlanSearchVo.setCompanyIdList(Arrays.asList(companyId));
        List<Integer> recommendedTypes = new ArrayList<>();
        recommendedTypes.add(RecommendationPlanPo.ONEKEY_RECOMMENDATION_PLAN_TYPE);

        /*三度云享家展示平台所有已发布的一键方案*/
        if (companyMetaDataStorage.getSanduCompanyId() == recommendationPlanSearchVo.getCompanyId()) {
            recommendationPlanSearchVo.setCompanyId(0);
            //recommendedTypes.add(RecommendationPlanPo.SHARE_RECOMMENDATION_PLAN_TYPE);
        }
        recommendationPlanSearchVo.setRecommendationPlanTypeList(recommendedTypes);
        recommendationPlanSearchVo.setReleaseStatus(RecommendationPlanPo.DESIGN_PLAN_IS_PUBLISH);

        /********************** 搜索推荐方案 *********************/
        SearchObjectResponse searchObjectResponse;
        try {
            searchObjectResponse = recommendationPlanSearchService.searchGoodDesign(recommendationPlanSearchVo, pageVo);
        } catch (RecommendationPlanSearchException e) {
            log.error(CLASS_LOG_PREFIX + "搜索推荐方案失败:RecommendationPlanSearchException:{}.", e);
            return new UniversalSearchResultResponse(false, CLASS_LOG_PREFIX + "搜索推荐方案失败:RecommendationPlanSearchException:{}.");
        }

        if (null == searchObjectResponse || 0 == searchObjectResponse.getHitTotal() || null == searchObjectResponse.getHitResult()) {
            log.info(CLASS_LOG_PREFIX + "未搜索到数据.....RecommendationPlanSearchVo:{}", recommendationPlanSearchVo.toString());
            return new UniversalSearchResultResponse(true, "ok");
        }

        /******************* 对象转换(RecommendationPlanIndexMappingData->RecommendationPlanVo) **************/
        List<RecommendationPlanIndexMappingData> recommendationPlanIndexMappingDataList = (List<RecommendationPlanIndexMappingData>) searchObjectResponse.getHitResult();
        //转换对象
        List<RecommendationPlanVo> recommendationPlanVoList = recommendationPlanSearchService.dispostRecommendPlanResultList(recommendationPlanIndexMappingDataList, recommendationPlanSearchVo.getUserId(), recommendationPlanSearchVo.getPlatformCode(), recommendationPlanSearchVo.getCompanyId());

        return new UniversalSearchResultResponse(true, "ok", searchObjectResponse.getHitTotal(), recommendationPlanVoList);
    }

    @Autowired
    private ShopSearchPlanService shopSearchPlanService;

    @RequestMapping("/testlist")
    UniversalSearchResultResponse getTestList(Integer shopId) {
        ShopPlanInfoPo shopPlanInfoPo = null;
        try {
            shopPlanInfoPo = (ShopPlanInfoPo) shopSearchPlanService.getShopPlanInfo(shopId);
        } catch (ElasticSearchException e) {
            e.printStackTrace();
        }
        return new UniversalSearchResultResponse(true, "OK", 0, shopPlanInfoPo);
    }

}
