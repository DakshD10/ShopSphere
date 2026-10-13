package com.shopsphere.controller;

import com.shopsphere.model.Category;
import com.shopsphere.model.Product;
import com.shopsphere.service.CategoryService;
import com.shopsphere.service.ProductService;
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
 * Controller providing Administrator Product CRUD and inventory management.
 */
@WebServlet(name = "AdminProductServlet", urlPatterns = {"/admin/products"})
public class AdminProductServlet extends HttpServlet {

    private ProductService productService;
    private CategoryService categoryService;

    @Override
    public void init() throws ServletException {
        this.productService = new ProductService();
        this.categoryService = new CategoryService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        try {
            List<Product> products = productService.getAllProducts();
            List<Category> categories = categoryService.getAllCategories();

            String editIdStr = req.getParameter("editId");
            if (editIdStr != null && !editIdStr.trim().isEmpty()) {
                try {
                    int editId = Integer.parseInt(editIdStr.trim());
                    Product editProduct = productService.getProductById(editId);
                    req.setAttribute("editProduct", editProduct);
                } catch (NumberFormatException ignored) {}
            }

            req.setAttribute("products", products);
            req.setAttribute("categories", categories);

            req.getRequestDispatcher("/admin/products.jsp").forward(req, res);

        } catch (SQLException e) {
            throw new ServletException("Database error loading product manager", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        HttpSession session = req.getSession(false);

        String action = req.getParameter("action");
        if (action == null) {
            action = "save";
        }

        try {
            if ("delete".equalsIgnoreCase(action)) {
                int productId = Integer.parseInt(req.getParameter("productId"));
                productService.deleteProduct(productId);
                if (session != null) session.setAttribute("notice", "Product ID #" + productId + " deleted successfully.");
            } else if ("save".equalsIgnoreCase(action)) {
                int productId = 0;
                String prodIdStr = req.getParameter("productId");
                if (prodIdStr != null && !prodIdStr.trim().isEmpty()) {
                    productId = Integer.parseInt(prodIdStr.trim());
                }

                int categoryId = Integer.parseInt(req.getParameter("categoryId"));
                String name = req.getParameter("name");
                String brand = req.getParameter("brand");
                String description = req.getParameter("description");
                double price = Double.parseDouble(req.getParameter("price"));
                double discount = 0.0;
                String discStr = req.getParameter("discount");
                if (discStr != null && !discStr.trim().isEmpty()) {
                    discount = Double.parseDouble(discStr.trim());
                }
                int stock = Integer.parseInt(req.getParameter("stock"));
                String imageUrl = req.getParameter("imageUrl");
                boolean status = "on".equalsIgnoreCase(req.getParameter("status")) || "true".equalsIgnoreCase(req.getParameter("status"));

                Product p = new Product(productId, categoryId, name, brand, description, price, discount, stock, imageUrl, status);
                productService.saveProduct(p);

                if (session != null) {
                    session.setAttribute("notice", (productId > 0 ? "Product updated: " : "New product created: ") + name);
                }
            }

            res.sendRedirect(req.getContextPath() + "/admin/products");

        } catch (IllegalArgumentException e) {
            if (session != null) session.setAttribute("error", e.getMessage());
            res.sendRedirect(req.getContextPath() + "/admin/products");
        } catch (SQLException e) {
            throw new ServletException("Database error saving product", e);
        }
    }
}
