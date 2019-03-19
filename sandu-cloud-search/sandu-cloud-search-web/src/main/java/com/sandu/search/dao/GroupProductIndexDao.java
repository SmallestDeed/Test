package com.sandu.search.dao;

import com.sandu.search.entity.elasticsearch.po.GroupProductPO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 分类产品索引数据访问层
 *
 * @date 20171212
 * @auth zhengyoucai
 */
@Repository
public interface GroupProductIndexDao {

    /**
     * 获取产品数据
     *
     * @param start 起始数
     * @param limit 最大数
     * @return
     */
    List<GroupProductPO> queryGroupProductList(@Param("start") int start, @Param("limit") int limit);
}
