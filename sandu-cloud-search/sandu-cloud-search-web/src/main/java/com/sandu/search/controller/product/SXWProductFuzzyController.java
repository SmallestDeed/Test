package com.sandu.search.controller.product;/**
 * @ Author     ：weisheng.
 * @ Date       ：Created in AM 11:23 2018/7/31 0031
 * @ Description：${description}
 * @ Modified By：
 * @Version: $version$
 */

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
import com.sandu.search.entity.elasticsearch.index.GoodsIndexMappingData;
import com.sandu.search.entity.elasticsearch.index.ProductIndexMappingData;
import com.sandu.search.entity.elasticsearch.response.SearchObjectResponse;
import com.sandu.search.entity.elasticsearch.search.product.ProductSearchAggregationVo;
import com.sandu.search.entity.elasticsearch.search.product.ProductSearchMatchVo;
import com.sandu.search.entity.product.universal.ProductBaseConditionVo;
import com.sandu.search.entity.product.universal.SXWProductBaseConditionVo;
import com.sandu.search.entity.product.universal.vo.BrandAndNameProductVo;
import com.sandu.search.entity.product.universal.vo.SXWProductVo;
import com.sandu.search.entity.product.vo.ProductCategoryVo;
import com.sandu.search.entity.response.ProductFuzzySearchVo;
import com.sandu.search.entity.response.SXWProductFuzzySearchVo;
import com.sandu.search.entity.response.SearchAggregationsResponse;
import com.sandu.search.entity.response.SearchResultResponse;
import com.sandu.search.entity.response.universal.UniversalSearchResultResponse;
import com.sandu.search.exception.ProductSearchException;
import com.sandu.search.service.product.SXWProductSearchService;
import com.sandu.search.storage.company.BrandMetaDataStorage;
import com.sandu.search.storage.company.CompanyCategoryRelMetaDataStorage;
import com.sandu.search.storage.company.CompanyMetaDataStorage;
import com.sandu.search.storage.product.ProductCategoryMetaDataStorage;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedLongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedStringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;

import static java.util.Arrays.asList;

/**
 * @Title: 随选网产品搜索
 * @Package
 * @Description:
 * @author weisheng
 * @date 2018/7/31 0031AM 11:23
 */
@Slf4j
@RestController
@EnableAutoConfiguration
@RequestMapping("/sxwmini/product/fuzzy")
public class SXWProductFuzzyController {
    private final static String CLASS_LOG_PREFIX = "[随选网]产品信息接口:";

    private HttpServletRequest request;
    private final CompanyMetaDataStorage companyMetaDataStorage;
    private final BrandMetaDataStorage brandMetaDataStorage;
    private final ProductCategoryMetaDataStorage productCategoryMetaDataStorage;
    private final CompanyCategoryRelMetaDataStorage companyCategoryRelMetaDataStorage;
    private final SXWProductSearchService sxwProductSearchService;


    @Autowired
    public SXWProductFuzzyController(HttpServletRequest request,
                                     CompanyMetaDataStorage companyMetaDataStorage,
                                     BrandMetaDataStorage brandMetaDataStorage,
                                     ProductCategoryMetaDataStorage productCategoryMetaDataStorage,
                                     CompanyCategoryRelMetaDataStorage companyCategoryRelMetaDataStorage,
                                     SXWProductSearchService sxwProductSearchService) {
        this.request = request;
        this.companyMetaDataStorage = companyMetaDataStorage;
        this.brandMetaDataStorage = brandMetaDataStorage;
        this.productCategoryMetaDataStorage = productCategoryMetaDataStorage;
        this.companyCategoryRelMetaDataStorage = companyCategoryRelMetaDataStorage;
        this.sxwProductSearchService = sxwProductSearchService;
    }

