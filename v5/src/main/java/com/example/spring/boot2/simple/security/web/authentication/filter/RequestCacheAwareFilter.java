package com.example.spring.boot2.simple.security.web.authentication.filter;

import com.example.spring.boot2.simple.security.web.savedrequest.HttpSessionRequestCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @since 2022-06-29.
 */
public class RequestCacheAwareFilter extends GenericFilterBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(RequestCacheAwareFilter.class);

    private HttpSessionRequestCache requestCache = new HttpSessionRequestCache();

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
        FilterChain chain) throws IOException, ServletException {

        final HttpServletRequest savedRequest = requestCache.getMatchingRequest((HttpServletRequest) request, (HttpServletResponse) response);
        if (savedRequest != null) {
            LOGGER.info("get cached request : {}", savedRequest.getRequestURI());
            chain.doFilter(savedRequest, response);
        } else {
            chain.doFilter(request, response);
        }
    }

}
