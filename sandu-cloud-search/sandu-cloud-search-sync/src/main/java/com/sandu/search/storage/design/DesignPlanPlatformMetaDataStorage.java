package com.sandu.search.storage.design;

import com.sandu.search.common.constant.PlatformConstant;
import com.sandu.search.common.constant.RedisConstant;
import com.sandu.search.common.constant.UserDefinedConstant;
import com.sandu.search.common.tools.JsonUtil;
import com.sandu.search.entity.elasticsearch.po.design.DesignPlanPlatformData;
import com.sandu.search.entity.elasticsearch.po.metadate.DesignPlanPlatformRelPo;
import com.sandu.search.exception.MetaDataException;
import com.sandu.search.service.metadata.MetaDataService;
import com.sandu.search.service.redis.RedisService;
import com.sandu.search.storage.StorageComponent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 方案平台元数据存储(全平台数据过滤)
 *
 * @date 20180703
 * @auth xiaoxc
 */
@Slf4j
@Component
public class DesignPlanPlatformMetaDataStorage {

    private final static String CLASS_LOG_PREFIX = "方案平台元数据存储(全平台数据过滤):";

    //默认缓存模式
    private static Integer STORAGE_MODE = StorageComponent.CACHE_MODE;

    private final RedisService redisService;
    private final MetaDataService metaDataService;

    @Autowired
    public DesignPlanPlatformMetaDataStorage(RedisService redisService, MetaDataService metaDataService) {
        this.redisService = redisService;
        this.metaDataService = metaDataService;
    }

    //方案平台元数据Map<方案ID,方案平台数据>
    private static Map<String, String> designPlanPlatformRelMap = null;

    //切换存储模式
    public void changeStorageMode(Integer storageMode) {
        //缓存模式
        if (StorageComponent.CACHE_MODE == storageMode) {
            //清空内存占用
            designPlanPlatformRelMap = null;
            //切换
            STORAGE_MODE = storageMode;
            //内存模式
        } else if(StorageComponent.MEMORY_MODE == storageMode) {
            /*平台数据过多，太占用内存，所以取消方案平台数据的内存策略,只将数据更新至缓存
             *切换在下面是因为更新数据如果是内存模式会将数据刷入内存
             */
            updateData();
            //切换
            STORAGE_MODE = storageMode;

            //designPlanPlatformRelMap = redisService.getMap(RedisConstant.DESIGN_PLAN_PLATFORM_REL_MAP_DATA);
        }
        log.info(CLASS_LOG_PREFIX + "方案平台存储模式切换成功，当前存储:{}.", StorageComponent.CACHE_MODE == STORAGE_MODE ? "缓存" : "内存");
    }

    //获取Map数据方法兼容
    private String getMap(String mapName, String keyName) {
        //缓存模式
        if (StorageComponent.CACHE_MODE == STORAGE_MODE) {
            return redisService.getMap(mapName, keyName);
            //内存模式
        } else if (StorageComponent.MEMORY_MODE == STORAGE_MODE) {
            if (RedisConstant.DESIGN_PLAN_PLATFORM_REL_MAP_DATA.equals(mapName)) {
                //平台数据过多，太占用内存，所以取消方案平台数据的内存策略
                //return designPlanPlatformRelMap.get(keyName);
                return redisService.getMap(mapName, keyName);
            }
        }
        return null;
    }

    //更新数据
    public void updateData() {

        log.info(CLASS_LOG_PREFIX + "开始获取方案平台元数据....");
        //方案平台元数据
        List<DesignPlanPlatformRelPo> designPlanPlatformRelPoList;
        try {
            designPlanPlatformRelPoList = metaDataService.queryAllPlatformPlanMetaData();
        } catch (MetaDataException e) {
            log.error(CLASS_LOG_PREFIX + "获取方案平台元数据失败: MetaDataException:{}", e);
            throw new NullPointerException(CLASS_LOG_PREFIX + "获取方案平台元数据失败,List<DesignPlanPlatformRelPo> is null.MetaDataException:" + e);
        }
        log.info(CLASS_LOG_PREFIX + "获取方案平台元数据完成,总条数:{}", (null == designPlanPlatformRelPoList ? 0 : designPlanPlatformRelPoList.size()));

        //处理对象更新到缓存
        Map<String, String> planPlatformRelJsonMap = this.planPlatformCache(designPlanPlatformRelPoList, UserDefinedConstant.FULL_UPDATE_TYPE);

        //内存模式
        if (StorageComponent.MEMORY_MODE == STORAGE_MODE) {
            designPlanPlatformRelMap = planPlatformRelJsonMap;
            log.info(CLASS_LOG_PREFIX + "方案平台元数据装载内存完成....");
        }
    }

