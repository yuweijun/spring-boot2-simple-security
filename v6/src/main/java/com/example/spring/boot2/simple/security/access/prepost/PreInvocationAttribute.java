package com.example.spring.boot2.simple.security.access.prepost;

import com.example.spring.boot2.simple.security.access.ConfigAttribute;

/**
 * Marker interface for attributes which are created from combined @PreFilter and @PreAuthorize annotations.
 * <p>
 * Consumed by a {@link PreInvocationAuthorizationAdvice}.
 *
 * @since 2022-07-08.
 */
public interface PreInvocationAttribute extends ConfigAttribute {

}
