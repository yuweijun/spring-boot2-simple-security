package com.example.spring.boot2.simple.security.web.authentication.logout;

import com.example.spring.boot2.simple.security.core.Authentication;
import com.example.spring.boot2.simple.security.web.authentication.DefaultRedirectStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @since 2022-06-29.
 */
public class SimpleUrlLogoutSuccessHandler implements LogoutSuccessHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleUrlLogoutSuccessHandler.class);

    private String defaultTargetUrl = "/";

    protected DefaultRedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        LOGGER.info("logout success for authentication : {}", authentication);
        handle(request, response, authentication);
    }

    protected void handle(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        if (response.isCommitted()) {
            LOGGER.debug("Response has already been committed. Unable to redirect to url : {}", defaultTargetUrl);
            return;
        }

        redirectStrategy.sendRedirect(request, response, defaultTargetUrl);
    }

    public void setDefaultTargetUrl(String defaultTargetUrl) {
        this.defaultTargetUrl = defaultTargetUrl;
    }
}

