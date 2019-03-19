package com.sandu.search.controller.product;

import com.sandu.search.common.constant.IndexInfoQueryConfig;
import com.sandu.search.common.constant.PlatformConstant;
import com.sandu.search.common.constant.QueryConditionField;
import com.sandu.search.common.constant.ReturnCode;
import com.sandu.search.common.tools.DomainUtil;
import com.sandu.search.common.tools.JsonUtil;
import com.sandu.search.common.tools.RequestHeaderUtil;
import com.sandu.search.entity.common.PageVo;
import com.sandu.search.entity.elasticsearch.constant.AggregationConstant;
import com.sandu.search.entity.elasticsearch.dco.MultiMatchField;
import com.sandu.search.entity.elasticsearch.index.ProductIndexMappingData;
import com.sandu.search.entity.elasticsearch.po.ProductPo;
import com.sandu.search.entity.elasticsearch.response.SearchObjectResponse;
import com.sandu.search.entity.elasticsearch.search.product.ProductSearchAggregationVo;
import com.sandu.search.entity.elasticsearch.search.product.ProductSearchMatchVo;
import com.sandu.search.entity.product.universal.ProductBaseConditionVo;
import com.sandu.search.entity.product.universal.vo.BrandAndNameProductVo;
import com.sandu.search.entity.response.ProductFuzzySearchVo;
import com.sandu.search.entity.response.SearchAggregationsResponse;
import com.sandu.search.entity.response.SearchResultResponse;
import com.sandu.search.exception.ProductSearchException;
import com.sandu.search.service.product.ProductSearchService;
import com.sandu.search.storage.company.BrandMetaDataStorage;
import com.sandu.search.storage.company.CompanyCategoryRelMetaDataStorage;
import com.sandu.search.storage.company.CompanyMetaDataStorage;
import com.sandu.search.storage.product.ProductCategoryMetaDataStorage;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedLongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

import static java.util.Arrays.asList;

/**
 * [品牌网站]产品信息接口
 *
 * @date 20171222
 * @auth pengxuangang
 */
@Slf4j
@RestController
@EnableAutoConfiguration
@RequestMapping("/brandsite/product/fuzzy")
public class ToCProductFuzzyController {

    private final static String CLASS_LOG_PREFIX = "[品牌网站]产品信息接口:";

    private HttpServletRequest request;
    private final BrandMetaDataStorage brandMetaDataStorage;
    private final ProductSearchService productSearchService;
    private final CompanyMetaDataStorage companyMetaDataStorage;
    private final ProductCategoryMetaDataStorage productCategoryMetaDataStorage;
    private final CompanyCategoryRelMetaDataStorage companyCategoryRelMetaDataStorage;

    @Autowired
    public ToCProductFuzzyController(HttpServletRequest request, BrandMetaDataStorage brandMetaDataStorage, ProductSearchService productSearchService, CompanyMetaDataStorage companyMetaDataStorage, ProductCategoryMetaDataStorage productCategoryMetaDataStorage, CompanyCategoryRelMetaDataStorage companyCategoryRelMetaDataStorage) {
        this.request = request;
        this.brandMetaDataStorage = brandMetaDataStorage;
        this.productSearchService = productSearchService;
        this.companyMetaDataStorage = companyMetaDataStorage;
        this.productCategoryMetaDataStorage = productCategoryMetaDataStorage;
        this.companyCategoryRelMetaDataStorage = companyCategoryRelMetaDataStorage;
    }


