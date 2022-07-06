package com.example.spring.boot2.simple.security.v4.web.authentication;

import com.example.spring.boot2.simple.security.v4.core.Authentication;
import com.example.spring.boot2.simple.security.v4.core.AuthenticationException;

/**
 * @author yuweijun 2022-06-25.
 */
public class AuthenticationManager {

    private AuthenticationProvider authenticationProvider;

    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        return authenticationProvider.authenticate(authentication);
    }

    public void setAuthenticationProvider(AuthenticationProvider authenticationProvider) {
        this.authenticationProvider = authenticationProvider;
    }
}
