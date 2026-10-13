package com.shopsphere.dao;

import com.shopsphere.model.Address;
import com.shopsphere.model.CartItem;
import com.shopsphere.model.Order;
import com.shopsphere.model.OrderItem;
import com.shopsphere.util.DB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Data Access Object for master orders, order items, and transactional checkout persistence.
 */
public class OrderDAO {

    /**
     * Executes an atomic, transactional order placement.
     * Inserts order, batch-inserts order_items, updates stock, logs payment, and commits.
     * Rolls back on any exception.
     *
     * @param order Master order details
     * @param items List of purchased items
     * @param couponId Optional coupon ID if applied
     * @return Generated order_id
     * @throws SQLException on transaction failure
     */
    public int createOrder(Order order, List<CartItem> items, Integer couponId) throws SQLException {
        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("Cannot place an order with empty items list");
        }

        String insertOrderSql = """
            INSERT INTO orders (user_id, address_id, total_amount, discount, payment_method, payment_status, order_status)
            VALUES (?, ?, ?, ?, ?, ?, ?)
            """;

        String insertItemSql = """
            INSERT INTO order_items (order_id, product_id, quantity, price)
            VALUES (?, ?, ?, ?)
            """;

        String updateStockSql = """
            UPDATE products SET stock = stock - ? 
            WHERE product_id = ? AND stock >= ?
            """;

        String insertPaymentSql = """
            INSERT INTO payments (order_id, payment_method, transaction_reference, amount, payment_status)
            VALUES (?, ?, ?, ?, ?)
            """;

        try (Connection con = DB.getConnection()) {
            con.setAutoCommit(false);
            try {
                // 1. Insert master order
                int orderId;
                try (PreparedStatement ps = con.prepareStatement(insertOrderSql, Statement.RETURN_GENERATED_KEYS)) {
                    ps.setInt(1, order.getUserId());
                    if (order.getAddressId() != null) {
                        ps.setInt(2, order.getAddressId());
                    } else {
                        ps.setNull(2, java.sql.Types.INTEGER);
                    }
                    ps.setDouble(3, order.getTotalAmount());
                    ps.setDouble(4, order.getDiscount());
                    ps.setString(5, order.getPaymentMethod());
                    ps.setString(6, order.getPaymentStatus());
                    ps.setString(7, order.getOrderStatus());

                    ps.executeUpdate();
                    try (ResultSet rs = ps.getGeneratedKeys()) {
                        if (!rs.next()) {
                            throw new SQLException("Failed to retrieve generated order_id");
                        }
                        orderId = rs.getInt(1);
                        order.setOrderId(orderId);
                    }
                }

                // 2. Validate stock and batch insert order items
                try (PreparedStatement itemPs = con.prepareStatement(insertItemSql);
                     PreparedStatement stockPs = con.prepareStatement(updateStockSql)) {

                    for (CartItem ci : items) {
                        // Check & decrement stock atomically
                        stockPs.setInt(1, ci.getQuantity());
                        stockPs.setInt(2, ci.getProductId());
                        stockPs.setInt(3, ci.getQuantity());
                        int updated = stockPs.executeUpdate();
                        if (updated == 0) {
                            throw new SQLException("Insufficient stock available for product ID: " + ci.getProductId());
                        }

                        // Add order item batch
                        itemPs.setInt(1, orderId);
                        itemPs.setInt(2, ci.getProductId());
                        itemPs.setInt(3, ci.getQuantity());
                        itemPs.setDouble(4, ci.getUnitPrice());
                        itemPs.addBatch();
                    }
                    itemPs.executeBatch();
                }

                // 3. Insert mock payment record
                try (PreparedStatement payPs = con.prepareStatement(insertPaymentSql)) {
                    payPs.setInt(1, orderId);
                    payPs.setString(2, order.getPaymentMethod());
                    payPs.setString(3, "TXN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
                    payPs.setDouble(4, order.getTotalAmount());
                    payPs.setString(5, "COD".equalsIgnoreCase(order.getPaymentMethod()) ? "PENDING" : "SUCCESS");
                    payPs.executeUpdate();
                }

                // 4. Record coupon usage if coupon was applied
                if (couponId != null && couponId > 0) {
                    new CouponDAO().recordUsage(con, couponId, order.getUserId(), orderId);
                }

                // 5. Commit atomic transaction
                con.commit();
                return orderId;

            } catch (Exception ex) {
                con.rollback();
                throw new SQLException("Order transaction rolled back: " + ex.getMessage(), ex);
            } finally {
                con.setAutoCommit(true);
            }
        }
    }

