package com.example.spring.boot2.simple.security.v4.web.savedrequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

/**
 * @since 2022-06-29.
 */
public class DefaultSavedRequest {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultSavedRequest.class);

    private static final String HEADER_IF_NONE_MATCH = "If-None-Match";
    private static final String HEADER_IF_MODIFIED_SINCE = "If-Modified-Since";

    private final ArrayList<Locale> locales = new ArrayList<>();
    private final Map<String, List<String>> headers = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
    private final Map<String, String[]> parameters = new TreeMap<>();
    private final String contextPath;
    private final String method;
    private final String pathInfo;
    private final String queryString;
    private final String requestURI;
    private final String requestURL;
    private final String scheme;
    private final String serverName;
    private final String servletPath;
    private final int serverPort;

    public DefaultSavedRequest(HttpServletRequest request) {
        Assert.notNull(request, "Request required");

        // Headers
        Enumeration<String> names = request.getHeaderNames();

        while (names.hasMoreElements()) {
            String name = names.nextElement();
            // Skip If-Modified-Since and If-None-Match header. SEC-1412, SEC-1624.
            if (HEADER_IF_MODIFIED_SINCE.equalsIgnoreCase(name)
                || HEADER_IF_NONE_MATCH.equalsIgnoreCase(name)) {
                continue;
            }
            Enumeration<String> values = request.getHeaders(name);

            while (values.hasMoreElements()) {
                this.addHeader(name, values.nextElement());
            }
        }

        // Locales
        addLocales(request.getLocales());

        // Parameters
        addParameters(request.getParameterMap());

        // Primitives
        this.method = request.getMethod();
        this.pathInfo = request.getPathInfo();
        this.queryString = request.getQueryString();
        this.requestURI = request.getRequestURI();
        this.serverPort = getServerPort(request);
        this.requestURL = request.getRequestURL().toString();
        this.scheme = request.getScheme();
        this.serverName = request.getServerName();
        this.contextPath = request.getContextPath();
        this.servletPath = request.getServletPath();
    }

    private void addHeader(String name, String value) {
        List<String> values = headers.computeIfAbsent(name, k -> new ArrayList<>());

        values.add(value);
    }

    private void addLocales(Enumeration<Locale> locales) {
        while (locales.hasMoreElements()) {
            Locale locale = locales.nextElement();
            this.addLocale(locale);
        }
    }

    private void addLocale(Locale locale) {
        locales.add(locale);
    }

    private void addParameters(Map<String, String[]> parameters) {
        if (!ObjectUtils.isEmpty(parameters)) {
            for (String paramName : parameters.keySet()) {
                Object paramValues = parameters.get(paramName);
                if (paramValues instanceof String[]) {
                    this.addParameter(paramName, (String[]) paramValues);
                } else {
                    LOGGER.warn("ServletRequest.getParameterMap() returned non-String array");
                }
            }
        }
    }

    private void addParameter(String name, String[] values) {
        parameters.put(name, values);
    }

    public boolean doesRequestMatch(HttpServletRequest request) {
        if (!propertyEquals("pathInfo", this.pathInfo, request.getPathInfo())) {
            return false;
        }

        if (!propertyEquals("queryString", this.queryString, request.getQueryString())) {
            return false;
        }

        if (!propertyEquals("requestURI", this.requestURI, request.getRequestURI())) {
            return false;
        }

        if (!"GET".equals(request.getMethod()) && "GET".equals(method)) {
            // A save GET should not match an incoming non-GET method
            return false;
        }

        if (!propertyEquals("serverPort", this.serverPort, getServerPort(request))) {
            return false;
        }

        if (!propertyEquals("requestURL", this.requestURL, request.getRequestURL().toString())) {
            return false;
        }

        if (!propertyEquals("scheme", this.scheme, request.getScheme())) {
            return false;
        }

        if (!propertyEquals("serverName", this.serverName, request.getServerName())) {
            return false;
        }

        if (!propertyEquals("contextPath", this.contextPath, request.getContextPath())) {
            return false;
        }

        return propertyEquals("servletPath", this.servletPath, request.getServletPath());
    }

    public String getContextPath() {
        return contextPath;
    }

    public String getRedirectUrl() {
        return buildFullRequestUrl(scheme, serverName, serverPort, requestURI, queryString);
    }

    public Collection<String> getHeaderNames() {
        return headers.keySet();
    }

    public List<String> getHeaderValues(String name) {
        List<String> values = headers.get(name);

        if (values == null) {
            return Collections.emptyList();
        }

        return values;
    }

    public List<Locale> getLocales() {
        return locales;
    }

    public String getMethod() {
        return method;
    }

    public Map<String, String[]> getParameterMap() {
        return parameters;
    }

    public Collection<String> getParameterNames() {
        return parameters.keySet();
    }

    public String[] getParameterValues(String name) {
        return parameters.get(name);
    }

    public String getPathInfo() {
        return pathInfo;
    }

    public String getQueryString() {
        return (this.queryString);
    }

    public String getRequestURI() {
        return (this.requestURI);
    }

    public String getRequestURL() {
        return requestURL;
    }

    public String getScheme() {
        return scheme;
    }

    public String getServerName() {
        return serverName;
    }

    public int getServerPort() {
        return serverPort;
    }

    public String getServletPath() {
        return servletPath;
    }

    private boolean propertyEquals(String log, Object arg1, Object arg2) {
        if ((arg1 == null) && (arg2 == null)) {
            LOGGER.debug(log + ": both null (property equals)");

            return true;
        }

        if (arg1 == null || arg2 == null) {
            LOGGER.debug(log + ": arg1=" + arg1 + "; arg2=" + arg2 + " (property not equals)");

            return false;
        }

        if (arg1.equals(arg2)) {
            LOGGER.debug(log + ": arg1=" + arg1 + "; arg2=" + arg2 + " (property equals)");

            return true;
        } else {
            LOGGER.debug(log + ": arg1=" + arg1 + "; arg2=" + arg2 + " (property not equals)");

            return false;
        }
    }

    @Override
    public String toString() {
        return "DefaultSavedRequest[" + getRedirectUrl() + "]";
    }

    public int getServerPort(ServletRequest request) {
        return request.getServerPort();
    }

    public static String buildFullRequestUrl(String scheme, String serverName,
        int serverPort, String requestURI, String queryString) {

        scheme = scheme.toLowerCase();

        StringBuilder url = new StringBuilder();
        url.append(scheme).append("://").append(serverName);

        // Only add port if not default
        if ("http".equals(scheme)) {
            if (serverPort != 80) {
                url.append(":").append(serverPort);
            }
        } else if ("https".equals(scheme)) {
            if (serverPort != 443) {
                url.append(":").append(serverPort);
            }
        }

        // Use the requestURI as it is encoded (RFC 3986) and hence suitable for
        // redirects.
        url.append(requestURI);

        if (queryString != null) {
            url.append("?").append(queryString);
        }

        return url.toString();
    }

}
