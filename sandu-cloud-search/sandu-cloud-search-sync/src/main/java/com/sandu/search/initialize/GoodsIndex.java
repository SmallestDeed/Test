package com.sandu.search.initialize;/**
 * @ Author     ：weisheng.
 * @ Date       ：Created in PM 2:43 2018/8/1 0001
 * @ Description：${description}
 * @ Modified By：
 * @Version: $version$
 */

import com.sandu.search.common.constant.IndexInfoQueryConfig;
import com.sandu.search.common.tools.EntityCopyUtils;
import com.sandu.search.common.tools.EntityUtil;
import com.sandu.search.common.tools.JsonUtil;
import com.sandu.search.config.ElasticSearchConfig;
import com.sandu.search.entity.elasticsearch.constant.IndexConstant;
import com.sandu.search.entity.elasticsearch.constant.TypeConstant;
import com.sandu.search.entity.elasticsearch.dto.IndexRequestDTO;
import com.sandu.search.entity.elasticsearch.index.GoodsIndexMappingData;
import com.sandu.search.entity.elasticsearch.po.BaseGoodsSkuPo;
import com.sandu.search.entity.elasticsearch.po.BaseGoodsSpuPo;
import com.sandu.search.entity.elasticsearch.po.ProductCategoryPo;
import com.sandu.search.entity.elasticsearch.po.metadate.*;
import com.sandu.search.entity.elasticsearch.po.product.ProductPlatformData;
import com.sandu.search.entity.elasticsearch.response.BulkStatistics;
import com.sandu.search.exception.ElasticSearchException;
import com.sandu.search.exception.GoodsIndexException;
import com.sandu.search.exception.ProductIndexException;
import com.sandu.search.service.elasticsearch.ElasticSearchService;
import com.sandu.search.service.index.GoodsIndexService;
import com.sandu.search.service.metadata.MetaDataService;
import com.sandu.search.storage.goods.GoodsSkuMetaDataStorage;
import com.sandu.search.storage.goods.SpuInfoMetaDataStorage;
import com.sandu.search.storage.product.BaseProductDataStorage;
import com.sandu.search.storage.product.ProductCategoryMetaDataStorage;
import com.sandu.search.storage.product.ProductCategoryRelMetaDataStorage;
import com.sandu.search.storage.product.ProductPlatformMetaDataStorage;
import com.sandu.search.storage.resource.ResPicMetaDataStorage;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.search.DocValueFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.*;

/**
 * @author weisheng
 * @Title: 商品索引
 * @Package
 * @Description:
 * @date 2018/8/1 0001PM 2:43
 */
@Slf4j
@Component
public class GoodsIndex {

    private final static String CLASS_LOG_PREFIX = "初始化搜索引擎商品信息索引:";


    private final GoodsIndexService goodsIndexService;
    private final ElasticSearchConfig elasticSearchConfig;
    private final ProductPlatformMetaDataStorage productPlatformMetaDataStorage;
    private final ProductCategoryRelMetaDataStorage productCategoryRelMetaDataStorage;
    private final ProductCategoryMetaDataStorage productCategoryMetaDataStorage;
    private final ElasticSearchService elasticSearchService;
    private final GoodsSkuMetaDataStorage goodsSkuMetaDataStorage;
    private final SpuInfoMetaDataStorage spuInfoMetaDataStorage;
    private final ResPicMetaDataStorage resPicMetaDataStorage;
    private final BaseProductDataStorage baseProductDataStorage;

    @Autowired
    public GoodsIndex(GoodsIndexService goodsIndexService, ElasticSearchConfig elasticSearchConfig, ProductPlatformMetaDataStorage productPlatformMetaDataStorage, ProductCategoryRelMetaDataStorage productCategoryRelMetaDataStorage,
                      ProductCategoryMetaDataStorage productCategoryMetaDataStorage, ElasticSearchService elasticSearchService,
                      GoodsSkuMetaDataStorage goodsSkuMetaDataStorage, SpuInfoMetaDataStorage spuInfoMetaDataStorage,
                      ResPicMetaDataStorage resPicMetaDataStorage, BaseProductDataStorage baseProductDataStorage,MetaDataService metaDataService) {
        this.goodsIndexService = goodsIndexService;
        this.elasticSearchConfig = elasticSearchConfig;
        this.productPlatformMetaDataStorage = productPlatformMetaDataStorage;
        this.productCategoryRelMetaDataStorage = productCategoryRelMetaDataStorage;
        this.productCategoryMetaDataStorage = productCategoryMetaDataStorage;
        this.elasticSearchService = elasticSearchService;
        this.goodsSkuMetaDataStorage = goodsSkuMetaDataStorage;
        this.spuInfoMetaDataStorage = spuInfoMetaDataStorage;
        this.resPicMetaDataStorage = resPicMetaDataStorage;
        this.baseProductDataStorage = baseProductDataStorage;
    }

