package com.sandu.search.storage;

import com.sandu.search.storage.company.BrandMetaDataStorage;
import com.sandu.search.storage.company.CompanyCategoryRelMetaDataStorage;
import com.sandu.search.storage.company.CompanyMetaDataStorage;
import com.sandu.search.storage.company.UnionBrandMetaDataStorage;
import com.sandu.search.storage.design.*;
import com.sandu.search.storage.goods.GoodsSkuMetaDataStorage;
import com.sandu.search.storage.goods.SpuInfoMetaDataStorage;
import com.sandu.search.storage.groupproduct.GroupProductDetailsMetaDataStorage;
import com.sandu.search.storage.groupproduct.GroupProductRelMetaDataStorage;
import com.sandu.search.storage.product.*;
import com.sandu.search.storage.resource.RenderPicMetaDataStorage;
import com.sandu.search.storage.resource.ResPicMetaDataStorage;
import com.sandu.search.storage.space.SpaceCommonMetaDataStorage;
import com.sandu.search.storage.system.SysRoleFuncMetaDataStorage;
import com.sandu.search.storage.system.SysUserMetaDataStorage;
import com.sandu.search.storage.system.SystemDictionaryMetaDataStorage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 数据存储组件
 *
 * @date 20171229
 * @auth pengxuangang
 */
@Slf4j
@Component
public class StorageComponent {

    //缓存模式
    public final static int CACHE_MODE = 1;
    //内存模式
    public final static int MEMORY_MODE = 2;

    //启动应用是否已初始化数据
    @Value("${init.metadatafinish}")
    public boolean START_APPLICATION_DATA_FINISH;
    private final static String CLASS_LOG_PREFIX = "数据存储组件:";

    private final BrandMetaDataStorage brandMetaDataStorage;
    private final ResPicMetaDataStorage resPicMetaDataStorage;
    private final CompanyMetaDataStorage companyMetaDataStorage;
    private final RenderPicMetaDataStorage renderPicMetaDataStorage;
    private final UnionBrandMetaDataStorage unionBrandMetaDataStorage;
    private final SpaceCommonMetaDataStorage spaceCommonMetaDataStorage;
    private final ProductStyleMetaDataStorage productStyleMetaDataStorage;
    private final DesignTemplateMetaDataStorage designTemplateMetaDataStorage;
    private final ProductTextureMetaDataStorage productTextureMetaDataStorage;
    private final DesignPlanBrandMetaDataStorage designPlanBrandMetaDataStorage;
    private final ProductCategoryMetaDataStorage productCategoryMetaDataStorage;
    private final ProductPlatformMetaDataStorage productPlatformMetaDataStorage;
    private final ProductAttributeMetaDataStorage productAttributeMetaDataStorage;
    private final SystemDictionaryMetaDataStorage systemDictionaryMetaDataStorage;
    private final DesignPlanProductMetaDataStorage designPlanProductMetaDataStorage;
    private final ProductUsageCountMetaDataStorage productUsageCountMetaDataStorage;
    private final CompanyCategoryRelMetaDataStorage companyCategoryRelMetaDataStorage;
    private final ProductCategoryRelMetaDataStorage productCategoryRelMetaDataStorage;
    private final SpuInfoMetaDataStorage spuInfoMetaDataStorage;
    private final GoodsSkuMetaDataStorage goodsSkuMetaDataStorage;
    private final BaseProductDataStorage baseProductDataStorage;
    private final DesignPlanPlatformMetaDataStorage designPlanPlatformMetaDataStorage;
    private final SysUserMetaDataStorage sysUserMetaDataStorage;
    private final RecommendedPlanLivingMetaDataStorage recommendedPlanLivingMetaDataStorage;
    private final SysRoleFuncMetaDataStorage sysRoleFuncMetaDataStorage;
    private final DesignPlanRecommendationMetaDataStorage designPlanRecommendationMetaDataStorage;
    private final DesignPlanDecoratePriceMetaDataStorage designPlanDecoratePriceMetaDataStorage;
    private final CompanyShopPlanMetaDataStorage companyShopPlanMetaDataStorage;
    private final RecommendedPlanProductMetaDataStorage recommendedPlanProductMetaDataStorage;
    private final GroupProductRelMetaDataStorage groupProductRelMetaDataStorage;
    private final GroupProductDetailsMetaDataStorage groupProductDetailsMetaDataStorage;
    private final DesignRulesMetaDataStorage designRulesMetaDataStorage;

