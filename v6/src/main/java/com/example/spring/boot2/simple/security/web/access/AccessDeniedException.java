package com.example.spring.boot2.simple.security.web.access;

/**
 * @since 2022-06-29.
 */
public class AccessDeniedException extends RuntimeException {

    public AccessDeniedException(String msg) {
        super(msg);
    }

    public AccessDeniedException(String msg, Throwable t) {
        super(msg, t);
    }
}