    /**
     * 同步商品信息数据
     */
    public void syncGoodsInfoData() {

        log.info(CLASS_LOG_PREFIX + "开始索引商品信息数据........");
        //开始时间
        long startTime = System.currentTimeMillis();

        //数据查询初始位
        int start = 0;
        //每次数据量
        int limit = IndexInfoQueryConfig.DEFAULT_QUERY_PRODUCTPOINFO_LIMIT;

        //是否继续处理
        boolean isContinueHandler = true;
        //总数据量
        int totalProductCount = 0;
        //总索引量
        int totalIndexCount = 0;
        //异常数据
        int totalExceptionCount = 0;

        while (isContinueHandler) {
            List<BaseGoodsSpuPo> goodsPoList;
            /********************************** 查询产品信息 *********************************/
            //更新全量数据
            try {
                goodsPoList = goodsIndexService.queryGoodsPoList(start, limit);
            } catch (GoodsIndexException e) {
                log.error(CLASS_LOG_PREFIX + "查询商品列表失败:GoodsIndexException:{}", e);
                return;
            }

            //递增start下标
            start = start + limit;

            //无数据中断操作
            if (null == goodsPoList || 0 == goodsPoList.size()) {
                log.info(CLASS_LOG_PREFIX + "查询商品列表数据为空：start:{},limit:{}.", start, limit);
                return;
            }
            //数据不足指定数据量表示已查询出最后一条数据,终止循环
            if (goodsPoList.size() < IndexInfoQueryConfig.DEFAULT_QUERY_PRODUCTPOINFO_LIMIT) {
                isContinueHandler = false;
            }

            //索引产品数据
            int successIndexCount = indexGoodsData(goodsPoList);

            //累加数据量
            totalProductCount += goodsPoList.size();
            totalIndexCount += successIndexCount;
            totalExceptionCount += goodsPoList.size() - successIndexCount;
        }

        log.info(CLASS_LOG_PREFIX + "索引所有商品列表数据完成!!!商品数据量:{}, 索引数据量:{},失败数:{},共耗时:{}ms", new String[]{
                totalProductCount + "",
                totalIndexCount + "",
                totalExceptionCount + "",
                (System.currentTimeMillis() - startTime) + ""
        });
    }