    /**
     * 根据条件搜索产品列表[ProductSearchCondition]
     *
     * @date 20180122
     * @auth pengxuangang
     */
    @PostMapping("/list")
    SearchResultResponse queryProductListByCondition(@RequestBody ProductFuzzySearchVo productFuzzySearchVo) {

        if (null == productFuzzySearchVo) {
            log.warn(CLASS_LOG_PREFIX + "搜索产品失败，必需参数为空,models is null.");
            return new SearchResultResponse(ReturnCode.MUST_PARAM_IS_NULL);
        }

        //条件对象
        ProductBaseConditionVo productBaseConditionVo = productFuzzySearchVo.getProductBaseConditionVo();
        //分页对象
        PageVo pageVo = productFuzzySearchVo.getPageVo();

        if (null == productBaseConditionVo) {
            productBaseConditionVo = new ProductBaseConditionVo();
        }

        /************************************************** 构造分页对象 **********************************************/
        if (null == pageVo) {
            pageVo = new PageVo();
        }
        if (0 == pageVo.getPageSize()) {
            pageVo.setPageSize(IndexInfoQueryConfig.DEFAULT_SEARCH_DATA_SIZE);
        }

        /************************************ 构造产品搜索条件 ********************************************************/
        ProductSearchMatchVo productSearchMatchVo = new ProductSearchMatchVo();

        //单值多字段匹配
        List<MultiMatchField> multiMatchFieldList = new ArrayList<>();

        //构造产品搜索对象--搜索关键字(产品名>产品二级分类名>产品三级分类名>产品型号>产品描述)
        if (!StringUtils.isEmpty(productBaseConditionVo.getSearchKeyword())) {
            List<String> fidleNameList = asList(
                    QueryConditionField.QUERY_CONDITION_FIELD_PRODUCT_NAME,
                    QueryConditionField.QUERY_CONDITION_FIELD_PRODUCT_SECONDLEVEL_CATEGORYNAME,
                    QueryConditionField.QUERY_CONDITION_FIELD_PRODUCT_THIRDLEVEL_CATEGORYNAME,
                    QueryConditionField.QUERY_CONDITION_FIELD_PRODUCT_MODEL_NUMBER,
                    QueryConditionField.QUERY_CONDITION_FIELD_PRODUCT_DESCRIPTION);
            multiMatchFieldList.add(new MultiMatchField(productBaseConditionVo.getSearchKeyword(), fidleNameList));
        }

        //构造产品搜索对象--产品品牌ID
        String domainName = DomainUtil.getDomainNameByHost(request);
        log.info(CLASS_LOG_PREFIX + "公司域名-domainName:{}", domainName);

        //平台来源标识
        String platformCode = RequestHeaderUtil.getPlatformIdByRequest(request);
        //此接口仅品牌网站与移动端可用
        if (StringUtils.isEmpty(platformCode) || (!PlatformConstant.PLATFORM_CODE_TOC_SITE.equals(platformCode) && !PlatformConstant.PLATFORM_CODE_TOC_MOBILE.equals(platformCode))) {
            log.warn(CLASS_LOG_PREFIX + "平台标识无效:{}.", platformCode);
            return new SearchResultResponse(ReturnCode.PLATFORM_CODE_INVALID);
        }

        //公司ID
        Integer companyId = companyMetaDataStorage.getCompanyIdByDomainName(domainName);
        if (null == companyId || companyId <= 0 ) {
            log.info(CLASS_LOG_PREFIX + "未获取到公司信息:domainName:{}", domainName);
            return new SearchResultResponse(ReturnCode.NO_ALIVE_COMPANY);
        }
        //获取公司品牌ID
        List<Integer> brandIdList = brandMetaDataStorage.queryBrandIdListByCompanyIdList(Collections.singletonList(companyId));
        log.info(CLASS_LOG_PREFIX + "组合查询条件--产品品牌ID->品牌ID列表:{}", JsonUtil.toJson(brandIdList));
        if (null != brandIdList && 0 < brandIdList.size()) {
            productSearchMatchVo.setBrandIdList(brandIdList);
        }

        //构造产品搜索对象--产品发布状态
        productSearchMatchVo.setPutawayStatusList(Collections.singletonList(ProductPo.PUTAWAYSTATE_ALEARY_PUBLISH));

        //构造产品搜索对象--二级分类ID
        productSearchMatchVo.setSecondLevelCategoryId(productBaseConditionVo.getSecondLevelCategoryId());

        //构造产品搜索对象--三级分类ID
        int thirdLevelCategoryId = productBaseConditionVo.getThirdLevelCategoryId();
        productSearchMatchVo.setThirdLevelCategoryId(thirdLevelCategoryId);

        //构造产品搜索对象--产品分类列表(1.获取公司有权限的分类, 2.分析可用分类)
        List<Integer> companyAliveCategoryIdList = companyCategoryRelMetaDataStorage.queryCategoryIdListByCompanyId(companyId);
        if (null == companyAliveCategoryIdList || 0 >= companyAliveCategoryIdList.size()) {
            log.info(CLASS_LOG_PREFIX + "未获取到公司有权限的分类DomainName:{}", DomainUtil.getDomainNameByHost(request));
            return new SearchResultResponse(ReturnCode.SEARCH_PRODUCT_NO_ALIVE_COMPANY_CATEGORY);
        }
        List<Integer> fifthLevelCategoryIdList = productBaseConditionVo.getFifthLevelCategoryIdList();
        if (0 != thirdLevelCategoryId && null != fifthLevelCategoryIdList && 0 < fifthLevelCategoryIdList.size()) {
            //过滤符合查询条件的产品分类--有3级分类后才能过滤4,5级分类
            List<Integer> categoryIdList = productCategoryMetaDataStorage.queryAliveCategoryLongCodeByCategoryIdList(thirdLevelCategoryId, fifthLevelCategoryIdList, companyAliveCategoryIdList);
            //构造查询条件
            productSearchMatchVo.setProductCategoryIdList(categoryIdList);
        }

        //构造产品搜索对象--是否包含白膜产品
        productSearchMatchVo.setIncludeWhiteMembraneProduct(false);
        //装回对象
        productSearchMatchVo.setMultiMatchFieldList(multiMatchFieldList);

        //构造产品搜索对象--平台编码
        productSearchMatchVo.setPlatformCode(platformCode);

        /************************************************************** 构造产品聚合条件 ********************************************************/
        List<ProductSearchAggregationVo> productSearchAggregationVoList = new ArrayList<>(4);
        //构造产品聚合对象-产品二级分类ID
        productSearchAggregationVoList.add(new ProductSearchAggregationVo(
                AggregationConstant.TWO_LEVEL_CATEGORY_AGGREGATION,
                QueryConditionField.QUERY_CONDITION_FIELD_PRODUCT_SECONDLEVEL_CATEGORID,
                100
                ));
        //构造产品聚合对象-产品三级分类ID
        productSearchAggregationVoList.add(new ProductSearchAggregationVo(
                AggregationConstant.THREE_LEVEL_CATEGORY_AGGREGATION,
                QueryConditionField.QUERY_CONDITION_FIELD_PRODUCT_THIRDLEVEL_CATEGORYID,
                100
        ));
        //构造产品聚合对象-产品四级分类ID
        productSearchAggregationVoList.add(new ProductSearchAggregationVo(
                AggregationConstant.FOUR_LEVEL_CATEGORY_AGGREGATION,
                QueryConditionField.QUERY_CONDITION_FIELD_PRODUCT_FOURTHLEVEL_CATEGORYID,
                100
        ));
        //构造产品聚合对象-产品所有分类ID
        productSearchAggregationVoList.add(new ProductSearchAggregationVo(
                AggregationConstant.ALL_LEVEL_CATEGORY_AGGREGATION,
                QueryConditionField.QUERY_CONDITION_FIELD_PRODUCT_ALLCATEGORYID,
                999
        ));

        /********************************************** 搜索产品 *******************************************************/
        log.info(CLASS_LOG_PREFIX + "开始搜索产品，搜索条件:productBaseConditionVo{}", productBaseConditionVo.toString());
        SearchObjectResponse searchObjectResponse;
        try {
            searchObjectResponse = productSearchService.searchProduct(productSearchMatchVo, productSearchAggregationVoList, pageVo);
        } catch (ProductSearchException e) {
            log.error(CLASS_LOG_PREFIX + "搜索产品失败:ProductSearchException:{}.", e);
            return new SearchResultResponse(ReturnCode.SEARCH_PRODUCT_FAIL);
        }

        if (null == searchObjectResponse || 0 == searchObjectResponse.getHitTotal() || null == searchObjectResponse.getHitResult()) {
            //未搜索到数据
            log.info(CLASS_LOG_PREFIX + "未搜索到数据.....productBaseConditionVo:{}, pageVo:{}", productBaseConditionVo.toString(), pageVo.toString());
            return new SearchResultResponse(ReturnCode.SUCCESS, "", 0);
        }

        /********************************************** 格式化返回数据 *******************************************************/
        //格式化返回数据
        List<ProductIndexMappingData> productList = (List<ProductIndexMappingData>) searchObjectResponse.getHitResult();

        //格式化搜索结果数据
        List<BrandAndNameProductVo> brandAndNameProductVoList = new ArrayList<>(productList.size());
        productList.forEach(productIndexMappingData -> {
            BrandAndNameProductVo brandAndNameProductVo = new BrandAndNameProductVo();
            brandAndNameProductVo.setId(productIndexMappingData.getId());
            brandAndNameProductVo.setProductName(productIndexMappingData.getProductName());
            brandAndNameProductVo.setProductBrandName(productIndexMappingData.getProductBrandName());
            brandAndNameProductVo.setPicPath(productIndexMappingData.getProductPicPath());
            brandAndNameProductVo.setProductModelNumber(productIndexMappingData.getProductModelNumber());
            brandAndNameProductVo.setProductSalePrice(productIndexMappingData.getProductSalePrice());
            brandAndNameProductVoList.add(brandAndNameProductVo);
        });

        //格式化搜索聚合数据
        Map<String, Aggregation> aggregationMap = searchObjectResponse.getSearchAggs().asMap();
        List<SearchAggregationsResponse> searchAggregationsResponseList = null;
        if (null != aggregationMap && 0 < aggregationMap.size()) {
            //初始化结果集
            searchAggregationsResponseList = new ArrayList<>(aggregationMap.size());
            for (Map.Entry<String, Aggregation> entry : aggregationMap.entrySet()) {
                //聚合数据对象
                Aggregation aggregation = entry.getValue();
                //聚合结果集
                List<? extends Terms.Bucket> bucketList = ((ParsedLongTerms) aggregation).getBuckets();
                //聚合返回对象<值, 文档数>
                Map<String, Long> aggregationResponseMap = new HashMap<>(bucketList.size());

                if (0 < bucketList.size()) {
                    //不同聚合数据分类处理
                    switch (entry.getKey()) {
                        case AggregationConstant.ALL_LEVEL_CATEGORY_AGGREGATION :
                            //所有级分类聚合
                            searchAggregationsResponseList.addAll(analyzeCategoryLevelByCategoryIdList(bucketList));
                            break;
                        default:
                            //其他分类聚合
                            bucketList.forEach(bucket -> aggregationResponseMap.put(bucket.getKey().toString(), bucket.getDocCount()));
                            searchAggregationsResponseList.add(new SearchAggregationsResponse(aggregation.getName(), aggregationResponseMap));
                    }
                }
            }
        }

        //结果集
        Map<String, Object> resultMap = new HashMap<>(2);
        resultMap.put("list", brandAndNameProductVoList);
        resultMap.put("aggs", searchAggregationsResponseList);

        return new SearchResultResponse(ReturnCode.SUCCESS, resultMap, searchObjectResponse.getHitTotal());
    }

