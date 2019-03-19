package com.sandu.search.controller.design;

import com.sandu.common.LoginContext;
import com.sandu.gson.GsonUtil;
import com.sandu.search.common.constant.*;
import com.sandu.search.common.tools.*;
import com.sandu.search.common.util.ResponseEnvelope;
import com.sandu.search.common.util.Utils;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 推荐方案接口
 *
 * @date 2018/7/1
 * @auth xiaoxc
 */
@Slf4j
@RestController
@EnableAutoConfiguration
@RequestMapping("/all/recommendationplan/search")
public class RecommendationPlanController {


    private final static String CLASS_LOG_PREFIX = "推荐方案接口:";

    private final HttpServletRequest request;
    private final RecommendationPlanSearchService recommendationPlanSearchService;
    private final RecommendedPlanService recommendedPlanService;

    @Autowired
    public RecommendationPlanController(RecommendedPlanService recommendedPlanService, HttpServletRequest request, RecommendationPlanSearchService recommendationPlanSearchService) {
        this.request = request;
        this.recommendationPlanSearchService = recommendationPlanSearchService;
        this.recommendedPlanService = recommendedPlanService;
    }


    /**
     * @desc 查询推荐方案列表
     *
     * @param searchKeyword      搜索关键字(方案编码，名称，创建人名称，品牌名称，小区名称)
     * @param displayType        方案类型(样板间：public,一键方案:decorate,测试:test,公开:open 等)
     * @param houseType          空间类型
     * @param spaceArea          空间面积
     * @param designStyleId      方案风格
     * @param companyId          企业ID(特殊点直接传企业ID，其余通过用户信息获取)
     * @param spaceLayoutType    空间布局（卫生间浴缸，淋浴房区分）
     * @param designerId         方案设计师Id
     * @param decoratePriceType  装修类型（1:半包 2：全包 3：包软装 4：清水）
     * @param decoratePriceRange 装修范围
     * @param start              数据起始数
     * @param dataSize           单页数据条数
     * @param msgId              u3d标识 (原样返回)
     * @return
     */
    @RequestMapping("/list")
    UniversalSearchResultResponse queryRecommendationPlanList(@RequestParam(required = false)
                                                                 String searchKeyword,
                                                                 String displayType,
                                                                 Integer houseType,
                                                                 String spaceArea,
                                                                 Integer designStyleId,
                                                                 Integer companyId,
                                                                 String spaceLayoutType,
                                                                 Integer designerId,
                                                                 Integer decoratePriceType,
                                                                 Integer decoratePriceRange,
                                                                 Integer start,
                                                                 Integer dataSize,
                                                                 Integer msgId) {


        /************************* 设置搜索条件 **********************/
        RecommendationPlanSearchVo recommendationPlanSearchVo = new RecommendationPlanSearchVo();
        MessageUtil messageUtil = recommendPlanSearchCommonAttribute(recommendationPlanSearchVo, searchKeyword, displayType, houseType, spaceArea, designStyleId, companyId, spaceLayoutType, designerId, decoratePriceType, decoratePriceRange);

        if (!messageUtil.isStauts()) {
            return new UniversalSearchResultResponse(false, messageUtil.getMessage(), msgId);
        }



        /************************* 构造分页对象 ************************/
        PageVo pageVo = new PageVo();
        if (null == start) {
            start = IndexInfoQueryConfig.DEFAULT_SEARCH_DATA_START;
        }
        if (null == dataSize) {
            dataSize = IndexInfoQueryConfig.DEFAULT_SEARCH_DATA_SIZE;
        }
        pageVo.setStart(start);
        pageVo.setPageSize(dataSize);

        /************************* 搜索推荐方案 *************************/
        SearchObjectResponse searchObjectResponse;
        try {
            searchObjectResponse = recommendationPlanSearchService.searchRecommendationPlan(recommendationPlanSearchVo, pageVo);
        } catch (RecommendationPlanSearchException e) {
            log.error(CLASS_LOG_PREFIX + "搜索推荐方案失败:RecommendationPlanSearchException:{}.", e);
            return new UniversalSearchResultResponse(false, CLASS_LOG_PREFIX + "搜索推荐方案失败:RecommendationPlanSearchException:{}.", msgId);
        }

        if (null == searchObjectResponse || 0 == searchObjectResponse.getHitTotal() || null == searchObjectResponse.getHitResult()) {
            log.info(CLASS_LOG_PREFIX + "未搜索到数据.....RecommendationPlanSearchVo:{}, pageVo:{}", recommendationPlanSearchVo.toString(), pageVo.toString());
            return new UniversalSearchResultResponse(true, "ok", msgId);
        }

        /******************* 对象转换(RecommendationPlanIndexMappingData->RecommendationPlanVo) **************/
        List<RecommendationPlanIndexMappingData> recommendationPlanIndexMappingDataList = (List<RecommendationPlanIndexMappingData>) searchObjectResponse.getHitResult();

        //转换对象
        List<RecommendationPlanVo> recommendationPlanVoList = recommendationPlanSearchService.dispostRecommendPlanResultList(recommendationPlanIndexMappingDataList, recommendationPlanSearchVo.getUserId(), recommendationPlanSearchVo.getPlatformCode(), recommendationPlanSearchVo.getCompanyId());
            
        return new UniversalSearchResultResponse(true, "ok", msgId, searchObjectResponse.getHitTotal(), recommendationPlanVoList);
    }


