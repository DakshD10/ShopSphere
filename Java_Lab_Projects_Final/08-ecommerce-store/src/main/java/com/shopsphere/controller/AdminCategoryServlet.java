package com.shopsphere.controller;

import com.shopsphere.model.Category;
import com.shopsphere.service.CategoryService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * Controller providing Administrator Category management.
 */
@WebServlet(name = "AdminCategoryServlet", urlPatterns = {"/admin/categories"})
public class AdminCategoryServlet extends HttpServlet {

    private CategoryService categoryService;

    @Override
    public void init() throws ServletException {
        this.categoryService = new CategoryService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        try {
            List<Category> categories = categoryService.getAllCategories();

            String editIdStr = req.getParameter("editId");
            if (editIdStr != null && !editIdStr.trim().isEmpty()) {
                try {
                    int editId = Integer.parseInt(editIdStr.trim());
                    Category editCategory = categoryService.getCategoryById(editId);
                    req.setAttribute("editCategory", editCategory);
                } catch (NumberFormatException ignored) {}
            }

            req.setAttribute("categories", categories);
            req.getRequestDispatcher("/admin/categories.jsp").forward(req, res);

        } catch (SQLException e) {
            throw new ServletException("Database error loading category manager", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        HttpSession session = req.getSession(false);

        String action = req.getParameter("action");
        if (action == null) {
            action = "save";
        }

        try {
            if ("delete".equalsIgnoreCase(action)) {
                int categoryId = Integer.parseInt(req.getParameter("categoryId"));
                categoryService.deleteCategory(categoryId);
                if (session != null) session.setAttribute("notice", "Category deleted successfully.");
            } else if ("save".equalsIgnoreCase(action)) {
                int categoryId = 0;
                String catIdStr = req.getParameter("categoryId");
                if (catIdStr != null && !catIdStr.trim().isEmpty()) {
                    categoryId = Integer.parseInt(catIdStr.trim());
                }

                String name = req.getParameter("name");
                String description = req.getParameter("description");
                boolean status = "on".equalsIgnoreCase(req.getParameter("status")) || "true".equalsIgnoreCase(req.getParameter("status"));

                Category c = new Category(categoryId, name, description, status);
                categoryService.saveCategory(c);

                if (session != null) {
                    session.setAttribute("notice", (categoryId > 0 ? "Category updated: " : "Category created: ") + name);
                }
            }

            res.sendRedirect(req.getContextPath() + "/admin/categories");

        } catch (IllegalArgumentException e) {
            if (session != null) session.setAttribute("error", e.getMessage());
            res.sendRedirect(req.getContextPath() + "/admin/categories");
        } catch (SQLException e) {
            throw new ServletException("Database error saving category", e);
        }
    }
}
