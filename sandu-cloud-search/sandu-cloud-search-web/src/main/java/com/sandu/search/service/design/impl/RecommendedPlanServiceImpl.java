package com.sandu.search.service.design.impl;

import com.sandu.gson.GsonUtil;
import com.sandu.search.common.tools.MessageUtil;
import com.sandu.search.common.util.ResponseEnvelope;
import com.sandu.search.common.util.Utils;
import com.sandu.search.entity.response.universal.UniversalSearchResultResponse;
import com.sandu.search.service.design.RecommendedPlanService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * @desc 推荐方案实现
 *
 * @auth xiaoxc
 * @data 2018/11/10
 */
@Slf4j
@Service("recommendedPlanService")
public class RecommendedPlanServiceImpl implements RecommendedPlanService {

    private final static String CLASS_LOG_PREFIX = "推荐方案实现服务:";

    @Value("${app.wxapi.url}")
    private String wxapiUrl;

    @Override
    public MessageUtil syncRecommendedPlanFavorite() {
        try {
            String url = wxapiUrl+"/v1/miniprogram/designplanfavorite/syncDeginplanFavorite";
            String jsonResult = Utils.doGetMethod(url);
            if (StringUtils.isBlank(jsonResult)) {
                log.error(CLASS_LOG_PREFIX + "远程调用同步方案缓存点赞收藏结果jsonResult为空" + url);
                return new MessageUtil(false, "远程调用同步方案缓存点赞收藏结果jsonResult为空！");
            }
            ResponseEnvelope response = GsonUtil.fromJson(jsonResult, ResponseEnvelope.class);
            if (!response.isStatus()) {
                log.error(CLASS_LOG_PREFIX + "远程调用同步方案缓存点赞收藏结果失败" + jsonResult);
                return new MessageUtil(false, "远程调用同步方案缓存点赞收藏结果失败！");
            } else {
                log.info("远程调用同步方案缓存点赞收藏成功");
                return new MessageUtil(true);
            }
        }catch (Exception e){
            log.error("远程调用同步方案缓存点赞收藏结果异常" + e);
            return new MessageUtil(false, "远程调用同步方案缓存点赞收藏异常！");
        }
    }

}