    /**
     * @desc 小程序、移动端查询推荐方案列表
     */
    @RequestMapping("/mini/list")
    UniversalSearchResultResponse queryRecommendationPlanList(@RequestBody RecommendationPlanCondition planCondition) {

        //条件对象
        RecommendationPlanSearchVo recommendationPlanSearchVo = planCondition.getRecommendationPlanSearchVo();
        if (null == recommendationPlanSearchVo) {
            log.warn(CLASS_LOG_PREFIX + "搜索方案失败，必需参数为空,models is null.");
            return new UniversalSearchResultResponse(false, "Param is empty!");
        }

        //手动同步缓存数据到数据库update by weis 2018.10.31
        MessageUtil message = recommendedPlanService.syncRecommendedPlanFavorite();
        if (null != message && !message.isStauts()) {
            log.error(CLASS_LOG_PREFIX + message.getMessage());
        }

        //获取需要过滤的置顶方案数据update by weis 2018.11.12
        List<String> ids;
        try {
            ids = recommendationPlanSearchService.getAllSuperiorPlan();
            if(null != ids && ids.size() > 0){
                recommendationPlanSearchVo.setIds(ids);
            }
            log.info("获取置顶方案数据"+GsonUtil.toJson(ids));
        }catch (Exception e){
            log.error("获取置顶方案数据异常"+e);
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
        /********************** 登录信息 ************************/
        //获取用户登录信息
        LoginUser loginUser = LoginContext.getLoginUser(LoginUser.class);
        int userId = 0;
        if (null != loginUser) {
            userId = loginUser.getId();
        } else {
            return new UniversalSearchResultResponse(false, "请登录！");
        }

        log.info(CLASS_LOG_PREFIX + "当前登录用户ID:{}.", userId);

        /********************* 必传参数校验 ********************/
        //接口平台限制
        String platformCode = recommendationPlanSearchVo.getPlatformCode();
        if (StringUtils.isEmpty(platformCode)){
            platformCode = RequestHeaderUtil.getPlatformIdByRequest(request);
        }
        MessageUtil messageUtil = recommendationPlanSearchService.paramVerify(recommendationPlanSearchVo.getDisplayType(), platformCode);
        if (!messageUtil.isStauts()) {
            return new UniversalSearchResultResponse(false, messageUtil.getMessage());
        }

        /******************** 构造搜索条件 *******************/
        //获取企业相关信息
        Integer companyId = recommendationPlanSearchVo.getCompanyId();
        if (null == companyId || companyId == 0) {
            companyId = recommendationPlanSearchService.getCompanyInfo(platformCode, loginUser);
        }
        if (null == companyId || companyId == 0) {
            return new UniversalSearchResultResponse(false, "数据异常！");
        }
        recommendationPlanSearchVo.setUserId(userId);
        recommendationPlanSearchVo.setCompanyId(companyId);
        recommendationPlanSearchVo.setPlatformCode(platformCode);

        //查询条件对象
        MessageUtil searchCondition = recommendationPlanSearchService.searchRecommendedCondition(recommendationPlanSearchVo, loginUser);
        if (!searchCondition.isStauts()) {
            return new UniversalSearchResultResponse(false, searchCondition.getMessage());
        }

        /********************** 搜索推荐方案 *********************/
        SearchObjectResponse searchObjectResponse;
        try {
            if (null != recommendationPlanSearchVo.getShopId() && 0 < recommendationPlanSearchVo.getShopId()) {
                searchObjectResponse = recommendationPlanSearchService.searchCompanyShopPlanRel(recommendationPlanSearchVo, pageVo);
            } else {
                searchObjectResponse = recommendationPlanSearchService.searchRecommendationPlan(recommendationPlanSearchVo, pageVo);
            }
        } catch (RecommendationPlanSearchException e) {
            log.error(CLASS_LOG_PREFIX + "搜索推荐方案失败:RecommendationPlanSearchException:{}.", e);
            return new UniversalSearchResultResponse(false, CLASS_LOG_PREFIX + "搜索推荐方案失败:RecommendationPlanSearchException:{}.");
        }

        if (null == searchObjectResponse || 0 == searchObjectResponse.getHitTotal() || null == searchObjectResponse.getHitResult()) {
            log.info(CLASS_LOG_PREFIX + "未搜索到数据.....RecommendationPlanSearchVo:{}, pageVo:{}", recommendationPlanSearchVo.toString(), pageVo.toString());
            return new UniversalSearchResultResponse(true, "ok");
        }

        /******************* 对象转换(RecommendationPlanIndexMappingData->RecommendationPlanVo) **************/
        List<RecommendationPlanIndexMappingData> recommendationPlanIndexMappingDataList = (List<RecommendationPlanIndexMappingData>) searchObjectResponse.getHitResult();
        //转换对象
        List<RecommendationPlanVo> recommendationPlanVoList = recommendationPlanSearchService.dispostRecommendPlanResultList(recommendationPlanIndexMappingDataList, recommendationPlanSearchVo.getUserId(), recommendationPlanSearchVo.getPlatformCode(), recommendationPlanSearchVo.getCompanyId());

        return new UniversalSearchResultResponse(true, "ok", searchObjectResponse.getHitTotal(), recommendationPlanVoList);
    }

    /**
     * @desc 小程序、移动端查询推荐方案数量
     */
    @RequestMapping("/mini/count")
    UniversalSearchResultResponse queryRecommendationPlanCount(@RequestBody RecommendationPlanCondition planCondition) {

        //条件对象
        RecommendationPlanSearchVo recommendationPlanSearchVo = planCondition.getRecommendationPlanSearchVo();
        if (null == recommendationPlanSearchVo) {
            log.warn(CLASS_LOG_PREFIX + "搜索方案失败，必需参数为空,models is null.");
            return new UniversalSearchResultResponse(false, "Param is empty!");
        }

        //分页对象
        PageVo pageVo = planCondition.getPageVo();
        /********************* 构造分页对象 *********************/
        if (null == pageVo) {
            pageVo = new PageVo();
        }
        if (0 == pageVo.getPageSize()) {
            pageVo.setPageSize(1);
        }

        /********************** 登录信息 ************************/
        //获取用户登录信息
        LoginUser loginUser = LoginContext.getLoginUser(LoginUser.class);
        int userId = 0;
        if (null != loginUser) {
            userId = loginUser.getId();
        } else {
            return new UniversalSearchResultResponse(false, "请登录！");
        }

        log.info(CLASS_LOG_PREFIX + "当前登录用户ID:{}.", userId);

        /********************* 必传参数校验 ********************/
        //接口平台限制
        String platformCode = recommendationPlanSearchVo.getPlatformCode();
        if (StringUtils.isEmpty(platformCode)){
            platformCode = RequestHeaderUtil.getPlatformIdByRequest(request);
        }
        MessageUtil messageUtil = recommendationPlanSearchService.paramVerify(recommendationPlanSearchVo.getDisplayType(), platformCode);
        if (!messageUtil.isStauts()) {
            return new UniversalSearchResultResponse(false, messageUtil.getMessage());
        }

        /******************** 构造产品搜索条件 *******************/
        //获取企业相关信息
        Integer companyId = recommendationPlanSearchVo.getCompanyId();
        if (null == companyId || companyId == 0) {
            companyId = recommendationPlanSearchService.getCompanyInfo(platformCode, loginUser);
        }
        if (null == companyId || companyId == 0) {
            return new UniversalSearchResultResponse(false, "数据异常！");
        }
        recommendationPlanSearchVo.setUserId(userId);
        recommendationPlanSearchVo.setCompanyId(companyId);
        recommendationPlanSearchVo.setPlatformCode(platformCode);

        //查询条件对象
        MessageUtil searchCondition = recommendationPlanSearchService.searchRecommendedCondition(recommendationPlanSearchVo, loginUser);
        if (!searchCondition.isStauts()) {
            return new UniversalSearchResultResponse(false, searchCondition.getMessage());
        }

        /********************** 搜索推荐方案 *********************/
        SearchObjectResponse searchObjectResponse;
        try {
            if (null != recommendationPlanSearchVo.getShopId() && 0 < recommendationPlanSearchVo.getShopId()) {
                searchObjectResponse = recommendationPlanSearchService.searchCompanyShopPlanRel(recommendationPlanSearchVo, pageVo);
            } else {
                searchObjectResponse = recommendationPlanSearchService.searchRecommendationPlan(recommendationPlanSearchVo, pageVo);
            }
        } catch (RecommendationPlanSearchException e) {
            log.error(CLASS_LOG_PREFIX + "搜索推荐方案数量失败:RecommendationPlanSearchException:{}.", e);
            return new UniversalSearchResultResponse(false, CLASS_LOG_PREFIX + "搜索推荐方案数量失败:RecommendationPlanSearchException:{}.");
        }

        if (null == searchObjectResponse || 0 == searchObjectResponse.getHitTotal() || null == searchObjectResponse.getHitResult()) {
            log.info(CLASS_LOG_PREFIX + "未搜索到数据.....RecommendationPlanSearchVo:{}, pageVo:{}", recommendationPlanSearchVo.toString(), pageVo.toString());
            return new UniversalSearchResultResponse(true, "ok", 0, "");
        }

        return new UniversalSearchResultResponse(true, "ok", searchObjectResponse.getHitTotal(), "");
    }

    /**
     * @desc 查询推荐方案数量
     *
     * @param searchKeyword     搜索关键字(方案编码，名称，创建人名称，品牌名称，小区名称)
     * @param displayType       方案类型(样板间：public,一键方案:decorate,测试:test,公开:open 等)
     * @param houseType         空间类型
     * @param spaceArea         空间面积
     * @param designStyleId     方案风格
     * @param companyId         企业ID(特殊点直接传企业ID，其余通过用户信息获取)
     * @param spaceLayoutType   空间布局（卫生间浴缸，淋浴房区分）
     * @param designerId        方案设计师Id
     * @param msgId             u3d标识 (原样返回)
     * @return
     */
    @RequestMapping("/count")
    UniversalSearchResultResponse queryRecommendationPlanCount(@RequestParam(required = false)
                                                              String searchKeyword,
                                                              String displayType,
                                                              Integer houseType,
                                                              String spaceArea,
                                                              Integer designStyleId,
                                                              Integer companyId,
                                                              String spaceLayoutType,
                                                              Integer designerId,
                                                              Integer decoratePriceType,
                                                              Integer decoratePriceRange,
                                                              Integer msgId) {

        /************************* 设置搜索条件 **********************/
        RecommendationPlanSearchVo recommendationPlanSearchVo = new RecommendationPlanSearchVo();
        MessageUtil messageUtil = recommendPlanSearchCommonAttribute(recommendationPlanSearchVo, searchKeyword, displayType, houseType, spaceArea, designStyleId, companyId, spaceLayoutType, designerId, decoratePriceType, decoratePriceRange);
        if (!messageUtil.isStauts()) {
            return new UniversalSearchResultResponse(false, messageUtil.getMessage(), msgId);
        }

        /************************* 构造分页对象 *********************/
        PageVo pageVo = new PageVo();
        pageVo.setStart(IndexInfoQueryConfig.DEFAULT_SEARCH_DATA_START);
        pageVo.setPageSize(1);


        /************************ 搜索推荐方案 ***********************/
        SearchObjectResponse searchObjectResponse;
        try {
            searchObjectResponse = recommendationPlanSearchService.searchRecommendationPlan(recommendationPlanSearchVo, pageVo);
        } catch (RecommendationPlanSearchException e) {
            log.error(CLASS_LOG_PREFIX + "搜索推荐方案失败:RecommendationPlanSearchException:{}.", e);
            return new UniversalSearchResultResponse(false, CLASS_LOG_PREFIX + "搜索推荐方案失败:RecommendationPlanSearchException:{}.", msgId);
        }

        if (null == searchObjectResponse || 0 == searchObjectResponse.getHitTotal() || null == searchObjectResponse.getHitResult()) {
            log.info(CLASS_LOG_PREFIX + "未搜索到数据.....RecommendationPlanSearchVo:{}, pageVo:{}", recommendationPlanSearchVo.toString(), pageVo.toString());
            return new UniversalSearchResultResponse(true, "ok", 0, msgId);
        }

        return new UniversalSearchResultResponse(true, "ok", searchObjectResponse.getHitTotal(), msgId);
    }


    /**
     * @desc 搜索推荐方案数据公共属性设置
     * @param recommendationPlanSearchVo
     * @param searchKeyword
     * @param displayType
     * @param houseType
     * @param spaceArea
     * @param designStyleId
     * @param companyId
     * @param spaceLayoutType
     * @param designerId
     * @param decoratePriceType
     * @param decoratePriceRange
     * @return
     */
    private MessageUtil recommendPlanSearchCommonAttribute(RecommendationPlanSearchVo recommendationPlanSearchVo,
                                                     String searchKeyword,
                                                     String displayType,
                                                     Integer houseType,
                                                     String spaceArea,
                                                     Integer designStyleId,
                                                     Integer companyId,
                                                     String spaceLayoutType,
                                                     Integer designerId,
                                                     Integer decoratePriceType,
                                                     Integer decoratePriceRange) {
        /************************************* 必传参数校验 *********************************/
        //接口平台限制
        String platformCode = RequestHeaderUtil.getPlatformIdByRequest(request);
        MessageUtil messageUtil = recommendationPlanSearchService.paramVerify(displayType, platformCode);
        if (!messageUtil.isStauts()) {
            return new MessageUtil(false, messageUtil.getMessage());
        }

        /********************************** 登录信息 *****************************/
        //获取用户登录信息
        LoginUser loginUser = LoginContext.getLoginUser(LoginUser.class);
        int userId = 0;
        if (null != loginUser) {
            userId = loginUser.getId();
        } else {
            return new MessageUtil(false, "请登录");
        }
        log.info(CLASS_LOG_PREFIX + "当前登录用户ID:{}.", userId);

        /************************************* 构造产品搜索条件 *********************************/
        recommendationPlanSearchVo.setPlatformCode(platformCode);
        recommendationPlanSearchVo.setDisplayType(displayType);
        recommendationPlanSearchVo.setSpaceArea(!StringUtils.isEmpty(spaceArea)?Integer.parseInt(spaceArea):0);
        recommendationPlanSearchVo.setDesignStyleId(designStyleId);
        recommendationPlanSearchVo.setSpaceLayoutType(spaceLayoutType);
        recommendationPlanSearchVo.setHouseType(houseType);
        recommendationPlanSearchVo.setUserId(userId);
        recommendationPlanSearchVo.setDesignerId(designerId);
        recommendationPlanSearchVo.setSearchKeyword(searchKeyword);
        recommendationPlanSearchVo.setDecoratePriceType(decoratePriceType);
        recommendationPlanSearchVo.setDecoratePriceRange(decoratePriceRange);
        //获取企业相关信息
        if (null == companyId || companyId == 0) {
            companyId = recommendationPlanSearchService.getCompanyInfo(platformCode, loginUser);
        }
        if (null == companyId || companyId == 0) {
            return new MessageUtil(false, "数据异常！");
        }
        recommendationPlanSearchVo.setCompanyId(companyId);
        //动态条件设置
        MessageUtil searchCondition = recommendationPlanSearchService.searchRecommendedCondition(recommendationPlanSearchVo, loginUser);
        if (!searchCondition.isStauts()) {
            return new MessageUtil(false, searchCondition.getMessage());
        }

        return new MessageUtil(true);
    }



}