package com.example.spring.boot2.simple.security.v5.web.authentication.logout;

import com.example.spring.boot2.simple.security.v5.core.Authentication;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @since 2022-06-29.
 */
public interface LogoutSuccessHandler {

    void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException;

}
