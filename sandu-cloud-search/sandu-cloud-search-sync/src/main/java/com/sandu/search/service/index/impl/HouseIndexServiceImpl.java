package com.sandu.search.service.index.impl;

import com.sandu.search.common.constant.IndexInfoQueryConfig;
import com.sandu.search.dao.HouseIndexDao;
import com.sandu.search.entity.elasticsearch.po.house.HousePo;
import com.sandu.search.exception.HouseIndexException;
import com.sandu.search.service.index.HouseIndexService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 户型索引服务
 *
 * @date 20180109
 * @auth pengxuangang
 */
@Slf4j
@Service("houseIndexService")
public class HouseIndexServiceImpl implements HouseIndexService {

    private final static String CLASS_LOG_PREFIX = "户型索引服务:";

    private final HouseIndexDao houseIndexDao;

    @Autowired
    public HouseIndexServiceImpl(HouseIndexDao houseIndexDao) {
        this.houseIndexDao = houseIndexDao;
    }

    @Override
    public List<HousePo> queryHousePoList(int start, int limit) throws HouseIndexException {

        //初始化数据条数
        if (0 == limit) {
            limit = IndexInfoQueryConfig.DEFAULT_QUERY_HOUSEPOINFO_LIMIT;
        }

        //查询户型信息
        log.info(CLASS_LOG_PREFIX + "正在查询户型信息第{}-{}条.", start, (start + limit));
        List<HousePo> housePoList;
        try {
            housePoList = houseIndexDao.queryHousePoList(start, limit);
        } catch (Exception e) {
            log.error(CLASS_LOG_PREFIX + "获取户型数据第{}-{}条失败,Exception:" + e, start, (start + limit));
            throw new HouseIndexException(CLASS_LOG_PREFIX + "获取户型数据第" + start + "-" + (start + limit) + "条失败,Exception:" + e);
        }
        log.info(CLASS_LOG_PREFIX + "查询户型信息完成,List<ProductPo>长度:", housePoList.size());

        return housePoList;
    }
}
