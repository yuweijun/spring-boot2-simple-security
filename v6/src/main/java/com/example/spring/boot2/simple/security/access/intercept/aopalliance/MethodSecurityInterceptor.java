package com.example.spring.boot2.simple.security.access.intercept.aopalliance;

import com.example.spring.boot2.simple.security.access.intercept.AbstractSecurityInterceptor;
import com.example.spring.boot2.simple.security.access.intercept.InterceptorStatusToken;
import com.example.spring.boot2.simple.security.access.method.MethodSecurityMetadataSource;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

/**
 * @since 2022-07-09.
 */
public class MethodSecurityInterceptor extends AbstractSecurityInterceptor implements MethodInterceptor {

    private MethodSecurityMetadataSource securityMetadataSource;

    /**
     * This method should be used to enforce security on a <code>MethodInvocation</code>.
     *
     * @param mi The method being invoked which requires a security decision
     * @return The returned value from the method invocation (possibly modified by the {@code AfterInvocationManager}).
     * @throws Throwable if any error occurs
     */
    @Override
    public Object invoke(MethodInvocation mi) throws Throwable {
        InterceptorStatusToken token = super.beforeInvocation(mi);

        Object result;
        try {
            result = mi.proceed();
        } finally {
            super.finallyInvocation(token);
        }
        return super.afterInvocation(token, result);
    }

    @Override
    public MethodSecurityMetadataSource getSecurityMetadataSource() {
        return this.securityMetadataSource;
    }

    public void setSecurityMetadataSource(MethodSecurityMetadataSource securityMetadataSource) {
        this.securityMetadataSource = securityMetadataSource;
    }
}
