package com.example.spring.boot2.simple.security.v5.access.intercept;

import com.example.spring.boot2.simple.security.v5.access.ConfigAttribute;
import com.example.spring.boot2.simple.security.v5.core.context.SecurityContext;

import java.util.Collection;

/**
 * @since 2022-07-06.
 */
public class InterceptorStatusToken {
    // ~ Instance fields
    // ================================================================================================

    private SecurityContext securityContext;
    private Collection<ConfigAttribute> attr;
    private Object secureObject;
    private boolean contextHolderRefreshRequired;

    // ~ Constructors
    // ===================================================================================================

    public InterceptorStatusToken(SecurityContext securityContext,
        boolean contextHolderRefreshRequired, Collection<ConfigAttribute> attributes,
        Object secureObject) {
        this.securityContext = securityContext;
        this.contextHolderRefreshRequired = contextHolderRefreshRequired;
        this.attr = attributes;
        this.secureObject = secureObject;
    }

    // ~ Methods
    // ========================================================================================================

    public Collection<ConfigAttribute> getAttributes() {
        return attr;
    }

    public SecurityContext getSecurityContext() {
        return securityContext;
    }

    public Object getSecureObject() {
        return secureObject;
    }

    public boolean isContextHolderRefreshRequired() {
        return contextHolderRefreshRequired;
    }
}
