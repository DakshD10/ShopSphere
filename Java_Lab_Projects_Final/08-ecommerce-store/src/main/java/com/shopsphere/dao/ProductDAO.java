package com.shopsphere.dao;

import com.shopsphere.model.Product;
import com.shopsphere.util.DB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for products table with filtering, search, and inventory controls.
 */
public class ProductDAO {

    public List<Product> findAll() throws SQLException {
        String sql = """
            SELECT p.product_id, p.category_id, c.name AS category_name, p.name, p.brand,
                   p.description, p.price, p.discount, p.stock, p.image_url, p.status, p.created_at
            FROM products p
            LEFT JOIN categories c ON p.category_id = c.category_id
            ORDER BY p.product_id DESC
            """;
        return executeQueryList(sql);
    }

    public List<Product> findAllActive() throws SQLException {
        String sql = """
            SELECT p.product_id, p.category_id, c.name AS category_name, p.name, p.brand,
                   p.description, p.price, p.discount, p.stock, p.image_url, p.status, p.created_at
            FROM products p
            LEFT JOIN categories c ON p.category_id = c.category_id
            WHERE p.status = TRUE
            ORDER BY p.product_id DESC
            """;
        return executeQueryList(sql);
    }

    public Product findById(int productId) throws SQLException {
        String sql = """
            SELECT p.product_id, p.category_id, c.name AS category_name, p.name, p.brand,
                   p.description, p.price, p.discount, p.stock, p.image_url, p.status, p.created_at
            FROM products p
            LEFT JOIN categories c ON p.category_id = c.category_id
            WHERE p.product_id = ?
            """;
        try (Connection con = DB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, productId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        }
        return null;
    }

