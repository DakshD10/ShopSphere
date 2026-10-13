package com.shopsphere.dao;

import com.shopsphere.model.Product;
import com.shopsphere.model.WishlistItem;
import com.shopsphere.util.DB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for customer wishlist.
 */
public class WishlistDAO {

    public List<WishlistItem> getWishlistByUser(int userId) throws SQLException {
        List<WishlistItem> list = new ArrayList<>();
        String sql = """
            SELECT w.wishlist_id, w.user_id, w.product_id, w.created_at,
                   p.name, p.brand, p.description, p.price, p.discount, p.stock, p.image_url, p.status,
                   cat.name AS category_name
            FROM wishlist w
            JOIN products p ON w.product_id = p.product_id
            LEFT JOIN categories cat ON p.category_id = cat.category_id
            WHERE w.user_id = ?
            ORDER BY w.wishlist_id DESC
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

                    WishlistItem item = new WishlistItem();
                    item.setWishlistId(rs.getInt("wishlist_id"));
                    item.setUserId(rs.getInt("user_id"));
                    item.setProductId(p.getProductId());
                    item.setProduct(p);
                    item.setCreatedAt(rs.getTimestamp("created_at"));

                    list.add(item);
                }
            }
        }
        return list;
    }

    public boolean addToWishlist(int userId, int productId) throws SQLException {
        String sql = "INSERT IGNORE INTO wishlist (user_id, product_id) VALUES (?, ?)";
        try (Connection con = DB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setInt(2, productId);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean removeFromWishlist(int userId, int productId) throws SQLException {
        String sql = "DELETE FROM wishlist WHERE user_id = ? AND product_id = ?";
        try (Connection con = DB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setInt(2, productId);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean isInWishlist(int userId, int productId) throws SQLException {
        String sql = "SELECT 1 FROM wishlist WHERE user_id = ? AND product_id = ? LIMIT 1";
        try (Connection con = DB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setInt(2, productId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }
}
