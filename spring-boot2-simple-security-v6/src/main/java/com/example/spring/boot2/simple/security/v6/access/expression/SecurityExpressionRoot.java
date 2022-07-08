package com.example.spring.boot2.simple.security.v6.access.expression;

import com.example.spring.boot2.simple.security.v6.core.Authentication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * @since 2022-07-07.
 */
public abstract class SecurityExpressionRoot {

    private static final Logger LOGGER = LoggerFactory.getLogger(SecurityExpressionRoot.class);

    /**
     * Allows "permitAll" expression
     */
    public final boolean permitAll = true;

    /**
     * Allows "denyAll" expression
     */
    public final boolean denyAll = false;

    public final String read = "read";
    public final String write = "write";
    public final String create = "create";
    public final String delete = "delete";
    public final String admin = "administration";

    protected final Authentication authentication;

    private Set<String> roles;

    private String defaultRolePrefix = "ROLE_";

    public SecurityExpressionRoot(Authentication authentication) {
        if (authentication == null) {
            throw new IllegalArgumentException("Authentication object cannot be null");
        }
        this.authentication = authentication;
    }

    private static String getRoleWithDefaultPrefix(String defaultRolePrefix, String role) {
        if (role == null) {
            return null;
        }

        if (defaultRolePrefix == null || defaultRolePrefix.length() == 0) {
            return role;
        }

        if (role.startsWith(defaultRolePrefix)) {
            return role;
        }

        return defaultRolePrefix + role;
    }

    public final boolean hasAuthority(String authority) {
        LOGGER.debug("has authority : {}", authority);
        return hasAnyAuthority(authority);
    }

    public final boolean hasAnyAuthority(String... authorities) {
        LOGGER.debug("has any authority : {}", authorities);
        return hasAnyAuthorityName(null, authorities);
    }

    public final boolean hasRole(String role) {
        LOGGER.debug("has role : {}", role);
        return hasAnyRole(role);
    }

    public final boolean hasAnyRole(String... roles) {
        LOGGER.debug("has any role : {}", roles);
        return hasAnyAuthorityName(defaultRolePrefix, roles);
    }

    private boolean hasAnyAuthorityName(String prefix, String... roles) {
        Set<String> roleSet = getAuthoritySet();

        for (String role : roles) {
            String defaultedRole = getRoleWithDefaultPrefix(prefix, role);
            if (roleSet.contains(defaultedRole)) {
                return true;
            }
        }

        return false;
    }

    public final Authentication getAuthentication() {
        return authentication;
    }

    public final boolean permitAll() {
        return true;
    }

    public final boolean denyAll() {
        return false;
    }

    public final boolean isAnonymous() {
        return !authentication.isAuthenticated();
    }

    public final boolean isAuthenticated() {
        return !isAnonymous();
    }

    public Object getPrincipal() {
        return authentication.getPrincipal();
    }

    private Set<String> getAuthoritySet() {
        if (roles == null) {
            Collection<String> userAuthorities = authentication.getAuthorities();
            roles = new HashSet<>(userAuthorities);
        }

        return roles;
    }

    public boolean hasPermission(Object target, Object permission) {
        LOGGER.info("target : {}, permission : {}", target, permission);
        return true;
    }

    public boolean hasPermission(Object targetId, String targetType, Object permission) {
        return true;
    }
}
