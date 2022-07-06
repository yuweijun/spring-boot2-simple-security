package com.example.spring.boot2.simple.security.v5.core.context;

import com.example.spring.boot2.simple.security.v5.core.Authentication;

import java.io.Serializable;

public interface SecurityContext extends Serializable {

    Authentication getAuthentication();

    void setAuthentication(Authentication authentication);

}
