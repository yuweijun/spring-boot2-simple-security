package com.example.spring.boot2.simple.security.v2;

import com.example.spring.boot2.simple.security.v2.config.EnableSimpleWebSecurity;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableSimpleWebSecurity
@SpringBootApplication
public class SimpleSecurityV2Application {

    public static void main(String[] args) {
        SpringApplication.run(SimpleSecurityV2Application.class, args);
    }

}
