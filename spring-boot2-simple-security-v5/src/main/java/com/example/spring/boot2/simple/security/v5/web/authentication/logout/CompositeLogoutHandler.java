package com.example.spring.boot2.simple.security.v5.web.authentication.logout;

import com.example.spring.boot2.simple.security.v5.core.Authentication;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

/**
 * @since 2022-06-29.
 */
public class CompositeLogoutHandler implements LogoutHandler {

    private final List<LogoutHandler> logoutHandlers;

    public CompositeLogoutHandler(LogoutHandler... logoutHandlers) {
        Assert.notEmpty(logoutHandlers, "LogoutHandlers are required");
        this.logoutHandlers = Arrays.asList(logoutHandlers);
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        for (LogoutHandler handler : this.logoutHandlers) {
            handler.logout(request, response, authentication);
        }
    }
}
