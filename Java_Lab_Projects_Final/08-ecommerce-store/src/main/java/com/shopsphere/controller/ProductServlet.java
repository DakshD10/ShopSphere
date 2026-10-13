package com.shopsphere.controller;

import com.shopsphere.model.Category;
import com.shopsphere.model.Product;
import com.shopsphere.model.User;
import com.shopsphere.service.CartService;
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
 * Controller for product catalog, search, category filtering, and sorting.
 * Maps to root ("/") and "/products".
 */
@WebServlet(name = "ProductServlet", urlPatterns = {"/products", "/index"})
public class ProductServlet extends HttpServlet {

    private ProductService productService;
    private CategoryService categoryService;
    private CartService cartService;

    @Override
    public void init() throws ServletException {
        this.productService = new ProductService();
        this.categoryService = new CategoryService();
        this.cartService = new CartService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        try {
            // Read query parameters
            String catParam = req.getParameter("category");
            Integer categoryId = null;
            if (catParam != null && !catParam.trim().isEmpty()) {
                try {
                    categoryId = Integer.parseInt(catParam.trim());
                } catch (NumberFormatException ignored) {}
            }

            String search = req.getParameter("search");
            String sort = req.getParameter("sort");

            Double minPrice = null;
            String minPriceParam = req.getParameter("minPrice");
            if (minPriceParam != null && !minPriceParam.trim().isEmpty()) {
                try {
                    minPrice = Double.parseDouble(minPriceParam.trim());
                } catch (NumberFormatException ignored) {}
            }

            Double maxPrice = null;
            String maxPriceParam = req.getParameter("maxPrice");
            if (maxPriceParam != null && !maxPriceParam.trim().isEmpty()) {
                try {
                    maxPrice = Double.parseDouble(maxPriceParam.trim());
                } catch (NumberFormatException ignored) {}
            }

            // Retrieve filtered and sorted products
            List<Product> products = productService.filterAndSort(categoryId, search, minPrice, maxPrice, sort);
            List<Category> categories = categoryService.getActiveCategories();

            // Set cart badge counter if user is logged in
            HttpSession session = req.getSession(false);
            if (session != null && session.getAttribute("user") != null) {
                User user = (User) session.getAttribute("user");
                int cartCount = cartService.getCartCount(user.getUserId());
                session.setAttribute("cartCount", cartCount);
            }

            // Pass attributes to view
            req.setAttribute("products", products);
            req.setAttribute("categories", categories);
            req.setAttribute("selectedCategory", categoryId);
            req.setAttribute("searchQuery", search);
            req.setAttribute("sortBy", sort);
            req.setAttribute("minPrice", minPrice);
            req.setAttribute("maxPrice", maxPrice);

            req.getRequestDispatcher("/products.jsp").forward(req, res);

        } catch (SQLException e) {
            throw new ServletException("Unable to load product catalog. Check database connection.", e);
        }
    }
}
