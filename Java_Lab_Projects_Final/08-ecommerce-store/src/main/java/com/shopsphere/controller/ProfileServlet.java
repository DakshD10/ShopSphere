package com.shopsphere.controller;

import com.shopsphere.dao.AddressDAO;
import com.shopsphere.dao.OrderDAO;
import com.shopsphere.dao.UserDAO;
import com.shopsphere.model.Address;
import com.shopsphere.model.Order;
import com.shopsphere.model.User;
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
 * ProfileServlet: Controller for customer account profile, addresses, and order summary.
 */
@WebServlet(name = "ProfileServlet", urlPatterns = {"/profile"})
public class ProfileServlet extends HttpServlet {

    private final AddressDAO addressDAO = new AddressDAO();
    private final OrderDAO orderDAO = new OrderDAO();
    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;

        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login?redirect=/profile");
            return;
        }

        try {
            // Reload user fresh from DB
            User freshUser = userDAO.findById(user.getUserId());
            if (freshUser != null) {
                session.setAttribute("user", freshUser);
                user = freshUser;
            }

            List<Address> addresses = addressDAO.findByUserId(user.getUserId());
            List<Order> recentOrders = orderDAO.findByUserId(user.getUserId());

            req.setAttribute("profileUser", user);
            req.setAttribute("addresses", addresses);
            req.setAttribute("recentOrders", recentOrders);
            req.getRequestDispatcher("/profile.jsp").forward(req, resp);

        } catch (SQLException e) {
            throw new ServletException("Database error loading profile", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;

        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String action = req.getParameter("action");
        if ("addAddress".equals(action)) {
            String line = req.getParameter("addressLine");
            String city = req.getParameter("city");
            String state = req.getParameter("state");
            String pin = req.getParameter("pincode");
            String type = req.getParameter("addressType");

            if (line != null && !line.trim().isEmpty() && pin != null && !pin.trim().isEmpty()) {
                Address a = new Address();
                a.setUserId(user.getUserId());
                a.setAddressLine(line.trim());
                a.setCity(city != null ? city.trim() : "");
                a.setState(state != null ? state.trim() : "");
                a.setPincode(pin.trim());
                a.setAddressType(type != null ? type.trim() : "HOME");
                try {
                    addressDAO.createAddress(a);
                    session.setAttribute("flashSuccess", "New address saved to your profile!");
                } catch (SQLException e) {
                    session.setAttribute("flashError", "Failed to save address: " + e.getMessage());
                }
            }
        }

        resp.sendRedirect(req.getContextPath() + "/profile");
    }
}
