package com.example.spring.boot2.simple.security.access.expression;

import com.example.spring.boot2.simple.security.core.Authentication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.expression.BeanResolver;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

/**
 * @since 2022-07-07.
 */

public abstract class AbstractSecurityExpressionHandler<T> implements SecurityExpressionHandler<T>, ApplicationContextAware {

    private ExpressionParser expressionParser = new SpelExpressionParser();

    private BeanResolver br;

    protected abstract SecurityExpressionRoot createSecurityExpressionRoot(Authentication authentication, T invocation);

    @Override
    public final ExpressionParser getExpressionParser() {
        return expressionParser;
    }

    @Override
    public final EvaluationContext createEvaluationContext(Authentication authentication, T invocation) {
        SecurityExpressionRoot root = createSecurityExpressionRoot(authentication, invocation);
        StandardEvaluationContext ctx = createEvaluationContextInternal(authentication, invocation);
        ctx.setBeanResolver(br);
        ctx.setRootObject(root);

        return ctx;
    }

    protected StandardEvaluationContext createEvaluationContextInternal(Authentication authentication, T invocation) {
        return new StandardEvaluationContext();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        br = new BeanFactoryResolver(applicationContext);
    }
}