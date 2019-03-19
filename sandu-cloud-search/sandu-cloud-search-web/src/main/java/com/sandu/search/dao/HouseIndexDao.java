package com.sandu.search.dao;

import com.sandu.search.entity.elasticsearch.po.house.HousePo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 户型数据访问层
 *
 * @date 20180109
 * @auth pengxuangang
 */
@Repository
public interface HouseIndexDao {

    /**
     * 获取户型数据
     *
     * @param start 起始数
     * @param limit 最大数
     * @return
     */
    List<HousePo> queryHousePoList(@Param("start") int start, @Param("limit") int limit);
}
