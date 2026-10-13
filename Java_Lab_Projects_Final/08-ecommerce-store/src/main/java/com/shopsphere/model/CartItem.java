package com.shopsphere.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Represents an item in the customer shopping cart.
 */
public class CartItem implements Serializable {
    private static final long serialVersionUID = 1L;

    private int cartId;
    private int userId;
    private int productId;
    private Product product;
    private int quantity;

    public CartItem() {
        this.quantity = 1;
    }

    public CartItem(int productId, Product product, int quantity) {
        this.productId = productId;
        this.product = product;
        this.quantity = quantity;
    }

    public CartItem(int cartId, int userId, int productId, Product product, int quantity) {
        this.cartId = cartId;
        this.userId = userId;
        this.productId = productId;
        this.product = product;
        this.quantity = quantity;
    }

    public int getCartId() {
        return cartId;
    }

    public void setCartId(int cartId) {
        this.cartId = cartId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getUnitPrice() {
        if (product != null) {
            return product.getDiscountedPrice();
        }
        return 0.0;
    }

    public double getSubtotal() {
        double subtotal = getUnitPrice() * quantity;
        return BigDecimal.valueOf(subtotal).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }
}
