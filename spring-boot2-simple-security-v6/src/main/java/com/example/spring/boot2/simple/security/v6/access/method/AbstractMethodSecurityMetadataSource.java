package com.example.spring.boot2.simple.security.v6.access.method;

import com.example.spring.boot2.simple.security.v6.access.ConfigAttribute;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.AopProxyUtils;

import java.util.Collection;

/**
 * @since 2022-07-08.
 */
public abstract class AbstractMethodSecurityMetadataSource implements MethodSecurityMetadataSource {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    public final Collection<ConfigAttribute> getAttributes(Object object) {
        if (object instanceof MethodInvocation) {
            MethodInvocation mi = (MethodInvocation) object;
            Object target = mi.getThis();
            Class<?> targetClass = null;

            if (target != null) {
                targetClass = target instanceof Class<?> ? (Class<?>) target : AopProxyUtils.ultimateTargetClass(target);
            }

            Collection<ConfigAttribute> attrs = getAttributes(mi.getMethod(), targetClass);
            logger.debug("get attributes : {} from targetClass", attrs, targetClass);
            if (attrs != null && !attrs.isEmpty()) {
                return attrs;
            }

            if (target != null && !(target instanceof Class<?>)) {
                attrs = getAttributes(mi.getMethod(), target.getClass());
                logger.debug("get attributes : {} from targetClass", attrs, target.getClass());
            }
            return attrs;
        }

        throw new IllegalArgumentException("Object must be a non-null MethodInvocation");
    }

}
