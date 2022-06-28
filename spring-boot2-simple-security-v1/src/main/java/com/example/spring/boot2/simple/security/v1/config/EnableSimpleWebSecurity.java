package com.example.spring.boot2.simple.security.v1.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(value = java.lang.annotation.RetentionPolicy.RUNTIME)
@Target(value = {java.lang.annotation.ElementType.TYPE})
@Documented
@Import({SpringWebMvcConfig.class, WebSecurityConfiguration.class})
@Configuration
public @interface EnableSimpleWebSecurity {

}

