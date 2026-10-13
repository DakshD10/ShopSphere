package com.servlet;

import java.io.IOException;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * Section 33.3 & Practical Task 8 of Professor PDF:
 * Authentication Filter protecting /home.
 * If user has a valid session with "username", chain.doFilter() is called.
 * Otherwise, the request is redirected to login.html.
 */
@WebFilter("/home")
public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        // Obtain existing session without creating a new one
        HttpSession session = req.getSession(false);

        if (session != null && session.getAttribute("username") != null) {
            // User is authenticated, proceed down the filter chain to HomeServlet
            chain.doFilter(request, response);
        } else {
            // Unauthenticated: redirect to login form
            String contextPath = req.getContextPath();
            res.sendRedirect(contextPath + "/login.html");
        }
    }
}
