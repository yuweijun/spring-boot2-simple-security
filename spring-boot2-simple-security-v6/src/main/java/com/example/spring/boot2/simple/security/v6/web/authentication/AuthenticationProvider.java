package com.example.spring.boot2.simple.security.v6.web.authentication;

import com.example.spring.boot2.simple.security.v6.core.Authentication;
import com.example.spring.boot2.simple.security.v6.core.AuthenticationException;

/**
 * @since 2022-06-28.
 */
public interface AuthenticationProvider {

    Authentication authenticate(Authentication authentication) throws AuthenticationException;

}
