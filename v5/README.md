# core classes

AbstractSecurityExpressionHandler
DefaultFilterInvocationSecurityMetadataSource
DefaultWebSecurityExpressionHandler
ExpressionUtils
SecurityExpressionHandler
SecurityExpressionRoot
WebExpressionConfigAttribute
WebExpressionVoter
WebSecurityExpressionRoot
FilterInvocationSecurityMetadataSource
FilterSecurityInterceptor
InterceptorStatusToken
SecurityMetadataSource
AffirmativeBasedDecisionManager
AccessDecisionManager
AccessDecisionVoter

# add last filter FilterSecurityInterceptor in SecurityFilterChain#filters which does authorization by access dicision manager

    this.accessDecisionManager.decide(authentication, object, attributes);

# add filterSecurityInterceptor configuration in WebSecurityConfiguration

    private FilterSecurityInterceptor filterSecurityInterceptor() {
        FilterSecurityInterceptor securityInterceptor = new FilterSecurityInterceptor();
        securityInterceptor.setAccessDecisionManager(accessDecisionManager());
        securityInterceptor.setAuthenticationManager(authenticationManager());
        securityInterceptor.setSecurityMetadataSource(filterInvocationSecurityMetadataSource());
        return securityInterceptor;
    }

====================================================================================================
