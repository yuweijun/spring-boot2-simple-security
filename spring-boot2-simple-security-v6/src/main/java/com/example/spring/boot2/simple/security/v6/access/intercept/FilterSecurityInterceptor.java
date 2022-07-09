package com.example.spring.boot2.simple.security.v6.access.intercept;

import com.example.spring.boot2.simple.security.v6.web.FilterInvocation;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

/**
 * @since 2022-07-06.
 */
public class FilterSecurityInterceptor extends AbstractSecurityInterceptor implements Filter {

    private SecurityMetadataSource securityMetadataSource;

    @Override
    public SecurityMetadataSource getSecurityMetadataSource() {
        return securityMetadataSource;
    }

    public void setSecurityMetadataSource(SecurityMetadataSource securityMetadataSource) {
        this.securityMetadataSource = securityMetadataSource;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        FilterInvocation fi = new FilterInvocation(request, response, chain);
        invoke(fi);
    }

    public void invoke(FilterInvocation fi) throws IOException, ServletException {
        InterceptorStatusToken token = beforeInvocation(fi);

        try {
            fi.getChain().doFilter(fi.getRequest(), fi.getResponse());
        } finally {
            finallyInvocation(token);
        }

        afterInvocation(token, null);
    }

}
