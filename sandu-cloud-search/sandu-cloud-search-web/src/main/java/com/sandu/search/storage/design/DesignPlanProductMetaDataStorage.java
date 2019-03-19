package com.sandu.search.storage.design;

import com.sandu.search.common.constant.DesignPlanType;
import com.sandu.search.common.constant.RedisConstant;
import com.sandu.search.common.tools.JsonUtil;
import com.sandu.search.entity.designplan.po.DesignPlanProductPo;
import com.sandu.search.entity.elasticsearch.index.ProductIndexMappingData;
import com.sandu.search.entity.product.vo.BaiMoProductSearchConditionsVO;
import com.sandu.search.exception.MetaDataException;
import com.sandu.search.service.metadata.MetaDataService;
import com.sandu.search.service.redis.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * 设计方案产品元数据存储
 *
 * @date 20180131
 * @auth pengxuangang
 */
@Slf4j
@Component
public class DesignPlanProductMetaDataStorage {

    private final static String CLASS_LOG_PREFIX = "设计方案产品元数据存储:";

    private final RedisService redisService;
    private final MetaDataService metaDataService;

    @Autowired
    public DesignPlanProductMetaDataStorage(RedisService redisService, MetaDataService metaDataService) {
        this.redisService = redisService;
        this.metaDataService = metaDataService;
    }

