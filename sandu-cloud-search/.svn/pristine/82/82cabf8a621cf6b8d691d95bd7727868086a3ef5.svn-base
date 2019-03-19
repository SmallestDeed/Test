package com.sandu.search.entity.user;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class LoginUser implements Serializable {

	private static final long serialVersionUID = 2712880546833875417L;
	private Integer id;// 数字主键 s
	private Integer userType;
	private String loginId;// 字符主键
	private String loginName;// 登录名称
	private String loginPhone;// 登录手机号
	private String loginEmail;// 登录手机号
	private String name;// 登录中文名
    private String appKey;
    private String token;
    private String mediaType;
    private Integer groupId;//组织ID
	private Integer sex;//性别
	private String picPath;//头像
	private String brandIds;//品牌(多个用逗号隔开)
	
	private String serverUrl;
	private String resourcesUrl;
	
	private String siteName;
	
	private String sitekey;
	
	private String userKey; // the key of cache user in redis
	/**
	 * 用户余额，单位：分
	 */
	private Double balanceAmount;
	/**
	 * 总计消费金额，单位：分
	 */
	private Double consumAmount;
	//用户企业ID
    private int businessAdministrationId;
    //用户菜单权限编码列表
    private List<String> roleCodeList;
}
