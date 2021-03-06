package com.example.spring.boot2.simple.security.web;

import javax.servlet.Filter;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author yuweijun 2022-06-26.
 */
public interface SecurityFilterChain {

    boolean matches(HttpServletRequest request);

    List<Filter> getFilters();

}
