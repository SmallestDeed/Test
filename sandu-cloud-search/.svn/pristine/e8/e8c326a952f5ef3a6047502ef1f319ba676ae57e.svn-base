package com.sandu.search.storage.product;

import com.google.common.collect.Ordering;
import com.google.gson.reflect.TypeToken;
import com.sandu.search.common.constant.RedisConstant;
import com.sandu.search.common.constant.SystemDictionaryType;
import com.sandu.search.common.tools.JsonUtil;
import com.sandu.search.common.tools.StringUtil;
import com.sandu.search.entity.elasticsearch.po.ProductCategoryPo;
import com.sandu.search.entity.elasticsearch.po.metadate.SystemDictionaryPo;
import com.sandu.search.entity.product.dto.ProductTypeCategory;
import com.sandu.search.entity.product.vo.ProductCategoryVo;
import com.sandu.search.exception.MetaDataException;
import com.sandu.search.service.metadata.MetaDataService;
import com.sandu.search.service.redis.RedisService;
import com.sandu.search.storage.system.SystemDictionaryMetaDataStorage;

import lombok.extern.log4j.Log4j2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Nullable;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static java.util.stream.Collectors.toList;

/**
 * 产品分类元数据存储
 *
 * @date 20171213
 * @auth pengxuangang
 */
@Component
@Log4j2
public class ProductCategoryMetaDataStorage {

    //最后更新时间
    private static long lastUpdateTime = System.currentTimeMillis();

    private final RedisService redisService;
    
    private final SystemDictionaryMetaDataStorage systemDictionaryMetaDataStorage;

    private final MetaDataService metaDataService;

    private final static String CLASS_LOG_PREFIX = "[产品分类元数据存储]:";

    @Autowired
    public ProductCategoryMetaDataStorage(MetaDataService metaDataService, RedisService redisService, SystemDictionaryMetaDataStorage systemDictionaryMetaDataStorage) {
        this.redisService = redisService;
        this.systemDictionaryMetaDataStorage = systemDictionaryMetaDataStorage;
        this.metaDataService = metaDataService;
    }

    //产品分类Map元数据
    private Map<String, String> productCategoryPoMap = null;
    //产品父子分类关系Map元数据<parentCategoryId, List<childrenCategoryId>>
    private Map<String, String> parentChildrenCategoryMap = null;
    //产品分类编码找产品分类ID
    private Map<String, String> categoryCodeMap = null;
    //产品分类长编码
    private List<String> categoryLongCodeList = null;
    //分类对象Map<Code, ProductCategoryVo>
//    private Map<String, String> productCategoryVoMap = new WeakHashMap<>();
    /**
        update by steve, Because  an entry in a WeakHashMap will automatically be removed when its key is no longer in ordinary use.
        So this is not stable, Can't support high performance for aggs data. So temporary changed it to ConcurrentHashMap.
        TODO: Plan to update this to google guava cache.
    **/
    private Map<String, String> productCategoryVoMap = new ConcurrentHashMap<String, String>();
    //获取Map数据方法兼容(时间不能大于5分钟)
    private String getMap(String mapName, String keyName) {
        //更新数据
        if (null == productCategoryPoMap || 0 >= productCategoryPoMap.size()
                || null == parentChildrenCategoryMap || 0 >= parentChildrenCategoryMap.size()
                || null == categoryCodeMap || 0 >= categoryCodeMap.size()
                || System.currentTimeMillis() - lastUpdateTime > 5*60*1000) {
            productCategoryPoMap = redisService.getMap(RedisConstant.PRODUCT_CATEGORY_DATA);
            parentChildrenCategoryMap = redisService.getMap(RedisConstant.PRODUCT_PARENT_CHILD_CATEGORY_DATA);
            categoryCodeMap = redisService.getMap(RedisConstant.PRODUCT_CATEGORY_CODE_AND_ID_DATA);
            //更新时间
            lastUpdateTime = System.currentTimeMillis();
        }

        //获取数据
        if (RedisConstant.PRODUCT_CATEGORY_DATA.equals(mapName)) {
            return productCategoryPoMap.get(keyName);
        } else if (RedisConstant.PRODUCT_PARENT_CHILD_CATEGORY_DATA.equals(mapName)) {
            return parentChildrenCategoryMap.get(keyName);
        } else if (RedisConstant.PRODUCT_CATEGORY_CODE_AND_ID_DATA.equals(mapName)) {
            return categoryCodeMap.get(keyName);
        } else if (RedisConstant.PRODUCT_CATEGORY_CODE_AND_VO_DATA.equals(mapName)) {

            //检查内存
            if (productCategoryVoMap.containsKey(keyName)) {
                return productCategoryVoMap.get(keyName);
            }

            //加载缓存
            String productCategoryVoJsonStr = redisService.getMap(RedisConstant.PRODUCT_CATEGORY_CODE_AND_VO_DATA, keyName);
            if (StringUtils.isEmpty(productCategoryVoJsonStr)) {
                return null;
            }

            //存入内存
            productCategoryVoMap.put(keyName, productCategoryVoJsonStr);
            return productCategoryVoJsonStr;
        }

        return null;
    }

