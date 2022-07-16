package com.example.spring.boot2.simple.security.config.method.configuration;

import com.example.spring.boot2.simple.security.access.intercept.aopalliance.MethodSecurityMetadataSourceAdvisor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

/**
 * load by {@link GlobalMethodSecuritySelector}
 *
 * @since 2022-07-09.
 */
class MethodSecurityMetadataSourceAdvisorRegistrar implements ImportBeanDefinitionRegistrar {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodSecurityMetadataSourceAdvisorRegistrar.class);

    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        LOGGER.debug("importingClassMetadata.getAnnotationTypes() : {}", importingClassMetadata.getAnnotationTypes());
        BeanDefinitionBuilder advisor = BeanDefinitionBuilder.rootBeanDefinition(MethodSecurityMetadataSourceAdvisor.class);
        advisor.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
        advisor.addConstructorArgValue("methodSecurityInterceptor");
        advisor.addConstructorArgReference("methodSecurityMetadataSource");
        advisor.addConstructorArgValue("methodSecurityMetadataSource");

        registry.registerBeanDefinition("metaDataSourceAdvisor", advisor.getBeanDefinition());
    }
}
