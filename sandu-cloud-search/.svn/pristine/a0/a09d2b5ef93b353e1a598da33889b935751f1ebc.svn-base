package com.sandu.search.service.metadata;

import com.sandu.search.entity.designplan.po.DesignPlanProductPo;
import com.sandu.search.entity.elasticsearch.po.metadate.CompanyPo;
import com.sandu.search.entity.elasticsearch.po.metadate.ProductAttributePo;
import com.sandu.search.entity.elasticsearch.po.metadate.RecommendedPlanFavoritePo;
import com.sandu.search.entity.elasticsearch.po.metadate.SysUserPo;
import com.sandu.search.entity.product.vo.BaiMoProductSearchConditionsVO;
import com.sandu.search.exception.MetaDataException;

import java.util.List;

/**
 * 元数据服务
 *
 * @date 20171213
 * @auth pengxuangang
 */
public interface MetaDataService {

    /**
     * 获取草稿设计方案产品元数据
     *
     * @return
     */
    DesignPlanProductPo getTempDesignPlanProductMetaDataById(Integer id) throws MetaDataException;

    /**
     * 获取推荐设计方案产品元数据
     *
     * @return
     */
    DesignPlanProductPo getRecommendDesignPlanProductMetaDataById(Integer id) throws MetaDataException;

    /**
     * 获取自定义设计方案产品元数据
     *
     * @return
     */
    DesignPlanProductPo getDiyDesignPlanProductMetaDataById(Integer id) throws MetaDataException;
 
    List<ProductAttributePo> queryProductAttrMetaDataById(Integer id) throws MetaDataException;

    BaiMoProductSearchConditionsVO getProductInfoById(Integer id) throws MetaDataException;

//   List<BaseCompanyMiniProgramConfig> queryBaseCompanyMiniProgramConfig() throws MetaDataException;

    /**
     * 获取自定义推荐方案收藏状态元数据
     *
     * @return
     */
    List<RecommendedPlanFavoritePo> getRecommendationPlanCollectBidMetaDataByIds(List<Integer> recommendIdList, List<Integer> fullHouseIdList, Integer userId) throws MetaDataException;

    /**
     * 获取企业信息
     * @param companyId
     * @return
     */
    CompanyPo queryCompanyById(Integer companyId) throws MetaDataException;

    /**
     * 获取商品下的产品
     * @param spuId
     * @return
     */
    List<Integer> getProductIdsBySpuId(Integer spuId) throws MetaDataException;

    /**
     * 获取用户信息
     * @param userId
     * @return
     * @throws MetaDataException
     */
    SysUserPo getUserById(Integer userId) throws MetaDataException;

    /**
     * 根据APPID查询可见品牌ID
     * @param appId
     * @return
     */
    String getEnableBrandIdsByAppId(String appId);
}