    //获取List数据方法兼容
    private List<String> getList(String listName) {
        //缓存模式
        if (null == categoryLongCodeList || 0 >= categoryLongCodeList.size()) {
            categoryLongCodeList = redisService.getList(RedisConstant.PRODUCT_CATEGORY_LONG_CODE_DATA);
        }

        if (RedisConstant.PRODUCT_CATEGORY_LONG_CODE_DATA.equals(listName)) {
            return categoryLongCodeList;
        }
        return null;
    }

    /**
     * 更新静态变量categoryLongCodeList(所有运营分类信息)
     * 
     * @author huangsongbo
     */
    @Scheduled(fixedDelayString = "${task.productCategory.categoryLongCodeList.incr}")
    private void updateCategoryLongCodeList() {
    	categoryLongCodeList = redisService.getList(RedisConstant.PRODUCT_CATEGORY_LONG_CODE_DATA);
    	log.info("{}静态变量(categoryLongCodeList)更新完毕", CLASS_LOG_PREFIX);
    }
    
    /**
     * 根据分类ID获取产品分类数据
     *
     * @return
     */
    public ProductCategoryPo getProductCategory(Integer productCategoryId) {
        if (null == productCategoryId || 0 == productCategoryId) {
            return null;
        }

        String productCategoryPoStr = getMap(RedisConstant.PRODUCT_CATEGORY_DATA, productCategoryId + "");
        if (StringUtils.isEmpty(productCategoryPoStr)) {
            return null;
        }
        return JsonUtil.fromJson(productCategoryPoStr, ProductCategoryPo.class);
    }

    /**
     * 根据分类Code获取分类ID
     *
     * @return
     */
    public String getProductCategoryCodeById(String productCategoryCode) {
        if (StringUtils.isEmpty(productCategoryCode)) {
            return null;
        }

        String productCategoryPoStr = getMap(RedisConstant.PRODUCT_CATEGORY_CODE_AND_ID_DATA, productCategoryCode);
        if (StringUtils.isEmpty(productCategoryPoStr)) {
            return null;
        }
        return productCategoryPoStr;
    }

    /**
     * 根据父分类ID获取子分类ID列表
     *
     * @return
     */
    public List<Integer> queryChildCategoryIdListByParentCategoryId(Integer parentCategoryId) {
        if (null == parentCategoryId || 0 == parentCategoryId) {
            return null;
        }

        String parentCategoryIdListStr = getMap(RedisConstant.PRODUCT_PARENT_CHILD_CATEGORY_DATA, parentCategoryId + "");
        if (StringUtils.isEmpty(parentCategoryIdListStr)) {
            return null;
        }
        return JsonUtil.fromJson(parentCategoryIdListStr, new TypeToken<List<Integer>>() {}.getType());
    }

    /**
     * 根据分类编码获取产品分类数据
     *
     * @return
     */
    public ProductCategoryPo getProductCategoryByCategoryCode(String productCategoryCode) {
        if (StringUtils.isEmpty(productCategoryCode)) {
            return null;
        }

        //获取分类ID
        String productCategoryId = getProductCategoryCodeById(productCategoryCode);
        if (StringUtils.isEmpty(productCategoryId)) {
            return null;
        }

        //获取分类对象
        return getProductCategory(Integer.parseInt(productCategoryId));
    }

