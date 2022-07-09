package com.example.spring.boot2.simple.security.v6.access;

import com.example.spring.boot2.simple.security.v6.core.Authentication;

import java.util.Collection;

/**
 * @since 2022-07-07.
 */
public interface AccessDecisionVoter<S> {

    int ACCESS_GRANTED = 1;
    int ACCESS_ABSTAIN = 0;
    int ACCESS_DENIED = -1;

    /**
     * @return either {@link #ACCESS_GRANTED}, {@link #ACCESS_ABSTAIN} or {@link #ACCESS_DENIED}
     */
    int vote(Authentication authentication, S object, Collection<ConfigAttribute> attributes);
}

