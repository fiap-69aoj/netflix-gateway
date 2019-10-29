package com.netflix.gateway.gateway.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import com.netflix.zuul.http.HttpServletRequestWrapper;
import org.springframework.util.StringUtils;
import org.springframework.web.util.UrlPathHelper;

import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

/**
 * @author flaoliveira
 * @version : $<br/>
 * : $
 * @since 20/10/2019 14:37
 */
//@Component
public class RedirectFilterBkp extends ZuulFilter {

    private Logger LOGGER = Logger.getLogger(RedirectFilterBkp.class.getCanonicalName());

    private UrlPathHelper urlPathHelper = new UrlPathHelper();

    @Override
    public boolean shouldFilter () {
        return true;
    }

    @Override
    public Object run () throws ZuulException {
        RequestContext ctx = RequestContext.getCurrentContext();
        String remoteHost = ctx.getRequest().getRemoteHost();
        String requestURL = ctx.getRequest().getRequestURL().toString();
        if (!requestURL.contains("proxyurl")) {
            LOGGER.info(String.format("remoteHost %s requestURL %s", remoteHost, requestURL));
            String originatingRequestUri = this.urlPathHelper.getOriginatingRequestUri(ctx.getRequest());
            final String requestURI = this.urlPathHelper.getPathWithinApplication(ctx.getRequest());
            LOGGER.info(String.format("URI %s original URI %s", requestURI, originatingRequestUri));
            String protocol = requestURL.substring(0, requestURL.indexOf("//") + 2);
            String urlWithoutProtocol = requestURL.substring(requestURL.indexOf("//") + 2);
            String[] split = urlWithoutProtocol.substring(0, urlWithoutProtocol.indexOf("/")).split("\\.");
            String subPath = split[0];
            final String newURL = protocol + "." + split[1] + "." + split[2];
            //Here the main thing is to create a HttpServletRequestWrapper and override the request coming from the actual request
            HttpServletRequestWrapper httpServletRequestWrapper = new HttpServletRequestWrapper(ctx.getRequest()) {
                public String getRequestURI() {
                    if (requestURI != null && !requestURI.equals("/")) {
                        if (!StringUtils.isEmpty(subPath)) {
                            return "/" + subPath + requestURI;
                        } else {
                            return requestURI;
                        }
                    }
                    if (!StringUtils.isEmpty(subPath)) {
                        return "/" + subPath;
                    } else {
                        return "/";
                    }
                }
                public StringBuffer getRequestURL() {
                    return new StringBuffer(newURL);
                }
            };
            ctx.setRequest(httpServletRequestWrapper);
            HttpServletRequest request = ctx.getRequest();
            LOGGER.info("PreFilter: " + String.format("%s request to %s", request.getMethod(), request.getRequestURL().toString()));
        }
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
