package com.example.spring.boot2.simple.security.v5.web;

import com.example.spring.boot2.simple.security.v5.web.util.UrlUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @since 2022-07-06.
 */
public class FilterInvocation {

    private FilterChain chain;
    private HttpServletRequest request;
    private HttpServletResponse response;

    public FilterInvocation(ServletRequest request, ServletResponse response, FilterChain chain) {
        if ((request == null) || (response == null) || (chain == null)) {
            throw new IllegalArgumentException("Cannot pass null values to constructor");
        }

        this.request = (HttpServletRequest) request;
        this.response = (HttpServletResponse) response;
        this.chain = chain;
    }

    public FilterInvocation(String servletPath, String method) {
        this(null, servletPath, method);
    }

    public FilterInvocation(String contextPath, String servletPath, String method) {
        this(contextPath, servletPath, null, null, method);
    }

    public FilterInvocation(String contextPath, String servletPath, String pathInfo,
        String query, String method) {
        DummyRequest request = new DummyRequest();
        if (contextPath == null) {
            contextPath = "/cp";
        }
        request.setContextPath(contextPath);
        request.setServletPath(servletPath);
        request.setRequestURI(
            contextPath + servletPath + (pathInfo == null ? "" : pathInfo));
        request.setPathInfo(pathInfo);
        request.setQueryString(query);
        request.setMethod(method);
        this.request = request;
    }

    public FilterChain getChain() {
        return this.chain;
    }

    public String getFullRequestUrl() {
        return UrlUtils.buildFullRequestUrl(this.request);
    }

    public HttpServletRequest getHttpRequest() {
        return this.request;
    }

    public HttpServletResponse getHttpResponse() {
        return this.response;
    }

    public String getRequestUrl() {
        return UrlUtils.buildRequestUrl(this.request);
    }

    public HttpServletRequest getRequest() {
        return getHttpRequest();
    }

    public HttpServletResponse getResponse() {
        return getHttpResponse();
    }

    @Override
    public String toString() {
        return "FilterInvocation: URL: " + getRequestUrl();
    }

    static class DummyRequest extends HttpServletRequestWrapper {
        private static final HttpServletRequest UNSUPPORTED_REQUEST = (HttpServletRequest) Proxy
            .newProxyInstance(DummyRequest.class.getClassLoader(),
                new Class[]{HttpServletRequest.class},
                new UnsupportedOperationExceptionInvocationHandler());

        private String requestURI;
        private String contextPath = "";
        private String servletPath;
        private String pathInfo;
        private String queryString;
        private String method;

        DummyRequest() {
            super(UNSUPPORTED_REQUEST);
        }

        public String getCharacterEncoding() {
            return "UTF-8";
        }

        public Object getAttribute(String attributeName) {
            return null;
        }

        public void setRequestURI(String requestURI) {
            this.requestURI = requestURI;
        }

        public void setPathInfo(String pathInfo) {
            this.pathInfo = pathInfo;
        }

        @Override
        public String getRequestURI() {
            return this.requestURI;
        }

        public void setContextPath(String contextPath) {
            this.contextPath = contextPath;
        }

        @Override
        public String getContextPath() {
            return this.contextPath;
        }

        public void setServletPath(String servletPath) {
            this.servletPath = servletPath;
        }

        @Override
        public String getServletPath() {
            return this.servletPath;
        }

        public void setMethod(String method) {
            this.method = method;
        }

        @Override
        public String getMethod() {
            return this.method;
        }

        @Override
        public String getPathInfo() {
            return this.pathInfo;
        }

        @Override
        public String getQueryString() {
            return this.queryString;
        }

        public void setQueryString(String queryString) {
            this.queryString = queryString;
        }

        @Override
        public String getServerName() {
            return null;
        }

    }

    static class UnsupportedOperationExceptionInvocationHandler implements InvocationHandler {
        public Object invoke(Object proxy, Method method, Object[] args) {
            throw new UnsupportedOperationException(method + " is not supported");
        }
    }
}
