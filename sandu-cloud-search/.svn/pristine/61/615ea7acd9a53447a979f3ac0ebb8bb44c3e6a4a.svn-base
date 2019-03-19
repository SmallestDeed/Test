package com.sandu.search.common.tools;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.*;
import java.util.Map.Entry;

/**
 * Map集合工具类
 *
 * @date 20180301
 * @auth pengxuangang
 */
public class MapUtil {

    /**
     * 对集合中Value进行排序(低到高,普通版)
     *
     * @param map
     * @return
     */
    public static List<String> sortMap(Map<String, Integer> map) {
        //获取entrySet
        Set<Entry<String, Integer>> mapEntries = map.entrySet();

        //使用链表来对集合进行排序，使用LinkedList，利于插入元素
        List<Entry<String, Integer>> result = new LinkedList<>(mapEntries);
        //自定义比较器来比较链表中的元素
        //基于entry的值（Entry.getValue()），来排序链表
        result.sort(Comparator.comparing(Entry::getValue));

        //将排好序的存入到LinkedHashMap(可保持顺序)中，需要存储键和值信息对到新的映射中。
        Map<String, Integer> linkMap = new LinkedHashMap<>();
        List<String> sortList = new ArrayList<>();
        for (Entry<String, Integer> newEntry : result) {
            //集合类型
            linkMap.put(newEntry.getKey(), newEntry.getValue());
            //列表类型
            sortList.add(newEntry.getKey());
        }

        return sortList;
    }

    /**
     * 对map中的数据按照value排序(Lambda版)
     *
     */
    public static Map sortMapForLambda(Map<String, Integer> oldMap) {

        ArrayList<Entry<String, Integer>> list = new ArrayList<>(oldMap.entrySet());

        list.sort(Comparator.comparing(Entry::getValue));

        LinkedHashMap<String,Integer> newMap = new LinkedHashMap<>();
        List<String> keyList = new ArrayList<>();

        list.forEach((Entry<String, Integer> obj) -> {
            keyList.add(obj.getKey());
            newMap.put(obj.getKey(), obj.getValue());
        });

        return newMap;
    }

    /**
     * 将Map的Key/Value转换为String
     *
     * @return
     */
    public static Map<String, Integer> coverMapGenericsInteger(Map<Integer, Integer> orgMap) {
        if (null == orgMap || 0 >= orgMap.size()) {
            return null;
        }

        Map<String, Integer> newMap = new LinkedHashMap<>(orgMap.size());

        for (Entry<Integer, Integer> entry : orgMap.entrySet()) {
            newMap.put("" + entry.getKey(), entry.getValue());
        }

        return newMap;
    }

    /**
     * 将Map的Key/Value转换为String
     *
     * @return
     */
    public static Map<String, Integer> coverMapGenericsString(Map<String, String> orgMap) {
        if (null == orgMap || 0 >= orgMap.size()) {
            return null;
        }

        Map<String, Integer> newMap = new LinkedHashMap<>(orgMap.size());

        for (Entry<String, String> entry : orgMap.entrySet()) {
            if(StringUtils.isNotBlank(entry.getValue()) && NumberUtils.isDigits(entry.getValue())) {
                newMap.put("" + entry.getKey(), Integer.parseInt(entry.getValue()));
            }

        }

        return newMap;
    }
}
