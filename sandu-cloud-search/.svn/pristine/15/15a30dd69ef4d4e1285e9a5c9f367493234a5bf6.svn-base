package com.sandu.search.controller.design;

import com.sandu.common.LoginContext;
import com.sandu.search.common.constant.IndexInfoQueryConfig;
import com.sandu.search.common.constant.PlatformConstant;
import com.sandu.search.common.constant.RedisConstant;
import com.sandu.search.common.tools.DateTool;
import com.sandu.search.common.tools.DomainUtil;
import com.sandu.search.common.tools.EntityCopyUtils;
import com.sandu.search.common.tools.RequestHeaderUtil;
import com.sandu.search.entity.common.PageVo;
import com.sandu.search.entity.designplan.convert.RecommendationPlanConvert;
import com.sandu.search.entity.designplan.po.RecommendationPlanPo;
import com.sandu.search.entity.designplan.vo.RecommendationPlanSearchVo;
import com.sandu.search.entity.designplan.vo.RecommendationPlanVo;
import com.sandu.search.entity.elasticsearch.index.RecommendationPlanIndexMappingData;
import com.sandu.search.entity.elasticsearch.response.SearchObjectResponse;
import com.sandu.search.entity.user.LoginUser;
import com.sandu.search.exception.RecommendationPlanSearchException;
import com.sandu.search.service.design.RecommendationPlanSearchService;
import com.sandu.search.service.redis.RedisService;
import com.sandu.search.storage.company.CompanyMetaDataStorage;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 推荐方案接口(为适配老接口参数，，在这里做个转换)
 *
 * @date 2018/6/1
 * @auth pengxuangang
 * @mail xuangangpeng@gmail.com
 */
@Slf4j
@RestController
@EnableAutoConfiguration
@RequestMapping("/brandsite/recommendationplan/normal")
public class RecommendationPlanOriginController {

    private final static String CLASS_LOG_PREFIX = "[运营网站]推荐方案接口:";

    private final DomainUtil domainUtil;
    private final RedisService redisService;
    private final HttpServletRequest request;
    private final CompanyMetaDataStorage companyMetaDataStorage;
    private final RecommendationPlanSearchService recommendationPlanSearchService;

    @Autowired
    public RecommendationPlanOriginController(DomainUtil domainUtil, RedisService redisService, HttpServletRequest request, CompanyMetaDataStorage companyMetaDataStorage, RecommendationPlanSearchService recommendationPlanSearchService) {
        this.domainUtil = domainUtil;
        this.redisService = redisService;
        this.request = request;
        this.companyMetaDataStorage = companyMetaDataStorage;
        this.recommendationPlanSearchService = recommendationPlanSearchService;
    }

