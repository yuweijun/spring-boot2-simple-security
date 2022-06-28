package com.example.spring.boot2.simple.security.v2.crypto.password;

/**
 * {@see org.springframework.security.crypto.password.NoOpPasswordEncoder}
 *
 * @since 2022-06-26.
 */
public class PasswordEncoder {

    private static final PasswordEncoder INSTANCE = new PasswordEncoder();

    private PasswordEncoder() {
    }

    public String encode(CharSequence rawPassword) {
        return "${NoOpPasswordEncoder}" + rawPassword;
    }

    public static PasswordEncoder getInstance() {
        return INSTANCE;
    }

    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return encode(rawPassword).equals(encodedPassword);
    }
}
