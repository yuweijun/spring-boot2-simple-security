package com.example.spring.boot2.simple.security.v2.web.authentication;

import com.example.spring.boot2.simple.security.v2.core.userdetails.UserDetailsService;
import com.example.spring.boot2.simple.security.v2.core.Authentication;
import com.example.spring.boot2.simple.security.v2.core.AuthenticationException;
import com.example.spring.boot2.simple.security.v2.core.userdetails.User;
import com.example.spring.boot2.simple.security.v2.crypto.password.PasswordEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

/**
 * @author yuweijun 2022-06-25.
 */
public class AuthenticationManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationManager.class);

    private UserDetailsService userDetailsService;

    private PasswordEncoder passwordEncoder;

    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        final String username = authentication.getName();
        final String password = authentication.getPrincipal();

        // TODO move below codes to DaoAuthenticationProvider
        if (userDetailsService == null) {
            // subclass should override this method
            return null;
        }

        try {
            final User user = userDetailsService.loadUserByUsername(username);

            if (passwordEncoder.matches(password, user.getPassword())) {
                authentication.setAuthenticated(true);
                final Collection<String> authorities = user.getAuthorities();
                LOGGER.info("AuthenticationManager#authenticate() for : {}", authorities);
                return authentication;
            } else {
                throw new AuthenticationException("user authenticate failed");
            }
        } catch (Exception e) {
            throw new AuthenticationException(e.getMessage(), e.getCause());
        }
    }

    public void setUserDetailsService(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }}
