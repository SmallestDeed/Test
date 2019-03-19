package com.sandu.search.service.metadata.impl;

import com.sandu.search.common.util.Utils;
import com.sandu.search.dao.MetaDataDao;
import com.sandu.search.entity.designplan.po.DesignPlanProductPo;
import com.sandu.search.entity.designplan.po.RecommendedPlanProductPo;
import com.sandu.search.entity.elasticsearch.po.*;
import com.sandu.search.entity.elasticsearch.po.house.HouseLivingPo;
import com.sandu.search.entity.elasticsearch.po.metadate.*;
import com.sandu.search.entity.product.dto.SplitTextureDTO;
import com.sandu.search.exception.MetaDataException;
import com.sandu.search.service.index.IndexGroupProductService;
import com.sandu.search.service.index.ProductIndexService;
import com.sandu.search.service.metadata.MetaDataService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 元数据服务
 *
 * @date 20171213
 * @auth pengxuangang
 */
@Slf4j
@Service("metaDataService")
public class MetaDataServiceImpl implements MetaDataService {

    private final static String CLASS_LOG_PREFIX = "元数据服务:";

    private final MetaDataDao metaDataDao;
    private final ProductIndexService productIndexService;
    private final IndexGroupProductService indexGroupProductService;

    @Autowired
    public MetaDataServiceImpl(MetaDataDao metaDataDao,
                               ProductIndexService productIndexService,
                               IndexGroupProductService indexGroupProductService) {
        this.metaDataDao = metaDataDao;
        this.productIndexService = productIndexService;
        this.indexGroupProductService = indexGroupProductService;
    }

    @Override
    public List<ProductCategoryPo> queryProductCategoryMetaData() throws MetaDataException {

        //查询产品分类元数据
        log.info(CLASS_LOG_PREFIX + "查询产品分类元数据...");
        List<ProductCategoryPo> productCategoryPoList;
        try {
            productCategoryPoList = metaDataDao.queryProductCategoryMetaData();
        } catch (Exception e) {
            log.error(CLASS_LOG_PREFIX + "查询产品分类元数据失败!Exception:{}", e);
            throw new MetaDataException(CLASS_LOG_PREFIX + "查询产品分类元数据失败!Exception:{}" + e);
        }
        log.info(CLASS_LOG_PREFIX + "查询产品分类元数据完成.List<ProductCategoryPo>:{}", null == productCategoryPoList ? 0 : productCategoryPoList.size());
        return productCategoryPoList;
    }

    @Override
    public List<ProductStylePo> queryProductStyleMetaData() throws MetaDataException {
        //查询产品风格元数据
        log.info(CLASS_LOG_PREFIX + "查询产品风格元数据...");
        List<ProductStylePo> productStylePoList;
        try {
            productStylePoList = metaDataDao.queryProductStyleMetaData();
        } catch (Exception e) {
            log.error(CLASS_LOG_PREFIX + "查询产品风格元数据失败!Exception:{}", e);
            throw new MetaDataException(CLASS_LOG_PREFIX + "查询产品风格元数据失败!Exception:{}" + e);
        }
        log.info(CLASS_LOG_PREFIX + "查询产品风格元数据完成.List<ProductStylePo>:{}", null == productStylePoList ? 0 : productStylePoList.size());
        return productStylePoList;
    }

    @Override
    public List<ProductTexturePo> queryProductTextureMetaData() throws MetaDataException {
        //查询产品材质元数据
        log.info(CLASS_LOG_PREFIX + "查询产品材质元数据...");
        List<ProductTexturePo> productTexturePoList;
        try {
            productTexturePoList = metaDataDao.queryProductTextureMetaData();
        } catch (Exception e) {
            log.error(CLASS_LOG_PREFIX + "查询产品材质元数据失败!Exception:{}", e);
            throw new MetaDataException(CLASS_LOG_PREFIX + "查询产品材质元数据失败!Exception:{}" + e);
        }
        log.info(CLASS_LOG_PREFIX + "查询产品材质元数据完成.List<ProductStylePo>:{}", null == productTexturePoList ? 0 : productTexturePoList.size());
        return productTexturePoList;
    }

    @Override
    public List<BrandPo> queryBrandMetaData() throws MetaDataException {

        //查询品牌元数据
        log.info(CLASS_LOG_PREFIX + "查询品牌元数据...");
        List<BrandPo> brandPoList;
        try {
            brandPoList = metaDataDao.queryBrandMetaData();
        } catch (Exception e) {
            log.error(CLASS_LOG_PREFIX + "查询品牌元数据失败!Exception:{}", e);
            throw new MetaDataException(CLASS_LOG_PREFIX + "查询品牌元数据失败!Exception:{}" + e);
        }
        log.info(CLASS_LOG_PREFIX + "查询品牌元数据完成.List<BrandPo>:{}", null == brandPoList ? 0 : brandPoList.size());

        return brandPoList;
    }

    @Override
    public List<ProductCategoryRelPo> queryProductCategoryRelMetaData() throws MetaDataException {
        //查询产品分类关联信息
        log.info(CLASS_LOG_PREFIX + "查询产品分类关联信息...");
        List<ProductCategoryRelPo> productCategoryRelList;
        try {
            productCategoryRelList = metaDataDao.queryProductCategoryRelMetaData();
        } catch (Exception e) {
            log.error(CLASS_LOG_PREFIX + "查询产品分类关联信息失败!Exception:{}", e);
            throw new MetaDataException(CLASS_LOG_PREFIX + "查询产品分类关联信息失败!Exception:{}" + e);
        }
        log.info(CLASS_LOG_PREFIX + "查询产品分类关联信息完成.List<ProductCategoryRelPo>:{}", null == productCategoryRelList ? 0 : productCategoryRelList.size());

        return productCategoryRelList;
    }

    @Override
    public List<ResPicPo> queryResPicMetaData() throws MetaDataException {
        //查询图片资源元数据
        log.info(CLASS_LOG_PREFIX + "查询图片资源元数据...");
        List<ResPicPo> resPicList;
        try {
            resPicList = metaDataDao.queryResPicMetaData();
        } catch (Exception e) {
            log.error(CLASS_LOG_PREFIX + "查询图片资源元数据失败!Exception:{}", e);
            throw new MetaDataException(CLASS_LOG_PREFIX + "查询图片资源元数据失败!Exception:{}" + e);
        }
        log.info(CLASS_LOG_PREFIX + "查询图片资源元数据完成.List<ResPicPo>:{}", null == resPicList ? 0 : resPicList.size());

        return resPicList;
    }

