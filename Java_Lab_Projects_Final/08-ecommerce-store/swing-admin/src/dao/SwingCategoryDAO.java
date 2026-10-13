package dao;

import model.AdminCategory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SwingCategoryDAO {
    private final List<AdminCategory> fallbackCategories = new ArrayList<>();

    public SwingCategoryDAO() {
        seedFallback();
    }

    private void seedFallback() {
        fallbackCategories.add(new AdminCategory(1, "Electronics", "Gadgets, smartphones, and audio gear"));
        fallbackCategories.add(new AdminCategory(2, "Apparel", "Men and women casual and formal clothing"));
        fallbackCategories.add(new AdminCategory(3, "Accessories", "Peripherals and everyday carry items"));
        fallbackCategories.add(new AdminCategory(4, "Books", "Computer science and technical literature"));
    }

    public List<AdminCategory> getAllCategories() {
        List<AdminCategory> list = new ArrayList<>();
        String sql = "SELECT category_id, name, description FROM categories ORDER BY category_id ASC";
        try (Connection conn = SwingDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new AdminCategory(
                        rs.getInt("category_id"),
                        rs.getString("name"),
                        rs.getString("description")
                ));
            }
            return list;
        } catch (Exception e) {
            return new ArrayList<>(fallbackCategories);
        }
    }

    public boolean addCategory(String name, String description) {
        String sql = "INSERT INTO categories (name, description, slug) VALUES (?, ?, ?)";
        try (Connection conn = SwingDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setString(2, description);
            ps.setString(3, name.toLowerCase().replaceAll("[^a-z0-9]+", "-"));
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            int newId = fallbackCategories.isEmpty() ? 1 : fallbackCategories.get(fallbackCategories.size() - 1).getCategoryId() + 1;
            fallbackCategories.add(new AdminCategory(newId, name, description));
            return true;
        }
    }
}
