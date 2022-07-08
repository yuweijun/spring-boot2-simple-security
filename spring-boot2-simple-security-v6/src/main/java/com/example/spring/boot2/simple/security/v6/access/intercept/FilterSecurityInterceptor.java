package com.example.spring.boot2.simple.security.v6.access.intercept;

import com.example.spring.boot2.simple.security.v6.access.AccessDecisionManager;
import com.example.spring.boot2.simple.security.v6.access.ConfigAttribute;
import com.example.spring.boot2.simple.security.v6.core.Authentication;
import com.example.spring.boot2.simple.security.v6.core.AuthenticationException;
import com.example.spring.boot2.simple.security.v6.core.context.SecurityContextHolder;
import com.example.spring.boot2.simple.security.v6.web.FilterInvocation;
import com.example.spring.boot2.simple.security.v6.web.access.AccessDeniedException;
import com.example.spring.boot2.simple.security.v6.web.authentication.AuthenticationManager;
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

/**
 * @since 2022-07-06.
 */
public class FilterSecurityInterceptor extends GenericFilterBean implements Filter {

    private static final Logger LOGGER = LoggerFactory.getLogger(FilterSecurityInterceptor.class);

    private AccessDecisionManager accessDecisionManager;

    private AuthenticationManager authenticationManager;

    private FilterInvocationSecurityMetadataSource securityMetadataSource;

    public void setAccessDecisionManager(AccessDecisionManager accessDecisionManager) {
        this.accessDecisionManager = accessDecisionManager;
    }

    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    public void setSecurityMetadataSource(FilterInvocationSecurityMetadataSource securityMetadataSource) {
        this.securityMetadataSource = securityMetadataSource;
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
        Collection<ConfigAttribute> attributes = this.securityMetadataSource.getAttributes(object);

        if (attributes == null || attributes.isEmpty()) {
            logger.debug("Public object - authentication not attempted");

            return null; // no further work post-invocation
        }

        LOGGER.debug("Secure object: " + object + "; Attributes: " + attributes);

        // authentication
        Authentication authentication = authenticateIfRequired();

        try {
            // authorization
            this.accessDecisionManager.decide(authentication, object, attributes);
        } catch (AccessDeniedException accessDeniedException) {
            LOGGER.error("authentication access denied : {}", authentication);
            throw accessDeniedException;
        }

        LOGGER.debug("Authorization successful");

        // Attempt to run as a different user
        // Authentication runAs = this.runAsManager.buildRunAs(authentication, object, attributes);
        // LOGGER.debug("RunAsManager did not change Authentication object");

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
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            throw new AuthenticationException("An Authentication object was not found in the SecurityContext");
        }

        return SecurityContextHolder.getContext().getAuthentication();
    }
}
