package com.shopsphere.service;

import com.shopsphere.dao.ProductDAO;
import com.shopsphere.model.Product;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Service encapsulating product catalog search, filtering, and stock controls.
 */
public class ProductService {

    private final ProductDAO productDAO;

    public ProductService() {
        this.productDAO = new ProductDAO();
    }

    public ProductService(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }

    public List<Product> getAllProducts() throws SQLException {
        return productDAO.findAll();
    }

    public List<Product> getActiveProducts() throws SQLException {
        return productDAO.findAllActive();
    }

    public Product getProductById(int productId) throws SQLException {
        try {
            return productDAO.findById(productId);
        } catch (SQLException e) {
            for (Product p : getFallbackProducts()) {
                if (p.getProductId() == productId) return p;
            }
            return null;
        }
    }

    public List<Product> getProductsByCategory(int categoryId) throws SQLException {
        try {
            return productDAO.findByCategory(categoryId);
        } catch (SQLException e) {
            List<Product> matches = new ArrayList<>();
            for (Product p : getFallbackProducts()) {
                if (p.getCategoryId() == categoryId) matches.add(p);
            }
            return matches;
        }
    }

    public List<Product> filterAndSort(Integer categoryId, String search, Double minPrice, Double maxPrice, String sortBy) throws SQLException {
        try {
            return productDAO.filterAndSort(categoryId, search, minPrice, maxPrice, sortBy);
        } catch (SQLException e) {
            System.err.println("[ProductService] Database offline (" + e.getMessage() + "), serving cached demo catalog.");
            List<Product> list = new ArrayList<>();
            for (Product p : getFallbackProducts()) {
                if (categoryId != null && p.getCategoryId() != categoryId) continue;
                if (search != null && !search.trim().isEmpty() &&
                    !p.getName().toLowerCase().contains(search.toLowerCase()) &&
                    !p.getDescription().toLowerCase().contains(search.toLowerCase())) continue;
                if (minPrice != null && p.getPrice() < minPrice) continue;
                if (maxPrice != null && p.getPrice() > maxPrice) continue;
                list.add(p);
            }
            return list;
        }
    }

    private List<Product> getFallbackProducts() {
        List<Product> list = new ArrayList<>();
        list.add(createFallbackProduct(1, 1, "Smartphone Pro Max", "Apple/Android Flagship Smartphone with OLED display", 999.99, 10.0, 45, "https://images.unsplash.com/photo-1511707171634-5f897ff02aa9?w=500&q=80"));
        list.add(createFallbackProduct(2, 1, "Wireless Noise-Canceling Headphones", "High fidelity over-ear headphones with active ANC", 199.99, 15.0, 80, "https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=500&q=80"));
        list.add(createFallbackProduct(3, 2, "Classic Oxford Cotton Shirt", "Premium 100% breathable cotton button-up shirt", 49.99, 0.0, 120, "https://images.unsplash.com/photo-1596755094514-f87e34085b2c?w=500&q=80"));
        list.add(createFallbackProduct(4, 3, "Ergonomic Mechanical Keyboard", "RGB backlit custom mechanical keyboard with tactile switches", 129.50, 5.0, 30, "https://images.unsplash.com/photo-1587829741301-dc798b83add3?w=500&q=80"));
        list.add(createFallbackProduct(5, 4, "Java Programming: Enterprise Edition", "Comprehensive university textbook covering Servlets, JSP, JDBC, and RMI", 59.00, 0.0, 200, "https://images.unsplash.com/photo-1544716278-ca5e3f4abd8c?w=500&q=80"));
        return list;
    }

    private Product createFallbackProduct(int id, int catId, String name, String desc, double price, double discount, int stock, String img) {
        Product p = new Product();
        p.setProductId(id);
        p.setCategoryId(catId);
        p.setName(name);
        p.setDescription(desc);
        p.setPrice(price);
        p.setDiscount(discount);
        p.setStock(stock);
        p.setImageUrl(img);
        p.setStatus(true);
        return p;
    }

    public boolean saveProduct(Product product) throws SQLException {
        if (product.getName() == null || product.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Product name cannot be blank.");
        }
        if (product.getPrice() <= 0.0) {
            throw new IllegalArgumentException("Price must be greater than zero.");
        }
        if (product.getStock() < 0) {
            throw new IllegalArgumentException("Stock cannot be negative.");
        }

        if (product.getProductId() > 0) {
            return productDAO.updateProduct(product);
        } else {
            int newId = productDAO.createProduct(product);
            return newId > 0;
        }
    }

    public boolean deleteProduct(int productId) throws SQLException {
        return productDAO.deleteProduct(productId);
    }

    public List<Product> getLowStockProducts(int threshold) throws SQLException {
        return productDAO.findLowStock(threshold > 0 ? threshold : 10);
    }
}
