package com.sandu.search.controller;

import com.sandu.search.common.tools.JsonUtil;
import com.sandu.search.datasync.handler.ProductMessageHandler;
import com.sandu.search.datasync.task.ProductTask;
import com.sandu.search.entity.elasticsearch.index.ProductIndexMappingData;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 产品数据同步接口
 *
 * @date 2018/3/22
 * @auth pengxuangang
 * @mail xuangangpeng@gmail.com
 */
@Slf4j
@RestController
@EnableAutoConfiguration
@RequestMapping("/sync/product")
public class ProductDataSyncController {

    private final ProductMessageHandler productMessageHandler;

    private final static String CLASS_LOG_PREFIX = "[数据同步]产品信息同步接口:";

    @Autowired
    public ProductDataSyncController(ProductMessageHandler productMessageHandler) {
        this.productMessageHandler = productMessageHandler;
    }

    /**
     * 同步产品信息
     *
     * @param productIdList
     * @return
     */
    @PostMapping
    String syncProductInfoList(@RequestBody List<Integer> productIdList) {

        if (null == productIdList || 0 >= productIdList.size()) {
            log.info(CLASS_LOG_PREFIX + "同步产品信息失败，产品ID为空!");
            return CLASS_LOG_PREFIX + "同步产品信息失败，产品ID为空!";
        }

        //转换对象
        List<ProductIndexMappingData> productIndexMappingDataList = new ArrayList<>(productIdList.size());
        productIdList.stream().distinct().forEach(productId -> {
            ProductIndexMappingData productIndexMappingData = new ProductIndexMappingData();
            productIndexMappingData.setId(productId);
            productIndexMappingDataList.add(productIndexMappingData);
        });

        log.info(CLASS_LOG_PREFIX + "Rest服务收到待同步产品，产品ID:{}", JsonUtil.toJson(productIdList));

        //加入队列标识
        boolean addAndUpdateFlag = productMessageHandler.addAndUpdate(productIndexMappingDataList);

        if (!addAndUpdateFlag) {
            log.error(CLASS_LOG_PREFIX + "同步产品信息失败。加入待更新产品队列失败!");
            return CLASS_LOG_PREFIX + "同步产品信息失败。加入待更新产品队列失败!";
        }

        log.info(CLASS_LOG_PREFIX + "产品已加入待更新列表,产品ID:{}.", JsonUtil.toJson(productIdList));
        return CLASS_LOG_PREFIX + "产品已加入待更新列表,产品ID:" + JsonUtil.toJson(productIdList);
    }


    /**
     * 查询待同步产品列表
     *
     * @return
     */
    @GetMapping
    Map<String, Object> syncProductInfoList() {
        List<Integer> waitUpdateProductIdList = productMessageHandler.queryWaitUpdateProductIdList();

        Map<String, Object> resultMap = new HashMap<>(2);
        resultMap.put("obj", waitUpdateProductIdList);
        resultMap.put("size", waitUpdateProductIdList.size());
        return resultMap;
    }
    
    /**
     * 强制更新某些产品
     * 
     * @author huangsongbo
     * @param productIdList
     * @return
     */
    @PostMapping("/syncProductInfoListImmediately")
    public String syncProductInfoListImmediately(@RequestBody List<Integer> productIdList) {
    	if(productIdList == null || productIdList.size() < 1) {
    		return "no data to update";
    	}
    	productMessageHandler.syncProductInfoListImmediately(productIdList);
    	return "success";
    }
    
    @PostMapping("/updateIsRuningFullTask")
    public String updateIsRuningFullTask(String str) {
    	if(StringUtils.isEmpty(str)) {
    		return "str不能为空";
    	}
    	
    	boolean old = ProductTask.isRuningProductSyncTask;
    	
    	if(StringUtils.equals("true", str)) {
    		ProductTask.isRuningProductSyncTask = true;
    		return "isRuningFullTask: " + old + " -> " +ProductTask.isRuningProductSyncTask;
    	}
    	if(StringUtils.equals("false", str)) {
    		ProductTask.isRuningProductSyncTask = false;
    		return "isRuningFullTask: " + old + " -> " +ProductTask.isRuningProductSyncTask;
    	}
    	return "未知str";
    }
    
}
