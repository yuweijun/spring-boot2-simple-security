package com.example.spring.boot2.simple.security.v4.web.authentication;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @since 2022-06-29.
 */
public class DefaultRedirectStrategy {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultRedirectStrategy.class);

    public void sendRedirect(HttpServletRequest request, HttpServletResponse response, String redirectUrl) throws IOException {
        String url = response.encodeRedirectURL(redirectUrl);
        LOGGER.info("Redirecting to '" + url + "'");

        response.sendRedirect(url);
    }

}
