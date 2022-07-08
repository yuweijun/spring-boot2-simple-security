package com.example.spring.boot2.simple.security.v6.core.userdetails;

/**
 * {@see org.springframework.security.core.userdetails.UserDetailsService}
 */
public interface UserDetailsService {

    User loadUserByUsername(String username);

}
