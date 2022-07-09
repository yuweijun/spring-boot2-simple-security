package com.example.spring.boot2.simple.security.v6.web;

import com.example.spring.boot2.simple.security.v6.web.authentication.AuthenticationManager;
import com.example.spring.boot2.simple.security.v6.web.configurers.ExpressionUrlAuthorizationConfigurer;

import java.util.List;

/**
 * @since 2022-07-09.
 */
public class HttpSecurity {

    private AuthenticationManager authenticationManager;

    public HttpSecurity(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    public List<SecurityFilterChain> getFilterChains() {
        ExpressionUrlAuthorizationConfigurer configurer = new ExpressionUrlAuthorizationConfigurer(authenticationManager);
        return configurer.getFilterChains();
    }

}
