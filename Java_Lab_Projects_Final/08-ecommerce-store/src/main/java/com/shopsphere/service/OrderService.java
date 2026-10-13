package com.shopsphere.service;

import com.shopsphere.dao.CartDAO;
import com.shopsphere.dao.CouponDAO;
import com.shopsphere.dao.OrderDAO;
import com.shopsphere.model.CartItem;
import com.shopsphere.model.Coupon;
import com.shopsphere.model.Order;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Service orchestrating order placement, coupon validation, and transactional persistence.
 */
public class OrderService {

    private final OrderDAO orderDAO;
    private final CartDAO cartDAO;
    private final CouponDAO couponDAO;
    private final CartService cartService;

    public OrderService() {
        this.orderDAO = new OrderDAO();
        this.cartDAO = new CartDAO();
        this.couponDAO = new CouponDAO();
        this.cartService = new CartService();
    }

    public OrderService(OrderDAO orderDAO, CartDAO cartDAO, CouponDAO couponDAO, CartService cartService) {
        this.orderDAO = orderDAO;
        this.cartDAO = cartDAO;
        this.couponDAO = couponDAO;
        this.cartService = cartService;
    }

    /**
     * Executes atomic, transactional order placement.
     * Computes all order financials strictly on the server side.
     */
    public int placeOrder(int userId, Integer addressId, String paymentMethod, String couponCode) throws SQLException {
        List<CartItem> cartItems = cartDAO.getCartByUser(userId);
        if (cartItems == null || cartItems.isEmpty()) {
            throw new IllegalStateException("Your shopping cart is empty.");
        }

        // Validate and apply coupon if provided
        Coupon appliedCoupon = null;
        Integer couponId = null;

        if (couponCode != null && !couponCode.trim().isEmpty()) {
            Coupon c = couponDAO.findByCode(couponCode.trim());
            if (c == null || !c.isStatus()) {
                throw new IllegalArgumentException("The coupon code entered is invalid or inactive.");
            }
            if (couponDAO.hasUserUsedCoupon(c.getCouponId(), userId)) {
                throw new IllegalArgumentException("You have already redeemed this coupon code previously.");
            }
            appliedCoupon = c;
            couponId = c.getCouponId();
        }

        // Compute authoritative totals on the server
        CartService.CartTotals totals = cartService.calculateTotals(cartItems, appliedCoupon);

        Order order = new Order();
        order.setUserId(userId);
        order.setAddressId(addressId);
        order.setTotalAmount(totals.getTotal());
        order.setDiscount(totals.getDiscount());
        order.setPaymentMethod(paymentMethod != null ? paymentMethod.trim().toUpperCase() : "COD");
        order.setPaymentStatus("COD".equalsIgnoreCase(paymentMethod) ? "PENDING" : "PAID");
        order.setOrderStatus("PLACED");

        // Transactional commit through OrderDAO
        int orderId = orderDAO.createOrder(order, cartItems, couponId);

        // Clear cart after successful transaction
        cartDAO.clearCart(userId);

        return orderId;
    }

    public Order getOrderById(int orderId) throws SQLException {
        return orderDAO.findById(orderId);
    }

    public List<Order> getOrdersByUser(int userId) throws SQLException {
        return orderDAO.findByUserId(userId);
    }

    public List<Order> getAllOrders() throws SQLException {
        return orderDAO.findAllOrders();
    }

    public boolean updateOrderStatus(int orderId, String status) throws SQLException {
        return orderDAO.updateOrderStatus(orderId, status);
    }

    public boolean updatePaymentStatus(int orderId, String status) throws SQLException {
        return orderDAO.updatePaymentStatus(orderId, status);
    }

    public Map<String, Object> getDashboardMetrics() throws SQLException {
        return orderDAO.getSalesMetrics();
    }
}
