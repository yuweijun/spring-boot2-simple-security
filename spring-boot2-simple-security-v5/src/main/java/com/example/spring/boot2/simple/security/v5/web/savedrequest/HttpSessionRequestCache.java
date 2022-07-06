package com.example.spring.boot2.simple.security.v5.web.savedrequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @since 2022-06-29.
 */
public class HttpSessionRequestCache {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpSessionRequestCache.class);

    private static final String SAVED_REQUEST = "SPRING_SECURITY_SAVED_REQUEST";

    private String sessionAttrName = SAVED_REQUEST;

    public void saveRequest(HttpServletRequest request) {
        LOGGER.debug("save request for : ", request.getRequestURI());
        DefaultSavedRequest savedRequest = new DefaultSavedRequest(request);

        // Store the HTTP request itself. Used by
        // AbstractAuthenticationProcessingFilter
        // for redirection after successful authentication (SEC-29)
        request.getSession().setAttribute(this.sessionAttrName, savedRequest);
        LOGGER.debug("DefaultSavedRequest added to Session: " + savedRequest);
    }

    public DefaultSavedRequest getRequest(HttpServletRequest currentRequest) {
        HttpSession session = currentRequest.getSession(false);

        if (session != null) {
            return (DefaultSavedRequest) session.getAttribute(this.sessionAttrName);
        }

        return null;
    }

    public void removeRequest(HttpServletRequest currentRequest, HttpServletResponse response) {
        HttpSession session = currentRequest.getSession(false);

        if (session != null) {
            LOGGER.debug("Removing DefaultSavedRequest from session if present");
            session.removeAttribute(this.sessionAttrName);
        }
    }

    public HttpServletRequest getMatchingRequest(HttpServletRequest request, HttpServletResponse response) {
        DefaultSavedRequest savedRequest = getRequest(request);

        if (!matchesSavedRequest(request, savedRequest)) {
            return null;
        }

        removeRequest(request, response);
        return new SavedRequestAwareWrapper(savedRequest, request);
    }

    private boolean matchesSavedRequest(HttpServletRequest request, DefaultSavedRequest savedRequest) {
        if (savedRequest == null) {
            LOGGER.debug("savedRequest request doesn't exist");
            return false;
        }

        return savedRequest.doesRequestMatch(request);
    }

}
