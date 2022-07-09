package com.example.spring.boot2.simple.security.v6.access.intercept;

import com.example.spring.boot2.simple.security.v6.access.AccessDecisionManager;
import com.example.spring.boot2.simple.security.v6.access.ConfigAttribute;
import com.example.spring.boot2.simple.security.v6.core.Authentication;
import com.example.spring.boot2.simple.security.v6.core.AuthenticationException;
import com.example.spring.boot2.simple.security.v6.core.context.SecurityContextHolder;
import com.example.spring.boot2.simple.security.v6.web.access.AccessDeniedException;
import com.example.spring.boot2.simple.security.v6.web.authentication.AuthenticationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

/**
 * @since 2022-07-09.
 */
public abstract class AbstractSecurityInterceptor {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private AccessDecisionManager accessDecisionManager;

    private AuthenticationManager authenticationManager;

    protected abstract SecurityMetadataSource getSecurityMetadataSource();

    public void setAccessDecisionManager(AccessDecisionManager accessDecisionManager) {
        this.accessDecisionManager = accessDecisionManager;
    }

    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    protected InterceptorStatusToken beforeInvocation(Object object) {
        Collection<ConfigAttribute> attributes = this.getSecurityMetadataSource().getAttributes(object);

        if (attributes == null || attributes.isEmpty()) {
            logger.debug("Public object - authentication not attempted");

            return null; // no further work post-invocation
        }

        logger.debug("Secure object: {}; Attributes: {}", object, attributes);

        // authentication
        Authentication authentication = authenticateIfRequired();

        try {
            // authorization
            this.accessDecisionManager.decide(authentication, object, attributes);
        } catch (AccessDeniedException accessDeniedException) {
            logger.error("authentication access denied : {}", authentication);
            throw accessDeniedException;
        }

        logger.debug("Authorization successful");

        // Attempt to run as a different user
        // Authentication runAs = this.runAsManager.buildRunAs(authentication, object, attributes);
        // LOGGER.debug("RunAsManager did not change Authentication object");

        // no further work post-invocation
        return new InterceptorStatusToken(SecurityContextHolder.getContext(), false, attributes, object);
    }

    protected void finallyInvocation(InterceptorStatusToken token) {
        if (token != null && token.isContextHolderRefreshRequired()) {
            logger.debug("Reverting to original Authentication: {}", token.getSecurityContext().getAuthentication());

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
