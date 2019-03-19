package com.sandu.search.service.metadata;

import com.sandu.search.entity.designplan.po.DesignPlanProductPo;
import com.sandu.search.entity.designplan.po.RecommendedPlanProductPo;
import com.sandu.search.entity.elasticsearch.po.*;
import com.sandu.search.entity.elasticsearch.po.house.HouseLivingPo;
import com.sandu.search.entity.elasticsearch.po.metadate.*;
import com.sandu.search.exception.MetaDataException;

import java.util.List;

/**
 * 元数据服务
 *
 * @date 20171213
 * @auth pengxuangang
 */
public interface MetaDataService {

    /**
     * 查询产品分类元数据
     *
     * @return
     */
    List<ProductCategoryPo> queryProductCategoryMetaData() throws MetaDataException;

    /**
     * 查询产品风格元数据
     *
     * @return
     */
    List<ProductStylePo> queryProductStyleMetaData() throws MetaDataException;

    /**
     * 查询产品材质元数据
     *
     * @return
     */
    List<ProductTexturePo> queryProductTextureMetaData() throws MetaDataException;

    /**
     * 查询品牌元数据
     *
     * @return
     */
    List<BrandPo> queryBrandMetaData() throws MetaDataException;

    /**
     * 查询产品分类关联信息
     *
     * @return
     */
    List<ProductCategoryRelPo> queryProductCategoryRelMetaData() throws MetaDataException;

    /**
     * 查询图片资源元数据
     *
     * @return
     */
    List<ResPicPo> queryResPicMetaData() throws MetaDataException;

    /**
     * 根据ID查询图片资源元数据
     *
     * @return
     */
    ResPicPo getResPicMetaDataById(int id) throws MetaDataException;

    /**
     * 查询产品组合关联元数据
     *
     * @return
     */
    List<ProductGroupRelPo> queryProductGroupRelMetaData() throws MetaDataException;

    /**
     * 查询产品组合元数据
     *
     * @return
     */
    List<ProductGroupPo> queryProductGroupMetaData() throws MetaDataException;

    /**
     * 查询草稿设计方案产品元数据
     *
     * @return
     */
    List<DesignPlanProductPo> queryTempDesignPlanProductMetaData() throws MetaDataException;

    /**
     * 查询推荐设计方案产品元数据
     *
     * @return
     */
    List<DesignPlanProductPo> queryRecommendDesignPlanProductMetaData() throws MetaDataException;

    /**
     * 查询自定义设计方案产品元数据
     *
     * @return
     */
    List<DesignPlanProductPo> queryDiyDesignPlanProductMetaData() throws MetaDataException;

    /**
     * 查询系统字典元数据
     *
     * @return
     * @throws MetaDataException
     */
    List<SystemDictionaryPo> querySystemDictionaryMetaData() throws MetaDataException;

    /**
     * 查询户型小区元数据
     *
     * @return
     */
    List<HouseLivingPo> queryHouseLivingMetaData() throws MetaDataException;

    /**
     * 查询区域元数据
     *
     * @return
     */
    List<AreaPo> queryAreaMetaData() throws MetaDataException;

    /**
     * 查询公司元数据
     *
     * @return
     */
    List<CompanyPo> queryCompanyMetaData() throws MetaDataException;

    /**
     * 查询公司分类关联元数据
     *
     * @return
     */
    List<CompanyCategoryRelPo> queryCompanyCategoryRelMetaData() throws MetaDataException;

    /**
     * 查询设计方案样板房元数据
     *
     * @return
     * @throws MetaDataException
     */
    List<DesignTemplatePo> queryDesignTemplatePoMetaData() throws MetaDataException;

    /**
     * 查询所有平台产品数据(平台数据过滤)
     *
     * @return
     */
    List<ProductPlatformRelPo> queryAllPlatformProductMetaData() throws MetaDataException;

    /**
     * 查询产品属性元信息
     *
     * @return
     */
    List<ProductAttributePo> queryProductAttrMetaData() throws MetaDataException;

    /**
     * 查询产品使用次数统计信息
     *
     * @return
     */
    List<ProductUsagePo> queryProductUsageCountSatatistics() throws MetaDataException;

    /**
     * 查询联盟品牌元数据
     *
     * @return
     */
    List<UnionBrandPo> queryUnionBrandMetaData() throws MetaDataException;


    /**
     * 查询设计方案品牌元数据
     *
     * @return
     */
    List<DesignPlanBrandPo> queryDesignPlanBrandMetaData() throws MetaDataException;

    /**
     * 查询空间元数据
     *
     * @return
     */
    List<SpaceCommonPo> querySpaceCommonMetaData() throws MetaDataException;

    /**
     * 查询小程序公司品牌配置
     *
     * @return
     * @throws MetaDataException
     */
    List<BaseCompanyMiniProgramConfig> queryBaseCompanyMiniProgramConfig() throws MetaDataException;

