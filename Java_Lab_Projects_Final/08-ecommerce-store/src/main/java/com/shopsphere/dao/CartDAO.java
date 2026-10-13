package com.shopsphere.dao;

import com.shopsphere.model.CartItem;
import com.shopsphere.model.Product;
import com.shopsphere.util.DB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for persistent database-backed cart operations.
 */
public class CartDAO {

    public List<CartItem> getCartByUser(int userId) throws SQLException {
        List<CartItem> list = new ArrayList<>();
        String sql = """
            SELECT c.cart_id, c.user_id, c.product_id, c.quantity,
                   p.name, p.brand, p.description, p.price, p.discount, p.stock, p.image_url, p.status,
                   cat.name AS category_name
            FROM cart c
            JOIN products p ON c.product_id = p.product_id
            LEFT JOIN categories cat ON p.category_id = cat.category_id
            WHERE c.user_id = ?
            ORDER BY c.cart_id ASC
            """;

        try (Connection con = DB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Product p = new Product();
                    p.setProductId(rs.getInt("product_id"));
                    p.setName(rs.getString("name"));
                    p.setBrand(rs.getString("brand"));
                    p.setDescription(rs.getString("description"));
                    p.setPrice(rs.getDouble("price"));
                    p.setDiscount(rs.getDouble("discount"));
                    p.setStock(rs.getInt("stock"));
                    p.setImageUrl(rs.getString("image_url"));
                    p.setStatus(rs.getBoolean("status"));
                    p.setCategoryName(rs.getString("category_name"));

                    CartItem item = new CartItem();
                    item.setCartId(rs.getInt("cart_id"));
                    item.setUserId(rs.getInt("user_id"));
                    item.setProductId(p.getProductId());
                    item.setProduct(p);
                    item.setQuantity(rs.getInt("quantity"));

                    list.add(item);
                }
            }
        }
        return list;
    }

    public boolean addToCart(int userId, int productId, int quantity) throws SQLException {
        String sql = """
            INSERT INTO cart (user_id, product_id, quantity)
            VALUES (?, ?, ?)
            ON DUPLICATE KEY UPDATE quantity = quantity + VALUES(quantity)
            """;
        try (Connection con = DB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setInt(2, productId);
            ps.setInt(3, quantity > 0 ? quantity : 1);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean updateQuantity(int userId, int productId, int quantity) throws SQLException {
        if (quantity <= 0) {
            return removeFromCart(userId, productId);
        }
        String sql = "UPDATE cart SET quantity = ? WHERE user_id = ? AND product_id = ?";
        try (Connection con = DB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, quantity);
            ps.setInt(2, userId);
            ps.setInt(3, productId);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean removeFromCart(int userId, int productId) throws SQLException {
        String sql = "DELETE FROM cart WHERE user_id = ? AND product_id = ?";
        try (Connection con = DB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setInt(2, productId);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean clearCart(int userId) throws SQLException {
        String sql = "DELETE FROM cart WHERE user_id = ?";
        try (Connection con = DB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            return ps.executeUpdate() > 0;
        }
    }

    public int getCartItemCount(int userId) throws SQLException {
        String sql = "SELECT COALESCE(SUM(quantity), 0) FROM cart WHERE user_id = ?";
        try (Connection con = DB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return 0;
    }
}