    public List<Product> findByCategory(int categoryId) throws SQLException {
        String sql = """
            SELECT p.product_id, p.category_id, c.name AS category_name, p.name, p.brand,
                   p.description, p.price, p.discount, p.stock, p.image_url, p.status, p.created_at
            FROM products p
            LEFT JOIN categories c ON p.category_id = c.category_id
            WHERE p.category_id = ? AND p.status = TRUE
            ORDER BY p.product_id DESC
            """;
        try (Connection con = DB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, categoryId);
            try (ResultSet rs = ps.executeQuery()) {
                List<Product> list = new ArrayList<>();
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
                return list;
            }
        }
    }

    public List<Product> filterAndSort(Integer categoryId, String search, Double minPrice, Double maxPrice, String sortBy) throws SQLException {
        StringBuilder sql = new StringBuilder("""
            SELECT p.product_id, p.category_id, c.name AS category_name, p.name, p.brand,
                   p.description, p.price, p.discount, p.stock, p.image_url, p.status, p.created_at
            FROM products p
            LEFT JOIN categories c ON p.category_id = c.category_id
            WHERE p.status = TRUE
            """);

        List<Object> params = new ArrayList<>();

        if (categoryId != null && categoryId > 0) {
            sql.append(" AND p.category_id = ?");
            params.add(categoryId);
        }

        if (search != null && !search.trim().isEmpty()) {
            sql.append(" AND (LOWER(p.name) LIKE ? OR LOWER(p.brand) LIKE ? OR LOWER(p.description) LIKE ?)");
            String term = "%" + search.trim().toLowerCase() + "%";
            params.add(term);
            params.add(term);
            params.add(term);
        }

        if (minPrice != null && minPrice >= 0) {
            sql.append(" AND p.price >= ?");
            params.add(minPrice);
        }

        if (maxPrice != null && maxPrice > 0) {
            sql.append(" AND p.price <= ?");
            params.add(maxPrice);
        }

        // Sorting
        if ("price_asc".equalsIgnoreCase(sortBy)) {
            sql.append(" ORDER BY p.price ASC");
        } else if ("price_desc".equalsIgnoreCase(sortBy)) {
            sql.append(" ORDER BY p.price DESC");
        } else if ("name_asc".equalsIgnoreCase(sortBy)) {
            sql.append(" ORDER BY p.name ASC");
        } else {
            sql.append(" ORDER BY p.product_id DESC");
        }

        try (Connection con = DB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }

            try (ResultSet rs = ps.executeQuery()) {
                List<Product> list = new ArrayList<>();
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
                return list;
            }
        }
    }

    public int createProduct(Product p) throws SQLException {
        String sql = """
            INSERT INTO products (category_id, name, brand, description, price, discount, stock, image_url, status)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;
        try (Connection con = DB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, p.getCategoryId());
            ps.setString(2, p.getName());
            ps.setString(3, p.getBrand());
            ps.setString(4, p.getDescription());
            ps.setDouble(5, p.getPrice());
            ps.setDouble(6, p.getDiscount());
            ps.setInt(7, p.getStock());
            ps.setString(8, p.getImageUrl());
            ps.setBoolean(9, p.isStatus());

            int rows = ps.executeUpdate();
            if (rows > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        int id = rs.getInt(1);
                        p.setProductId(id);
                        return id;
                    }
                }
            }
            return -1;
        }
    }

    public boolean updateProduct(Product p) throws SQLException {
        String sql = """
            UPDATE products 
            SET category_id = ?, name = ?, brand = ?, description = ?, price = ?, 
                discount = ?, stock = ?, image_url = ?, status = ?
            WHERE product_id = ?
            """;
        try (Connection con = DB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, p.getCategoryId());
            ps.setString(2, p.getName());
            ps.setString(3, p.getBrand());
            ps.setString(4, p.getDescription());
            ps.setDouble(5, p.getPrice());
            ps.setDouble(6, p.getDiscount());
            ps.setInt(7, p.getStock());
            ps.setString(8, p.getImageUrl());
            ps.setBoolean(9, p.isStatus());
            ps.setInt(10, p.getProductId());

            return ps.executeUpdate() > 0;
        }
    }

    public boolean deleteProduct(int productId) throws SQLException {
        String sql = "DELETE FROM products WHERE product_id = ?";
        try (Connection con = DB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, productId);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean updateStock(int productId, int deltaQuantity) throws SQLException {
        String sql = "UPDATE products SET stock = stock + ? WHERE product_id = ? AND (stock + ?) >= 0";
        try (Connection con = DB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, deltaQuantity);
            ps.setInt(2, productId);
            ps.setInt(3, deltaQuantity);
            return ps.executeUpdate() > 0;
        }
    }

    public List<Product> findLowStock(int threshold) throws SQLException {
        String sql = """
            SELECT p.product_id, p.category_id, c.name AS category_name, p.name, p.brand,
                   p.description, p.price, p.discount, p.stock, p.image_url, p.status, p.created_at
            FROM products p
            LEFT JOIN categories c ON p.category_id = c.category_id
            WHERE p.stock <= ?
            ORDER BY p.stock ASC
            """;
        try (Connection con = DB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, threshold);
            try (ResultSet rs = ps.executeQuery()) {
                List<Product> list = new ArrayList<>();
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
                return list;
            }
        }
    }

    private List<Product> executeQueryList(String sql) throws SQLException {
        List<Product> list = new ArrayList<>();
        try (Connection con = DB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(mapRow(rs));
            }
        }
        return list;
    }

    private Product mapRow(ResultSet rs) throws SQLException {
        Product p = new Product();
        p.setProductId(rs.getInt("product_id"));
        p.setCategoryId(rs.getInt("category_id"));
        p.setCategoryName(rs.getString("category_name"));
        p.setName(rs.getString("name"));
        p.setBrand(rs.getString("brand"));
        p.setDescription(rs.getString("description"));
        p.setPrice(rs.getDouble("price"));
        p.setDiscount(rs.getDouble("discount"));
        p.setStock(rs.getInt("stock"));
        p.setImageUrl(rs.getString("image_url"));
        p.setStatus(rs.getBoolean("status"));
        p.setCreatedAt(rs.getTimestamp("created_at"));
        return p;
    }
}