    public Order findById(int orderId) throws SQLException {
        String sql = """
            SELECT o.order_id, o.user_id, o.address_id, o.total_amount, o.discount,
                   o.payment_method, o.payment_status, o.order_status, o.created_at,
                   a.address_line, a.city, a.state, a.pincode, a.address_type
            FROM orders o
            LEFT JOIN addresses a ON o.address_id = a.address_id
            WHERE o.order_id = ?
            """;

        try (Connection con = DB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, orderId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Order order = mapRow(rs);
                    order.setItems(getOrderItems(orderId));
                    return order;
                }
            }
        }
        return null;
    }

    public List<Order> findByUserId(int userId) throws SQLException {
        List<Order> list = new ArrayList<>();
        String sql = """
            SELECT o.order_id, o.user_id, o.address_id, o.total_amount, o.discount,
                   o.payment_method, o.payment_status, o.order_status, o.created_at,
                   a.address_line, a.city, a.state, a.pincode, a.address_type
            FROM orders o
            LEFT JOIN addresses a ON o.address_id = a.address_id
            WHERE o.user_id = ?
            ORDER BY o.order_id DESC
            """;

        try (Connection con = DB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Order order = mapRow(rs);
                    order.setItems(getOrderItems(order.getOrderId()));
                    list.add(order);
                }
            }
        }
        return list;
    }

    public List<Order> findAllOrders() throws SQLException {
        List<Order> list = new ArrayList<>();
        String sql = """
            SELECT o.order_id, o.user_id, o.address_id, o.total_amount, o.discount,
                   o.payment_method, o.payment_status, o.order_status, o.created_at,
                   a.address_line, a.city, a.state, a.pincode, a.address_type
            FROM orders o
            LEFT JOIN addresses a ON o.address_id = a.address_id
            ORDER BY o.order_id DESC
            """;

        try (Connection con = DB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Order order = mapRow(rs);
                order.setItems(getOrderItems(order.getOrderId()));
                list.add(order);
            }
        }
        return list;
    }

    public List<OrderItem> getOrderItems(int orderId) throws SQLException {
        List<OrderItem> list = new ArrayList<>();
        String sql = """
            SELECT oi.order_item_id, oi.order_id, oi.product_id, p.name AS product_name,
                   oi.quantity, oi.price
            FROM order_items oi
            JOIN products p ON oi.product_id = p.product_id
            WHERE oi.order_id = ?
            ORDER BY oi.order_item_id ASC
            """;

        try (Connection con = DB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, orderId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    OrderItem item = new OrderItem(
                            rs.getInt("order_item_id"),
                            rs.getInt("order_id"),
                            rs.getInt("product_id"),
                            rs.getString("product_name"),
                            rs.getInt("quantity"),
                            rs.getDouble("price")
                    );
                    list.add(item);
                }
            }
        }
        return list;
    }

    public boolean updateOrderStatus(int orderId, String status) throws SQLException {
        String sql = "UPDATE orders SET order_status = ? WHERE order_id = ?";
        try (Connection con = DB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, status);
            ps.setInt(2, orderId);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean updatePaymentStatus(int orderId, String status) throws SQLException {
        String sql = "UPDATE orders SET payment_status = ? WHERE order_id = ?";
        try (Connection con = DB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, status);
            ps.setInt(2, orderId);
            return ps.executeUpdate() > 0;
        }
    }

    public Map<String, Object> getSalesMetrics() throws SQLException {
        Map<String, Object> map = new HashMap<>();
        String sql = """
            SELECT COUNT(*) AS total_orders,
                   COALESCE(SUM(total_amount), 0) AS total_revenue,
                   COUNT(CASE WHEN order_status = 'PLACED' THEN 1 END) AS pending_orders,
                   COUNT(CASE WHEN order_status = 'DELIVERED' THEN 1 END) AS delivered_orders
            FROM orders
            """;
        try (Connection con = DB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                map.put("totalOrders", rs.getInt("total_orders"));
                map.put("totalRevenue", rs.getDouble("total_revenue"));
                map.put("pendingOrders", rs.getInt("pending_orders"));
                map.put("deliveredOrders", rs.getInt("delivered_orders"));
            }
        }
        return map;
    }

    private Order mapRow(ResultSet rs) throws SQLException {
        Order o = new Order();
        o.setOrderId(rs.getInt("order_id"));
        o.setUserId(rs.getInt("user_id"));
        int addrId = rs.getInt("address_id");
        if (!rs.wasNull()) {
            o.setAddressId(addrId);
            Address a = new Address();
            a.setAddressId(addrId);
            a.setAddressLine(rs.getString("address_line"));
            a.setCity(rs.getString("city"));
            a.setState(rs.getString("state"));
            a.setPincode(rs.getString("pincode"));
            a.setAddressType(rs.getString("address_type"));
            o.setAddress(a);
        }
        o.setTotalAmount(rs.getDouble("total_amount"));
        o.setDiscount(rs.getDouble("discount"));
        o.setPaymentMethod(rs.getString("payment_method"));
        o.setPaymentStatus(rs.getString("payment_status"));
        o.setOrderStatus(rs.getString("order_status"));
        o.setCreatedAt(rs.getTimestamp("created_at"));
        return o;
    }
}
