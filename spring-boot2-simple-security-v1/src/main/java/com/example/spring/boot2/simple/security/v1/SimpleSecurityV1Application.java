package com.example.spring.boot2.simple.security.v1;

import com.example.spring.boot2.simple.security.v1.config.EnableSimpleWebSecurity;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableSimpleWebSecurity
@SpringBootApplication
public class SimpleSecurityV1Application {

    public static void main(String[] args) {
        SpringApplication.run(SimpleSecurityV1Application.class, args);
    }

}
