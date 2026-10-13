package com.servlet;

import java.io.IOException;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;

/**
 * Section 32 & Practical Task 7 of Professor PDF:
 * Simple Filter Application that intercepts all requests ("/*"),
 * logs the incoming request URI and HTTP method to standard output / Tomcat log,
 * and calls chain.doFilter() to continue processing.
 */
@WebFilter("/*")
public class MyFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        String uri = req.getRequestURI();
        String method = req.getMethod();

        System.out.println("[MyFilter] Request intercepted -> URI: " + uri + " | Method: " + method);

        // Required: Pass processing to the next component in the filter chain
        chain.doFilter(request, response);

        System.out.println("[MyFilter] Response completed -> URI: " + uri);
    }
}
