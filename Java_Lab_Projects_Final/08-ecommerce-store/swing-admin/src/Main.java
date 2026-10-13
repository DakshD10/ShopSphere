import controller.AdminSwingController;
import view.AdminDashboardFrame;
import view.LoginFrame;

import javax.swing.*;

/**
 * Main: Entry point for ShopSphere Desktop Administration Application.
 * Configures Pluggable Look-and-Feel and launches the MVC hierarchy on the EDT.
 */
public class Main {
    public static void main(String[] args) {
        // Step 1: Configure Pluggable Look and Feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("Could not set system look and feel: " + e.getMessage());
        }

        // Step 2: Launch on Swing Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(() -> {
            LoginFrame loginFrame = new LoginFrame();
            AdminDashboardFrame dashboardFrame = new AdminDashboardFrame();
            AdminSwingController controller = new AdminSwingController(loginFrame, dashboardFrame);
            controller.start();
        });
    }
}
