package com.sandu.search.dao;

import com.sandu.search.entity.designplan.po.DesignPlanProductPo;
import com.sandu.search.entity.designplan.po.RecommendedPlanProductPo;
import com.sandu.search.entity.elasticsearch.po.*;
import com.sandu.search.entity.elasticsearch.po.house.HouseLivingPo;
import com.sandu.search.entity.elasticsearch.po.metadate.*;
import com.sandu.search.exception.MetaDataException;
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
     * 查询产品分类元数据
     *
     * @return
     */
    List<ProductCategoryPo> queryProductCategoryMetaData();

    /**
     * 查询产品风格元数据
     *
     * @return
     */
    List<ProductStylePo> queryProductStyleMetaData();

    /**
     * 查询产品材质元数据
     *
     * @return
     */
    List<ProductTexturePo> queryProductTextureMetaData();

    /**
     * 查询品牌元数据
     *
     * @return
     */
    List<BrandPo> queryBrandMetaData();

    /**
     * 查询产品分类关联信息
     *
     * @return
     */
    List<ProductCategoryRelPo> queryProductCategoryRelMetaData();

    /**
     * 查询图片资源元数据
     *
     * @return
     */
    List<ResPicPo> queryResPicMetaData();

    /**
     * 根据ID查询图片资源元数据
     *
     * @return
     */
    ResPicPo getResPicMetaDataById(@Param("id") int id);

    /**
     * 查询产品组合关联元数据
     *
     * @return
     */
    List<ProductGroupRelPo> queryProductGroupRelMetaData();

    /**
     * 查询产品组合元数据
     *
     * @return
     */
    List<ProductGroupPo> queryProductGroupMetaData();

    /**
     * 查询草稿设计方案产品元数据
     *
     * @return
     */
    List<DesignPlanProductPo> queryTempDesignPlanProductMetaData();

    /**
     * 查询推荐设计方案产品元数据
     *
     * @return
     */
    List<DesignPlanProductPo> queryRecommendDesignPlanProductMetaData();

    /**
     * 查询自定义设计方案产品元数据
     *
     * @return
     */
    List<DesignPlanProductPo> queryDiyDesignPlanProductMetaData();

    /**
     * 查询系统字典元数据
     *
     * @return
     * @throws MetaDataException
     */
    List<SystemDictionaryPo> querySystemDictionaryMetaData();

    /**
     * 查询户型小区元数据
     *
     * @return
     */
    List<HouseLivingPo> queryHouseLivingMetaData();

    /**
     * 查询区域元数据
     *
     * @return
     */
    List<AreaPo> queryAreaMetaData();

    /**
     * 查询公司元数据
     *
     * @return
     */
    List<CompanyPo> queryCompanyMetaData();

    /**
     * 查询公司分类关联元数据
     *
     * @return
     */
    List<CompanyCategoryRelPo> queryCompanyCategoryRelMetaData();

    /**
     * 查询设计方案样板房元数据
     *
     * @return
     */
    List<DesignTemplatePo> queryDesignTemplatePoMetaData();

    /**
     * 查询2B平台产品数据
     *
     * @return
     */
    List<ProductPlatformRelPo> queryToBPlatformProductMetaData();

    /**
     * 查询2C平台产品数据
     *
     * @return
     */
    List<ProductPlatformRelPo> queryToCPlatformProductMetaData();

    /**
     * 查询Sandu平台产品数据
     *
     * @return
     */
    List<ProductPlatformRelPo> querySanduPlatformProductMetaData();

    /**
     * 查询产品属性元信息
     *
     * @return
     */
    List<ProductAttributePo> queryProductAttrMetaData();

    /**
     * 查询产品使用次数统计
     *
     * @return
     */
    List<ProductUsagePo> queryProductUsageCountSatatistics();

    /**
     * 查询联盟品牌元数据
     *
     * @return
     */
    List<UnionBrandPo> queryUnionBrandMetaData();


    /**
     * 查询设计方案品牌元数据
     *
     * @return
     */
    List<DesignPlanBrandPo> queryDesignPlanBrandMetaData();

    /**
     * 查询空间元数据
     *
     * @return
     */
    List<SpaceCommonPo> querySpaceCommonMetaData();

    List<BaseCompanyMiniProgramConfig> queryBaseCompanyMiniProgramConfigMetaData();

    BaseCompanyMiniProgramConfig queryBaseCompanyMiniProgramConfigMetaDataByAppId(@Param("appId") String appId);


    /**
     * 查询商品元数据
     *
     * @return
     */
    List<GoodsSkuPo> queryGoodsSkuMetaData();

    List<SpuSaleInfoPo> querySpuInfoMetaData();

    List<BaseProductDataPo> queryBaseProductData();

    SpuSaleInfoPo querySpuInfoById(@Param("spuId") Integer id);

    List<BaseProductDataPo> queryBaseProductDataById(@Param("spuId") Integer spuId);

    GoodsSkuPo queryGoodsSkuMetaDataById(@Param("spuId") int spuId, @Param("productId") int productId);

    ProductPlatformRelPo queryAllPlatformProductById(@Param("productId") Integer productId);

    List<ProductCategoryPo> queryProductCategoryById(@Param("categoryId") int productId);

    List<ProductCategoryRelPo> queryProductCategoryRelByProductId(@Param("productId") int productId);

    /**
     * 查询ToB平台方案数据
     *
     * @return
     */
    List<DesignPlanPlatformRelPo> queryToBPlatformPlanMetaData();

    /**
     * 查询ToC平台方案数据
     *
     * @return
     */
    List<DesignPlanPlatformRelPo> queryToCPlatformPlanMetaData();

    /**
     * 查询ToB平台方案数据
     *
     * @return
     */
    List<DesignPlanPlatformRelPo> queryToBPlatformByPlanIdsMetaData(@Param("recommendedPlanIdList") List<Integer> recommendedPlanIdList, @Param("type") int type);

    /**
     * 查询ToC平台方案数据
     *
     * @return
     */
    List<DesignPlanPlatformRelPo> queryToCPlatformByPlanIdsMetaData(@Param("recommendedPlanIdList") List<Integer> recommendedPlanIdList, @Param("type") int type);

    /**
     * 查询用户元数据
     *
     * @return
     */
    List<SysUserPo> queryUserMetaData();

    /**
     * 查询方案小区元数据
     *
     * @return
     */
    List<RecommendedPlanLivingPo> queryPlanLivingMetaData();

    /**
     * 查询推荐方案封面图片资源元数据
     *
     * @return
     */
    List<ResPicPo> queryRecommendPlanCoverPicMetaData();

    /**
     * 查询推荐方案最后渲染的图片资源元数据
     *
     * @return
     */
    List<ResPicPo> queryRecommendedPlanFinallyRenderPicMetaData();

    /**
     * 查询角色菜单元数据
     *
     * @return
     */
    List<SysRoleFuncPo> queryRoleFuncMetaData();

    /**
     * 查询推荐方案元数据
     *
     * @return
     */
    List<DesignPlanRecommendedPo> queryDesignPlanRecommendedMetaData();

	String selectPicPathFromResPicById(@Param("id") Integer id);

    /**
     * 查询店铺元数据
     *
     * @return
     */
    List<CompanyShopPo> queryCompanyShopMetaData();

	List<ResPicPo> selectFromResPicByIdList(@Param("idList") List<Integer> idList);


    /**
     * 查询装修报价元数据
     *
     * @return
     */
    List<DecoratePricePo> queryPlanDecoratePriceMetaData();

    /**
     * 查询装修报价元数据
     *
     * @return
     */
    List<DecoratePricePo> queryPlanDecoratePriceByIdsMetaData(@Param("planId") Integer planId, @Param("type") int type);

    /**
     * 查询店铺方案元数据
     *
     * @return
     */
    List<CompanyShopPlanPo> queryCompanyShopPlanMetaData();

    /**
     * 根据PlanID查询店铺方案元数据
     *
     * @return
     */
    List<CompanyShopPlanPo> queryCompanyShopPlanByPlanIdMetaData(@Param("planId") Integer planId);

    /**
     * 查询推荐方案产品元数据
     *
     * @return
     */
    List<RecommendedPlanProductPo> queryRecommendedPlanProductMetaData();

    /**
     * 查询推荐方案产品元数据
     *
     * @return
     */
    List<Integer> queryRecommendedPlanProductByPlanIdMetaData(@Param("planId") Integer planId);

    /**
     * 根据全屋方案ids查询店铺方案
     * @param list
     * @return
     */
    List<CompanyShopPlanPo> selectCompanyShopPlanByFullHouseIds(List<Integer> list);

    /**
     * 根据店铺Id查询店铺方案
     * @param shopId
     * @return
     */
    List<CompanyShopPlanPo> queryPlanInfoByShopId(@Param("shopId") Integer shopId);
    /**
     * 查询组合产品详细信息元数据
     * @return
     */
    List<ProductGroupDetailPo> queryProductGroupDetail();
}
