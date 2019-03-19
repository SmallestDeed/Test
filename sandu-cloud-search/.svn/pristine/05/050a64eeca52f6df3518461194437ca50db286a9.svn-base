package com.sandu.search.controller.product;

import com.sandu.common.LoginContext;
import com.sandu.search.common.constant.*;
import com.sandu.search.common.tools.DomainUtil;
import com.sandu.search.common.tools.EntityCopyUtils;
import com.sandu.search.common.tools.ProductDataOrderUtil;
import com.sandu.search.common.tools.RequestHeaderUtil;
import com.sandu.search.entity.common.PageVo;
import com.sandu.search.entity.elasticsearch.dco.ValueOffset;
import com.sandu.search.entity.elasticsearch.dco.ValueRange;
import com.sandu.search.entity.elasticsearch.index.ProductIndexMappingData;
import com.sandu.search.entity.elasticsearch.po.ProductPo;
import com.sandu.search.entity.elasticsearch.response.SearchObjectResponse;
import com.sandu.search.entity.elasticsearch.search.product.ProductSearchMatchVo;
import com.sandu.search.entity.product.universal.vo.BrandAndNameProductVo;
import com.sandu.search.entity.response.SearchResultResponse;
import com.sandu.search.entity.user.LoginUser;
import com.sandu.search.exception.ProductSearchException;
import com.sandu.search.service.product.ProductSearchService;
import com.sandu.search.storage.company.BrandMetaDataStorage;
import com.sandu.search.storage.company.CompanyMetaDataStorage;
import com.sandu.search.storage.company.UnionBrandMetaDataStorage;
import com.sandu.search.storage.design.DesignPlanProductMetaDataStorage;
import com.sandu.search.storage.product.ProductCategoryMetaDataStorage;
import com.sandu.search.strategy.ProductReplaceSearchFullPraveStrategy;
import com.sandu.search.strategy.ProductReplaceSearchHeightStrategy;
import com.sandu.search.strategy.ProductTypeMatchStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * [移动端]替换产品接口
 *
 * @date 2018/3/14
 * @auth pengxuangang
 * @mail xuangangpeng@gmail.com
 */
@Slf4j
@RestController
@EnableAutoConfiguration
@RequestMapping("/mobile/product/replace")
public class ToCProductReplaceController {

    private final static String CLASS_LOG_PREFIX = "[移动端|运营网站]替换产品接口:";

    private HttpServletRequest request;
    private final BrandMetaDataStorage brandMetaDataStorage;
    private final ProductSearchService productSearchService;
    private final CompanyMetaDataStorage companyMetaDataStorage;
    private final ProductTypeMatchStrategy productTypeMatchStrategy;
    private final UnionBrandMetaDataStorage unionBrandMetaDataStorage;
    private final ProductCategoryMetaDataStorage productCategoryMetaDataStorage;
    private final DesignPlanProductMetaDataStorage designPlanProductMetaDataStorage;

    @Autowired
    public ToCProductReplaceController(HttpServletRequest request, BrandMetaDataStorage brandMetaDataStorage, ProductSearchService productSearchService, CompanyMetaDataStorage companyMetaDataStorage, ProductTypeMatchStrategy productTypeMatchStrategy, UnionBrandMetaDataStorage unionBrandMetaDataStorage, ProductCategoryMetaDataStorage productCategoryMetaDataStorage, DesignPlanProductMetaDataStorage designPlanProductMetaDataStorage) {
        this.request = request;
        this.brandMetaDataStorage = brandMetaDataStorage;
        this.productSearchService = productSearchService;
        this.companyMetaDataStorage = companyMetaDataStorage;
        this.productTypeMatchStrategy = productTypeMatchStrategy;
        this.unionBrandMetaDataStorage = unionBrandMetaDataStorage;
        this.productCategoryMetaDataStorage = productCategoryMetaDataStorage;
        this.designPlanProductMetaDataStorage = designPlanProductMetaDataStorage;
    }


