package com.example.spring.boot2.simple.security.v6.web.authentication;

import com.example.spring.boot2.simple.security.v6.core.Authentication;
import com.example.spring.boot2.simple.security.v6.web.savedrequest.DefaultSavedRequest;
import com.example.spring.boot2.simple.security.v6.web.savedrequest.HttpSessionRequestCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author yuweijun 2022-06-25.
 */
public class SavedRequestAwareAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(SavedRequestAwareAuthenticationSuccessHandler.class);

    private HttpSessionRequestCache requestCache = new HttpSessionRequestCache();

    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        LOGGER.info("login authenticate success for : {}", authentication);

        if (response.isCommitted()) {
            LOGGER.info("response has been committed");
            return;
        }

        DefaultSavedRequest savedRequest = requestCache.getRequest(request);
        if (savedRequest == null) {
            LOGGER.info("NO savedRequest, current request is : {}", request.getRequestURI());
            super.onAuthenticationSuccess(request, response, authentication);

            return;
        }

        String targetUrl = savedRequest.getRedirectUrl();
        LOGGER.debug("Redirecting to DefaultSavedRequest Url: {}", targetUrl);

        redirectStrategy.sendRedirect(request, response, targetUrl);
    }

}