    @Override
    public ResPicPo getResPicMetaDataById(int id) throws MetaDataException {
        //根据ID查询图片资源元数据
        log.info(CLASS_LOG_PREFIX + "根据ID查询图片资源元数据...");
        ResPicPo resPicPo;
        try {
            resPicPo = metaDataDao.getResPicMetaDataById(id);
        } catch (Exception e) {
            log.error(CLASS_LOG_PREFIX + "根据ID查询图片资源元数据失败!Exception:{}", e);
            throw new MetaDataException(CLASS_LOG_PREFIX + "根据ID查询图片资源元数据失败!Exception:{}" + e);
        }
        log.info(CLASS_LOG_PREFIX + "根据ID查询图片资源元数据完成.resPicPo:{}", resPicPo);

        return resPicPo;
    }

    @Override
    public List<ProductGroupRelPo> queryProductGroupRelMetaData() throws MetaDataException {
        //查询产品组合关联元数据
        log.info(CLASS_LOG_PREFIX + "查询产品组合关联元数据...");
        List<ProductGroupRelPo> productGroupRelPoList;
        try {
            productGroupRelPoList = metaDataDao.queryProductGroupRelMetaData();
        } catch (Exception e) {
            log.error(CLASS_LOG_PREFIX + "查询产品组合关联元数据失败!Exception:{}", e);
            throw new MetaDataException(CLASS_LOG_PREFIX + "查询产品组合关联元数据失败!Exception:{}" + e);
        }
        log.info(CLASS_LOG_PREFIX + "查询产品组合关联元数据完成.List<ProductGroupRelPo>:{}", null == productGroupRelPoList ? 0 : productGroupRelPoList.size());

        return productGroupRelPoList;
    }

    @Override
    public List<ProductGroupPo> queryProductGroupMetaData() throws MetaDataException {
        //查询产品组合元数据
        log.info(CLASS_LOG_PREFIX + "查询产品组合元数据...");
        List<ProductGroupPo> productGroupPoList;
        try {
            productGroupPoList = metaDataDao.queryProductGroupMetaData();
        } catch (Exception e) {
            log.error(CLASS_LOG_PREFIX + "查询产品组合元数据失败!Exception:{}", e);
            throw new MetaDataException(CLASS_LOG_PREFIX + "查询产品组合元数据失败!Exception:{}" + e);
        }
        log.info(CLASS_LOG_PREFIX + "查询产品组合元数据完成.List<ProductGroupPo>:{}", null == productGroupPoList ? 0 : productGroupPoList.size());

        return productGroupPoList;
    }

    @Override
    public List<DesignPlanProductPo> queryTempDesignPlanProductMetaData() throws MetaDataException {

        //查询草稿设计方案产品元数据
        log.info(CLASS_LOG_PREFIX + "查询草稿设计方案产品元数据...");

        List<DesignPlanProductPo> designPlanProductPoList;
        try {
            designPlanProductPoList = metaDataDao.queryTempDesignPlanProductMetaData();
        } catch (Exception e) {
            log.error(CLASS_LOG_PREFIX + "查询草稿设计方案产品元数据失败!Exception:{}", e);
            throw new MetaDataException(CLASS_LOG_PREFIX + "查询草稿设计方案产品元数据失败!Exception:{}" + e);
        }

        log.info(CLASS_LOG_PREFIX + "查询草稿设计方案产品元数据完成.List<DesignPlanProductPo>数据条数:{}", null == designPlanProductPoList ? 0 : designPlanProductPoList.size());

        return designPlanProductPoList;
    }

    @Override
    public List<DesignPlanProductPo> queryRecommendDesignPlanProductMetaData() throws MetaDataException {

        //查询推荐设计方案产品元数据
        log.info(CLASS_LOG_PREFIX + "查询推荐设计方案产品元数据...");

        List<DesignPlanProductPo> designPlanProductPoList;
        try {
            designPlanProductPoList = metaDataDao.queryRecommendDesignPlanProductMetaData();
        } catch (Exception e) {
            log.error(CLASS_LOG_PREFIX + "查询推荐设计方案产品元数据失败!Exception:{}", e);
            throw new MetaDataException(CLASS_LOG_PREFIX + "查询推荐设计方案产品元数据失败!Exception:{}" + e);
        }

        log.info(CLASS_LOG_PREFIX + "查询推荐设计方案产品元数据完成.List<DesignPlanProductPo>数据条数:{}", null == designPlanProductPoList ? 0 : designPlanProductPoList.size());

        return designPlanProductPoList;
    }

    @Override
    public List<DesignPlanProductPo> queryDiyDesignPlanProductMetaData() throws MetaDataException {

        //查询自定义设计方案产品元数据
        log.info(CLASS_LOG_PREFIX + "查询自定义设计方案产品元数据...");

        List<DesignPlanProductPo> designPlanProductPoList;
        try {
            designPlanProductPoList = metaDataDao.queryDiyDesignPlanProductMetaData();
        } catch (Exception e) {
            log.error(CLASS_LOG_PREFIX + "查询自定义设计方案产品元数据失败!Exception:{}", e);
            throw new MetaDataException(CLASS_LOG_PREFIX + "查询自定义设计方案产品元数据失败!Exception:{}" + e);
        }

        log.info(CLASS_LOG_PREFIX + "查询自定义设计方案产品元数据完成.List<DesignPlanProductPo>数据条数:{}", null == designPlanProductPoList ? 0 : designPlanProductPoList.size());

        return designPlanProductPoList;
    }

    @Override
    public List<SystemDictionaryPo> querySystemDictionaryMetaData() throws MetaDataException {
        //查询系统字典元数据
        log.info(CLASS_LOG_PREFIX + "查询系统字典元数据...");
        List<SystemDictionaryPo> systemDictionaryPoList;
        try {
            systemDictionaryPoList = metaDataDao.querySystemDictionaryMetaData();
        } catch (Exception e) {
            log.error(CLASS_LOG_PREFIX + "查询系统字典元数据失败!Exception:{}", e);
            throw new MetaDataException(CLASS_LOG_PREFIX + "查询系统字典元数据失败!Exception:{}" + e);
        }
        log.info(CLASS_LOG_PREFIX + "查询系统字典元数据完成.List<SystemDictionaryPo>:{}", null == systemDictionaryPoList ? 0 : systemDictionaryPoList.size());

        return systemDictionaryPoList;
    }

    @Override
    public List<HouseLivingPo> queryHouseLivingMetaData() throws MetaDataException {
        //查询户型小区元数据
        log.info(CLASS_LOG_PREFIX + "查询户型小区元数据...");
        List<HouseLivingPo> houseLivingPoList;
        try {
            houseLivingPoList = metaDataDao.queryHouseLivingMetaData();
        } catch (Exception e) {
            log.error(CLASS_LOG_PREFIX + "查询户型小区元数据失败!Exception:{}", e);
            throw new MetaDataException(CLASS_LOG_PREFIX + "查询户型小区元数据失败!Exception:{}" + e);
        }
        log.info(CLASS_LOG_PREFIX + "查询户型小区元数据完成.List<HouseLivingPo>:{}", null == houseLivingPoList ? 0 : houseLivingPoList.size());

        return houseLivingPoList;
    }

