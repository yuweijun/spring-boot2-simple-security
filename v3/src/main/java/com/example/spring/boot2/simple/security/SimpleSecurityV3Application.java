package com.example.spring.boot2.simple.security;

import com.example.spring.boot2.simple.security.config.EnableSimpleWebSecurity;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableSimpleWebSecurity
@SpringBootApplication
public class SimpleSecurityV3Application {

    public static void main(String[] args) {
        SpringApplication.run(SimpleSecurityV3Application.class, args);
    }

}