    /**
     * 索引产品数据
     *
     * @param goodsPoList 商品对象
     * @return 索引成功数
     */
    public int indexGoodsData(List<BaseGoodsSpuPo> goodsPoList) {
        if (null == goodsPoList || goodsPoList.size() == 0) {
            log.warn(CLASS_LOG_PREFIX + "----查询到商品列表数据为空");
            return 0;
        }
        String indexName = StringUtils.isEmpty(elasticSearchConfig.getReIndexGoodsDataIndexName()) ? IndexConstant.INDEX_PRODUCT_INFO_GOODS : elasticSearchConfig.getReIndexGoodsDataIndexName();
        log.info("The name of indexGoodsData {} ", indexName);
        //失败数
        int failCount = 0;

        log.info(CLASS_LOG_PREFIX + "批量提交数据对象,当前商品总条数:{}.", goodsPoList.size());
        //批量提交数据对象
        List<Object> bulkIndexList = new ArrayList<>(IndexInfoQueryConfig.DEFAULT_QUERY_PRODUCTPOINFO_LIMIT);

        /********************************** 封装商品列表索引数据 *********************************/
        for (BaseGoodsSpuPo baseGoodsSpuPo : goodsPoList) {

            GoodsIndexMappingData goodsIndexMappingData = new GoodsIndexMappingData();

            //处理商品基础数据
            goodsIndexMappingData.setId(baseGoodsSpuPo.getId());
            goodsIndexMappingData.setGoodsCompanyId(baseGoodsSpuPo.getCompanyId());
            goodsIndexMappingData.setGoodsGetTime(baseGoodsSpuPo.getGetTime());
            goodsIndexMappingData.setDataIsDelete(baseGoodsSpuPo.getDataIsDelete());
            goodsIndexMappingData.setGoodsGmtCreatetime(baseGoodsSpuPo.getGmtCreate());
            goodsIndexMappingData.setGoodsGmtModified(baseGoodsSpuPo.getGmtModified());
            SpuSaleInfoPo spuInfo = spuInfoMetaDataStorage.getSpuInfoBySpuId(baseGoodsSpuPo.getId());
            if (null != spuInfo) {
                goodsIndexMappingData.setGoodsisPreSell(spuInfo.getIsPresell());
                goodsIndexMappingData.setGoodsIsSpecialOffer(spuInfo.getIsSpecialOffer());
            } else {
                goodsIndexMappingData.setGoodsisPreSell(0);
                goodsIndexMappingData.setGoodsIsSpecialOffer(0);
            }

            goodsIndexMappingData.setGoodsIsPutaway(baseGoodsSpuPo.getIsPutaway());
            goodsIndexMappingData.setGoodsSpuName(baseGoodsSpuPo.getSpuName());
            goodsIndexMappingData.setGoodsSpuPicId(baseGoodsSpuPo.getPicId());
            goodsIndexMappingData.setGoodsSpuPicIds(baseGoodsSpuPo.getPicIds());

            //商品商品的封面图片数据
            goodsIndexMappingData.setGoodsSpuPicPath(baseGoodsSpuPo.getPicPath());
            Integer picId = goodsIndexMappingData.getGoodsSpuPicId();
            if (null != picId && 0 != picId) {
                goodsIndexMappingData.setGoodsSpuPicPath(resPicMetaDataStorage.getPicPathByPicId(picId));
            }

            //处理商品的图片列表数据
            String goodsSpuPicIds = goodsIndexMappingData.getGoodsSpuPicIds();
            if (!StringUtils.isEmpty(goodsSpuPicIds)) {
                String[] picIdArr = goodsSpuPicIds.split(",");
                List<Integer> picIdList = new ArrayList<>(picIdArr.length);
                for (String id : picIdArr) {
                    picIdList.add(Integer.valueOf(id));
                }
                if (null != picIdList && picIdList.size() > 0) {
                    List<String> list = resPicMetaDataStorage.queryPicPathListByPicIdList(picIdList, null);
                    goodsIndexMappingData.setGoodsSpuPicPathList(list);
                }
            }

            //处理商品中每一个单元的sku数据
            List<BaseGoodsSkuPo> baseGoodsSkuPoList = new ArrayList<>(goodsPoList.size());
            //当商品表spu对应的每一个baseproduct数据
            List<BaseProductDataPo> baseProductDataPoList = baseProductDataStorage.getBaseProductListBySpuId(baseGoodsSpuPo.getId());
            if (null != baseProductDataPoList && baseProductDataPoList.size() > 0) {
                for (BaseProductDataPo baseProductDataPo : baseProductDataPoList) {
                    BaseGoodsSkuPo baseGoodsSkuPo = new BaseGoodsSkuPo();
                    EntityCopyUtils.copyData(baseProductDataPo, baseGoodsSkuPo);
                    baseGoodsSkuPo.setPrice(goodsSkuMetaDataStorage.getGoodsSkuPriceBySpuId(baseGoodsSpuPo.getId(), baseGoodsSkuPo.getProductId()));
                    baseGoodsSkuPoList.add(baseGoodsSkuPo);
                }

                goodsIndexMappingData.setGoodsDefaultPrice(goodsSkuMetaDataStorage.getGoodsDefaultPrice(baseGoodsSkuPoList));


                for (BaseGoodsSkuPo baseGoodsSkuPo : baseGoodsSkuPoList) {

                    /*************** Field4:产品全平台过滤数据 *******************/
                    ProductPlatformData productPlatformData = productPlatformMetaDataStorage.queryProductPlatformByProductId(baseGoodsSkuPo.getProductId());
                    if (null != productPlatformData) {
                        //平台数据
                        EntityCopyUtils.copyData(productPlatformData, baseGoodsSkuPo);
                        //平台编码列表
                        List<String> platformCodeList = new ArrayList<>(9);
                        if (null != productPlatformData.getPlatformProductToBMobile()) {
                            platformCodeList.add(productPlatformData.getPlatformProductToBMobile().getPlatformCode());
                        }
                        if (null != productPlatformData.getPlatformProductToBPc()) {
                            platformCodeList.add(productPlatformData.getPlatformProductToBPc().getPlatformCode());
                        }
                        if (null != productPlatformData.getPlatformProductToCSite()) {
                            platformCodeList.add(productPlatformData.getPlatformProductToCSite().getPlatformCode());
                        }
                        if (null != productPlatformData.getPlatformProductToCMobile()) {
                            platformCodeList.add(productPlatformData.getPlatformProductToCMobile().getPlatformCode());
                        }
                        if (null != productPlatformData.getPlatformProductSanduManager()) {
                            platformCodeList.add(productPlatformData.getPlatformProductSanduManager().getPlatformCode());
                        }
                        if (null != productPlatformData.getPlatformProductHouseDraw()) {
                            platformCodeList.add(productPlatformData.getPlatformProductHouseDraw().getPlatformCode());
                        }
                        if (null != productPlatformData.getPlatformProductMerchantsManager()) {
                            platformCodeList.add(productPlatformData.getPlatformProductMerchantsManager().getPlatformCode());
                        }
                        if (null != productPlatformData.getPlatformProductTest()) {
                            platformCodeList.add(productPlatformData.getPlatformProductTest().getPlatformCode());
                        }
                        if (null != productPlatformData.getPlatformProductU3dPlugin()) {
                            platformCodeList.add(productPlatformData.getPlatformProductU3dPlugin().getPlatformCode());
                        }
                        if (null != productPlatformData.getPlatformProductMiniProgram()) {
                            platformCodeList.add(productPlatformData.getPlatformProductMiniProgram().getPlatformCode());
                        }
                        baseGoodsSkuPo.setPlatformCodeList(platformCodeList);
                    }

                    /***************产品分类数据 *******************/
                    baseGoodsSkuPo.setAllProductCategoryId(productCategoryRelMetaDataStorage.getCategoryListByProductId(baseGoodsSkuPo.getProductId()));
                    baseGoodsSkuPo.setAllProductCategoryName(productCategoryMetaDataStorage.queryProductCategoryIdByProductId(baseGoodsSkuPo.getAllProductCategoryId()));
                    if (null != baseGoodsSkuPo.getAllProductCategoryId() && baseGoodsSkuPo.getAllProductCategoryId().size() > 0) {
                        Integer categoryId = baseGoodsSkuPo.getAllProductCategoryId().get(0);
                        baseGoodsSkuPo.setProductCategoryId(categoryId);
                    }
                    ProductCategoryPo productCategoryPo = productCategoryMetaDataStorage.getProductCategory(baseGoodsSkuPo.getProductCategoryId());
                    if (null != productCategoryPo) {
                        //Step 1: 确定当前产品分类层级

                        if (null == productCategoryPo) {
                            log.warn(CLASS_LOG_PREFIX + "产品分类数据-确定当前产品分类层级-ProductCategoryPo is null.ProductIndexMappingData:{}.", baseGoodsSkuPo.toString());
                            failCount++;
                            continue;
                        }
                        baseGoodsSkuPo.setProductCategoryCode(productCategoryPo.getProductCategoryCode());
                        //产品长编码列表
                        baseGoodsSkuPo.setProductCategoryLongCodeList(
                                productCategoryMetaDataStorage.queryProductCategoryLongCodeByCategoryIdList(
                                        baseGoodsSkuPo.getAllProductCategoryId()));
                        baseGoodsSkuPo.setParentCategoryId(productCategoryPo.getParentCategoryId());
                        baseGoodsSkuPo.setCategoryName(productCategoryPo.getCategoryName());
                        baseGoodsSkuPo.setCategoryLevel(productCategoryPo.getCategoryLevel());
                        baseGoodsSkuPo.setCategoryOrder(productCategoryPo.getCategoryOrder());
                        baseGoodsSkuPo.setCategorySystemCode(productCategoryPo.getCategorySystemCode());

                        baseGoodsSkuPo.setProductCategoryLongCodeList2(
                                productCategoryMetaDataStorage.queryProductCategoryLongCodeByCategoryIdList(
                                        baseGoodsSkuPo.getAllProductCategoryId()));
                        baseGoodsSkuPo.setParentCategoryId(productCategoryPo.getParentCategoryId());
                        baseGoodsSkuPo.setCategoryName(productCategoryPo.getCategoryName());
                        baseGoodsSkuPo.setCategoryLevel(productCategoryPo.getCategoryLevel());
                        baseGoodsSkuPo.setCategoryOrder(productCategoryPo.getCategoryOrder());
                        baseGoodsSkuPo.setCategorySystemCode(productCategoryPo.getCategorySystemCode());

                        //Step 2: 根据不同层级装配数据
                        ProductCategoryPo firstLevelProductCategoryPo, secondLevelProductCategoryPo, thirdLevelProductCategoryPo, fourthLevelProductCategoryPo;
                        switch (productCategoryPo.getCategoryLevel()) {
                            case 2:
                                //二级对象
                                baseGoodsSkuPo.setSecondLevelCategoryId(productCategoryPo.getId());
                                baseGoodsSkuPo.setSecondLevelCategoryName(productCategoryPo.getCategoryName());

                                //一级对象
                                firstLevelProductCategoryPo = productCategoryMetaDataStorage.getProductCategory(productCategoryPo.getParentCategoryId());
                                baseGoodsSkuPo.setFirstLevelCategoryId(firstLevelProductCategoryPo.getId());
                                baseGoodsSkuPo.setFirstLevelCategoryName(firstLevelProductCategoryPo.getCategoryName());
                                //每级数据添加
                                baseGoodsSkuPo.setAllLevelCategoryName(Arrays.asList(
                                        baseGoodsSkuPo.getFirstLevelCategoryName(),
                                        baseGoodsSkuPo.getSecondLevelCategoryName()));
                                break;
                            case 3:
                                //三级对象
                                baseGoodsSkuPo.setThirdLevelCategoryId(productCategoryPo.getId());
                                baseGoodsSkuPo.setThirdLevelCategoryName(productCategoryPo.getCategoryName());
                                //二级对象
                                secondLevelProductCategoryPo = productCategoryMetaDataStorage.getProductCategory(productCategoryPo.getParentCategoryId());
                                baseGoodsSkuPo.setSecondLevelCategoryId(secondLevelProductCategoryPo.getId());
                                baseGoodsSkuPo.setSecondLevelCategoryName(secondLevelProductCategoryPo.getCategoryName());
                                //一级对象
                                firstLevelProductCategoryPo = productCategoryMetaDataStorage.getProductCategory(secondLevelProductCategoryPo.getParentCategoryId());
                                baseGoodsSkuPo.setFirstLevelCategoryId(firstLevelProductCategoryPo.getId());
                                baseGoodsSkuPo.setFirstLevelCategoryName(firstLevelProductCategoryPo.getCategoryName());
                                //每级数据添加
                                baseGoodsSkuPo.setAllLevelCategoryName(Arrays.asList(
                                        baseGoodsSkuPo.getFirstLevelCategoryName(),
                                        baseGoodsSkuPo.getSecondLevelCategoryName(),
                                        baseGoodsSkuPo.getThirdLevelCategoryName()));
                                break;
                            case 4:
                                //四级对象
                                baseGoodsSkuPo.setFourthLevelCategoryId(productCategoryPo.getId());
                                baseGoodsSkuPo.setFourthLevelCategoryName(productCategoryPo.getCategoryName());
                                //三级对象
                                thirdLevelProductCategoryPo = productCategoryMetaDataStorage.getProductCategory(productCategoryPo.getParentCategoryId());
                                baseGoodsSkuPo.setThirdLevelCategoryId(thirdLevelProductCategoryPo.getId());
                                baseGoodsSkuPo.setThirdLevelCategoryName(thirdLevelProductCategoryPo.getCategoryName());
                                //二级对象
                                secondLevelProductCategoryPo = productCategoryMetaDataStorage.getProductCategory(thirdLevelProductCategoryPo.getParentCategoryId());
                                baseGoodsSkuPo.setSecondLevelCategoryId(secondLevelProductCategoryPo.getId());
                                baseGoodsSkuPo.setSecondLevelCategoryName(secondLevelProductCategoryPo.getCategoryName());
                                //一级对象
                                firstLevelProductCategoryPo = productCategoryMetaDataStorage.getProductCategory(secondLevelProductCategoryPo.getParentCategoryId());
                                baseGoodsSkuPo.setFirstLevelCategoryId(firstLevelProductCategoryPo.getId());
                                baseGoodsSkuPo.setFirstLevelCategoryName(firstLevelProductCategoryPo.getCategoryName());
                                //每级数据添加
                                baseGoodsSkuPo.setAllLevelCategoryName(Arrays.asList(
                                        baseGoodsSkuPo.getFirstLevelCategoryName(),
                                        baseGoodsSkuPo.getSecondLevelCategoryName(),
                                        baseGoodsSkuPo.getThirdLevelCategoryName(),
                                        baseGoodsSkuPo.getFourthLevelCategoryName()));
                                break;
                            case 5:
                                //五级对象
                                baseGoodsSkuPo.setFifthLevelCategoryId(productCategoryPo.getId());
                                baseGoodsSkuPo.setFifthLevelCategoryName(productCategoryPo.getCategoryName());
                                //四级对象
                                fourthLevelProductCategoryPo = productCategoryMetaDataStorage.getProductCategory(productCategoryPo.getParentCategoryId());
                                baseGoodsSkuPo.setFourthLevelCategoryId(fourthLevelProductCategoryPo.getId());
                                baseGoodsSkuPo.setFourthLevelCategoryName(fourthLevelProductCategoryPo.getCategoryName());
                                //三级对象
                                thirdLevelProductCategoryPo = productCategoryMetaDataStorage.getProductCategory(fourthLevelProductCategoryPo.getParentCategoryId());
                                baseGoodsSkuPo.setThirdLevelCategoryId(thirdLevelProductCategoryPo.getId());
                                baseGoodsSkuPo.setThirdLevelCategoryName(thirdLevelProductCategoryPo.getCategoryName());
                                //二级对象
                                secondLevelProductCategoryPo = productCategoryMetaDataStorage.getProductCategory(thirdLevelProductCategoryPo.getParentCategoryId());
                                baseGoodsSkuPo.setSecondLevelCategoryId(secondLevelProductCategoryPo.getId());
                                baseGoodsSkuPo.setSecondLevelCategoryName(secondLevelProductCategoryPo.getCategoryName());
                                //一级对象
                                firstLevelProductCategoryPo = productCategoryMetaDataStorage.getProductCategory(secondLevelProductCategoryPo.getParentCategoryId());
                                baseGoodsSkuPo.setFirstLevelCategoryId(firstLevelProductCategoryPo.getId());
                                baseGoodsSkuPo.setFirstLevelCategoryName(firstLevelProductCategoryPo.getCategoryName());
                                //每级数据添加
                                baseGoodsSkuPo.setAllLevelCategoryName(Arrays.asList(
                                        baseGoodsSkuPo.getFirstLevelCategoryName(),
                                        baseGoodsSkuPo.getSecondLevelCategoryName(),
                                        baseGoodsSkuPo.getThirdLevelCategoryName(),
                                        baseGoodsSkuPo.getFourthLevelCategoryName(),
                                        baseGoodsSkuPo.getFifthLevelCategoryName()));
                                break;
                            default:
                                failCount++;
                                log.error(CLASS_LOG_PREFIX + "根据不同层级装配不同分类数据异常!未知分类层级Level:{}", productCategoryPo.getCategoryLevel());
                        }
                    }
                }
            }
            goodsIndexMappingData.setGoodsSkuPoList(baseGoodsSkuPoList);


            //处理对象空值
            goodsIndexMappingData = (GoodsIndexMappingData) EntityUtil.setEntityNullValueToZeroForintegerAndDouble(goodsIndexMappingData);
            if (null == goodsIndexMappingData) {
                continue;
            }

            /*
            * 创建索引对象---分类索引从1级开始建立
            * */

            IndexRequestDTO indexRequestDTO = new IndexRequestDTO(
                    indexName,  //IndexConstant.INDEX_PRODUCT_INFO
                    TypeConstant.TYPE_GOODS,
                    goodsIndexMappingData.getId() + "",
                    JsonUtil.toJson(goodsIndexMappingData)
            );

            //加入批量对象
            bulkIndexList.add(indexRequestDTO);


        }


        //索引数据
        BulkStatistics bulkStatistics = null;
        try {
            bulkStatistics = elasticSearchService.bulk(bulkIndexList, null);
        } catch (ElasticSearchException e) {
            log.error(CLASS_LOG_PREFIX + "索引商品列表数据异常:ElasticSearchException:{}", e);
        }
        log.info(CLASS_LOG_PREFIX + "索引商品列表数据成功:成功索引数:{},无效索引数:{},BulkStatistics:{}", new String[]{
                bulkIndexList.size() + "",
                failCount + "",
                null == bulkStatistics ? null : bulkStatistics.toString()
        });

        return goodsPoList.size() - failCount;

    }





