package com.example.spring.boot2.simple.security.v6.web.authentication;

import com.example.spring.boot2.simple.security.v6.core.Authentication;
import com.example.spring.boot2.simple.security.v6.web.WebAttributes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * @since 2022-06-29.
 */
public class SimpleUrlAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleUrlAuthenticationSuccessHandler.class);

    protected String defaultTargetUrl = "/";

    protected DefaultRedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        handle(request, response, authentication);
        clearAuthenticationAttributes(request);
    }

    protected void handle(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        if (response.isCommitted()) {
            LOGGER.debug("Response has already been committed. Unable to redirect to url : {}", defaultTargetUrl);
            return;
        }

        redirectStrategy.sendRedirect(request, response, defaultTargetUrl);
    }

    protected final void clearAuthenticationAttributes(HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        if (session == null) {
            return;
        }

        session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
    }

    public void setDefaultTargetUrl(String defaultTargetUrl) {
        this.defaultTargetUrl = defaultTargetUrl;
    }

    public void setRedirectStrategy(DefaultRedirectStrategy redirectStrategy) {
        this.redirectStrategy = redirectStrategy;
    }
}
