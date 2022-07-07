package com.example.spring.boot2.simple.security.v5.access.expression;

import com.example.spring.boot2.simple.security.v5.core.Authentication;
import org.springframework.aop.framework.AopInfrastructureBean;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;

/**
 * @since 2022-07-07.
 */
public interface SecurityExpressionHandler<T> extends AopInfrastructureBean {
    /**
     * @return an expression parser for the expressions used by the implementation.
     */
    ExpressionParser getExpressionParser();

    /**
     * Provides an evaluation context in which to evaluate security expressions for the invocation type.
     */
    EvaluationContext createEvaluationContext(Authentication authentication, T invocation);
}
