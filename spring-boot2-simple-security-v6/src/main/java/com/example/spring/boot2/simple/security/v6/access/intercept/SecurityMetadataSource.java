package com.example.spring.boot2.simple.security.v6.access.intercept;

import com.example.spring.boot2.simple.security.v6.access.ConfigAttribute;

import java.util.Collection;

/**
 * @since 2022-07-07.
 */
public interface SecurityMetadataSource {

    Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException;

    Collection<ConfigAttribute> getAllConfigAttributes();

}
