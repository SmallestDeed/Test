package com.sandu.search.storage.system;

import com.google.gson.reflect.TypeToken;
import com.sandu.search.common.constant.RedisConstant;
import com.sandu.search.common.constant.SystemDictionaryType;
import com.sandu.search.common.tools.JsonUtil;
import com.sandu.search.entity.elasticsearch.po.metadate.SystemDictionaryPo;
import com.sandu.search.service.redis.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 系统字典值元数据存储
 *
 * @date 20180104
 * @auth pengxuangang
 */
@Component
public class SystemDictionaryMetaDataStorage {

    private final RedisService redisService;

    @Autowired
    public SystemDictionaryMetaDataStorage(RedisService redisService) {
        this.redisService = redisService;
    }

    /**
     * 通过类型查询系统字典数据
     *
     * @param dictionaryType 字典类型
     * @return
     */
    public List<SystemDictionaryPo> querySystemDictionaryListByType(String dictionaryType) {
        if (StringUtils.isEmpty(dictionaryType)) {
            return null;
        }

        String systemDictionaryPoListStr = redisService.getMap(RedisConstant.SYSTEM_DICTIONARY_TYPE_DATA, dictionaryType);
        if (StringUtils.isEmpty(systemDictionaryPoListStr)) {
            return null;
        }

        return JsonUtil.fromJson(systemDictionaryPoListStr, new TypeToken<List<SystemDictionaryPo>>() {}.getType());
    }

    /**
     * 通过类型和值查询系统字典名
     *
     * @param dictionaryType 字典类型
     * @return
     */
    public String getSystemDictionaryNameByTypeAndValue(String dictionaryType, Integer dictionaryValue) {

        List<SystemDictionaryPo> systemDictionaryList = querySystemDictionaryListByType(dictionaryType);
        if (null == systemDictionaryList || 0 == systemDictionaryList.size()) {
            return null;
        }

        String dictionaryName = null;
        for (SystemDictionaryPo systemDictionaryPo : systemDictionaryList) {
            if (systemDictionaryPo.getDictionaryValue() == dictionaryValue) {
                dictionaryName = systemDictionaryPo.getDictionaryName();
                break;
            }
        }

        return dictionaryName;
    }

    /**
     * 通过类型和值查询系统字典名
     *
     * @param dictionaryType 字典类型
     * @return
     */
    public String getSystemDictionaryValueKeyByTypeAndValue(String dictionaryType, Integer dictionaryValue) {

        List<SystemDictionaryPo> systemDictionaryList = querySystemDictionaryListByType(dictionaryType);
        if (null == systemDictionaryList || 0 == systemDictionaryList.size()) {
            return null;
        }
        String valueKey = null;
        for (SystemDictionaryPo systemDictionaryPo : systemDictionaryList) {
            if (systemDictionaryPo.getDictionaryValue() == dictionaryValue) {
                valueKey = systemDictionaryPo.getDictionaryKey();
                break;
            }
        }

        return valueKey;
    }


