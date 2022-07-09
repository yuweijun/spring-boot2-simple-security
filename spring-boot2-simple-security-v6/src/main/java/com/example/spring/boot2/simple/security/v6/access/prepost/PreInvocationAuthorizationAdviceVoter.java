package com.example.spring.boot2.simple.security.v6.access.prepost;

import com.example.spring.boot2.simple.security.v6.access.AccessDecisionVoter;
import com.example.spring.boot2.simple.security.v6.access.ConfigAttribute;
import com.example.spring.boot2.simple.security.v6.core.Authentication;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

/**
 * @since 2022-07-09.
 */
public class PreInvocationAuthorizationAdviceVoter implements AccessDecisionVoter<MethodInvocation> {

    private static final Logger LOGGER = LoggerFactory.getLogger(PreInvocationAuthorizationAdviceVoter.class);

    private final PreInvocationAuthorizationAdvice preAdvice;

    public PreInvocationAuthorizationAdviceVoter(PreInvocationAuthorizationAdvice pre) {
        this.preAdvice = pre;
    }

    @Override
    public int vote(Authentication authentication, MethodInvocation method, Collection<ConfigAttribute> attributes) {
        // Find prefilter and preauth (or combined) attributes
        // if both null, abstain
        // else call advice with them

        PreInvocationAttribute preAttr = findPreInvocationAttribute(attributes);
        LOGGER.debug("PreInvocationAttribute is : {}", preAttr);

        if (preAttr == null) {
            // No expression based metadata, so abstain
            return ACCESS_ABSTAIN;
        }

        boolean allowed = preAdvice.before(authentication, method, preAttr);

        return allowed ? ACCESS_GRANTED : ACCESS_DENIED;
    }

    private PreInvocationAttribute findPreInvocationAttribute(Collection<ConfigAttribute> config) {
        for (ConfigAttribute attribute : config) {
            if (attribute instanceof PreInvocationAttribute) {
                return (PreInvocationAttribute) attribute;
            }
        }

        return null;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }
}
