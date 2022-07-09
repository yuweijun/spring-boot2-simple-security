package com.example.spring.boot2.simple.security.v6.access.prepost;

import com.example.spring.boot2.simple.security.v6.core.Authentication;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.framework.AopInfrastructureBean;

/**
 * @since 2022-07-09.
 */
public interface PreInvocationAuthorizationAdvice extends AopInfrastructureBean {

    /**
     * The "before" advice which should be executed to perform any filtering necessary and
     * to decide whether the method call is authorised.
     *
     * @param authentication the information on the principal on whose account the
     * decision should be made
     * @param mi the method invocation being attempted
     * @param preInvocationAttribute the attribute built from the @PreFilter and @PostFilter
     * annotations.
     * @return true if authorised, false otherwise
     */
    boolean before(Authentication authentication, MethodInvocation mi, PreInvocationAttribute preInvocationAttribute);

}
