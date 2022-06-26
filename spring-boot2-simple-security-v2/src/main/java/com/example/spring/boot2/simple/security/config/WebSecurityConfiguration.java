package com.example.spring.boot2.simple.security.config;

import com.example.spring.boot2.simple.security.web.DefaultSecurityFilterChain;
import com.example.spring.boot2.simple.security.web.FilterChainProxy;
import com.example.spring.boot2.simple.security.web.SecurityFilterChain;
import com.example.spring.boot2.simple.security.web.authentication.filter.SecurityContextPersistenceFilter;
import com.example.spring.boot2.simple.security.web.authentication.filter.UsernamePasswordAuthenticationFilter;
import com.example.spring.boot2.simple.security.web.util.matcher.RegexRequestMatcher;
import com.example.spring.boot2.simple.security.web.util.matcher.RequestMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.web.servlet.DelegatingFilterProxyRegistrationBean;
import org.springframework.boot.web.servlet.filter.OrderedFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import java.util.ArrayList;
import java.util.List;

public class WebSecurityConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebSecurityConfiguration.class);

    public static final String DEFAULT_FILTER_NAME = "springSecurityFilterChain";

    public static final int DEFAULT_FILTER_ORDER = OrderedFilter.REQUEST_WRAPPER_FILTER_MAX_ORDER - 100;

    private UsernamePasswordAuthenticationFilter usernamePasswordAuthenticationFilter = new UsernamePasswordAuthenticationFilter();

    private SecurityContextPersistenceFilter securityContextPersistenceFilter = new SecurityContextPersistenceFilter();

    /**
     * <pre>
     * {ApplicationFilterConfig@6808} "ApplicationFilterConfig[name=springSecurityFilterChain, filterClass=org.springframework.boot.web.servlet.DelegatingFilterProxyRegistrationBean$1]"
     *     filter = {DelegatingFilterProxyRegistrationBean$1@6803}
     *         this$0 = {DelegatingFilterProxyRegistrationBean@6804} "springSecurityFilterChain urls=[/*] order=-100"
     *         contextAttribute = null
     *         webApplicationContext = {AnnotationConfigServletWebServerApplicationContext@6805} "org.springframework.boot.web.servlet.context.AnnotationConfigServletWebServerApplicationContext@60b349a2, started on Sun Jun 26 13:28:45 CST 2022"
     *         targetBeanName = "springSecurityFilterChain"
     *         targetFilterLifecycle = false
     *         delegate = {FilterChainProxy@6792}
     * </pre>
     * {@see org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration.springSecurityFilterChain()} {@see
     * org.springframework.security.config.annotation.AbstractSecurityBuilder.build()}
     */
    @Bean
    public FilterChainProxy springSecurityFilterChain() {
        LOGGER.info("config bean : {}", DEFAULT_FILTER_NAME);
        final FilterChainProxy filterChainProxy = new FilterChainProxy();
        filterChainProxy.setChains(getFilterChains());
        return filterChainProxy;
    }

    private List<SecurityFilterChain> getFilterChains() {
        List<SecurityFilterChain> chains = new ArrayList<>();

        SecurityFilterChain loginChain = getLoginSecurityFilterChain();
        chains.add(loginChain);

        List<RequestMatcher> ignoreRequestMatchers = getIgnoreRequestMatchers();
        for (RequestMatcher ignoreRequest : ignoreRequestMatchers) {
            chains.add(new DefaultSecurityFilterChain(ignoreRequest));
        }

        List<RequestMatcher> authorizeRequestMatchers = getAuthorizeRequestMatchers();
        List<Filter> authorizeFilters = new ArrayList<>();
        authorizeFilters.add(securityContextPersistenceFilter);

        for (RequestMatcher authorizeRequest : authorizeRequestMatchers) {
            chains.add(new DefaultSecurityFilterChain(authorizeRequest, authorizeFilters));
        }

        return chains;
    }

    private List<RequestMatcher> getAuthorizeRequestMatchers() {
        List<RequestMatcher> authorizeRequestMatchers = new ArrayList<>();
        authorizeRequestMatchers.add(new RegexRequestMatcher("/login", RequestMethod.GET.name()));
        authorizeRequestMatchers.add(new RegexRequestMatcher("/user", RequestMethod.GET.name()));
        authorizeRequestMatchers.add(new RegexRequestMatcher("/authentication", RequestMethod.GET.name()));
        return authorizeRequestMatchers;
    }

    private List<RequestMatcher> getIgnoreRequestMatchers() {
        List<RequestMatcher> ignoreRequestMatchers = new ArrayList<>();
        ignoreRequestMatchers.add(new RegexRequestMatcher("/", RequestMethod.GET.name()));
        ignoreRequestMatchers.add(new RegexRequestMatcher("/favicon.ico", RequestMethod.GET.name()));
        return ignoreRequestMatchers;
    }

    private SecurityFilterChain getLoginSecurityFilterChain() {
        List<Filter> loginFilters = new ArrayList<>();

        // securityContextPersistenceFilter must before usernamePasswordAuthenticationFilter
        loginFilters.add(securityContextPersistenceFilter);
        loginFilters.add(usernamePasswordAuthenticationFilter);
        RegexRequestMatcher loginRequestMatcher = new RegexRequestMatcher("/login", RequestMethod.POST.name());
        return new DefaultSecurityFilterChain(loginRequestMatcher, loginFilters);
    }

    /**
     * {@see org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration}
     */
    @Bean
    @ConditionalOnBean(name = DEFAULT_FILTER_NAME)
    public DelegatingFilterProxyRegistrationBean securityFilterChainRegistration() {
        LOGGER.info("config bean : securityFilterChainRegistration");
        DelegatingFilterProxyRegistrationBean registration = new DelegatingFilterProxyRegistrationBean(DEFAULT_FILTER_NAME);
        registration.setOrder(DEFAULT_FILTER_ORDER);
        registration.setDispatcherTypes(DispatcherType.ASYNC, DispatcherType.ERROR, DispatcherType.REQUEST);
        return registration;
    }

}
