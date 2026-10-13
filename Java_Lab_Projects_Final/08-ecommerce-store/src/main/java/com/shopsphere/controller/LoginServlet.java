package com.shopsphere.controller;

import com.shopsphere.model.User;
import com.shopsphere.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Controller handling user authentication (both Customer and Administrator).
 */
@WebServlet(name = "LoginServlet", urlPatterns = {"/login"})
public class LoginServlet extends HttpServlet {

    private UserService userService;

    @Override
    public void init() throws ServletException {
        this.userService = new UserService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            User user = (User) session.getAttribute("user");
            if (user.isAdmin()) {
                res.sendRedirect(req.getContextPath() + "/admin/dashboard");
            } else {
                res.sendRedirect(req.getContextPath() + "/products");
            }
            return;
        }

        String errorParam = req.getParameter("error");
        if ("auth_required".equals(errorParam)) {
            req.setAttribute("error", "Please log in to proceed.");
        } else if ("admin_required".equals(errorParam)) {
            req.setAttribute("error", "Administrator credentials required.");
        }

        String noticeParam = req.getParameter("notice");
        if ("logged_out".equals(noticeParam)) {
            req.setAttribute("notice", "You have been logged out successfully.");
        }

        req.getRequestDispatcher("/login.jsp").forward(req, res);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        String email = req.getParameter("email");
        String password = req.getParameter("password");

        try {
            User user = userService.login(email, password);
            if (user == null) {
                req.setAttribute("error", "Invalid email or password.");
                req.setAttribute("email", email);
                req.getRequestDispatcher("/login.jsp").forward(req, res);
                return;
            }

            HttpSession session = req.getSession(true);
            session.setAttribute("user", user);
            session.setAttribute("role", user.getRole());

            // Handle post-login redirection if user was attempting to reach a protected page
            String redirectUrl = (String) session.getAttribute("redirectAfterLogin");
            if (redirectUrl != null && !redirectUrl.isEmpty()) {
                session.removeAttribute("redirectAfterLogin");
                res.sendRedirect(redirectUrl);
                return;
            }

            if (user.isAdmin()) {
                res.sendRedirect(req.getContextPath() + "/admin/dashboard");
            } else {
                res.sendRedirect(req.getContextPath() + "/products");
            }

        } catch (IllegalStateException e) {
            req.setAttribute("error", e.getMessage());
            req.setAttribute("email", email);
            req.getRequestDispatcher("/login.jsp").forward(req, res);

        } catch (SQLException e) {
            req.setAttribute("error", "Database connection error. Please verify database service.");
            req.setAttribute("email", email);
            req.getRequestDispatcher("/login.jsp").forward(req, res);
        }
    }
}
