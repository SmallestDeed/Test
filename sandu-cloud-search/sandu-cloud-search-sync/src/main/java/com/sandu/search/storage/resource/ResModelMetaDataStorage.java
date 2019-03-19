package com.sandu.search.storage.resource;

import com.sandu.search.dao.ResModelMapper;
import com.sandu.search.storage.StorageComponent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
public class ResModelMetaDataStorage {

    private final static String CLASS_LOG_PREFIX = "模型资源元数据存储:";
    //默认缓存模式
    private static Integer STORAGE_MODE = StorageComponent.CACHE_MODE;

    // 模型存储map，map<模型id，模型文件地址>
    private static Map<String, String> resModelMap = null;

    @Autowired
    private ResModelMapper resModelMapper;

    /**
     * 切换存储模式
     *
     * @param storageMode
     */
    public void changeStorageMode(Integer storageMode) {
        STORAGE_MODE = storageMode;
        if (StorageComponent.MEMORY_MODE == storageMode) {
            updateData();
        } else if (StorageComponent.CACHE_MODE == storageMode) {
            resModelMap = null;
        }
    }

    /**
     * 更新模型资源元数据
     */
    public void updateData() {
        log.info(CLASS_LOG_PREFIX + "更新模型资源元数据开始。。。");
        // List<ResModel> resModelList = resModelMapper.queryResModelMetaData();
    }

}
