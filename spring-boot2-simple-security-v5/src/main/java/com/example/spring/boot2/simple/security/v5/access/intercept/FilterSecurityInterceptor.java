package com.example.spring.boot2.simple.security.v5.access.intercept;

import com.example.spring.boot2.simple.security.v5.access.AccessDecisionManager;
import com.example.spring.boot2.simple.security.v5.access.ConfigAttribute;
import com.example.spring.boot2.simple.security.v5.core.Authentication;
import com.example.spring.boot2.simple.security.v5.core.context.SecurityContextHolder;
import com.example.spring.boot2.simple.security.v5.web.FilterInvocation;
import com.example.spring.boot2.simple.security.v5.web.access.AccessDeniedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;

/**
 * @since 2022-07-06.
 */
public class FilterSecurityInterceptor extends GenericFilterBean implements Filter {

    private static final Logger LOGGER = LoggerFactory.getLogger(FilterSecurityInterceptor.class);

    private AccessDecisionManager accessDecisionManager;

    public AccessDecisionManager getAccessDecisionManager() {
        return accessDecisionManager;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        FilterInvocation fi = new FilterInvocation(request, response, chain);
        invoke(fi);
    }

    public void invoke(FilterInvocation fi) throws IOException, ServletException {

        InterceptorStatusToken token = beforeInvocation(fi);

        try {
            fi.getChain().doFilter(fi.getRequest(), fi.getResponse());
        } finally {
            finallyInvocation(token);
        }

        afterInvocation(token, null);
    }

    protected InterceptorStatusToken beforeInvocation(Object object) {
        Assert.notNull(object, "Object was null");
        Collection<ConfigAttribute> attributes = Collections.EMPTY_LIST;

        // Collection<ConfigAttribute> attributes = this.obtainSecurityMetadataSource().getAttributes(object);
        //
        // if (attributes == null || attributes.isEmpty()) {
        //     logger.debug("Public object - authentication not attempted");
        //
        //     return null; // no further work post-invocation
        // }
        // LOGGER.debug("Secure object: " + object + "; Attributes: " + attributes);

        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            throw new AccessDeniedException("An Authentication object was not found in the SecurityContext");
        }

        Authentication authenticated = authenticateIfRequired();

        // Attempt authorization
        try {
            this.accessDecisionManager.decide(authenticated, object, attributes);
        } catch (AccessDeniedException accessDeniedException) {
            throw accessDeniedException;
        }

        LOGGER.debug("Authorization successful");

        // Attempt to run as a different user
        // Authentication runAs = this.runAsManager.buildRunAs(authenticated, object, attributes);
        LOGGER.debug("RunAsManager did not change Authentication object");

        // no further work post-invocation
        return new InterceptorStatusToken(SecurityContextHolder.getContext(), false, attributes, object);
    }

    protected void finallyInvocation(InterceptorStatusToken token) {
        if (token != null && token.isContextHolderRefreshRequired()) {
            LOGGER.debug("Reverting to original Authentication: {}", token.getSecurityContext().getAuthentication());

            SecurityContextHolder.setContext(token.getSecurityContext());
        }
    }

    protected Object afterInvocation(InterceptorStatusToken token, Object returnedObject) {
        return returnedObject;
    }

    private Authentication authenticateIfRequired() {
        return SecurityContextHolder.getContext().getAuthentication();
    }
}
