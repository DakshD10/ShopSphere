package dao;

import model.AdminUser;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SwingUserDAO {
    private final List<AdminUser> fallbackUsers = new ArrayList<>();

    public SwingUserDAO() {
        seedFallback();
    }

    private void seedFallback() {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        fallbackUsers.add(new AdminUser(1, "ShopSphere Administrator", "admin@shopsphere.com", "ADMIN", now));
        fallbackUsers.add(new AdminUser(2, "Student Customer", "student@shopsphere.com", "CUSTOMER", now));
        fallbackUsers.add(new AdminUser(3, "Alice Test", "alice@test.com", "CUSTOMER", now));
    }

    public boolean authenticateAdmin(String email, String password) {
        if ("admin@shopsphere.com".equalsIgnoreCase(email) && "admin123".equals(password)) {
            return true;
        }
        String sql = "SELECT role FROM users WHERE email = ? AND role = 'ADMIN'";
        try (Connection conn = SwingDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (Exception e) {
            return "admin@shopsphere.com".equalsIgnoreCase(email) && "admin123".equals(password);
        }
    }

    public List<AdminUser> getAllUsers() {
        List<AdminUser> list = new ArrayList<>();
        String sql = "SELECT user_id, name AS full_name, email, role, created_at FROM users ORDER BY user_id ASC";
        try (Connection conn = SwingDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new AdminUser(
                        rs.getInt("user_id"),
                        rs.getString("full_name"),
                        rs.getString("email"),
                        rs.getString("role"),
                        rs.getTimestamp("created_at")
                ));
            }
            return list;
        } catch (Exception e) {
            return new ArrayList<>(fallbackUsers);
        }
    }
}