    @Autowired
    public StorageComponent(DesignPlanDecoratePriceMetaDataStorage designPlanDecoratePriceMetaDataStorage, DesignPlanRecommendationMetaDataStorage designPlanRecommendationMetaDataStorage, BrandMetaDataStorage brandMetaDataStorage, ResPicMetaDataStorage resPicMetaDataStorage, CompanyMetaDataStorage companyMetaDataStorage, RenderPicMetaDataStorage renderPicMetaDataStorage, UnionBrandMetaDataStorage unionBrandMetaDataStorage, SpaceCommonMetaDataStorage spaceCommonMetaDataStorage, ProductStyleMetaDataStorage productStyleMetaDataStorage, DesignTemplateMetaDataStorage designTemplateMetaDataStorage, ProductTextureMetaDataStorage productTextureMetaDataStorage, DesignPlanBrandMetaDataStorage designPlanBrandMetaDataStorage, ProductCategoryMetaDataStorage productCategoryMetaDataStorage, ProductPlatformMetaDataStorage productPlatformMetaDataStorage, ProductAttributeMetaDataStorage productAttributeMetaDataStorage, SystemDictionaryMetaDataStorage systemDictionaryMetaDataStorage, DesignPlanProductMetaDataStorage designPlanProductMetaDataStorage, ProductUsageCountMetaDataStorage productUsageCountMetaDataStorage, CompanyCategoryRelMetaDataStorage companyCategoryRelMetaDataStorage, ProductCategoryRelMetaDataStorage productCategoryRelMetaDataStorage, DesignPlanPlatformMetaDataStorage designPlanPlatformMetaDataStorage, SysUserMetaDataStorage sysUserMetaDataStorage, RecommendedPlanLivingMetaDataStorage recommendedPlanLivingMetaDataStorage, SysRoleFuncMetaDataStorage sysRoleFuncMetaDataStorage, SpuInfoMetaDataStorage spuInfoMetaDataStorage, GoodsSkuMetaDataStorage goodsSkuMetaDataStorage,
                            BaseProductDataStorage baseProductDataStorage,
                            CompanyShopPlanMetaDataStorage companyShopPlanMetaDataStorage,
                            RecommendedPlanProductMetaDataStorage recommendedPlanProductMetaDataStorage,
                            GroupProductRelMetaDataStorage groupProductRelMetaDataStorage,
                            GroupProductDetailsMetaDataStorage groupProductDetailsMetaDataStorage,
                            DesignRulesMetaDataStorage designRulesMetaDataStorage) {
        this.brandMetaDataStorage = brandMetaDataStorage;
        this.resPicMetaDataStorage = resPicMetaDataStorage;
        this.companyMetaDataStorage = companyMetaDataStorage;
        this.renderPicMetaDataStorage = renderPicMetaDataStorage;
        this.unionBrandMetaDataStorage = unionBrandMetaDataStorage;
        this.spaceCommonMetaDataStorage = spaceCommonMetaDataStorage;
        this.productStyleMetaDataStorage = productStyleMetaDataStorage;
        this.designTemplateMetaDataStorage = designTemplateMetaDataStorage;
        this.productTextureMetaDataStorage = productTextureMetaDataStorage;
        this.designPlanBrandMetaDataStorage = designPlanBrandMetaDataStorage;
        this.productCategoryMetaDataStorage = productCategoryMetaDataStorage;
        this.productPlatformMetaDataStorage = productPlatformMetaDataStorage;
        this.productAttributeMetaDataStorage = productAttributeMetaDataStorage;
        this.systemDictionaryMetaDataStorage = systemDictionaryMetaDataStorage;
        this.designPlanProductMetaDataStorage = designPlanProductMetaDataStorage;
        this.productUsageCountMetaDataStorage = productUsageCountMetaDataStorage;
        this.companyCategoryRelMetaDataStorage = companyCategoryRelMetaDataStorage;
        this.productCategoryRelMetaDataStorage = productCategoryRelMetaDataStorage;
        this.spuInfoMetaDataStorage = spuInfoMetaDataStorage;
        this.goodsSkuMetaDataStorage = goodsSkuMetaDataStorage;
        this.baseProductDataStorage = baseProductDataStorage;
        this.designPlanPlatformMetaDataStorage = designPlanPlatformMetaDataStorage;
        this.sysUserMetaDataStorage = sysUserMetaDataStorage;
        this.recommendedPlanLivingMetaDataStorage = recommendedPlanLivingMetaDataStorage;
        this.sysRoleFuncMetaDataStorage = sysRoleFuncMetaDataStorage;
        this.designPlanRecommendationMetaDataStorage = designPlanRecommendationMetaDataStorage;
        this.designPlanDecoratePriceMetaDataStorage = designPlanDecoratePriceMetaDataStorage;
        this.companyShopPlanMetaDataStorage = companyShopPlanMetaDataStorage;
        this.recommendedPlanProductMetaDataStorage = recommendedPlanProductMetaDataStorage;
        this.groupProductRelMetaDataStorage = groupProductRelMetaDataStorage;
        this.groupProductDetailsMetaDataStorage = groupProductDetailsMetaDataStorage;
        this.designRulesMetaDataStorage = designRulesMetaDataStorage;
    }

