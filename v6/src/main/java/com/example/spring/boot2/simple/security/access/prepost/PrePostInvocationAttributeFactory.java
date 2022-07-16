package com.example.spring.boot2.simple.security.access.prepost;

import org.springframework.aop.framework.AopInfrastructureBean;

/**
 * @since 2022-07-08.
 */
public interface PrePostInvocationAttributeFactory extends AopInfrastructureBean {

    PreInvocationAttribute createPreInvocationAttribute(String preAuthorizeAttribute);

}
