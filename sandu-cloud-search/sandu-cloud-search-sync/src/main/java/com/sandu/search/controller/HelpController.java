package com.sandu.search.controller;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 帮助接口
 *
 * @date 20180123
 * @auth pengxuangang
 */
@Data
@RestController
@RequestMapping("/")
@ConfigurationProperties(prefix = "fullsearch")
public class HelpController {

    @Value("${spring.profiles.active}")
    private String proFileActive;
    private String version;
    private Map<String, String> updatelog = new LinkedHashMap<>();

    /**
     * 检查当前应用版本
     *
     * @return
     */
    @RequestMapping("/version")
    String version() {

        //日志对象
        Map<String, List<String>> updatelogMap = new LinkedHashMap<>(updatelog.size());
        //格式化升级日志
        for (Map.Entry<String, String> entry : updatelog.entrySet()) {
            updatelogMap.put(entry.getKey(), Arrays.asList(entry.getValue().split(" ")));
        }

        //构建HTML对象
        StringBuffer htmlStringBuffer = new StringBuffer();
        htmlStringBuffer.append("<html>");
        htmlStringBuffer.append("<head><title>系统版本信息</title></head>");

        htmlStringBuffer.append("<b>");
        htmlStringBuffer.append("当前系统配置文件:");
        htmlStringBuffer.append("</b>");
        htmlStringBuffer.append("</br>");
        htmlStringBuffer.append(proFileActive);
        htmlStringBuffer.append("</br>");
        htmlStringBuffer.append("</br>");


        htmlStringBuffer.append("<b>");
        htmlStringBuffer.append("当前系统版本号:");
        htmlStringBuffer.append("</b>");
        htmlStringBuffer.append("</br>");
        htmlStringBuffer.append(version);
        htmlStringBuffer.append("</br>");
        htmlStringBuffer.append("</br>");

        htmlStringBuffer.append("<b>");
        htmlStringBuffer.append("升级日志:");
        htmlStringBuffer.append("</b>");
        htmlStringBuffer.append("</br>");

        htmlStringBuffer.append("<b>");
        htmlStringBuffer.append("##########1.1.2后应用拆分#########");
        htmlStringBuffer.append("</b>");
        htmlStringBuffer.append("</br>");

        for (Map.Entry<String, List<String>> entry : updatelogMap.entrySet()) {

            String keyStr = entry.getKey();
            int i = keyStr.indexOf("(");
            //版本号
            String versionNoStr = keyStr.substring(0, i);
            //发布日期
            String dateStr = keyStr.substring(i, keyStr.length());

            htmlStringBuffer.append("<b>");
            htmlStringBuffer.append(versionNoStr);
            htmlStringBuffer.append(":");
            htmlStringBuffer.append("</b>");
            htmlStringBuffer.append("<a style=\"font-size: 5px\">");
            htmlStringBuffer.append(dateStr);
            htmlStringBuffer.append("</a>");
            htmlStringBuffer.append("</br>");

            entry.getValue().forEach( logStr -> {
                htmlStringBuffer.append("<a style=\"font-size: 12px\">");
                htmlStringBuffer.append(logStr);
                htmlStringBuffer.append("</a>");
                htmlStringBuffer.append("</br>");
            });
            htmlStringBuffer.append("</br>");
        }

        htmlStringBuffer.append("</html>");

        return htmlStringBuffer.toString();
    }
}