    /**
     * 启动应用时加载元数据
     *
     * @date 2018/5/31
     * @auth pengxuangang
     * @mail xuangangpeng@gmail.com
     */
    public void startApplicationLoadMetaData() {
        if (!START_APPLICATION_DATA_FINISH) {
            reloadAllStorageInCache(ChangeStorageModeModules.all);
        }
    }

    //重新加载所有缓存数据(用以数据同步时清空数据)
    public void reloadAllStorageInCache(ChangeStorageModeModules module) {
        long startTime = System.currentTimeMillis();
        log.info(CLASS_LOG_PREFIX + "开始重新加载所有内存中缓存的数据.....");

        /************************** 产品 *********************************/
        if(ChangeStorageModeModules.all.equals(module)
                || ChangeStorageModeModules.product.equals(module)) {
            //品牌元数据
            log.info(CLASS_LOG_PREFIX + "开始更新品牌元数据...");
            brandMetaDataStorage.updateData();
            log.info(CLASS_LOG_PREFIX + "品牌元数据更新完成.");
        }

        if (ChangeStorageModeModules.all.equals(module)
                || ChangeStorageModeModules.product.equals(module)
                || ChangeStorageModeModules.goods.equals(module)) {
            //产品分类元数据
            log.info(CLASS_LOG_PREFIX + "开始更新产品分类元数据...");
            productCategoryMetaDataStorage.updateData();
            log.info(CLASS_LOG_PREFIX + "产品分类元数据更新完成.");
        }

        if (ChangeStorageModeModules.all.equals(module)
                || ChangeStorageModeModules.product.equals(module)
                || ChangeStorageModeModules.goods.equals(module)) {
            //产品分类关联数据
            log.info(CLASS_LOG_PREFIX + "开始更新产品分类关联元数据...");
            productCategoryRelMetaDataStorage.updateData();
            log.info(CLASS_LOG_PREFIX + "产品分类关联元数据更新完成.");
        }

        //产品组合关联数据
        /*log.info(CLASS_LOG_PREFIX + "开始更新产品组合关联数据...");
        productGroupRelMetaDataStorage.updateData(isEnforceLoad);
        log.info(CLASS_LOG_PREFIX + "产品组合关联数据更新完成.");*/

        if (ChangeStorageModeModules.all.equals(module)
                || ChangeStorageModeModules.product.equals(module)) {
            //产品风格数据
            log.info(CLASS_LOG_PREFIX + "开始更新产品风格数据...");
            productStyleMetaDataStorage.updateData();
            log.info(CLASS_LOG_PREFIX + "产品风格数据更新完成.");
        }

        if (ChangeStorageModeModules.all.equals(module)
                || ChangeStorageModeModules.product.equals(module)) {
            //产品材质数据
            log.info(CLASS_LOG_PREFIX + "开始更新产品材质数据...");
            productTextureMetaDataStorage.updateData();
            log.info(CLASS_LOG_PREFIX + "产品材质数据更新完成.");
        }
        
        /*
        //图片资源数据
        log.info(CLASS_LOG_PREFIX + "开始更新图片资源数据...");
        resPicMetaDataStorage.updateData();
        log.info(CLASS_LOG_PREFIX + "图片资源数据更新完成.");
        */

        /*
        //户型小区数据
        log.info(CLASS_LOG_PREFIX + "开始更新户型小区数据...");
        houseLivingMetaDataStorage.updateData(isEnforceLoad);
        log.info(CLASS_LOG_PREFIX + "户型小区数据更新完成.");
        */

        if (ChangeStorageModeModules.all.equals(module)
                || ChangeStorageModeModules.product.equals(module)) {
            //系统字典数据
            log.info(CLASS_LOG_PREFIX + "开始更新系统字典数据...");
            systemDictionaryMetaDataStorage.updateData();
            log.info(CLASS_LOG_PREFIX + "系统字典数据更新完成.");
        }

        /*
        //区域数据
        log.info(CLASS_LOG_PREFIX + "开始更新区域数据...");
        systemAreaMetaDataStorage.updateData(isEnforceLoad);
        log.info(CLASS_LOG_PREFIX + "区域数据更新完成.");
        */

        if (ChangeStorageModeModules.all.equals(module)
                || ChangeStorageModeModules.product.equals(module)) {
            //公司分类关联数据
            log.info(CLASS_LOG_PREFIX + "开始更新公司分类关联数据...");
            companyCategoryRelMetaDataStorage.updateData();
            log.info(CLASS_LOG_PREFIX + "公司分类关联数据更新完成.");
        }

        if (ChangeStorageModeModules.all.equals(module)
                || ChangeStorageModeModules.product.equals(module)) {
            //公司数据
            log.info(CLASS_LOG_PREFIX + "开始更新公司数据...");
            companyMetaDataStorage.updateData();
            log.info(CLASS_LOG_PREFIX + "公司数据更新完成.");
        }
        
        /*
        //设计方案产品数据
        log.info(CLASS_LOG_PREFIX + "开始同步设计方案产品数据元数据.....");
        designPlanProductMetaDataStorage.updateData();
        log.info(CLASS_LOG_PREFIX + "同步设计方案产品数据元数据完成.");
        */

        if (ChangeStorageModeModules.all.equals(module)
                || ChangeStorageModeModules.product.equals(module)) {
            //联盟品牌数据
            log.info(CLASS_LOG_PREFIX + "开始同步联盟品牌数据元数据.....");
            unionBrandMetaDataStorage.updateData();
            log.info(CLASS_LOG_PREFIX + "同步设计联盟品牌数据元数据完成.");
        }

        if (ChangeStorageModeModules.all.equals(module)
                || ChangeStorageModeModules.product.equals(module)
                || ChangeStorageModeModules.goods.equals(module)) {
            //产品平台数据
            log.info(CLASS_LOG_PREFIX + "开始同步产品平台数据.");
            productPlatformMetaDataStorage.updateData();
            log.info(CLASS_LOG_PREFIX + "同步产品平台数据完成.");
        }
        
        /*
        //产品属性数据
        log.info(CLASS_LOG_PREFIX + "开始同步产品属性数据.");
        productAttributeMetaDataStorage.updateData();
        log.info(CLASS_LOG_PREFIX + "同步产品属性数据完成.");
        */

        if (ChangeStorageModeModules.all.equals(module)
                || ChangeStorageModeModules.product.equals(module)) {
            //产品使用次数
            log.info(CLASS_LOG_PREFIX + "开始同步产品使用次数数据.");
            productUsageCountMetaDataStorage.updateData();
            log.info(CLASS_LOG_PREFIX + "同步产品产品使用次数完成.");
        }

        if (ChangeStorageModeModules.all.equals(module)
                || ChangeStorageModeModules.goods.equals(module)) {
            //spu详情数据
            log.info(CLASS_LOG_PREFIX + "开始更新商品详情数据...");
            spuInfoMetaDataStorage.updateData();
            log.info(CLASS_LOG_PREFIX + "商品详情数据更新完成.");
        }

        if (ChangeStorageModeModules.all.equals(module)
                || ChangeStorageModeModules.goods.equals(module)) {
            //sku产品单元数据
            log.info(CLASS_LOG_PREFIX + "开始更新sku产品单元数据...");
            goodsSkuMetaDataStorage.updateData();
            log.info(CLASS_LOG_PREFIX + "sku产品单元数据更新完成.");
        }

        if(ChangeStorageModeModules.all.equals(module)
                || ChangeStorageModeModules.goods.equals(module)) {
            //产品基础数据
            log.info(CLASS_LOG_PREFIX + "开始更新产品基础数据...");
            baseProductDataStorage.updateData();
            log.info(CLASS_LOG_PREFIX + "更新产品基础数据完成.");
        }

        /************************** 推荐方案 **************************/
        if (ChangeStorageModeModules.all.equals(module)
                || ChangeStorageModeModules.recommendedPlan.equals(module)) {
            reloadAllRecommendationPlanStorageInCache();
        }

        /****************************** 组合产品 *******************************/
        if (ChangeStorageModeModules.all.equals(module)
                || ChangeStorageModeModules.groupProduct.equals(module)) {
            reloadAllGroupProductStorageInCache();
        }

        log.info(CLASS_LOG_PREFIX + "重新加载所有内存中缓存的数据完成，耗时:{}ms.", (System.currentTimeMillis() - startTime));

        Runtime.getRuntime().gc();
        //标识初始化完成
        START_APPLICATION_DATA_FINISH = true;
    }

