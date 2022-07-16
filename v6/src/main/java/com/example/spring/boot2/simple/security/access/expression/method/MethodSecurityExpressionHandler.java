package com.example.spring.boot2.simple.security.access.expression.method;

import com.example.spring.boot2.simple.security.access.expression.SecurityExpressionHandler;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;

/**
 * @since 2022-07-08.
 */
public interface MethodSecurityExpressionHandler extends SecurityExpressionHandler<MethodInvocation> {

    /**
     * Filters a target collection or array. Only applies to method invocations.
     *
     * @param filterTarget     the array or collection to be filtered.
     * @param filterExpression the expression which should be used as the filter condition.
     *                         If it returns false on evaluation, the object will be removed from the returned collection
     * @param ctx              the current evaluation context (as created through a call to
     * @return the filtered collection or array
     */
    Object filter(Object filterTarget, Expression filterExpression, EvaluationContext ctx);

    /**
     * Used to inform the expression system of the return object for the given evaluation context. Only applies to method invocations.
     *
     * @param returnObject the return object value
     * @param ctx          the context within which the object should be set (as created through a
     */
    void setReturnObject(Object returnObject, EvaluationContext ctx);

}
