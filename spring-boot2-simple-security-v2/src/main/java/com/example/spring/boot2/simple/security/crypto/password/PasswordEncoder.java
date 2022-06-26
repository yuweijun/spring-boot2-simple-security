package com.example.spring.boot2.simple.security.crypto.password;

/**
 * @author yuweijun 2022-06-26.
 */
public class PasswordEncoder {

    public String encode(CharSequence rawPassword) {
        return rawPassword.toString();
    }

    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return rawPassword.toString().equals(encodedPassword);
    }

    public static PasswordEncoder getInstance() {
        return INSTANCE;
    }

    private static final PasswordEncoder INSTANCE = new PasswordEncoder();

    /**
     * {@see org.springframework.security.crypto.password.NoOpPasswordEncoder}
     */
    private PasswordEncoder() {
    }

}
