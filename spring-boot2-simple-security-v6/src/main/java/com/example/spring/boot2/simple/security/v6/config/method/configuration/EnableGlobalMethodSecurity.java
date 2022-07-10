package com.example.spring.boot2.simple.security.v6.config.method.configuration;

import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * {@see org.springframework.context.annotation.AspectJAutoProxyRegistrar}
 * {@link org.springframework.boot.autoconfigure.aop.AopAutoConfiguration}
 * {@link org.springframework.aop.config.AopConfigUtils}
 *
 * @since 2022-07-09.
 */
@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = {ElementType.TYPE})
@Documented
@Import({GlobalMethodSecuritySelector.class})
@Configuration
public @interface EnableGlobalMethodSecurity {

    boolean proxyTargetClass() default false;

    AdviceMode mode() default AdviceMode.PROXY;

}