    //增量处理方案平台数据
    public void updateDataByRecommendedPlanIds(List<Integer> recommendedPlanIdList, int type) {

        log.info(CLASS_LOG_PREFIX + "开始获取增量方案平台元数据....");
        //方案平台元数据
        List<DesignPlanPlatformRelPo> designPlanPlatformRelPoList = null;
        try {
            if (null != recommendedPlanIdList && 0 < recommendedPlanIdList.size()) {
                designPlanPlatformRelPoList = metaDataService.queryPlatformPlanByIdsMetaData(recommendedPlanIdList, type);
            }
        } catch (MetaDataException e) {
            log.error(CLASS_LOG_PREFIX + "获取方案平台元数据失败: MetaDataException:{}", e);
            throw new NullPointerException(CLASS_LOG_PREFIX + "获取方案平台元数据失败,List<DesignPlanPlatformRelPo> is null.MetaDataException:" + e);
        }
        log.info(CLASS_LOG_PREFIX + "获取方案平台元数据完成,总条数:{}", (null == designPlanPlatformRelPoList ? 0 : designPlanPlatformRelPoList.size()));

        //处理对象更新到缓存
        this.planPlatformCache(designPlanPlatformRelPoList, UserDefinedConstant.INCREMENT_UPDATE_TYPE);

    }

    private Map<String, String> planPlatformCache(List<DesignPlanPlatformRelPo> designPlanPlatformRelPoList, String type) {
        //临时对象
        Map<String, DesignPlanPlatformData> tempPlanPlatformRelMap = new HashMap<>();

        //转换Map
        if (null != designPlanPlatformRelPoList && 0 != designPlanPlatformRelPoList.size()) {
            for(DesignPlanPlatformRelPo designPlanPlatformRelPo : designPlanPlatformRelPoList) {
                if (null == designPlanPlatformRelPo) {
                    continue;
                }
                int planType = designPlanPlatformRelPo.getPlatformDesignPlanType();

                //检查对象/方案ID/平台ID
                if (0 < designPlanPlatformRelPo.getPlanId() && !StringUtils.isEmpty(designPlanPlatformRelPo.getPlatformCode())) {
                    //方案ID
                    int planId = designPlanPlatformRelPo.getPlanId();
                    //方案平台对象
                    DesignPlanPlatformData designPlanPlatformData = new DesignPlanPlatformData();

                    String mapkey = "";
                    //平台方案类型区分---公开、推荐方案(都存于一键方案表ID不会冲突)
                    if (PlatformConstant.PLANFORM_DESING_PLAN_TYPE_OPEN == planType || PlatformConstant.PLANFORM_DESING_PLAN_TYPE_ONEKEY == planType) {
                        mapkey = planId + "_" + PlatformConstant.PLANFORM_DESING_PLAN_TYPE_ONEKEY;
                    }
                    //平台方案类型区分---全屋方案
                    if (PlatformConstant.PLANFORM_DESING_PLAN_TYPE_FULL_HOUSE == planType) {
                        mapkey = planId + "_" + planType;
                    }
                    //更新数据
                    if (tempPlanPlatformRelMap.containsKey(mapkey)) {
                        designPlanPlatformData = tempPlanPlatformRelMap.get(mapkey);
                    }

                    switch (designPlanPlatformRelPo.getPlatformCode()) {
                        //2B-移动端
                        case PlatformConstant.PLATFORM_CODE_TOB_MOBILE:
                            designPlanPlatformData.setPlatformDesignPlanToBMobile(designPlanPlatformRelPo);
                            break;
                        //2B-PC
                        case PlatformConstant.PLATFORM_CODE_TOB_PC:
                            designPlanPlatformData.setPlatformDesignPlanToBPc(designPlanPlatformRelPo);
                            break;
                        //品牌2C-网站
                        case PlatformConstant.PLATFORM_CODE_TOC_SITE:
                            designPlanPlatformData.setPlatformDesignPlanToCSite(designPlanPlatformRelPo);
                            break;
                        //2C-移动端
                        case PlatformConstant.PLATFORM_CODE_TOC_MOBILE:
                            designPlanPlatformData.setPlatformDesignPlanToCMobile(designPlanPlatformRelPo);
                            break;
                        //三度-后台管理
                        case PlatformConstant.PLATFORM_CODE_SANDU_MANAGER:
                            designPlanPlatformData.setPlatformDesignPlanSanduManager(designPlanPlatformRelPo);
                            break;
                        //户型绘制工具
                        case PlatformConstant.PLATFORM_CODE_HOUSE_DRAW:
                            designPlanPlatformData.setPlatformDesignPlanHouseDraw(designPlanPlatformRelPo);
                            break;
                        //商家管理后台
                        case PlatformConstant.PLATFORM_CODE_MERCHANTS_MANAGER:
                            designPlanPlatformData.setPlatformDesignPlanMerchantsManager(designPlanPlatformRelPo);
                            break;
                        //测试
                        case PlatformConstant.PLATFORM_CODE_TEST:
                            designPlanPlatformData.setPlatformDesignPlanTest(designPlanPlatformRelPo);
                            break;
                        //U3D转换插件
                        case PlatformConstant.PLATFORM_CODE_U3D_PLUGIN:
                            designPlanPlatformData.setPlatformDesignPlanU3dPlugin(designPlanPlatformRelPo);
                            break;
                        //小程序
                        case PlatformConstant.PLATFORM_CODE_MINI_PROGRAM:
                            designPlanPlatformData.setPlatformDesignPlanMiniProgram(designPlanPlatformRelPo);
                            break;
                        //随选网
                        case PlatformConstant.PLATFORM_CODE_SELECT_DECORATION:
                            designPlanPlatformData.setPlatformDesignPlanSelectDecoration(designPlanPlatformRelPo);
                            break;
                        default:
                            log.warn(CLASS_LOG_PREFIX + "无法识别平台编码,designPlanPlatformRelPo:{}", designPlanPlatformRelPo.toString());
                    }
                    //装入Map
                    tempPlanPlatformRelMap.put(mapkey, designPlanPlatformData);
                }
            }
        }
        log.info(CLASS_LOG_PREFIX + "格式化方案平台元数据完成....");

        //数据转换
        Map<String, String> planPlatformRelJsonMap = new HashMap<>(tempPlanPlatformRelMap.size());
        tempPlanPlatformRelMap.forEach((k, v) -> planPlatformRelJsonMap.put(k , JsonUtil.toJson(v)));
        log.info(CLASS_LOG_PREFIX + "格式化方案平台Json元数据完成....");

        if (UserDefinedConstant.FULL_UPDATE_TYPE.equals(type)){
            //由于数据库下架方案平台是物理删除数据,全量更新先删后增
            redisService.del(RedisConstant.DESIGN_PLAN_PLATFORM_REL_MAP_DATA);
            redisService.del(RedisConstant.DESIGN_PLAN_PLATFORM_REL_INCREMENT_MAP_DATA);
            //装载缓存
            redisService.addMapCompatible(RedisConstant.DESIGN_PLAN_PLATFORM_REL_MAP_DATA, planPlatformRelJsonMap);

        }
        if (UserDefinedConstant.INCREMENT_UPDATE_TYPE.equals(type)) {
            //由于数据库下架方案平台是物理删除数据,先删后增
            //redisService.del(RedisConstant.DESIGN_PLAN_PLATFORM_REL_INCREMENT_MAP_DATA);
            //装载缓存
            redisService.addMapCompatible(RedisConstant.DESIGN_PLAN_PLATFORM_REL_INCREMENT_MAP_DATA, planPlatformRelJsonMap);

        }
        log.info(CLASS_LOG_PREFIX + "方案平台元数据装载缓存完成....");

        return planPlatformRelJsonMap;
    }


