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
 * Controller handling user registration requests.
 */
@WebServlet(name = "RegisterServlet", urlPatterns = {"/register"})
public class RegisterServlet extends HttpServlet {

    private UserService userService;

    @Override
    public void init() throws ServletException {
        this.userService = new UserService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        // If already logged in, redirect to home
        HttpSession session = req.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            res.sendRedirect(req.getContextPath() + "/products");
            return;
        }
        req.getRequestDispatcher("/register.jsp").forward(req, res);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        String name = req.getParameter("name");
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        String mobile = req.getParameter("mobile");

        try {
            User newUser = userService.register(name, email, password, mobile);

            // Automatically log in newly registered customer
            HttpSession session = req.getSession(true);
            session.setAttribute("user", newUser);
            session.setAttribute("role", newUser.getRole());

            res.sendRedirect(req.getContextPath() + "/products?notice=registered");

        } catch (IllegalArgumentException e) {
            req.setAttribute("error", e.getMessage());
            req.setAttribute("name", name);
            req.setAttribute("email", email);
            req.setAttribute("mobile", mobile);
            req.getRequestDispatcher("/register.jsp").forward(req, res);

        } catch (SQLException e) {
            req.setAttribute("error", "Database error occurred during registration. Please try again.");
            req.setAttribute("name", name);
            req.setAttribute("email", email);
            req.setAttribute("mobile", mobile);
            req.getRequestDispatcher("/register.jsp").forward(req, res);
        }
    }
}
