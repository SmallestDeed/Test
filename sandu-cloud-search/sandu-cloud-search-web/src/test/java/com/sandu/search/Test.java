package com.sandu.search;

public class Test {
    public static void main(String[] args) {
        String referer = "https://servicewechat.com/wxe24ed743feb9c17f/devtools/page-frame.html";

        if (null != referer && !"".equals(referer) && (
                referer.startsWith("http://servicewechat.com/")
                        || referer.startsWith("https://servicewechat.com/")
        )) {
            //去掉前缀
            referer = referer.substring(referer.indexOf("//servicewechat.com/") + 20, referer.length());
            //去掉后缀
            referer = referer.substring(0, referer.indexOf("/"));

            if (!"".equals(referer) && 0 > referer.indexOf("\\.")) {
                String a = "";
                //companyId = companyMetaDataStorage.getCompanyIdByMiniProgramAppId(referer);
            }
        }


        String b = "";
    }
}