    /**
     * 选择更新哪些模块的元数据
     *
     * @author huangsongbo
     */
    public enum ChangeStorageModeModules {
        all, product, goods, recommendedPlan, groupProduct
    }

    //切换存储模式
    public void changeStorageMode(int storageMode, ChangeStorageModeModules module) {
        if (CACHE_MODE == storageMode || MEMORY_MODE == storageMode) {
            long startTime = System.currentTimeMillis();
            if(ChangeStorageModeModules.all.equals(module)
                    || ChangeStorageModeModules.product.equals(module)) {
                //品牌元数据
                brandMetaDataStorage.changeStorageMode(storageMode);
            }

            if (ChangeStorageModeModules.all.equals(module)
                    || ChangeStorageModeModules.product.equals(module)
                    || ChangeStorageModeModules.goods.equals(module)) {
                //产品分类元数据
                productCategoryMetaDataStorage.changeStorageMode(storageMode);
            }

            if (ChangeStorageModeModules.all.equals(module)
                    || ChangeStorageModeModules.product.equals(module)
                    || ChangeStorageModeModules.goods.equals(module)) {
                //产品分类关联数据
                productCategoryRelMetaDataStorage.changeStorageMode(storageMode);
            }

            if (ChangeStorageModeModules.all.equals(module)
                    || ChangeStorageModeModules.product.equals(module)) {
                //产品风格数据
                productStyleMetaDataStorage.changeStorageMode(storageMode);
            }

            if (ChangeStorageModeModules.all.equals(module)
                    || ChangeStorageModeModules.product.equals(module)) {
                //产品材质数据
                productTextureMetaDataStorage.changeStorageMode(storageMode);
            }

            // update by huangsongbo 2018.9.10 图片改为从db取(查询785s 数据量900w 耗时太长,占用内存太多)
         /*   if(ChangeStorageModeModules.all.equals(module)
            		|| ChangeStorageModeModules.product.equals(module)
            		|| ChangeStorageModeModules.goods.equals(module)) {
            	//图片资源数据
                resPicMetaDataStorage.changeStorageMode(storageMode);
            }*/
         
            if(ChangeStorageModeModules.all.equals(module)
                    || ChangeStorageModeModules.product.equals(module)) {
                //系统字典数据
                systemDictionaryMetaDataStorage.changeStorageMode(storageMode);
            }

            if (ChangeStorageModeModules.all.equals(module)
                    || ChangeStorageModeModules.product.equals(module)) {
                //公司分类关联数据
                companyCategoryRelMetaDataStorage.changeStorageMode(storageMode);
            }

            if (ChangeStorageModeModules.all.equals(module)
                    || ChangeStorageModeModules.product.equals(module)) {
                //公司数据
                companyMetaDataStorage.changeStorageMode(storageMode);
            }
            
            /*if(ChangeStorageModeModules.all.equals(module) 
            		|| ChangeStorageModeModules.product.equals(module)) {
            	//设计方案产品数据
                designPlanProductMetaDataStorage.changeStorageMode(storageMode);
            }*/

            if(ChangeStorageModeModules.all.equals(module)
                    || ChangeStorageModeModules.product.equals(module)) {
                //联盟品牌数据
                unionBrandMetaDataStorage.changeStorageMode(storageMode);
            }

            if (ChangeStorageModeModules.all.equals(module)
                    || ChangeStorageModeModules.product.equals(module)
                    || ChangeStorageModeModules.goods.equals(module)) {
                //产品平台数据
                productPlatformMetaDataStorage.changeStorageMode(storageMode);
            }
            
            /*if(ChangeStorageModeModules.all.equals(module) 
            		|| ChangeStorageModeModules.product.equals(module)) {
            	//产品属性数据
                productAttributeMetaDataStorage.changeStorageMode(storageMode);
            }*/

            if(ChangeStorageModeModules.all.equals(module)
                    || ChangeStorageModeModules.product.equals(module)) {
                //产品使用次数
                productUsageCountMetaDataStorage.changeStorageMode(storageMode);
            }

            if (ChangeStorageModeModules.all.equals(module)
                    || ChangeStorageModeModules.goods.equals(module)) {
                //商品售卖信息数据
                spuInfoMetaDataStorage.changeStorageMode(storageMode);
            }

            if (ChangeStorageModeModules.all.equals(module)
                    || ChangeStorageModeModules.goods.equals(module)) {
                //sku产品单元数据
                goodsSkuMetaDataStorage.changeStorageMode(storageMode);
            }

            if(ChangeStorageModeModules.all.equals(module)
            		|| ChangeStorageModeModules.goods.equals(module)) {
            	//产品基础数据
                //baseProductDataStorage.changeStorageMode(storageMode);
            }

            if (ChangeStorageModeModules.all.equals(module)
                    || ChangeStorageModeModules.recommendedPlan.equals(module)) {
                // 推荐方案
                this.changeRecommendationPlanStorageMode(storageMode);
            }


            /************************** 组合产品 ************************/
            if (ChangeStorageModeModules.all.equals(module)
                    || ChangeStorageModeModules.groupProduct.equals(module)) {
                // 组合产品相关
                this.changeGroupProductStorageMode(storageMode);
            }

            log.info(CLASS_LOG_PREFIX + "切换存储模式完成!耗时:{}ms.", (System.currentTimeMillis() - startTime));

            Runtime.getRuntime().gc();
        }
    }

