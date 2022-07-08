package com.example.spring.boot2.simple.security.v6;

import com.example.spring.boot2.simple.security.v6.config.EnableSimpleWebSecurity;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableSimpleWebSecurity
@SpringBootApplication
public class SimpleSecurityV6Application {

    public static void main(String[] args) {
        SpringApplication.run(SimpleSecurityV6Application.class, args);
    }

}
