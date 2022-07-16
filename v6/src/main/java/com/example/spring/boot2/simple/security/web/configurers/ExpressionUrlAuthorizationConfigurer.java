package com.example.spring.boot2.simple.security.web.configurers;

import com.example.spring.boot2.simple.security.access.AccessDecisionVoter;
import com.example.spring.boot2.simple.security.access.ConfigAttribute;
import com.example.spring.boot2.simple.security.access.expression.DefaultFilterInvocationSecurityMetadataSource;
import com.example.spring.boot2.simple.security.access.expression.DefaultWebSecurityExpressionHandler;
import com.example.spring.boot2.simple.security.access.expression.WebExpressionConfigAttribute;
import com.example.spring.boot2.simple.security.access.expression.WebExpressionVoter;
import com.example.spring.boot2.simple.security.access.intercept.FilterInvocationSecurityMetadataSource;
import com.example.spring.boot2.simple.security.access.intercept.FilterSecurityInterceptor;
import com.example.spring.boot2.simple.security.access.vote.AffirmativeBasedDecisionManager;
import com.example.spring.boot2.simple.security.access.AccessDecisionManager;
import com.example.spring.boot2.simple.security.web.DefaultSecurityFilterChain;
import com.example.spring.boot2.simple.security.web.SecurityFilterChain;
import com.example.spring.boot2.simple.security.web.authentication.AuthenticationManager;
import com.example.spring.boot2.simple.security.web.authentication.filter.ExceptionTranslationFilter;
import com.example.spring.boot2.simple.security.web.authentication.filter.RequestCacheAwareFilter;
import com.example.spring.boot2.simple.security.web.authentication.filter.SecurityContextPersistenceFilter;
import com.example.spring.boot2.simple.security.web.authentication.filter.UsernamePasswordAuthenticationFilter;
import com.example.spring.boot2.simple.security.web.authentication.logout.LogoutFilter;
import com.example.spring.boot2.simple.security.web.authentication.logout.SecurityContextLogoutHandler;
import com.example.spring.boot2.simple.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import com.example.spring.boot2.simple.security.web.authentication.ui.DefaultLoginPageGeneratingFilter;
import com.example.spring.boot2.simple.security.web.authentication.ui.DefaultLogoutPageGeneratingFilter;
import com.example.spring.boot2.simple.security.web.util.matcher.AnyRequestMatcher;
import com.example.spring.boot2.simple.security.web.util.matcher.RegexRequestMatcher;
import com.example.spring.boot2.simple.security.web.util.matcher.RequestMatcher;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.Filter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @since 2022-07-09.
 */
public class ExpressionUrlAuthorizationConfigurer {

    /**
     * below filters is not extend from {@link org.springframework.web.filter.OncePerRequestFilter}, it will be added to filterChain if decleared as @Bean
     */
    private final ExceptionTranslationFilter exceptionTranslationFilter = new ExceptionTranslationFilter();

    private final RequestCacheAwareFilter requestCacheAwareFilter = new RequestCacheAwareFilter();

    private final UsernamePasswordAuthenticationFilter usernamePasswordAuthenticationFilter = new UsernamePasswordAuthenticationFilter();

    private final SecurityContextPersistenceFilter securityContextPersistenceFilter = new SecurityContextPersistenceFilter();

    private final DefaultLoginPageGeneratingFilter defaultLoginPageGeneratingFilter = new DefaultLoginPageGeneratingFilter(usernamePasswordAuthenticationFilter);

    private final DefaultLogoutPageGeneratingFilter defaultLogoutPageGeneratingFilter = new DefaultLogoutPageGeneratingFilter();

    private final SecurityContextLogoutHandler securityContextLogoutHandler = new SecurityContextLogoutHandler();

    private final SimpleUrlLogoutSuccessHandler simpleUrlLogoutSuccessHandler = new SimpleUrlLogoutSuccessHandler();

    private final LogoutFilter logoutFilter = new LogoutFilter(simpleUrlLogoutSuccessHandler, securityContextLogoutHandler);

    private AuthenticationManager authenticationManager;

