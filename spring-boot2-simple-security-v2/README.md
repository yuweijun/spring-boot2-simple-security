# core classes

1. PasswordEncoder
2. User
3. UserDetailsService 
4. InMemoryUserDetailsService
5. AuthenticationManager
6. RequestMatcher
7. SecurityFilterChain

# ApplicationFilterConfig object inspect

```
{ApplicationFilterConfig@6808} "ApplicationFilterConfig[name=springSecurityFilterChain, filterClass=org.springframework.boot.web.servlet.DelegatingFilterProxyRegistrationBean$1]"
    filter = {DelegatingFilterProxyRegistrationBean$1@6803}
        this$0 = {DelegatingFilterProxyRegistrationBean@6804} "springSecurityFilterChain urls=[/*] order=-100"
        contextAttribute = null
        webApplicationContext = {AnnotationConfigServletWebServerApplicationContext@6805} "org.springframework.boot.web.servlet.context.AnnotationConfigServletWebServerApplicationContext@60b349a2, started on Sun Jun 26 13:28:45 CST 2022"
        targetBeanName = "springSecurityFilterChain"
        targetFilterLifecycle = false
        delegate = {FilterChainProxy@6792}
```

====================================================================================================
