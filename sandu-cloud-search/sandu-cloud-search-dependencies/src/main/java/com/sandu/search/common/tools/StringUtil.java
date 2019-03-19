package com.sandu.search.common.tools;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 字符操作类
 *
 * @date 2018/3/15
 * @auth pengxuangang
 * @mail xuangangpeng@gmail.com
 */
public class StringUtil {

    /**
     * 获取指定字符串出现的次数
     *
     * @param srcText  源字符串
     * @param findText 要查找的字符串
     * @return
     */
    public static int appearNumber(String srcText, String findText) {
        if (null == srcText || "".equals(srcText) || null == findText || "".equals(findText)) {
            return 0;
        }

        //现字符串
        String newSrcText = srcText.replace(findText, "");

        return (srcText.length() - newSrcText.length())/findText.length();
    }

    /**
     * 字符串集合转换整形集合
     *
     * @param strings 字符串集合
     * @return
     */
    public static List<Integer> transformInteger(List<String> strings) {
        if (null == strings || 0 == strings.size()) {
           return null;
        }
        //转换
        List<Integer> integers = new ArrayList<>(strings.size());
        for (String str : strings) {
            if (!StringUtils.isEmpty(str)) {
                integers.add(Integer.valueOf(str));
            }
        }
        return integers;
    }

    /**
     * 字符串集合转换整形集合
     *
     * @param integers 集合
     * @return
     */
    public static List<String> transformString(List<Integer> integers) {
        if (null == integers || 0 == integers.size()) {
            return null;
        }
        //转换
        List<String> strings = new ArrayList<>(integers.size());
        for (Integer integer : integers) {
            if (null != integer) {
                strings.add(String.valueOf(integer));
            }
        }
        return strings;
    }

    /**
     * 验证Integer类型是否为空或小于等于0
     * @param cs
     * @return
     */
    public static boolean isEmpty(Integer cs) {
        return null == cs || 0 >= cs;
    }

}
