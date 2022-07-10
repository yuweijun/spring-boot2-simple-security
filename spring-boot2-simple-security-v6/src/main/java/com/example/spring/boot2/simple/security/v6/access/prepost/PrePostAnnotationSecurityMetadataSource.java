package com.example.spring.boot2.simple.security.v6.access.prepost;

import com.example.spring.boot2.simple.security.v6.access.ConfigAttribute;
import com.example.spring.boot2.simple.security.v6.access.method.AbstractMethodSecurityMetadataSource;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ClassUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * @since 2022-07-08.
 */
public class PrePostAnnotationSecurityMetadataSource extends AbstractMethodSecurityMetadataSource {

    private final PrePostInvocationAttributeFactory attributeFactory;

    public PrePostAnnotationSecurityMetadataSource(PrePostInvocationAttributeFactory attributeFactory) {
        this.attributeFactory = attributeFactory;
    }

    @Override
    public Collection<ConfigAttribute> getAttributes(Method method, Class<?> targetClass) {
        if (method.getDeclaringClass() == Object.class) {
            return Collections.emptyList();
        }

        logger.debug("Looking for Pre/Post annotations for method '{}' on target class '{}'", method.getName(), targetClass);
        PreAuthorize preAuthorize = findAnnotation(method, targetClass, PreAuthorize.class);

        if (preAuthorize == null) {
            // There is no meta-data so return
            logger.info("No expression annotations found");
            return Collections.emptyList();
        }

        String preAuthorizeAttribute = preAuthorize.value();

        ArrayList<ConfigAttribute> attrs = new ArrayList<>();

        PreInvocationAttribute pre = attributeFactory.createPreInvocationAttribute(preAuthorizeAttribute);

        if (pre != null) {
            attrs.add(pre);
        }

        return attrs;
    }

    private <A extends Annotation> A findAnnotation(Method method, Class<?> targetClass, Class<A> annotationClass) {
        // The method may be on an interface, but we need attributes from the target class.
        // If the target class is null, the method will be unchanged.
        Method specificMethod = ClassUtils.getMostSpecificMethod(method, targetClass);
        A annotation = AnnotationUtils.findAnnotation(specificMethod, annotationClass);

        if (annotation != null) {
            logger.debug("{} found on specific method: {}", annotation, specificMethod);
            return annotation;
        }

        // Check the original (e.g. interface) method
        if (specificMethod != method) {
            annotation = AnnotationUtils.findAnnotation(method, annotationClass);

            if (annotation != null) {
                logger.debug(annotation + " found on: " + method);
                return annotation;
            }
        }

        // Check the class-level (note declaringClass, not targetClass, which may not
        // actually implement the method)
        annotation = AnnotationUtils.findAnnotation(specificMethod.getDeclaringClass(), annotationClass);

        if (annotation != null) {
            logger.debug(annotation + " found on: " + specificMethod.getDeclaringClass().getName());
            return annotation;
        }

        return null;
    }

}
