package com.example.spring.boot2.simple.security.v6.config.method.configuration;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;

/**
 * @since 2022-07-08.
 */
public class GlobalMethodSecurityConfiguration implements BeanFactoryAware {

    private BeanFactory context;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {

    }

}