    /**
     * 根据产品分类ID列表获取产品分类长编码列表
     *
     * @param categoryIdList 产品分类ID列表
     * @return
     */
    public List<String> queryProductCategoryLongCodeByCategoryIdList(List<Integer> categoryIdList) {

        if (null == categoryIdList || 0 == categoryIdList.size()) {
            return null;
        }

        List<String> categoryLongCodeList = new ArrayList<>(categoryIdList.size());
        categoryIdList.forEach(categoryId -> {
            if (null != categoryId && 0 != categoryId) {
                ProductCategoryPo productCategory = getProductCategory(categoryId);
                if (null != productCategory) {
                    String productCategoryLongCode = productCategory.getProductCategoryLongCode();
                    if (!StringUtils.isEmpty(productCategoryLongCode)) {
                        categoryLongCodeList.add(productCategoryLongCode);
                    }
                }
            }
        });

        return categoryLongCodeList;
    }

    /**
     * 根据产品分类ID获取所有分类名
     *
     * @param categoryIdList 分类IDList
     * @return
     */
    public List<String> queryProductCategoryIdByProductId(List<Integer> categoryIdList) {
        if (null == categoryIdList || 0 == categoryIdList.size()) {
            return null;
        }

        List<String> categoryNameList = new ArrayList<>(categoryIdList.size());
        categoryIdList.forEach(categoryId -> {
            if (null != categoryId && 0 != categoryId) {
                ProductCategoryPo productCategory = getProductCategory(categoryId);
                if (null != productCategory) {
                    categoryNameList.add(productCategory.getCategoryName());
                }
            }
        });

        return categoryNameList;
    }

    /**
     * 根据部分分类长编码名查询所有包含分类长编码名
     *
     * @param codeName
     * @return
     */
    public List<String> queryAllCategoryCodeByCodeName(String codeName) {

        if (StringUtils.isEmpty(codeName)) {
            return null;
        }

        //使用Map结构是为了去除重复--查询后合并
        Map<String, String> categoryCodeMap = new HashMap<>();

        String[] codeNameArr = codeName.split(",");

        List<String> longCodeList = getList(RedisConstant.PRODUCT_CATEGORY_LONG_CODE_DATA);
        if (null == longCodeList || 0 >= longCodeList.size()) {
            return null;
        }

        for (String nowCodeName : codeNameArr) {
            longCodeList.forEach(productCategoryLongCode -> {
                if (!StringUtils.isEmpty(productCategoryLongCode) && productCategoryLongCode.contains("." + nowCodeName + ".")) {
                    categoryCodeMap.put(productCategoryLongCode, productCategoryLongCode);
                }
            });
        }

        List<String> categoryCodeList = new ArrayList<>(categoryCodeMap.size());
        //转换为List
        if (0 < categoryCodeMap.size()) {
            for (Map.Entry<String, String> entries : categoryCodeMap.entrySet()) {
                categoryCodeList.add(entries.getKey());
            }
        }
        return categoryCodeList;
    }

