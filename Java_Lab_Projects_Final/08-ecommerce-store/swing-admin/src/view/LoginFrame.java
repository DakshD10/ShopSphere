package view;

import controller.AdminSwingController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * LoginFrame: Swing View for Administrator Authentication.
 * Demonstrates JFrame, JPanel, JLabel, JTextField, JPasswordField, JButton, and JOptionPane.
 */
public class LoginFrame extends JFrame {
    private final JTextField txtEmail = new JTextField("admin@shopsphere.com", 20);
    private final JPasswordField txtPassword = new JPasswordField("admin123", 20);
    private final JButton btnLogin = new JButton("Login to Portal");
    private final JButton btnReset = new JButton("Reset Fields");
    private final JLabel lblStatus = new JLabel(" ");

    public LoginFrame() {
        setTitle("ShopSphere Desktop Portal - Admin Login");
        setSize(460, 320);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        initComponents();
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

        // Header Banner
        JPanel headerPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        JLabel lblTitle = new JLabel("ShopSphere Desktop Admin", JLabel.CENTER);
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 18));
        lblTitle.setForeground(new Color(24, 76, 120));

        JLabel lblSubtitle = new JLabel("Enterprise Java Swing MVC Client", JLabel.CENTER);
        lblSubtitle.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblSubtitle.setForeground(Color.GRAY);

        headerPanel.add(lblTitle);
        headerPanel.add(lblSubtitle);
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Form Grid
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Admin Email:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0;
        formPanel.add(txtEmail, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1;
        formPanel.add(txtPassword, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        lblStatus.setForeground(new Color(180, 40, 40));
        lblStatus.setHorizontalAlignment(SwingConstants.CENTER);
        formPanel.add(lblStatus, gbc);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        btnLogin.setBackground(new Color(24, 76, 120));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFocusPainted(false);
        btnLogin.setFont(new Font("SansSerif", Font.BOLD, 13));

        buttonPanel.add(btnLogin);
        buttonPanel.add(btnReset);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        btnReset.addActionListener(e -> {
            txtEmail.setText("");
            txtPassword.setText("");
            lblStatus.setText(" ");
        });

        setContentPane(mainPanel);
    }

    public String getEmail() { return txtEmail.getText().trim(); }
    public String getPassword() { return new String(txtPassword.getPassword()); }

    public void setStatusMessage(String msg) {
        lblStatus.setText(msg);
    }

    public void addLoginListener(ActionListener listener) {
        btnLogin.addActionListener(listener);
        txtPassword.addActionListener(listener);
    }

    public void showErrorMessage(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Authentication Error", JOptionPane.ERROR_MESSAGE);
    }
}