    @Override
    public List<AreaPo> queryAreaMetaData() throws MetaDataException {
        //查询区域元数据
        log.info(CLASS_LOG_PREFIX + "查询区域元数据...");
        List<AreaPo> areaPoList;
        try {
            areaPoList = metaDataDao.queryAreaMetaData();
        } catch (Exception e) {
            log.error(CLASS_LOG_PREFIX + "查询区域元数据失败!Exception:{}", e);
            throw new MetaDataException(CLASS_LOG_PREFIX + "查询区域元数据失败!Exception:{}" + e);
        }
        log.info(CLASS_LOG_PREFIX + "查询区域元数据完成.List<AreaPo>:{}", null == areaPoList ? 0 : areaPoList.size());

        return areaPoList;
    }

    @Override
    public List<CompanyPo> queryCompanyMetaData() throws MetaDataException {
        //查询公司元数据
        log.info(CLASS_LOG_PREFIX + "查询公司元数据...");
        List<CompanyPo> companyPoList;
        try {
            companyPoList = metaDataDao.queryCompanyMetaData();
        } catch (Exception e) {
            log.error(CLASS_LOG_PREFIX + "查询公司元数据失败!Exception:{}", e);
            throw new MetaDataException(CLASS_LOG_PREFIX + "查询公司元数据失败!Exception:{}" + e);
        }
        log.info(CLASS_LOG_PREFIX + "查询公司元数据完成.List<CompanyPo>:{}", null == companyPoList ? 0 : companyPoList.size());

        return companyPoList;
    }

    @Override
    public List<CompanyCategoryRelPo> queryCompanyCategoryRelMetaData() throws MetaDataException {
        //查询公司分类关联元数据
        log.info(CLASS_LOG_PREFIX + "查询公司分类关联元数据...");
        List<CompanyCategoryRelPo> companyCategoryRelPoList;
        try {
            companyCategoryRelPoList = metaDataDao.queryCompanyCategoryRelMetaData();
        } catch (Exception e) {
            log.error(CLASS_LOG_PREFIX + "查询公司分类关联元数据失败!Exception:{}", e);
            throw new MetaDataException(CLASS_LOG_PREFIX + "查询公司分类关联元数据失败!Exception:{}" + e);
        }
        log.info(CLASS_LOG_PREFIX + "查询公司分类关联元数据完成.List<CompanyCategoryRelPo>:{}", null == companyCategoryRelPoList ? 0 : companyCategoryRelPoList.size());

        return companyCategoryRelPoList;
    }

    @Override
    public List<DesignTemplatePo> queryDesignTemplatePoMetaData() throws MetaDataException {
        //查询设计方案样板房元数据
        log.info(CLASS_LOG_PREFIX + "查询设计方案样板房元数据...");
        List<DesignTemplatePo> designTemplatePoList;
        try {
            designTemplatePoList = metaDataDao.queryDesignTemplatePoMetaData();
        } catch (Exception e) {
            log.error(CLASS_LOG_PREFIX + "查询设计方案样板房元数据失败!Exception:{}", e);
            throw new MetaDataException(CLASS_LOG_PREFIX + "查询设计方案样板房元数据失败!Exception:{}" + e);
        }
        log.info(CLASS_LOG_PREFIX + "查询设计方案样板房元数据完成.List<DesignTemplatePo>:{}", null == designTemplatePoList ? 0 : designTemplatePoList.size());

        return designTemplatePoList;
    }

    @Override
    public List<ProductPlatformRelPo> queryAllPlatformProductMetaData() throws MetaDataException {

        //查询所有平台产品数据(平台数据过滤)
        log.info(CLASS_LOG_PREFIX + "查询所有平台产品数据(平台数据过滤)...");

        //所有数据
        List<ProductPlatformRelPo> productPlatformRelPoList = new ArrayList<>();

        //TOB数据
        List<ProductPlatformRelPo> productToBPlatformRelPoList;
        //TOC数据
        List<ProductPlatformRelPo> productToCPlatformRelPoList;
        //Sandu数据
        List<ProductPlatformRelPo> productSanduPlatformRelPoList;

        /************************** TOB ******************************/
        log.info(CLASS_LOG_PREFIX + "查询TOB平台产品数据(平台数据过滤1/3)...");
        try {
            productToBPlatformRelPoList = metaDataDao.queryToBPlatformProductMetaData();
        } catch (Exception e) {
            log.error(CLASS_LOG_PREFIX + "查询TOB平台产品数据(平台数据过滤)失败!Exception:{}", e);
            throw new MetaDataException(CLASS_LOG_PREFIX + "查询TOB平台产品数据(平台数据过滤)失败!Exception:{}" + e);
        }
        if (null != productToBPlatformRelPoList && 0 < productToBPlatformRelPoList.size()) {
            productPlatformRelPoList.addAll(productToBPlatformRelPoList);
        }

        /************************** TOC ******************************/
        log.info(CLASS_LOG_PREFIX + "查询TOC平台产品数据(平台数据过滤2/3)...");
        try {
            productToCPlatformRelPoList = metaDataDao.queryToCPlatformProductMetaData();
        } catch (Exception e) {
            log.error(CLASS_LOG_PREFIX + "查询TOC平台产品数据(平台数据过滤)失败!Exception:{}", e);
            throw new MetaDataException(CLASS_LOG_PREFIX + "查询TOC平台产品数据(平台数据过滤)失败!Exception:{}" + e);
        }
        if (null != productToCPlatformRelPoList && 0 < productToCPlatformRelPoList.size()) {
            productPlatformRelPoList.addAll(productToCPlatformRelPoList);
        }

        /************************** Sandu ******************************/
        log.info(CLASS_LOG_PREFIX + "查询Sandu平台产品数据(平台数据过滤3/3)...");
        try {
            productSanduPlatformRelPoList = metaDataDao.querySanduPlatformProductMetaData();
        } catch (Exception e) {
            log.error(CLASS_LOG_PREFIX + "查询Sandu平台产品数据(平台数据过滤)失败!Exception:{}", e);
            throw new MetaDataException(CLASS_LOG_PREFIX + "查询Sandu平台产品数据(平台数据过滤)失败!Exception:{}" + e);
        }
        if (null != productSanduPlatformRelPoList && 0 < productSanduPlatformRelPoList.size()) {
            productPlatformRelPoList.addAll(productSanduPlatformRelPoList);
        }

        log.info(CLASS_LOG_PREFIX + "查询所有平台产品数据(平台数据过滤)完成.List<ProductPlatformRelPo>:{}", productPlatformRelPoList.size());

        return productPlatformRelPoList;
    }

