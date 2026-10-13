package dao;

import model.AdminOrder;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SwingOrderDAO {
    private final List<AdminOrder> fallbackOrders = new ArrayList<>();

    public SwingOrderDAO() {
        seedFallback();
    }

    private void seedFallback() {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        fallbackOrders.add(new AdminOrder(1, "ORD-20261014-0001", "student@shopsphere.com", 1248.98, "CONFIRMED", now));
        fallbackOrders.add(new AdminOrder(2, "ORD-20261014-0002", "customer2@example.com", 249.98, "PROCESSING", now));
        fallbackOrders.add(new AdminOrder(3, "ORD-20261014-0003", "alice@test.com", 49.99, "DELIVERED", now));
    }

    public List<AdminOrder> getAllOrders() {
        List<AdminOrder> list = new ArrayList<>();
        String sql = "SELECT o.order_id, CONCAT('ORD-2026-', o.order_id) AS order_number, u.email AS customer_email, o.total_amount, o.order_status, o.created_at " +
                     "FROM orders o LEFT JOIN users u ON o.user_id = u.user_id ORDER BY o.order_id DESC";
        try (Connection conn = SwingDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new AdminOrder(
                        rs.getInt("order_id"),
                        rs.getString("order_number"),
                        rs.getString("customer_email"),
                        rs.getDouble("total_amount"),
                        rs.getString("order_status"),
                        rs.getTimestamp("created_at")
                ));
            }
            return list;
        } catch (Exception e) {
            return new ArrayList<>(fallbackOrders);
        }
    }

    public boolean updateOrderStatus(int orderId, String newStatus) {
        String sql = "UPDATE orders SET order_status = ? WHERE order_id = ?";
        try (Connection conn = SwingDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newStatus);
            ps.setInt(2, orderId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            for (AdminOrder o : fallbackOrders) {
                if (o.getOrderId() == orderId) {
                    o.setStatus(newStatus);
                    return true;
                }
            }
            return false;
        }
    }
}
