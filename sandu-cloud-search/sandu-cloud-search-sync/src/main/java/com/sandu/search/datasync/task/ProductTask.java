package com.sandu.search.datasync.task;

import com.sandu.search.datasync.handler.GoodsMessageHandler;
import com.sandu.search.datasync.handler.ProductMessageHandler;
import com.sandu.search.initialize.GoodsIndex;
import com.sandu.search.initialize.ProductIndex;
import com.sandu.search.storage.StorageComponent;
import com.sandu.search.storage.StorageComponent.ChangeStorageModeModules;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 同步产品分类数据任务
 *
 * @date 20171229
 * @auth pengxuangang
 */
@Slf4j
@Component
public class ProductTask {

    private final static String CLASS_LOG_PREFIX = "同步产品数据任务:";

    private final ProductIndex productIndex;
    private final StorageComponent storageComponent;
    private final ProductMessageHandler productMessageHandler;
    private final GoodsIndex goodsIndex;
    private final GoodsMessageHandler goodsMessageHandler;

    //产品增量任务执行次数
    private static int incrTaskExcuteCount = 0;
    //产品全量任务执行次数
    private static int fullTaskExcuteCount = 0;
    // 是否正在执行全量更新(作用:1.避免同时执行两个全量更新;2.避免执行全量更新的同时执行增量更新)
    public static boolean isRuningProductSyncTask = false;

    //商品增量任务执行次数
    public static boolean isRuningGoodsDataTask = false;

    @Autowired
    public ProductTask(ProductIndex productIndex, StorageComponent storageComponent, ProductMessageHandler productMessageHandler,GoodsIndex goodsIndex,
                       GoodsMessageHandler goodsMessageHandler) {
        this.productIndex = productIndex;
        this.storageComponent = storageComponent;
        this.productMessageHandler = productMessageHandler;
        this.goodsIndex = goodsIndex;
        this.goodsMessageHandler = goodsMessageHandler;
    }

    /**
     * 产品数据增量更新
     *
     * @date 20180324
     * @auth pengxuangang
     * @strategy 每5分钟一次(集成10分钟)
     */
    /*@Scheduled(cron = "${task.product.incr.cron}")*/
    public void productIncrementDataSyncTask() {

        long startTime = System.currentTimeMillis();
        
        log.info(CLASS_LOG_PREFIX + "准备执行增量更新...");
        
        //第一次任务跳过
        /*if (0 == incrTaskExcuteCount) {
            ++incrTaskExcuteCount;
            log.info(CLASS_LOG_PREFIX + "产品数据增量更新,第一次任务跳过...");
            return;
        }*/

        // 如果正在执行全量更新,则不执行增量更新 add by huangsongbo
        if(isRuningProductSyncTask) {
        	log.info(CLASS_LOG_PREFIX + "增量更新停止(原因:正在进行全量更新)");
        	return;
        }
        
        //更新缓存数据
        storageComponent.reloadAllStorageInCache(ChangeStorageModeModules.product);

        log.info(CLASS_LOG_PREFIX + "产品数据增量更新.....");
        productMessageHandler.updateProductInfo();
        log.info(CLASS_LOG_PREFIX + "产品数据增量更新完成...耗时:{}ms", (System.currentTimeMillis() - startTime));

    }

    /**
     * 产品数据全量同步
     *
     * @date 20171229
     * @auth pengxuangang
     * @strategy 每1小时一次(集成2小时)
     */
    /*@Scheduled(cron = "${task.product.all.cron}")*/
    public void productDataSyncTask() {
        //任务开始时间
        long startTime = System.currentTimeMillis();

        log.info(CLASS_LOG_PREFIX + "准备执行全量更新...");
        
        // 如果正在执行全量更新,则不需要同时执行第二个全量更新任务 add by huangsongbo
        if(isRuningProductSyncTask) {
        	log.info(CLASS_LOG_PREFIX + "全量更新停止(原因:有任务正在进行全量更新)");
        	return;
        }
        
        isRuningProductSyncTask = true;

        ChangeStorageModeModules module = ChangeStorageModeModules.product;
        
        //切换存储模式
        storageComponent.changeStorageMode(StorageComponent.MEMORY_MODE, module);

        log.info(CLASS_LOG_PREFIX + "开始全量索引产品数据....");
        try {
        	productIndex.syncProductInfoData();
        } catch (Exception e) {
        	log.info(CLASS_LOG_PREFIX + "全量更新产品出错");
		} finally {
			isRuningProductSyncTask = false;
		}
        
        log.info(CLASS_LOG_PREFIX + "全量索引产品数据完成...耗时:{}ms", (System.currentTimeMillis() - startTime));

        //切换存储模式
        storageComponent.changeStorageMode(StorageComponent.CACHE_MODE, module);
        
    }

    /**
     * 执行fullSyncNum - 1 次增量后,执行一次全量同步 
     */
    @Value("${task.product.incr.fullSyncNum}")
    private Integer fullSyncNum;
    