    @Override
    public List<ProductAttributePo> queryProductAttrMetaData() throws MetaDataException {
        //查询产品属性元信息
        log.info(CLASS_LOG_PREFIX + "查询产品属性元信息...");
        List<ProductAttributePo> productAttributePoList;
        try {
            productAttributePoList = metaDataDao.queryProductAttrMetaData();
        } catch (Exception e) {
            log.error(CLASS_LOG_PREFIX + "查询产品属性元信息失败!Exception:{}", e);
            throw new MetaDataException(CLASS_LOG_PREFIX + "查询产品属性元信息失败!Exception:{}" + e);
        }
        log.info(CLASS_LOG_PREFIX + "查询产品属性元信息完成.List<ProductAttributePo>:{}", null == productAttributePoList ? 0 : productAttributePoList.size());

        return productAttributePoList;
    }


    @Override
    public List<ProductUsagePo> queryProductUsageCountSatatistics() throws MetaDataException {
        //查询产品使用次数统计信息
        log.info(CLASS_LOG_PREFIX + "查询产品使用次数统计信息...");
        List<ProductUsagePo> productAttributePoList;
        try {
            productAttributePoList = metaDataDao.queryProductUsageCountSatatistics();
        } catch (Exception e) {
            log.error(CLASS_LOG_PREFIX + "查询产品使用次数统计信息失败!Exception:{}", e);
            throw new MetaDataException(CLASS_LOG_PREFIX + "查询产品使用次数统计信息失败!Exception:{}" + e);
        }
        log.info(CLASS_LOG_PREFIX + "查询产品使用次数统计信息完成.List<ProductUsagePo>:{}", null == productAttributePoList ? 0 : productAttributePoList.size());

        return productAttributePoList;
    }

    @Override
    public List<UnionBrandPo> queryUnionBrandMetaData() throws MetaDataException {
        //查询联盟品牌元数据
        log.info(CLASS_LOG_PREFIX + "查询联盟品牌元数据...");
        List<UnionBrandPo> unionBrandPoList;
        try {
            unionBrandPoList = metaDataDao.queryUnionBrandMetaData();
        } catch (Exception e) {
            log.error(CLASS_LOG_PREFIX + "查询联盟品牌元数据失败!Exception:{}", e);
            throw new MetaDataException(CLASS_LOG_PREFIX + "查询联盟品牌元数据!Exception:{}" + e);
        }
        log.info(CLASS_LOG_PREFIX + "查询联盟品牌元数据.List<UnionBrandPo>:{}", null == unionBrandPoList ? 0 : unionBrandPoList.size());

        return unionBrandPoList;
    }

    @Override
    public List<DesignPlanBrandPo> queryDesignPlanBrandMetaData() throws MetaDataException {
        //查询设计方案品牌元数据
        log.info(CLASS_LOG_PREFIX + "查询设计方案品牌元数据...");
        List<DesignPlanBrandPo> designPlanBrandList;
        try {
            designPlanBrandList = metaDataDao.queryDesignPlanBrandMetaData();
        } catch (Exception e) {
            log.error(CLASS_LOG_PREFIX + "查询设计方案品牌元数据失败!Exception:{}", e);
            throw new MetaDataException(CLASS_LOG_PREFIX + "查询设计方案品牌元数据!Exception:{}" + e);
        }
        log.info(CLASS_LOG_PREFIX + "查询设计方案品牌元数据.List<DesignPlanBrandPo>:{}", null == designPlanBrandList ? 0 : designPlanBrandList.size());

        return designPlanBrandList;
    }

    @Override
    public List<SpaceCommonPo> querySpaceCommonMetaData() throws MetaDataException {
        //查询空间元数据
        log.info(CLASS_LOG_PREFIX + "查询空间元数据...");
        List<SpaceCommonPo> spaceCommonList;
        try {
            spaceCommonList = metaDataDao.querySpaceCommonMetaData();
        } catch (Exception e) {
            log.error(CLASS_LOG_PREFIX + "查询空间元数据失败!Exception:{}", e);
            throw new MetaDataException(CLASS_LOG_PREFIX + "查询空间元数据!Exception:{}" + e);
        }
        log.info(CLASS_LOG_PREFIX + "查询空间元数据.List<SpaceCommonPo>:{}", null == spaceCommonList ? 0 : spaceCommonList.size());

        return spaceCommonList;
    }

    @Override
    public List <BaseCompanyMiniProgramConfig> queryBaseCompanyMiniProgramConfig() throws MetaDataException {
        List <BaseCompanyMiniProgramConfig> result = new ArrayList <>();
        try {
            result = metaDataDao.queryBaseCompanyMiniProgramConfigMetaData();
        } catch (Exception e) {
            log.error(CLASS_LOG_PREFIX + "查询公司小程序品牌元数据失败!Exception:{}", e);
            throw new MetaDataException(CLASS_LOG_PREFIX + "查询公司小程序品牌元数据!Exception:{}" + e);
        }
        return result;
    }

    @Override
    public List<SpuSaleInfoPo> querySpuInfoMetaData()throws MetaDataException {
        //查询商品详情元数据
        log.info(CLASS_LOG_PREFIX + "查询商品详情元数据...");
        List<SpuSaleInfoPo> spuInfoList;;
        try {
            spuInfoList = metaDataDao.querySpuInfoMetaData();
        } catch (Exception e) {
            log.error(CLASS_LOG_PREFIX + "查询商品详情元数据失败!Exception:{}", e);
            throw new MetaDataException(CLASS_LOG_PREFIX + "查询商品列表元数据!Exception:{}" + e);
        }
        log.info(CLASS_LOG_PREFIX + "查询商品详情元数据.List<SpuSaleInfoPo>:{}", null == spuInfoList ? 0 : spuInfoList.size());

        return spuInfoList;
    }

    @Override
    public List<GoodsSkuPo> querygoodsSkuMetaData() throws MetaDataException {
        //查询sku产品单元元数据
        log.info(CLASS_LOG_PREFIX + "查询sku产品单元元数据...");
        List<GoodsSkuPo> goodsSkuList;
        try {
            goodsSkuList = metaDataDao.queryGoodsSkuMetaData();
        } catch (Exception e) {
            log.error(CLASS_LOG_PREFIX + "查询sku产品单元元数据失败!Exception:{}", e);
            throw new MetaDataException(CLASS_LOG_PREFIX + "查询sku产品单元元数据!Exception:{}" + e);
        }
        log.info(CLASS_LOG_PREFIX + "查询sku产品单元元数据.List<SpuSaleInfoPo>:{}", null == goodsSkuList ? 0 : goodsSkuList.size());

        return goodsSkuList;
    }

    @Override
    public List<BaseProductDataPo> queryBaseProductData() throws MetaDataException{
        //查询sku产品单元元数据
        log.info(CLASS_LOG_PREFIX + "查询基础产品元数据...");
        List<BaseProductDataPo> baseProductDataList;
        try {
            baseProductDataList = metaDataDao.queryBaseProductData();
        } catch (Exception e) {
            log.error(CLASS_LOG_PREFIX + "查询基础产品元数据失败!Exception:{}", e);
            throw new MetaDataException(CLASS_LOG_PREFIX + "查询基础产品元数据!Exception:{}" + e);
        }
        log.info(CLASS_LOG_PREFIX + "查询基础产品元数据.List<BaseProductDataPo>:{}", null == baseProductDataList ? 0 : baseProductDataList.size());

        return baseProductDataList;
    }

