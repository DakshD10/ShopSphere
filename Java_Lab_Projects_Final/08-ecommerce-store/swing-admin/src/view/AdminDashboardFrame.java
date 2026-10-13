package view;

import model.AdminCategory;
import model.AdminOrder;
import model.AdminProduct;
import model.AdminUser;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * AdminDashboardFrame: Full-featured Swing Desktop Administration Dashboard.
 * Demonstrates:
 * - JFrame, JPanel, JLabel, JTextField, JPasswordField
 * - JButton, JCheckBox, JRadioButton, JComboBox, JTable, JScrollPane, JOptionPane
 * - Pluggable Look and Feel
 * - Tabbed navigation and Swing MVC architecture.
 */
public class AdminDashboardFrame extends JFrame {

    // Tabbed Pane
    private final JTabbedPane tabbedPane = new JTabbedPane();

    // --- TAB 1: Products Components ---
    private final DefaultTableModel productTableModel = new DefaultTableModel(
            new String[]{"ID", "Product Name", "Category", "Price ($)", "Stock", "Active"}, 0) {
        @Override
        public boolean isCellEditable(int row, int col) { return false; }
    };
    private final JTable productTable = new JTable(productTableModel);
    private final JTextField txtProdId = new JTextField(5);
    private final JTextField txtProdName = new JTextField(15);
    private final JTextField txtProdPrice = new JTextField(8);
    private final JTextField txtProdStock = new JTextField(8);
    private final JComboBox<AdminCategory> cmbProdCategory = new JComboBox<>();
    private final JCheckBox chkProdActive = new JCheckBox("Is Available / Active", true);
    private final JButton btnAddProduct = new JButton("Add Product");
    private final JButton btnUpdateProduct = new JButton("Update Selected");
    private final JButton btnDeleteProduct = new JButton("Delete Selected");
    private final JButton btnRefreshProducts = new JButton("Refresh");

    // --- TAB 2: Orders Components ---
    private final DefaultTableModel orderTableModel = new DefaultTableModel(
            new String[]{"Order ID", "Order #", "Customer Email", "Total ($)", "Status", "Date"}, 0) {
        @Override
        public boolean isCellEditable(int row, int col) { return false; }
    };
    private final JTable orderTable = new JTable(orderTableModel);
    private final JComboBox<String> cmbOrderStatus = new JComboBox<>(new String[]{
            "PENDING", "CONFIRMED", "PROCESSING", "SHIPPED", "DELIVERED", "CANCELLED"
    });
    private final JButton btnUpdateOrderStatus = new JButton("Update Order Status");
    private final JButton btnRefreshOrders = new JButton("Refresh Orders");

    // --- TAB 3: Categories Components ---
    private final DefaultTableModel categoryTableModel = new DefaultTableModel(
            new String[]{"Category ID", "Name", "Description"}, 0) {
        @Override
        public boolean isCellEditable(int row, int col) { return false; }
    };
    private final JTable categoryTable = new JTable(categoryTableModel);
    private final JTextField txtCatName = new JTextField(15);
    private final JTextField txtCatDesc = new JTextField(20);
    private final JButton btnAddCategory = new JButton("Create Category");

    // --- TAB 4: Users / Customers Components ---
    private final DefaultTableModel userTableModel = new DefaultTableModel(
            new String[]{"User ID", "Full Name", "Email", "Role", "Created At"}, 0) {
        @Override
        public boolean isCellEditable(int row, int col) { return false; }
    };
    private final JTable userTable = new JTable(userTableModel);
    private final JButton btnRefreshUsers = new JButton("Refresh Customers");

    // Header controls
    private final JButton btnLogout = new JButton("Logout");
    private final JRadioButton rbSystemLaf = new JRadioButton("System Look & Feel", true);
    private final JRadioButton rbMetalLaf = new JRadioButton("Cross-Platform (Metal)");

    public AdminDashboardFrame() {
        setTitle("ShopSphere Enterprise Desktop Administration Suite (Swing MVC)");
        setSize(1020, 680);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        initComponents();
    }