    /**
     * 查询推荐方案
     *
     * @param curPage               当前页
     * @param pageSize              页面大小
     * @param spaceType             空间类型
     * @param designPlanStyleId     设计方案风格ID
     * @param spaceArea             空间面积
     * @param isSortByReleaseTime   是否根据发布时间排序
     * @return
     */
    @GetMapping("/list")
    OriginResponse queryRecommendationPlanListByCondition(@RequestParam Integer curPage,
                                                          Integer pageSize,
                                                          Integer spaceType,
                                                          Integer designPlanStyleId,
                                                          String spaceArea,
                                                          Integer isSortByReleaseTime) {

        /************************************************** 检查参数 **********************************************/
        //参数转换
        if (null == curPage || 0 >= curPage) {
            curPage = 1;
        }
        if (null == pageSize || 0 >= pageSize) {
            pageSize = IndexInfoQueryConfig.DEFAULT_SEARCH_DATA_SIZE;
        }

        //条件对象
        RecommendationPlanSearchVo recommendationPlanSearchVo = new RecommendationPlanSearchVo();
        if (null != spaceType && 0 < spaceType) {
            recommendationPlanSearchVo.setHouseType(spaceType);
        }
        if (null != designPlanStyleId && 0 < designPlanStyleId) {
            recommendationPlanSearchVo.setDesignStyleId(designPlanStyleId);
        }
        if (!StringUtils.isEmpty(spaceArea)) {
            recommendationPlanSearchVo.setSpaceArea(Integer.valueOf(spaceArea));
        }
        if (null != isSortByReleaseTime && 1 == isSortByReleaseTime) {
            recommendationPlanSearchVo.setSortType(RecommendationPlanSearchVo.RELEASE_TIME_SORT_DESC);
        }

        //平台来源标识
        String platformCode = RequestHeaderUtil.getPlatformIdByRequest(request);
        //公司信息获取
        int companyId = 0;
        if (PlatformConstant.PLATFORM_CODE_TOC_SITE.equals(platformCode)) {
            //品牌网站HOST解析
            String domainName = DomainUtil.getDomainNameByHost(request);
            log.info(CLASS_LOG_PREFIX + "公司域名-domainName:{}", domainName);

            if (StringUtils.isEmpty(domainName)) {
                log.info(CLASS_LOG_PREFIX + "品牌网站HOST解析失败 domainname is null!");
                return new OriginResponse(false, CLASS_LOG_PREFIX + "品牌网站HOST解析失败 domainname is null!");
            }

            //获取为空则直接获取公司--(小程序有公司但没有公司域名)
            companyId = companyMetaDataStorage.getCompanyIdByDomainName(domainName);
            log.info(CLASS_LOG_PREFIX + "Brand2C公司ID获取完成! DomainName:{}, CompanyId:{}.", domainName, companyId);
        } else if (PlatformConstant.PLATFORM_CODE_MINI_PROGRAM.equals(platformCode)) {
            //小程序获取公司ID
            companyId = domainUtil.getCompanyIdInMiniProgramByRequest(request);
            log.info(CLASS_LOG_PREFIX + "小程序获取公司ID为:{}.", companyId);
        }

        //公司ID
        if (0 < companyId) {
            //三度云享家应用需单独处理--去掉品牌限制|平台限制
            if (companyMetaDataStorage.getSanduCompanyId() != companyId) {
                recommendationPlanSearchVo.setCompanyId(companyId);
                log.info(CLASS_LOG_PREFIX + "当前公司非三度云享家公司,需方案公司过滤,当前公司:{}.", companyId);
            } else {
                log.info(CLASS_LOG_PREFIX + "已识别为三度云享家公司，移除公司限制,排除分享和交付方案来源的推荐方案! CompanyId:{}.", companyId);
                recommendationPlanSearchVo.setPlanSource(Arrays.asList(
                        RecommendationPlanPo.SHARE_PLAN_SOURCE,
                        RecommendationPlanPo.DELIVER_PLAN_SOURCE
                ));
            }
        }

        /************************************************** 构造分页对象 **********************************************/
        //分页对象
        PageVo pageVo = new PageVo();
        pageVo.setStart(pageSize * (curPage - 1));
        pageVo.setPageSize(pageSize);

        /************************************ 构造产品搜索条件 ********************************************************/
        //一键方案
        recommendationPlanSearchVo.setRecommendationPlanType(RecommendationPlanPo.ONEKEY_RECOMMENDATION_PLAN_TYPE);
        //已发布状态方案
        recommendationPlanSearchVo.setReleaseStatus(RecommendationPlanPo.DESIGN_PLAN_IS_PUBLISH);

        /********************************************** 搜索推荐方案 *******************************************************/
        SearchObjectResponse searchObjectResponse;
        try {
            searchObjectResponse = recommendationPlanSearchService.searchRecommendationPlan(recommendationPlanSearchVo, pageVo);
        } catch (RecommendationPlanSearchException e) {
            log.error(CLASS_LOG_PREFIX + "搜索推荐方案失败:RecommendationPlanSearchException:{}.", e);
            return new OriginResponse(false, CLASS_LOG_PREFIX + "搜索推荐方案失败:RecommendationPlanSearchException:{}.");
        }

        if (null == searchObjectResponse || 0 == searchObjectResponse.getHitTotal() || null == searchObjectResponse.getHitResult()) {
            log.info(CLASS_LOG_PREFIX + "未搜索到数据.....RecommendationPlanSearchVo:{}, pageVo:{}", recommendationPlanSearchVo.toString(), pageVo.toString());
            return new OriginResponse(true, null, 0);
        }

        /******************************************* 登录信息(用户收藏点赞数) *************************************/
        //获取用户登录信息
        LoginUser loginUser = LoginContext.getLoginUser(LoginUser.class);
        int userId = 0;
        if (null != loginUser) {
            userId = loginUser.getId();
        }
        log.info(CLASS_LOG_PREFIX + "当前登录用户ID:{}.", userId);

        /******************* 对象转换(RecommendationPlanIndexMappingData->RecommendationPlanVo) **************/
        List<RecommendationPlanIndexMappingData> recommendationPlanIndexMappingDataList = (List<RecommendationPlanIndexMappingData>) searchObjectResponse.getHitResult();
        List<RecommendationPlanVo> recommendationPlanVoList = null;
        if (null != recommendationPlanIndexMappingDataList && 0 < recommendationPlanIndexMappingDataList.size()) {
            //初始化
            recommendationPlanVoList = new ArrayList<>(recommendationPlanIndexMappingDataList.size());

            //转换对象
            for (RecommendationPlanIndexMappingData recommendationPlanIndexMappingData : recommendationPlanIndexMappingDataList) {
                RecommendationPlanVo recommendationPlanVo = new RecommendationPlanVo();
                //复制对象
                //EntityCopyUtils.copyData(recommendationPlanIndexMappingData, recommendationPlanVo);
                RecommendationPlanConvert.parseToPlanVoByPlanIndexMappingData(recommendationPlanIndexMappingData, recommendationPlanVo);
                //使用空间面积
                List<String> applySpaceAreaList = recommendationPlanIndexMappingData.getApplySpaceAreaList();
                if (null != applySpaceAreaList && 0 < applySpaceAreaList.size()) {
                    StringBuffer applySpaceAreaSB = new StringBuffer();
                    applySpaceAreaList.forEach(area -> applySpaceAreaSB.append(area).append(","));
                    //移除最后一个逗号
                    recommendationPlanVo.setApplySpaceAreas(applySpaceAreaSB.substring(0, applySpaceAreaSB.length() - 1).toString());
                }
                //装入对象
                recommendationPlanVoList.add(recommendationPlanVo);
            }

            //点赞|收藏数据
            for (RecommendationPlanVo recommendationPlanVo : recommendationPlanVoList) {
                //点赞|收藏
                if (0 < userId) {
                    //查询点赞收藏
                    String collectLikeStr = redisService.getMap(RedisConstant.RECOMMENDATION_PLAN_USER_COLLECT_LIKE, userId + ":" + recommendationPlanVo.getPlanRecommendedId());
                    if (!StringUtils.isEmpty(collectLikeStr)) {
                        String[] split = collectLikeStr.split(":");
                        if (2 == split.length) {
                            //点赞
                            recommendationPlanVo.setIsLike(Integer.parseInt(split[0]));
                            //收藏
                            recommendationPlanVo.setIsFavorite(Integer.parseInt(split[1]));
                        }
                    }
                }

                //收藏数
                String collectCountStr = redisService.getMap(RedisConstant.RECOMMENDATION_PLAN_COLLECT_COUNT, recommendationPlanVo.getPlanRecommendedId() + "");
                if (!StringUtils.isEmpty(collectCountStr)) {
                    recommendationPlanVo.setCollectNum(Integer.parseInt(collectCountStr));
                }
                //点赞数
                String likeCountStr = redisService.getMap(RedisConstant.RECOMMENDATION_PLAN_LIKE_COUNT, recommendationPlanVo.getPlanRecommendedId() + "");
                if (!StringUtils.isEmpty(likeCountStr)) {
                    recommendationPlanVo.setLikeNum(Integer.parseInt(likeCountStr));
                }
            }


        }

        if (null == recommendationPlanVoList || 0 >= recommendationPlanVoList.size()) {
            log.info(CLASS_LOG_PREFIX + "查询推荐方案结果为空。");
            return new OriginResponse(true, null, 0);
        }

        //转换对象
        List<RecommendationBean> recommendationBeanList = new ArrayList<>(recommendationPlanVoList.size());
        for (RecommendationPlanVo recommendationPlanVo : recommendationPlanVoList) {
            //对象转换
            RecommendationBean recommendationBean = new RecommendationBean();
            recommendationBean.setApplySpaceAreas(recommendationPlanVo.getApplySpaceAreas());
            //recommendationBean.setBrandName(recommendationPlanVo.getBrandName());
            recommendationBean.setCollectNum(recommendationPlanVo.getCollectNum());
            recommendationBean.setDesignPlanCoverPath(recommendationPlanVo.getCoverPath());
            recommendationBean.setDesignPlanDesc(recommendationPlanVo.getRemark());
            recommendationBean.setDesignPlanId(recommendationPlanVo.getPlanId());
            recommendationBean.setDesignPlanRecommendId(recommendationPlanVo.getPlanRecommendedId());
            recommendationBean.setDesignPlanStyleId(recommendationPlanVo.getDesignStyleId());
            recommendationBean.setDesignPlanStyleName(recommendationPlanVo.getStyleName());
            recommendationBean.setDesignPlanName(recommendationPlanVo.getPlanName());
            recommendationBean.setDisplayType(recommendationPlanVo.getRecommendedType());
            recommendationBean.setGroupPrimaryId(0);
            recommendationBean.setIsFavorite(recommendationPlanVo.getIsFavorite());
            recommendationBean.setIsLike(recommendationPlanVo.getIsLike());
            recommendationBean.setIsSortByReleaseTime(0);
            recommendationBean.setIsSortByRenderCount(0);
            recommendationBean.setLikeNum(recommendationPlanVo.getLikeNum());
            recommendationBean.setReleaseTime(recommendationPlanVo.getReleaseTime());
            recommendationBean.setRenderCount(0);
            recommendationBean.setSpaceArea(recommendationPlanVo.getSpaceAreas());
            recommendationBean.setSpaceShape(recommendationPlanVo.getSpaceShape());
            recommendationBean.setSpaceStyleName(recommendationPlanVo.getStyleName());
            recommendationBean.setSpaceType(recommendationPlanVo.getSpaceFunctionId());
            recommendationBean.setTemplateId(recommendationPlanVo.getDesignTemplateId());
            recommendationBeanList.add(recommendationBean);
        }

        return new OriginResponse(true, recommendationBeanList, searchObjectResponse.getHitTotal());
    }
}

