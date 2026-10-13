package com.shopsphere.controller;

import com.shopsphere.dao.ReviewDAO;
import com.shopsphere.model.Product;
import com.shopsphere.model.Review;
import com.shopsphere.service.ProductService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Controller displaying detailed product information, customer reviews, and ratings.
 * Also manages the 'ss_recent_views' cookie for convenience product history.
 */
@WebServlet(name = "ProductDetailsServlet", urlPatterns = {"/product-details"})
public class ProductDetailsServlet extends HttpServlet {

    private static final String COOKIE_RECENT_VIEWS = "ss_recent_views";
    private ProductService productService;
    private ReviewDAO reviewDAO;

    @Override
    public void init() throws ServletException {
        this.productService = new ProductService();
        this.reviewDAO = new ReviewDAO();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String idParam = req.getParameter("id");
        if (idParam == null || idParam.trim().isEmpty()) {
            res.sendRedirect(req.getContextPath() + "/products");
            return;
        }

        try {
            int productId = Integer.parseInt(idParam.trim());
            Product product = productService.getProductById(productId);

            if (product == null) {
                res.sendError(HttpServletResponse.SC_NOT_FOUND, "Product not found.");
                return;
            }

            List<Review> reviews = reviewDAO.findByProductId(productId);
            double avgRating = reviewDAO.getAverageRating(productId);

            // Handle Recently Viewed Products Cookie (Section 15)
            List<Product> recentProducts = updateAndGetRecentViews(req, res, productId);

            req.setAttribute("product", product);
            req.setAttribute("reviews", reviews);
            req.setAttribute("avgRating", avgRating);
            req.setAttribute("recentProducts", recentProducts);

            req.getRequestDispatcher("/product-details.jsp").forward(req, res);

        } catch (NumberFormatException e) {
            res.sendRedirect(req.getContextPath() + "/products");
        } catch (SQLException e) {
            throw new ServletException("Database error loading product details", e);
        }
    }

    private List<Product> updateAndGetRecentViews(HttpServletRequest req, HttpServletResponse res, int currentProductId) {
        String rawCookieVal = "";
        Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            for (Cookie c : cookies) {
                if (COOKIE_RECENT_VIEWS.equals(c.getName())) {
                    rawCookieVal = c.getValue();
                    break;
                }
            }
        }

        Set<String> idSet = new LinkedHashSet<>();
        // Current product is added first
        idSet.add(String.valueOf(currentProductId));

        if (!rawCookieVal.isEmpty()) {
            String[] existingIds = rawCookieVal.split("-");
            for (String eid : existingIds) {
                if (!eid.trim().isEmpty()) {
                    idSet.add(eid.trim());
                    if (idSet.size() >= 5) break;
                }
            }
        }

        // Save back cookie
        String newCookieVal = String.join("-", idSet);
        Cookie updatedCookie = new Cookie(COOKIE_RECENT_VIEWS, newCookieVal);
        updatedCookie.setMaxAge(7 * 24 * 3600); // 7 days retention
        String path = req.getContextPath();
        updatedCookie.setPath(path.isEmpty() ? "/" : path);
        res.addCookie(updatedCookie);

        // Load other recently viewed products (excluding current)
        List<Product> recents = new ArrayList<>();
        for (String idStr : idSet) {
            try {
                int pid = Integer.parseInt(idStr);
                if (pid != currentProductId) {
                    Product p = productService.getProductById(pid);
                    if (p != null) {
                        recents.add(p);
                    }
                }
            } catch (Exception ignored) {}
        }
        return recents;
    }
}
