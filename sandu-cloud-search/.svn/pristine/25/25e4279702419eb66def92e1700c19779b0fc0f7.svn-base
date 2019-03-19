package com.sandu.search.common.tools;

import com.sandu.search.common.constant.HeaderConstant;
import com.sandu.search.common.constant.PlatformConstant;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * 请求头工具类
 *
 * @date 2018/6/4
 * @auth pengxuangang
 * @mail xuangangpeng@gmail.com
 */
public class RequestHeaderUtil {

    /**
     * 根据请求从头部获取平台ID
     *
     * @date 2018/6/4
     * @auth pengxuangang
     * @mail xuangangpeng@gmail.com
     */
    public static String getPlatformIdByRequest(HttpServletRequest request) {
        String platformCode = request.getHeader(HeaderConstant.PLATFORM_CODE);

        //检查是否为小程序
        if (!StringUtils.isEmpty(platformCode)){
            String customReferer = request.getHeader(HeaderConstant.CUSTOM_REFERER);

            if (!StringUtils.isEmpty(customReferer)
                    && (customReferer.startsWith("https://servicewechat.com/")
                    || customReferer.startsWith("http://servicewechat.com/"))) {
                platformCode = PlatformConstant.PLATFORM_CODE_MINI_PROGRAM;
            }
        }
        return platformCode;
    }

    /**
     * 获取平台ID
     * @param platformCode
     * @return
     */
    public static int getPlatformIdByCode(String platformCode) {
        switch (platformCode) {
            case PlatformConstant.PLATFORM_CODE_TOB_MOBILE :
                return PlatformConstant.PLATFORM_ID_TOB_MOBILE;
            case PlatformConstant.PLATFORM_CODE_TOB_PC :
                return PlatformConstant.PLATFORM_ID_TOB_PC;
            case PlatformConstant.PLATFORM_CODE_TOC_SITE :
                return PlatformConstant.PLATFORM_ID_TOC_SITE;
            case PlatformConstant.PLATFORM_CODE_TOC_MOBILE :
                return PlatformConstant.PLATFORM_ID_TOC_MOBILE;
            case PlatformConstant.PLATFORM_CODE_MINI_PROGRAM :
                return PlatformConstant.PLATFORM_ID_MINI_PROGRAM;
            case PlatformConstant.PLATFORM_CODE_SELECT_DECORATION :
                return PlatformConstant.PLATFORM_ID_SELECT_DECORATION;
            default:
                return 0;
        }
    }
}