@Data
class OriginResponse {

    public OriginResponse(boolean success, Object message) {
        this.success = success;
        this.status = success;
        this.message = message;
        this.flag = success;
    }

    public OriginResponse(boolean success, List<RecommendationBean> obj, long totalCount) {
        this.success = success;
        this.status = success;
        this.totalCount = totalCount;
        this.flag = success;
        this.obj = obj;
    }

    /**
     * success : true
     * status : true
     * message : null
     * obj : [{"designPlanId":30118,"designPlanRecommendId":105710,"designPlanName":"客餐厅(C03_0279)","designPlanCoverPath":"/AA/c_basedesign_recommended/2018/06/14/17/design/designPlanRecommended/render/181957_1528969608524.png","designPlanDesc":null,"designPlanStyleId":null,"designPlanStyleName":null,"spaceType":3,"spaceStyleName":"美式田园","isSortByReleaseTime":null,"isSortByRenderCount":null,"renderCount":2,"releaseTime":"2018-06-14 17:50:01","applySpaceAreas":"17,22,27,32,37,42,47,57,67,77,87,97,107,117","spaceArea":"17","brandName":null,"isFavorite":null,"spaceShape":2,"displayType":null,"isLike":null,"likeNum":null,"collectNum":null,"groupPrimaryId":0,"templateId":null},{"designPlanId":27421,"designPlanRecommendId":102022,"designPlanName":"NORK.S504308","designPlanCoverPath":"/AA/c_basedesign_recommended/2018/05/28/designPlanRecommended/20180528144823383Uha.png","designPlanDesc":null,"designPlanStyleId":null,"designPlanStyleName":null,"spaceType":3,"spaceStyleName":"美式田园","isSortByReleaseTime":null,"isSortByRenderCount":null,"renderCount":10,"releaseTime":"2018-05-22 18:35:03","applySpaceAreas":"32,37,42","spaceArea":"32","brandName":null,"isFavorite":null,"spaceShape":2,"displayType":null,"isLike":null,"likeNum":26,"collectNum":4,"groupPrimaryId":0,"templateId":null},{"designPlanId":27422,"designPlanRecommendId":102021,"designPlanName":"NORK.S504308","designPlanCoverPath":"/AA/c_basedesign_recommended/2018/05/28/designPlanRecommended/20180528144824546WVG.png","designPlanDesc":null,"designPlanStyleId":null,"designPlanStyleName":null,"spaceType":3,"spaceStyleName":"美式田园","isSortByReleaseTime":null,"isSortByRenderCount":null,"renderCount":2,"releaseTime":"2018-05-22 18:35:03","applySpaceAreas":"17,22,27","spaceArea":"17","brandName":null,"isFavorite":null,"spaceShape":2,"displayType":null,"isLike":null,"likeNum":20,"collectNum":0,"groupPrimaryId":0,"templateId":null},{"designPlanId":27418,"designPlanRecommendId":102023,"designPlanName":"NORK.S504308","designPlanCoverPath":"/AA/c_basedesign_recommended/2018/05/28/designPlanRecommended/20180528144825468cFZ.png","designPlanDesc":null,"designPlanStyleId":null,"designPlanStyleName":null,"spaceType":3,"spaceStyleName":"美式田园","isSortByReleaseTime":null,"isSortByRenderCount":null,"renderCount":6,"releaseTime":"2018-05-22 18:35:03","applySpaceAreas":"47,57,67,77,87,97,107,117","spaceArea":"47","brandName":null,"isFavorite":null,"spaceShape":2,"displayType":null,"isLike":null,"likeNum":27,"collectNum":3,"groupPrimaryId":0,"templateId":null},{"designPlanId":27328,"designPlanRecommendId":101898,"designPlanName":"Nork.S404883","designPlanCoverPath":"/AA/c_basedesign_recommended/2018/05/28/designPlanRecommended/20180528144811905CIn.png","designPlanDesc":null,"designPlanStyleId":null,"designPlanStyleName":null,"spaceType":3,"spaceStyleName":"简欧","isSortByReleaseTime":null,"isSortByRenderCount":null,"renderCount":10,"releaseTime":"2018-05-22 17:45:19","applySpaceAreas":"37,42,47","spaceArea":"37","brandName":null,"isFavorite":null,"spaceShape":4,"displayType":null,"isLike":null,"likeNum":16,"collectNum":4,"groupPrimaryId":0,"templateId":null},{"designPlanId":27329,"designPlanRecommendId":101900,"designPlanName":"NORK.S105121","designPlanCoverPath":"/AA/c_basedesign_recommended/2018/05/28/designPlanRecommended/20180528144820747rxR.png","designPlanDesc":null,"designPlanStyleId":null,"designPlanStyleName":null,"spaceType":3,"spaceStyleName":"美式田园","isSortByReleaseTime":null,"isSortByRenderCount":null,"renderCount":null,"releaseTime":"2018-05-22 17:45:19","applySpaceAreas":"47,57,67,77,87,97,107,117","spaceArea":"47","brandName":null,"isFavorite":null,"spaceShape":2,"displayType":null,"isLike":null,"likeNum":18,"collectNum":3,"groupPrimaryId":0,"templateId":null},{"designPlanId":27337,"designPlanRecommendId":101899,"designPlanName":"Nork.S404883","designPlanCoverPath":"/AA/c_basedesign_recommended/2018/05/28/designPlanRecommended/20180528144821594dsq.png","designPlanDesc":null,"designPlanStyleId":null,"designPlanStyleName":null,"spaceType":3,"spaceStyleName":"简欧","isSortByReleaseTime":null,"isSortByRenderCount":null,"renderCount":12,"releaseTime":"2018-05-22 17:45:19","applySpaceAreas":"22,27,32","spaceArea":"22","brandName":null,"isFavorite":null,"spaceShape":4,"displayType":null,"isLike":null,"likeNum":18,"collectNum":2,"groupPrimaryId":0,"templateId":null},{"designPlanId":27322,"designPlanRecommendId":101902,"designPlanName":"NORK.S014238","designPlanCoverPath":"/AA/c_basedesign_recommended/2018/05/28/designPlanRecommended/20180528144819022Lrj.png","designPlanDesc":null,"designPlanStyleId":null,"designPlanStyleName":null,"spaceType":3,"spaceStyleName":"美式田园","isSortByReleaseTime":null,"isSortByRenderCount":null,"renderCount":1,"releaseTime":"2018-05-22 17:45:18","applySpaceAreas":"32,37,42","spaceArea":"32","brandName":null,"isFavorite":null,"spaceShape":2,"displayType":null,"isLike":null,"likeNum":18,"collectNum":1,"groupPrimaryId":0,"templateId":null},{"designPlanId":27325,"designPlanRecommendId":101901,"designPlanName":"NORK.S014238","designPlanCoverPath":"/AA/c_basedesign_recommended/2018/05/28/designPlanRecommended/20180528144819855seQ.png","designPlanDesc":null,"designPlanStyleId":null,"designPlanStyleName":null,"spaceType":3,"spaceStyleName":"美式田园","isSortByReleaseTime":null,"isSortByRenderCount":null,"renderCount":null,"releaseTime":"2018-05-22 17:45:18","applySpaceAreas":"17,22,27","spaceArea":"17","brandName":null,"isFavorite":null,"spaceShape":2,"displayType":null,"isLike":null,"likeNum":16,"collectNum":2,"groupPrimaryId":0,"templateId":null},{"designPlanId":27290,"designPlanRecommendId":101903,"designPlanName":"Nork.S404883","designPlanCoverPath":"/AA/c_basedesign_recommended/2018/05/28/designPlanRecommended/20180528144818108ZYD.png","designPlanDesc":null,"designPlanStyleId":null,"designPlanStyleName":null,"spaceType":3,"spaceStyleName":"简欧","isSortByReleaseTime":null,"isSortByRenderCount":null,"renderCount":6,"releaseTime":"2018-05-22 17:45:18","applySpaceAreas":"57,67,77,87,97,107,117","spaceArea":"57","brandName":null,"isFavorite":null,"spaceShape":4,"displayType":null,"isLike":null,"likeNum":14,"collectNum":1,"groupPrimaryId":0,"templateId":null},{"designPlanId":27279,"designPlanRecommendId":101905,"designPlanName":"NORK.S014238","designPlanCoverPath":"/AA/c_basedesign_recommended/2018/05/28/designPlanRecommended/20180528144734119nvH.png","designPlanDesc":null,"designPlanStyleId":null,"designPlanStyleName":null,"spaceType":3,"spaceStyleName":"美式田园","isSortByReleaseTime":null,"isSortByRenderCount":null,"renderCount":1,"releaseTime":"2018-05-22 17:45:17","applySpaceAreas":"47,57,67,77,87,97,107,117","spaceArea":"47","brandName":null,"isFavorite":null,"spaceShape":2,"displayType":null,"isLike":null,"likeNum":16,"collectNum":2,"groupPrimaryId":0,"templateId":null},{"designPlanId":27219,"designPlanRecommendId":101911,"designPlanName":"Nork.S105169","designPlanCoverPath":"/AA/c_basedesign_recommended/2018/05/28/designPlanRecommended/20180528144817080FRY.png","designPlanDesc":null,"designPlanStyleId":null,"designPlanStyleName":null,"spaceType":3,"spaceStyleName":"现代奢华","isSortByReleaseTime":null,"isSortByRenderCount":null,"renderCount":2,"releaseTime":"2018-05-22 17:45:16","applySpaceAreas":"32,37,42","spaceArea":"32","brandName":null,"isFavorite":null,"spaceShape":8,"displayType":null,"isLike":null,"likeNum":16,"collectNum":2,"groupPrimaryId":0,"templateId":null},{"designPlanId":27225,"designPlanRecommendId":101910,"designPlanName":"Nork.S105169","designPlanCoverPath":"/AA/c_basedesign_recommended/2018/05/28/designPlanRecommended/20180528144731012iqL.png","designPlanDesc":null,"designPlanStyleId":null,"designPlanStyleName":null,"spaceType":3,"spaceStyleName":"现代奢华","isSortByReleaseTime":null,"isSortByRenderCount":null,"renderCount":7,"releaseTime":"2018-05-22 17:45:16","applySpaceAreas":"17,22,27","spaceArea":"22","brandName":null,"isFavorite":null,"spaceShape":8,"displayType":null,"isLike":null,"likeNum":16,"collectNum":2,"groupPrimaryId":0,"templateId":null},{"designPlanId":27208,"designPlanRecommendId":101913,"designPlanName":"Nork.S105169","designPlanCoverPath":"/AA/c_basedesign_recommended/2018/05/28/designPlanRecommended/20180528144755483AES.png","designPlanDesc":null,"designPlanStyleId":null,"designPlanStyleName":null,"spaceType":3,"spaceStyleName":"现代奢华","isSortByReleaseTime":null,"isSortByRenderCount":null,"renderCount":3,"releaseTime":"2018-05-22 17:45:16","applySpaceAreas":"47,57,67,77,87,97,107,117","spaceArea":"47","brandName":null,"isFavorite":null,"spaceShape":3,"displayType":null,"isLike":null,"likeNum":13,"collectNum":1,"groupPrimaryId":0,"templateId":null},{"designPlanId":27199,"designPlanRecommendId":101915,"designPlanName":"NORK.S405040","designPlanCoverPath":"/AA/c_basedesign_recommended/2018/05/28/designPlanRecommended/20180528144754515JqL.png","designPlanDesc":null,"designPlanStyleId":null,"designPlanStyleName":null,"spaceType":3,"spaceStyleName":"美式古典","isSortByReleaseTime":null,"isSortByRenderCount":null,"renderCount":null,"releaseTime":"2018-05-22 17:45:15","applySpaceAreas":"17,22,27","spaceArea":"17","brandName":null,"isFavorite":null,"spaceShape":2,"displayType":null,"isLike":null,"likeNum":16,"collectNum":1,"groupPrimaryId":0,"templateId":null},{"designPlanId":27184,"designPlanRecommendId":101919,"designPlanName":"NORK.S405040","designPlanCoverPath":"/AA/c_basedesign_recommended/2018/05/28/designPlanRecommended/20180528144803272LVN.png","designPlanDesc":null,"designPlanStyleId":null,"designPlanStyleName":null,"spaceType":3,"spaceStyleName":"美式古典","isSortByReleaseTime":null,"isSortByRenderCount":null,"renderCount":5,"releaseTime":"2018-05-22 17:45:14","applySpaceAreas":"47,57,67,77,87,97,107,117","spaceArea":"47","brandName":null,"isFavorite":null,"spaceShape":2,"displayType":null,"isLike":null,"likeNum":14,"collectNum":1,"groupPrimaryId":0,"templateId":null},{"designPlanId":27150,"designPlanRecommendId":101921,"designPlanName":"Nork.S404708","designPlanCoverPath":"/AA/c_basedesign_recommended/2018/05/28/designPlanRecommended/20180528144826666oDw.png","designPlanDesc":null,"designPlanStyleId":null,"designPlanStyleName":null,"spaceType":3,"spaceStyleName":"美式田园","isSortByReleaseTime":null,"isSortByRenderCount":null,"renderCount":1,"releaseTime":"2018-05-22 17:45:14","applySpaceAreas":"47,57,67,77,87,97,107,117","spaceArea":"47","brandName":null,"isFavorite":null,"spaceShape":2,"displayType":null,"isLike":null,"likeNum":12,"collectNum":1,"groupPrimaryId":0,"templateId":null},{"designPlanId":27197,"designPlanRecommendId":101918,"designPlanName":"NORK.S405040","designPlanCoverPath":"/AA/c_basedesign_recommended/2018/05/28/designPlanRecommended/20180528144804352chG.png","designPlanDesc":null,"designPlanStyleId":null,"designPlanStyleName":null,"spaceType":3,"spaceStyleName":"美式古典","isSortByReleaseTime":null,"isSortByRenderCount":null,"renderCount":1,"releaseTime":"2018-05-22 17:45:14","applySpaceAreas":"32,37,42","spaceArea":"32","brandName":null,"isFavorite":null,"spaceShape":2,"displayType":null,"isLike":null,"likeNum":17,"collectNum":2,"groupPrimaryId":0,"templateId":null},{"designPlanId":27181,"designPlanRecommendId":101920,"designPlanName":"NORK.S004958","designPlanCoverPath":"/AA/c_basedesign_recommended/2018/05/28/designPlanRecommended/20180528144802287spg.png","designPlanDesc":null,"designPlanStyleId":null,"designPlanStyleName":null,"spaceType":3,"spaceStyleName":"美式古典","isSortByReleaseTime":null,"isSortByRenderCount":null,"renderCount":null,"releaseTime":"2018-05-22 17:45:14","applySpaceAreas":"17,22,27","spaceArea":"17","brandName":null,"isFavorite":null,"spaceShape":4,"displayType":null,"isLike":null,"likeNum":17,"collectNum":1,"groupPrimaryId":0,"templateId":null},{"designPlanId":27154,"designPlanRecommendId":101922,"designPlanName":"Nork.S404708","designPlanCoverPath":"/AA/c_basedesign_recommended/2018/05/28/designPlanRecommended/20180528144825778EXT.png","designPlanDesc":null,"designPlanStyleId":null,"designPlanStyleName":null,"spaceType":3,"spaceStyleName":"美式田园","isSortByReleaseTime":null,"isSortByRenderCount":null,"renderCount":null,"releaseTime":"2018-05-22 17:45:13","applySpaceAreas":"32,37,42","spaceArea":"32","brandName":null,"isFavorite":null,"spaceShape":8,"displayType":null,"isLike":null,"likeNum":10,"collectNum":0,"groupPrimaryId":0,"templateId":null},{"designPlanId":27157,"designPlanRecommendId":101923,"designPlanName":"Nork.S404708","designPlanCoverPath":"/AA/c_basedesign_recommended/2018/05/28/designPlanRecommended/20180528144824713bql.png","designPlanDesc":null,"designPlanStyleId":null,"designPlanStyleName":null,"spaceType":3,"spaceStyleName":"美式田园","isSortByReleaseTime":null,"isSortByRenderCount":null,"renderCount":null,"releaseTime":"2018-05-22 17:45:13","applySpaceAreas":"17,22,27","spaceArea":"17","brandName":null,"isFavorite":null,"spaceShape":2,"displayType":null,"isLike":null,"likeNum":10,"collectNum":0,"groupPrimaryId":0,"templateId":null},{"designPlanId":27124,"designPlanRecommendId":101929,"designPlanName":"Nork.S104037","designPlanCoverPath":"/AA/c_basedesign_recommended/2018/05/28/designPlanRecommended/20180528144817754wAr.png","designPlanDesc":null,"designPlanStyleId":null,"designPlanStyleName":null,"spaceType":3,"spaceStyleName":"简欧","isSortByReleaseTime":null,"isSortByRenderCount":null,"renderCount":null,"releaseTime":"2018-05-22 17:45:12","applySpaceAreas":"32,37,42","spaceArea":"32","brandName":null,"isFavorite":null,"spaceShape":2,"displayType":null,"isLike":null,"likeNum":13,"collectNum":0,"groupPrimaryId":0,"templateId":null},{"designPlanId":27127,"designPlanRecommendId":101928,"designPlanName":"Nork.S104037","designPlanCoverPath":"/AA/c_basedesign_recommended/2018/05/28/designPlanRecommended/20180528144822333hig.png","designPlanDesc":null,"designPlanStyleId":null,"designPlanStyleName":null,"spaceType":3,"spaceStyleName":"简欧","isSortByReleaseTime":null,"isSortByRenderCount":null,"renderCount":null,"releaseTime":"2018-05-22 17:45:12","applySpaceAreas":"17,22,27","spaceArea":"17","brandName":null,"isFavorite":null,"spaceShape":2,"displayType":null,"isLike":null,"likeNum":10,"collectNum":0,"groupPrimaryId":0,"templateId":null},{"designPlanId":27119,"designPlanRecommendId":101931,"designPlanName":"Nork.S10403","designPlanCoverPath":"/AA/c_basedesign_recommended/2018/05/28/designPlanRecommended/20180528144816726ATA.png","designPlanDesc":null,"designPlanStyleId":null,"designPlanStyleName":null,"spaceType":3,"spaceStyleName":"简欧","isSortByReleaseTime":null,"isSortByRenderCount":null,"renderCount":null,"releaseTime":"2018-05-22 17:45:11","applySpaceAreas":"47,57,67,77,87,97,107,117","spaceArea":"47","brandName":null,"isFavorite":null,"spaceShape":3,"displayType":null,"isLike":null,"likeNum":9,"collectNum":0,"groupPrimaryId":0,"templateId":null}]
     * totalCount : 41
     * statusCode : null
     * msgId : null
     * datalist : null
     * flag : false
     */