    /**
     * 通过分类ID列表分析出每个分类层级
     *
     * @param bucketList    桶对象
     * @return
     */
    private List<SearchAggregationsResponse> analyzeCategoryLevelByCategoryIdList(List<? extends Terms.Bucket> bucketList) {

        if (null == bucketList || 0 >= bucketList.size()) {
            return null;
        }

        //分类对象
        Map<String, Long> fiveAggregationMap = new HashMap<>();

        //遍历分类ID
        bucketList.forEach(bucket -> {
            //分类层级
            Integer categoryLevel = productCategoryMetaDataStorage.getCategoryLevelByCategoryId(Integer.parseInt(bucket.getKey().toString()));
            //层级过滤
            switch (categoryLevel) {
                case 5:
                    //产品仅允许被挂在5级分类下
                    fiveAggregationMap.put(bucket.getKey().toString(), bucket.getDocCount());
                    break;
                default:
                    log.info(CLASS_LOG_PREFIX + "聚合数据分类层级分析跳过:category id:{}, level:{}.", bucket.getKey().toString(), categoryLevel);
            }
        });

        //分类层级结果(5级)
        return Collections.singletonList(new SearchAggregationsResponse(AggregationConstant.FIVE_LEVEL_CATEGORY_AGGREGATION, fiveAggregationMap));
    }

}