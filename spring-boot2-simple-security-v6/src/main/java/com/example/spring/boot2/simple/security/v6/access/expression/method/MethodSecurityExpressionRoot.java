package com.example.spring.boot2.simple.security.v6.access.expression.method;

import com.example.spring.boot2.simple.security.v6.access.expression.SecurityExpressionRoot;
import com.example.spring.boot2.simple.security.v6.core.Authentication;

/**
 * @since 2022-07-08.
 */
public class MethodSecurityExpressionRoot extends SecurityExpressionRoot {

    private Object filterObject;

    private Object returnObject;

    private Object target;

    MethodSecurityExpressionRoot(Authentication a) {
        super(a);
    }

    public void setFilterObject(Object filterObject) {
        this.filterObject = filterObject;
    }

    public Object getFilterObject() {
        return filterObject;
    }

    public void setReturnObject(Object returnObject) {
        this.returnObject = returnObject;
    }

    public Object getReturnObject() {
        return returnObject;
    }

    /**
     * Sets the "this" property for use in expressions. Typically, this will be the "this"
     * property of the {@code JoinPoint} representing the method invocation which is being
     * protected.
     *
     * @param target the target object on which the method in is being invoked.
     */
    void setThis(Object target) {
        this.target = target;
    }

    public Object getThis() {
        return target;
    }
}