    private boolean success;
    private boolean status;
    private Object message;
    private long totalCount;
    private Object statusCode;
    private Object msgId;
    private Object datalist;
    private boolean flag;
    private List<RecommendationBean> obj;

}

@Data
class RecommendationBean {
    /**
     * designPlanId : 30118
     * designPlanRecommendId : 105710
     * designPlanName : 客餐厅(C03_0279)
     * designPlanCoverPath : /AA/c_basedesign_recommended/2018/06/14/17/design/designPlanRecommended/render/181957_1528969608524.png
     * designPlanDesc : null
     * designPlanStyleId : null
     * designPlanStyleName : null
     * spaceType : 3
     * spaceStyleName : 美式田园
     * isSortByReleaseTime : null
     * isSortByRenderCount : null
     * renderCount : 2
     * releaseTime : 2018-06-14 17:50:01
     * applySpaceAreas : 17,22,27,32,37,42,47,57,67,77,87,97,107,117
     * spaceArea : 17
     * brandName : null
     * isFavorite : null
     * spaceShape : 2
     * displayType : null
     * isLike : null
     * likeNum : null
     * collectNum : null
     * groupPrimaryId : 0
     * templateId : null
     */

    private int designPlanId;
    private int designPlanRecommendId;
    private String designPlanName;
    private String designPlanCoverPath;
    private Object designPlanDesc;
    private Object designPlanStyleId;
    private Object designPlanStyleName;
    private int spaceType;
    private String spaceStyleName;
    private Object isSortByReleaseTime;
    private Object isSortByRenderCount;
    private int renderCount;
    private String releaseTime;
    private String applySpaceAreas;
    private Integer spaceArea;
    private Object brandName;
    private Object isFavorite;
    private int spaceShape;
    private Object displayType;
    private Object isLike;
    private Object likeNum;
    private Object collectNum;
    private int groupPrimaryId;
    private Object templateId;
}