    /**
     * 产品替换搜索列表[ProductBaseConditionVo]----此接口仅品牌网站与移动端可用
     *
     * @param productTypeValue        产品大类
     * @param productTypeSmallValue   产品小类
     * @param start                   数据起始数
     * @param dataSize                单页数据条数
     * @param productCategoryId       产品分类ID
     * @param productCategoryLongCode 产品分类长编码
     * @param designPlanProductId     设计方案产品ID
     * @param designPlanType          设计方案类型(Desc: com.sandu.search.common.constant.DesignPlanType)
     * @param productId               产品ID(用于新增柜类产品长度过滤)
     * @return
     */
    @RequestMapping("/list")
    SearchResultResponse queryProductReplaceByProductCondition(@RequestParam(required = false)
                                                               Integer productTypeValue,
                                                               Integer productTypeSmallValue,
                                                               Integer start,
                                                               Integer dataSize,
                                                               Integer productCategoryId,
                                                               String productCategoryLongCode,
                                                               Integer designPlanProductId,
                                                               int designPlanType,
                                                               Integer productId) {
        //参数转换
        productTypeValue = (null == productTypeValue) ? 0 : productTypeValue;
        productTypeSmallValue = (null == productTypeSmallValue) ? 0 : productTypeSmallValue;
        designPlanProductId = (null == designPlanProductId) ? 0 : designPlanProductId;

        //平台来源标识
        String platformCode = RequestHeaderUtil.getPlatformIdByRequest(request);
        //此接口仅品牌网站与移动端可用
        if (StringUtils.isEmpty(platformCode) || (!PlatformConstant.PLATFORM_CODE_TOC_SITE.equals(platformCode)
                                                && !PlatformConstant.PLATFORM_CODE_TOC_MOBILE.equals(platformCode)
                                                && !PlatformConstant.PLATFORM_CODE_MINI_PROGRAM.equals(platformCode))) {
            log.warn(CLASS_LOG_PREFIX + "平台标识无效:{}.", platformCode);
            return new SearchResultResponse(ReturnCode.PLATFORM_CODE_INVALID);
        }

        /******************************************* 用户登录信息 **********************************************/
        //获取用户登录信息
        LoginUser loginUser = LoginContext.getLoginUser(LoginUser.class);

        if (null == loginUser) {
            log.warn(CLASS_LOG_PREFIX + "获取用户信息未空，用户未登录。");
            return new SearchResultResponse(ReturnCode.USER_NO_LOGIN);
        }
        //用户ID(用于计算用户已使用产品次数)
        Integer userId = loginUser.getId();
        //域名名
        String domainName = DomainUtil.getDomainNameByHost(request);
        log.info(CLASS_LOG_PREFIX + "公司域名-domainName:{}", domainName);
        //公司ID
        Integer companyId = companyMetaDataStorage.getCompanyIdByDomainName(domainName);
        if (null == companyId || companyId <= 0 ) {
            log.info(CLASS_LOG_PREFIX + "未获取到公司信息:domainName:{}", domainName);
            return new SearchResultResponse(ReturnCode.NO_ALIVE_COMPANY);
        }

        /********************************************* 白膜信息 ***************************************************/
        //白膜产品对象
        ProductIndexMappingData whiteMembraneProduct = null;
        //获取白膜产品(后面可能需要白膜产品的信息做筛选)
        if (0 != designPlanProductId) {
            int productWhiteMembraneId = designPlanProductMetaDataStorage.getWhiteMembraneProductIdById(designPlanProductId, designPlanType);
            log.info(CLASS_LOG_PREFIX + "获取白膜产品(后面可能需要白膜产品的信息做筛选)完成!designPlanProductId:{}, productWhiteMembraneId:{},designPlanType:{}.", new Integer[]{
                            designPlanProductId,
                            productWhiteMembraneId,
                            designPlanType
                    });
            if (0 != productWhiteMembraneId) {
//                    whiteMembraneProduct = productSearchService.searchProductById(productWhiteMembraneId);
                    whiteMembraneProduct = designPlanProductMetaDataStorage.getProductInfoById(productWhiteMembraneId);
            }

            //根据产品编码检查产品是否为白膜产品(区分用户是在设计方案场景中新增产品[无白膜]还是替换产品[有白膜])
            if (null != productId && 0 != productId && (null == whiteMembraneProduct || !whiteMembraneProduct.getProductCode().startsWith("baimo_"))) {
                //不是白膜则根据产品ID来查询
                try {
                    whiteMembraneProduct =  productSearchService.searchProductById(productId);
                } catch (ProductSearchException e) {
                    log.error(CLASS_LOG_PREFIX + "获取白膜产品(后面可能需要白膜产品的信息做筛选)失败!ProductSearchException:{}", e);
                    return new SearchResultResponse(ReturnCode.SEARCH_PRODUCT_GET_WHITE_MEMBRANE_FAIL);
                }

                if (null == whiteMembraneProduct) {
                    log.info(CLASS_LOG_PREFIX + "根据产品ID获取产品对象失败，未找到此产品ProductId:{}.", productId);
                    return new SearchResultResponse(ReturnCode.SEARCH_PRODUCT_GET_WHITE_MEMBRANE_FAIL);
                }

                //长度转为白膜全铺长度
                if (0 < whiteMembraneProduct.getProductLength()) {
                    //长度转为白膜全铺长度
                    whiteMembraneProduct.setProductFullPaveLength(whiteMembraneProduct.getProductLength());
                }
            }
        }

        /************************* 构造分页对象 **************************************/
        PageVo pageVo = new PageVo();
        if (null == start) {
            start = IndexInfoQueryConfig.DEFAULT_SEARCH_DATA_START;
        }
        if (null == dataSize) {
            dataSize = IndexInfoQueryConfig.DEFAULT_SEARCH_DATA_SIZE;
        }
        pageVo.setStart(start);
        pageVo.setPageSize(dataSize);

        /************************* 构造产品搜索条件 **************************************/
        ProductSearchMatchVo productSearchMatchVo = new ProductSearchMatchVo();

        //产品分类长编码列表
        List<String> productCategoryLongCodeList = new ArrayList<>();

        if (0 < companyId) {
            //构造产品搜索对象--公司品牌ID
            //获取公司品牌
            List<Integer> brandIdList = brandMetaDataStorage.queryBrandIdListByCompanyIdList(Collections.singletonList(companyId));

            //获取联盟品牌
            List<Integer> unionBrandIdList = unionBrandMetaDataStorage.queryUnionBrandByBrandIdList(brandIdList);

            if (null != unionBrandIdList && 0 < unionBrandIdList.size()) {
                //合并结果集
                brandIdList.addAll(unionBrandIdList);
                brandIdList = brandIdList.stream().distinct().collect(Collectors.toList());
            }

            productSearchMatchVo.setBrandIdList(brandIdList);
        }

        //构造产品搜索对象--产品发布状态
        productSearchMatchVo.setPutawayStatusList(Collections.singletonList(ProductPo.PUTAWAYSTATE_ALEARY_PUBLISH));

        //构造产品搜索对象--产品分类ID列表
        if (null != productCategoryId) {
            productSearchMatchVo.setProductCategoryIdList(Collections.singletonList(productCategoryId));
        }

        //构造产品搜索对象--产品分类长编码
        if (!StringUtils.isEmpty(productCategoryLongCode)) {
            //查询出符合条件分类
            List<String> categoryCodeList = productCategoryMetaDataStorage.queryAllCategoryCodeByCodeName(productCategoryLongCode);
            productCategoryLongCodeList.addAll(categoryCodeList);
        }

        //构造产品搜索对象--产品大类
        if (0 != productTypeValue) {
            productSearchMatchVo.setProductTypeValue(productTypeValue);
        }

        //构造产品搜索对象--产品小类---根据产品大小类匹配产品小类规则
        List<Integer> productSmallTypeList = productTypeMatchStrategy.matchProductSmallTypeRule(productTypeValue, productTypeSmallValue);
        if (null != productSmallTypeList && 0 < productSmallTypeList.size()) {
            productSearchMatchVo.setProductTypeSmallValueList(productSmallTypeList);
        }

        //构造产品搜索对象--白模全铺长度
        if (null != whiteMembraneProduct) {
            //产品白膜全铺长度
            int productFullPaveLength = whiteMembraneProduct.getProductFullPaveLength();
            if (0 < productFullPaveLength) {
                //适配白膜全铺长度策略
                ValueOffset fullPaveLengthValueOffset = ProductReplaceSearchFullPraveStrategy.adaptationFullPraveLength(productTypeValue, productTypeSmallValue, productFullPaveLength);
                if (null == fullPaveLengthValueOffset) {
                    //如果查询有特殊白膜过滤小类则增加白膜过滤
                    List<Integer> productTypeSmallValueList = productSearchMatchVo.getProductTypeSmallValueList();
                    if (null != productTypeSmallValueList && 0 < productTypeSmallValueList.size()) {
                        //遍历小类,对比产品大小类-如果有匹配的，则新增全铺长度过滤
                        for (Integer nowProductTypeSmallValue : productTypeSmallValueList) {
                            switch ((productTypeValue + "-" + nowProductTypeSmallValue)) {
                                case ProductTypeValue.PRODUCT_TYPE_VALUE_CABINET + "-" + ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_DIY_CLOTHES_CABINET:
                                    fullPaveLengthValueOffset = ProductReplaceSearchFullPraveStrategy.adaptationFullPraveLength(ProductTypeValue.PRODUCT_TYPE_VALUE_CABINET, ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_DIY_CLOTHES_CABINET, productFullPaveLength);
                                    break;
                                case ProductTypeValue.PRODUCT_TYPE_VALUE_BATHROOM + "-" + ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_DIY_CABINET_BATHROOM:
                                    fullPaveLengthValueOffset = ProductReplaceSearchFullPraveStrategy.adaptationFullPraveLength(ProductTypeValue.PRODUCT_TYPE_VALUE_BATHROOM, ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_DIY_CABINET_BATHROOM, productFullPaveLength);
                                    break;
                                case ProductTypeValue.PRODUCT_TYPE_VALUE_BATHROOM + "-" + ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_DIY_MIRROR_BATHROOM:
                                    fullPaveLengthValueOffset = ProductReplaceSearchFullPraveStrategy.adaptationFullPraveLength(ProductTypeValue.PRODUCT_TYPE_VALUE_BATHROOM, ProductSmallTypeValue.PRODUCT_SMALL_TYPE_VALUE_DIY_MIRROR_BATHROOM, productFullPaveLength);
                                    break;
                            }
                        }
                    }
                }

                productSearchMatchVo.setFullPaveLengthValueOffset(fullPaveLengthValueOffset);
            }
        }

        //构造产品搜索对象--高度
        if (null != whiteMembraneProduct) {
            //产品白膜高度
            int productWhiteMembraneHeight = whiteMembraneProduct.getProductHeight();
            if (0 < productWhiteMembraneHeight) {
                ValueRange productHeightValueRange = ProductReplaceSearchHeightStrategy.adaptationProductHeight(productTypeValue, productTypeSmallValue, productWhiteMembraneHeight);
                if (null != productHeightValueRange) {
                    productSearchMatchVo.setProductHeightValueRange(productHeightValueRange);
                }
            }

            //构造产品搜索对象--产品属性--匹配度计分排序
            Map<String, String> productFilterAttributeMap = whiteMembraneProduct.getProductFilterAttributeMap();
            if (null != productFilterAttributeMap && 0 < productFilterAttributeMap.size()) {
                productSearchMatchVo.setProductFilterAttributeMap(productFilterAttributeMap);
            }

            //构造产品聚合对象--产品属性--计分排序
            Map<String, String> productOrderAttributeMap = whiteMembraneProduct.getProductOrderAttributeMap();
            if (null != productOrderAttributeMap && 0 < productOrderAttributeMap.size()) {
                productSearchMatchVo.setProductOrderAttributeMap(productOrderAttributeMap);
            }

            //构造产品聚合对象--产品属性--产品使用数排序
            if (null != userId && 0 < userId) {
                productSearchMatchVo.setUserId(userId);
            }
        }

        //构造产品搜索对象--平台编码
        productSearchMatchVo.setPlatformCode(platformCode);

        //装回对象
        productSearchMatchVo.setProductCategoryLongCodeList(productCategoryLongCodeList);


        /************************* 搜索产品 ************************/
        SearchObjectResponse searchObjectResponse;
        try {
            searchObjectResponse = productSearchService.searchProduct(productSearchMatchVo, null, pageVo);
        } catch (ProductSearchException e) {
            log.error(CLASS_LOG_PREFIX + "搜索产品失败:ProductSearchException:{}.", e);
            return new SearchResultResponse(ReturnCode.SEARCH_PRODUCT_FAIL);
        }

        if (null == searchObjectResponse || 0 == searchObjectResponse.getHitTotal() || null == searchObjectResponse.getHitResult()) {
            //未搜索到数据
            log.info(CLASS_LOG_PREFIX + "未搜索到数据.....ProductSearchMatchVo:{}, pageVo:{}", productSearchMatchVo.toString(), pageVo.toString());
            return new SearchResultResponse(ReturnCode.SUCCESS);
        }

        //格式化返回数据
        List<ProductIndexMappingData> productList = (List<ProductIndexMappingData>) searchObjectResponse.getHitResult();

        //根据产品属性排序对数据进行排序
        if (null != productList && null != whiteMembraneProduct) {
            //白膜产品排序属性
            Map<String, String> whiteMembraneOrderAttributeMap = whiteMembraneProduct.getProductOrderAttributeMap();
            if (null != whiteMembraneOrderAttributeMap && 0 < whiteMembraneOrderAttributeMap.size()) {
                productList = ProductDataOrderUtil.productOrderByAttribute(productList, whiteMembraneOrderAttributeMap);
            }
        }

        //格式化数据
        List<BrandAndNameProductVo> brandAndNameProductVoList = new ArrayList<>(null == productList ? 10 : productList.size());
        if (null != productList && 0 < productList.size()) {
            productList.forEach(productIndexMappingData -> {
                //复制对象
                BrandAndNameProductVo brandAndNameProductVo = EntityCopyUtils.copyData(productIndexMappingData, BrandAndNameProductVo.class);
                if (null != brandAndNameProductVo) {
                    //字段名不同对象单独处理
                    brandAndNameProductVo.setPicPath(productIndexMappingData.getProductPicPath());
                    brandAndNameProductVoList.add(brandAndNameProductVo);
                }
            });
        }

        return new SearchResultResponse(ReturnCode.SUCCESS, brandAndNameProductVoList, searchObjectResponse.getHitTotal());
    }

}