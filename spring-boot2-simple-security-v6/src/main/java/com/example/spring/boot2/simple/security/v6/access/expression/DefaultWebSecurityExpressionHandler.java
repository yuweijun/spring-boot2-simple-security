package com.example.spring.boot2.simple.security.v6.access.expression;

import com.example.spring.boot2.simple.security.v6.core.Authentication;
import com.example.spring.boot2.simple.security.v6.web.FilterInvocation;

/**
 * @since 2022-07-07.
 */
public class DefaultWebSecurityExpressionHandler extends AbstractSecurityExpressionHandler<FilterInvocation>
    implements SecurityExpressionHandler<FilterInvocation> {

    protected SecurityExpressionRoot createSecurityExpressionRoot(Authentication authentication, FilterInvocation fi) {
        WebSecurityExpressionRoot root = new WebSecurityExpressionRoot(authentication, fi);
        return root;
    }

}
