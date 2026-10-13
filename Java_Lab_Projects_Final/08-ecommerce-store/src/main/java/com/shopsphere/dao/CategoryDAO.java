package com.shopsphere.dao;

import com.shopsphere.model.Category;
import com.shopsphere.util.DB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for categories table.
 */
public class CategoryDAO {

    public List<Category> findAll() throws SQLException {
        List<Category> list = new ArrayList<>();
        String sql = "SELECT category_id, name, description, status FROM categories ORDER BY name ASC";
        try (Connection con = DB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(mapRow(rs));
            }
        }
        return list;
    }

    public List<Category> findAllActive() throws SQLException {
        List<Category> list = new ArrayList<>();
        String sql = "SELECT category_id, name, description, status FROM categories WHERE status = TRUE ORDER BY name ASC";
        try (Connection con = DB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(mapRow(rs));
            }
        }
        return list;
    }

    public Category findById(int categoryId) throws SQLException {
        String sql = "SELECT category_id, name, description, status FROM categories WHERE category_id = ?";
        try (Connection con = DB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, categoryId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        }
        return null;
    }

    public int createCategory(Category cat) throws SQLException {
        String sql = "INSERT INTO categories (name, description, status) VALUES (?, ?, ?)";
        try (Connection con = DB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, cat.getName());
            ps.setString(2, cat.getDescription());
            ps.setBoolean(3, cat.isStatus());

            int rows = ps.executeUpdate();
            if (rows > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        int id = rs.getInt(1);
                        cat.setCategoryId(id);
                        return id;
                    }
                }
            }
            return -1;
        }
    }

    public boolean updateCategory(Category cat) throws SQLException {
        String sql = "UPDATE categories SET name = ?, description = ?, status = ? WHERE category_id = ?";
        try (Connection con = DB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, cat.getName());
            ps.setString(2, cat.getDescription());
            ps.setBoolean(3, cat.isStatus());
            ps.setInt(4, cat.getCategoryId());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean deleteCategory(int categoryId) throws SQLException {
        String sql = "DELETE FROM categories WHERE category_id = ?";
        try (Connection con = DB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, categoryId);
            return ps.executeUpdate() > 0;
        }
    }

    private Category mapRow(ResultSet rs) throws SQLException {
        return new Category(
                rs.getInt("category_id"),
                rs.getString("name"),
                rs.getString("description"),
                rs.getBoolean("status")
        );
    }
}
