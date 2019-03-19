package com.sandu.search.initialize;

import com.sandu.search.config.ElasticSearchConfig;
import com.sandu.search.datasync.task.GroupProductTask;
import com.sandu.search.datasync.task.ProductTask;
import com.sandu.search.datasync.task.RecommendationTask;
import com.sandu.search.storage.StorageComponent;
import com.sandu.search.storage.StorageComponent.ChangeStorageModeModules;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 索引总线
 *
 * @date 2018/6/21
 * @auth pengxuangang
 * @mail xuangangpeng@gmail.com
 */
@Slf4j
@Component
public class IndexBus {

    private final static String CLASS_LOG_PREFIX = "搜索引擎索引总线:";

    private final ProductIndex productIndex;
    private final GroupProductIndex groupProductIndex;
    private final StorageComponent storageComponent;
    private final ElasticSearchConfig elasticSearchConfig;
    private final RecommendationPlanIndex recommendationPlanIndex;
    private final GoodsIndex goodsIndex;
    private final FullHouseIndex fullHouseIndex;


    @Autowired
    public IndexBus(FullHouseIndex fullHouseIndex, ProductIndex productIndex, GroupProductIndex groupProductIndex, StorageComponent storageComponent, ElasticSearchConfig elasticSearchConfig, RecommendationPlanIndex recommendationPlanIndex, GoodsIndex goodsIndex) {
        this.productIndex = productIndex;
        this.groupProductIndex = groupProductIndex;
        this.storageComponent = storageComponent;
        this.elasticSearchConfig = elasticSearchConfig;
        this.recommendationPlanIndex = recommendationPlanIndex;
        this.goodsIndex = goodsIndex;
        this.fullHouseIndex = fullHouseIndex;
    }

    //初始化数据
    @PostConstruct
    public void initDataHub() {

        //是否需要索引推荐方案数据
        boolean indexRecommendationPlanData = elasticSearchConfig.isIndexRecommendationPlanData();
        log.info(CLASS_LOG_PREFIX + "是否需要索引推荐方案数据:{}", indexRecommendationPlanData);
        //是否需要索引产品数据
        boolean indexProductData = elasticSearchConfig.isIndexProductData();
        log.info(CLASS_LOG_PREFIX + "是否需要索引产品数据:{}", indexProductData);
        //是否需要索引商品数据
        boolean initGoodsData = elasticSearchConfig.isInitGoodsData();
        log.info(CLASS_LOG_PREFIX + "是否需要索引商品数据:{}", initGoodsData);
        //是否需要索引组合产品数据
        boolean indexGroupProductData = elasticSearchConfig.isIndexGroupProductData();
        log.info(CLASS_LOG_PREFIX + "是否需要索引组合产品数据:{}", indexGroupProductData);
        //是否索引
        if (indexRecommendationPlanData || indexProductData || indexGroupProductData || initGoodsData) {
            log.info(CLASS_LOG_PREFIX + "切换存储模式为内存...");
            
            if(indexProductData) {
            	ProductTask.isRuningProductSyncTask = true;
            }

            ChangeStorageModeModules module = ChangeStorageModeModules.all;
            
            //切换存储模式,同时更新元数据
            storageComponent.changeStorageMode(StorageComponent.MEMORY_MODE, module);

            //索引数据
            if (indexRecommendationPlanData) {
                RecommendationTask.isRuningFullTask = true;
                //切换推荐方案存储模式
                //storageComponent.changeRecommendationPlanStorageMode(StorageComponent.MEMORY_MODE);
                //索引全屋方案到推荐方案表
                log.info(CLASS_LOG_PREFIX + "开始同步全屋方案数据...");
                fullHouseIndex.syncFullHousePlanData();
                //同步推荐方案数据
                log.info(CLASS_LOG_PREFIX + "开始同步推荐方案数据...");
                recommendationPlanIndex.syncRecommendationPlanData();
            }

            //索引数据
            if (indexProductData) {
                //同步产品信息数据
                log.info(CLASS_LOG_PREFIX + "开始同步产品信息数据...");
                String reCreatedIndexName = elasticSearchConfig.getReIndexProductDataIndexName();
                log.info(CLASS_LOG_PREFIX + "重建索引名称:{}", reCreatedIndexName);
                productIndex.syncProductInfoData();
                ProductTask.isRuningProductSyncTask = false;
            }

            //索引数据
            if(initGoodsData){
                ProductTask.isRuningGoodsDataTask = true;
            }
            if (initGoodsData) {
                //同步商品列表数据
                log.info(CLASS_LOG_PREFIX + "开始同步商品列表数据...");
                String reIndexGoodsDataIndexName = elasticSearchConfig.getReIndexGoodsDataIndexName();
                elasticSearchConfig.getReIndexGoodsDataIndexName();
                log.info(CLASS_LOG_PREFIX + "重建索引名称:{}", reIndexGoodsDataIndexName);
                goodsIndex.syncGoodsInfoData();
                ProductTask.isRuningGoodsDataTask = false;
            }


            //索引组合产品数据
            if (indexGroupProductData) {
                GroupProductTask.isRunningFullTask = true;
                //同步组合产品信息数据
                log.info(CLASS_LOG_PREFIX + "开始同步组合产品信息数据...");
                String groupProductDataIndexName = elasticSearchConfig.getReIndexGroupProductDataIndexName();
                log.info(CLASS_LOG_PREFIX + "重建索引名称:{}", groupProductDataIndexName);
                groupProductIndex.syncGroupProductInfoData();

                GroupProductTask.isRunningFullTask = false;

            }

            //切换存储模式
            log.info(CLASS_LOG_PREFIX + "切换存储模式为缓存...");
            storageComponent.changeStorageMode(StorageComponent.CACHE_MODE, module);
        } else {
            //不索引数据时则更新缓存数据
            //初始化元数据
            storageComponent.startApplicationLoadMetaData();
        }

        log.info(CLASS_LOG_PREFIX + "初始化索引数据完成....");
    }
    
}
