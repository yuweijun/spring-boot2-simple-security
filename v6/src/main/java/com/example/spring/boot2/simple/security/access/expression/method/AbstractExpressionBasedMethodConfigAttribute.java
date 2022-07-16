package com.example.spring.boot2.simple.security.access.expression.method;

import com.example.spring.boot2.simple.security.access.ConfigAttribute;
import org.springframework.expression.Expression;
import org.springframework.expression.ParseException;
import org.springframework.expression.spel.standard.SpelExpressionParser;

/**
 * @since 2022-07-09.
 */
abstract class AbstractExpressionBasedMethodConfigAttribute implements ConfigAttribute {

    private final Expression authorizeExpression;

    AbstractExpressionBasedMethodConfigAttribute(String authorizeExpression) throws ParseException {
        SpelExpressionParser parser = new SpelExpressionParser();
        this.authorizeExpression = authorizeExpression == null ? null : parser.parseExpression(authorizeExpression);
    }

    AbstractExpressionBasedMethodConfigAttribute(Expression authorizeExpression) throws ParseException {
        this.authorizeExpression = authorizeExpression == null ? null : authorizeExpression;
    }

    Expression getAuthorizeExpression() {
        return authorizeExpression;
    }

    public String getAttribute() {
        return null;
    }
}