    /**
     * 根据分类ID列表查询可用分类长编码列表
     * <p>
     * Step：
     * 1.根据3级分类查询出4级，5级所有分类
     * 2.将5级分类的兄弟节点分类去掉(仅保留已选择同级分类)
     * 3.查询公司可使用的分类
     * 4.去掉结果中不包含在公司分类中的分类(合并需要查询的分类和已配置分类)
     *
     * @param thirdLevelCategoryId                产品三级分类ID
     * @param fifthLevelCategoryIdConditionList   产品五级分类ID列表
     * @param companyAliveCategoryIdConditionList 公司有效分类ID列表
     * @return
     */
    public List<Integer> queryAliveCategoryLongCodeByCategoryIdList(int thirdLevelCategoryId, List<Integer> fifthLevelCategoryIdConditionList, List<Integer> companyAliveCategoryIdConditionList) {

        if (null == fifthLevelCategoryIdConditionList || 0 >= fifthLevelCategoryIdConditionList.size() || 0 == thirdLevelCategoryId) {
            return null;
        }

        //可用分类Id列表
        List<Integer> categoryIdList = new ArrayList<>();

        /***************************** 1.根据3级分类查询出4级，5级所有分类 ***************************************/
        //四级分类ID列表
        List<Integer> fourthLevelCategoryIdList = queryCategoryIdListByParentCategoryIdList(Collections.singletonList(thirdLevelCategoryId));
        //五级分类ID列表
        List<Integer> fifthLevelCategoryIdList = queryCategoryIdListByParentCategoryIdList(fifthLevelCategoryIdConditionList);

        //将结果全装入总结果集
        categoryIdList.add(thirdLevelCategoryId);
        categoryIdList.addAll(fourthLevelCategoryIdList);
        categoryIdList.addAll(fifthLevelCategoryIdList);

        /***************************** 2.将5级分类的兄弟节点分类去掉(仅保留已选择同级分类) ***************************************/
        fifthLevelCategoryIdConditionList.forEach(fifthLevelCategoryIdCondition -> {
            //当前选中条件父分类ID
            Integer parentCategoryId = getParentCategoryIdByChildrenCategoryId(fifthLevelCategoryIdCondition);
            //查询出父类下有哪些子分类
            List<Integer> childrenCategoryIdList = queryCategoryIdListByParentCategoryIdList(Collections.singletonList(parentCategoryId));
            //遍历子节点
            if (null != childrenCategoryIdList && 0 < childrenCategoryIdList.size()) {
                //遍历节点，将子节点全部删除
                childrenCategoryIdList.forEach(childrenCategoryId -> {
                    for (Integer categoryId : categoryIdList) {
                        if (categoryId.equals(childrenCategoryId)) {
                            categoryIdList.remove(categoryId);
                            break;
                        }
                    }
                });
                //增加参与条件子节点(这样做每次少遍历一遍列表,提高性能)
                categoryIdList.add(fifthLevelCategoryIdCondition);
            }
        });

        /***************************** 3.查询公司可使用的分类 ***************************************/
        List<Integer> companyAliveCategoryIdList = new ArrayList<>();
        if (null != companyAliveCategoryIdConditionList && 0 < companyAliveCategoryIdConditionList.size()) {
            //加入已配置父分类
            companyAliveCategoryIdList.addAll(companyAliveCategoryIdConditionList);
            //遍历父分类，加入子分类
            companyAliveCategoryIdConditionList.forEach(companyAliveCategoryIdCondition -> {
                //查询第一级分类
                List<Integer> firstCategoryIdList = queryCategoryIdListByParentCategoryIdList(Collections.singletonList(companyAliveCategoryIdCondition));
                if (null != firstCategoryIdList && 0 < firstCategoryIdList.size()) {
                    companyAliveCategoryIdList.addAll(firstCategoryIdList);
                    //查询第二级分类
                    List<Integer> twoCategoryIdList = queryCategoryIdListByParentCategoryIdList(firstCategoryIdList);
                    if (null != twoCategoryIdList && 0 < twoCategoryIdList.size()) {
                        companyAliveCategoryIdList.addAll(twoCategoryIdList);
                        //查询第三级分类
                        List<Integer> threeCategoryIdList = queryCategoryIdListByParentCategoryIdList(twoCategoryIdList);
                        if (null != threeCategoryIdList && 0 < threeCategoryIdList.size()) {
                            companyAliveCategoryIdList.addAll(threeCategoryIdList);
                            //查询第四级分类
                            List<Integer> fourCategoryIdList = queryCategoryIdListByParentCategoryIdList(threeCategoryIdList);
                            if (null != fourCategoryIdList && 0 < fourCategoryIdList.size()) {
                                companyAliveCategoryIdList.addAll(fourCategoryIdList);
                                //查询第五级分类
                                List<Integer> fiveCategoryIdList = queryCategoryIdListByParentCategoryIdList(fourCategoryIdList);
                                if (null != fiveCategoryIdList && 0 < fiveCategoryIdList.size()) {
                                    companyAliveCategoryIdList.addAll(fiveCategoryIdList);
                                }
                            }
                        }
                    }
                }

            });
        }

        /***************************** 4.返回去掉结果中不包含在公司分类中的分类(合并需要查询的分类和已配置分类) ***************************************/
        return categoryIdList.stream().filter(companyAliveCategoryIdList::contains).collect(toList());
    }

