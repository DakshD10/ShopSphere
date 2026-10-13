package com.shopsphere.model;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Represents a saved favorite item for a customer.
 */
public class WishlistItem implements Serializable {
    private static final long serialVersionUID = 1L;

    private int wishlistId;
    private int userId;
    private int productId;
    private Product product;
    private Timestamp createdAt;

    public WishlistItem() {}

    public WishlistItem(int wishlistId, int userId, int productId, Product product) {
        this.wishlistId = wishlistId;
        this.userId = userId;
        this.productId = productId;
        this.product = product;
    }

    public int getWishlistId() {
        return wishlistId;
    }

    public void setWishlistId(int wishlistId) {
        this.wishlistId = wishlistId;
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

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}
