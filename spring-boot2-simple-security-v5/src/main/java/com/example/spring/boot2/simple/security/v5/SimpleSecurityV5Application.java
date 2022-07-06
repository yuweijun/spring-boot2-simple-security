package com.example.spring.boot2.simple.security.v5;

import com.example.spring.boot2.simple.security.v5.config.EnableSimpleWebSecurity;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableSimpleWebSecurity
@SpringBootApplication
public class SimpleSecurityV5Application {

    public static void main(String[] args) {
        SpringApplication.run(SimpleSecurityV5Application.class, args);
    }

}