    /**
     * 根据分类ID查询分类层级
     *
     * @param categoryId 分类ID
     * @return
     */
    public Integer getCategoryLevelByCategoryId(Integer categoryId) {
        if (null == categoryId || 0 >= categoryId) {
            return null;
        }

        ProductCategoryPo productCategory = getProductCategory(categoryId);
        return null == productCategory ? null : productCategory.getCategoryLevel();
    }

    /**
     * 根据父分类ID查询子分类IDList
     *
     * @param parentCategoryIdList
     * @return
     */
    public List<Integer> queryCategoryIdListByParentCategoryIdList(List<Integer> parentCategoryIdList) {

        if (null == parentCategoryIdList || 0 >= parentCategoryIdList.size()) {
            return null;
        }

        List<Integer> childrenCategoryIdList = new ArrayList<>();
        parentCategoryIdList.forEach(parentCategoryId -> {
            List<Integer> childCategoryIdList = queryChildCategoryIdListByParentCategoryId(parentCategoryId);
            if (null != childCategoryIdList && 0 < childCategoryIdList.size()) {
                childrenCategoryIdList.addAll(childCategoryIdList);
            }
        });

        return childrenCategoryIdList;
    }

    /**
     * 根据子分类ID查询父分类ID
     *
     * @param childrenId 子分类ID
     * @return
     */
    private Integer getParentCategoryIdByChildrenCategoryId(Integer childrenId) {
        if (null == childrenId || 0 >= childrenId) {
            return null;
        }

        ProductCategoryPo productCategory = getProductCategory(childrenId);
        return null == productCategory ? null : productCategory.getParentCategoryId();
    }


    /**
     * 根据分类ID查询分类编码
     *
     * @param categoryIds 分类ID组
     * @return
     */
    public List<String> queryCategoryCodeByCategoryIds(String[] categoryIds) {

        if (null == categoryIds || 0 >= categoryIds.length) {
            return null;
        }

        //分类code列表
        List<String> categoryCodeList = new ArrayList<>(categoryIds.length);

        for (String categoryIdStr : categoryIds) {
            if (!StringUtils.isEmpty(categoryIdStr)) {
                //分类ID
                int categoryId = Integer.parseInt(categoryIdStr);
                ProductCategoryPo productCategory = getProductCategory(categoryId);
                if (null != productCategory) {
                    categoryCodeList.add(productCategory.getProductCategoryCode());
                }
            }
        }

        return categoryCodeList;
    }

