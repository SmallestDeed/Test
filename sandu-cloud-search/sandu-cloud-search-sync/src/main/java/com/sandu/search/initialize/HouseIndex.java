package com.sandu.search.initialize;

import com.sandu.search.common.constant.IndexInfoQueryConfig;
import com.sandu.search.common.tools.JsonUtil;
import com.sandu.search.config.ElasticSearchConfig;
import com.sandu.search.entity.common.AreaZone;
import com.sandu.search.entity.elasticsearch.constant.IndexConstant;
import com.sandu.search.entity.elasticsearch.constant.TypeConstant;
import com.sandu.search.entity.elasticsearch.dto.IndexRequestDTO;
import com.sandu.search.entity.elasticsearch.index.HouseIndexMappingData;
import com.sandu.search.entity.elasticsearch.po.house.HouseLivingPo;
import com.sandu.search.entity.elasticsearch.po.house.HousePo;
import com.sandu.search.entity.elasticsearch.response.BulkStatistics;
import com.sandu.search.exception.ElasticSearchException;
import com.sandu.search.exception.HouseIndexException;
import com.sandu.search.service.elasticsearch.ElasticSearchService;
import com.sandu.search.service.index.HouseIndexService;
import com.sandu.search.storage.house.HouseLivingMetaDataStorage;
import com.sandu.search.storage.system.SystemAreaMetaDataStorage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 搜索引擎户型索引初始化
 *
 * @date 20171212
 * @auth pengxuangang
 */
@Slf4j
@Component
public class HouseIndex {

    private final static String CLASS_LOG_PREFIX = "初始化搜索引擎户型索引:";
    private final ElasticSearchConfig elasticSearchConfig;
    private final HouseIndexService houseIndexService;
    private final ElasticSearchService elasticSearchService;
    private final SystemAreaMetaDataStorage systemAreaMetaDataStorage;
    private final HouseLivingMetaDataStorage houseLivingMetaDataStorage;

    @Autowired
    public HouseIndex(ElasticSearchConfig elasticSearchConfig, HouseIndexService houseIndexService, ElasticSearchService elasticSearchService, SystemAreaMetaDataStorage systemAreaMetaDataStorage, HouseLivingMetaDataStorage houseLivingMetaDataStorage) {
        this.elasticSearchConfig = elasticSearchConfig;
        this.houseIndexService = houseIndexService;
        this.elasticSearchService = elasticSearchService;
        this.systemAreaMetaDataStorage = systemAreaMetaDataStorage;
        this.houseLivingMetaDataStorage = houseLivingMetaDataStorage;
    }