    /**
     * 根据条件搜索产品列表[ProductSearchCondition]
     *
     * @date 20180731
     * @auth weisheng
     */
    @PostMapping("/list")
    SearchResultResponse queryProductListByCondition(@RequestBody SXWProductFuzzySearchVo sxwProductFuzzySearchVo) {
        if (null == sxwProductFuzzySearchVo) {
            log.warn(CLASS_LOG_PREFIX + "搜索产品失败，必需参数为空,models is null.");
            return new SearchResultResponse(ReturnCode.MUST_PARAM_IS_NULL);
        }
        long startTime = Calendar.getInstance().getTimeInMillis();

        /************************************************** 构造分页对象 **********************************************/
        PageVo pageVo = sxwProductFuzzySearchVo.getPageVo();
        if (null == pageVo) {
            pageVo = new PageVo();
        }
        if (0 == pageVo.getPageSize()) {
            pageVo.setPageSize(IndexInfoQueryConfig.DEFAULT_SEARCH_DATA_SIZE);
        }
        /************************************************** 构造产品搜索条件 **********************************************/
        SXWProductBaseConditionVo sxwProductBaseConditionVo = sxwProductFuzzySearchVo.getSxwProductBaseConditionVo();
        if(null == sxwProductBaseConditionVo){
            sxwProductBaseConditionVo = new SXWProductBaseConditionVo();
        }
        ProductSearchMatchVo productSearchMatchVo = new ProductSearchMatchVo();

       //单值多字段匹配
        List<MultiMatchField> multiMatchFieldList = new ArrayList<>();

        //构造产品搜索对象--搜索关键字(产品名>产品二级分类名>产品三级分类名>产品型号>产品描述)
        if (!org.springframework.util.StringUtils.isEmpty(sxwProductBaseConditionVo.getSearchKeyword())) {
            List<String> fidleNameList = asList(
                    QueryConditionField.QUERY_CONDITION_FIELD_GOODS_SPU_NAME
                    );
            multiMatchFieldList.add(new MultiMatchField(sxwProductBaseConditionVo.getSearchKeyword(), fidleNameList));
        }
        //装回对象
        productSearchMatchVo.setMultiMatchFieldList(multiMatchFieldList);

        /*//平台来源标识
        String platformCode = RequestHeaderUtil.getPlatformIdByRequest(request);
        //此接口仅小程序可用
        if (org.springframework.util.StringUtils.isEmpty(platformCode) || (!PlatformConstant.PLATFORM_CODE_MINI_PROGRAM.equals(platformCode))) {
            log.warn(CLASS_LOG_PREFIX + "平台标识无效:{}.", platformCode);
            return new SearchResultResponse(ReturnCode.PLATFORM_CODE_INVALID);
        }*/
        String platformCode = "miniProgram";

        /*
        //构造产品搜索对象--产品品牌ID
        String domainName = DomainUtil.getDomainNameByHost(request);
        log.info(CLASS_LOG_PREFIX + "公司域名-domainName:{}", domainName);

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

        //构造产品搜索对象--产品分类列表(1.获取公司有权限的分类, 2.分析可用分类)
        List<Integer> companyAliveCategoryIdList = companyCategoryRelMetaDataStorage.queryCategoryIdListByCompanyId(companyId);
        if (null == companyAliveCategoryIdList || 0 >= companyAliveCategoryIdList.size()) {
            log.info(CLASS_LOG_PREFIX + "未获取到公司有权限的分类DomainName:{}", DomainUtil.getDomainNameByHost(request));
            return new SearchResultResponse(ReturnCode.SEARCH_PRODUCT_NO_ALIVE_COMPANY_CATEGORY);
        }*/

        //产品五级分类长编码和三级分类长编码
        List<String> productFiveCategoryLongCodes = sxwProductBaseConditionVo.getProductFiveCategoryLongCodes();
        List<String> productThreeCategoryLongCodes = sxwProductBaseConditionVo.getProductThreeCategoryLongCodes();
        log.info(CLASS_LOG_PREFIX + "组合查询条件--产品分类长编码列表:{}", JsonUtil.toJson(productFiveCategoryLongCodes)+"--------------"
        +JsonUtil.toJson(productThreeCategoryLongCodes));

        if (productFiveCategoryLongCodes != null && productFiveCategoryLongCodes.size() > 0){
            productSearchMatchVo.setProductFiveCategoryLongCodeList(productFiveCategoryLongCodes);
        }

        if (productThreeCategoryLongCodes != null && productThreeCategoryLongCodes.size() > 0){
            productSearchMatchVo.setProductThreeCategoryLongCodeList(productThreeCategoryLongCodes);
        }







        //构造产品搜索对象--平台编码
        productSearchMatchVo.setPlatformCode(platformCode);

        //构造产品搜索对象--公司ID
        productSearchMatchVo.setCompanyId(sxwProductBaseConditionVo.getCompanyId());


        /**************************************************************是否构造产品聚合条件 ********************************************************/
        int isAggregationCategory = sxwProductFuzzySearchVo.getIsAggregationCategory();
        List<ProductSearchAggregationVo> productSearchAggregationVoList = new ArrayList<>();
        if(isAggregationCategory==1){
            //构造产品聚合对象-所有产品分类
            productSearchAggregationVoList.add(new ProductSearchAggregationVo(
                    AggregationConstant.ALL_LEVEL_CATEGORY_AGGREGATION,
                    QueryConditionField.QUERY_CONDITION_FIELD_GOODS_SKU_PO_LIST
                            + "." +
                            QueryConditionField.QUERY_CONDITION_FIELD_PRODUCT_CATEGORY_LONG_CODE_LIST,
                    10000
            ));
        }




        /********************************************** 搜索产品 *******************************************************/
        log.info(CLASS_LOG_PREFIX + "开始搜索产品，搜索条件:productBaseConditionVo{}", sxwProductBaseConditionVo.toString());
        SearchObjectResponse searchObjectResponse;
        String returnCode;
        try {
            searchObjectResponse = sxwProductSearchService.searchProduct(productSearchMatchVo, productSearchAggregationVoList, pageVo);
            returnCode = ReturnCode.SUCCESS;
        } catch (ProductSearchException e) {
            log.error(CLASS_LOG_PREFIX + "搜索产品失败:ProductSearchException:{}.", e);
            return new SearchResultResponse(ReturnCode.SEARCH_PRODUCT_FAIL);
        }

        if (null == searchObjectResponse || 0 == searchObjectResponse.getHitTotal() || null == searchObjectResponse.getHitResult()) {
            //未搜索到数据
            log.info(CLASS_LOG_PREFIX + "未搜索到数据.....productBaseConditionVo:{}, pageVo:{}", sxwProductBaseConditionVo.toString(), pageVo.toString());
            productSearchMatchVo.setMultiMatchFieldList(null);
            log.info(CLASS_LOG_PREFIX + "搜索10条猜你喜欢数据.....productSearchMatchVo:{}, pageVo:{}", sxwProductBaseConditionVo.toString(), pageVo.toString());
            try {
                searchObjectResponse = sxwProductSearchService.searchProduct(productSearchMatchVo, productSearchAggregationVoList, pageVo);
                returnCode = ReturnCode.SEARCH_PRODUCT_LIST_NULL;
            } catch (ProductSearchException e) {
                log.error(CLASS_LOG_PREFIX + "搜索产品失败:ProductSearchException:{}.", e);
                return new SearchResultResponse(ReturnCode.SEARCH_PRODUCT_FAIL);
            }
        }

        /********************************************** 格式化返回数据 *******************************************************/
        //格式化返回数据
        List<GoodsIndexMappingData> productList = (List<GoodsIndexMappingData>) searchObjectResponse.getHitResult();

        //格式化搜索结果数据
        List<SXWProductVo> sxwProductVoList = new ArrayList<>(productList.size());
        productList.forEach(productIndexMappingData -> {
            SXWProductVo sxwProductVo = new SXWProductVo();
            sxwProductVo.setSpuId(productIndexMappingData.getId());
            sxwProductVo.setSpuName(productIndexMappingData.getGoodsSpuName());
            sxwProductVo.setPicPath(productIndexMappingData.getGoodsSpuPicPath());
            sxwProductVo.setPrice(productIndexMappingData.getGoodsDefaultPrice());
            sxwProductVo.setSellNumber(0);
            sxwProductVoList.add(sxwProductVo);
        });

        //格式化搜索聚合数据
        List<String> categoryCodeList = new ArrayList<>();
        if(isAggregationCategory==1) {
            for (Aggregation aggregation : searchObjectResponse.getSearchAggs().asList()) {
                //产品分类编码
                if (AggregationConstant.ALL_LEVEL_CATEGORY_AGGREGATION.equals(aggregation.getName())) {
                    List<? extends Terms.Bucket> buckets = ((ParsedStringTerms) aggregation).getBuckets();
                    if (null != buckets && 0 < buckets.size()) {
                        for (Terms.Bucket obj : buckets) {
                            String longCode = obj.getKeyAsString();
                            String[] categoryCodeSplit = longCode.split("\\.");
                            if (null != categoryCodeSplit && 7 == categoryCodeSplit.length) {
                                categoryCodeList.add(longCode);
                            }
                        }
                    }
                }
            }
        }
        //根据产品编码分离产品分类数据
        if (null != categoryCodeList && 0 < categoryCodeList.size()) {
            List<ProductCategoryVo> productCategoryVoList = productCategoryMetaDataStorage.formatCategoryInfoByCodeList(categoryCodeList, null);

            if (null != productCategoryVoList && 0 < productCategoryVoList.size()) {
                log.info(CLASS_LOG_PREFIX + "小程序产品替换搜索已聚合分类数据,聚合3级分类数:{}.", productCategoryVoList.size());
                long costTimeOfAggs = Calendar.getInstance().getTimeInMillis() - startTime;
                log.info("{}The Total cost time that Aggs data costTimeOfAggs= {}", CLASS_LOG_PREFIX,costTimeOfAggs);
                //构造返回对象
                Map<String, Object> resultMap = new HashMap<>(2);
                resultMap.put("obj", sxwProductVoList);
                resultMap.put("aggs", productCategoryVoList);
                return new SearchResultResponse(returnCode, resultMap, searchObjectResponse.getHitTotal());
            }
        }

        //构造返回对象
        Map<String, Object> resultMap = new HashMap<>(2);
        resultMap.put("obj", sxwProductVoList);
        return new SearchResultResponse(returnCode, resultMap, searchObjectResponse.getHitTotal());
    }
}
