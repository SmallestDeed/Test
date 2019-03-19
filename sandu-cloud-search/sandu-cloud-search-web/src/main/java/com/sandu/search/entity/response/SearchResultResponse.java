package com.sandu.search.entity.response;

import com.sandu.search.config.MessageConfig;
import lombok.Data;

import java.io.Serializable;

/**
 * 搜索结果返回
 *
 * @date 20171222
 * @auth pengxuangang
 */
@Data
public class SearchResultResponse implements Serializable {


    private static final long serialVersionUID = -8172269490110693777L;
    //返回码
    private String returnCode;
    //返回信息
    private String returnMessage;
    //返回对象
    private Object obj;
    //返回数据条数
    private long total;
    public SearchResultResponse(String returnCode) {
        this.returnCode = returnCode;
        //根据返回码获取配置文件返回信息
        this.returnMessage = MessageConfig.getMessageByCode(returnCode);
    }
    public SearchResultResponse(String returnCode, Object obj, long total) {
        this.returnCode = returnCode;
        //根据返回码获取配置文件返回信息
        this.returnMessage = MessageConfig.getMessageByCode(returnCode);
        this.obj = obj;
        this.total = total;
    }
}
