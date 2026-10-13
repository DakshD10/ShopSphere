package com.shopsphere.dao;

import com.shopsphere.model.Coupon;
import com.shopsphere.util.DB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for coupons and coupon redemption usage.
 */
public class CouponDAO {

    public Coupon findByCode(String code) throws SQLException {
        if (code == null) return null;
        String sql = """
            SELECT coupon_id, code, discount_type, discount_value, minimum_order, 
                   maximum_discount, expiry_date, status
            FROM coupons
            WHERE UPPER(code) = ?
            """;
        try (Connection con = DB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, code.trim().toUpperCase());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        }
        return null;
    }

    public List<Coupon> findAll() throws SQLException {
        List<Coupon> list = new ArrayList<>();
        String sql = """
            SELECT coupon_id, code, discount_type, discount_value, minimum_order, 
                   maximum_discount, expiry_date, status
            FROM coupons
            ORDER BY coupon_id DESC
            """;
        try (Connection con = DB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(mapRow(rs));
            }
        }
        return list;
    }

    public int createCoupon(Coupon c) throws SQLException {
        String sql = """
            INSERT INTO coupons (code, discount_type, discount_value, minimum_order, maximum_discount, expiry_date, status)
            VALUES (?, ?, ?, ?, ?, ?, ?)
            """;
        try (Connection con = DB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, c.getCode().trim().toUpperCase());
            ps.setString(2, c.getDiscountType());
            ps.setDouble(3, c.getDiscountValue());
            ps.setDouble(4, c.getMinimumOrder());
            ps.setDouble(5, c.getMaximumDiscount());
            ps.setDate(6, c.getExpiryDate());
            ps.setBoolean(7, c.isStatus());

            int rows = ps.executeUpdate();
            if (rows > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        int id = rs.getInt(1);
                        c.setCouponId(id);
                        return id;
                    }
                }
            }
            return -1;
        }
    }

    public boolean recordUsage(Connection con, int couponId, int userId, int orderId) throws SQLException {
        String sql = "INSERT INTO coupon_usage (coupon_id, user_id, order_id) VALUES (?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, couponId);
            ps.setInt(2, userId);
            ps.setInt(3, orderId);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean hasUserUsedCoupon(int couponId, int userId) throws SQLException {
        String sql = "SELECT 1 FROM coupon_usage WHERE coupon_id = ? AND user_id = ? LIMIT 1";
        try (Connection con = DB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, couponId);
            ps.setInt(2, userId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    private Coupon mapRow(ResultSet rs) throws SQLException {
        return new Coupon(
                rs.getInt("coupon_id"),
                rs.getString("code"),
                rs.getString("discount_type"),
                rs.getDouble("discount_value"),
                rs.getDouble("minimum_order"),
                rs.getDouble("maximum_discount"),
                rs.getDate("expiry_date"),
                rs.getBoolean("status")
        );
    }
}
