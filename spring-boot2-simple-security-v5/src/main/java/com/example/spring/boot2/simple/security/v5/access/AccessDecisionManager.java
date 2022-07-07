package com.example.spring.boot2.simple.security.v5.access;

import com.example.spring.boot2.simple.security.v5.core.Authentication;
import com.example.spring.boot2.simple.security.v5.web.access.AccessDeniedException;

import java.util.Collection;

/**
 * @since 2022-07-06.
 */
public interface AccessDecisionManager {

    void decide(Authentication authentication, Object object, Collection<ConfigAttribute> configAttributes) throws AccessDeniedException;

}