    /**
     * 排除非同产品大类的小类ID列表(并将字典valuekey转换为字典值)
     *
     * @param valueKeyList Key列表
     * @param productType  产品大类
     * @return
     */
    public List<Integer> excludeProductSmallTypeByKeyList(List<String> valueKeyList, int productType) {
        if (null == valueKeyList || 0 >= valueKeyList.size() || 0 == productType) {
            return null;
        }

        //获取产品分类字典数据
        List<SystemDictionaryPo> systemDictionaryPoList = querySystemDictionaryListByType(SystemDictionaryType.SYSTEM_DICTIONARY_TYPE_PRODUCTTYPE);
        //获取大类字典类型名
        String dictionaryType = systemDictionaryPoList.stream().filter(systemDictionaryPo -> productType == systemDictionaryPo.getDictionaryValue()).collect(Collectors.toList()).get(0).getDictionaryKey();
        //获取大类的子类集合
        List<SystemDictionaryPo> productSmallTypeSystemDictionaryPoList = querySystemDictionaryListByType(dictionaryType);
        //转换为Map对象
        Map<String, Integer> productSmallTypeKeyAndValueMap = new HashMap<>(productSmallTypeSystemDictionaryPoList.size());
        productSmallTypeSystemDictionaryPoList.forEach(productSmallTypeSystemDictionaryPo -> productSmallTypeKeyAndValueMap.put(productSmallTypeSystemDictionaryPo.getDictionaryKey(), productSmallTypeSystemDictionaryPo.getDictionaryValue()));

        //返回值列表
        List<Integer> valueList = new ArrayList<>(valueKeyList.size());
        valueKeyList.forEach(valueKey -> {
            if (!StringUtils.isEmpty(valueKey)) {
                //获取Key值
                String value = redisService.getMap(RedisConstant.SYSTEM_DICTIONARY_KEY_VALUE_DATA, valueKey);
                if (!StringUtils.isEmpty(value) && productSmallTypeKeyAndValueMap.containsKey(valueKey)) {
                    valueList.add(Integer.parseInt(value));
                }
            }
        });

        return valueList;
    }

    /**
     * 根据小类ValueKey获取所有的大小类(并将字典valuekey转换为字典值)
     *
     * @param valueKeyList Key列表
     * @return Map<大类,List<小类>>
     */
    public Map<Integer, List<Integer>> queryProductTypeByKeyList(List<String> valueKeyList) {
        if (null == valueKeyList || 0 >= valueKeyList.size()) {
            return null;
        }

        //产品小类valuekey对应大小类(Map<小类ValueKey, 大类Value-小类Value>)
        Map<String, String> productSmallTypeValueKeyMap = new HashMap<>();
        //所有大小类
        Map<Integer, List<Integer>> productTypeMap = new HashMap<>();

        //获取产品分类字典数据
        List<SystemDictionaryPo> systemDictionaryPoList = querySystemDictionaryListByType(SystemDictionaryType.SYSTEM_DICTIONARY_TYPE_PRODUCTTYPE);
        systemDictionaryPoList.forEach(systemDictionaryPo -> {
            //当前大类的小类
            List<SystemDictionaryPo> smallTypeSystemDictionaryPoList = querySystemDictionaryListByType(systemDictionaryPo.getDictionaryKey());
            smallTypeSystemDictionaryPoList.forEach(smallTypeSystemDictionaryPo ->
                    productSmallTypeValueKeyMap.put(
                            smallTypeSystemDictionaryPo.getDictionaryKey()
                            , systemDictionaryPo.getDictionaryValue() + "-" + smallTypeSystemDictionaryPo.getDictionaryValue()
                    )
            );
        });

        //匹配数据
        if (0 < productSmallTypeValueKeyMap.size()) {
            valueKeyList.forEach(valueKey -> {
                if (productSmallTypeValueKeyMap.containsKey(valueKey)) {
                    //解析当前对象
                    String productAndSamllTypeValue = productSmallTypeValueKeyMap.get(valueKey);
                    //大小类值对象
                    String[] productAndSmallTypes = productAndSamllTypeValue.split("-");
                    //产品大类
                    Integer productTypeValue = Integer.parseInt(productAndSmallTypes[0]);
                    //产品小类
                    Integer productSmallTypeValue = Integer.parseInt(productAndSmallTypes[1]);

                    List<Integer> productSmallTypeList = new ArrayList<>();
                    productSmallTypeList.add(productSmallTypeValue);
                    //将原值加回
                    if (productTypeMap.containsKey(productTypeValue)) {
                        productSmallTypeList.addAll(productTypeMap.get(productTypeValue));
                    }
                    //装回对象
                    productTypeMap.put(productTypeValue, productSmallTypeList);
                }
            });

            return productTypeMap;
        }

        return null;
    }
}