    /**
     * 根据方案产品ID获取白膜产品ID
     * 
     * update by huangsongbo 2018.9.11 
     * 改造改方法,所以rename: getWhiteMembraneProductIdById ->getWhiteMembraneProductIdById_old
     * 新方法见 getWhiteMembraneProductIdById
     *
     * @param designPlanProductId 设计方案产品ID
     * @param designPlanType      设计方案类型
     * @return
     */
    public int getWhiteMembraneProductIdById_old(int designPlanProductId, int designPlanType) {

        if (0 == designPlanProductId || 0 == designPlanType) {
            return 0;
        }

        //白膜产品ID
        int whiteMembraneProductId = 0;
        //设计方案类型
        switch (designPlanType) {
            //草稿
            case DesignPlanType.TEMP_DESIGN_PLAN_TYPE:
                String tempWhiteMembraneProductIdStr = redisService.getMap(RedisConstant.TEMP_DESIGNPLAN_PRODUCT_DATA, designPlanProductId + "");
                //查询内存对象
                if (!StringUtils.isEmpty(tempWhiteMembraneProductIdStr)) {
                    whiteMembraneProductId = Integer.parseInt(tempWhiteMembraneProductIdStr);
                } else {
                    //查询数据库
                    DesignPlanProductPo designPlanProductPo;
                    try {
                        designPlanProductPo = metaDataService.getTempDesignPlanProductMetaDataById(designPlanProductId);
                    } catch (MetaDataException e) {
                        log.error(CLASS_LOG_PREFIX + "数据库查询草稿设计方案产品白膜失败! MetaDataException:{}", e);
                        return 0;
                    }

                    if (null == designPlanProductPo) {
                        log.info(CLASS_LOG_PREFIX + "数据库查询草稿设计方案产品白膜未找到....designPlanProductId:{}.", designPlanProductId);
                        return 0;
                    }

                    //装回内存对象
                    redisService.addMap(RedisConstant.TEMP_DESIGNPLAN_PRODUCT_DATA, designPlanProductPo.getId() + "", designPlanProductPo.getInitProductId() + "");
                    log.info(CLASS_LOG_PREFIX + "数据库查询草稿设计方案产品白膜成功!DesignPlanProductPo:{}.", designPlanProductPo.toString());

                    whiteMembraneProductId = designPlanProductPo.getInitProductId();
                }
                break;
            //推荐
            case DesignPlanType.RECOMMEND_DESIGN_PLAN_TYPE:
                String recommendWhiteMembraneProductIdStr = redisService.getMap(RedisConstant.RECOMMEND_DESIGNPLAN_PRODUCT_DATA, designPlanProductId + "");
                //查询内存对象
                if (!StringUtils.isEmpty(recommendWhiteMembraneProductIdStr)) {
                    whiteMembraneProductId = Integer.parseInt(recommendWhiteMembraneProductIdStr);
                } else {
                    //查询数据库
                    DesignPlanProductPo designPlanProductPo;
                    try {
                        designPlanProductPo = metaDataService.getRecommendDesignPlanProductMetaDataById(designPlanProductId);
                    } catch (MetaDataException e) {
                        log.error(CLASS_LOG_PREFIX + "数据库查询推荐设计方案产品白膜失败! MetaDataException:{}", e);
                        return 0;
                    }

                    if (null == designPlanProductPo) {
                        log.info(CLASS_LOG_PREFIX + "数据库查询推荐设计方案产品白膜未找到....designPlanProductId:{}.", designPlanProductId);
                        return 0;
                    }

                    //装回内存对象
                    redisService.addMap(RedisConstant.RECOMMEND_DESIGNPLAN_PRODUCT_DATA, designPlanProductPo.getId() + "", designPlanProductPo.getInitProductId() + "");
                    log.info(CLASS_LOG_PREFIX + "数据库查询推荐设计方案产品白膜成功!DesignPlanProductPo:{}.", designPlanProductPo.toString());

                    whiteMembraneProductId = designPlanProductPo.getInitProductId();
                }
                break;
            //自定义
            case DesignPlanType.DIY_DESIGN_PLAN_TYPE:
                String diyWhiteMembraneProductIdStr = redisService.getMap(RedisConstant.DIY_DESIGNPLAN_PRODUCT_DATA, designPlanProductId + "");
                //查询内存对象
                if (!StringUtils.isEmpty(diyWhiteMembraneProductIdStr)) {
                    whiteMembraneProductId = Integer.parseInt(diyWhiteMembraneProductIdStr);
                } else {
                    //查询数据库
                    DesignPlanProductPo designPlanProductPo;
                    try {
                        designPlanProductPo = metaDataService.getDiyDesignPlanProductMetaDataById(designPlanProductId);
                    } catch (MetaDataException e) {
                        log.error(CLASS_LOG_PREFIX + "数据库查询自定义设计方案产品白膜失败! MetaDataException:{}", e);
                        return 0;
                    }

                    if (null == designPlanProductPo) {
                        log.info(CLASS_LOG_PREFIX + "数据库查询自定义设计方案产品白膜未找到....designPlanProductId:{}.", designPlanProductId);
                        return 0;
                    }

                    //装回内存对象
                    redisService.addMap(RedisConstant.DIY_DESIGNPLAN_PRODUCT_DATA, designPlanProductPo.getId() + "", designPlanProductPo.getInitProductId() + "");
                    log.info(CLASS_LOG_PREFIX + "数据库查询设计方案产品白膜成功!DesignPlanProductPo:{}.", designPlanProductPo.toString());

                    whiteMembraneProductId = designPlanProductPo.getInitProductId();
                }
                break;
            default:
                log.warn(CLASS_LOG_PREFIX + "根据方案产品ID获取白膜产品ID失败，设计方案类型异常,DesignPlanType:{}.", designPlanType);
        }
        return whiteMembraneProductId;
    }

