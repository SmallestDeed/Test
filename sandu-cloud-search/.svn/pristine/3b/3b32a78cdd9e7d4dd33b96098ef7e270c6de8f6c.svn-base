package com.sandu.search.aop;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import com.sandu.collect.KafkaProducer;
import com.sandu.collect.model.CollectionObject;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.validation.BindingResult;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
public class CollectUriAspect
{
    private static final Logger log = LoggerFactory.getLogger(CollectUriAspect.class);

    private static ObjectMapper mapper = new ObjectMapper();
    private KafkaProducer kafkaProducer;

    public void setKafkaProducer(KafkaProducer kafkaProducer)
    {
        this.kafkaProducer = kafkaProducer;
    }
    @Pointcut("@within(org.springframework.stereotype.Controller)")
    public void webRequest() {
    }

    @Pointcut("@within(org.springframework.web.bind.annotation.RestController)")
    public void restRequest() {
    }

    @Order(10)
    @Around("webRequest() || restRequest()")
    public Object doAround(ProceedingJoinPoint pjp) {
        ServletRequestAttributes attributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        String method = request.getMethod();

        boolean isProducer = false;
        long startTime = System.currentTimeMillis();
        CollectionObject.CollectionObjectBuilder obj = CollectionObject.builder();
        obj.startTime(Long.valueOf(startTime));

        List inputParams = new ArrayList();

        if ((HttpMethod.GET.matches(method)) || (HttpMethod.POST.matches(method)) ||
                (HttpMethod.PUT
                        .matches(method)) ||
                (HttpMethod.DELETE.matches(method)))
        {
            isProducer = true;

            obj.host(request.getHeader("Host"))
                    .uri(request
                            .getRequestURI())
                    .method(method)
                    .token(Strings.nullToEmpty(request
                            .getHeader("Authorization")))
                    .remoteAddress(getIp(request))
                    .origin(request
                            .getHeader("Origin"))
                    .referfer(Strings.nullToEmpty(request
                            .getHeader("Referer")))
                    .userAgent(request
                            .getHeader("User-Agent"))
                    .platform(Strings.nullToEmpty(request
                            .getHeader("Platform-Code")))
                    .params(Strings.nullToEmpty(request
                            .getQueryString()));

            String contentType = request.getContentType();
            if ((!Strings.isNullOrEmpty(contentType)) && (contentType.contains("application/json"))) {
                Object[] params = pjp.getArgs();
                int i = 0; for (int length = params.length; i < length; i++) {
                    Object tmp = params[i];
                    if (!(tmp instanceof BindingResult))
                    {
                        inputParams.add(tmp);
                    }
                }
            }
        }

        Object result = null;
        try {
            result = pjp.proceed();
        }
        catch (Throwable throwable) {
            obj.statusCode(Integer.valueOf(500));
            log.error("Aop Proceed Error: " + throwable.getMessage());
        }

        long endTime = System.currentTimeMillis();
        obj.endTime(Long.valueOf(endTime));
        if (isProducer) {
            if (inputParams.size() > 0) {
                try
                {
                    obj.body(mapper.writeValueAsString(inputParams));
                } catch (JsonProcessingException e) {
                    log.error("Request Body convert json error, " + e.getMessage());
                }
            }

            this.kafkaProducer.send(obj.build());
        }

        return result;
    }
    @Order(10)
    @AfterThrowing(value="webRequest() || restRequest()", throwing="ex")
    public void doAfterThrowing(Exception ex) {
        log.error("Throwing: " + ex.getMessage());
    }

    private String getIp(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if ((Strings.isNullOrEmpty(ip)) || ("unknown".equalsIgnoreCase(ip))) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if ((Strings.isNullOrEmpty(ip)) || ("unknown".equalsIgnoreCase(ip))) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if ((Strings.isNullOrEmpty(ip)) || ("unknown".equalsIgnoreCase(ip))) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}