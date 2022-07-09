package com.example.spring.boot2.simple.security.v6.access.expression.method;

import com.example.spring.boot2.simple.security.v6.access.prepost.PreInvocationAttribute;
import com.example.spring.boot2.simple.security.v6.access.prepost.PrePostInvocationAttributeFactory;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.ParseException;

/**
 * @since 2022-07-09.
 */
public class ExpressionBasedAnnotationAttributeFactory implements PrePostInvocationAttributeFactory {

    private final Object parserLock = new Object();

    private ExpressionParser parser;

    private MethodSecurityExpressionHandler handler;

    public ExpressionBasedAnnotationAttributeFactory(MethodSecurityExpressionHandler handler) {
        this.handler = handler;
    }

    @Override
    public PreInvocationAttribute createPreInvocationAttribute(String preAuthorizeAttribute) {
        try {
            ExpressionParser parser = getParser();
            Expression preAuthorizeExpression;
            if (preAuthorizeAttribute == null) {
                preAuthorizeExpression = parser.parseExpression("permitAll");
            } else {
                preAuthorizeExpression = parser.parseExpression(preAuthorizeAttribute);
            }
            return new PreInvocationExpressionAttribute(preAuthorizeExpression);
        } catch (ParseException e) {
            throw new IllegalArgumentException("Failed to parse expression '" + e.getExpressionString() + "'", e);
        }
    }

    private ExpressionParser getParser() {
        if (this.parser != null) {
            return this.parser;
        }
        synchronized (parserLock) {
            this.parser = handler.getExpressionParser();
            this.handler = null;
        }
        return this.parser;
    }
}