    /**
     * 根据分类编码列表格式化分类信息
     *
     * @param categoryCodeList  分类编码列表
     * @param currentProductSmallType
     * @return
     */
    public List<ProductCategoryVo> formatCategoryInfoByCodeList(List<String> categoryCodeList,Integer currentProductSmallType) {
        long startTime = Calendar.getInstance().getTimeInMillis();
        log.info("{}The startTime for in formatCategoryInfoByCodeList costTime startTime ={}", CLASS_LOG_PREFIX,startTime);
        if (null == categoryCodeList || 0 >= categoryCodeList.size()) {
            return null;
        }

        //三级分类Map<三级分类编码,四级分类编码>
        Map<String, Set<String>> thirdCategoryCodeMap = new HashMap<>();
        //四级分类Map<四级分类编码,五级分类编码>
        Map<String, Set<String>> fourthCategoryCodeMap = new HashMap<>();

        //遍历数据(转为3-4和4-5分类编码Map)
        for (String categoryCode : categoryCodeList) {

            if (StringUtils.isEmpty(categoryCode)) {
                continue;
            }

            String[] categoryCodeSplit = categoryCode.split("\\.");
            //长度为7才是5级分类
            if (null != categoryCodeSplit && 7 == categoryCodeSplit.length) {
                //三/四/五级分类编码
                String thirdCategoryCode = "." + categoryCodeSplit[1] + "." + categoryCodeSplit[2] + "." + categoryCodeSplit[3] + "." + categoryCodeSplit[4] + ".";
                String fourCategoryCode = thirdCategoryCode + categoryCodeSplit[5] + ".";
                String fiveCategoryCode = fourCategoryCode + categoryCodeSplit[6] + ".";

                //三级分类Map
                Set<String> fourCategoryCodeList = new HashSet<>();
                fourCategoryCodeList.add(fourCategoryCode);
                //加入原数据
                if (thirdCategoryCodeMap.containsKey(thirdCategoryCode)) {
                    fourCategoryCodeList.addAll(thirdCategoryCodeMap.get(thirdCategoryCode));
                }
                thirdCategoryCodeMap.put(thirdCategoryCode, fourCategoryCodeList);

                //四级分类Map
                Set<String> fiveCategoryCodeList = new HashSet<>();
                fiveCategoryCodeList.add(fiveCategoryCode);
                //加入原数据
                if (fourthCategoryCodeMap.containsKey(fourCategoryCode)) {
                    fiveCategoryCodeList.addAll(fourthCategoryCodeMap.get(fourCategoryCode));
                }
                fourthCategoryCodeMap.put(fourCategoryCode, fiveCategoryCodeList);
            }
        }
        long endFirstFor = Calendar.getInstance().getTimeInMillis() - startTime;
        log.info("{}The first for in formatCategoryInfoByCodeList costTime{}", CLASS_LOG_PREFIX,endFirstFor);
        //三级分类Map<三级分类编码,四级分类对象>
        Map<String, List<ProductCategoryVo>> thirdProductCategoryVoMap = new HashMap<>();
        //四级分类Map<四级分类编码,五级分类对象>
        Map<String, List<ProductCategoryVo>> fourthProductCategoryVoMap = new HashMap<>();
        //转换为分类对象
        if (null != thirdCategoryCodeMap && 0 < thirdCategoryCodeMap.size()) {
            for (Map.Entry<String, Set<String>> entries : thirdCategoryCodeMap.entrySet()) {
                //四级分类列表
                List<ProductCategoryVo> productCategoryVoList = new ArrayList<>();
                //获取分类数据
                Set<String> codeSet = entries.getValue();
                for (String code : codeSet) {
                    String productCategoryVoJsonStr = getMap(RedisConstant.PRODUCT_CATEGORY_CODE_AND_VO_DATA, code);
                    if (!StringUtils.isEmpty(productCategoryVoJsonStr)) {
                        productCategoryVoList.add(JsonUtil.fromJson(productCategoryVoJsonStr, ProductCategoryVo.class));
                    }
                }

                thirdProductCategoryVoMap.put(entries.getKey(), productCategoryVoList);
            }
        }
        long endSecondFor = Calendar.getInstance().getTimeInMillis() - startTime;
        log.info("{}The second for in formatCategoryInfoByCodeList costTime{}", CLASS_LOG_PREFIX,endSecondFor);
        if (null != fourthCategoryCodeMap && 0 < fourthCategoryCodeMap.size()) {
            for (Map.Entry<String, Set<String>> entries : fourthCategoryCodeMap.entrySet()) {
                //五级分类列表
                List<ProductCategoryVo> productCategoryVoList = new ArrayList<>();
                //获取分类数据
                Set<String> codeSet = entries.getValue();
                for (String code : codeSet) {
                    String productCategoryVoJsonStr = getMap(RedisConstant.PRODUCT_CATEGORY_CODE_AND_VO_DATA, code);
                    if (!StringUtils.isEmpty(productCategoryVoJsonStr)) {
                        productCategoryVoList.add(JsonUtil.fromJson(productCategoryVoJsonStr, ProductCategoryVo.class));
                    }
                }
                fourthProductCategoryVoMap.put(entries.getKey(), productCategoryVoList);
            }
        }
        long endThirdFor = Calendar.getInstance().getTimeInMillis() - startTime;
        log.info("{}The third for in formatCategoryInfoByCodeList costTime{}", CLASS_LOG_PREFIX,endThirdFor);
        //最终返回对象
        List<ProductCategoryVo> productCategoryVoList = null;
        //遍历三级分类构造对象
        if (null != thirdProductCategoryVoMap && 0 < thirdProductCategoryVoMap.size()) {
            productCategoryVoList = new ArrayList<>(thirdProductCategoryVoMap.size());
            for (Map.Entry<String, List<ProductCategoryVo>> entries : thirdProductCategoryVoMap.entrySet()) {
                //获取三级分类对象
                ProductCategoryVo productCategoryVo = getProductCategoryVoByCode(entries.getKey());
                if (productCategoryVo == null) {
                    continue;
                }
                //遍历四级分类对象
                List<ProductCategoryVo> fourProductCategoryVoList = entries.getValue();
                for (ProductCategoryVo fourProductCategoryVo : fourProductCategoryVoList) {
                    //查询5级分类对象
                    List<ProductCategoryVo> fiveProductCategoryVoList = fourthProductCategoryVoMap.get(fourProductCategoryVo.getCategoryCode());
                    //将5级加入4级
                    fourProductCategoryVo.setProductCategoryVoList(fiveProductCategoryVoList);
                }

                //将4级装入3级
                productCategoryVo.setProductCategoryVoList(fourProductCategoryVoList);
                currentProductSmallType = currentProductSmallType != null ?currentProductSmallType:0;
                if(productCategoryVo.getProductSmallTypeValue().intValue() == currentProductSmallType.intValue()) {
                    productCategoryVo.setSortOrder(0);
                }
                //加入列表
                productCategoryVoList.add(productCategoryVo);
            }
        }
        //The new feature WEIXIN-163, The current product aggs category type must display the first in category type list.
        Ordering<ProductCategoryVo> orderingBig = new Ordering<ProductCategoryVo>() {
            @Override
            public int compare(@Nullable ProductCategoryVo productCategoryVo, @Nullable ProductCategoryVo t1) {
                return productCategoryVo.getSortOrder() - t1.getSortOrder();
            }
        };
        productCategoryVoList = orderingBig.sortedCopy(productCategoryVoList);
        long endTotalFor = Calendar.getInstance().getTimeInMillis() - startTime;
        log.info("{}The Total for in formatCategoryInfoByCodeList costTime{}", CLASS_LOG_PREFIX,endTotalFor);

        return productCategoryVoList;
    }

