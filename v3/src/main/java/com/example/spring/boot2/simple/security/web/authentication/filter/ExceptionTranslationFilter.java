package com.example.spring.boot2.simple.security.web.authentication.filter;

import com.example.spring.boot2.simple.security.core.Authentication;
import com.example.spring.boot2.simple.security.core.AuthenticationException;
import com.example.spring.boot2.simple.security.core.context.SecurityContextHolder;
import com.example.spring.boot2.simple.security.web.access.AccessDeniedException;
import com.example.spring.boot2.simple.security.web.access.AccessDeniedHandler;
import com.example.spring.boot2.simple.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import com.example.spring.boot2.simple.security.web.savedrequest.HttpSessionRequestCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author yuweijun 2022-06-26.
 */
public class ExceptionTranslationFilter extends GenericFilterBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionTranslationFilter.class);

    private AccessDeniedHandler accessDeniedHandler = new AccessDeniedHandler();

    private LoginUrlAuthenticationEntryPoint authenticationEntryPoint = new LoginUrlAuthenticationEntryPoint("/login");

    private HttpSessionRequestCache requestCache = new HttpSessionRequestCache();

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
        throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        try {
            LOGGER.debug("ExceptionTranslationFilter started, Chain processed normally");
            chain.doFilter(request, response);
            LOGGER.debug("ExceptionTranslationFilter finished, Chain processed normally");
        } catch (IOException e) {
            throw e;
        } catch (Exception e) {
            Throwable t = e;
            do {
                LOGGER.debug("current exception is {}", t.getClass().getName());
                if (t instanceof AuthenticationException) {
                    LOGGER.debug("Authentication exception occurred; redirecting to authentication entry point", e);
                    sendStartAuthentication(request, response, chain, (AuthenticationException) t);
                    return;
                } else if (t instanceof AccessDeniedException) {
                    final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                    LOGGER.debug("AccessDeniedException for request : {}, and authentication is : {}", request.getRequestURI(), authentication, e);
                    accessDeniedHandler.handle(request, response, (AccessDeniedException) t);
                    return;
                }
                t = t.getCause();
            } while (t != null);
            throw e;
        }
    }

    protected void sendStartAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, AuthenticationException reason)
        throws ServletException, IOException {
        // Clear the SecurityContextHolder's Authentication, as the existing Authentication is no longer considered valid
        SecurityContextHolder.getContext().setAuthentication(null);
        requestCache.saveRequest(request);

        LOGGER.debug("Calling Authentication entry point.");
        authenticationEntryPoint.commence(request, response, reason);
    }

}
