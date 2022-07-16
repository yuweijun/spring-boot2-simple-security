package com.example.spring.boot2.simple.security.web.authentication.dao;

import com.example.spring.boot2.simple.security.core.Authentication;
import com.example.spring.boot2.simple.security.core.AuthenticationException;
import com.example.spring.boot2.simple.security.core.userdetails.User;
import com.example.spring.boot2.simple.security.core.userdetails.UserDetailsService;
import com.example.spring.boot2.simple.security.crypto.password.PasswordEncoder;
import com.example.spring.boot2.simple.security.web.authentication.AuthenticationProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

/**
 * @since 2022-07-06.
 */
public class DaoAuthenticationProvider implements AuthenticationProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(DaoAuthenticationProvider.class);

    private UserDetailsService userDetailsService;

    private PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (userDetailsService == null) {
            // subclass should override this method
            return null;
        }

        try {
            final String username = authentication.getName();
            final String password = authentication.getPrincipal();
            final User user = userDetailsService.loadUserByUsername(username);

            if (passwordEncoder.matches(password, user.getPassword())) {
                authentication.setAuthenticated(true);
                final Collection<String> authorities = user.getAuthorities();
                LOGGER.info("DaoAuthenticationProvider#authenticate() for : {}", authorities);

                authentication.getAuthorities().addAll(authorities);
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
    }

}
