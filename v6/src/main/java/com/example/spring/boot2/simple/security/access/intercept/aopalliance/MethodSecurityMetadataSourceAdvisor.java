package com.example.spring.boot2.simple.security.access.intercept.aopalliance;

import com.example.spring.boot2.simple.security.access.method.MethodSecurityMetadataSource;
import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractPointcutAdvisor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.util.Assert;

import java.io.IOException;
import java.io.ObjectInputStream;

/**
 * @since 2022-07-09.
 */
public class MethodSecurityMetadataSourceAdvisor extends AbstractPointcutAdvisor implements BeanFactoryAware {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodSecurityMetadataSourceAdvisor.class);

    private transient MethodInterceptor interceptor;
    private final MethodSecurityMetadataSourcePointcut pointcut;
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
        this.metadataSourceBeanName = attributeSourceBeanName;
        this.pointcut = new MethodSecurityMetadataSourcePointcut(attributeSource);
    }

    @Override
    public Pointcut getPointcut() {
        return pointcut;
    }

    @Override
    public Advice getAdvice() {
        synchronized (this.adviceMonitor) {
            if (interceptor == null) {
                Assert.notNull(adviceBeanName, "'adviceBeanName' must be set for use with bean factory lookup.");
                Assert.state(beanFactory != null, "BeanFactory must be set to resolve 'adviceBeanName'");
                interceptor = beanFactory.getBean(this.adviceBeanName, MethodInterceptor.class);
            }
            LOGGER.debug("interceptor for adviceBeanName : {} is : {}", adviceBeanName, interceptor);
            return interceptor;
        }
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        ois.defaultReadObject();
        adviceMonitor = new Object();
        MethodSecurityMetadataSource attributeSource = beanFactory.getBean(metadataSourceBeanName, MethodSecurityMetadataSource.class);
        this.pointcut.setAttributeSource(attributeSource);
    }

}
