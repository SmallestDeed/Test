package com.sandu.search.service.design;

import com.sandu.search.common.tools.MessageUtil;
import com.sandu.search.entity.common.PageVo;
import com.sandu.search.entity.designplan.vo.CollectRecommendedVo;
import com.sandu.search.entity.designplan.vo.RecommendationPlanSearchVo;
import com.sandu.search.entity.designplan.vo.RecommendationPlanVo;
import com.sandu.search.entity.designplan.vo.ShopRecommendedPlanVo;
import com.sandu.search.entity.elasticsearch.index.RecommendationPlanIndexMappingData;
import com.sandu.search.entity.elasticsearch.response.SearchObjectResponse;
import com.sandu.search.entity.user.LoginUser;
import com.sandu.search.exception.RecommendationPlanSearchException;

import java.util.List;

/**
 * 产品搜索服务
 *
 * @date 20180103
 * @auth pengxuangang
 */
public interface RecommendationPlanSearchService {

    /**
     * 通过条件搜索设计方案
     *
     * @param recommendationPlanVo                     设计方案搜索匹配对象
     * @param pageVo                                   分页对象
     * @return
     * @throws RecommendationPlanSearchException       设计方案搜索异常
     */
    SearchObjectResponse searchRecommendationPlan(RecommendationPlanSearchVo recommendationPlanVo, PageVo pageVo) throws RecommendationPlanSearchException;

    /**
     * 通过设计方案ID搜索设计方案
     *
     * @param recommendationPlanId                         设计方案ID
     * @return
     * @throws RecommendationPlanSearchException           设计方案搜索异常
     */
    RecommendationPlanIndexMappingData searchRecommendationPlanById(Integer recommendationPlanId) throws RecommendationPlanSearchException;

    /**
     * 通过条件搜索设计方案
     *
     * @param recommendationPlanVo                     设计方案搜索匹配对象
     * @return
     * @throws RecommendationPlanSearchException       设计方案搜索异常
     */
    SearchObjectResponse searchGoodDesign(RecommendationPlanSearchVo recommendationPlanVo, PageVo pageVo) throws RecommendationPlanSearchException;

    /**
     * 通过条件搜索设计方案
     *
     * @param recommendedPlanIdList   索引Ids
     * @param fullHousePlanIdList   索引Ids
     * @param recommendationPlanVo
     * @return
     * @throws RecommendationPlanSearchException       设计方案搜索异常
     */
    SearchObjectResponse searchRecommendationPlanByIds(List<Integer> recommendedPlanIdList, List<Integer> fullHousePlanIdList, RecommendationPlanSearchVo recommendationPlanVo, PageVo pageVo) throws RecommendationPlanSearchException;

    /**
     * 通过条件搜索设计方案
     *
     * @param recommendationPlanVo   店铺方案关联查询条件
     * @return
     * @throws RecommendationPlanSearchException       设计方案搜索异常
     */
    SearchObjectResponse searchCompanyShopPlanRel(RecommendationPlanSearchVo recommendationPlanVo, PageVo pageVo) throws RecommendationPlanSearchException;

    /**
     * 参数校验
     * @param displayType  推荐方案类型
     * @param platformCode 平台编码
     * @return
     */
    MessageUtil paramVerify(String displayType, String platformCode);

    /**
     * 参数校验
     * @param spuId  商品Id
     * @param platformCode 平台编码
     * @return
     */
    MessageUtil paramVerify(String platformCode, Integer spuId);

    /**
     * 获取企业ID
     * @param platformCode
     * @param loginUser
     * @return
     */
    Integer getCompanyInfo(String platformCode, LoginUser loginUser);

    /**
     * 设置查询推荐方案条件
     * @param recommendationPlanSearchVo
     * @return
     */
    MessageUtil searchRecommendedCondition(RecommendationPlanSearchVo recommendationPlanSearchVo, LoginUser loginUser);

    /**
     * 处理推荐方案结果集列表
     * @param recommendationPlanIndexMappingDataList
     * @param userId
     * @return
     */
    List<RecommendationPlanVo> dispostRecommendPlanResultList(List<RecommendationPlanIndexMappingData> recommendationPlanIndexMappingDataList, Integer userId, String platformCode, Integer companyId);

    /**
     * 获取收藏推荐方案列表
     * @param collectRecommendedVo
     * @return
     */
    List<Integer> queryCollectRecommendedPlanList(RecommendationPlanSearchVo collectRecommendedVo) throws RecommendationPlanSearchException;

    /**
     * 获取收藏全屋方案列表
     * @param collectRecommendedVo
     * @return
     */
    List<Integer> queryCollectFullHousePlanList(RecommendationPlanSearchVo collectRecommendedVo) throws RecommendationPlanSearchException;

    int queryCollectRecommendedPlanCount(RecommendationPlanSearchVo collectRecommendedVo);

    List<String> getAllSuperiorPlan();

}
