package com.example.spring.boot2.simple.security.v3.web.authentication;

import com.example.spring.boot2.simple.security.v3.core.AuthenticationException;
import org.springframework.util.Assert;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @since 2022-06-29.
 */
public class LoginUrlAuthenticationEntryPoint {

    private String loginFormUrl;

    private DefaultRedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    public LoginUrlAuthenticationEntryPoint(String loginFormUrl) {
        Assert.notNull(loginFormUrl, "loginFormUrl cannot be null");
        this.loginFormUrl = loginFormUrl;
    }

    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authenticationException) throws IOException, ServletException {
        String redirectUrl = getLoginFormUrl();
        redirectStrategy.sendRedirect(request, response, redirectUrl);
    }

    public String getLoginFormUrl() {
        return loginFormUrl;
    }
}
