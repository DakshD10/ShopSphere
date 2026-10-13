package dao;

import model.AdminProduct;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SwingProductDAO {
    private final List<AdminProduct> fallbackProducts = new ArrayList<>();

    public SwingProductDAO() {
        seedFallback();
    }

    private void seedFallback() {
        fallbackProducts.add(new AdminProduct(1, "Smartphone Pro Max", "Electronics", 999.99, 45, true));
        fallbackProducts.add(new AdminProduct(2, "Wireless Noise-Canceling Headphones", "Electronics", 199.99, 80, true));
        fallbackProducts.add(new AdminProduct(3, "Classic Oxford Cotton Shirt", "Apparel", 49.99, 120, true));
        fallbackProducts.add(new AdminProduct(4, "Ergonomic Mechanical Keyboard", "Accessories", 129.50, 30, true));
        fallbackProducts.add(new AdminProduct(5, "Java Programming: Enterprise Edition", "Books", 59.00, 200, true));
    }

    public List<AdminProduct> getAllProducts() {
        List<AdminProduct> list = new ArrayList<>();
        String sql = "SELECT p.product_id, p.name, c.name AS category_name, p.price, p.stock, p.status " +
                     "FROM products p LEFT JOIN categories c ON p.category_id = c.category_id ORDER BY p.product_id ASC";
        try (Connection conn = SwingDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new AdminProduct(
                        rs.getInt("product_id"),
                        rs.getString("name"),
                        rs.getString("category_name"),
                        rs.getDouble("price"),
                        rs.getInt("stock"),
                        rs.getBoolean("status")
                ));
            }
            return list;
        } catch (Exception e) {
            return new ArrayList<>(fallbackProducts);
        }
    }

    public boolean addProduct(String name, int categoryId, double price, int stock, boolean active) {
        String sql = "INSERT INTO products (name, category_id, price, stock, status, brand, description) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = SwingDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setInt(2, categoryId);
            ps.setDouble(3, price);
            ps.setInt(4, stock);
            ps.setBoolean(5, active);
            ps.setString(6, "ShopSphere");
            ps.setString(7, "Added via ShopSphere Swing Admin");
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            int newId = fallbackProducts.isEmpty() ? 1 : fallbackProducts.get(fallbackProducts.size() - 1).getId() + 1;
            fallbackProducts.add(new AdminProduct(newId, name, "Category " + categoryId, price, stock, active));
            return true;
        }
    }

    public boolean updateProduct(int id, String name, double price, int stock, boolean active) {
        String sql = "UPDATE products SET name = ?, price = ?, stock = ?, status = ? WHERE product_id = ?";
        try (Connection conn = SwingDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setDouble(2, price);
            ps.setInt(3, stock);
            ps.setBoolean(4, active);
            ps.setInt(5, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            for (AdminProduct p : fallbackProducts) {
                if (p.getId() == id) {
                    p.setName(name);
                    p.setPrice(price);
                    p.setStock(stock);
                    p.setActive(active);
                    return true;
                }
            }
            return false;
        }
    }

    public boolean deleteProduct(int id) {
        String sql = "DELETE FROM products WHERE product_id = ?";
        try (Connection conn = SwingDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            return fallbackProducts.removeIf(p -> p.getId() == id);
        }
    }
}
