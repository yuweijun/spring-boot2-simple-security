package com.example.spring.boot2.simple.security.v6.access.intercept.aopalliance;

import com.example.spring.boot2.simple.security.v6.access.method.MethodSecurityMetadataSource;
import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInterceptor;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractPointcutAdvisor;
import org.springframework.aop.support.StaticMethodMatcherPointcut;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.util.Assert;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Collection;

/**
 * @since 2022-07-09.
 */
public class MethodSecurityMetadataSourceAdvisor extends AbstractPointcutAdvisor implements BeanFactoryAware {

    private transient MethodSecurityMetadataSource attributeSource;
    private transient MethodInterceptor interceptor;
    private final Pointcut pointcut = new MethodSecurityMetadataSourcePointcut();
    private BeanFactory beanFactory;
    private final String adviceBeanName;
    private final String metadataSourceBeanName;
    private transient volatile Object adviceMonitor = new Object();

    /**
     * @param adviceBeanName          name of the MethodSecurityInterceptor bean
     * @param attributeSource         the SecurityMetadataSource (should be the same as the one used on the interceptor)
     * @param attributeSourceBeanName the bean name of the attributeSource (required for serialization)
     */
    public MethodSecurityMetadataSourceAdvisor(String adviceBeanName, MethodSecurityMetadataSource attributeSource, String attributeSourceBeanName) {
        Assert.notNull(adviceBeanName, "The adviceBeanName cannot be null");
        Assert.notNull(attributeSource, "The attributeSource cannot be null");
        Assert.notNull(attributeSourceBeanName, "The attributeSourceBeanName cannot be null");

        this.adviceBeanName = adviceBeanName;
        this.attributeSource = attributeSource;
        this.metadataSourceBeanName = attributeSourceBeanName;
    }

    public Pointcut getPointcut() {
        return pointcut;
    }

    public Advice getAdvice() {
        synchronized (this.adviceMonitor) {
            if (interceptor == null) {
                Assert.notNull(adviceBeanName, "'adviceBeanName' must be set for use with bean factory lookup.");
                Assert.state(beanFactory != null, "BeanFactory must be set to resolve 'adviceBeanName'");
                interceptor = beanFactory.getBean(this.adviceBeanName, MethodInterceptor.class);
            }
            return interceptor;
        }
    }

    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        ois.defaultReadObject();
        adviceMonitor = new Object();
        attributeSource = beanFactory.getBean(metadataSourceBeanName, MethodSecurityMetadataSource.class);
    }

    class MethodSecurityMetadataSourcePointcut extends StaticMethodMatcherPointcut implements Serializable {
        public boolean matches(Method m, Class targetClass) {
            Collection attributes = attributeSource.getAttributes(m, targetClass);
            return attributes != null && !attributes.isEmpty();
        }
    }
}
