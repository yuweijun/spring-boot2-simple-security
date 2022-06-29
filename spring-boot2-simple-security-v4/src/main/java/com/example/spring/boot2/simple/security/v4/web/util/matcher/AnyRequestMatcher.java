package com.example.spring.boot2.simple.security.v4.web.util.matcher;

import javax.servlet.http.HttpServletRequest;

/**
 * @since 2022-06-29.
 */
public final class AnyRequestMatcher implements RequestMatcher {

    public static final RequestMatcher INSTANCE = new AnyRequestMatcher();

    private AnyRequestMatcher() {
    }

    public boolean matches(HttpServletRequest request) {
        return true;
    }

    @Override
    public String toString() {
        return "any request";
    }
}