    private void initComponents() {
        JPanel rootPanel = new JPanel(new BorderLayout(10, 10));
        rootPanel.setBorder(BorderFactory.createEmptyBorder(10, 12, 10, 12));

        // --- North: Top Navigation Bar ---
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(new Color(24, 76, 120));
        topBar.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        JLabel lblTitle = new JLabel("ShopSphere Desktop Management Center");
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 18));
        lblTitle.setForeground(Color.WHITE);

        JPanel rightTop = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        rightTop.setOpaque(false);

        JLabel lblAdminBadge = new JLabel("👤 Logged in as: Administrator ");
        lblAdminBadge.setForeground(new Color(220, 240, 255));
        lblAdminBadge.setFont(new Font("SansSerif", Font.PLAIN, 12));

        btnLogout.setBackground(new Color(200, 50, 50));
        btnLogout.setForeground(Color.WHITE);
        btnLogout.setFocusPainted(false);

        rightTop.add(lblAdminBadge);
        rightTop.add(btnLogout);

        topBar.add(lblTitle, BorderLayout.WEST);
        topBar.add(rightTop, BorderLayout.EAST);
        rootPanel.add(topBar, BorderLayout.NORTH);

        // --- Center: Tabbed Panes ---
        tabbedPane.addTab("📦 Products & Catalog", createProductsPanel());
        tabbedPane.addTab("🛒 Customer Orders", createOrdersPanel());
        tabbedPane.addTab("🏷️ Categories", createCategoriesPanel());
        tabbedPane.addTab("👥 Customer Registry", createUsersPanel());
        tabbedPane.addTab("ℹ️ Architecture & Look-and-Feel", createInfoPanel());

        rootPanel.add(tabbedPane, BorderLayout.CENTER);

        // --- South: Status Bar ---
        JPanel statusBar = new JPanel(new BorderLayout());
        statusBar.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        JLabel lblStatus = new JLabel("Ready. Connected to ShopSphere Enterprise Catalog.");
        lblStatus.setFont(new Font("SansSerif", Font.ITALIC, 11));
        statusBar.add(lblStatus, BorderLayout.WEST);

        JLabel lblSyllabus = new JLabel("RTU Advanced Java: Swing, Event Handling, MVC, Pluggable L&F");
        lblSyllabus.setFont(new Font("SansSerif", Font.PLAIN, 11));
        statusBar.add(lblSyllabus, BorderLayout.EAST);

        rootPanel.add(statusBar, BorderLayout.SOUTH);

        setContentPane(rootPanel);
    }

    private JPanel createProductsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Table in ScrollPane
        productTable.setRowHeight(24);
        productTable.getSelectionModel().addListSelectionListener(e -> {
            int selectedRow = productTable.getSelectedRow();
            if (selectedRow >= 0) {
                txtProdId.setText(productTableModel.getValueAt(selectedRow, 0).toString());
                txtProdName.setText(productTableModel.getValueAt(selectedRow, 1).toString());
                txtProdPrice.setText(productTableModel.getValueAt(selectedRow, 3).toString());
                txtProdStock.setText(productTableModel.getValueAt(selectedRow, 4).toString());
                chkProdActive.setSelected(Boolean.parseBoolean(productTableModel.getValueAt(selectedRow, 5).toString()));
            }
        });
        panel.add(new JScrollPane(productTable), BorderLayout.CENTER);

        // Form at South
        JPanel formBox = new JPanel(new BorderLayout(5, 5));
        formBox.setBorder(BorderFactory.createTitledBorder("Product Details & Maintenance (MVC View)"));

        JPanel fieldsGrid = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        txtProdId.setEditable(false);
        txtProdId.setBackground(new Color(240, 240, 240));

        fieldsGrid.add(new JLabel("ID:"));
        fieldsGrid.add(txtProdId);
        fieldsGrid.add(new JLabel("Name:"));
        fieldsGrid.add(txtProdName);
        fieldsGrid.add(new JLabel("Category:"));
        fieldsGrid.add(cmbProdCategory);
        fieldsGrid.add(new JLabel("Price ($):"));
        fieldsGrid.add(txtProdPrice);
        fieldsGrid.add(new JLabel("Stock:"));
        fieldsGrid.add(txtProdStock);
        fieldsGrid.add(chkProdActive);

        formBox.add(fieldsGrid, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        btnPanel.add(btnRefreshProducts);
        btnPanel.add(btnAddProduct);
        btnPanel.add(btnUpdateProduct);
        btnPanel.add(btnDeleteProduct);
        formBox.add(btnPanel, BorderLayout.SOUTH);

        panel.add(formBox, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel createOrdersPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        orderTable.setRowHeight(24);
        panel.add(new JScrollPane(orderTable), BorderLayout.CENTER);

        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 8));
        actionPanel.setBorder(BorderFactory.createTitledBorder("Order Lifecycle Control"));
        actionPanel.add(new JLabel("Change Selected Order Status to:"));
        actionPanel.add(cmbOrderStatus);
        actionPanel.add(btnUpdateOrderStatus);
        actionPanel.add(btnRefreshOrders);

        panel.add(actionPanel, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel createCategoriesPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        categoryTable.setRowHeight(24);
        panel.add(new JScrollPane(categoryTable), BorderLayout.CENTER);

        JPanel form = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));
        form.setBorder(BorderFactory.createTitledBorder("Add New Category"));
        form.add(new JLabel("Name:"));
        form.add(txtCatName);
        form.add(new JLabel("Description:"));
        form.add(txtCatDesc);
        form.add(btnAddCategory);

        panel.add(form, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel createUsersPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        userTable.setRowHeight(24);
        panel.add(new JScrollPane(userTable), BorderLayout.CENTER);

        JPanel bar = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bar.add(btnRefreshUsers);
        panel.add(bar, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createInfoPanel() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Look and Feel group
        JPanel lafPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        lafPanel.setBorder(BorderFactory.createTitledBorder("Pluggable Look and Feel (Syllabus Demonstration)"));
        ButtonGroup bg = new ButtonGroup();
        bg.add(rbSystemLaf);
        bg.add(rbMetalLaf);
        lafPanel.add(new JLabel("Select Look & Feel Theme: "));
        lafPanel.add(rbSystemLaf);
        lafPanel.add(rbMetalLaf);

        rbSystemLaf.addActionListener(e -> setLaf(UIManager.getSystemLookAndFeelClassName()));
        rbMetalLaf.addActionListener(e -> setLaf(UIManager.getCrossPlatformLookAndFeelClassName()));

        panel.add(lafPanel, BorderLayout.NORTH);

        // Architecture description
        JTextArea txtInfo = new JTextArea();
        txtInfo.setEditable(false);
        txtInfo.setFont(new Font("Monospaced", Font.PLAIN, 12));
        txtInfo.setText(
                "========================================================================\n" +
                "  ShopSphere Enterprise Swing Admin Module - Architecture Overview\n" +
                "========================================================================\n\n" +
                "1. Architectural Pattern: Model-View-Controller (MVC)\n" +
                "   • Model:      AdminProduct, AdminOrder, AdminCategory, AdminUser\n" +
                "   • View:       AdminDashboardFrame, LoginFrame\n" +
                "   • Controller: AdminSwingController\n" +
                "   • DAO:        SwingProductDAO, SwingOrderDAO, SwingUserDAO, SwingCategoryDAO\n\n" +
                "2. RTU Syllabus Swing Components Demonstrated:\n" +
                "   • Containers:       JFrame, JPanel, JScrollPane, JTabbedPane\n" +
                "   • Controls:         JLabel, JTextField, JPasswordField, JButton\n" +
                "   • State Controls:   JCheckBox, JRadioButton, ButtonGroup\n" +
                "   • Selection/Data:   JComboBox, JTable, DefaultTableModel\n" +
                "   • Dialogs:          JOptionPane (Alert, Confirm, Error, Information)\n" +
                "   • Look and Feel:    Pluggable Look & Feel via UIManager\n\n" +
                "3. Multithreading:\n" +
                "   • GUI operations run exclusively on the Swing Event Dispatch Thread (EDT)\n" +
                "     via SwingUtilities.invokeLater().\n"
        );
        panel.add(new JScrollPane(txtInfo), BorderLayout.CENTER);
        return panel;
    }

    private void setLaf(String lafClassName) {
        try {
            UIManager.setLookAndFeel(lafClassName);
            SwingUtilities.updateComponentTreeUI(this);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Failed to switch Look & Feel: " + ex.getMessage());
        }
    }

    // --- Data Population Methods ---
    public void setProductList(List<AdminProduct> products) {
        productTableModel.setRowCount(0);
        for (AdminProduct p : products) {
            productTableModel.addRow(new Object[]{
                    p.getId(), p.getName(), p.getCategory(),
                    String.format("%.2f", p.getPrice()), p.getStock(), p.isActive()
            });
        }
    }

    public void setOrderList(List<AdminOrder> orders) {
        orderTableModel.setRowCount(0);
        for (AdminOrder o : orders) {
            orderTableModel.addRow(new Object[]{
                    o.getOrderId(), o.getOrderNumber(), o.getCustomerEmail(),
                    String.format("%.2f", o.getTotalAmount()), o.getStatus(), o.getOrderDate()
            });
        }
    }

    public void setCategoryList(List<AdminCategory> categories) {
        categoryTableModel.setRowCount(0);
        cmbProdCategory.removeAllItems();
        for (AdminCategory c : categories) {
            categoryTableModel.addRow(new Object[]{c.getCategoryId(), c.getName(), c.getDescription()});
            cmbProdCategory.addItem(c);
        }
    }

    public void setUserList(List<AdminUser> users) {
        userTableModel.setRowCount(0);
        for (AdminUser u : users) {
            userTableModel.addRow(new Object[]{
                    u.getUserId(), u.getFullName(), u.getEmail(), u.getRole(), u.getCreatedAt()
            });
        }
    }

    // --- Getters for Form Inputs ---
    public int getSelectedProductId() {
        int row = productTable.getSelectedRow();
        if (row < 0) return -1;
        return (int) productTableModel.getValueAt(row, 0);
    }

    public int getSelectedOrderId() {
        int row = orderTable.getSelectedRow();
        if (row < 0) return -1;
        return (int) orderTableModel.getValueAt(row, 0);
    }

    public String getProdNameInput() { return txtProdName.getText().trim(); }
    public String getProdPriceInput() { return txtProdPrice.getText().trim(); }
    public String getProdStockInput() { return txtProdStock.getText().trim(); }
    public AdminCategory getSelectedCategoryInput() { return (AdminCategory) cmbProdCategory.getSelectedItem(); }
    public boolean getProdActiveInput() { return chkProdActive.isSelected(); }
    public String getSelectedOrderStatus() { return (String) cmbOrderStatus.getSelectedItem(); }
    public String getCatNameInput() { return txtCatName.getText().trim(); }
    public String getCatDescInput() { return txtCatDesc.getText().trim(); }

    public void clearProductInputs() {
        txtProdId.setText("");
        txtProdName.setText("");
        txtProdPrice.setText("");
        txtProdStock.setText("");
        chkProdActive.setSelected(true);
        productTable.clearSelection();
    }

    public void clearCategoryInputs() {
        txtCatName.setText("");
        txtCatDesc.setText("");
    }

    // --- Listeners Binding ---
    public void addLogoutListener(ActionListener l) { btnLogout.addActionListener(l); }
    public void addProductAddListener(ActionListener l) { btnAddProduct.addActionListener(l); }
    public void addProductUpdateListener(ActionListener l) { btnUpdateProduct.addActionListener(l); }
    public void addProductDeleteListener(ActionListener l) { btnDeleteProduct.addActionListener(l); }
    public void addProductRefreshListener(ActionListener l) { btnRefreshProducts.addActionListener(l); }
    public void addOrderStatusUpdateListener(ActionListener l) { btnUpdateOrderStatus.addActionListener(l); }
    public void addOrderRefreshListener(ActionListener l) { btnRefreshOrders.addActionListener(l); }
    public void addCategoryAddListener(ActionListener l) { btnAddCategory.addActionListener(l); }
    public void addUserRefreshListener(ActionListener l) { btnRefreshUsers.addActionListener(l); }

    public void showMessage(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Information", JOptionPane.INFORMATION_MESSAGE);
    }

    public void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
