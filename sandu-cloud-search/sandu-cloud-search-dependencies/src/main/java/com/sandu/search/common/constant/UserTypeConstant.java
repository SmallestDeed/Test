package com.sandu.search.common.constant;

/**
 * 用户类型常量
 *
 * @date 20180119
 * @auth pengxuangang
 */
public class UserTypeConstant {

    //用户类型
    //内部用户
    public static final int USER_ROLE_TYPE_INNER = 1;
    //厂商
    public static final int USER_ROLE_TYPE_COMPANY = 2;
    //经销商
    public static final int USER_ROLE_TYPE_DEALER = 3;
    //设计公司
    public static final int USER_ROLE_TYPE_DESIGN_COMPANY = 4;
    //设计师
    public static final int USER_ROLE_TYPE_DESIGNER = 5;
    //装修公司
    public static final int USER_ROLE_TYPE_DECORATION_COMPANY = 6;
    //学校（培训机构）
    public static final int USER_ROLE_TYPE_SCHOOL = 7;
    //普通用户
    public static final int USER_ROLE_TYPE_NORMAL = 8;
    //游客
    public static final int USER_ROLE_TYPE_VISITOR = 9;
    //企业内部用户
    public static final int USER_ROLE_TYPE_COMPANY_INNER = 10;
    //独立经销商d_dealers
    public static final int USER_ROLE_TYPE_DEALER_D = 14;
    //独立经销商
    public static final int USER_ROLE_TYPE_INDEPENDENT_DEALERS = 14;


    /**
     * 用户使用类型：试用
     */
    public static final int USER_USE_TYPE_TRIAL = 0;

    /**
     * 用户使用类型：正式
     */
    public static final int USER_USE_TYPE_OFFICIAL =1;

    /**
     * 是否展示三度一键方案：1:是
     */
    public static final int USER_SHOW_SANDU_PLAN = 1;

    /**
     * 是否展示三度一键方案：0:否
     */
    public static final int USER_NOT_SHOW_SANDU_PLAN =0 ;

    //请求没有token: 登录状态1
    public static final int LOGIN_STATUS_ONE = 1;

    //请求有token,但是已过期 :登录状态2
    public static final int LOGIN_STATUS_TWO = 2;

    //请求有token,没有过期,但是redis没有:登录状态3
    public static final int LOGIN_STATUS_THREE = 3;

    //正常 ：登录状态4
    public static final int LOGIN_STATUS_FOUR = 4;

    //无效token ：登录状态5
    public static final int LOGIN_STATUS_FIVE = 5;

}