    // 切换组合产品存储模式
    private void changeGroupProductStorageMode(int storageMode) {
        if (CACHE_MODE == storageMode || MEMORY_MODE == storageMode) {
            long startTime = System.currentTimeMillis();
            // 组合产品关联数据
            groupProductRelMetaDataStorage.changeStorageMode(storageMode);
            // 组合产品详细信息数据
            groupProductDetailsMetaDataStorage.changeStorageMode(storageMode);
            // 组合产品设计规则信息
            designRulesMetaDataStorage.changeStorageMode(storageMode);

            log.info(CLASS_LOG_PREFIX + "组合产品切换存储模式完成！耗时：{}", System.currentTimeMillis() - startTime);

            Runtime.getRuntime().gc();
        }
    }

    // 重新加载所有的组合产品缓存数据（用以数据同步时清空数据）
    public void reloadAllGroupProductStorageInCache() {

        long startTime = System.currentTimeMillis();
        log.info(CLASS_LOG_PREFIX + "开始重新加载所有的产品组合内存中的缓存的数据.....");

        log.info(CLASS_LOG_PREFIX + "同步组合产品关联数据开始..");
        groupProductRelMetaDataStorage.updateData();
        log.info(CLASS_LOG_PREFIX + "同步组合产品关联数据完成..");

        log.info(CLASS_LOG_PREFIX + "同步组合产品详细数据开始..");
        groupProductDetailsMetaDataStorage.updateData();
        log.info(CLASS_LOG_PREFIX + "同步组合产品详细数据完成..");

        log.info(CLASS_LOG_PREFIX + "重新加载所有的产品组合内存中的缓存的数据完成，耗时：{}", System.currentTimeMillis() - startTime);

        Runtime.getRuntime().gc();
    }


