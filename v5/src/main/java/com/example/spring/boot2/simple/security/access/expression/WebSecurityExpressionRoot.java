package com.example.spring.boot2.simple.security.access.expression;

import com.example.spring.boot2.simple.security.core.Authentication;
import com.example.spring.boot2.simple.security.web.FilterInvocation;

import javax.servlet.http.HttpServletRequest;

/**
 * @since 2022-07-07.
 */
public class WebSecurityExpressionRoot extends SecurityExpressionRoot {

    public final HttpServletRequest request;

    private FilterInvocation filterInvocation;

    public WebSecurityExpressionRoot(Authentication a, FilterInvocation fi) {
        super(a);
        this.filterInvocation = fi;
        this.request = fi.getRequest();
    }

}
