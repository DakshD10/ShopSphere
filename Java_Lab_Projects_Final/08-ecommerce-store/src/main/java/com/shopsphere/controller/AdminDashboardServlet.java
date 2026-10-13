package com.shopsphere.controller;

import com.shopsphere.model.Order;
import com.shopsphere.model.Product;
import com.shopsphere.model.User;
import com.shopsphere.service.OrderService;
import com.shopsphere.service.ProductService;
import com.shopsphere.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Controller providing administrative sales metrics, recent orders, and inventory alerts.
 */
@WebServlet(name = "AdminDashboardServlet", urlPatterns = {"/admin/dashboard"})
public class AdminDashboardServlet extends HttpServlet {

    private OrderService orderService;
    private ProductService productService;
    private UserService userService;

    @Override
    public void init() throws ServletException {
        this.orderService = new OrderService();
        this.productService = new ProductService();
        this.userService = new UserService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        try {
            Map<String, Object> metrics = orderService.getDashboardMetrics();
            List<Product> lowStock = productService.getLowStockProducts(10);
            List<Order> allOrders = orderService.getAllOrders();
            List<Order> recentOrders = allOrders.size() > 5 ? allOrders.subList(0, 5) : allOrders;
            List<User> users = userService.getAllUsers();

            req.setAttribute("metrics", metrics);
            req.setAttribute("lowStockProducts", lowStock);
            req.setAttribute("recentOrders", recentOrders);
            req.setAttribute("totalUsers", users.size());

            req.getRequestDispatcher("/admin/dashboard.jsp").forward(req, res);

        } catch (SQLException e) {
            throw new ServletException("Database error generating administrator dashboard", e);
        }
    }
}
