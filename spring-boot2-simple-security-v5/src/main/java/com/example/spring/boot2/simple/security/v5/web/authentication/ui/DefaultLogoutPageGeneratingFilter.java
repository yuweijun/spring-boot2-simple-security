package com.example.spring.boot2.simple.security.v5.web.authentication.ui;

import com.example.spring.boot2.simple.security.v5.web.util.matcher.RegexRequestMatcher;
import com.example.spring.boot2.simple.security.v5.web.util.matcher.RequestMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.function.Function;

/**
 * @since 2022-06-29.
 */
public class DefaultLogoutPageGeneratingFilter extends OncePerRequestFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultLogoutPageGeneratingFilter.class);

    private RequestMatcher matcher = new RegexRequestMatcher("/logout", "GET");

    private Function<HttpServletRequest, Map<String, String>> resolveHiddenInputs = request -> Collections.emptyMap();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (this.matcher.matches(request)) {
            LOGGER.info("render HTML of logout page for : {}", request.getRequestURI());
            renderLogout(request, response);
        } else {
            filterChain.doFilter(request, response);
        }
    }

    private void renderLogout(HttpServletRequest request, HttpServletResponse response)
        throws IOException {
        String page = "<!DOCTYPE html>\n"
            + "<html lang=\"en\">\n"
            + "  <head>\n"
            + "    <meta charset=\"utf-8\">\n"
            + "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1, shrink-to-fit=no\">\n"
            + "    <meta name=\"description\" content=\"\">\n"
            + "    <meta name=\"author\" content=\"\">\n"
            + "    <title>Confirm Log Out?</title>\n"
            + "    <link href=\"https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta/css/bootstrap.min.css\" rel=\"stylesheet\" integrity=\"sha384-/Y6pD6FV/Vv2HJnA6t+vslU6fwYXjCFtcEpHbNJ0lyAFsXTsjBbfaDjzALeQsN6M\" crossorigin=\"anonymous\">\n"
            + "    <link href=\"https://getbootstrap.com/docs/4.0/examples/signin/signin.css\" rel=\"stylesheet\" crossorigin=\"anonymous\"/>\n"
            + "  </head>\n"
            + "  <body>\n"
            + "     <div class=\"container\">\n"
            + "      <form class=\"form-signin\" method=\"post\" action=\"" + request.getContextPath() + "/logout\">\n"
            + "        <h2 class=\"form-signin-heading\">Are you sure you want to log out?</h2>\n"
            + renderHiddenInputs(request)
            + "        <button class=\"btn btn-lg btn-primary btn-block\" type=\"submit\">Log Out</button>\n"
            + "      </form>\n"
            + "    </div>\n"
            + "  </body>\n"
            + "</html>";

        response.setContentType("text/html;charset=UTF-8");
        response.getWriter().write(page);
    }

    public void setResolveHiddenInputs(
        Function<HttpServletRequest, Map<String, String>> resolveHiddenInputs) {
        Assert.notNull(resolveHiddenInputs, "resolveHiddenInputs cannot be null");
        this.resolveHiddenInputs = resolveHiddenInputs;
    }

    private String renderHiddenInputs(HttpServletRequest request) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> input : this.resolveHiddenInputs.apply(request).entrySet()) {
            sb.append("<input name=\"").append(input.getKey()).append("\" type=\"hidden\" value=\"").append(input.getValue()).append("\" />\n");
        }
        return sb.toString();
    }
}
