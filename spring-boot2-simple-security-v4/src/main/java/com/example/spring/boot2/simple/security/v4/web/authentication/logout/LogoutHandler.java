package com.example.spring.boot2.simple.security.v4.web.authentication.logout;

import com.example.spring.boot2.simple.security.v4.core.Authentication;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @since 2022-06-29.
 */
public interface LogoutHandler {

    /**
     * Causes a logout to be completed. The method must complete successfully.
     *
     * @param request the HTTP request
     * @param response the HTTP response
     * @param authentication the current principal details
     */
    void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication);

}