    /**
     * 通过Key查询系统字典数据
     *
     * @param valueKeyList Key列表
     * @return
     */
    public ProductTypeCategory querySystemDictionaryValueListByKeyList(List<String> valueKeyList) {
        if (null == valueKeyList || 0 >= valueKeyList.size()) {
            return null;
        }

        //值列表
        List<Integer> valueList = new ArrayList<>(valueKeyList.size());
        //分类编码
        String categoryCodeKey = null;

        for (String valueKey : valueKeyList) {
            if (!StringUtils.isEmpty(valueKey)) {
                //获取分类编码
                ProductCategoryPo productCategoryPo = getProductCategoryByCategoryCode(valueKey);
                //检查分类级别(3级的分类CODE直接对应小类VALUEKEY，不需要转换)
                switch (productCategoryPo.getCategoryLevel()) {
                    case 3:
                        //直接赋值
                        categoryCodeKey = valueKey;
                        break;
                    case 4:
                        //查询3级分类
                        ProductCategoryPo thereLevelProductCategoryPo = getProductCategory(productCategoryPo.getParentCategoryId());
                        categoryCodeKey = thereLevelProductCategoryPo.getProductCategoryCode();
                        break;
                    case 5:
                        //查询4级分类
                        ProductCategoryPo fourLevelProductCategoryPo = getProductCategory(productCategoryPo.getParentCategoryId());
                        //查询3级分类
                        ProductCategoryPo newThereLevelProductCategoryPo = getProductCategory(fourLevelProductCategoryPo.getParentCategoryId());
                        categoryCodeKey = newThereLevelProductCategoryPo.getProductCategoryCode();
                        break;
                }
                //获取Key值
                if (!StringUtils.isEmpty(categoryCodeKey)) {
                    String value = redisService.getMap(RedisConstant.SYSTEM_DICTIONARY_KEY_VALUE_DATA, categoryCodeKey);
                    if (!StringUtils.isEmpty(value)) {
                        valueList.add(Integer.parseInt(value));
                    }
                }
            }
        }

        return new ProductTypeCategory(categoryCodeKey, valueList);
    }

