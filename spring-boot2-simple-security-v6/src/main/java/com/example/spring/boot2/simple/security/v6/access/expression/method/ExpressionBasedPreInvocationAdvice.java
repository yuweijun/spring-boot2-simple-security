package com.example.spring.boot2.simple.security.v6.access.expression.method;

import com.example.spring.boot2.simple.security.v6.access.expression.ExpressionUtils;
import com.example.spring.boot2.simple.security.v6.access.prepost.PreInvocationAttribute;
import com.example.spring.boot2.simple.security.v6.access.prepost.PreInvocationAuthorizationAdvice;
import com.example.spring.boot2.simple.security.v6.core.Authentication;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;

/**
 * @since 2022-07-09.
 */
public class ExpressionBasedPreInvocationAdvice implements PreInvocationAuthorizationAdvice {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExpressionBasedPreInvocationAdvice.class);

    private MethodSecurityExpressionHandler expressionHandler = new DefaultMethodSecurityExpressionHandler();

    public boolean before(Authentication authentication, MethodInvocation mi, PreInvocationAttribute attr) {
        PreInvocationExpressionAttribute preAttr = (PreInvocationExpressionAttribute) attr;
        EvaluationContext ctx = expressionHandler.createEvaluationContext(authentication, mi);
        Expression preAuthorize = preAttr.getAuthorizeExpression();
        LOGGER.info("preAuthorize is : {}", preAuthorize.getExpressionString());
        if (preAuthorize == null) {
            return true;
        }

        return ExpressionUtils.evaluateAsBoolean(preAuthorize, ctx);
    }

    public void setExpressionHandler(MethodSecurityExpressionHandler expressionHandler) {
        this.expressionHandler = expressionHandler;
    }
}
