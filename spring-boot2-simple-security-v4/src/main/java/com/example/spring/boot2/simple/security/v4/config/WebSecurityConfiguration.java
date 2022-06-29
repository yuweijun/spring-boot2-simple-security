package com.example.spring.boot2.simple.security.v4.config;

import com.example.spring.boot2.simple.security.v4.core.userdetails.InMemoryUserDetailsService;
import com.example.spring.boot2.simple.security.v4.core.userdetails.User;
import com.example.spring.boot2.simple.security.v4.core.userdetails.UserDetailsService;
import com.example.spring.boot2.simple.security.v4.crypto.password.PasswordEncoder;
import com.example.spring.boot2.simple.security.v4.web.DefaultSecurityFilterChain;
import com.example.spring.boot2.simple.security.v4.web.FilterChainProxy;
import com.example.spring.boot2.simple.security.v4.web.SecurityFilterChain;
import com.example.spring.boot2.simple.security.v4.web.authentication.AuthenticationManager;
import com.example.spring.boot2.simple.security.v4.web.authentication.filter.ExceptionTranslationFilter;
import com.example.spring.boot2.simple.security.v4.web.authentication.filter.RequestCacheAwareFilter;
import com.example.spring.boot2.simple.security.v4.web.authentication.filter.SecurityContextPersistenceFilter;
import com.example.spring.boot2.simple.security.v4.web.authentication.filter.UsernamePasswordAuthenticationFilter;
import com.example.spring.boot2.simple.security.v4.web.util.matcher.RegexRequestMatcher;
import com.example.spring.boot2.simple.security.v4.web.util.matcher.RequestMatcher;
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

    /**
     * below filters is not extend from {@link org.springframework.web.filter.OncePerRequestFilter}, it will be added to filterChain if decleared as @Bean
     */
    private final ExceptionTranslationFilter exceptionTranslationFilter = new ExceptionTranslationFilter();

    private final RequestCacheAwareFilter requestCacheAwareFilter = new RequestCacheAwareFilter();

    private final UsernamePasswordAuthenticationFilter usernamePasswordAuthenticationFilter = new UsernamePasswordAuthenticationFilter();

    private final SecurityContextPersistenceFilter securityContextPersistenceFilter = new SecurityContextPersistenceFilter();

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoder.getInstance();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        PasswordEncoder passwordEncoder = passwordEncoder();
        final InMemoryUserDetailsService userDetailsService = new InMemoryUserDetailsService();
        final User.UserBuilder builder = User.builder();
        User user = builder
            .username("user")
            .password(passwordEncoder.encode("user"))
            .roles("USER")
            .build();
        User admin = builder
            .username("admin")
            .password(passwordEncoder.encode("admin"))
            .roles("USER", "ADMIN")
            .build();

        userDetailsService.createUser(user);
        userDetailsService.createUser(admin);

        return userDetailsService;
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        AuthenticationManager authenticationManager = new AuthenticationManager();
        authenticationManager.setPasswordEncoder(passwordEncoder());
        authenticationManager.setUserDetailsService(userDetailsService());
        return authenticationManager;
    }

    /**
     * {@see org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration.springSecurityFilterChain()}
     *
     * {@see org.springframework.security.config.annotation.AbstractSecurityBuilder.build()}
     */
    @Bean
    public FilterChainProxy springSecurityFilterChain() {
        LOGGER.info("config bean : {}", DEFAULT_FILTER_NAME);
        final FilterChainProxy filterChainProxy = new FilterChainProxy();
        usernamePasswordAuthenticationFilter.setAuthenticationManager(authenticationManager());
        filterChainProxy.setChains(getFilterChains());
        return filterChainProxy;
    }

    /**
     * {@see org.springframework.security.config.annotation.web.builders.FilterComparator}
     */
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
        authorizeFilters.add(exceptionTranslationFilter);
        authorizeFilters.add(requestCacheAwareFilter);

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
        loginFilters.add(exceptionTranslationFilter);
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
