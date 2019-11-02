package com.netflix.gateway.gateway.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author flaoliveira
 * @version : $<br/>
 * : $
 * @since 02/11/2019 18:30
 */
public class LogInterceptor extends HandlerInterceptorAdapter {

    private static Logger logger = LoggerFactory.getLogger(LogInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        long startTime = System.currentTimeMillis();
        logger.info("C=LogInterceptor, M=preHandle, Request={}", request.getRequestURI());
        request.setAttribute("startTime", startTime);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, //
            Object handler, Exception ex) {
        long startTime = (Long) request.getAttribute("startTime");
        long endTime = System.currentTimeMillis();
        logger.info("C=LogInterceptor, M=afterCompletion, Request={}, SpendTime={}ms", request.getRequestURI(), (endTime - startTime));
    }

}