    public ExpressionUrlAuthorizationConfigurer(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
        usernamePasswordAuthenticationFilter.setAuthenticationManager(authenticationManager);
    }

    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    /**
     * {@see org.springframework.security.config.annotation.web.builders.FilterComparator}
     */
    public List<SecurityFilterChain> getFilterChains() {
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
        authorizeFilters.add(logoutFilter);
        authorizeFilters.add(defaultLoginPageGeneratingFilter);
        authorizeFilters.add(defaultLogoutPageGeneratingFilter);
        authorizeFilters.add(requestCacheAwareFilter);

        authorizeFilters.add(filterSecurityInterceptor());

        for (RequestMatcher authorizeRequest : authorizeRequestMatchers) {
            chains.add(new DefaultSecurityFilterChain(authorizeRequest, authorizeFilters));
        }

        return chains;
    }

    protected List<RequestMatcher> getAuthorizeRequestMatchers() {
        final String GET_METHOD = RequestMethod.GET.name();
        List<RequestMatcher> authorizeRequestMatchers = new ArrayList<>();
        authorizeRequestMatchers.add(new RegexRequestMatcher("/login", GET_METHOD));
        authorizeRequestMatchers.add(new RegexRequestMatcher("/logout", GET_METHOD));
        authorizeRequestMatchers.add(new RegexRequestMatcher("/user", GET_METHOD));
        authorizeRequestMatchers.add(new RegexRequestMatcher("/authentication", GET_METHOD));

        authorizeRequestMatchers.add(AnyRequestMatcher.INSTANCE);
        return authorizeRequestMatchers;
    }

    protected List<RequestMatcher> getIgnoreRequestMatchers() {
        final String GET_METHOD = RequestMethod.GET.name();
        List<RequestMatcher> ignoreRequestMatchers = new ArrayList<>();
        ignoreRequestMatchers.add(new RegexRequestMatcher("/", GET_METHOD));
        ignoreRequestMatchers.add(new RegexRequestMatcher("/favicon.ico", GET_METHOD));
        return ignoreRequestMatchers;
    }

    protected SecurityFilterChain getLoginSecurityFilterChain() {
        List<Filter> loginFilters = new ArrayList<>();

        // securityContextPersistenceFilter must before usernamePasswordAuthenticationFilter
        loginFilters.add(securityContextPersistenceFilter);
        loginFilters.add(exceptionTranslationFilter);
        loginFilters.add(usernamePasswordAuthenticationFilter);
        RegexRequestMatcher loginRequestMatcher = new RegexRequestMatcher("/login", RequestMethod.POST.name());
        return new DefaultSecurityFilterChain(loginRequestMatcher, loginFilters);
    }

    /**
     * this AccessDecisionManager is only for web filterSecurityInterceptor to authorize request
     * @return accessDecisionManager which is not the same as {@see MethodSecurityInterceptor.accessDecisionManager}
     */
    protected AccessDecisionManager accessDecisionManager() {
        AffirmativeBasedDecisionManager accessDecisionManager = new AffirmativeBasedDecisionManager();
        List<AccessDecisionVoter<?>> decisionVoters = new ArrayList<>();
        accessDecisionManager.setDecisionVoters(decisionVoters);

        WebExpressionVoter webExpressionVoter = new WebExpressionVoter();
        decisionVoters.add(webExpressionVoter);

        return accessDecisionManager;
    }

    protected FilterSecurityInterceptor filterSecurityInterceptor() {
        FilterSecurityInterceptor securityInterceptor = new FilterSecurityInterceptor();
        securityInterceptor.setAccessDecisionManager(accessDecisionManager());
        securityInterceptor.setSecurityMetadataSource(filterInvocationSecurityMetadataSource());
        return securityInterceptor;
    }

    protected FilterInvocationSecurityMetadataSource filterInvocationSecurityMetadataSource() {
        DefaultWebSecurityExpressionHandler expressionHandler = new DefaultWebSecurityExpressionHandler();
        final ExpressionParser expressionParser = expressionHandler.getExpressionParser();
        final Expression expression = expressionParser.parseExpression("hasAnyRole('ADMIN', 'USER')");
        WebExpressionConfigAttribute webExpressionConfigAttribute = new WebExpressionConfigAttribute(expression);

        Collection<ConfigAttribute> attributes = new ArrayList<>();
        attributes.add(webExpressionConfigAttribute);

        LinkedHashMap<RequestMatcher, Collection<ConfigAttribute>> requestMap = new LinkedHashMap<>();
        final List<RequestMatcher> authorizeRequestMatchers = getAuthorizeRequestMatchers();
        for (RequestMatcher authorizeRequestMatcher : authorizeRequestMatchers) {
            requestMap.put(authorizeRequestMatcher, attributes);
        }

        return new DefaultFilterInvocationSecurityMetadataSource(requestMap);
    }
}
