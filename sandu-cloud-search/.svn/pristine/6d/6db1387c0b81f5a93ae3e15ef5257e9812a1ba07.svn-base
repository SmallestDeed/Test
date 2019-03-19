package com.sandu.search.common.tools;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日期工具
 *
 * @date 2018/6/19
 * @auth pengxuangang
 * @mail xuangangpeng@gmail.com
 */
public class DateTool {

    /**
     * 根据时间错字符串获取日期时间字符串
     *
     * @param date
     * @return
     */
    public static String getDateStrByDate(Date date) {
        if (null == date) {
            return null;
        }

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return formatter.format(date);
    }
}
