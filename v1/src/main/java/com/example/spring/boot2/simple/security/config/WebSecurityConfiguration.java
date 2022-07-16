package com.example.spring.boot2.simple.security.config;

import com.example.spring.boot2.simple.security.web.FilterChainProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

/**
 * @author yuweijun 2022-06-26.
 */
public class WebSecurityConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebSecurityConfiguration.class);

    /**
     *
     * {@see org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration.springSecurityFilterChain()}
     */
    @Bean
    public FilterRegistrationBean<FilterChainProxy> filterChainProxy() {
        LOGGER.info("config filter bean : filterChainProxy");
        FilterRegistrationBean<FilterChainProxy> bean = new FilterRegistrationBean<>();
        bean.setFilter(new FilterChainProxy());
        bean.setName("filterChainProxy");
        bean.addUrlPatterns("/*");
        bean.setOrder(21);
        return bean;
    }
}
