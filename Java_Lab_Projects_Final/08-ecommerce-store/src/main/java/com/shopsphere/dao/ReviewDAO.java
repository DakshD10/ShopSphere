package com.shopsphere.dao;

import com.shopsphere.model.Review;
import com.shopsphere.util.DB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for customer product reviews and star ratings.
 */
public class ReviewDAO {

    public List<Review> findByProductId(int productId) throws SQLException {
        List<Review> list = new ArrayList<>();
        String sql = """
            SELECT r.review_id, r.user_id, u.name AS user_name, r.product_id, 
                   r.rating, r.review_text, r.created_at
            FROM reviews r
            JOIN users u ON r.user_id = u.user_id
            WHERE r.product_id = ?
            ORDER BY r.review_id DESC
            """;

        try (Connection con = DB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, productId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Review r = new Review();
                    r.setReviewId(rs.getInt("review_id"));
                    r.setUserId(rs.getInt("user_id"));
                    r.setUserName(rs.getString("user_name"));
                    r.setProductId(rs.getInt("product_id"));
                    r.setRating(rs.getInt("rating"));
                    r.setReviewText(rs.getString("review_text"));
                    r.setCreatedAt(rs.getTimestamp("created_at"));
                    list.add(r);
                }
            }
        }
        return list;
    }

    public int addReview(Review r) throws SQLException {
        String sql = "INSERT INTO reviews (user_id, product_id, rating, review_text) VALUES (?, ?, ?, ?)";
        try (Connection con = DB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, r.getUserId());
            ps.setInt(2, r.getProductId());
            ps.setInt(3, r.getRating());
            ps.setString(4, r.getReviewText());

            int rows = ps.executeUpdate();
            if (rows > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        int id = rs.getInt(1);
                        r.setReviewId(id);
                        return id;
                    }
                }
            }
            return -1;
        }
    }

    public double getAverageRating(int productId) throws SQLException {
        String sql = "SELECT COALESCE(AVG(rating), 0.0) FROM reviews WHERE product_id = ?";
        try (Connection con = DB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, productId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Math.round(rs.getDouble(1) * 10.0) / 10.0;
                }
            }
        }
        return 0.0;
    }
}
