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

/**
 * Controller displaying the order placement confirmation receipt.
 */
@WebServlet(name = "OrderSuccessServlet", urlPatterns = {"/order-success"})
public class OrderSuccessServlet extends HttpServlet {

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
            res.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String orderIdStr = req.getParameter("orderId");
        if (orderIdStr == null || orderIdStr.trim().isEmpty()) {
            res.sendRedirect(req.getContextPath() + "/orders");
            return;
        }

        try {
            int orderId = Integer.parseInt(orderIdStr.trim());
            Order order = orderService.getOrderById(orderId);

            if (order == null || (order.getUserId() != user.getUserId() && !user.isAdmin())) {
                res.sendRedirect(req.getContextPath() + "/orders");
                return;
            }

            req.setAttribute("order", order);
            req.getRequestDispatcher("/order-success.jsp").forward(req, res);

        } catch (NumberFormatException | SQLException e) {
            res.sendRedirect(req.getContextPath() + "/orders");
        }
    }
}
