package com.shopsphere.controller;

import com.shopsphere.dao.WishlistDAO;
import com.shopsphere.model.User;
import com.shopsphere.model.WishlistItem;
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
 * Controller managing customer saved favorites and wishlist items.
 */
@WebServlet(name = "WishlistServlet", urlPatterns = {"/wishlist"})
public class WishlistServlet extends HttpServlet {

    private WishlistDAO wishlistDAO;

    @Override
    public void init() throws ServletException {
        this.wishlistDAO = new WishlistDAO();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;
        if (user == null) {
            res.sendRedirect(req.getContextPath() + "/login?error=auth_required");
            return;
        }

        try {
            List<WishlistItem> wishlistItems = wishlistDAO.getWishlistByUser(user.getUserId());
            req.setAttribute("wishlistItems", wishlistItems);
            req.getRequestDispatcher("/wishlist.jsp").forward(req, res);
        } catch (SQLException e) {
            throw new ServletException("Database error loading wishlist", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;
        if (user == null) {
            res.sendRedirect(req.getContextPath() + "/login?error=auth_required");
            return;
        }

        String action = req.getParameter("action");
        String prodStr = req.getParameter("productId");

        if (prodStr != null && !prodStr.trim().isEmpty()) {
            try {
                int productId = Integer.parseInt(prodStr.trim());
                if ("remove".equalsIgnoreCase(action)) {
                    wishlistDAO.removeFromWishlist(user.getUserId(), productId);
                } else {
                    wishlistDAO.addToWishlist(user.getUserId(), productId);
                }
            } catch (NumberFormatException | SQLException ignored) {}
        }

        res.sendRedirect(req.getContextPath() + "/wishlist");
    }
}
