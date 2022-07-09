package com.example.spring.boot2.simple.security.v6.access.method;

import com.example.spring.boot2.simple.security.v6.access.ConfigAttribute;
import com.example.spring.boot2.simple.security.v6.access.intercept.SecurityMetadataSource;

import java.lang.reflect.Method;
import java.util.Collection;

/**
 * @since 2022-07-08.
 */
public interface MethodSecurityMetadataSource extends SecurityMetadataSource {

    Collection<ConfigAttribute> getAttributes(Method method, Class<?> targetClass);

}
