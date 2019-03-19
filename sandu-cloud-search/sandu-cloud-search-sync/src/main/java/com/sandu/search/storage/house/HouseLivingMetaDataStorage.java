package com.sandu.search.storage.house;

import com.sandu.search.entity.elasticsearch.po.house.HouseLivingPo;
import com.sandu.search.exception.MetaDataException;
import com.sandu.search.service.metadata.MetaDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 户型小区元数据存储
 *
 * @date 20171220
 * @auth pengxuangang
 */
@Slf4j
@Component
public class HouseLivingMetaDataStorage {

    private final static String CLASS_LOG_PREFIX = "户型小区元数据存储:";
    //户型小区资源元数据Map<livingId, HouseLivingPo>
    private static Map<Integer, HouseLivingPo> houseLivingMap = new HashMap<>();
    //元数据服务
    private final MetaDataService metaDataService;

    @Autowired
    public HouseLivingMetaDataStorage(MetaDataService metaDataService) {
        this.metaDataService = metaDataService;
    }

    /**
     * 更新数据
     * @param isEnforceLoad 是否强制更新
     */
    public void updateData(boolean isEnforceLoad) {

        //若无强制更新则更新前判断是否已有数据
        if (!isEnforceLoad) {
            if (null != houseLivingMap && 0 < houseLivingMap.size()) {
                return;
            }
        }


        List<HouseLivingPo> houseLivingPoList;
        try {
            //获取数据
            log.info(CLASS_LOG_PREFIX + "开始获取户型小区元数据元数据....");
            houseLivingPoList = metaDataService.queryHouseLivingMetaData();
            log.info(CLASS_LOG_PREFIX + "获取户型小区元数据完成,总条数:{}", (null == houseLivingPoList ? 0 : houseLivingPoList.size()));
        } catch (MetaDataException e) {
            log.error(CLASS_LOG_PREFIX + "获取户型小区元数据失败: MetaDataException:{}", e);
            throw new NullPointerException(CLASS_LOG_PREFIX + "获取户型小区元数据失败,List<HouseLivingPo> is null.MetaDataException:" + e);
        }

        //临时对象
        Map<Integer, HouseLivingPo> tempHouseLivingMap = new HashMap<>();

        //转换为Map元数据
        if (null != houseLivingPoList && 0 != houseLivingPoList.size()) {
            houseLivingPoList.forEach(houseLivingPo -> tempHouseLivingMap.put(houseLivingPo.getLivingId(), houseLivingPo));
        }

        //装回对象
        houseLivingMap = null;
        houseLivingMap = tempHouseLivingMap;
    }

    /**
     * 根据小区ID获取小区对象
     *
     * @param livingId 小区ID
     * @return
     */
    public HouseLivingPo getHouseLivingByLivingId(Integer livingId) {
        if (null == houseLivingMap || 0 >= houseLivingMap.size()) {
            //更新数据
            updateData(false);
        }
        HouseLivingPo houseLivingPo = null;
        if (null != livingId && 0 != livingId && houseLivingMap.containsKey(livingId)) {
            houseLivingPo = houseLivingMap.get(livingId);
        }
        return houseLivingPo;
    }
}
