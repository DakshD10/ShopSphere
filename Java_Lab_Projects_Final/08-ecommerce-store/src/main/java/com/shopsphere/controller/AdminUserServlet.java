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
import java.util.List;

/**
 * Controller providing Administrator Customer and Account Management.
 */
@WebServlet(name = "AdminUserServlet", urlPatterns = {"/admin/users"})
public class AdminUserServlet extends HttpServlet {

    private UserService userService;

    @Override
    public void init() throws ServletException {
        this.userService = new UserService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        try {
            List<User> users = userService.getAllUsers();
            req.setAttribute("users", users);
            req.getRequestDispatcher("/admin/users.jsp").forward(req, res);
        } catch (SQLException e) {
            throw new ServletException("Database error loading users list", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        HttpSession session = req.getSession(false);

        try {
            int userId = Integer.parseInt(req.getParameter("userId"));
            String status = req.getParameter("status");

            if (status != null && !status.trim().isEmpty()) {
                userService.updateUserStatus(userId, status.trim());
                if (session != null) {
                    session.setAttribute("notice", "Account status updated for User ID #" + userId);
                }
            }

            res.sendRedirect(req.getContextPath() + "/admin/users");

        } catch (NumberFormatException e) {
            res.sendRedirect(req.getContextPath() + "/admin/users");
        } catch (SQLException e) {
            throw new ServletException("Database error updating account status", e);
        }
    }
}
