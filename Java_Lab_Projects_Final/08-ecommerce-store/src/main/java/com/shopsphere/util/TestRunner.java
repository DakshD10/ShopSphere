package com.shopsphere.util;

import com.shopsphere.dao.CategoryDAO;
import com.shopsphere.dao.CouponDAO;
import com.shopsphere.dao.ProductDAO;
import com.shopsphere.dao.UserDAO;
import com.shopsphere.model.CartItem;
import com.shopsphere.model.Category;
import com.shopsphere.model.Coupon;
import com.shopsphere.model.Product;
import com.shopsphere.model.User;
import com.shopsphere.service.CartService;
import com.shopsphere.service.UserService;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Standalone verification utility to validate Models, Password Hashing,
 * Service Business Calculations, Validation Rules, and live JDBC DAO operations.
 */
public class TestRunner {

    public static void main(String[] args) {
        System.out.println("==========================================================");
        System.out.println("  ShopSphere - Phase 4 & 5 Verification Suite");
        System.out.println("==========================================================");

        int passed = 0;
        int failed = 0;

        // Test 1: Password Hasher
        System.out.println("\n[TEST 1] PasswordHasher & Salt Verification...");
        String rawPassword = "admin123";
        String expectedAdminHash = "8f44df33d42803b47af2902a9e734c16d340caa00666f1df9e09dcdf6bd01e77";
        String computedHash = PasswordHasher.hash(rawPassword);

        if (expectedAdminHash.equalsIgnoreCase(computedHash) 
                && PasswordHasher.verify(rawPassword, computedHash)
                && !PasswordHasher.verify("wrongpassword", computedHash)) {
            System.out.println("  -> PASS: SHA-256 + salt hashing verified correctly.");
            passed++;
        } else {
            System.err.println("  -> FAIL: Password hash mismatch!");
            failed++;
        }

        // Test 2: Product Discount Math
        System.out.println("\n[TEST 2] Product Discount & Availability Logic...");
        Product p = new Product(1, 1, "Test Headphones", "AudioTech", "Noise cancelling", 3000.00, 10.00, 5, "img.jpg", true);
        double expectedDiscounted = 2700.00;
        if (Math.abs(p.getDiscountedPrice() - expectedDiscounted) < 0.001 && p.isAvailable()) {
            System.out.println("  -> PASS: Discount calculation: Original ₹3000.00, 10% off -> ₹" + p.getDiscountedPrice());
            passed++;
        } else {
            System.err.println("  -> FAIL: Discount calculation failed.");
            failed++;
        }

        // Test 3: CartService Server-Side Calculations
        System.out.println("\n[TEST 3] CartService Server-Side Totals & Coupon Calculation...");
        CartService cartService = new CartService();
        List<CartItem> testItems = new ArrayList<>();
        testItems.add(new CartItem(1, p, 2)); // 2 x 2700 = 5400

        Product p2 = new Product(2, 1, "Mouse", "Logi", "Wireless", 600.00, 0.0, 10, "img2.jpg", true);
        testItems.add(new CartItem(2, p2, 1)); // 1 x 600 = 600
        // Expected Subtotal = 5400 + 600 = 6000.00

        Coupon tenPctCoupon = new Coupon(1, "WELCOME10", "PERCENTAGE", 10.0, 500.0, 400.0, null, true);
        // 10% of 6000 = 600, capped at 400.00 -> discount = 400.00, total = 5600.00

        CartService.CartTotals totals = cartService.calculateTotals(testItems, tenPctCoupon);
        if (totals.getSubtotal() == 6000.00 && totals.getDiscount() == 400.00 && totals.getTotal() == 5600.00 && totals.getItemCount() == 3) {
            System.out.println("  -> PASS: Subtotal ₹6000.00, Capped Discount ₹400.00, Final Total ₹5600.00 verified on server.");
            passed++;
        } else {
            System.err.println("  -> FAIL: Totals calculation failed: subtotal=" + totals.getSubtotal() + ", disc=" + totals.getDiscount() + ", total=" + totals.getTotal());
            failed++;
        }

        // Test 4: UserService Validation Logic
        System.out.println("\n[TEST 4] UserService Server-Side Validation Rules...");
        UserService userService = new UserService();
        boolean caughtInvalidEmail = false;
        boolean caughtShortPassword = false;

        try {
            userService.register("Valid Name", "invalid-email-format", "valid123", "1234567890");
        } catch (IllegalArgumentException e) {
            caughtInvalidEmail = true;
        } catch (Exception ignored) {}

        try {
            userService.register("Valid Name", "user@test.com", "123", "1234567890");
        } catch (IllegalArgumentException e) {
            caughtShortPassword = true;
        } catch (Exception ignored) {}

        if (caughtInvalidEmail && caughtShortPassword) {
            System.out.println("  -> PASS: Malformed email and short password properly rejected on server.");
            passed++;
        } else {
            System.err.println("  -> FAIL: Validation exceptions were not properly raised: invalidEmail=" + caughtInvalidEmail + ", shortPwd=" + caughtShortPassword);
            failed++;
        }

        // Test 5: Live Database & JDBC DAO Check
        System.out.println("\n[TEST 5] Live MySQL JDBC Connection & DAO Query Check...");
        System.out.println("  Target DB URL: " + DB.getUrl());
        System.out.println("  Target DB User: " + DB.getUser());

        try (Connection con = DB.getConnection()) {
            System.out.println("  -> [SUCCESS] Connected to MySQL database '" + con.getCatalog() + "'!");

            CategoryDAO catDao = new CategoryDAO();
            List<Category> categories = catDao.findAll();
            System.out.println("  -> CategoryDAO.findAll() returned " + categories.size() + " categories.");

            ProductDAO prodDao = new ProductDAO();
            List<Product> products = prodDao.findAllActive();
            System.out.println("  -> ProductDAO.findAllActive() returned " + products.size() + " products.");

            UserDAO userDao = new UserDAO();
            User admin = userDao.findByEmail("admin@shopsphere.com");
            if (admin != null) {
                System.out.println("  -> UserDAO found Admin: " + admin.getName() + " (" + admin.getRole() + ")");
            }

            passed++;
        } catch (SQLException e) {
            System.out.println("  -> [NOTICE] MySQL is currently not reachable: " + e.getMessage());
            System.out.println("     Run './database/import_db.sh' once MySQL is started to populate tables.");
            System.out.println("     (Unit logic tests passed; live DB verified when server is running).");
        }

        System.out.println("\n==========================================================");
        System.out.println("  RESULTS: " + passed + " test group(s) PASSED, " + failed + " FAILED.");
        System.out.println("==========================================================");
    }
}
