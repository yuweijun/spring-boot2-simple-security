package com.example.spring.boot2.simple.security.v5.access.vote;

import com.example.spring.boot2.simple.security.v5.access.AccessDecisionManager;
import com.example.spring.boot2.simple.security.v5.access.ConfigAttribute;
import com.example.spring.boot2.simple.security.v5.core.Authentication;
import com.example.spring.boot2.simple.security.v5.web.access.AccessDeniedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

/**
 * @since 2022-07-06.
 */
public class AffirmativeBasedDecisionManager implements AccessDecisionManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(AffirmativeBasedDecisionManager.class);

    public void decide(Authentication authentication, Object object,
        Collection<ConfigAttribute> configAttributes) throws AccessDeniedException {
        int deny = 0;

        int result = vote(authentication, object, configAttributes);

        LOGGER.debug("Voter returned: {}", result);

        switch (result) {
            case ACCESS_GRANTED:
                return;

            case ACCESS_DENIED:
                deny++;

                break;

            default:
                break;
        }

        if (deny > 0) {
            throw new AccessDeniedException("Access is denied");
        }
    }

    private int vote(Authentication authentication, Object object, Collection<ConfigAttribute> configAttributes) {
        return 0;
    }
}
