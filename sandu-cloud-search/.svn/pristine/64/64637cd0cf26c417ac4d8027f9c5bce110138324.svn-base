package com.sandu.search.interceptor;


import com.sandu.common.LoginContext;
import com.sandu.search.common.constant.UserTypeConstant;
import com.sandu.search.common.tools.JsonUtil;
import com.sandu.search.entity.response.universal.UniversalSearchResultResponse;
import com.sandu.search.entity.user.LoginUser;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Slf4j
public class LoginUserInterceptor implements HandlerInterceptor {

    private static final String CLASS_LOG_PREFIX = "拦截器：";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String method = request.getMethod();
        if(method.equals("OPTIONS")){
            log.info("进入options方法");
            return true;
        }

        String currentMsgId = request.getParameter("msgId");
        Integer msgId = StringUtils.isEmpty(currentMsgId) ? 0 : Integer.getInteger(currentMsgId);

        String currentURL = request.getServletPath();
    	//不需要登录就能访问
        if (currentURL.contains("/index")) {
            log.info(CLASS_LOG_PREFIX + "不拦截url：" + currentURL);
            return true;
        }

        Integer loginStatus = LoginContext.getLoginStatus();
        switch (loginStatus) {
            case UserTypeConstant.LOGIN_STATUS_ONE:
                log.error("authorization is null, requestUrl={}", currentURL);
                resposeMessageToCustomer(response, msgId,"请登录" );
                return false;
            case UserTypeConstant.LOGIN_STATUS_TWO:
                log.error("APP token is EXPIRED, requestUrl={}", currentURL);
                resposeMessageToCustomer(response, msgId,"请登录");
                return false;
            case UserTypeConstant.LOGIN_STATUS_FIVE:
                log.error("App token is INVALID,requestUrl={}", currentURL);
                resposeMessageToCustomer(response, msgId,"请登录");
                return false;
            case UserTypeConstant.LOGIN_STATUS_THREE:
                log.error("用户在其他地方登录,requestUrl={}", currentURL);
                resposeMessageToCustomer(response, msgId,"用户在其他地方登录");
                return false;
            case UserTypeConstant.LOGIN_STATUS_FOUR:
                return true;
            default:
                LoginUser loginUser = LoginContext.getLoginUser(LoginUser.class);
                if (null == loginUser) {
                    log.error(CLASS_LOG_PREFIX + "获取用户信息为空");
                    resposeMessageToCustomer(response, msgId,"请登陆");
                    return false;
                }
                return true;
        }
    }
      

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
    }

    public void resposeMessageToCustomer(HttpServletResponse response, Integer msgId, String message) throws IOException {

        PrintWriter pw = null;
        try {
            response.setContentType("application/json;charset=utf-8");
            UniversalSearchResultResponse responseEnvelope = new UniversalSearchResultResponse(false, message, msgId);
            String jsonString = JsonUtil.toJson(responseEnvelope);
            pw = response.getWriter();
            pw.print(jsonString);
            pw.flush();
            pw.close();
        } catch (IOException e) {
        	log.error("返回数据异常："+message);
        } finally {
            if (null != pw) {
                pw.close();
            }
        }
    }


}
