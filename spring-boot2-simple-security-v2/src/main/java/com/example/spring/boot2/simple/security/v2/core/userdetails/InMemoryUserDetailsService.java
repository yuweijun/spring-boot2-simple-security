package com.example.spring.boot2.simple.security.v2.core.userdetails;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @since 2022-06-28.
 */
public class InMemoryUserDetailsService implements UserDetailsService {

    protected final Log logger = LogFactory.getLog(getClass());

    private final Map<String, User> users = new HashMap<>();

    public void createUser(User user) {
        users.put(user.getUsername().toLowerCase(), user);
    }

    public void deleteUser(String username) {
        users.remove(username.toLowerCase());
    }

    public void updateUser(User user) {
        users.put(user.getUsername().toLowerCase(), user);
    }

    public boolean userExists(String username) {
        return users.containsKey(username.toLowerCase());
    }

    @Override
    public User loadUserByUsername(String username) {
        User user = users.get(username.toLowerCase());

        if (user == null) {
            throw new RuntimeException("USER NOT FOUND : " + username);
        }

        return new User(user.getUsername(), user.getPassword(), user.isEnabled(),
            user.isAccountNonExpired(), user.isCredentialsNonExpired(),
            user.isAccountNonLocked(), user.getAuthorities());
    }

}