    /**
     * 初始化搜索引擎户型信息
     *
     * @date 20171212
     * @auth pengxuangang
     */
    //@PostConstruct
    public void initElasticSearchHouseIndex() {

        //开始时间
        long startTime = System.currentTimeMillis();

        boolean initHouseIndex = true;

        //若配置为空，则为定时任务加载，默认初始化
        if (null != elasticSearchConfig) {
            initHouseIndex = elasticSearchConfig.isInitHouseIndex();
        }

        log.info(CLASS_LOG_PREFIX + "是否需要户型索引初始化:", initHouseIndex);

        log.info(CLASS_LOG_PREFIX + "开始索引户型数据........");

        //数据查询初始位
        int start = 0;
        //每次数据量
        int limit = IndexInfoQueryConfig.DEFAULT_QUERY_HOUSEPOINFO_LIMIT;

        //是否继续处理
        boolean isContinueHandler = true;
        //总数据量
        int totalProductCount = 0;
        //总索引量
        int totalIndexCount = 0;
        //异常数据
        int totalExceptionCount = 0;

        while (isContinueHandler) {

            List<HousePo> housePoList;
            /********************************** 查询户型信息 *********************************/
            try {
                housePoList = houseIndexService.queryHousePoList(start, limit);
            } catch (HouseIndexException e) {
                log.error(CLASS_LOG_PREFIX + "查询户型信息失败:HouseIndexException:{}", e);
                return;
            }
            //无数据中断操作
            if (null == housePoList || 0 == housePoList.size()) {
                log.info(CLASS_LOG_PREFIX + "查询户型数据为空：start:{},limit:{}.", start, limit);
                return;
            }
            //数据不足指定数据量表示已查询出最后一条数据,下轮终止循环
            if (housePoList.size() < IndexInfoQueryConfig.DEFAULT_QUERY_HOUSEPOINFO_LIMIT) {
                isContinueHandler = false;
            }

            //批量提交数据对象
            List<Object> bulkIndexList = new ArrayList<>(IndexInfoQueryConfig.DEFAULT_QUERY_PRODUCTPOINFO_LIMIT);

            int failCount = 0;
            /********************************** 处理户型信息数据 *********************************/
            for (HousePo housePo : housePoList) {
                //定义索引数据对象
                HouseIndexMappingData houseIndexMappingData = new HouseIndexMappingData();

                /*************** Field1:户型基本数据 *******************/
                houseIndexMappingData.setHouseId(housePo.getHouseId());
                houseIndexMappingData.setSystemCode(housePo.getSystemCode());
                houseIndexMappingData.setHouseCode(housePo.getHouseCode());
                houseIndexMappingData.setHouseDoorCode(housePo.getHouseDoorCode());
                houseIndexMappingData.setHouseName(housePo.getHouseName());
                houseIndexMappingData.setHousePicId(housePo.getHousePicId());
                houseIndexMappingData.setHouseLivingId(housePo.getHouseLivingId());
                houseIndexMappingData.setHouseTotalArea(housePo.getHouseTotalArea());
                houseIndexMappingData.setHouseCommonCode(housePo.getHouseCommonCode());
                houseIndexMappingData.setHouseAreaLongCode(housePo.getHouseAreaLongCode());
                houseIndexMappingData.setHouseStatus(housePo.getHouseStatus());

                /*************** Field2:户型小区数据 *******************/
                //户型小区数据
                HouseLivingPo houseLivingPo = houseLivingMetaDataStorage.getHouseLivingByLivingId(houseIndexMappingData.getHouseLivingId());
                if (0 != houseIndexMappingData.getHouseLivingId() && null != houseLivingPo) {
                    houseIndexMappingData.setLivingType(houseLivingPo.getLivingType());
                    houseIndexMappingData.setLivingCode(houseLivingPo.getLivingCode());
                    houseIndexMappingData.setLivingName(houseLivingPo.getLivingName());
                    houseIndexMappingData.setLivingAddress(houseLivingPo.getLivingAddress());
                    houseIndexMappingData.setLivingDesc(houseLivingPo.getLivingDesc());
                    houseIndexMappingData.setLivingAreaId(houseLivingPo.getLivingAreaId());
                    houseIndexMappingData.setLivingBuildArea(houseLivingPo.getLivingBuildArea());
                    houseIndexMappingData.setLivingCoverArea(houseLivingPo.getLivingCoverArea());
                    houseIndexMappingData.setLivingAreaLongCode(houseLivingPo.getLivingAreaLongCode());
                }

                /*************** Field3:地区数据 *******************/
                AreaZone areaZone = systemAreaMetaDataStorage.getAreaZoneByAreaCode(houseIndexMappingData.getLivingAreaId());
                if (null != areaZone) {
                    houseIndexMappingData.setZoneProvinceId(areaZone.getZoneProvinceId());
                    houseIndexMappingData.setZoneProvinceName(areaZone.getZoneProvinceName());
                    houseIndexMappingData.setZoneCityId(areaZone.getZoneCityId());
                    houseIndexMappingData.setZoneCityName(areaZone.getZoneCityName());
                    houseIndexMappingData.setZoneDistrictId(areaZone.getZoneDistrictId());
                    houseIndexMappingData.setZoneDistrictName(areaZone.getZoneDistrictName());
                    houseIndexMappingData.setZoneLongId(areaZone.getZoneLongId());
                    houseIndexMappingData.setZoneLongName(areaZone.getZoneLongName());
                }

                /*
                * 创建索引对象
                * */
                IndexRequestDTO indexRequestDTO = new IndexRequestDTO(
                        IndexConstant.INDEX_HOUSE,
                        TypeConstant.TYPE_HUSE,
                        houseIndexMappingData.getHouseId() + "",
                        JsonUtil.toJson(houseIndexMappingData)
                );

                //加入批量对象
                bulkIndexList.add(indexRequestDTO);
            }

            //若不需要初始化则仅加载一次，只初始化数据至内存中即可
            if (!initHouseIndex) {
                log.info(CLASS_LOG_PREFIX + "已初始化元数据至内存..........");
                isContinueHandler = false;
                continue;
            }

            //索引数据
            BulkStatistics bulkStatistics;
            try {
                bulkStatistics = elasticSearchService.bulk(bulkIndexList, null);
            } catch (ElasticSearchException e) {
                log.error(CLASS_LOG_PREFIX + "索引户型数据异常:ElasticSearchException:{}", e);
                continue;
            }
            log.info(CLASS_LOG_PREFIX + "索引户型数据成功:成功索引数:{},无效索引数:{},BulkStatistics:{}", new String[]{
                    bulkIndexList.size() + "",
                    failCount + "",
                    bulkStatistics.toString()
            });

            //递增start下标
            start = start + limit;

            //累加数据量
            totalProductCount += housePoList.size();
            totalIndexCount += bulkIndexList.size();
            totalExceptionCount += failCount;
        }

        log.info(CLASS_LOG_PREFIX + "索引所有户型数据完成!!!户型数据量:{}, 索引数据量:{},失败数:{},共耗时:{}ms", new String[]{
                totalProductCount + "",
                totalIndexCount + "",
                totalExceptionCount + "",
                (System.currentTimeMillis() - startTime) + ""
        });
    }
}