    List<SpuSaleInfoPo> querySpuInfoMetaData() throws MetaDataException;

    List<GoodsSkuPo> querygoodsSkuMetaData() throws MetaDataException;

    List<BaseProductDataPo> queryBaseProductData() throws MetaDataException;

    SpuSaleInfoPo querySpuInfoById(Integer id) throws Exception;

    List<BaseProductDataPo> queryBaseProductDataById(Integer spuId) throws MetaDataException;

    GoodsSkuPo querygoodsSkuMetaDataByspuId(int spuId, int productId) throws MetaDataException;

    ProductPlatformRelPo queryAllPlatformProductById(Integer productId) throws MetaDataException;

    List<ProductCategoryPo> queryProductCategoryByProductId(int productId) throws MetaDataException;


    /**
     * 查询方案平台元数据
     *
     * @return
     * @throws MetaDataException
     */
    List<DesignPlanPlatformRelPo> queryAllPlatformPlanMetaData() throws MetaDataException;

    /**
     * 查询方案平台元数据
     *
     * @return
     * @throws MetaDataException
     */
    List<DesignPlanPlatformRelPo> queryPlatformPlanByIdsMetaData(List<Integer> recommendedPlanIdList, int type) throws MetaDataException;

    /**
     * 查询用户元数据
     *
     * @return
     */
    List<SysUserPo> queryUserMetaData() throws MetaDataException;

    /**
     * 查询图片资源元数据
     *
     * @return
     */
    List<ResPicPo> queryRecommendPlanCoverPicMetaData() throws MetaDataException;

    /**
     * 查询推荐方案最后渲染的图片资源元数据
     *
     * @return
     */
    List<ResPicPo> queryRecommendedPlanFinallyRenderPicMetaData() throws MetaDataException;

    /**
     * 查询方案小区元数据
     *
     * @return
     */
    List<RecommendedPlanLivingPo> queryPlanLivingMetaData() throws MetaDataException;

    /**
     * 查询角色菜单元数据
     *
     * @return
     */
    List<SysRoleFuncPo> queryRoleFuncMetaData() throws MetaDataException;

    /**
     * 查询推荐方案元数据
     *
     * @return
     */
    List<DesignPlanRecommendedPo> queryDesignPlanRecommendedMetaData() throws MetaDataException;

    /**
     * 通过图片id获取图片路径
     * from res_pic
     * 
     * @author huangsongbo
     * @param picId
     * @return
     */
	String getPicPathFromResPicById(Integer picId);

	/**
	 * 
	 * @author huangsongbo
	 * @param picIdList
	 * @return
	 */
	List<ResPicPo> getPicListByPicIdList(List<Integer> picIdList);

    /**
     * 查询店铺元数据
     *
     * @return
     * @throws MetaDataException
     */
    List<CompanyShopPo> queryCompanyShopMetaData() throws MetaDataException;

    /**
     * 查询装修报价元数据
     *
     * @return
     * @throws MetaDataException
     */
    List<DecoratePricePo> queryPlanDecoratePriceMetaData() throws MetaDataException;

    /**
     * 查询装修报价元数据
     *
     * @return
     * @throws MetaDataException
     */
    List<DecoratePricePo> queryPlanDecoratePriceByIdsMetaData(Integer planId, int type) throws MetaDataException;

    /**
     * 查询店铺方案元数据
     *
     * @return
     */
    List<CompanyShopPlanPo> queryCompanyShopPlanMetaData() throws MetaDataException;

    /**
     * 根据PlanID查询店铺方案元数据
     *
     * @return
     */
    List<CompanyShopPlanPo> queryCompanyShopPlanByPlanIdMetaData(Integer planId) throws MetaDataException;

    /**
     * 查询推荐方案产品元数据
     *
     * @return
     */
    List<RecommendedPlanProductPo> queryRecommendedPlanProductMetaData() throws MetaDataException;

    /**
     * 根据方案ID查询推荐方案产品元数据
     *
     * @return
     */
    List<Integer> queryRecommendedPlanProductByPlanIdMetaData(Integer planId) throws MetaDataException;

    /**
     * 根据全屋方案ids查店铺方案数据
     * @param fullHousePlanIdList
     * @return
     */
    List<CompanyShopPlanPo> queryCompanyShopPlanByFullHouseIds(List<Integer> fullHousePlanIdList) throws MetaDataException;

    /**
     * 根据店铺Id查询店铺方案
     * @param shopId
     * @return
     */
    List<CompanyShopPlanPo> queryPlanInfoByShopId(Integer shopId) throws MetaDataException;

    /**
     * 查询组合产品详细信息
     * @return
     * @throws MetaDataException
     */
    List<ProductGroupDetailPo> queryProductGroupDetail() throws MetaDataException;
}
