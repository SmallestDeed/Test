package com.sandu.search.common.util;


import com.sandu.common.properties.AppProperties;
import com.sandu.common.properties.ResProperties;
import com.sandu.search.entity.product.dto.ResPic;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;


//import com.sun.xml.internal.messaging.saaj.packaging.mime.internet.MimeUtility;

public class Utils {
    private static Logger logger = Logger.getLogger(Utils.class);

    /**
     * 调http接口 get
     *
     * @param url
     * @return
     */
    public static String doGetMethod(String url) {
        String result = null;
        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(url);
            HttpResponse response = httpClient.execute(httpGet);
            logger.error("utlis ------ doGetMethod  ------ response : " + response);
            int code = response.getStatusLine().getStatusCode();
            if (code == 200) {
                result = EntityUtils.toString(response.getEntity());
            }
        } catch (IOException e) {
            logger.error("doGetMethod exception : {}", e);
        }
        return result;
    }


    /**
     * 检验一个String是否为空。
     * 要校验的字符串。
     *
     * @return 如果是空，将返回true，否则返回false。
     */
    public static boolean isEmpty(String str) {
        if ((str == null) || (str.length() <= 0)) {
            return true;
        }

        return false;
    }

    /**
     * str(逗号隔开格式)转化为list
     *
     * @return
     * @author huangsongbo
     */
    public static List<String> getListFromStr(String str) {
        List<String> list = new ArrayList<String>();
        if (StringUtils.isBlank(str))
            return list;
        if (str.startsWith(",")) {
            str = str.substring(1, str.length());
        }
        if (str.endsWith(",")) {
            str = str.substring(0, str.length() - 1);
        }
        String[] strs = str.split(",");
        list = Arrays.asList(strs);
        return list;
    }

    /**
     * 取得对应图片的缩略图的图片id(针对于空间,户型等图片存在res_pic的表中的图片)
     *
     * @return
     * @author huangsongbo
     */
    public static Integer getSmallPicId(ResPic resPic, String type) {
        if (resPic == null)
            return -1;
        String smallPicInfo = resPic.getSmallPicInfo();
        if (StringUtils.isBlank(smallPicInfo))
            return null;
        Map<String, String> msgMap = getMapFromStr(smallPicInfo);
        String picIdStr = msgMap.get(type);
        if (StringUtils.isBlank(picIdStr))
            return null;
        return Integer.valueOf(picIdStr);
    }

    /**
     * 特殊格式字符串处理成map:格式:web:38;ipad:37;
     *
     * @return
     * @author huangsongbo
     */
    public static Map<String, String> getMapFromStr(String fileDesc) {
        Map<String, String> map = new HashMap<String, String>();
        String[] strs = fileDesc.split(";");
        for (String str : strs) {
            if (str.split(":").length == 2) {
                map.put(str.split(":")[0].trim(), str.split(":")[1].trim());
            }
        }
        return map;
    }


    /**
     * 路径中含有/[yyyy]/[MM]/[dd]/[HH]/[mm]/[ss]/
     *
     * @param filePath
     * @return
     * @author huangsongbo
     */
    public static String replaceDate(String filePath) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
        String dateInfo = simpleDateFormat.format(new Date());
        String[] dateInfoArrays = dateInfo.split("_");
        if (StringUtils.isBlank(filePath)) {
            return filePath;
        }
        if (filePath.indexOf("[yyyy]") != -1 || filePath.indexOf("[YYYY]") != -1) {
            filePath = filePath.replace("[yyyy]", dateInfoArrays[0]);
            filePath = filePath.replace("[YYYY]", dateInfoArrays[0]);
        }
        if (filePath.indexOf("[MM]") != -1) {
            filePath = filePath.replace("[MM]", dateInfoArrays[1]);
        }
        if (filePath.indexOf("[dd]") != -1) {
            filePath = filePath.replace("[dd]", dateInfoArrays[2]);
        }
        if (filePath.indexOf("[HH]") != -1) {
            filePath = filePath.replace("[HH]", dateInfoArrays[3]);
        }
        if (filePath.indexOf("[mm]") != -1) {
            filePath = filePath.replace("[mm]", dateInfoArrays[4]);
        }
        if (filePath.indexOf("[ss]") != -1) {
            filePath = filePath.replace("[ss]", dateInfoArrays[5]);
        }
        return filePath;
    }

    /**
     * 不同系统对应的路径处理
     *
     * @param uploadRoot
     * @param valueByFileKey
     * @return
     * @author huangsongbo
     */
    public static String dealWithPath(String uploadRoot, String valueByFileKey) {
        if (StringUtils.isEmpty(valueByFileKey)) {
            valueByFileKey = Utils.getValueByFileKey(AppProperties.APP, AppProperties.SYSTEM_FORMAT_FILEKEY, "linux");
        }

        if (StringUtils.equals("linux", valueByFileKey)) {
            uploadRoot = uploadRoot.replace("\\", "/");
        } else {
            uploadRoot = uploadRoot.replace("/", "\\");
        }
        return uploadRoot;
    }

    public static String getValueByFileKey(ResourceBundle configUtil, String key, String defalut) {
        String returnStr = getValueByFileKeyOriginal(configUtil, key, defalut);
        return replaceDate(returnStr);
    }

    /**
     * 配置文件取值
     *
     * @param configUtil 配置文件类别
     * @param key        key
     * @param defalut    默认值
     * @return
     * @author huangsongbo
     */
    // public static String getValueByFileKey(ConfigUtil configUtil, String key,
    // String defalut) {
    public static String getValueByFileKeyOriginal(ResourceBundle configUtil, String key, String defalut) {
        String value = "";
        try {
            /* 如果配置文件为res.properties,key值应该加上.upload.path */
            if (configUtil.equals(ResProperties.RES)) {
                if (StringUtils.equals("product.groupProduct.file.location.path", key)) {
                    key = "product.groupProduct.file.location";
                }
                key = key.replace(".upload.path", "");
                key += ".upload.path";
            }
            // value=configUtil.getValue(key);
            value = configUtil.getString(key);
            /* 检测带有%%的情况 */
            /* value="%product.baseProduct.pic.upload.path%web/" */
            int num = value.length() - value.replace("%", "").length();
            /* 两个"%" */
            if (num == 2) {
                int startIndex = value.indexOf("%");
                int endIndex = value.lastIndexOf("%");
                /* key="product.baseProduct.pic.upload.path" */
                String appKey = value.substring(startIndex + 1, endIndex);
                String value3 = value.substring(endIndex + 1, value.length());
                // String value2=configUtil.getValue(appKey);
                String value2 = configUtil.getString(appKey);
                value = value2 + value3;
            }
            /* 检测带有%%的情况->end */

            // 处理时间 ->start
            /* value = Utils.replaceDate(value); */
            // 处理时间 ->end

        } catch (Exception e) {
            // e.printStackTrace();
            value = defalut;
        }
        return value;
    }

}