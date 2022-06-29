package com.example.spring.boot2.simple.security.v4;

import com.example.spring.boot2.simple.security.v4.config.EnableSimpleWebSecurity;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableSimpleWebSecurity
@SpringBootApplication
public class SimpleSecurityV4Application {

    public static void main(String[] args) {
        SpringApplication.run(SimpleSecurityV4Application.class, args);
    }

}
