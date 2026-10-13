package com.shopsphere.filter;

import com.shopsphere.model.User;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Set;

/**
 * Filter that protects customer account, checkout, and order routes.
 * Redirects unauthenticated requests to /login.
 */
@WebFilter(filterName = "AuthenticationFilter", urlPatterns = {
        "/cart", "/checkout", "/orders", "/order-details", "/order-success", "/profile", "/wishlist", "/add-review"
})
public class AuthenticationFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        HttpSession session = httpRequest.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;

        if (user == null) {
            // Save requested URL so user returns to it after login
            HttpSession newSession = httpRequest.getSession(true);
            String targetUri = httpRequest.getRequestURI();
            String queryString = httpRequest.getQueryString();
            if (queryString != null && !queryString.isEmpty()) {
                targetUri += "?" + queryString;
            }
            newSession.setAttribute("redirectAfterLogin", targetUri);

            httpResponse.sendRedirect(httpRequest.getContextPath() + "/login?error=auth_required");
            return;
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {}
}
