package com.netflix.gateway.gateway.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.http.HttpServletRequestWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.util.UrlPathHelper;

import java.util.List;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

/**
 * @author flaoliveira
 * @version : $<br/>
 * : $
 * @since 20/10/2019 14:37
 */
//@Component
public class RedirectFilter extends ZuulFilter {

    private Logger LOGGER = Logger.getLogger(RedirectFilter.class.getCanonicalName());

    private UrlPathHelper urlPathHelper = new UrlPathHelper();

    @Autowired
    private DiscoveryClient discoveryClient;

    @Override
    public boolean shouldFilter () {
        return true;
    }

    @Override
    public Object run () {
        RequestContext ctx = RequestContext.getCurrentContext();
        final String requestURI = this.urlPathHelper.getPathWithinApplication(ctx.getRequest());
        final String requestURL = ctx.getRequest().getRequestURL().toString();

        String serviceId = String.format("netflix-%s", requestURI.replace("/", ""));
        List<ServiceInstance> instances = discoveryClient.getInstances(serviceId);

        final String urlToRedirect = instances.get(0).getUri().toString() + requestURI;

        HttpServletRequestWrapper httpServletRequestWrapper = new HttpServletRequestWrapper(ctx.getRequest()) {

            public String getRequestURI () {
                if (requestURI != null && !requestURI.equals("/")) {
                    return requestURI;
                }
                return "/";
            }

            public StringBuffer getRequestURL () {
                return new StringBuffer(urlToRedirect);
            }
        };
        ctx.setRequest(httpServletRequestWrapper);
        HttpServletRequest request = ctx.getRequest();
        LOGGER.info("PreFilter: " + String
                .format("%s request to %s", request.getMethod(), request.getRequestURL().toString()));

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