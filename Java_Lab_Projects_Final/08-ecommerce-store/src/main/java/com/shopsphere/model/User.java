package com.shopsphere.model;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Represents a registered system user (Customer or Administrator).
 */
public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    private int userId;
    private String name;
    private String email;
    private String password;
    private String mobile;
    private String role;     // "CUSTOMER" or "ADMIN"
    private String status;   // "ACTIVE", "INACTIVE", "SUSPENDED"
    private Timestamp createdAt;

    public User() {
        this.role = "CUSTOMER";
        this.status = "ACTIVE";
    }

    public User(int userId, String name, String email, String password, String mobile, String role, String status) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.password = password;
        this.mobile = mobile;
        this.role = role != null ? role : "CUSTOMER";
        this.status = status != null ? status : "ACTIVE";
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isAdmin() {
        return "ADMIN".equalsIgnoreCase(this.role);
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", role='" + role + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
