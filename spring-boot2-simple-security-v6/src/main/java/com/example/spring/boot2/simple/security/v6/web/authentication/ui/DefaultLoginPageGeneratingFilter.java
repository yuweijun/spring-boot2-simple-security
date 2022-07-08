package com.example.spring.boot2.simple.security.v6.web.authentication.ui;

import com.example.spring.boot2.simple.security.v6.core.AuthenticationException;
import com.example.spring.boot2.simple.security.v6.web.WebAttributes;
import com.example.spring.boot2.simple.security.v6.web.authentication.filter.UsernamePasswordAuthenticationFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.util.HtmlUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Map;
import java.util.function.Function;

/**
 * @since 2022-06-29.
 */
public class DefaultLoginPageGeneratingFilter extends GenericFilterBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultLoginPageGeneratingFilter.class);

    public static final String DEFAULT_LOGIN_PAGE_URL = "/login";
    public static final String ERROR_PARAMETER_NAME = "error";

    private String loginPageUrl;
    private String logoutSuccessUrl;
    private String failureUrl;
    private String usernameParameter;
    private String passwordParameter;
    private String rememberMeParameter;
    private Function<HttpServletRequest, Map<String, String>> resolveHiddenInputs = request -> Collections.emptyMap();

    public DefaultLoginPageGeneratingFilter() {
    }

    public DefaultLoginPageGeneratingFilter(UsernamePasswordAuthenticationFilter filter) {
        init(filter);
    }

    private void init(UsernamePasswordAuthenticationFilter authFilter) {
        this.loginPageUrl = DEFAULT_LOGIN_PAGE_URL;
        this.logoutSuccessUrl = DEFAULT_LOGIN_PAGE_URL + "?logout";
        this.failureUrl = DEFAULT_LOGIN_PAGE_URL + "?" + ERROR_PARAMETER_NAME;
        if (authFilter != null) {
            usernameParameter = authFilter.getUsernameParameter();
            passwordParameter = authFilter.getPasswordParameter();
        }
    }

    public void setResolveHiddenInputs(Function<HttpServletRequest, Map<String, String>> resolveHiddenInputs) {
        this.resolveHiddenInputs = resolveHiddenInputs;
    }

    public void setLogoutSuccessUrl(String logoutSuccessUrl) {
        this.logoutSuccessUrl = logoutSuccessUrl;
    }

    public String getLoginPageUrl() {
        return loginPageUrl;
    }

    public void setLoginPageUrl(String loginPageUrl) {
        this.loginPageUrl = loginPageUrl;
    }

    public void setFailureUrl(String failureUrl) {
        this.failureUrl = failureUrl;
    }

    public void setUsernameParameter(String usernameParameter) {
        this.usernameParameter = usernameParameter;
    }

    public void setPasswordParameter(String passwordParameter) {
        this.passwordParameter = passwordParameter;
    }

    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
        throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        boolean loginError = isErrorPage(request);
        boolean logoutSuccess = isLogoutSuccess(request);
        if (isLoginUrlRequest(request) || loginError || logoutSuccess) {
            LOGGER.info("render HTML of login page for : {}", request.getRequestURI());
            String loginPageHtml = generateLoginPageHtml(request, loginError, logoutSuccess);
            response.setContentType("text/html;charset=UTF-8");
            response.setContentLength(loginPageHtml.getBytes(StandardCharsets.UTF_8).length);
            response.getWriter().write(loginPageHtml);

            return;
        }

        chain.doFilter(request, response);
    }

    private String generateLoginPageHtml(HttpServletRequest request, boolean loginError, boolean logoutSuccess) {
        String errorMsg = "Invalid credentials";

        if (loginError) {
            HttpSession session = request.getSession(false);

            if (session != null) {
                AuthenticationException ex = (AuthenticationException) session.getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
                errorMsg = ex != null ? ex.getMessage() : "Invalid credentials";
            }
        }

        String html = "<!DOCTYPE html>\n"
            + "<html lang=\"en\">\n"
            + "  <head>\n"
            + "    <meta charset=\"utf-8\">\n"
            + "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1, shrink-to-fit=no\">\n"
            + "    <meta name=\"description\" content=\"\">\n"
            + "    <meta name=\"author\" content=\"\">\n"
            + "    <title>Please sign in</title>\n"
            + "    <link href=\"https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta/css/bootstrap.min.css\" rel=\"stylesheet\" integrity=\"sha384-/Y6pD6FV/Vv2HJnA6t+vslU6fwYXjCFtcEpHbNJ0lyAFsXTsjBbfaDjzALeQsN6M\" crossorigin=\"anonymous\">\n"
            + "    <link href=\"https://getbootstrap.com/docs/4.0/examples/signin/signin.css\" rel=\"stylesheet\" crossorigin=\"anonymous\"/>\n"
            + "  </head>\n"
            + "  <body>\n"
            + "     <div class=\"container\">\n"
            + "      <form class=\"form-signin\" method=\"post\" action=\"" + request.getContextPath() + this.loginPageUrl + "\">\n"
            + "        <h2 class=\"form-signin-heading\">Please sign in</h2>\n"
            + createError(loginError, errorMsg)
            + createLogoutSuccess(logoutSuccess)
            + "        <p>\n"
            + "          <label for=\"username\" class=\"sr-only\">Username</label>\n"
            + "          <input type=\"text\" id=\"username\" name=\"" + this.usernameParameter + "\" class=\"form-control\" placeholder=\"Username\" required autofocus>\n"
            + "        </p>\n"
            + "        <p>\n"
            + "          <label for=\"password\" class=\"sr-only\">Password</label>\n"
            + "          <input type=\"password\" id=\"password\" name=\"" + this.passwordParameter + "\" class=\"form-control\" placeholder=\"Password\" required>\n"
            + "        </p>\n"
            + createRememberMe(this.rememberMeParameter)
            + renderHiddenInputs(request)
            + "        <button class=\"btn btn-lg btn-primary btn-block\" type=\"submit\">Sign in</button>\n"
            + "      </form>\n"
            + "    </div>\n"
            + "  </body>\n"
            + "</html>";

        return html;
    }

    private String renderHiddenInputs(HttpServletRequest request) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> input : this.resolveHiddenInputs.apply(request).entrySet()) {
            sb.append("<input name=\"").append(input.getKey()).append("\" type=\"hidden\" value=\"").append(input.getValue()).append("\" />\n");
        }
        return sb.toString();
    }

    private String createRememberMe(String paramName) {
        if (paramName == null) {
            return "";
        }
        return "<p><input type='checkbox' name='" + paramName + "'/> Remember me on this computer.</p>\n";
    }

    private boolean isLogoutSuccess(HttpServletRequest request) {
        return logoutSuccessUrl != null && matches(request, logoutSuccessUrl);
    }

    private boolean isLoginUrlRequest(HttpServletRequest request) {
        return matches(request, loginPageUrl);
    }

    private boolean isErrorPage(HttpServletRequest request) {
        return matches(request, failureUrl);
    }

    private static String createError(boolean isError, String message) {
        return isError ? "<div class=\"alert alert-danger\" role=\"alert\">" + HtmlUtils.htmlEscape(message) + "</div>" : "";
    }

    private static String createLogoutSuccess(boolean isLogoutSuccess) {
        return isLogoutSuccess ? "<div class=\"alert alert-success\" role=\"alert\">You have been signed out</div>" : "";
    }

    private boolean matches(HttpServletRequest request, String url) {
        if (!"GET".equals(request.getMethod()) || url == null) {
            return false;
        }
        String uri = request.getRequestURI();
        int pathParamIndex = uri.indexOf(';');

        if (pathParamIndex > 0) {
            uri = uri.substring(0, pathParamIndex);
        }

        if (request.getQueryString() != null) {
            uri += "?" + request.getQueryString();
        }

        if ("".equals(request.getContextPath())) {
            return uri.equals(url);
        }

        return uri.equals(request.getContextPath() + url);
    }
}
