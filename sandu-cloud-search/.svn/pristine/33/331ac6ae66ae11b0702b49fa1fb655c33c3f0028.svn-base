package com.sandu.search.common.tools;

import com.sandu.search.entity.elasticsearch.index.ProductIndexMappingData;

import java.util.*;
import java.util.Map.Entry;

/**
 * 产品数据排序工具类
 *
 * @date 20180301
 * @auth pengxuangang
 */
public class ProductDataOrderUtil {

    /**
     * 根据白膜产品排序属性对产品列表进行排序(需要做数据排序，所以这里使用了LinkedHashMap)
     *
     * @param orgProductList                 原始产品列表
     * @param whiteMembraneOrderAttributeMap 白膜产品排序属性集合
     * @return
     */
    public static List<ProductIndexMappingData> productOrderByAttribute(List<ProductIndexMappingData> orgProductList
            , Map<String, String> whiteMembraneOrderAttributeMap) {
        if (null == orgProductList || 0 >= orgProductList.size()
                || null == whiteMembraneOrderAttributeMap || 0 >= whiteMembraneOrderAttributeMap.size()) {
            return null;
        }

        //原始产品集合
        Map<Integer, ProductIndexMappingData> orgProductMap = new LinkedHashMap<>(orgProductList.size());
        //排序后产品列表
        List<ProductIndexMappingData> newProductList = new ArrayList<>(orgProductList.size());
        //产品分值集合
        Map<Integer, Integer> productScoreMap = new LinkedHashMap<>();
        //产品排序属性集合<产品ID,<属性编码,属性值>>
        Map<Integer, Map<String, String>> productAttributeMap = new LinkedHashMap<>(orgProductList.size());

        //转换为产品排序属性集合
        orgProductList.forEach(orgProduct -> {
            //转换数据结构
            orgProductMap.put(orgProduct.getId(), orgProduct);
            //产品属性集合
            productAttributeMap.put(orgProduct.getId(), orgProduct.getProductOrderAttributeMap());
        });

        /************************* 计算白膜产品每项排序属性分值 ***************************/
        //白膜产品分值集合<属性编码, 分值>
        Map<String, Integer> whiteMembraneScoreMap = new LinkedHashMap<>(whiteMembraneOrderAttributeMap.size());
        //对白膜属性排序值进行排序-计算每项分值
        List<String> sortList = MapUtil.sortMap(MapUtil.coverMapGenericsString(whiteMembraneOrderAttributeMap));
        //倍数，每项倍数是10倍
        int orgMultiple = 1;
        for (int i = (sortList.size() - 1); i >= 0; i--) {
            whiteMembraneScoreMap.put(sortList.get(i), orgMultiple * 10);
            //倍数递增
            orgMultiple *= 10;
        }

        /************************* 移除产品与白膜产品未匹配的排序属性 ***************************/
        //已匹配的产品属性
        Map<Integer, Map<String, String>> productMatchAttributeMap = new LinkedHashMap<>(productAttributeMap.size());
        for (Entry<Integer, Map<String, String>> entry : productAttributeMap.entrySet()) {
            //产品旧排序属性
            Map<String, String> orgProductOrderAttrMap = entry.getValue();
            //产品新排序属性
            Map<String, String> newProductOrderAttrMap = new LinkedHashMap<>();
            if (null != orgProductOrderAttrMap && 0 < orgProductOrderAttrMap.size()) {
                for (Entry<String, String> productEntries : orgProductOrderAttrMap.entrySet()) {
                    //该条排序属性是否匹配
                    boolean isMatchAttr = false;
                    //遍历白膜排序属性对象
                    for (Entry<String, String> whiteMembraneEntries : whiteMembraneOrderAttributeMap.entrySet()) {
                        //KEY/Value均匹配
                        if (productEntries.getKey().equals(whiteMembraneEntries.getKey())
                                && productEntries.getValue().equals(whiteMembraneEntries.getValue())) {
                            isMatchAttr = true;
                            break;
                        }
                    }
                    //若匹配则将此条属性加到新排序集合
                    if (isMatchAttr) {
                        newProductOrderAttrMap.put(productEntries.getKey(), productEntries.getValue());
                    }
                }
            }

            //已匹配的产品对象
            productMatchAttributeMap.put(entry.getKey(), newProductOrderAttrMap);
        }

        /************************* 为每个产品打分 ***************************/
        if (0 < productMatchAttributeMap.size()) {
            for (Entry<Integer, Map<String, String>> entry : productMatchAttributeMap.entrySet()) {
                //当前分值
                int score = 0;
                //已匹配属性
                Map<String, String> productMatchAttr = entry.getValue();
                if (null != productMatchAttr && 0 < productMatchAttr.size()) {
                    for (Entry<String, String> entries : productMatchAttr.entrySet()) {
                        if (whiteMembraneScoreMap.containsKey(entries.getKey())) {
                            //分值递增
                            score += whiteMembraneScoreMap.get(entries.getKey());
                        }
                    }
                }
                //装入分值
                productScoreMap.put(entry.getKey(), score);
            }
        }

        /*************************  对打分进行排序 ***************************/
        //产品ID分值排序(低到高)
        Map<String,Integer> productOrderScoreMap = MapUtil.sortMapForLambda(MapUtil.coverMapGenericsInteger(productScoreMap));
        //列表类型
        List<String> productOrderScoreList = new ArrayList<>();
        for (Entry<String, Integer> entries : productOrderScoreMap.entrySet()) {
            productOrderScoreList.add(entries.getKey());
        }
        //转换为有序集合
        LinkedList<String> linkedList = new LinkedList<>();

        /************************* 重新分配产品列表顺序 ***************************/
        /**
         * 将
         * Map<A,0>
         * Map<B,1>
         * Map<C,1>
         * 变成
         * Map<B,1>
         * Map<C,1>
         * Map<A,0>
         */
        if (0 < productOrderScoreList.size()) {
            //上次插入值
            Integer beforeValue = 0;
            //循环次数
            int forCount = 0;
            //原值索引位
            int orgValueIndex = 0;
            //倒序遍历
            for (int i = productOrderScoreList.size() - 1; i >= 0; i--) {
                //循环次数递增
                ++forCount;
                //当前Key
                String key = productOrderScoreList.get(i);

                //获取当前值
                Integer nowValue = productOrderScoreMap.get(key);
                //检查和上个是否大于或相同
                if (nowValue > beforeValue) {
                    linkedList.addFirst(key);
                    //值被改变后重计算变值索引位
                    orgValueIndex = forCount - 1;
                } else if (beforeValue.equals(nowValue)){
                    //加入上个等值索引位
                    linkedList.add(orgValueIndex, key);
                } else if (beforeValue > nowValue){
                    linkedList.addLast(key);
                    //值被改变后重计算变值索引位
                    orgValueIndex = forCount - 1;
                } else {
                    linkedList.add(key);
                }
                //装回before
                beforeValue = nowValue;
            }
        }

        /************************* 装回对象 ***************************/
        if (0 < linkedList.size()) {
            linkedList.forEach(newProduct -> newProductList.add(orgProductMap.get(Integer.parseInt(newProduct))));
        }

        return newProductList;
    }
}
