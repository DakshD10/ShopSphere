package com.shopsphere.dao;

import com.shopsphere.model.User;
import com.shopsphere.util.DB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for users table.
 */
public class UserDAO {

    /**
     * Inserts a new user record.
     *
     * @param user User to register
     * @return generated user_id, or -1 on failure
     * @throws SQLException on database error
     */
    public int createUser(User user) throws SQLException {
        String sql = "INSERT INTO users (name, email, password, mobile, role, status) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection con = DB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, user.getName());
            ps.setString(2, user.getEmail().trim().toLowerCase());
            ps.setString(3, user.getPassword());
            ps.setString(4, user.getMobile());
            ps.setString(5, user.getRole() != null ? user.getRole() : "CUSTOMER");
            ps.setString(6, user.getStatus() != null ? user.getStatus() : "ACTIVE");

            int rows = ps.executeUpdate();
            if (rows > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        int id = rs.getInt(1);
                        user.setUserId(id);
                        return id;
                    }
                }
            }
            return -1;
        }
    }

    public User findByEmail(String email) throws SQLException {
        if (email == null) return null;
        String sql = "SELECT user_id, name, email, password, mobile, role, status, created_at FROM users WHERE LOWER(email) = ?";
        try (Connection con = DB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, email.trim().toLowerCase());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        }
        return null;
    }

    public User findById(int userId) throws SQLException {
        String sql = "SELECT user_id, name, email, password, mobile, role, status, created_at FROM users WHERE user_id = ?";
        try (Connection con = DB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        }
        return null;
    }

    public boolean emailExists(String email) throws SQLException {
        String sql = "SELECT 1 FROM users WHERE LOWER(email) = ? LIMIT 1";
        try (Connection con = DB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, email.trim().toLowerCase());
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    public List<User> findAll() throws SQLException {
        List<User> list = new ArrayList<>();
        String sql = "SELECT user_id, name, email, password, mobile, role, status, created_at FROM users ORDER BY user_id DESC";
        try (Connection con = DB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(mapRow(rs));
            }
        }
        return list;
    }

    public boolean updateStatus(int userId, String status) throws SQLException {
        String sql = "UPDATE users SET status = ? WHERE user_id = ?";
        try (Connection con = DB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, status);
            ps.setInt(2, userId);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean updateProfile(int userId, String name, String mobile) throws SQLException {
        String sql = "UPDATE users SET name = ?, mobile = ? WHERE user_id = ?";
        try (Connection con = DB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, name);
            ps.setString(2, mobile);
            ps.setInt(3, userId);
            return ps.executeUpdate() > 0;
        }
    }

    private User mapRow(ResultSet rs) throws SQLException {
        User u = new User();
        u.setUserId(rs.getInt("user_id"));
        u.setName(rs.getString("name"));
        u.setEmail(rs.getString("email"));
        u.setPassword(rs.getString("password"));
        u.setMobile(rs.getString("mobile"));
        u.setRole(rs.getString("role"));
        u.setStatus(rs.getString("status"));
        u.setCreatedAt(rs.getTimestamp("created_at"));
        return u;
    }
}
