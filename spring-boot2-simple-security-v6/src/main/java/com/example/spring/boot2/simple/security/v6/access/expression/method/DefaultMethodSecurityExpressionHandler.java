package com.example.spring.boot2.simple.security.v6.access.expression.method;

import com.example.spring.boot2.simple.security.v6.access.expression.AbstractSecurityExpressionHandler;
import com.example.spring.boot2.simple.security.v6.access.expression.ExpressionUtils;
import com.example.spring.boot2.simple.security.v6.core.Authentication;
import com.example.spring.boot2.simple.security.v6.core.parameters.DefaultSecurityParameterNameDiscoverer;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

/**
 * @since 2022-07-08.
 */
public class DefaultMethodSecurityExpressionHandler extends AbstractSecurityExpressionHandler<MethodInvocation> implements MethodSecurityExpressionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultMethodSecurityExpressionHandler.class);

    private ParameterNameDiscoverer parameterNameDiscoverer = new DefaultSecurityParameterNameDiscoverer();

    private String defaultRolePrefix = "ROLE_";

    public DefaultMethodSecurityExpressionHandler() {
    }

    @Override
    public StandardEvaluationContext createEvaluationContextInternal(Authentication auth,
        MethodInvocation mi) {
        return new MethodSecurityEvaluationContext(auth, mi, getParameterNameDiscoverer());
    }

    @Override
    protected MethodSecurityExpressionRoot createSecurityExpressionRoot(
        Authentication authentication, MethodInvocation invocation) {
        MethodSecurityExpressionRoot root = new MethodSecurityExpressionRoot(authentication);
        root.setThis(invocation.getThis());

        return root;
    }

    @Override
    public Object filter(Object filterTarget, Expression filterExpression, EvaluationContext ctx) {
        MethodSecurityExpressionRoot rootObject = (MethodSecurityExpressionRoot) ctx.getRootObject().getValue();
        List<Object> retainList;

        LOGGER.debug("Filtering with expression: {}", filterExpression.getExpressionString());

        if (filterTarget instanceof Collection) {
            Collection<Object> collection = (Collection<Object>) filterTarget;
            retainList = new ArrayList<>(collection.size());

            LOGGER.debug("Filtering collection with " + collection.size() + " elements");

            for (Object filterObject : collection) {
                rootObject.setFilterObject(filterObject);

                if (ExpressionUtils.evaluateAsBoolean(filterExpression, ctx)) {
                    retainList.add(filterObject);
                }
            }

            LOGGER.debug("Retaining elements: " + retainList);

            collection.clear();
            collection.addAll(retainList);

            return filterTarget;
        }

        if (filterTarget.getClass().isArray()) {
            Object[] array = (Object[]) filterTarget;
            retainList = new ArrayList<>(array.length);

            LOGGER.debug("Filtering array with " + array.length + " elements");

            for (Object o : array) {
                rootObject.setFilterObject(o);

                if (ExpressionUtils.evaluateAsBoolean(filterExpression, ctx)) {
                    retainList.add(o);
                }
            }

            LOGGER.debug("Retaining elements: {}", retainList);

            Object[] filtered = (Object[]) Array.newInstance(filterTarget.getClass()
                                                                         .getComponentType(), retainList.size());
            for (int i = 0; i < retainList.size(); i++) {
                filtered[i] = retainList.get(i);
            }

            return filtered;
        }

        if (filterTarget instanceof Stream) {
            final Stream<?> original = (Stream<?>) filterTarget;
            return original.filter(filterObject -> {
                               rootObject.setFilterObject(filterObject);
                               return ExpressionUtils.evaluateAsBoolean(filterExpression, ctx);
                           })
                           .onClose(original::close);
        }

        throw new IllegalArgumentException("Filter target must be a collection, array, or stream type, but was " + filterTarget);
    }

    public void setParameterNameDiscoverer(ParameterNameDiscoverer parameterNameDiscoverer) {
        this.parameterNameDiscoverer = parameterNameDiscoverer;
    }

    protected ParameterNameDiscoverer getParameterNameDiscoverer() {
        return parameterNameDiscoverer;
    }

    @Override
    public void setReturnObject(Object returnObject, EvaluationContext ctx) {
        ((MethodSecurityExpressionRoot) ctx.getRootObject().getValue())
            .setReturnObject(returnObject);
    }

}