    public int getWhiteMembraneProductIdById(int designPlanProductId, int designPlanType) {

        if (0 == designPlanProductId || 0 == designPlanType) {
            return 0;
        }

        //白膜产品ID
        int whiteMembraneProductId = 0;
        
        DesignPlanProductPo designPlanProductPo;
        
        //设计方案类型
        switch (designPlanType) {
            //草稿
            case DesignPlanType.TEMP_DESIGN_PLAN_TYPE:
                try {
                    designPlanProductPo = metaDataService.getTempDesignPlanProductMetaDataById(designPlanProductId);
                } catch (MetaDataException e) {
                    log.error(CLASS_LOG_PREFIX + "数据库查询草稿设计方案产品白膜失败! MetaDataException:{}", e);
                    return 0;
                }

                if (null == designPlanProductPo) {
                    log.info(CLASS_LOG_PREFIX + "数据库查询草稿设计方案产品白膜未找到....designPlanProductId:{}.", designPlanProductId);
                    return 0;
                }

                log.info(CLASS_LOG_PREFIX + "数据库查询草稿设计方案产品白膜成功!DesignPlanProductPo:{}.", designPlanProductPo.toString());

                whiteMembraneProductId = designPlanProductPo.getInitProductId();
                break;
                
            //推荐
            case DesignPlanType.RECOMMEND_DESIGN_PLAN_TYPE:
                try {
                    designPlanProductPo = metaDataService.getRecommendDesignPlanProductMetaDataById(designPlanProductId);
                } catch (MetaDataException e) {
                    log.error(CLASS_LOG_PREFIX + "数据库查询推荐设计方案产品白膜失败! MetaDataException:{}", e);
                    return 0;
                }

                if (null == designPlanProductPo) {
                    log.info(CLASS_LOG_PREFIX + "数据库查询推荐设计方案产品白膜未找到....designPlanProductId:{}.", designPlanProductId);
                    return 0;
                }

                log.info(CLASS_LOG_PREFIX + "数据库查询推荐设计方案产品白膜成功!DesignPlanProductPo:{}.", designPlanProductPo.toString());

                whiteMembraneProductId = designPlanProductPo.getInitProductId();
                break;
                
            //自定义
            case DesignPlanType.DIY_DESIGN_PLAN_TYPE:

                //查询数据库
                try {
                    designPlanProductPo = metaDataService.getDiyDesignPlanProductMetaDataById(designPlanProductId);
                } catch (MetaDataException e) {
                    log.error(CLASS_LOG_PREFIX + "数据库查询自定义设计方案产品白膜失败! MetaDataException:{}", e);
                    return 0;
                }

                if (null == designPlanProductPo) {
                    log.info(CLASS_LOG_PREFIX + "数据库查询自定义设计方案产品白膜未找到....designPlanProductId:{}.", designPlanProductId);
                    return 0;
                }

                //装回内存对象
                redisService.addMap(RedisConstant.DIY_DESIGNPLAN_PRODUCT_DATA, designPlanProductPo.getId() + "", designPlanProductPo.getInitProductId() + "");
                log.info(CLASS_LOG_PREFIX + "数据库查询设计方案产品白膜成功!DesignPlanProductPo:{}.", designPlanProductPo.toString());

                whiteMembraneProductId = designPlanProductPo.getInitProductId();
                break;
            default:
                log.warn(CLASS_LOG_PREFIX + "根据方案产品ID获取白膜产品ID失败，设计方案类型异常,DesignPlanType:{}.", designPlanType);
        }
        return whiteMembraneProductId;
    }
    
