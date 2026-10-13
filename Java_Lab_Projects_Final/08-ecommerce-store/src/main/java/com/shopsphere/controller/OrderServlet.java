package com.shopsphere.controller;

import com.shopsphere.model.Order;
import com.shopsphere.model.User;
import com.shopsphere.service.OrderService;
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
 * Controller displaying customer order history and detailed order tracking breakdown.
 */
@WebServlet(name = "OrderServlet", urlPatterns = {"/orders", "/order-details"})
public class OrderServlet extends HttpServlet {

    private OrderService orderService;

    @Override
    public void init() throws ServletException {
        this.orderService = new OrderService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;
        if (user == null) {
            res.sendRedirect(req.getContextPath() + "/login?error=auth_required");
            return;
        }

        String servletPath = req.getServletPath();

        try {
            if ("/order-details".equals(servletPath)) {
                String idStr = req.getParameter("id");
                if (idStr == null || idStr.trim().isEmpty()) {
                    res.sendRedirect(req.getContextPath() + "/orders");
                    return;
                }
                int orderId = Integer.parseInt(idStr.trim());
                Order order = orderService.getOrderById(orderId);

                if (order == null || (order.getUserId() != user.getUserId() && !user.isAdmin())) {
                    res.sendError(HttpServletResponse.SC_NOT_FOUND, "Order not found or unauthorized.");
                    return;
                }

                req.setAttribute("order", order);
                req.getRequestDispatcher("/order-details.jsp").forward(req, res);

            } else {
                // Customer order history list
                List<Order> orders = orderService.getOrdersByUser(user.getUserId());
                req.setAttribute("orders", orders);
                req.getRequestDispatcher("/orders.jsp").forward(req, res);
            }

        } catch (NumberFormatException e) {
            res.sendRedirect(req.getContextPath() + "/orders");
        } catch (SQLException e) {
            throw new ServletException("Database error loading customer orders", e);
        }
    }
}
