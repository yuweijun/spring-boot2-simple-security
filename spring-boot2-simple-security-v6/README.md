# core classes

ExpressionUrlAuthorizationConfigurer
HttpSecurity
DefaultSecurityParameterNameDiscoverer
AnnotationParameterNameDiscoverer
MethodSecurityMetadataSourceAdvisorRegistrar
GlobalMethodSecuritySelector
GlobalMethodSecurityConfiguration
EnableGlobalMethodSecurity
PrePostInvocationAttributeFactory
PrePostAnnotationSecurityMetadataSource
PreInvocationAuthorizationAdviceVoter
PreInvocationAuthorizationAdvice
PreInvocationAttribute
PreAuthorize
MethodSecurityMetadataSource
AbstractMethodSecurityMetadataSource
MethodSecurityMetadataSourceAdvisor
MethodSecurityInterceptor
AbstractSecurityInterceptor
PreInvocationExpressionAttribute
MethodSecurityExpressionRoot
MethodSecurityExpressionHandler
MethodSecurityEvaluationContext
ExpressionBasedPreInvocationAdvice
ExpressionBasedAnnotationAttributeFactory
DefaultMethodSecurityExpressionHandler
AbstractExpressionBasedMethodConfigAttribute

# AutoProxyRegistrar config error issue

    AutoProxyRegistrar was imported but no annotations were found having both 'mode' and 'proxyTargetClass' attributes of type AdviceMode
    and boolean respectively. This means that auto proxy creator registration and configuration may not have occurred as intended, 
    and components may not be proxied as expected. 

    Check to ensure that AutoProxyRegistrar has been @Import'ed on the same class where these annotations are declared; 
    otherwise remove the import of AutoProxyRegistrar altogether.

## fix above issue by adding properties proxyTargetClass and mode for EnableGlobalMethodSecurity

add @EnableGlobalMethodSecurity in class WebSecurityConfiguration which imported by annotation @EnableSimpleWebSecurity

    public @interface EnableGlobalMethodSecurity {
        boolean proxyTargetClass() default false;
        AdviceMode mode() default AdviceMode.PROXY;
    }


====================================================================================================
