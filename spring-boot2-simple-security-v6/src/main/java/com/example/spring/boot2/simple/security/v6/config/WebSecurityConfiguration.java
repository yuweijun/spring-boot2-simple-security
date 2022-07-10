package com.example.spring.boot2.simple.security.v6.config;

import com.example.spring.boot2.simple.security.v6.access.AccessDecisionManager;
import com.example.spring.boot2.simple.security.v6.access.AccessDecisionVoter;
import com.example.spring.boot2.simple.security.v6.access.expression.method.DefaultMethodSecurityExpressionHandler;
import com.example.spring.boot2.simple.security.v6.access.expression.method.ExpressionBasedAnnotationAttributeFactory;
import com.example.spring.boot2.simple.security.v6.access.expression.method.ExpressionBasedPreInvocationAdvice;
import com.example.spring.boot2.simple.security.v6.access.expression.method.MethodSecurityExpressionHandler;
import com.example.spring.boot2.simple.security.v6.access.intercept.aopalliance.MethodSecurityInterceptor;
import com.example.spring.boot2.simple.security.v6.access.method.MethodSecurityMetadataSource;
import com.example.spring.boot2.simple.security.v6.access.prepost.PreInvocationAuthorizationAdviceVoter;
import com.example.spring.boot2.simple.security.v6.access.prepost.PrePostAnnotationSecurityMetadataSource;
import com.example.spring.boot2.simple.security.v6.access.vote.AffirmativeBasedDecisionManager;
import com.example.spring.boot2.simple.security.v6.config.method.configuration.EnableGlobalMethodSecurity;
import com.example.spring.boot2.simple.security.v6.core.userdetails.InMemoryUserDetailsService;
import com.example.spring.boot2.simple.security.v6.core.userdetails.User;
import com.example.spring.boot2.simple.security.v6.core.userdetails.UserDetailsService;
import com.example.spring.boot2.simple.security.v6.crypto.password.PasswordEncoder;
import com.example.spring.boot2.simple.security.v6.web.FilterChainProxy;
import com.example.spring.boot2.simple.security.v6.web.HttpSecurity;
import com.example.spring.boot2.simple.security.v6.web.authentication.AuthenticationManager;
import com.example.spring.boot2.simple.security.v6.web.authentication.dao.DaoAuthenticationProvider;
import org.aopalliance.intercept.MethodInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.web.servlet.DelegatingFilterProxyRegistrationBean;
import org.springframework.boot.web.servlet.filter.OrderedFilter;
import org.springframework.context.annotation.Bean;

import javax.servlet.DispatcherType;
import java.util.ArrayList;
import java.util.List;

@EnableGlobalMethodSecurity
public class WebSecurityConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebSecurityConfiguration.class);

    public static final String DEFAULT_FILTER_NAME = "springSecurityFilterChain";

    public static final int DEFAULT_FILTER_ORDER = OrderedFilter.REQUEST_WRAPPER_FILTER_MAX_ORDER - 100;

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
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(userDetailsService());
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        AuthenticationManager authenticationManager = new AuthenticationManager();
        authenticationManager.setAuthenticationProvider(daoAuthenticationProvider());
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
        HttpSecurity httpSecurity = new HttpSecurity(authenticationManager());
        final FilterChainProxy filterChainProxy = new FilterChainProxy();
        filterChainProxy.setChains(httpSecurity.getFilterChains());

        return filterChainProxy;
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

    @Bean
    public DefaultMethodSecurityExpressionHandler getMethodSecurityExpressionHandler() {
        return new DefaultMethodSecurityExpressionHandler();
    }


    @Bean
    public MethodSecurityMetadataSource methodSecurityMetadataSource() {
        ExpressionBasedAnnotationAttributeFactory attributeFactory = new ExpressionBasedAnnotationAttributeFactory(getMethodSecurityExpressionHandler());
        return new PrePostAnnotationSecurityMetadataSource(attributeFactory);
    }

    /**
     * define advice bean for {@see com.example.spring.boot2.simple.security.v6.access.intercept.aopalliance.MethodSecurityMetadataSourceAdvisor}
     */
    @Bean
    public MethodInterceptor methodSecurityInterceptor() {
        LOGGER.info("config advice bean 'methodSecurityInterceptor' for advisor bean 'methodSecurityMetadataSourceAdvisor'");

        MethodSecurityInterceptor methodSecurityInterceptor = new MethodSecurityInterceptor();
        methodSecurityInterceptor.setAccessDecisionManager(accessDecisionManager());
        methodSecurityInterceptor.setSecurityMetadataSource(methodSecurityMetadataSource());
        methodSecurityInterceptor.setAuthenticationManager(authenticationManager());

        return methodSecurityInterceptor;
    }

    private AccessDecisionManager accessDecisionManager() {
        AffirmativeBasedDecisionManager accessDecisionManager = new AffirmativeBasedDecisionManager();
        List<AccessDecisionVoter<?>> decisionVoters = new ArrayList<>();
        accessDecisionManager.setDecisionVoters(decisionVoters);

        MethodSecurityExpressionHandler defaultMethodExpressionHandler = getMethodSecurityExpressionHandler();
        ExpressionBasedPreInvocationAdvice expressionAdvice = new ExpressionBasedPreInvocationAdvice();
        expressionAdvice.setExpressionHandler(defaultMethodExpressionHandler);
        PreInvocationAuthorizationAdviceVoter preInvocationAuthorizationVoter = new PreInvocationAuthorizationAdviceVoter(expressionAdvice);
        decisionVoters.add(preInvocationAuthorizationVoter);

        return accessDecisionManager;
    }
}