    /**
     * 通过产品编码获取产品大小类(List第一个是产品大类，第二个是小类)
     *
     * @param productCode 产品编码
     * @return
     */
    public List[] getProductTypeByProductCode(String productCode) {

        if (StringUtils.isEmpty(productCode)) {
            return null;
        }

        //结果集
        List[] productTypeListResult = new List[2];
        //大类集合
        List<Integer> productTypeList = new ArrayList<>(1);
        //小类集合
        List<Integer> productSmallTypeList = new ArrayList<>();

        List<SystemDictionaryPo> systemDictionaryList = systemDictionaryMetaDataStorage.querySystemDictionaryListByType(productCode);

        //若包含此编码的类型则是产品大类
        if (null != systemDictionaryList && 0 < systemDictionaryList.size()) {
            //查询出大类ID值
            //根据类型查询出字典数据
            List<SystemDictionaryPo> systemDictionaryPoList = systemDictionaryMetaDataStorage.querySystemDictionaryListByType(SystemDictionaryType.SYSTEM_DICTIONARY_TYPE_PRODUCTTYPE);
            if (null != systemDictionaryPoList && 0 < systemDictionaryPoList.size()) {
                for (SystemDictionaryPo systemDictionaryPo : systemDictionaryPoList) {
                    if (productCode.equals(systemDictionaryPo.getDictionaryKey())) {
                        //加入大类
                        productTypeList.add(systemDictionaryPo.getDictionaryValue());
                        break;
                    }
                }
            }
            //查询出小类集合
            for (SystemDictionaryPo systemDictionary : systemDictionaryList) {
                productSmallTypeList.add(systemDictionary.getDictionaryValue());
            }
        } else {
            //不包含的则是小类
            //根据分类code查询小类ID
            ProductTypeCategory productTypeCategory = querySystemDictionaryValueListByKeyList(Arrays.asList(productCode.split(",")));
            productSmallTypeList = productTypeCategory.getProductSmallTypeValueList();
            if (null == productSmallTypeList || 0 >= productSmallTypeList.size()) {
                return null;
            }

            //根据小类第一个值查询出大类
            String productCategoryCode = productTypeCategory.getProductCategoryCode();
            String dictionaryType = redisService.getMap(RedisConstant.SYSTEM_DICTIONARY_KEY_TYPE_DATA, productCategoryCode);
            if (!StringUtils.isEmpty(dictionaryType)) {
                //获取到小类的Type去反查大类
                List<SystemDictionaryPo> systemDictionaryPoList = systemDictionaryMetaDataStorage.querySystemDictionaryListByType(SystemDictionaryType.SYSTEM_DICTIONARY_TYPE_PRODUCTTYPE);
                for (SystemDictionaryPo parentSystemDictionaryPo : systemDictionaryPoList) {
                    //检查父类valuekey相同和子类类型是否
                    if (parentSystemDictionaryPo.getDictionaryKey().equals(dictionaryType)) {
                        productTypeList.add(parentSystemDictionaryPo.getDictionaryValue());
                        break;
                    }
                }
            }
            
            List<SystemDictionaryPo> systemDictionaryListSmallType = systemDictionaryMetaDataStorage.querySystemDictionaryListByType(dictionaryType);
            if(systemDictionaryListSmallType != null && systemDictionaryListSmallType.size() > 0) {
            	productSmallTypeList.clear();
            	for(SystemDictionaryPo systemDictionaryPo : systemDictionaryListSmallType) {
            		productSmallTypeList.add(systemDictionaryPo.getDictionaryValue());
            	}
            }
            
        }

        //组合数据
        productTypeListResult[0] = productTypeList;
        productTypeListResult[1] = productSmallTypeList;

        return productTypeListResult;
    }

    /**
     *
     * @param longCode
     * @return
     */
    public ProductCategoryVo getProductCategoryVoByCode(String longCode) {
        String productCategoryVoJsonStr = getMap(RedisConstant.PRODUCT_CATEGORY_CODE_AND_VO_DATA, longCode);
        if (StringUtils.isEmpty(productCategoryVoJsonStr)) {
            return null;
        }
        ProductCategoryVo productCategoryVo = JsonUtil.fromJson(productCategoryVoJsonStr, ProductCategoryVo.class);
        return productCategoryVo;
    }

}