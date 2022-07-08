package com.example.spring.boot2.simple.security.v6.access.expression;

import com.example.spring.boot2.simple.security.v6.access.AccessDecisionVoter;
import com.example.spring.boot2.simple.security.v6.access.ConfigAttribute;
import com.example.spring.boot2.simple.security.v6.core.Authentication;
import com.example.spring.boot2.simple.security.v6.web.FilterInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;

import java.util.Collection;

/**
 * @since 2022-07-07.
 */
public class WebExpressionVoter implements AccessDecisionVoter<FilterInvocation> {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebExpressionVoter.class);

    private SecurityExpressionHandler<FilterInvocation> expressionHandler = new DefaultWebSecurityExpressionHandler();

    public int vote(Authentication authentication, FilterInvocation fi, Collection<ConfigAttribute> attributes) {
        WebExpressionConfigAttribute webExpressionConfigAttribute = findConfigAttribute(attributes);

        if (webExpressionConfigAttribute == null) {
            LOGGER.info("webExpressionConfigAttribute is null");
            return ACCESS_ABSTAIN;
        }

        EvaluationContext ctx = expressionHandler.createEvaluationContext(authentication, fi);
        final Expression authorizeExpression = webExpressionConfigAttribute.getAuthorizeExpression();
        return ExpressionUtils.evaluateAsBoolean(authorizeExpression, ctx) ? ACCESS_GRANTED : ACCESS_DENIED;
    }

    private WebExpressionConfigAttribute findConfigAttribute(Collection<ConfigAttribute> attributes) {
        for (ConfigAttribute attribute : attributes) {
            if (attribute instanceof WebExpressionConfigAttribute) {
                return (WebExpressionConfigAttribute) attribute;
            }
        }

        return null;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }
}
