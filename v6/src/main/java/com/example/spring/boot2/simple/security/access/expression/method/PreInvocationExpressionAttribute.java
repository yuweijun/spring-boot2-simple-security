package com.example.spring.boot2.simple.security.access.expression.method;

import com.example.spring.boot2.simple.security.access.prepost.PreInvocationAttribute;
import org.springframework.expression.Expression;
import org.springframework.expression.ParseException;

/**
 * @since 2022-07-09.
 */
class PreInvocationExpressionAttribute extends AbstractExpressionBasedMethodConfigAttribute implements PreInvocationAttribute {

    PreInvocationExpressionAttribute(String authorizeExpression) throws ParseException {
        super(authorizeExpression);
    }

    PreInvocationExpressionAttribute(Expression authorizeExpression) throws ParseException {
        super(authorizeExpression);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        Expression authorize = getAuthorizeExpression();
        sb.append("[authorize: '");
        sb.append(authorize == null ? "null" : authorize.getExpressionString());
        sb.append("']");
        return sb.toString();
    }
}
