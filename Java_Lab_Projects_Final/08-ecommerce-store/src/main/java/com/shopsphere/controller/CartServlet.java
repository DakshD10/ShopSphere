package com.shopsphere.controller;

import com.shopsphere.dao.CouponDAO;
import com.shopsphere.model.CartItem;
import com.shopsphere.model.Coupon;
import com.shopsphere.model.User;
import com.shopsphere.service.CartService;
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
 * Controller managing customer shopping cart operations:
 * view, add, update quantity, remove item, apply coupon.
 */
@WebServlet(name = "CartServlet", urlPatterns = {"/cart"})
public class CartServlet extends HttpServlet {

    private CartService cartService;
    private CouponDAO couponDAO;

    @Override
    public void init() throws ServletException {
        this.cartService = new CartService();
        this.couponDAO = new CouponDAO();
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
            List<CartItem> cartItems = cartService.getCart(user.getUserId());
            Coupon coupon = (session != null) ? (Coupon) session.getAttribute("appliedCoupon") : null;

            // Authoritative server-side financial calculation
            CartService.CartTotals totals = cartService.calculateTotals(cartItems, coupon);

            // Update session counter
            session.setAttribute("cartCount", totals.getItemCount());

            req.setAttribute("cartItems", cartItems);
            req.setAttribute("totals", totals);
            req.setAttribute("appliedCoupon", coupon);

            req.getRequestDispatcher("/cart.jsp").forward(req, res);

        } catch (SQLException e) {
            throw new ServletException("Database error loading shopping cart", e);
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
        if (action == null) {
            action = "add";
        }

        try {
            switch (action) {
                case "add" -> {
                    int productId = Integer.parseInt(req.getParameter("productId"));
                    int quantity = 1;
                    String qtyStr = req.getParameter("quantity");
                    if (qtyStr != null && !qtyStr.trim().isEmpty()) {
                        quantity = Integer.parseInt(qtyStr.trim());
                    }
                    cartService.addToCart(user.getUserId(), productId, quantity);
                    session.setAttribute("notice", "Item added to your cart!");
                }
                case "update" -> {
                    int productId = Integer.parseInt(req.getParameter("productId"));
                    int quantity = Integer.parseInt(req.getParameter("quantity"));
                    cartService.updateQuantity(user.getUserId(), productId, quantity);
                }
                case "remove" -> {
                    int productId = Integer.parseInt(req.getParameter("productId"));
                    cartService.removeFromCart(user.getUserId(), productId);
                    session.setAttribute("notice", "Item removed from cart.");
                }
                case "applyCoupon" -> {
                    String code = req.getParameter("couponCode");
                    if (code != null && !code.trim().isEmpty()) {
                        Coupon c = couponDAO.findByCode(code.trim());
                        if (c != null && c.isStatus()) {
                            if (couponDAO.hasUserUsedCoupon(c.getCouponId(), user.getUserId())) {
                                session.setAttribute("couponError", "You have already redeemed this coupon.");
                            } else {
                                session.setAttribute("appliedCoupon", c);
                                session.setAttribute("couponNotice", "Coupon " + c.getCode() + " applied successfully!");
                            }
                        } else {
                            session.setAttribute("couponError", "Invalid or expired coupon code.");
                        }
                    }
                }
                case "removeCoupon" -> {
                    session.removeAttribute("appliedCoupon");
                    session.setAttribute("couponNotice", "Coupon removed.");
                }
                default -> {}
            }

            res.sendRedirect(req.getContextPath() + "/cart");

        } catch (NumberFormatException e) {
            res.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid numeric input parameter.");
        } catch (IllegalArgumentException e) {
            session.setAttribute("cartError", e.getMessage());
            res.sendRedirect(req.getContextPath() + "/cart");
        } catch (SQLException e) {
            throw new ServletException("Database error processing cart action", e);
        }
    }
}
