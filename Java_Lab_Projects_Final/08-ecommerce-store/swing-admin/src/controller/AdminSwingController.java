package controller;

import dao.SwingCategoryDAO;
import dao.SwingOrderDAO;
import dao.SwingProductDAO;
import dao.SwingUserDAO;
import model.AdminCategory;
import view.AdminDashboardFrame;
import view.LoginFrame;

import javax.swing.*;

/**
 * AdminSwingController: Swing MVC Controller managing business logic and event handling.
 * Coordinates interaction between Swing Views and JDBC DAOs.
 */
public class AdminSwingController {
    private final LoginFrame loginFrame;
    private final AdminDashboardFrame dashboardFrame;
    private final SwingProductDAO productDAO;
    private final SwingOrderDAO orderDAO;
    private final SwingCategoryDAO categoryDAO;
    private final SwingUserDAO userDAO;

    public AdminSwingController(LoginFrame loginFrame, AdminDashboardFrame dashboardFrame) {
        this.loginFrame = loginFrame;
        this.dashboardFrame = dashboardFrame;
        this.productDAO = new SwingProductDAO();
        this.orderDAO = new SwingOrderDAO();
        this.categoryDAO = new SwingCategoryDAO();
        this.userDAO = new SwingUserDAO();

        initController();
    }

    private void initController() {
        // Login bindings
        loginFrame.addLoginListener(e -> handleLogin());

        // Dashboard bindings
        dashboardFrame.addLogoutListener(e -> handleLogout());
        dashboardFrame.addProductRefreshListener(e -> refreshProducts());
        dashboardFrame.addProductAddListener(e -> handleAddProduct());
        dashboardFrame.addProductUpdateListener(e -> handleUpdateProduct());
        dashboardFrame.addProductDeleteListener(e -> handleDeleteProduct());
        dashboardFrame.addOrderStatusUpdateListener(e -> handleUpdateOrderStatus());
        dashboardFrame.addOrderRefreshListener(e -> refreshOrders());
        dashboardFrame.addCategoryAddListener(e -> handleAddCategory());
        dashboardFrame.addUserRefreshListener(e -> refreshUsers());
    }

    public void start() {
        loginFrame.setVisible(true);
    }

    private void handleLogin() {
        String email = loginFrame.getEmail();
        String password = loginFrame.getPassword();

        if (email.isEmpty() || password.isEmpty()) {
            loginFrame.setStatusMessage("Please provide both email and password.");
            return;
        }

        boolean valid = userDAO.authenticateAdmin(email, password);
        if (valid) {
            loginFrame.setVisible(false);
            loadAllDashboardData();
            dashboardFrame.setVisible(true);
        } else {
            loginFrame.showErrorMessage("Invalid credentials! Only administrators can access this portal.\nDefault: admin@shopsphere.com / admin123");
        }
    }

    private void handleLogout() {
        dashboardFrame.setVisible(false);
        loginFrame.setVisible(true);
    }

    private void loadAllDashboardData() {
        refreshCategories();
        refreshProducts();
        refreshOrders();
        refreshUsers();
    }

    private void refreshProducts() {
        dashboardFrame.setProductList(productDAO.getAllProducts());
    }

    private void refreshOrders() {
        dashboardFrame.setOrderList(orderDAO.getAllOrders());
    }

    private void refreshCategories() {
        dashboardFrame.setCategoryList(categoryDAO.getAllCategories());
    }

    private void refreshUsers() {
        dashboardFrame.setUserList(userDAO.getAllUsers());
    }

    private void handleAddProduct() {
        String name = dashboardFrame.getProdNameInput();
        String priceStr = dashboardFrame.getProdPriceInput();
        String stockStr = dashboardFrame.getProdStockInput();
        AdminCategory category = dashboardFrame.getSelectedCategoryInput();
        boolean active = dashboardFrame.getProdActiveInput();

        if (name.isEmpty() || priceStr.isEmpty() || stockStr.isEmpty()) {
            dashboardFrame.showError("Please complete all product fields.");
            return;
        }

        try {
            double price = Double.parseDouble(priceStr);
            int stock = Integer.parseInt(stockStr);
            int catId = category != null ? category.getCategoryId() : 1;

            if (price < 0 || stock < 0) {
                dashboardFrame.showError("Price and Stock must be non-negative.");
                return;
            }

            boolean ok = productDAO.addProduct(name, catId, price, stock, active);
            if (ok) {
                dashboardFrame.showMessage("Product added successfully!");
                dashboardFrame.clearProductInputs();
                refreshProducts();
            } else {
                dashboardFrame.showError("Failed to add product.");
            }
        } catch (NumberFormatException ex) {
            dashboardFrame.showError("Price must be a decimal and Stock must be an integer.");
        }
    }

    private void handleUpdateProduct() {
        int id = dashboardFrame.getSelectedProductId();
        if (id < 0) {
            dashboardFrame.showError("Please select a product from the table first.");
            return;
        }

        String name = dashboardFrame.getProdNameInput();
        String priceStr = dashboardFrame.getProdPriceInput();
        String stockStr = dashboardFrame.getProdStockInput();
        boolean active = dashboardFrame.getProdActiveInput();

        try {
            double price = Double.parseDouble(priceStr);
            int stock = Integer.parseInt(stockStr);

            boolean ok = productDAO.updateProduct(id, name, price, stock, active);
            if (ok) {
                dashboardFrame.showMessage("Product #" + id + " updated successfully!");
                dashboardFrame.clearProductInputs();
                refreshProducts();
            } else {
                dashboardFrame.showError("Failed to update product.");
            }
        } catch (NumberFormatException ex) {
            dashboardFrame.showError("Price must be a decimal and Stock must be an integer.");
        }
    }

    private void handleDeleteProduct() {
        int id = dashboardFrame.getSelectedProductId();
        if (id < 0) {
            dashboardFrame.showError("Please select a product from the table first.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(dashboardFrame,
                "Are you sure you want to delete Product #" + id + "?",
                "Confirm Deletion", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            boolean ok = productDAO.deleteProduct(id);
            if (ok) {
                dashboardFrame.showMessage("Product #" + id + " deleted.");
                dashboardFrame.clearProductInputs();
                refreshProducts();
            } else {
                dashboardFrame.showError("Failed to delete product.");
            }
        }
    }

    private void handleUpdateOrderStatus() {
        int orderId = dashboardFrame.getSelectedOrderId();
        if (orderId < 0) {
            dashboardFrame.showError("Please select an order from the table to update.");
            return;
        }

        String newStatus = dashboardFrame.getSelectedOrderStatus();
        boolean ok = orderDAO.updateOrderStatus(orderId, newStatus);
        if (ok) {
            dashboardFrame.showMessage("Order #" + orderId + " status updated to " + newStatus);
            refreshOrders();
        } else {
            dashboardFrame.showError("Failed to update order status.");
        }
    }

    private void handleAddCategory() {
        String name = dashboardFrame.getCatNameInput();
        String desc = dashboardFrame.getCatDescInput();

        if (name.isEmpty()) {
            dashboardFrame.showError("Category name cannot be empty.");
            return;
        }

        boolean ok = categoryDAO.addCategory(name, desc);
        if (ok) {
            dashboardFrame.showMessage("Category '" + name + "' created successfully!");
            dashboardFrame.clearCategoryInputs();
            refreshCategories();
        } else {
            dashboardFrame.showError("Failed to create category.");
        }
    }
}
