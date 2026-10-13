package com.shopsphere.service;

import com.shopsphere.dao.CategoryDAO;
import com.shopsphere.model.Category;

import java.sql.SQLException;
import java.util.List;

/**
 * Service managing product categories.
 */
public class CategoryService {

    private final CategoryDAO categoryDAO;

    public CategoryService() {
        this.categoryDAO = new CategoryDAO();
    }

    public CategoryService(CategoryDAO categoryDAO) {
        this.categoryDAO = categoryDAO;
    }

    public List<Category> getAllCategories() throws SQLException {
        try {
            return categoryDAO.findAll();
        } catch (SQLException e) {
            return getFallbackCategories();
        }
    }

    public List<Category> getActiveCategories() throws SQLException {
        try {
            return categoryDAO.findAllActive();
        } catch (SQLException e) {
            return getFallbackCategories();
        }
    }

    public Category getCategoryById(int categoryId) throws SQLException {
        try {
            return categoryDAO.findById(categoryId);
        } catch (SQLException e) {
            for (Category c : getFallbackCategories()) {
                if (c.getCategoryId() == categoryId) return c;
            }
            return null;
        }
    }

    private List<Category> getFallbackCategories() {
        List<Category> list = new java.util.ArrayList<>();
        list.add(createCategory(1, "Electronics", "Smartphones, Audio gear, and Laptops", true));
        list.add(createCategory(2, "Apparel", "Men and Women Clothing and Footwear", true));
        list.add(createCategory(3, "Accessories", "Bags, Keyboards, and Daily Essentials", true));
        list.add(createCategory(4, "Books", "Technical and Academic textbooks", true));
        return list;
    }

    private Category createCategory(int id, String name, String desc, boolean status) {
        Category c = new Category();
        c.setCategoryId(id);
        c.setName(name);
        c.setDescription(desc);
        c.setStatus(status);
        return c;
    }

    public boolean saveCategory(Category category) throws SQLException {
        if (category.getName() == null || category.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Category name is required.");
        }
        if (category.getCategoryId() > 0) {
            return categoryDAO.updateCategory(category);
        } else {
            return categoryDAO.createCategory(category) > 0;
        }
    }

    public boolean deleteCategory(int categoryId) throws SQLException {
        return categoryDAO.deleteCategory(categoryId);
    }
}
