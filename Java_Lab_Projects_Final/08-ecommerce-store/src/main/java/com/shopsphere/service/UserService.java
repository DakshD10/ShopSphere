package com.shopsphere.service;

import com.shopsphere.dao.UserDAO;
import com.shopsphere.model.User;
import com.shopsphere.util.PasswordHasher;

import java.sql.SQLException;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Service handling customer and administrator authentication,
 * registration validation, and account profile management.
 */
public class UserService {

    private final UserDAO userDAO;
    private static final Pattern EMAIL_REGEX = 
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    public UserService() {
        this.userDAO = new UserDAO();
    }

    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    /**
     * Registers a new customer with server-side field validation and password hashing.
     */
    public User register(String name, String email, String password, String mobile) throws SQLException {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Full name is required.");
        }
        if (email == null || !EMAIL_REGEX.matcher(email.trim()).matches()) {
            throw new IllegalArgumentException("A valid email address is required.");
        }
        if (password == null || password.trim().length() < 6) {
            throw new IllegalArgumentException("Password must be at least 6 characters long.");
        }

        String cleanEmail = email.trim().toLowerCase();
        if (userDAO.emailExists(cleanEmail)) {
            throw new IllegalArgumentException("An account with this email address already exists.");
        }

        String hashedPassword = PasswordHasher.hash(password.trim());
        User user = new User();
        user.setName(name.trim());
        user.setEmail(cleanEmail);
        user.setPassword(hashedPassword);
        user.setMobile(mobile != null ? mobile.trim() : "");
        user.setRole("CUSTOMER");
        user.setStatus("ACTIVE");

        int userId = userDAO.createUser(user);
        if (userId <= 0) {
            throw new SQLException("Failed to create user record.");
        }
        user.setUserId(userId);
        return user;
    }

    /**
     * Authenticates a user against stored SHA-256 password hash.
     */
    public User login(String email, String password) throws SQLException {
        if (email == null || password == null) {
            return null;
        }

        User user = userDAO.findByEmail(email.trim().toLowerCase());
        if (user == null) {
            return null; // Email not registered
        }

        if (!"ACTIVE".equalsIgnoreCase(user.getStatus())) {
            throw new IllegalStateException("Your account is " + user.getStatus().toLowerCase() + ". Please contact support.");
        }

        if (PasswordHasher.verify(password.trim(), user.getPassword())) {
            return user;
        }

        return null; // Password mismatch
    }

    public User getUserById(int userId) throws SQLException {
        return userDAO.findById(userId);
    }

    public boolean updateProfile(int userId, String name, String mobile) throws SQLException {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty.");
        }
        return userDAO.updateProfile(userId, name.trim(), mobile != null ? mobile.trim() : "");
    }

    public List<User> getAllUsers() throws SQLException {
        return userDAO.findAll();
    }

    public boolean updateUserStatus(int userId, String status) throws SQLException {
        return userDAO.updateStatus(userId, status);
    }
}