    @Override
    public SpuSaleInfoPo querySpuInfoById(Integer id)throws Exception {
        //查询sku产品单元元数据
        log.info(CLASS_LOG_PREFIX + "查询单个商品详情");
        SpuSaleInfoPo spuInfo;
        try {
            spuInfo = metaDataDao.querySpuInfoById(id);
        } catch (Exception e) {
            log.error(CLASS_LOG_PREFIX + "查询单个商品详情数据失败!Exception:{}", e);
            throw new Exception(CLASS_LOG_PREFIX + "查询单个商品详情!Exception:{}" + e);
        }
        log.info(CLASS_LOG_PREFIX + "查询单个商品详情.spuInfo:{}", null== spuInfo ? null : spuInfo);

        return spuInfo;
    }


    @Override
    public List<BaseProductDataPo> queryBaseProductDataById(Integer spuId) throws MetaDataException{
        //查询sku产品单元元数据
        log.info(CLASS_LOG_PREFIX + "查询基础产品元数据...");
        List<BaseProductDataPo> baseProductDataList;
        try {
            baseProductDataList = metaDataDao.queryBaseProductDataById(spuId);
        } catch (Exception e) {
            log.error(CLASS_LOG_PREFIX + "查询基础产品元数据失败!Exception:{}", e);
            throw new MetaDataException(CLASS_LOG_PREFIX + "查询基础产品元数据!Exception:{}" + e);
        }
        log.info(CLASS_LOG_PREFIX + "查询基础产品元数据.List<BaseProductDataPo>:{}", null == baseProductDataList ? 0 : baseProductDataList.size());

        return baseProductDataList;
    }

    @Override
    public GoodsSkuPo querygoodsSkuMetaDataByspuId(int spuId, int productId)  throws MetaDataException{
        //查询sku产品单元元数据
        log.info(CLASS_LOG_PREFIX + "查询sku产品单元元数据开始."+"spuId:"+spuId+"-------------"+"productId:"+productId);
        GoodsSkuPo goodsSkuPo;
        try {
            goodsSkuPo = metaDataDao.queryGoodsSkuMetaDataById(spuId,productId);
        } catch (Exception e) {
            log.error(CLASS_LOG_PREFIX + "查询sku产品单元元数据失败!Exception:{}", e);
            throw new MetaDataException(CLASS_LOG_PREFIX + "查询sku产品单元元数据!Exception:{}" + e);
        }
        log.info(CLASS_LOG_PREFIX + "查询sku产品单元元数据.goodsSkuPo:{}", goodsSkuPo);

        return goodsSkuPo;
    }

    @Override
    public ProductPlatformRelPo queryAllPlatformProductById(Integer productId) throws MetaDataException {
        if (null == productId || 0 == productId) {
            return null;
        }
        ProductPlatformRelPo  productPlatformData;
        try {
            productPlatformData = metaDataDao.queryAllPlatformProductById(productId);
        } catch (Exception e) {
            log.error(CLASS_LOG_PREFIX + "获取产品平台元数据失败: MetaDataException:{}", e);
            throw new NullPointerException(CLASS_LOG_PREFIX + "获取产品平台元数据失败,productPlatformData is null.MetaDataException:" + e);
        }
        log.info(CLASS_LOG_PREFIX + "获取产品平台元数据完成:{}", productPlatformData);

        return productPlatformData;
    }

    @Override
    public List<ProductCategoryPo> queryProductCategoryByProductId(int productId) throws MetaDataException {
        //查询产品分类信息
        log.info(CLASS_LOG_PREFIX + "查询单个产品关联分类信息...");

        List<ProductCategoryRelPo> productCategoryRelPoList;
        try {

            productCategoryRelPoList = metaDataDao.queryProductCategoryRelByProductId(productId);

        } catch (Exception e) {
            log.error(CLASS_LOG_PREFIX + "查询单个产品关联分类信息失败!Exception:{}", e);
            throw new MetaDataException(CLASS_LOG_PREFIX + "查询单个产品关联分类信息失败!Exception:{}" + e);
        }


        if(null ==productCategoryRelPoList && productCategoryRelPoList.size()  == 0 ){
            return null;
        }

        List<ProductCategoryPo> productCategoryPos = new ArrayList<>();
        for(ProductCategoryRelPo productCategoryRelPo : productCategoryRelPoList){
            List<ProductCategoryPo> productCategoryPoList;
            try {

                productCategoryPoList = metaDataDao.queryProductCategoryById(productCategoryRelPo.getCategoryId());
            } catch (Exception e) {
                log.error(CLASS_LOG_PREFIX + "查询单个产品分类信息失败!Exception:{}", e);
                throw new MetaDataException(CLASS_LOG_PREFIX + "查询单个产品分类信息失败!Exception:{}" + e);
            }
            log.info(CLASS_LOG_PREFIX + "查询单个产品分类信息.List<ProductCategoryPo>:{}", null == productCategoryPoList ? 0 : productCategoryPoList.size());
            if(null != productCategoryPoList && productCategoryPoList.size() > 0 ){
                productCategoryPos.addAll(productCategoryPoList);
            }
        }

        return productCategoryPos;
    }



    @Override
    public List<DesignPlanPlatformRelPo> queryAllPlatformPlanMetaData() throws MetaDataException {

        //查询所有平台方案数据(平台数据过滤)
        log.info(CLASS_LOG_PREFIX + "查询所有平台方案数据(平台数据过滤)...");

        //所有数据
        List<DesignPlanPlatformRelPo> designPlanPlatformRelPoList = new ArrayList<>();

        //TOB数据
        List<DesignPlanPlatformRelPo> designPlanToBPlatformRelPoList;
        //TOC数据
        List<DesignPlanPlatformRelPo> designPlanPToClatformRelPoList;

        /************************** TOB ******************************/
        log.info(CLASS_LOG_PREFIX + "查询TOB平台方案数据(平台数据过滤1/3)...");
        try {
            designPlanToBPlatformRelPoList = metaDataDao.queryToBPlatformPlanMetaData();
        } catch (Exception e) {
            log.error(CLASS_LOG_PREFIX + "查询TOB平台方案数据(平台数据过滤)失败!Exception:{}", e);
            throw new MetaDataException(CLASS_LOG_PREFIX + "查询TOB平台方案数据(平台数据过滤)失败!Exception:{}" + e);
        }
        if (null != designPlanToBPlatformRelPoList && 0 < designPlanToBPlatformRelPoList.size()) {
            designPlanPlatformRelPoList.addAll(designPlanToBPlatformRelPoList);
        }

        /************************** TOC ******************************/
        log.info(CLASS_LOG_PREFIX + "查询TOC平台方案数据(平台数据过滤2/3)...");
        try {
            designPlanPToClatformRelPoList = metaDataDao.queryToCPlatformPlanMetaData();
        } catch (Exception e) {
            log.error(CLASS_LOG_PREFIX + "查询TOC平台方案数据(平台数据过滤)失败!Exception:{}", e);
            throw new MetaDataException(CLASS_LOG_PREFIX + "查询TOC平台方案数据(平台数据过滤)失败!Exception:{}" + e);
        }
        if (null != designPlanPToClatformRelPoList && 0 < designPlanPToClatformRelPoList.size()) {
            designPlanPlatformRelPoList.addAll(designPlanPToClatformRelPoList);
        }

        log.info(CLASS_LOG_PREFIX + "查询所有平台方案数据(平台数据过滤)完成.List<ProductPlatformRelPo>:{}", designPlanPlatformRelPoList.size());

        return designPlanPlatformRelPoList;
    }

