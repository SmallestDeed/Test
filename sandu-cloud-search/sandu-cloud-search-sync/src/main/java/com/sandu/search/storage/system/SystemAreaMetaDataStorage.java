package com.sandu.search.storage.system;

import com.sandu.search.common.constant.RedisConstant;
import com.sandu.search.common.tools.JsonUtil;
import com.sandu.search.entity.common.AreaZone;
import com.sandu.search.entity.elasticsearch.po.metadate.AreaPo;
import com.sandu.search.exception.MetaDataException;
import com.sandu.search.service.metadata.MetaDataService;
import com.sandu.search.service.redis.RedisService;
import com.sandu.search.storage.StorageComponent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 区域元数据存储
 *
 * @date 20180104
 * @auth pengxuangang
 */
@Slf4j
@Component
public class SystemAreaMetaDataStorage {

    private final static String CLASS_LOG_PREFIX = "区域元数据存储:";
    //默认缓存模式
    private static Integer STORAGE_MODE = StorageComponent.CACHE_MODE;

    private final RedisService redisService;
    private final MetaDataService metaDataService;

    @Autowired
    public SystemAreaMetaDataStorage(RedisService redisService, MetaDataService metaDataService) {
        this.redisService = redisService;
        this.metaDataService = metaDataService;
    }

    //区域元数据Map<areacode,AreaZone>
    private static Map<String, String> areaMap = null;

    //切换存储模式
    public void changeStorageMode(Integer storageMode) {
        //缓存模式
        if (StorageComponent.CACHE_MODE == storageMode) {
            //清空内存占用
            areaMap = null;
            //切换
            STORAGE_MODE = storageMode;
            //内存模式
        } else if(StorageComponent.MEMORY_MODE == storageMode) {
            //切换
            STORAGE_MODE = storageMode;
            //写入内存
            updateData();
            //areaMap = redisService.getMap(RedisConstant.AREA_DATA);
        }
        log.info(CLASS_LOG_PREFIX + "区域存储模式切换成功，当前存储:{}.", StorageComponent.CACHE_MODE == STORAGE_MODE ? "缓存" : "内存");
    }

    //获取Map数据方法兼容
    private String getMap(String mapName, String keyName) {
        //缓存模式
        if (StorageComponent.CACHE_MODE == STORAGE_MODE) {
            return redisService.getMap(mapName, keyName);
            //内存模式
        } else if (StorageComponent.MEMORY_MODE == STORAGE_MODE) {
            if (RedisConstant.AREA_DATA.equals(mapName)) {
                return areaMap.get(keyName);
            }
        }
        return null;
    }

    //更新数据
    public void updateData() {
        log.info(CLASS_LOG_PREFIX + "开始获取区域元数据....");
        //区域元数据
        List<AreaPo> areaPoList;
        try {
            areaPoList = metaDataService.queryAreaMetaData();
        } catch (MetaDataException e) {
            log.error(CLASS_LOG_PREFIX + "获取区域元数据失败: MetaDataException:{}", e);
            throw new NullPointerException(CLASS_LOG_PREFIX + "获取区域元数据失败,List<AreaPo> is null.MetaDataException:" + e);
        }
        log.info(CLASS_LOG_PREFIX + "获取区域元数据完成,总条数:{}", (null == areaPoList ? 0 : areaPoList.size()));

        //临时对象
        Map<String, AreaZone> tempAreaMap = new HashMap<>();

        //转换Map
        if (null != areaPoList && 0 != areaPoList.size()) {

            Map<String, AreaPo> areaCodeMap = new HashMap<>(areaPoList.size());
            //遍历Map切换数据格式
            areaPoList.forEach(areaPo -> areaCodeMap.put(areaPo.getAreaCode(), areaPo));
            //遍历Map解析数据
            for (Map.Entry<String, AreaPo> entry : areaCodeMap.entrySet()) {
                AreaPo areaDistrictPo = entry.getValue();
                //三级地区信息--这里可以写死
                if (null != areaDistrictPo && 3 == areaDistrictPo.getAreaLevelId()) {
                    AreaZone areaZone = new AreaZone();
                    //区
                    areaZone.setZoneDistrictId(Integer.parseInt(areaDistrictPo.getAreaCode()));
                    areaZone.setZoneDistrictName(areaDistrictPo.getAreaName());
                    //市
                    String zoneCityId = areaDistrictPo.getAreaParentId();
                    AreaPo areaCityPo = areaCodeMap.get(zoneCityId);
                    areaZone.setZoneCityId(Integer.parseInt(zoneCityId));
                    areaZone.setZoneCityName(areaCityPo.getAreaName());
                    //省
                    String zoneProvinceId = areaCityPo.getAreaParentId();
                    AreaPo areaProvincePo = areaCodeMap.get(zoneProvinceId);
                    if (null == areaProvincePo) {
                        log.warn(CLASS_LOG_PREFIX + "解析省区域信息失败，未找到areacode对应区域。areacode:{}", zoneProvinceId);
                    } else {
                        areaZone.setZoneProvinceId(Integer.parseInt(zoneProvinceId));
                        areaZone.setZoneProvinceName(areaProvincePo.getAreaName());
                    }
                    //邮编
                    areaZone.setZoneZipCode(areaDistrictPo.getAreaZipCode());
                    //区域长ID
                    areaZone.setZoneLongId(Arrays.asList(areaZone.getZoneProvinceId() + "",
                            areaZone.getZoneCityId() + "",
                            areaZone.getZoneDistrictId() + ""));
                    //区域长名
                    areaZone.setZoneLongName(Arrays.asList(areaZone.getZoneProvinceName(),
                            areaZone.getZoneCityName(),
                            areaZone.getZoneDistrictName()));

                    tempAreaMap.put(areaDistrictPo.getAreaCode(), areaZone);
                }
            }
        }
        log.info(CLASS_LOG_PREFIX + "格式化区域元数据完成....");

        //数据转换
        Map<String, String> areaJsonMap = new HashMap<>(tempAreaMap.size());
        tempAreaMap.forEach((k, v) -> areaJsonMap.put(k, JsonUtil.toJson(v)));
        log.info(CLASS_LOG_PREFIX + "格式化区域Json元数据完成....");

        //装载缓存
        redisService.addMapCompatible(RedisConstant.AREA_DATA, areaJsonMap);
        log.info(CLASS_LOG_PREFIX + "区域元数据装载缓存完成....");

        //内存模式
        if (StorageComponent.MEMORY_MODE == STORAGE_MODE) {
            areaMap = areaJsonMap;
            log.info(CLASS_LOG_PREFIX + "区域元数据装载内存完成....");
        }
    }

    /**
     * 根据区域编码查询区域对象
     *
     * @param areaCode 区域编码
     * @return
     */
    public AreaZone getAreaZoneByAreaCode(String areaCode) {
        if (StringUtils.isEmpty(areaCode)) {
            return null;
        }

        String areaZoneStr = getMap(RedisConstant.AREA_DATA, areaCode);
        if (StringUtils.isEmpty(areaZoneStr)) {
            return null;
        }

        return JsonUtil.fromJson(areaZoneStr, AreaZone.class);
    }
}
