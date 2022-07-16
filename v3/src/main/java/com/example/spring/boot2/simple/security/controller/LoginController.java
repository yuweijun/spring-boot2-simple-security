package com.example.spring.boot2.simple.security.controller;

import com.example.spring.boot2.simple.security.web.savedrequest.DefaultSavedRequest;
import com.example.spring.boot2.simple.security.web.savedrequest.HttpSessionRequestCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * @author yuweijun 2022-06-24.
 */
@Controller
public class LoginController {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginController.class);

    private HttpSessionRequestCache requestCache = new HttpSessionRequestCache();

    @GetMapping("/login")
    public String getLoginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String postUserLogin(HttpServletRequest request) {
        LOGGER.info("post to login page");
        final DefaultSavedRequest savedRequest = requestCache.getRequest(request);
        if (savedRequest != null) {
            final String redirectUrl = savedRequest.getRedirectUrl();
            LOGGER.debug("get redirectUrl from savedRequest : {}", redirectUrl);
            return "redirect:" + redirectUrl;
        }

        return "redirect:/";
    }

}
