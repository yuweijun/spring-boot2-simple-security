package com.example.spring.boot2.simple.security.v6.access.intercept.aopalliance;

import com.example.spring.boot2.simple.security.v6.access.ConfigAttribute;
import com.example.spring.boot2.simple.security.v6.access.method.MethodSecurityMetadataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.support.StaticMethodMatcherPointcut;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Collection;

/**
 * call stack of this pointcut
 * <pre>
 * matches:29, MethodSecurityMetadataSourcePointcut (com.example.spring.boot2.simple.security.v6.access.intercept.aopalliance)
 * canApply:252, AopUtils (org.springframework.aop.support)
 * canApply:289, AopUtils (org.springframework.aop.support)
 * findAdvisorsThatCanApply:321, AopUtils (org.springframework.aop.support)
 * findAdvisorsThatCanApply:128, AbstractAdvisorAutoProxyCreator (org.springframework.aop.framework.autoproxy)
 * findEligibleAdvisors:97, AbstractAdvisorAutoProxyCreator (org.springframework.aop.framework.autoproxy)
 * getAdvicesAndAdvisorsForBean:78, AbstractAdvisorAutoProxyCreator (org.springframework.aop.framework.autoproxy)
 * wrapIfNecessary:347, AbstractAutoProxyCreator (org.springframework.aop.framework.autoproxy)
 * postProcessAfterInitialization:299, AbstractAutoProxyCreator (org.springframework.aop.framework.autoproxy)
 * applyBeanPostProcessorsAfterInitialization:430, AbstractAutowireCapableBeanFactory (org.springframework.beans.factory.support)
 * initializeBean:1798, AbstractAutowireCapableBeanFactory (org.springframework.beans.factory.support)
 * doCreateBean:594, AbstractAutowireCapableBeanFactory (org.springframework.beans.factory.support)
 * createBean:516, AbstractAutowireCapableBeanFactory (org.springframework.beans.factory.support)
 * lambda$doGetBean$0:324, AbstractBeanFactory (org.springframework.beans.factory.support)
 * getObject:-1, 1226791876 (org.springframework.beans.factory.support.AbstractBeanFactory$$Lambda$229)
 * getSingleton:226, DefaultSingletonBeanRegistry (org.springframework.beans.factory.support)
 * doGetBean:322, AbstractBeanFactory (org.springframework.beans.factory.support)
 * getBean:207, AbstractBeanFactory (org.springframework.beans.factory.support)
 * registerBeanPostProcessors:241, PostProcessorRegistrationDelegate (org.springframework.context.support)
 * registerBeanPostProcessors:723, AbstractApplicationContext (org.springframework.context.support)
 * refresh:536, AbstractApplicationContext (org.springframework.context.support)
 * </pre>
 *
 * @since 2022-07-10.
 */
class MethodSecurityMetadataSourcePointcut extends StaticMethodMatcherPointcut implements Serializable {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodSecurityMetadataSourcePointcut.class);

    private transient MethodSecurityMetadataSource attributeSource;

    public MethodSecurityMetadataSourcePointcut(MethodSecurityMetadataSource attributeSource) {
        this.attributeSource = attributeSource;
    }

    @Override
    public boolean matches(Method m, Class targetClass) {
        Collection<ConfigAttribute> attributes = attributeSource.getAttributes(m, targetClass);
        LOGGER.debug("get attributes : {}, for method '{}' of class '{}'", attributes, m.getName(), targetClass.getName());
        return attributes != null && !attributes.isEmpty();
    }

    public void setAttributeSource(MethodSecurityMetadataSource attributeSource) {
        this.attributeSource = attributeSource;
    }
}
