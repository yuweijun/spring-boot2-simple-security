package com.example.spring.boot2.simple.security.access.expression.method;

import com.example.spring.boot2.simple.security.core.Authentication;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.aop.support.AopUtils;
import org.springframework.context.expression.MethodBasedEvaluationContext;
import org.springframework.core.ParameterNameDiscoverer;

import java.lang.reflect.Method;

/**
 * @since 2022-07-08.
 */
class MethodSecurityEvaluationContext extends MethodBasedEvaluationContext {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodSecurityEvaluationContext.class);

    MethodSecurityEvaluationContext(Authentication user, MethodInvocation mi, ParameterNameDiscoverer parameterNameDiscoverer) {
        super(mi.getThis(), getSpecificMethod(mi), mi.getArguments(), parameterNameDiscoverer);
    }

    private static Method getSpecificMethod(MethodInvocation mi) {
        LOGGER.info("MethodInvocation : {}", mi);
        return AopUtils.getMostSpecificMethod(mi.getMethod(), AopProxyUtils.ultimateTargetClass(mi.getThis()));
    }
}
