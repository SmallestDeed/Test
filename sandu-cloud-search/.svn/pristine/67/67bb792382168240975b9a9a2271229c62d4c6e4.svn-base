package com.sandu.search.datasync.handler;

import com.sandu.search.common.tools.EntityUtil;
import com.sandu.search.common.tools.JsonUtil;
import com.sandu.search.config.ElasticSearchConfig;
import com.sandu.search.entity.elasticsearch.constant.IndexConstant;
import com.sandu.search.entity.elasticsearch.constant.TypeConstant;
import com.sandu.search.entity.elasticsearch.dto.DeleteRequestDTO;
import com.sandu.search.entity.elasticsearch.dto.UpdateRequestDTO;
import com.sandu.search.entity.elasticsearch.index.ProductIndexMappingData;
import com.sandu.search.entity.elasticsearch.po.ProductPo;
import com.sandu.search.entity.elasticsearch.response.BulkStatistics;
import com.sandu.search.exception.ProductIndexException;
import com.sandu.search.exception.ElasticSearchException;
import com.sandu.search.initialize.ProductIndex;
import com.sandu.search.service.elasticsearch.ElasticSearchService;
import com.sandu.search.service.index.ProductIndexService;
import com.sandu.search.storage.StorageComponent;
import com.sandu.search.storage.StorageComponent.ChangeStorageModeModules;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 产品消息处理
 *
 * @date 2018/3/24
 * @auth pengxuangang
 * @mail xuangangpeng@gmail.com
 */
@Slf4j
@Component
public class ProductMessageHandler {

    private final static String CLASS_LOG_PREFIX = "Rabbit产品消息处理:";

    private final ProductIndex productIndex;
    private final ElasticSearchService elasticSearchService;
    private final ProductIndexService productIndexService;
    private final StorageComponent storageComponent;
    private final ElasticSearchConfig elasticSearchConfig;

    //待更新产品列表
    private static volatile List<Integer> waitUpdateProductIdList = new ArrayList<>();

    @Autowired
    public ProductMessageHandler(
    		ProductIndex productIndex, 
    		ElasticSearchService elasticSearchService, 
    		ProductIndexService productIndexService,
    		StorageComponent storageComponent,
            ElasticSearchConfig elasticSearchConfig) {
        this.productIndex = productIndex;
        this.elasticSearchService = elasticSearchService;
        this.productIndexService = productIndexService;
        this.storageComponent = storageComponent;
        this.elasticSearchConfig = elasticSearchConfig;
    }

    /**
     * 新增/更新产品消息处理
     *
     * @param productIndexMappingDataList 产品分类索引对象
     * @return
     */
    public boolean addAndUpdate(List<ProductIndexMappingData> productIndexMappingDataList) {

        if (null == productIndexMappingDataList || 0 >= productIndexMappingDataList.size()) {
            log.info(CLASS_LOG_PREFIX + "消费消息失败，消息对象为空。");
            return false;
        }

        //根据产品ID更新
        List<Integer> productIdList = new ArrayList<>(productIndexMappingDataList.size());
        //根据产品属性增量更新
        List<ProductIndexMappingData> productList = new ArrayList<>(productIndexMappingDataList.size());

        productIndexMappingDataList.forEach(productIndexMappingData -> {
            //判断更新类型
            List<String> fieldList = null;
            try {
                fieldList = EntityUtil.queryHasValueExcludeNameIsIdInField(productIndexMappingData);
            } catch (IllegalAccessException e) {
                log.warn(CLASS_LOG_PREFIX + "检查对象失败,IllegalAccessException:{}.", e);
            }

            if (null != fieldList && 0 < fieldList.size()) {
                //属性增量更新
                productList.add(productIndexMappingData);
            } else {
                //ID更新
                productIdList.add(productIndexMappingData.getId());
            }
        });

        //ID更新
        if (null != productIdList && 0 < productIdList.size()) {
            //加入待更新产品列表
            waitUpdateProductIdList.addAll(productIdList);
            log.info(CLASS_LOG_PREFIX + "消费消息成功，产品已加入待更新产品列表!productIdList:{},现有待更新产品数据条数:{}.", JsonUtil.toJson(productIdList), waitUpdateProductIdList.size());
        }

        //属性增量更新
        if (null != productList && 0 < productList.size()) {
            //加入属性增量更新列表
            updateIncrProductInfo(productList);
            log.info(CLASS_LOG_PREFIX + "消费消息成功，产品已加入属性增量更新列表!productList:{},现有待更新产品数据条数:{}.", JsonUtil.toJsonExcludeEmpty(productList), productList.size());
        }

        return true;
    }