    //重新加载所有推荐方案缓存数据(用以数据同步时清空数据)
    public void reloadAllRecommendationPlanStorageInCache() {

        long startTime = System.currentTimeMillis();
        log.info(CLASS_LOG_PREFIX + "开始重新加载所有推荐方案内存中缓存的数据.....");

        //渲染图片数据
        //log.info(CLASS_LOG_PREFIX + "开始同步渲染图片数据数据.");
        //renderPicMetaDataStorage.updateData();
        //log.info(CLASS_LOG_PREFIX + "同步渲染图片数据完成.");

        //设计方案品牌数据
        //log.info(CLASS_LOG_PREFIX + "开始同步设计方案品牌数据.");
        //designPlanBrandMetaDataStorage.updateData();
        //log.info(CLASS_LOG_PREFIX + "同步设计方案品牌数据完成.");

        //设计方案样板房数据
        //log.info(CLASS_LOG_PREFIX + "开始更新设计方案样板房数据...");
        //designTemplateMetaDataStorage.updateData();
        //log.info(CLASS_LOG_PREFIX + "设计方案样板房数据更新完成.");


        //空间数据
        //log.info(CLASS_LOG_PREFIX + "开始更新空间数据...");
        //spaceCommonMetaDataStorage.updateData();
        //log.info(CLASS_LOG_PREFIX + "空间数据更新完成.");

        //方案平台数据
        log.info(CLASS_LOG_PREFIX + "开始同步方案平台数据.");
        designPlanPlatformMetaDataStorage.updateData();
        log.info(CLASS_LOG_PREFIX + "同步方案平台数据完成.");

        //用户数据
        //log.info(CLASS_LOG_PREFIX + "开始同步用户数据.");
        //sysUserMetaDataStorage.updateData();
        //log.info(CLASS_LOG_PREFIX + "同步用户数据完成.");

        //方案小区数据
        //log.info(CLASS_LOG_PREFIX + "开始同步方案小区数据.");
        //recommendedPlanLivingMetaDataStorage.updateData();
        //log.info(CLASS_LOG_PREFIX + "同步方案小区数据完成.");


        //菜单权限数据
        log.info(CLASS_LOG_PREFIX + "开始同步菜单权限数据.");
        sysRoleFuncMetaDataStorage.updateData();
        log.info(CLASS_LOG_PREFIX + "同步方案菜单权限完成.");

        //打组主方案子方案适用面积集合
        log.info(CLASS_LOG_PREFIX + "开始同步推荐方案打组子方案面积集合数据.");
        designPlanRecommendationMetaDataStorage.updateData();
        log.info(CLASS_LOG_PREFIX + "同步推荐方案打组子方案面积集合数据完成.");

        //装修报价方案适用面积集合
        log.info(CLASS_LOG_PREFIX + "开始同步方案装修报价数据.");
        designPlanDecoratePriceMetaDataStorage.updateData();
        log.info(CLASS_LOG_PREFIX + "同步方案装修报价数据完成.");

        //店铺方案数据
        log.info(CLASS_LOG_PREFIX + "开始同步店铺方案数据.");
        companyShopPlanMetaDataStorage.updateData();
        log.info(CLASS_LOG_PREFIX + "同步店铺方案数据完成.");

        //推荐方案产品数据
        log.info(CLASS_LOG_PREFIX + "开始同步推荐方案产品数据.");
        recommendedPlanProductMetaDataStorage.updateData();
        log.info(CLASS_LOG_PREFIX + "同步推荐方案产品数据完成.");

        log.info(CLASS_LOG_PREFIX + "重新加载所有推荐方案内存中缓存的数据完成，耗时:{}ms.", (System.currentTimeMillis() - startTime));

        Runtime.getRuntime().gc();
        //标识初始化完成
        //START_APPLICATION_DATA_FINISH = true;
    }