    /**
     * 根据方案ID获取方案平台数据
     *
     * @param planId 方案ID
     * @param planType 方案类型
     * @return
     */
    public DesignPlanPlatformData queryPlanPlatformByPlanId(Integer planId , Integer planType) {
        if (null == planId || 0 == planId) {
            return null;
        }

        String planPlatformRelStr = getMap(RedisConstant.DESIGN_PLAN_PLATFORM_REL_MAP_DATA, planId + "_" + planType);
        if (StringUtils.isEmpty(planPlatformRelStr)) {
            return null;
        }

        return JsonUtil.fromJson(planPlatformRelStr, DesignPlanPlatformData.class);
    }

    /**
     * 根据方案ID获取方案平台数据
     *
     * @param planId 方案ID
     * @param logicType 业务逻辑类型
     * @param planId 方案类型
     * @return
     */
    public DesignPlanPlatformData queryPlanPlatformByPlanId(Integer planId, String logicType, Integer planType) {
        if (null == planId || 0 == planId) {
            return null;
        }

        String planPlatformRelStr = "";
        if (UserDefinedConstant.FULL_UPDATE_TYPE.equals(logicType)){
            planPlatformRelStr = getMap(RedisConstant.DESIGN_PLAN_PLATFORM_REL_MAP_DATA, planId + "_" + planType);
        }

        if (UserDefinedConstant.INCREMENT_UPDATE_TYPE.equals(logicType)) {
            planPlatformRelStr = getMap(RedisConstant.DESIGN_PLAN_PLATFORM_REL_INCREMENT_MAP_DATA, planId + "_" + planType);
        }

        if (StringUtils.isEmpty(planPlatformRelStr)) {
            return null;
        }

        return JsonUtil.fromJson(planPlatformRelStr, DesignPlanPlatformData.class);
    }
}
