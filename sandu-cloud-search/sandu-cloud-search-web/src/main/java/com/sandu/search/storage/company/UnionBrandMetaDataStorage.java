package com.sandu.search.storage.company;

import com.google.gson.reflect.TypeToken;
import com.sandu.search.common.constant.RedisConstant;
import com.sandu.search.common.tools.JsonUtil;
import com.sandu.search.service.redis.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 联盟品牌元数据存储
 *
 * @date 20171218
 * @auth pengxuangang
 */
@Component
public class UnionBrandMetaDataStorage {

    private final RedisService redisService;

    @Autowired
    public UnionBrandMetaDataStorage(RedisService redisService) {
        this.redisService = redisService;
    }

    /**
     * 根据品牌ID列表查询包含的异业联盟品牌
     *
     * @param brandIdList   品牌ID列表
     * @return
     */
    public List<Integer> queryUnionBrandByBrandIdList(List<Integer> brandIdList) {

        if (null == brandIdList || 0 >= brandIdList.size()) {
            return null;
        }

        //联盟列表
        List<Integer> unionList = new ArrayList<>();
        //联盟品牌列表
        List<Integer> unionBrandList = new ArrayList<>();

        //查询出每个品牌的联盟
        for (Integer brandId : brandIdList) {
            if (null != brandId && 0 < brandId) {
                List<Integer> unionIdList = queryUnionIdListByBrandId(brandId);
                if (null != unionIdList && 0 < unionIdList.size()) {
                    unionList.addAll(unionIdList);
                }
            }
        }

        if (0 < unionList.size()) {
            //多个品牌可能对应多个联盟
            unionList = unionList.stream().distinct().collect(Collectors.toList());
            unionList.forEach(unionId -> {
                List<Integer> nowBrandIdList = queryBrandIdListByUnionId(unionId);
                if (null != nowBrandIdList && 0 < nowBrandIdList.size()) {
                    unionBrandList.addAll(nowBrandIdList);
                }
            });
        }

        if (0 < unionBrandList.size()) {
            //多个联盟对应多个品牌
            return unionBrandList.stream().distinct().collect(Collectors.toList());
        }

        return null;
    }

    /**
     * 获取联盟ID列表
     *
     * @date 2018/6/14
     * @auth pengxuangang
     * @mail xuangangpeng@gmail.com
     */
    private List<Integer> queryUnionIdListByBrandId(Integer brandId) {
        if (null == brandId || 0 >= brandId) {
            return null;
        }

        String unionIdListStr = redisService.getMap(RedisConstant.BRAND_UNION_DATA, brandId + "");

        if (StringUtils.isEmpty(unionIdListStr)) {
            return null;
        }

        return JsonUtil.fromJson(unionIdListStr, new TypeToken<List<Integer>>() {}.getType());
    }

    /**
     * 获取品牌ID列表
     *
     * @date 2018/6/14
     * @auth pengxuangang
     * @mail xuangangpeng@gmail.com
     */
    private List<Integer> queryBrandIdListByUnionId(Integer unionId) {
        if (null == unionId || 0 >= unionId) {
            return null;
        }

        String brandIdListStr = redisService.getMap(RedisConstant.UNION_BRAND_DATA, unionId + "");

        if (StringUtils.isEmpty(brandIdListStr)) {
            return null;
        }

        return JsonUtil.fromJson(brandIdListStr, new TypeToken<List<Integer>>() {}.getType());
    }
}