    @Override
    public List<DesignPlanPlatformRelPo> queryPlatformPlanByIdsMetaData(List<Integer> recommendedPlanIdList, int type) throws MetaDataException {
        //查询所有平台方案数据(平台数据过滤)
        log.info(CLASS_LOG_PREFIX + "查询指定方案平台数据(平台数据过滤)...");

        //所有数据
        List<DesignPlanPlatformRelPo> designPlanPlatformRelPoList = new ArrayList<>();

        //TOB数据
        List<DesignPlanPlatformRelPo> designPlanToBPlatformRelPoList;
        //TOC数据
        List<DesignPlanPlatformRelPo> designPlanPToClatformRelPoList;

        /************************** TOB ******************************/
        log.info(CLASS_LOG_PREFIX + "查询TOB平台方案数据(平台数据过滤1/3)...");
        try {
            designPlanToBPlatformRelPoList = metaDataDao.queryToBPlatformByPlanIdsMetaData(recommendedPlanIdList, type);
        } catch (Exception e) {
            log.error(CLASS_LOG_PREFIX + "查询TOB平台方案数据(平台数据过滤)失败!Exception:{}", e);
            throw new MetaDataException(CLASS_LOG_PREFIX + "查询TOB平台方案数据(平台数据过滤)失败!Exception:{}" + e);
        }
        if (null != designPlanToBPlatformRelPoList && 0 < designPlanToBPlatformRelPoList.size()) {
            designPlanPlatformRelPoList.addAll(designPlanToBPlatformRelPoList);
        }

        /************************** TOC ******************************/
        log.info(CLASS_LOG_PREFIX + "查询TOC平台方案数据(平台数据过滤2/3)...");
        try {
            designPlanPToClatformRelPoList = metaDataDao.queryToCPlatformByPlanIdsMetaData(recommendedPlanIdList, type);
        } catch (Exception e) {
            log.error(CLASS_LOG_PREFIX + "查询TOC平台方案数据(平台数据过滤)失败!Exception:{}", e);
            throw new MetaDataException(CLASS_LOG_PREFIX + "查询TOC平台方案数据(平台数据过滤)失败!Exception:{}" + e);
        }
        if (null != designPlanPToClatformRelPoList && 0 < designPlanPToClatformRelPoList.size()) {
            designPlanPlatformRelPoList.addAll(designPlanPToClatformRelPoList);
        }

        log.info(CLASS_LOG_PREFIX + "查询指定平台方案数据(平台数据过滤)完成.List<ProductPlatformRelPo>:{}", designPlanPlatformRelPoList.size());

        return designPlanPlatformRelPoList;
    }

    @Override
    public List<SysUserPo> queryUserMetaData() throws MetaDataException {
        //查询空间元数据
        log.info(CLASS_LOG_PREFIX + "查询用户元数据...");
        List<SysUserPo> userPoList;
        try {
            userPoList = metaDataDao.queryUserMetaData();
        } catch (Exception e) {
            log.error(CLASS_LOG_PREFIX + "查询用户元数据失败！Exception:{}", e);
            throw new MetaDataException(CLASS_LOG_PREFIX + "查询用户元数据失败！Exception:{}" + e);
        }
        log.info(CLASS_LOG_PREFIX + "查询用户元数据.List<SysUserPo>:{}", null == userPoList ? 0 : userPoList.size());

        return userPoList;
    }

    @Override
    public List<RecommendedPlanLivingPo> queryPlanLivingMetaData() throws MetaDataException {
        //查询空间元数据
        log.info(CLASS_LOG_PREFIX + "查询方案小区元数据...");
        List<RecommendedPlanLivingPo> planLivingPoList;
        try {
            planLivingPoList = metaDataDao.queryPlanLivingMetaData();
        } catch (Exception e) {
            log.error(CLASS_LOG_PREFIX + "查询小区元数据失败！Exception:{}", e);
            throw new MetaDataException(CLASS_LOG_PREFIX + "查询小区元数据失败！Exception:{}" + e);
        }
        log.info(CLASS_LOG_PREFIX + "查询小区元数据.List<RecommendedPlanLivingPo>:{}", null == planLivingPoList ? 0 : planLivingPoList.size());

        return planLivingPoList;
    }

    @Override
    public List<ResPicPo> queryRecommendPlanCoverPicMetaData() throws MetaDataException {
        //查询图片资源元数据
        log.info(CLASS_LOG_PREFIX + "查询图片资源元数据...");
        List<ResPicPo> resPicList;
        try {
            resPicList = metaDataDao.queryRecommendPlanCoverPicMetaData();
        } catch (Exception e) {
            log.error(CLASS_LOG_PREFIX + "查询图片资源元数据失败!Exception:{}", e);
            throw new MetaDataException(CLASS_LOG_PREFIX + "查询图片资源元数据失败!Exception:{}" + e);
        }
        log.info(CLASS_LOG_PREFIX + "查询图片资源元数据完成.List<ResPicPo>:{}", null == resPicList ? 0 : resPicList.size());

        return resPicList;
    }

    @Override
    public List<ResPicPo> queryRecommendedPlanFinallyRenderPicMetaData() throws MetaDataException {
        //查询推荐方案720渲染图片资源元数据
        log.info(CLASS_LOG_PREFIX + "查询推荐方案720渲染图片资源元数据...");
        List<ResPicPo> resPicList;
        try {
            resPicList = metaDataDao.queryRecommendedPlanFinallyRenderPicMetaData();
        } catch (Exception e) {
            log.error(CLASS_LOG_PREFIX + "查询推荐方案720渲染图片资源元数据失败!Exception:{}", e);
            throw new MetaDataException(CLASS_LOG_PREFIX + "查询推荐方案720渲染图片资源元数据失败!Exception:{}" + e);
        }
        log.info(CLASS_LOG_PREFIX + "查询推荐方案720渲染图片资源元数据完成.List<ResPicPo>:{}", null == resPicList ? 0 : resPicList.size());

        return resPicList;
    }

