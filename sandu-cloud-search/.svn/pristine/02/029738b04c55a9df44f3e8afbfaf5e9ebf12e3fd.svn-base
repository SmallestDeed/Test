package com.sandu.search.service.metadata.impl;

import com.sandu.search.common.constant.ProductAttributeTypeConstant;
import com.sandu.search.common.tools.JsonUtil;
import com.sandu.search.dao.MetaDataDao;
import com.sandu.search.entity.designplan.po.DesignPlanProductPo;
import com.sandu.search.entity.elasticsearch.po.metadate.CompanyPo;
import com.sandu.search.entity.elasticsearch.po.metadate.ProductAttributePo;
import com.sandu.search.entity.elasticsearch.po.metadate.RecommendedPlanFavoritePo;
import com.sandu.search.entity.elasticsearch.po.metadate.SysUserPo;
import com.sandu.search.entity.product.vo.BaiMoProductSearchConditionsVO;
import com.sandu.search.exception.MetaDataException;
import com.sandu.search.service.metadata.MetaDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 元数据服务
 *
 * @date 20171213
 * @auth pengxuangang
 */
@Slf4j
@Service("metaDataService")
public class MetaDataServiceImpl implements MetaDataService {

    private final static String CLASS_LOG_PREFIX = "元数据服务:";

    private final MetaDataDao metaDataDao;

    @Autowired
    public MetaDataServiceImpl(MetaDataDao metaDataDao) {
        this.metaDataDao = metaDataDao;
    }

    @Override
    public DesignPlanProductPo getTempDesignPlanProductMetaDataById(Integer id) throws MetaDataException {

        //获取草稿设计方案产品元数据
        log.info(CLASS_LOG_PREFIX + "获取草稿设计方案产品元数据...");

        DesignPlanProductPo designPlanProductPo;
        try {
            designPlanProductPo = metaDataDao.getTempDesignPlanProductMetaDataById(id);
        } catch (Exception e) {
            log.error(CLASS_LOG_PREFIX + "获取草稿设计方案产品元数据失败!Exception:{}", e);
            throw new MetaDataException(CLASS_LOG_PREFIX + "获取草稿设计方案产品元数据失败!Exception:{}" + e);
        }

        log.info(CLASS_LOG_PREFIX + "获取草稿设计方案产品元数据完成.DesignPlanProductPo:{}", null == designPlanProductPo ? null : designPlanProductPo.toString());

        return designPlanProductPo;
    }

    @Override
    public DesignPlanProductPo getRecommendDesignPlanProductMetaDataById(Integer id) throws MetaDataException {

        //查询推荐设计方案产品元数据
        log.info(CLASS_LOG_PREFIX + "获取推荐设计方案产品元数据...");

        DesignPlanProductPo designPlanProductPo;
        try {
            designPlanProductPo = metaDataDao.getRecommendDesignPlanProductMetaDataById(id);
        } catch (Exception e) {
            log.error(CLASS_LOG_PREFIX + "获取推荐设计方案产品元数据失败!Exception:{}", e);
            throw new MetaDataException(CLASS_LOG_PREFIX + "获取推荐设计方案产品元数据失败!Exception:{}" + e);
        }

        log.info(CLASS_LOG_PREFIX + "获取推荐设计方案产品元数据完成.DesignPlanProductPo:{}", null == designPlanProductPo ? null : designPlanProductPo.toString());

        return designPlanProductPo;
    }

    @Override
    public DesignPlanProductPo getDiyDesignPlanProductMetaDataById(Integer id) throws MetaDataException {

        //查询自定义设计方案产品元数据
        log.info(CLASS_LOG_PREFIX + "获取自定义设计方案产品元数据...");

        DesignPlanProductPo designPlanProductPo;
        try {
            designPlanProductPo = metaDataDao.getDiyDesignPlanProductMetaDataById(id);
        } catch (Exception e) {
            log.error(CLASS_LOG_PREFIX + "获取自定义设计方案产品元数据失败!Exception:{}", e);
            throw new MetaDataException(CLASS_LOG_PREFIX + "获取自定义设计方案产品元数据失败!Exception:{}" + e);
        }

        log.info(CLASS_LOG_PREFIX + "获取自定义设计方案产品元数据完成.DesignPlanProductPo:{}", null == designPlanProductPo ? null : designPlanProductPo.toString());

        return designPlanProductPo;
    }
 
    @Override
    public List <ProductAttributePo> queryProductAttrMetaDataById(Integer id) throws MetaDataException {
        return metaDataDao.queryProductAttrMetaDataById(id);
    }
	
