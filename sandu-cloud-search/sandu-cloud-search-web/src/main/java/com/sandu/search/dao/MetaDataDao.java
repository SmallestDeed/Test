package com.sandu.search.dao;

import com.sandu.search.entity.designplan.po.DesignPlanProductPo;
import com.sandu.search.entity.elasticsearch.po.metadate.CompanyPo;
import com.sandu.search.entity.elasticsearch.po.metadate.ProductAttributePo;
import com.sandu.search.entity.elasticsearch.po.metadate.RecommendedPlanFavoritePo;
import com.sandu.search.entity.elasticsearch.po.metadate.SysUserPo;
import com.sandu.search.entity.product.vo.BaiMoProductSearchConditionsVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 元数据数据访问层
 *
 * @date 20171213
 * @auth pengxuangang
 */
@Repository
public interface MetaDataDao {

    /**
     * 获取草稿设计方案产品元数据
     *
     * @return
     */
    DesignPlanProductPo getTempDesignPlanProductMetaDataById(@Param("id") Integer id);

    /**
     * 获取推荐设计方案产品元数据
     *
     * @return
     */
    DesignPlanProductPo getRecommendDesignPlanProductMetaDataById(@Param("id") Integer id);

    /**
     * 获取自定义设计方案产品元数据
     *
     * @return
     */
    DesignPlanProductPo getDiyDesignPlanProductMetaDataById(@Param("id") Integer id);

    /**
     * 获取自定义推荐方案收藏状态元数据
     *
     * @return
     */
    List<RecommendedPlanFavoritePo> getRecommendationPlanCollectBidMetaDataByIds(@Param("recommendIdList") List<Integer> recommendIdList, @Param("fullHouseIdList")List<Integer> fullHouseIdList, @Param("userId") Integer userId);

    List<ProductAttributePo> queryProductAttrMetaDataById(@Param("id") Integer id);

    BaiMoProductSearchConditionsVO getProductInfoById(@Param("id") Integer id);

    /**
     * 获取企业信息
     * @param companyId
     * @return
     */
    CompanyPo queryCompanyById(@Param("companyId") Integer companyId);

    /**
     * 获取商品下的产品
     * @param spuId
     * @return
     */
    List<Integer> getProductIdsBySpuId(@Param("spuId") Integer spuId);

    /**
     * 获取商品下的产品
     * @param userId
     * @return
     */
    SysUserPo queryUserById(@Param("userId") Integer userId);

    /**
     * 根据APPID查询可见品牌id
     * @param appId
     * @return
     */
    String getEnableBrandIdsByAppId(String appId);

    /**
     * 根据appId查询企业信息
     * @param appId
     * @return
     */
    CompanyPo queryCompanyPoByAppId(String appId);

    CompanyPo getCompanyByAppId(@Param("appId") String appId);
}
