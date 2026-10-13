package com.shopsphere.controller;

import com.shopsphere.dao.AddressDAO;
import com.shopsphere.model.Address;
import com.shopsphere.model.CartItem;
import com.shopsphere.model.Coupon;
import com.shopsphere.model.User;
import com.shopsphere.service.CartService;
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
 * Controller managing the checkout workflow:
 * shipping address selection, server-side totals, coupon validation, mock payment, and order creation.
 */
@WebServlet(name = "CheckoutServlet", urlPatterns = {"/checkout"})
public class CheckoutServlet extends HttpServlet {

    private CartService cartService;
    private OrderService orderService;
    private AddressDAO addressDAO;

    @Override
    public void init() throws ServletException {
        this.cartService = new CartService();
        this.orderService = new OrderService();
        this.addressDAO = new AddressDAO();
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
            if (cartItems == null || cartItems.isEmpty()) {
                res.sendRedirect(req.getContextPath() + "/cart?error=empty_cart");
                return;
            }

            Coupon coupon = (Coupon) session.getAttribute("appliedCoupon");
            CartService.CartTotals totals = cartService.calculateTotals(cartItems, coupon);
            List<Address> addresses = addressDAO.findByUserId(user.getUserId());

            req.setAttribute("cartItems", cartItems);
            req.setAttribute("totals", totals);
            req.setAttribute("addresses", addresses);
            req.setAttribute("appliedCoupon", coupon);

            req.getRequestDispatcher("/checkout.jsp").forward(req, res);

        } catch (SQLException e) {
            throw new ServletException("Database error preparing checkout", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        HttpSession session = req.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;
        if (user == null) {
            res.sendRedirect(req.getContextPath() + "/login?error=auth_required");
            return;
        }

        try {
            Integer selectedAddressId = null;
            String addressOption = req.getParameter("addressOption"); // addressId string or "new"

            if ("new".equalsIgnoreCase(addressOption)) {
                String line = req.getParameter("newAddressLine");
                String city = req.getParameter("newCity");
                String state = req.getParameter("newState");
                String pincode = req.getParameter("newPincode");
                String type = req.getParameter("newAddressType");

                if (line == null || line.trim().isEmpty() || city == null || city.trim().isEmpty() ||
                        state == null || state.trim().isEmpty() || pincode == null || pincode.trim().isEmpty()) {
                    throw new IllegalArgumentException("All address fields are required for shipping delivery.");
                }

                Address newAddr = new Address(0, user.getUserId(), line.trim(), city.trim(), state.trim(), pincode.trim(), type);
                int generatedAddrId = addressDAO.createAddress(newAddr);
                selectedAddressId = generatedAddrId > 0 ? generatedAddrId : null;
            } else if (addressOption != null && !addressOption.trim().isEmpty() && !"existing".equalsIgnoreCase(addressOption)) {
                try {
                    selectedAddressId = Integer.parseInt(addressOption.trim());
                } catch (NumberFormatException ignored) {}
            }

            if (selectedAddressId == null && !"new".equalsIgnoreCase(addressOption)) {
                String addrStr = req.getParameter("addressId");
                if (addrStr != null && !addrStr.trim().isEmpty()) {
                    try {
                        selectedAddressId = Integer.parseInt(addrStr.trim());
                    } catch (NumberFormatException ignored) {}
                }
            }

            String paymentMethod = req.getParameter("paymentMethod");
            if (paymentMethod == null || paymentMethod.trim().isEmpty()) {
                paymentMethod = "COD";
            }

            Coupon coupon = (Coupon) session.getAttribute("appliedCoupon");
            String couponCode = (coupon != null) ? coupon.getCode() : null;

            // Place order using OrderService (atomic transaction with stock decrement)
            int orderId = orderService.placeOrder(user.getUserId(), selectedAddressId, paymentMethod, couponCode);

            // Cleanup session
            session.removeAttribute("appliedCoupon");
            session.setAttribute("cartCount", 0);

            res.sendRedirect(req.getContextPath() + "/order-success?orderId=" + orderId);

        } catch (IllegalArgumentException | IllegalStateException e) {
            session.setAttribute("checkoutError", e.getMessage());
            res.sendRedirect(req.getContextPath() + "/checkout");
        } catch (SQLException e) {
            throw new ServletException("Order transaction failed: " + e.getMessage(), e);
        }
    }
}