    //切换推荐方案存储模式
    public void changeRecommendationPlanStorageMode(int storageMode) {
        if (CACHE_MODE == storageMode || MEMORY_MODE == storageMode) {
            long startTime = System.currentTimeMillis();
            //公司数据
            companyMetaDataStorage.changeStorageMode(storageMode);
            //品牌元数据
            brandMetaDataStorage.changeStorageMode(storageMode);
            //渲染图片数据
            renderPicMetaDataStorage.changeStorageMode(storageMode);
            //方案品牌数据
            //designPlanBrandMetaDataStorage.changeStorageMode(storageMode);
            //方案样板房数据
            //designTemplateMetaDataStorage.changeStorageMode(storageMode);
            //空间数据
            //spaceCommonMetaDataStorage.changeStorageMode(storageMode);
            //方案平台数据
            designPlanPlatformMetaDataStorage.changeStorageMode(storageMode);
            //用户数据
            //sysUserMetaDataStorage.changeStorageMode(storageMode);
            //方案小区数据
            //recommendedPlanLivingMetaDataStorage.changeStorageMode(storageMode);
            //菜单角色数据
            sysRoleFuncMetaDataStorage.changeStorageMode(storageMode);
            //打组主方案子方案适用面积集合
            designPlanRecommendationMetaDataStorage.changeStorageMode(storageMode);
            //装修方案报价(推荐方案、全屋方案报价)
            designPlanDecoratePriceMetaDataStorage.changeStorageMode(storageMode);
            //店铺方案
            companyShopPlanMetaDataStorage.changeStorageMode(storageMode);
            //推荐方案产品数据
            recommendedPlanProductMetaDataStorage.changeStorageMode(storageMode);

            log.info(CLASS_LOG_PREFIX + "切换推荐方案存储模式完成!耗时:{}ms.", (System.currentTimeMillis() - startTime));

            Runtime.getRuntime().gc();
        }
    }
}
