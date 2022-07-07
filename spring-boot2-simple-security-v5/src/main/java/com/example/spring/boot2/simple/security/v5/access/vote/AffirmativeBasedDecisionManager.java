package com.example.spring.boot2.simple.security.v5.access.vote;

import com.example.spring.boot2.simple.security.v5.access.AccessDecisionManager;
import com.example.spring.boot2.simple.security.v5.access.AccessDecisionVoter;
import com.example.spring.boot2.simple.security.v5.access.ConfigAttribute;
import com.example.spring.boot2.simple.security.v5.access.expression.WebExpressionVoter;
import com.example.spring.boot2.simple.security.v5.core.Authentication;
import com.example.spring.boot2.simple.security.v5.web.access.AccessDeniedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.example.spring.boot2.simple.security.v5.access.AccessDecisionVoter.ACCESS_DENIED;
import static com.example.spring.boot2.simple.security.v5.access.AccessDecisionVoter.ACCESS_GRANTED;

/**
 * @since 2022-07-06.
 */
public class AffirmativeBasedDecisionManager implements AccessDecisionManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(AffirmativeBasedDecisionManager.class);

    private List<AccessDecisionVoter<?>> decisionVoters;

    public List<AccessDecisionVoter<?>> getDecisionVoters() {
        if (decisionVoters == null) {
            this.decisionVoters = new ArrayList<>();
            WebExpressionVoter webExpressionVoter = new WebExpressionVoter();

            decisionVoters.add(webExpressionVoter);
        }

        return this.decisionVoters;
    }

    public void setDecisionVoters(List<AccessDecisionVoter<?>> decisionVoters) {
        this.decisionVoters = decisionVoters;
    }

    public void decide(Authentication authentication, Object object, Collection<ConfigAttribute> configAttributes) throws AccessDeniedException {
        int deny = 0;
        for (AccessDecisionVoter voter : getDecisionVoters()) {
            int result = voter.vote(authentication, object, configAttributes);

            LOGGER.debug("Voter '{}' returned: {}", voter, result);

            switch (result) {
                case ACCESS_GRANTED:
                    return;

                case ACCESS_DENIED:
                    deny++;

                    break;

                default:
                    break;
            }
        }

        if (deny > 0) {
            throw new AccessDeniedException("Access is denied");
        }
    }

}
