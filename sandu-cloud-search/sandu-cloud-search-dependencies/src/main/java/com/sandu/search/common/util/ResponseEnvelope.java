package com.sandu.search.common.util;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.List;


@Scope("prototype")
@Component
public class ResponseEnvelope<T> implements Serializable {

    //返回成功字符串
    private static final String RESPONSE_MESSAGE_SUCCESS = "success";

    private static final long serialVersionUID = -5084759450031494382L;
    
    private boolean success = true;
    //返回状态
    private boolean status = true;
    //返回提示信息
    private String message;
    //返回对象
    private Object obj;
    //数据总条数
    private long totalCount;
    //状态码，不同状态不同业务逻辑处理（StatusCode类）
    private String statusCode;

    private String msgId;//pc专用
    private List<T> datalist;//pc专用
    private boolean flag;//pc专用

    public ResponseEnvelope() {
    }

    public ResponseEnvelope(boolean status) {
        this.status = status;
    }

    public ResponseEnvelope(boolean status, String message) {
        this.status = status;
        this.message = message;
    }

    public ResponseEnvelope(boolean success, Object obj) {
        this.success=success;
        this.obj = obj;
    }

    public ResponseEnvelope(boolean status, String message, Object obj) {
        this.status = status;
        this.message = (null == message || "".equals(message) ? RESPONSE_MESSAGE_SUCCESS : message);
        this.obj = obj;
    }

    public ResponseEnvelope(boolean status, String message, Object obj, long totalCount) {
        this.status = status;
        this.message = message;
        this.obj = obj;
        this.totalCount = totalCount;
    }

    public ResponseEnvelope(boolean status, List datalist, Object obj) {
        this.status = status;
        this.obj = obj;
        this.datalist = datalist;
    }

    public List<T> getDatalist() {
		return datalist;
	}

	public void setDatalist(List<T> datalist) {
		this.datalist = datalist;
	}

	public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }

    public long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }
    
    public boolean isSuccess() {
      return success;
  }

  public void setSuccess(boolean success) {
      this.success = success;
  }

    @Override
    public String toString() {
        return "ResponseEnvelope{" +
                "status=" + status +
                ", message='" + message + '\'' +
                ", obj=" + obj +
                ", totalCount=" + totalCount +
                ", statusCode=" + statusCode +
                '}';
    }

	public String getMsgId() {
		return msgId;
	}

	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}

	public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

}
