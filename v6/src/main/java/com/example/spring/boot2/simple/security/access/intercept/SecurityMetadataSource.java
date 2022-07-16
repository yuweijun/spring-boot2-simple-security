package com.example.spring.boot2.simple.security.access.intercept;

import com.example.spring.boot2.simple.security.access.ConfigAttribute;

import java.util.Collection;

/**
 * @since 2022-07-07.
 */
public interface SecurityMetadataSource {

    Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException;

}
