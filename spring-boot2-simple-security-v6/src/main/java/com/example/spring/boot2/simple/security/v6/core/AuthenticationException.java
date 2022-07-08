package com.example.spring.boot2.simple.security.v6.core;

public class AuthenticationException extends RuntimeException {

    public AuthenticationException(String msg, Throwable t) {
        super(msg, t);
    }

    public AuthenticationException(String msg) {
        super(msg);
    }

}
