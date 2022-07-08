package com.example.spring.boot2.simple.security.v6.core.parameters;

import org.springframework.core.BridgeMethodResolver;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @since 2022-07-08.
 */
public class AnnotationParameterNameDiscoverer implements ParameterNameDiscoverer {

    private final Set<String> annotationClassesToUse;

    public AnnotationParameterNameDiscoverer(String... annotationClassToUse) {
        this(new HashSet<>(Arrays.asList(annotationClassToUse)));
    }

    public AnnotationParameterNameDiscoverer(Set<String> annotationClassesToUse) {
        Assert.notEmpty(annotationClassesToUse, "annotationClassesToUse cannot be null or empty");
        this.annotationClassesToUse = annotationClassesToUse;
    }

    @Override
    public String[] getParameterNames(Method method) {
        Method originalMethod = BridgeMethodResolver.findBridgedMethod(method);
        String[] paramNames = lookupParameterNames(METHOD_METHODPARAM_FACTORY, originalMethod);
        if (paramNames != null) {
            return paramNames;
        }

        Class<?> declaringClass = method.getDeclaringClass();
        Class<?>[] interfaces = declaringClass.getInterfaces();
        for (Class<?> intrfc : interfaces) {
            Method intrfcMethod = ReflectionUtils.findMethod(intrfc, method.getName(), method.getParameterTypes());
            if (intrfcMethod != null) {
                return lookupParameterNames(METHOD_METHODPARAM_FACTORY, intrfcMethod);
            }
        }

        return null;
    }

    @Override
    public String[] getParameterNames(Constructor<?> constructor) {
        return lookupParameterNames(CONSTRUCTOR_METHODPARAM_FACTORY, constructor);
    }

    private <T extends AccessibleObject> String[] lookupParameterNames(ParameterNameFactory<T> parameterNameFactory, T t) {
        Annotation[][] parameterAnnotations = parameterNameFactory.findParameterAnnotations(t);
        int parameterCount = parameterAnnotations.length;
        String[] paramNames = new String[parameterCount];
        boolean found = false;
        for (int i = 0; i < parameterCount; i++) {
            Annotation[] annotations = parameterAnnotations[i];
            String parameterName = findParameterName(annotations);
            if (parameterName != null) {
                found = true;
                paramNames[i] = parameterName;
            }
        }
        return found ? paramNames : null;
    }

    private String findParameterName(Annotation[] parameterAnnotations) {
        for (Annotation paramAnnotation : parameterAnnotations) {
            if (annotationClassesToUse.contains(paramAnnotation.annotationType().getName())) {
                return (String) AnnotationUtils.getValue(paramAnnotation, "value");
            }
        }
        return null;
    }

    private static final ParameterNameFactory<Constructor<?>> CONSTRUCTOR_METHODPARAM_FACTORY = constructor -> constructor.getParameterAnnotations();

    private static final ParameterNameFactory<Method> METHOD_METHODPARAM_FACTORY = method -> method.getParameterAnnotations();

    private interface ParameterNameFactory<T extends AccessibleObject> {
        Annotation[][] findParameterAnnotations(T t);
    }
}
