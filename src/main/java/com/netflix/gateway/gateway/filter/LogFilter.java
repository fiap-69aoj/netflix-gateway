package com.netflix.gateway.gateway.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

/**
 * @author flaoliveira
 * @version : $<br/>
 * : $
 * @since 20/10/2019 14:37
 */
@Component
public class LogFilter extends ZuulFilter {

    private Logger LOGGER = Logger.getLogger(LogFilter.class.getCanonicalName());

    @Override
    public boolean shouldFilter () {
        return true;
    }

    @Override
    public Object run () throws ZuulException {
        HttpServletRequest request = RequestContext.getCurrentContext().getRequest();
        LOGGER.info(String.format("logging the request %s, params: %s", request.getRequestURL(), request.getParameterMap()));
        return null;
    }

    @Override
    public String filterType () {
        return "pre";
    }

    @Override
    public int filterOrder () {
        return 1;
    }
}
