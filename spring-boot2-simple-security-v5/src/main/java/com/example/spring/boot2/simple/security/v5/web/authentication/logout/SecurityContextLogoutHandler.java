package com.example.spring.boot2.simple.security.v5.web.authentication.logout;

import com.example.spring.boot2.simple.security.v5.core.Authentication;
import com.example.spring.boot2.simple.security.v5.core.context.SecurityContext;
import com.example.spring.boot2.simple.security.v5.core.context.SecurityContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @since 2022-06-29.
 */
public class SecurityContextLogoutHandler implements LogoutHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(SecurityContextLogoutHandler.class);

    private boolean invalidateHttpSession = true;

    private boolean clearAuthentication = true;

    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        Assert.notNull(request, "HttpServletRequest required");
        if (invalidateHttpSession) {
            HttpSession session = request.getSession(false);
            if (session != null) {
                LOGGER.debug("Invalidating session: " + session.getId());
                session.invalidate();
            }
        }

        if (clearAuthentication) {
            SecurityContext context = SecurityContextHolder.getContext();
            context.setAuthentication(null);
        }

        SecurityContextHolder.clearContext();
    }

    public boolean isInvalidateHttpSession() {
        return invalidateHttpSession;
    }

    public void setInvalidateHttpSession(boolean invalidateHttpSession) {
        this.invalidateHttpSession = invalidateHttpSession;
    }

    public void setClearAuthentication(boolean clearAuthentication) {
        this.clearAuthentication = clearAuthentication;
    }
}