    /**
     * add by huangsongbo 2018.10.11
     * 根据designPlanType来识别查哪张表(样板(design_templet_product)/草图(design_plan_product)/推荐(design_plan_recommended_product))
     * designPlanProductId即为对应表的id
     * 
     * @param designPlanProductId
     * @param designPlanType
     * @return
     */
    public DesignPlanProductPo getDesignPlanProductPoById(int designPlanProductId, int designPlanType) {

        if (0 == designPlanProductId || 0 == designPlanType) {
            return null;
        }

        //白膜产品ID
        
        DesignPlanProductPo designPlanProductPo = null;
        
        //设计方案类型
        switch (designPlanType) {
            //草稿
            case DesignPlanType.TEMP_DESIGN_PLAN_TYPE:
                try {
                    designPlanProductPo = metaDataService.getTempDesignPlanProductMetaDataById(designPlanProductId);
                } catch (MetaDataException e) {
                    log.error(CLASS_LOG_PREFIX + "数据库查询草稿设计方案产品白膜失败! MetaDataException:{}", e);
                    return null;
                }

                if (null == designPlanProductPo) {
                    log.info(CLASS_LOG_PREFIX + "数据库查询草稿设计方案产品白膜未找到....designPlanProductId:{}.", designPlanProductId);
                    return null;
                }

                log.info(CLASS_LOG_PREFIX + "数据库查询草稿设计方案产品白膜成功!DesignPlanProductPo:{}.", designPlanProductPo.toString());

                break;
                
            //推荐
            case DesignPlanType.RECOMMEND_DESIGN_PLAN_TYPE:
                try {
                    designPlanProductPo = metaDataService.getRecommendDesignPlanProductMetaDataById(designPlanProductId);
                } catch (MetaDataException e) {
                    log.error(CLASS_LOG_PREFIX + "数据库查询推荐设计方案产品白膜失败! MetaDataException:{}", e);
                    return null;
                }

                if (null == designPlanProductPo) {
                    log.info(CLASS_LOG_PREFIX + "数据库查询推荐设计方案产品白膜未找到....designPlanProductId:{}.", designPlanProductId);
                    return null;
                }

                log.info(CLASS_LOG_PREFIX + "数据库查询推荐设计方案产品白膜成功!DesignPlanProductPo:{}.", designPlanProductPo.toString());

                break;
                
            //自定义
            case DesignPlanType.DIY_DESIGN_PLAN_TYPE:

                //查询数据库
                try {
                    designPlanProductPo = metaDataService.getDiyDesignPlanProductMetaDataById(designPlanProductId);
                } catch (MetaDataException e) {
                    log.error(CLASS_LOG_PREFIX + "数据库查询自定义设计方案产品白膜失败! MetaDataException:{}", e);
                    return null;
                }

                if (null == designPlanProductPo) {
                    log.info(CLASS_LOG_PREFIX + "数据库查询自定义设计方案产品白膜未找到....designPlanProductId:{}.", designPlanProductId);
                    return null;
                }

                //装回内存对象
                redisService.addMap(RedisConstant.DIY_DESIGNPLAN_PRODUCT_DATA, designPlanProductPo.getId() + "", designPlanProductPo.getInitProductId() + "");
                log.info(CLASS_LOG_PREFIX + "数据库查询设计方案产品白膜成功!DesignPlanProductPo:{}.", designPlanProductPo.toString());

                break;
            default:
                log.warn(CLASS_LOG_PREFIX + "根据方案产品ID获取白膜产品ID失败，设计方案类型异常,DesignPlanType:{}.", designPlanType);
        }
        return designPlanProductPo;
    }
    
    public ProductIndexMappingData getProductInfoById(Integer id) {
        BaiMoProductSearchConditionsVO baiMoProduct = null;
        ProductIndexMappingData whiteProduct = null;
        try {
            baiMoProduct = metaDataService.getProductInfoById(id);
        } catch (MetaDataException e) {
            log.error(CLASS_LOG_PREFIX + "getBaiMoProductInfoById! MetaDataException:{}", e);
            return  null;
        }
        if(baiMoProduct != null && baiMoProduct.getId() >0) {
            whiteProduct = new ProductIndexMappingData();
            whiteProduct.setProductWidth(baiMoProduct.getProductWidth());
            whiteProduct.setProductHeight(baiMoProduct.getProductHeight());
            whiteProduct.setProductLength(baiMoProduct.getProductLength());
            whiteProduct.setProductFullPaveLength(baiMoProduct.getProductFullPaveLength());
            whiteProduct.setProductFilterAttributeMap(baiMoProduct.getProductFilterAttributeMap());
            whiteProduct.setProductOrderAttributeMap(baiMoProduct.getProductOrderAttributeMap());
            whiteProduct.setId(baiMoProduct.getId());
            whiteProduct.setProductCode(baiMoProduct.getProductCode());
            // add by huangsongbo 2018.8.3 ->start
            whiteProduct.setProductTypeValue(baiMoProduct.getProductTypeValue());
            whiteProduct.setProductTypeSmallValue(baiMoProduct.getProductTypeSmallValue());
            // add by huangsongbo 2018.8.3 ->end
        }
        return  whiteProduct;
    }
}