    @Override
    public List<SysRoleFuncPo> queryRoleFuncMetaData() throws MetaDataException {
       //查询角色菜单元数据
       log.info(CLASS_LOG_PREFIX + "查询角色菜单元数据...");
       List<SysRoleFuncPo> roleFuncPoList;
       try {
           roleFuncPoList = metaDataDao.queryRoleFuncMetaData();
       } catch (Exception e) {
           log.error(CLASS_LOG_PREFIX + "查询角色菜单元数据失败！Exception:{}", e);
           throw new MetaDataException(CLASS_LOG_PREFIX + "查询角色菜单元数据失败！Exception:{}" + e);
       }
       log.info(CLASS_LOG_PREFIX + "查询角色菜单元数据.List<SysRoleFuncPo>:{}", null == roleFuncPoList ? 0 : roleFuncPoList.size());

       return roleFuncPoList;
    }

    @Override
    public List<DesignPlanRecommendedPo> queryDesignPlanRecommendedMetaData() throws MetaDataException {
        log.info(CLASS_LOG_PREFIX + "查询推荐方案元数据...");
        List<DesignPlanRecommendedPo> recommendedPoList;
        try {
            recommendedPoList = metaDataDao.queryDesignPlanRecommendedMetaData();
        } catch (Exception e) {
            log.error(CLASS_LOG_PREFIX + "查询推荐方案元数据失败！Exception:{}", e);
            throw new MetaDataException(CLASS_LOG_PREFIX + "查询推荐方案元数据失败！Exception:{}" + e);
        }
        log.info(CLASS_LOG_PREFIX + "查询推荐方案元数据.List<DesignPlanRecommendedPo>:{}", null == recommendedPoList ? 0 : recommendedPoList.size());

        return recommendedPoList;
    }


	@Override
	public String getPicPathFromResPicById(Integer picId) {
		// 参数验证 ->start
		if(picId == null) {
			return null;
		}
		// 参数验证 ->end
		
		return metaDataDao.selectPicPathFromResPicById(picId);
	}

	@Override
	public List<ResPicPo> getPicListByPicIdList(List<Integer> picIdList) {
		// 参数验证 ->start
		if(picIdList == null || picIdList.size() == 0) {
			return null;
		}
		// 参数验证 ->end
		
		return metaDataDao.selectFromResPicByIdList(picIdList);
	}


    @Override
    public List<CompanyShopPo> queryCompanyShopMetaData() throws MetaDataException {
        log.info(CLASS_LOG_PREFIX + "查询店铺元数据...");
        List<CompanyShopPo> companyShopPoList;
        try {
            companyShopPoList = metaDataDao.queryCompanyShopMetaData();
        } catch (Exception e) {
            log.error(CLASS_LOG_PREFIX + "查询店铺元数据失败！Exception:{}", e);
            throw new MetaDataException(CLASS_LOG_PREFIX + "查询店铺元数据失败！Exception:{}" + e);
        }
        log.info(CLASS_LOG_PREFIX + "查询店铺元数据.List<CompanyShopPo>:{}", null == companyShopPoList ? 0 : companyShopPoList.size());

        return companyShopPoList;
    }

    @Override
    public List<DecoratePricePo> queryPlanDecoratePriceMetaData() throws MetaDataException {
        log.info(CLASS_LOG_PREFIX + "查询装修报价元数据...");
        List<DecoratePricePo> decoratePricePoList;
        try {
            decoratePricePoList = metaDataDao.queryPlanDecoratePriceMetaData();
        } catch (Exception e) {
            log.error(CLASS_LOG_PREFIX + "查询装修报价元数据失败！Exception:{}", e);
            throw new MetaDataException(CLASS_LOG_PREFIX + "查询装修报价元数据失败！Exception:{}" + e);
        }
        log.info(CLASS_LOG_PREFIX + "查询装修报价元数据.List<DecoratePricePo>:{}", null == decoratePricePoList ? 0 : decoratePricePoList.size());

        return decoratePricePoList;
    }

    @Override
    public List<DecoratePricePo> queryPlanDecoratePriceByIdsMetaData(Integer planId, int type) throws MetaDataException {
        //log.info(CLASS_LOG_PREFIX + "查询装修报价元数据...");
        List<DecoratePricePo> decoratePricePoList;
        try {
            decoratePricePoList = metaDataDao.queryPlanDecoratePriceByIdsMetaData(planId, type);
        } catch (Exception e) {
            log.error(CLASS_LOG_PREFIX + "查询装修报价元数据失败！planId:{},Exception:{}", planId, e);
            throw new MetaDataException(CLASS_LOG_PREFIX + "查询装修报价元数据失败！planId:{}" + planId + ",Exception:{}" + e);
        }
        //log.info(CLASS_LOG_PREFIX + "查询装修报价元数据.List<DecoratePricePo>:{}", null == decoratePricePoList ? 0 : decoratePricePoList.size());

        return decoratePricePoList;
    }

    @Override
    public List<CompanyShopPlanPo> queryCompanyShopPlanMetaData() throws MetaDataException {
        log.info(CLASS_LOG_PREFIX + "查询店铺方案元数据...");
        List<CompanyShopPlanPo> shopPlanPoList;
        try {
            shopPlanPoList = metaDataDao.queryCompanyShopPlanMetaData();
        } catch (Exception e) {
            log.error(CLASS_LOG_PREFIX + "查询店铺方案元数据失败！Exception:{}", e);
            throw new MetaDataException(CLASS_LOG_PREFIX + "查询店铺方案元数据失败！Exception:{}" + e);
        }
        log.info(CLASS_LOG_PREFIX + "查询店铺方案元数据.List<CompanyShopPlanPo>:{}", null == shopPlanPoList ? 0 : shopPlanPoList.size());

        return shopPlanPoList;
    }

    @Override
    public List<CompanyShopPlanPo> queryCompanyShopPlanByPlanIdMetaData(Integer planId) throws MetaDataException {
        List<CompanyShopPlanPo> shopPlanIdList;
        try {
            shopPlanIdList = metaDataDao.queryCompanyShopPlanByPlanIdMetaData(planId);
        } catch (Exception e) {
            log.error(CLASS_LOG_PREFIX + "查询店铺方案元数据失败！planId：{}，Exception:{}", planId, e);
            throw new MetaDataException(CLASS_LOG_PREFIX + "查询店铺方案元数据失败！Exception:{}" + e);
        }

        return shopPlanIdList;
    }

