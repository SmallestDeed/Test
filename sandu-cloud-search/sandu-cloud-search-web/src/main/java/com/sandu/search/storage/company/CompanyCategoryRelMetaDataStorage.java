package com.sandu.search.storage.company;

import com.google.gson.reflect.TypeToken;
import com.sandu.search.common.constant.RedisConstant;
import com.sandu.search.common.tools.JsonUtil;
import com.sandu.search.service.redis.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 公司分类关联元数据存储
 *
 * @date 20171218
 * @auth pengxuangang
 */
@Component
public class CompanyCategoryRelMetaDataStorage {

    private final RedisService redisService;

    @Autowired
    public CompanyCategoryRelMetaDataStorage(RedisService redisService) {
        this.redisService = redisService;
    }

    /**
     * 根据公司ID查询公司分类ID列表
     *
     * @param companyId 公司ID
     * @return
     */
    public List<Integer> queryCategoryIdListByCompanyId(Integer companyId) {

        if (null == companyId || companyId == 0) {
            return null;
        }

        String categoryListStr = redisService.getMap(RedisConstant.COMPANY_CATEGORY_REL_DATA, companyId + "");
        if (StringUtils.isEmpty(categoryListStr)) {
            return null;
        }

        return JsonUtil.fromJson(categoryListStr, new TypeToken<List<Integer>>() {
        }.getType());
    }
}