    /**
     * 根据商品ID索引商品数据进行增量更新
     *
     * @param goodsPoList 商品对象
     * @return 索引成功数
     */
    public int indexGoodsDataByGoodsIdList(List<BaseGoodsSpuPo> goodsPoList) {
        if (null == goodsPoList || goodsPoList.size() == 0) {
            log.warn(CLASS_LOG_PREFIX + "----查询到商品列表数据为空");
            return 0;
        }
        String indexName = StringUtils.isEmpty(elasticSearchConfig.getReIndexGoodsDataIndexName()) ? IndexConstant.INDEX_PRODUCT_INFO_GOODS : elasticSearchConfig.getReIndexGoodsDataIndexName();
        log.info("The name of indexGoodsData {} ", indexName);
        //失败数
        int failCount = 0;

        log.info(CLASS_LOG_PREFIX + "批量提交数据对象,当前商品总条数:{}.", goodsPoList.size());
        //批量提交数据对象
        List<Object> bulkIndexList = new ArrayList<>(IndexInfoQueryConfig.DEFAULT_QUERY_PRODUCTPOINFO_LIMIT);

        /********************************** 封装商品列表索引数据 *********************************/
        for (BaseGoodsSpuPo baseGoodsSpuPo : goodsPoList) {

            GoodsIndexMappingData goodsIndexMappingData = new GoodsIndexMappingData();

            //封装商品基础数据
            goodsIndexMappingData.setId(baseGoodsSpuPo.getId());
            goodsIndexMappingData.setGoodsCompanyId(baseGoodsSpuPo.getCompanyId());
            goodsIndexMappingData.setGoodsGetTime(baseGoodsSpuPo.getGetTime());
            goodsIndexMappingData.setGoodsGmtCreatetime(baseGoodsSpuPo.getGmtCreate());
            goodsIndexMappingData.setGoodsGmtModified(baseGoodsSpuPo.getGmtModified());
            goodsIndexMappingData.setGoodsIsPutaway(baseGoodsSpuPo.getIsPutaway());
            goodsIndexMappingData.setGoodsSpuName(baseGoodsSpuPo.getSpuName());
            goodsIndexMappingData.setGoodsSpuPicId(baseGoodsSpuPo.getPicId());
            goodsIndexMappingData.setGoodsSpuPicIds(baseGoodsSpuPo.getPicIds());
            //从数据库查询该商品预售信息.
            SpuSaleInfoPo spuInfo = spuInfoMetaDataStorage.querySpuInfoBySpuId(baseGoodsSpuPo.getId());
            if (null != spuInfo) {
                goodsIndexMappingData.setGoodsisPreSell(spuInfo.getIsPresell());
                goodsIndexMappingData.setGoodsIsSpecialOffer(spuInfo.getIsSpecialOffer());
            } else {
                goodsIndexMappingData.setGoodsisPreSell(0);
                goodsIndexMappingData.setGoodsIsSpecialOffer(0);
            }



            //从数据库查询商品的封面图片数据
            Integer picId = goodsIndexMappingData.getGoodsSpuPicId();
            if (null != picId && 0 != picId) {
                ResPicPo resPicPo = resPicMetaDataStorage.queryPicPathByPicId(picId);
                if(null != resPicPo){
                    goodsIndexMappingData.setGoodsSpuPicPath(resPicPo.getPicPath());
                }

            }

            //从数据库查询商品的图片列表数据
            String goodsSpuPicIds = goodsIndexMappingData.getGoodsSpuPicIds();
            if (!StringUtils.isEmpty(goodsSpuPicIds)) {
                String[] picIdArr = goodsSpuPicIds.split(",");
                List<Integer> picIdList = new ArrayList<>(picIdArr.length);
                for (String id : picIdArr) {
                    picIdList.add(Integer.valueOf(id));
                }
                if (null != picIdList && picIdList.size() > 0) {
                    List<String> list = resPicMetaDataStorage.queryPicPathList(picIdList);
                    goodsIndexMappingData.setGoodsSpuPicPathList(list);
                }
            }


            //从数据库查询商品中每一个单元的sku数据
            List<BaseGoodsSkuPo> baseGoodsSkuPoList = new ArrayList<>(goodsPoList.size());
            List<BaseProductDataPo> baseProductDataPoList = baseProductDataStorage.queryBaseProductDataListById(baseGoodsSpuPo.getId());
            if (null != baseProductDataPoList && baseProductDataPoList.size() > 0) {
                for (BaseProductDataPo baseProductDataPo : baseProductDataPoList) {
                    BaseGoodsSkuPo baseGoodsSkuPo = new BaseGoodsSkuPo();
                    EntityCopyUtils.copyData(baseProductDataPo, baseGoodsSkuPo);
                    GoodsSkuPo goodsSkuPo = goodsSkuMetaDataStorage.getGoodsSkuPriceById(baseGoodsSpuPo.getId(), baseGoodsSkuPo.getProductId());
                    if(null!=goodsSkuPo){
                        baseGoodsSkuPo.setPrice(goodsSkuPo.getPrice());
                    }
                    baseGoodsSkuPoList.add(baseGoodsSkuPo);
                }
                //处理商品默认价格
                goodsIndexMappingData.setGoodsDefaultPrice(goodsSkuMetaDataStorage.getGoodsDefaultPrice(baseGoodsSkuPoList));

                for (BaseGoodsSkuPo baseGoodsSkuPo : baseGoodsSkuPoList) {
                    /*************** Field4:产品2C平台数据 *******************/
                    ProductPlatformRelPo productPlatformRelPo = productPlatformMetaDataStorage.queryProductPlatformById(baseGoodsSkuPo.getProductId());
                    if (null != productPlatformRelPo) {
                        //平台数据
                        baseGoodsSkuPo.setPlatformProductMiniProgram(productPlatformRelPo);
                        //平台编码列表
                        List<String> platformCodeList = new ArrayList<>(1);
                        if (null != productPlatformRelPo) {
                            platformCodeList.add(productPlatformRelPo.getPlatformCode());
                        }
                        baseGoodsSkuPo.setPlatformCodeList(platformCodeList);
                    }

                    /***************从数据库获取该产品所有分类数据 *******************/
                    List<ProductCategoryPo> productCategoryPoList = productCategoryRelMetaDataStorage.queryCategoryListByProductId(baseGoodsSkuPo.getProductId());
                    if(null != productCategoryPoList && productCategoryPoList.size() > 0 ){
                        List<ProductCategoryPo> firstLevelCategoryList = new ArrayList<>();
                        List<ProductCategoryPo> secondLevelCategoryList = new ArrayList<>();
                        List<ProductCategoryPo> thirdLevelCategoryList = new ArrayList<>();
                        List<ProductCategoryPo> fourthLevelCategoryList = new ArrayList<>();
                        List<ProductCategoryPo> fifthLevelCategoryList = new ArrayList<>();
                        List<String> allfifthLevelCategoryName = new ArrayList<>();
                        List<Integer> allfifthLevelCategoryId = new ArrayList<>();
                        List<String> allfifthLevelCategoryCode = new ArrayList<>();
                        for(ProductCategoryPo productCategoryPo : productCategoryPoList){
                            switch (productCategoryPo.getCategoryLevel()) {
                                case 1:
                                    firstLevelCategoryList.add(productCategoryPo);
                                break;

                                case 2:
                                    secondLevelCategoryList.add(productCategoryPo);
                                    break;

                                case 3:
                                    thirdLevelCategoryList.add(productCategoryPo);
                                    break;

                                case 4:
                                    fourthLevelCategoryList.add(productCategoryPo);
                                    break;

                                case 5:
                                    fifthLevelCategoryList.add(productCategoryPo);
                                    allfifthLevelCategoryName.add(productCategoryPo.getCategoryName());
                                    allfifthLevelCategoryId.add(productCategoryPo.getId());
                                    allfifthLevelCategoryCode.add(productCategoryPo.getProductCategoryLongCode());
                                    break;

                                default:
                                log.error(CLASS_LOG_PREFIX + "根据不同层级装配不同分类数据异常!未知分类层级Level:{}", productCategoryPo.getCategoryLevel());
                            }
                        }

                        if(null != allfifthLevelCategoryName && allfifthLevelCategoryName.size()>0){
                            baseGoodsSkuPo.setAllProductCategoryName(allfifthLevelCategoryName);
                        }

                        if(null != allfifthLevelCategoryId && allfifthLevelCategoryId.size()>0){
                            baseGoodsSkuPo.setAllProductCategoryId(allfifthLevelCategoryId);
                        }

                        if(null != allfifthLevelCategoryCode && allfifthLevelCategoryCode.size()>0){
                            baseGoodsSkuPo.setProductCategoryLongCodeList(allfifthLevelCategoryCode);
                            baseGoodsSkuPo.setProductCategoryLongCodeList2(allfifthLevelCategoryCode);
                        }


                        List<String> allLevelCategoryName = new ArrayList<>();

                        if(null != firstLevelCategoryList && firstLevelCategoryList.size() >0){
                            baseGoodsSkuPo.setFirstLevelCategoryId(firstLevelCategoryList.get(0).getId());
                            baseGoodsSkuPo.setFirstLevelCategoryName(firstLevelCategoryList.get(0).getCategoryName());
                            allLevelCategoryName.add(firstLevelCategoryList.get(0).getCategoryName());
                        }
                        if(null != secondLevelCategoryList && secondLevelCategoryList.size() >0){
                            baseGoodsSkuPo.setSecondLevelCategoryId(secondLevelCategoryList.get(0).getId());
                            baseGoodsSkuPo.setSecondLevelCategoryName(secondLevelCategoryList.get(0).getCategoryName());
                            allLevelCategoryName.add(secondLevelCategoryList.get(0).getCategoryName());
                        }
                        if(null != thirdLevelCategoryList && thirdLevelCategoryList.size() >0){
                            baseGoodsSkuPo.setThirdLevelCategoryId(thirdLevelCategoryList.get(0).getId());
                            baseGoodsSkuPo.setThirdLevelCategoryName(thirdLevelCategoryList.get(0).getCategoryName());
                            allLevelCategoryName.add(thirdLevelCategoryList.get(0).getCategoryName());
                        }
                        if(null != fourthLevelCategoryList && fourthLevelCategoryList.size() >0){
                            baseGoodsSkuPo.setFourthLevelCategoryId(fourthLevelCategoryList.get(0).getId());
                            baseGoodsSkuPo.setFourthLevelCategoryName(fourthLevelCategoryList.get(0).getCategoryName());
                            allLevelCategoryName.add(fourthLevelCategoryList.get(0).getCategoryName());
                        }
                        if(null != fifthLevelCategoryList && fifthLevelCategoryList.size() >0){
                            baseGoodsSkuPo.setFifthLevelCategoryId(fifthLevelCategoryList.get(0).getId());
                            baseGoodsSkuPo.setFifthLevelCategoryName(fifthLevelCategoryList.get(0).getCategoryName());
                            baseGoodsSkuPo.setProductCategoryId(fifthLevelCategoryList.get(0).getId());
                            baseGoodsSkuPo.setProductCategoryCode(fifthLevelCategoryList.get(0).getProductCategoryCode());
                            baseGoodsSkuPo.setParentCategoryId(fifthLevelCategoryList.get(0).getParentCategoryId());
                            baseGoodsSkuPo.setCategoryName(fifthLevelCategoryList.get(0).getCategoryName());
                            baseGoodsSkuPo.setCategoryLevel(fifthLevelCategoryList.get(0).getCategoryLevel());
                            baseGoodsSkuPo.setCategoryOrder(fifthLevelCategoryList.get(0).getCategoryOrder());
                            baseGoodsSkuPo.setCategorySystemCode(fifthLevelCategoryList.get(0).getCategorySystemCode());
                            allLevelCategoryName.add(fifthLevelCategoryList.get(0).getCategoryName());

                        }

                        if(null != allLevelCategoryName && allLevelCategoryName.size() >0){
                            baseGoodsSkuPo.setAllLevelCategoryName(allLevelCategoryName);
                        }



                    }



                }
            }
            goodsIndexMappingData.setGoodsSkuPoList(baseGoodsSkuPoList);


            //处理对象空值
            goodsIndexMappingData = (GoodsIndexMappingData) EntityUtil.setEntityNullValueToZeroForintegerAndDouble(goodsIndexMappingData);
            if (null == goodsIndexMappingData) {
                continue;
            }


                       /*
            * 创建索引对象---分类索引从1级开始建立
            * */

            IndexRequestDTO indexRequestDTO = new IndexRequestDTO(
                    indexName,  //IndexConstant.INDEX_PRODUCT_INFO
                    TypeConstant.TYPE_GOODS,
                    goodsIndexMappingData.getId() + "",
                    JsonUtil.toJson(goodsIndexMappingData)
            );

            //加入批量对象
            bulkIndexList.add(indexRequestDTO);


        }


        //索引数据
        BulkStatistics bulkStatistics = null;
        try {
            bulkStatistics = elasticSearchService.bulk(bulkIndexList, null);
        } catch (ElasticSearchException e) {
            log.error(CLASS_LOG_PREFIX + "索引商品列表数据异常:ElasticSearchException:{}", e);
        }
        log.info(CLASS_LOG_PREFIX + "索引商品列表数据成功:成功索引数:{},无效索引数:{},BulkStatistics:{}", new String[]{
                bulkIndexList.size() + "",
                failCount + "",
                null == bulkStatistics ? null : bulkStatistics.toString()
        });

        return goodsPoList.size() - failCount;

    }

















}