    @Override
    public List<RecommendedPlanProductPo> queryRecommendedPlanProductMetaData() throws MetaDataException {
        log.info(CLASS_LOG_PREFIX + "查询推荐方案产品元数据...");
        List<RecommendedPlanProductPo> recommendPlanProductPoList;
        try {
            recommendPlanProductPoList = metaDataDao.queryRecommendedPlanProductMetaData();
        } catch (Exception e) {
            log.error(CLASS_LOG_PREFIX + "查询推荐方案产品元数据失败！Exception:{}", e);
            throw new MetaDataException(CLASS_LOG_PREFIX + "查询推荐方案产品元数据失败！Exception:{}" + e);
        }
        log.info(CLASS_LOG_PREFIX + "查询推荐方案产品元数据.List<RecommendedPlanProductPo>:{}", null == recommendPlanProductPoList ? 0 : recommendPlanProductPoList.size());

        return recommendPlanProductPoList;
    }

    @Override
    public List<Integer> queryRecommendedPlanProductByPlanIdMetaData(Integer planId) throws MetaDataException {

        List<Integer> productIdList;
        try {
            productIdList = metaDataDao.queryRecommendedPlanProductByPlanIdMetaData(planId);
        } catch (Exception e) {
            log.error(CLASS_LOG_PREFIX + "查询推荐方案产品元数据失败！planId:{},Exception:{}", planId, e);
            throw new MetaDataException(CLASS_LOG_PREFIX + "查询推荐方案产品元数据失败！Exception:{}" + e);
        }

        return productIdList;
    }

    @Override
    public List<CompanyShopPlanPo> queryCompanyShopPlanByFullHouseIds(List<Integer> fullHousePlanIdList) throws MetaDataException {
        List<CompanyShopPlanPo> companyShopPlanPoList;
        try {
            companyShopPlanPoList = metaDataDao.selectCompanyShopPlanByFullHouseIds(fullHousePlanIdList);
        } catch (Exception e){
            log.error(CLASS_LOG_PREFIX + "查询店铺全屋方案元数据失败！planId:{},Exception:{}", fullHousePlanIdList, e);
            throw new MetaDataException(CLASS_LOG_PREFIX + "查询店铺全屋方案元数据失败！Exception:{}" + e);
        }
        return companyShopPlanPoList;
    }

    @Override
    public List<CompanyShopPlanPo> queryPlanInfoByShopId(Integer shopId) throws MetaDataException {
        List<CompanyShopPlanPo> planInfoList;
        try {
            planInfoList = metaDataDao.queryPlanInfoByShopId(shopId);
        } catch (Exception e){
            log.error(CLASS_LOG_PREFIX + "查询店铺方案数据失败！shopId:{},Exception:{}", shopId, e);
            throw new MetaDataException(CLASS_LOG_PREFIX + "查询店铺方案数据失败！Exception:{}" + e);
        }
        return planInfoList;
    }

    @Override
    public List<ProductGroupDetailPo> queryProductGroupDetail() throws MetaDataException {
        List<ProductGroupDetailPo> productGroupDetailPoList;
        try {
            productGroupDetailPoList = metaDataDao.queryProductGroupDetail();
        } catch (Exception e) {
            log.error(CLASS_LOG_PREFIX + "查询组合产品详细信息元数据失败！Exception:{}", e);
            throw new MetaDataException(CLASS_LOG_PREFIX + "查询组合产品详细信息元数据失败！Exception:{}", e);
        }

        if (null == productGroupDetailPoList || 0 >= productGroupDetailPoList.size()) {
            log.error(CLASS_LOG_PREFIX+"查询组合产品详细信息元数据为空。。。");
            return null;
        }
        log.info(CLASS_LOG_PREFIX+"查询组合产品详细信息元数据完成，总条数：{}",productGroupDetailPoList.size());

        for (ProductGroupDetailPo detailPo : productGroupDetailPoList) {

            // 普通产品 和 拆分材质产品 分开
            Integer productID = detailPo.getProductId();

            /*处理拆分材质产品返回默认材质*/
            Integer isSplit=0;
            List<SplitTextureDTO> splitTextureDTOList = null;

            String splitTexturesInfo = detailPo.getSplitTexturesChooseInfo();
            if (StringUtils.isEmpty(splitTexturesInfo)) {
                splitTexturesInfo = detailPo.getSplitTexturesInfo();
            }

            if ( StringUtils.isNotBlank(splitTexturesInfo)) {
                Map<String,Object> map=productIndexService.dealWithSplitTextureInfo(productID, splitTexturesInfo,"choose");
                isSplit=(Integer) map.get("isSplit");
                splitTextureDTOList=(List<SplitTextureDTO>) map.get("splitTexturesChoose");
            } else {
                /*普通产品*/
                String materialIds = detailPo.getMaterialPicIds();
                Integer materialId=0;
                if(StringUtils.isNotBlank(materialIds)){
                    List<String> materialIdStrList= Utils.getListFromStr(materialIds);
                    if(materialIdStrList!=null&&materialIdStrList.size()>0){
                        materialId=Integer.valueOf(materialIdStrList.get(0));
                    }
                }
                // if ( materialId!=null && materialId>0 ) {
                //     ProductTexturePo groupProductTexture = productTextureMetaDataStorage.getGroupProductTexture(materialId);
                //     if ( groupProductTexture!=null ) {
                //         splitTextureDTOList = new ArrayList<SplitTextureDTO>();
                //         List<SplitTextureDTO.ResTextureDTO> resTextureDTOList=new ArrayList<SplitTextureDTO.ResTextureDTO>();
                //         SplitTextureDTO splitTextureDTO=new SplitTextureDTO("1", "", null);
                        // SplitTextureDTO.ResTextureDTO resTextureDTO=indexGroupProductService.fromResTexture(groupProductTexture);
                        // detailPo.setMaterialPicPaths(new String[]{resTextureDTO.getPath()});
                        // resTextureDTO.setKey(splitTextureDTO.getKey());
                        // resTextureDTO.setProductId(productID);
                        // resTextureDTOList.add(resTextureDTO);
                //         splitTextureDTO.setList(resTextureDTOList);
                //         splitTextureDTOList.add(splitTextureDTO);
                //     }
                // }
            }
            detailPo.setIsSplit(isSplit);
            detailPo.setSplitTexturesChoose(splitTextureDTOList);
            /*处理拆分材质产品返回默认材质->end*/

            /* 处理产品属性相关信息 -> start */
            // Map<String, String> tempMap = new HashMap<>();
            // String propValue = detailPo.getProductPropValue() == null ? "" : detailPo.getProductPropValue().toString().trim();
            // tempMap.put(detailPo.getProductAttributeKey(),propValue);


            /** 在组合产品查询列表 中 增加产品属性 **/
            // Map<String,String> map = indexGroupProductService.getPropertyMap(productID);
            // detailPo.setPropertyMap(map);
            // 进行 规则 service 的调用
            // Map<String,String> rulesMap = indexGroupProductService.getRulesSecondaryList(productID, detailPo.getProductCode(),
            //         detailPo.getProductTypeMark(), detailPo.getProductSmallTypeMark(), tempMap);
            // detailPo.setRulesMap(rulesMap);
            /* 处理产品属性相关信息 -> end */


        }

        return productGroupDetailPoList;
    }
}
