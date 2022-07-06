package com.example.spring.boot2.simple.security.v5.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author yuweijun 2022-06-25.
 */
public class FilterChainProxy extends GenericFilterBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(FilterChainProxy.class);

    private List<SecurityFilterChain> chains = new ArrayList<>();

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        List<Filter> filters = getFilters((HttpServletRequest) request);

        if (filters.size() == 0) {
            LOGGER.info("filter size is 0 for request : {}", ((HttpServletRequest) request).getRequestURI());
            chain.doFilter(request, response);

            return;
        }

        VirtualFilterChain vfc = new VirtualFilterChain(chain, filters);
        vfc.doFilter(request, response);
    }

    /**
     * {@see org.springframework.security.config.annotation.web.builders.FilterComparator}
     * @param request
     */
    private List<Filter> getFilters(HttpServletRequest request) {
        for (SecurityFilterChain chain : chains) {
            if (chain.matches(request)) {
                LOGGER.info("match request with chain : {}", chain);
                return chain.getFilters();
            }
        }

        return Collections.emptyList();
    }

    public List<SecurityFilterChain> getChains() {
        return chains;
    }

    public void setChains(List<SecurityFilterChain> chains) {
        this.chains = chains;
    }
}
