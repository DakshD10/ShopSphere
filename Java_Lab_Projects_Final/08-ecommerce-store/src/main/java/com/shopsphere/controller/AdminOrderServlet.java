package com.shopsphere.controller;

import com.shopsphere.model.Order;
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
 * Controller providing Administrator Order status progression and payment status management.
 */
@WebServlet(name = "AdminOrderServlet", urlPatterns = {"/admin/orders"})
public class AdminOrderServlet extends HttpServlet {

    private OrderService orderService;

    @Override
    public void init() throws ServletException {
        this.orderService = new OrderService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        try {
            List<Order> orders = orderService.getAllOrders();
            req.setAttribute("orders", orders);
            req.getRequestDispatcher("/admin/orders.jsp").forward(req, res);
        } catch (SQLException e) {
            throw new ServletException("Database error loading administrative orders", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        HttpSession session = req.getSession(false);

        try {
            int orderId = Integer.parseInt(req.getParameter("orderId"));
            String orderStatus = req.getParameter("orderStatus");
            String paymentStatus = req.getParameter("paymentStatus");

            if (orderStatus != null && !orderStatus.trim().isEmpty()) {
                orderService.updateOrderStatus(orderId, orderStatus.trim());
            }

            if (paymentStatus != null && !paymentStatus.trim().isEmpty()) {
                orderService.updatePaymentStatus(orderId, paymentStatus.trim());
            }

            if (session != null) {
                session.setAttribute("notice", "Order #" + orderId + " updated to " + orderStatus + " / " + paymentStatus);
            }

            res.sendRedirect(req.getContextPath() + "/admin/orders");

        } catch (NumberFormatException e) {
            res.sendRedirect(req.getContextPath() + "/admin/orders");
        } catch (SQLException e) {
            throw new ServletException("Database error updating order status", e);
        }
    }
}
