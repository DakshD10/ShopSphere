package com.shopsphere.service;

import com.shopsphere.dao.CartDAO;
import com.shopsphere.dao.ProductDAO;
import com.shopsphere.model.CartItem;
import com.shopsphere.model.Coupon;
import com.shopsphere.model.Product;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.util.List;

/**
 * Service managing shopping cart operations and strictly enforcing server-side total calculations.
 */
public class CartService {

    private final CartDAO cartDAO;
    private final ProductDAO productDAO;

    public CartService() {
        this.cartDAO = new CartDAO();
        this.productDAO = new ProductDAO();
    }

    public CartService(CartDAO cartDAO, ProductDAO productDAO) {
        this.cartDAO = cartDAO;
        this.productDAO = productDAO;
    }

    public List<CartItem> getCart(int userId) throws SQLException {
        return cartDAO.getCartByUser(userId);
    }

    public boolean addToCart(int userId, int productId, int quantity) throws SQLException {
        Product p = productDAO.findById(productId);
        if (p == null || !p.isStatus() || p.getStock() < 1) {
            throw new IllegalArgumentException("Product is unavailable or out of stock.");
        }
        int qtyToAdd = Math.max(1, quantity);
        return cartDAO.addToCart(userId, productId, qtyToAdd);
    }

    public boolean updateQuantity(int userId, int productId, int quantity) throws SQLException {
        if (quantity <= 0) {
            return cartDAO.removeFromCart(userId, productId);
        }
        Product p = productDAO.findById(productId);
        if (p == null) {
            return false;
        }
        if (quantity > p.getStock()) {
            throw new IllegalArgumentException("Only " + p.getStock() + " units available in stock.");
        }
        return cartDAO.updateQuantity(userId, productId, quantity);
    }

    public boolean removeFromCart(int userId, int productId) throws SQLException {
        return cartDAO.removeFromCart(userId, productId);
    }

    public boolean clearCart(int userId) throws SQLException {
        return cartDAO.clearCart(userId);
    }

    public int getCartCount(int userId) throws SQLException {
        return cartDAO.getCartItemCount(userId);
    }

    /**
     * Strongly typed container for server-computed financial totals.
     */
    public static class CartTotals implements Serializable {
        private static final long serialVersionUID = 1L;

        private final double subtotal;
        private final double discount;
        private final double total;
        private final int itemCount;

        public CartTotals(double subtotal, double discount, double total, int itemCount) {
            this.subtotal = subtotal;
            this.discount = discount;
            this.total = total;
            this.itemCount = itemCount;
        }

        public double getSubtotal() { return subtotal; }
        public double getDiscount() { return discount; }
        public double getTotal() { return total; }
        public int getItemCount() { return itemCount; }
    }

    /**
     * Calculates order financials strictly on the server side using BigDecimal.
     */
    public CartTotals calculateTotals(List<CartItem> items, Coupon coupon) {
        if (items == null || items.isEmpty()) {
            return new CartTotals(0.0, 0.0, 0.0, 0);
        }

        BigDecimal subtotalBd = BigDecimal.ZERO;
        int totalQty = 0;

        for (CartItem item : items) {
            subtotalBd = subtotalBd.add(BigDecimal.valueOf(item.getSubtotal()));
            totalQty += item.getQuantity();
        }

        double subtotal = subtotalBd.setScale(2, RoundingMode.HALF_UP).doubleValue();
        double discount = 0.0;

        if (coupon != null && coupon.isValidForOrder(subtotal)) {
            discount = coupon.calculateDiscount(subtotal);
        }

        double finalTotal = Math.max(0.0, subtotal - discount);
        double roundedTotal = BigDecimal.valueOf(finalTotal).setScale(2, RoundingMode.HALF_UP).doubleValue();

        return new CartTotals(subtotal, discount, roundedTotal, totalQty);
    }
}