    /**
     * 产品同步定时钟
     * 包含全量同步和增量同步
     * 
     * @author huangsongbo
     */
    @Scheduled(cron = "${task.product.incr.cron}")
    public void productSync() {
    	log.info("准备执行产品同步");
    	if(isRuningProductSyncTask) {
    		log.info("产品同步任务停止(有其他产品同步任务正在执行)");
    		return;
    	}
    	
    	isRuningProductSyncTask = true;
    	
    	long startTime = System.currentTimeMillis();
    	
    	if(fullSyncNum == null) {
    		fullSyncNum = 10;
    	}
    	
    	Callable<String> callable = new Callable<String>() {
			
			@Override
			public String call() throws Exception {
				try {
		    		if(incrTaskExcuteCount % fullSyncNum == 0 && incrTaskExcuteCount != 0) {
		        		log.info("准备执行产品\"全量\"同步, incrTaskExcuteCount = {}", incrTaskExcuteCount);
		        		productFullSync();
		        	}else {
		        		log.info("准备执行产品\"增量\"同步, incrTaskExcuteCount = {}", incrTaskExcuteCount);
		        		productIncrSync();
		        	}
		    	} catch (Exception e) {
		    		e.printStackTrace();
					log.error(CLASS_LOG_PREFIX + e.getMessage());
				} 
				return "over";
			}
			
		};
		
		ExecutorService executorService = Executors.newSingleThreadExecutor();
		Future<String> future = executorService.submit(callable);
		try {
			future.get(20, TimeUnit.MINUTES);
		} catch (InterruptedException e) {
			log.info("本次产品同步异常(InterruptedException)");
		} catch (ExecutionException e) {
			log.info("本次产品同步异常(ExecutionException)");
		} catch (TimeoutException e) {
			log.info("本次产品同步异常(TimeoutException)");
		} finally {
			isRuningProductSyncTask = false;
			incrTaskExcuteCount ++;
			log.info("本次产品同步完成, 耗时: {}秒", (System.currentTimeMillis() - startTime) / 1000);
		}
    	
    }
    
    /**
     * 增量同步
     * 
     * @author huangsongbo
     */
    private void productIncrSync() {
    	// 增量同步
    	productMessageHandler.updateProductInfo();
	}

    /**
     * 全量同步
     * 
     * @author huangsongbo
     */
	private void productFullSync() {
		ChangeStorageModeModules module = ChangeStorageModeModules.product;
        
        //切换存储模式
        storageComponent.changeStorageMode(StorageComponent.MEMORY_MODE, module);

        productIndex.syncProductInfoData();
        
        //切换存储模式
        storageComponent.changeStorageMode(StorageComponent.CACHE_MODE, module);
	}

	/**
     * 商品列表数据全量同步
     *
     * @date 20180801
     * @auth weisheng
     * @strategy 每1小时一次
     */
    @Scheduled(cron ="${task.goods.all.cron}")
    public void goodsDataSyncTask() {
        //任务开始时间
        long startTime = System.currentTimeMillis();

        log.info(CLASS_LOG_PREFIX + "准备执行全量更新...");

        // 如果正在执行全量更新,则不需要同时执行第二个全量更新任务 add by huangsongbo
        if(isRuningGoodsDataTask) {
            log.info(CLASS_LOG_PREFIX + "全量更新停止(原因:有任务正在进行全量更新)");
            return;
        }

        isRuningGoodsDataTask = true;

        ChangeStorageModeModules module = ChangeStorageModeModules.goods;

        //切换存储模式
        try {
            storageComponent.changeStorageMode(StorageComponent.MEMORY_MODE, module);

            log.info(CLASS_LOG_PREFIX + "开始全量索引商品列表数据....");
            goodsIndex.syncGoodsInfoData();
            log.info(CLASS_LOG_PREFIX + "全量索引商品列表数据完成...耗时:{}ms", (System.currentTimeMillis() - startTime));

            //切换存储模式
            storageComponent.changeStorageMode(StorageComponent.CACHE_MODE, module);
			isRuningGoodsDataTask = false;
        }catch (Exception e){
            log.error("商品全量更新失败"+e);
            isRuningGoodsDataTask = false;
        }finally {
            isRuningGoodsDataTask = false;
        }

    }



    /**
     * 商品数据增量更新
     *
     * @date 20180813
     * @auth weisheng
     * @strategy 每5分钟一次
     */
    @Scheduled(cron ="${task.goods.incr.cron}")
    public void goodsIncrementDataSyncTask() {

        long startTime = System.currentTimeMillis();

        log.info(CLASS_LOG_PREFIX + "准备执行商品列表增量更新...");

        //第一次任务跳过
        if (0 == incrTaskExcuteCount) {
            ++incrTaskExcuteCount;
            log.info(CLASS_LOG_PREFIX + "商品列表数据增量更新,第一次任务跳过...");
            return;
        }

        // 如果正在执行全量更新,则不执行增量更新
        if(isRuningGoodsDataTask) {
            log.info(CLASS_LOG_PREFIX + "商品列表数据增量更新停止(原因:商品正在进行全量更新)");
            return;
        }

        log.info(CLASS_LOG_PREFIX + "商品列表数据增量更新.....");
        goodsMessageHandler.updateGoodsInfo();
        log.info(CLASS_LOG_PREFIX + "商品列表数据增量更新完成...耗时:{}ms", (System.currentTimeMillis() - startTime));

    }


}
