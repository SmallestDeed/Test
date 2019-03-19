package com.sandu.search.storage.resource;

import com.sandu.search.common.constant.RedisConstant;
import com.sandu.search.entity.elasticsearch.po.metadate.ResPicPo;
import com.sandu.search.exception.MetaDataException;
import com.sandu.search.service.metadata.MetaDataService;
import com.sandu.search.service.redis.RedisService;
import com.sandu.search.storage.StorageComponent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 渲染图片资源元数据存储
 *
 * @date 2018/5/31
 * @auth pengxuangang
 * @mail xuangangpeng@gmail.com
 */
@Slf4j
@Component
public class RenderPicMetaDataStorage {

    private final static String CLASS_LOG_PREFIX = "渲染图片资源元数据存储:";
    //默认缓存模式
    private static Integer STORAGE_MODE = StorageComponent.CACHE_MODE;

    private final RedisService redisService;
    private final MetaDataService metaDataService;

    @Autowired
    public RenderPicMetaDataStorage(MetaDataService metaDataService, RedisService redisService) {
        this.metaDataService = metaDataService;
        this.redisService = redisService;
    }

    private static Map<String, String> renderCoverPicMap = null;
    private static Map<String, String> render720PicMap = null;

    //切换存储模式
    @SuppressWarnings("all")
    public void changeStorageMode(Integer storageMode) {
        //缓存模式
        if (StorageComponent.CACHE_MODE == storageMode) {
            //清空内存占用
            renderCoverPicMap = null;
            render720PicMap = null;
            //切换
            STORAGE_MODE = storageMode;
            //内存模式
        } else if(StorageComponent.MEMORY_MODE == storageMode) {
            //切换
            STORAGE_MODE = storageMode;
            //写入内存
            updateData();
        }
        log.info(CLASS_LOG_PREFIX + "渲染图片资源存储模式切换成功，当前存储:{}.", StorageComponent.CACHE_MODE == STORAGE_MODE ? "缓存" : "内存");
    }

    //获取Map数据方法兼容
    private String getMap(String mapName, String keyName) {
        //缓存模式
        if (StorageComponent.CACHE_MODE == STORAGE_MODE) {
            return redisService.getMap(mapName, keyName);
            //内存模式
        } else if (StorageComponent.MEMORY_MODE == STORAGE_MODE) {
            if (RedisConstant.RECOMMENDATION_PLAN_COVER_PIC_DATA.equals(mapName)) {
                return renderCoverPicMap.get(keyName);
                //return redisService.getMap(mapName, keyName);
            }
            if (RedisConstant.RECOMMENDATION_PLAN_RENDER_720_PIC_DATA.equals(mapName)) {
                return render720PicMap.get(keyName);
                //return redisService.getMap(mapName, keyName);
            }
        }
        return null;
    }

    //更新数据
    public void updateData() {
        log.info(CLASS_LOG_PREFIX + "查询渲染图片资源元数据....");

        //方案封面图片
        List<ResPicPo> resCoverPicList;
        try {
            resCoverPicList = metaDataService.queryRecommendPlanCoverPicMetaData();
        } catch (MetaDataException e) {
            log.error(CLASS_LOG_PREFIX + "查询渲染封面图片资源元数据失败: MetaDataException:{}", e);
            throw new NullPointerException(CLASS_LOG_PREFIX + "查询渲染封面图片资源元数据失败,List<ResPicPo> is null.MetaDataException:" + e);
        }
        log.info(CLASS_LOG_PREFIX + "查询渲染封面图片资源元数据完成,总条数:{}", (null == resCoverPicList ? 0 : resCoverPicList.size()));

        // 推荐方案列表720图片地址
        List<ResPicPo> resRender720PicList;
        try {
            resRender720PicList = metaDataService.queryRecommendedPlanFinallyRenderPicMetaData();
        } catch (MetaDataException e) {
            log.error(CLASS_LOG_PREFIX + "查询渲染720图片资源元数据失败: MetaDataException:{}", e);
            throw new NullPointerException(CLASS_LOG_PREFIX + "查询渲染720图片资源元数据失败,List<ResPicPo> is null.MetaDataException:" + e);
        }
        log.info(CLASS_LOG_PREFIX + "查询渲染720图片资源元数据完成,总条数:{}", (null == resRender720PicList ? 0 : resRender720PicList.size()));


        //Map对象
        Map<String, String> tempPlanCoverPicMap = new HashMap<>();
        Map<String, String> tempRender720PicMap = new HashMap<>();

        //转换为Map元数据
        if (null != resCoverPicList && 0 != resCoverPicList.size()) {
            resCoverPicList.forEach(resPicPo -> tempPlanCoverPicMap.put(resPicPo.getId() + "", resPicPo.getPicPath()));
        }
        log.info(CLASS_LOG_PREFIX + "转换方案封面图片资源元数据完成");

        //转换为Map元数据
        if (null != resRender720PicList && 0 != resRender720PicList.size()) {
            resRender720PicList.forEach(resPicPo -> tempRender720PicMap.put(resPicPo.getRecommendationPlanId() + "", resPicPo.getPicPath()));
        }
        log.info(CLASS_LOG_PREFIX + "转换方案封面图片资源元数据完成");

        //缓存装载
        redisService.addMapCompatible(RedisConstant.RECOMMENDATION_PLAN_COVER_PIC_DATA, tempPlanCoverPicMap);
        log.info(CLASS_LOG_PREFIX + "缓存装载方案封面图片资源元数据完成");
        redisService.addMapCompatible(RedisConstant.RECOMMENDATION_PLAN_RENDER_720_PIC_DATA, tempRender720PicMap);
        log.info(CLASS_LOG_PREFIX + "缓存装载方案渲染720图片资源元数据完成");

        //内存模式
        if (StorageComponent.MEMORY_MODE == STORAGE_MODE) {
            renderCoverPicMap = tempPlanCoverPicMap;
            render720PicMap = tempRender720PicMap;
            log.info(CLASS_LOG_PREFIX + "内存装载渲染图片资源元数据完成....");
        }
    }

    /**
     * 根据图片ID获取图片路径
     *
     * @param planId 推荐方案ID
     * @return
     */
    public String getPicPathByPicId(Integer planId) {
        if (null == planId || 0 == planId) {
            return null;
        }
        return getMap(RedisConstant.RECOMMENDATION_PLAN_COVER_PIC_DATA, planId + "");
    }


    /**
     * 根据推荐方案ID获取图片路径
     *
     * @param planId 推荐方案ID
     * @return
     */
    public String getPicPathByPlanId(Integer planId) {
        if (null == planId || 0 == planId) {
            return null;
        }
        return getMap(RedisConstant.RECOMMENDATION_PLAN_RENDER_720_PIC_DATA, planId + "");
    }

}
