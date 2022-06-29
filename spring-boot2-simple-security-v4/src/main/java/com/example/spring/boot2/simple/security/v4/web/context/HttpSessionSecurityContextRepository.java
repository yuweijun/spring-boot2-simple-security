package com.example.spring.boot2.simple.security.v4.web.context;

import com.example.spring.boot2.simple.security.v4.core.context.SecurityContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author yuweijun 2022-06-25.
 */
public class HttpSessionSecurityContextRepository implements SecurityContextRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpSessionSecurityContextRepository.class);

    public static final String SPRING_SECURITY_CONTEXT_KEY = "SPRING_SECURITY_CONTEXT";

    private String springSecurityContextKey = SPRING_SECURITY_CONTEXT_KEY;

    @Override
    public SecurityContext loadContext(HttpServletRequest request) {
        HttpSession session = request.getSession();
        return (SecurityContext) session.getAttribute(springSecurityContextKey);
    }

    @Override
    public void saveContext(SecurityContext context, HttpServletRequest request, HttpServletResponse response) {
        LOGGER.info("response is committed : {}", response.isCommitted());
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.setAttribute(springSecurityContextKey, context);
        } else {
            LOGGER.info("session has been invalidated");
        }
    }

    @Override
    public boolean containsContext(HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        if (session == null) {
            return false;
        }

        return session.getAttribute(springSecurityContextKey) != null;
    }
}
