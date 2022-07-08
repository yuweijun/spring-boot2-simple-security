package com.example.spring.boot2.simple.security.v6.access.expression;

import com.example.spring.boot2.simple.security.v6.access.ConfigAttribute;
import org.springframework.expression.Expression;

/**
 * @since 2022-07-07.
 */
public class WebExpressionConfigAttribute implements ConfigAttribute {

    private final Expression authorizeExpression;

    public WebExpressionConfigAttribute(Expression authorizeExpression) {
        this.authorizeExpression = authorizeExpression;
    }

    Expression getAuthorizeExpression() {
        return this.authorizeExpression;
    }

    @Override
    public String getAttribute() {
        return null;
    }

    @Override
    public String toString() {
        return this.authorizeExpression.getExpressionString();
    }
}