	@Override
    public BaiMoProductSearchConditionsVO getProductInfoById(Integer id) throws MetaDataException {
        BaiMoProductSearchConditionsVO productVO = metaDataDao.getProductInfoById(id);
        if(productVO != null) {
            List <ProductAttributePo> productAttributePoList= queryProductAttrMetaDataById(id);
            //临时对象
            Map<Integer, Map<String, String>> tempProductAttributeFilterMap = new HashMap<>();
            Map<Integer, Map<String, String>> tempProductAttributeOrderMap = new HashMap<>();

            //转换Map
            if (null != productAttributePoList && 0 != productAttributePoList.size()) {
                productAttributePoList.forEach(productAttributePo -> {
                    if (null != productAttributePo) {
                        //产品ID
                        Integer productId = productAttributePo.getProductId();
                        //属性Map
                        Map<String, String> attributeMap = new HashMap<>();
                        //插入值
                        attributeMap.put(productAttributePo.getAttributeCode(), productAttributePo.getAttributeValue());
                        //过滤属性
                        if (ProductAttributeTypeConstant.PRODUCT_ATTRIBUTE_TYPE_FILTER.equals(productAttributePo.getAttributeType())) {
                            //更新属性
                            if (tempProductAttributeFilterMap.containsKey(productId)) {
                                //加入原对象
                                attributeMap.putAll(tempProductAttributeFilterMap.get(productId));
                            }
                            //装回对象
                            tempProductAttributeFilterMap.put(productId, attributeMap);
                        } else {
                            //除过滤属性类型的其他均为排序属性
                            //更新属性
                            if (tempProductAttributeOrderMap.containsKey(productId)) {
                                //加入原对象
                                attributeMap.putAll(tempProductAttributeOrderMap.get(productId));
                            }
                            //装回对象
                            tempProductAttributeOrderMap.put(productId, attributeMap);
                        }
                    }
                });
            }
            log.info(CLASS_LOG_PREFIX + "格式化属性元数据完成....");

            //转换Json对象
            Map<String, String> productAttributeFilterJsonMap = new HashMap<>(tempProductAttributeFilterMap.size());
            Map<String, String> productAttributeOrderJsonMap = new HashMap<>(tempProductAttributeOrderMap.size());
            tempProductAttributeFilterMap.forEach((k, v) -> productAttributeFilterJsonMap.put(k + "", JsonUtil.toJson(v)));
            tempProductAttributeOrderMap.forEach((k, v) -> productAttributeOrderJsonMap.put(k + "", JsonUtil.toJson(v)));

            productVO.setProductFilterAttributeMap(productAttributeFilterJsonMap);
            productVO.setProductOrderAttributeMap(productAttributeOrderJsonMap);
        }
        return productVO;
    }
	
    @Override
    public List<RecommendedPlanFavoritePo>  getRecommendationPlanCollectBidMetaDataByIds(List<Integer> recommendIdList, List<Integer> fullHouseIdList, Integer userId) throws MetaDataException {
        //查询自定义推荐方案收藏状态元数据
        List<RecommendedPlanFavoritePo> favoritePoList;
        try {
            favoritePoList = metaDataDao.getRecommendationPlanCollectBidMetaDataByIds(recommendIdList, fullHouseIdList,userId);
        } catch (Exception e) {
            log.error(CLASS_LOG_PREFIX + "获取自定义推荐方案收藏状态元数据失败!Exception:{}", e);
            throw new MetaDataException(CLASS_LOG_PREFIX + "获取自定义推荐方案收藏状态元数据失败!Exception:{}" + e);
        }

        if (null == favoritePoList || 0 == favoritePoList.size()) {
            return  null;
        }


        return favoritePoList;
    }


    @Override
    public CompanyPo queryCompanyById(Integer companyId) throws MetaDataException {
        //查询单个企业元数据
        log.info(CLASS_LOG_PREFIX + "查询单个企业元数据...companyId={}",companyId);
        CompanyPo companyPo;
        try {
            companyPo = metaDataDao.queryCompanyById(companyId);
        } catch (Exception e) {
            log.error(CLASS_LOG_PREFIX + "查询单个企业元数据失败！Exception:{}", e);
            throw new MetaDataException(CLASS_LOG_PREFIX + "查询单个企业元数据失败！Exception:{}" + e);
        }
        log.info(CLASS_LOG_PREFIX + "查询单个企业元数据.companyPo:{}", JsonUtil.toJson(companyPo));

        return companyPo;
    }

    @Override
    public List<Integer> getProductIdsBySpuId(Integer spuId) throws MetaDataException {

        List<Integer> productIdList;
        try {
            productIdList = metaDataDao.getProductIdsBySpuId(spuId);
        } catch (Exception e) {
            log.error(CLASS_LOG_PREFIX + "根据spuId="+spuId+"查询产品数据失败！Exception:{}", e);
            throw new MetaDataException(CLASS_LOG_PREFIX + "根据spuId="+spuId+"查询产品数据失败！Exception:{}" + e);
        }

        return productIdList;
    }

    @Override
    public SysUserPo getUserById(Integer userId) throws MetaDataException {

        SysUserPo sysUserPo;
        try {
            sysUserPo = metaDataDao.queryUserById(userId);
        } catch (Exception e) {
            log.error(CLASS_LOG_PREFIX + "根据user={}查询产品数据失败！Exception:{}", userId, e);
            throw new MetaDataException(CLASS_LOG_PREFIX + "根据user="+userId+"查询产品数据失败！Exception:{}" + e);
        }

        return sysUserPo;
    }

    @Override
    public String getEnableBrandIdsByAppId(String appId) {
        return metaDataDao.getEnableBrandIdsByAppId(appId);
    }
}
