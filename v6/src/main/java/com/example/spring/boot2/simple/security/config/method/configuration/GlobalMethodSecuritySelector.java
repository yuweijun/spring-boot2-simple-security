package com.example.spring.boot2.simple.security.config.method.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AutoProxyRegistrar;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.ClassUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @since 2022-07-09.
 */
final class GlobalMethodSecuritySelector implements ImportSelector {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalMethodSecuritySelector.class);

    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        Class<EnableGlobalMethodSecurity> annotation = EnableGlobalMethodSecurity.class;
        Map<String, Object> annotationAttributes = importingClassMetadata.getAnnotationAttributes(annotation.getName(), false);
        LOGGER.debug("annotationAttributes is : {}", annotationAttributes);

        AnnotationAttributes attributes = AnnotationAttributes.fromMap(annotationAttributes);
        LOGGER.debug("attributes is : {}", attributes);

        final String className = importingClassMetadata.getClassName();
        LOGGER.debug("importingClassMetadata.getClassName() is : {}", className);

        Class<?> importingClass = ClassUtils.resolveClassName(className, ClassUtils.getDefaultClassLoader());

        // spring boot will load AopAutoConfiguration and enable CglibAutoProxyConfiguration
        String autoProxyClassName = AutoProxyRegistrar.class.getName();

        List<String> classNames = new ArrayList<>();
        classNames.add(MethodSecurityMetadataSourceAdvisorRegistrar.class.getName());
        classNames.add(autoProxyClassName);

        boolean skipMethodSecurityConfiguration = GlobalMethodSecurityConfiguration.class.isAssignableFrom(importingClass);
        if (!skipMethodSecurityConfiguration) {
            classNames.add(GlobalMethodSecurityConfiguration.class.getName());
        }

        return classNames.toArray(new String[0]);
    }
}