    /**
     * 更新增量产品数据
     *
     * @return
     */
    public boolean updateIncrProductInfo(List<ProductIndexMappingData> productList) {

        if (null == productList || 0 == productList.size()) {
            return true;
        }

        //bulk操作列表
        List<Object> bulkUpdateList = new ArrayList<>();

        //构造更新对象
        for (ProductIndexMappingData product : productList) {

            if (null == product || null == product.getId() || 0 == product.getId()) {
                continue;
            }

            //创建索引对象
            UpdateRequestDTO updateRequestDTO = new UpdateRequestDTO(
                    IndexConstant.INDEX_PRODUCT_INFO_ALIASES,
                    TypeConstant.TYPE_DEFAULT,
                    product.getId() + "",
                    JsonUtil.toJsonExcludeEmpty(product)
            );

            //加入待更新列表
            bulkUpdateList.add(updateRequestDTO);
        }

        //提交数据
        BulkStatistics bulkStatistics = null;
        try {
            bulkStatistics = elasticSearchService.bulk(bulkUpdateList, null);
        } catch (ElasticSearchException e) {
            log.error(CLASS_LOG_PREFIX + "索引产品数据异常:ElasticSearchException:{}", e);
        }
        log.info(CLASS_LOG_PREFIX + "索引产品数据成功:成功索引数:{},BulkStatistics:{}", new String[]{
                bulkUpdateList.size() + "",
                null == bulkStatistics ? null : bulkStatistics.toString()
        });

        return true;
    }

    /**
     * 更新产品数据
     *
     * @return
     */
    public boolean updateProductInfo() {

        //去空
        waitUpdateProductIdList = waitUpdateProductIdList.stream().filter(waitUpdateProductId -> null != waitUpdateProductId && 0 != waitUpdateProductId).collect(Collectors.toList());
        //去重
        waitUpdateProductIdList = waitUpdateProductIdList.stream().distinct().collect(Collectors.toList());
        
        if (null != waitUpdateProductIdList && 0 < waitUpdateProductIdList.size()) {
        	
            //更新产品列表
            List<Integer> updateProductIdList = new ArrayList<>(waitUpdateProductIdList);
            //更新完成删除待更新区域产品数据
            waitUpdateProductIdList = new ArrayList<>();

            // 更新必要的元数据
        	storageComponent.reloadAllStorageInCache(ChangeStorageModeModules.product);
            
            log.info(CLASS_LOG_PREFIX + "准备更新数据，查询产品信息--总条数:{}, ID列表:{}.", updateProductIdList.size(), updateProductIdList);
            //查询产品信息
            List<ProductPo> productPoList;
            try {
                productPoList = productIndexService.queryProductPoListByProductIdList(updateProductIdList);
            } catch (ProductIndexException e) {
                log.error(CLASS_LOG_PREFIX + "消费消息失败。查询产品信息失败,还原产品状态为待更新!ProductIndexException:{}.", e);
                //还原产品数据,留存下次更新
                waitUpdateProductIdList.addAll(updateProductIdList);
                return false;
            }

            //更新数据
            log.info(CLASS_LOG_PREFIX + "开始更新数据，数据总条数:{}.", updateProductIdList.size());
            int updateProdcutSuccessCount = productIndex.indexProdcutData(productPoList);
            log.info(CLASS_LOG_PREFIX + "更新数据完成，成功{}条,失败{}条.", updateProdcutSuccessCount, productPoList.size() - updateProdcutSuccessCount);
        }else {
        	// 更新必要的元数据
        	storageComponent.reloadAllStorageInCache(ChangeStorageModeModules.product);
        }

        return true;
    }

    /**
     * 获取待更新产品ID列表
     *
     * @return
     */
    public List<Integer> queryWaitUpdateProductIdList() {
        return waitUpdateProductIdList;
    }

    /**
     * 删除产品消息处理
     *
     * @param productIndexMappingDataList 产品分类索引对象
     * @return
     */
    public boolean delete(List<ProductIndexMappingData> productIndexMappingDataList) throws ElasticSearchException {

        if (null == productIndexMappingDataList || 0 >= productIndexMappingDataList.size()) {
            log.info(CLASS_LOG_PREFIX + "消费消息失败，消息对象为空。");
            return false;
        }
        String indexName = StringUtils.isEmpty(elasticSearchConfig.getReIndexProductDataIndexName())?IndexConstant.INDEX_PRODUCT_INFO_ALIASES:elasticSearchConfig.getReIndexProductDataIndexName();
        for (ProductIndexMappingData productIndexMappingData : productIndexMappingDataList) {
            if (productIndexMappingData.getId() != null) {
                DeleteRequestDTO deleteRequestDTO = new DeleteRequestDTO(
                        indexName,
                        TypeConstant.TYPE_DEFAULT,
                        productIndexMappingData.getId().toString());
                boolean falg = elasticSearchService.delete(deleteRequestDTO);
                if (!falg) {
                    log.error(CLASS_LOG_PREFIX + "删除产品失败,productId:{}", productIndexMappingData.getId());
                }
            }
        }

        return true;
    }

	public void syncProductInfoListImmediately(List<Integer> productIdList) {
		
		List<ProductPo> productPoList = new ArrayList<ProductPo>();
        try {
            productPoList = productIndexService.queryProductPoListByProductIdList(productIdList);
        } catch (ProductIndexException e) {
            log.error(CLASS_LOG_PREFIX + "e");
        }
		
		//更新数据
        log.info(CLASS_LOG_PREFIX + "开始更新数据，数据总条数:{}.", productIdList.size());
        int updateProdcutSuccessCount = productIndex.indexProdcutData(productPoList);
        log.info(CLASS_LOG_PREFIX + "更新数据完成，成功{}条,失败{}条.", updateProdcutSuccessCount, productPoList.size() - updateProdcutSuccessCount);
	}
}
