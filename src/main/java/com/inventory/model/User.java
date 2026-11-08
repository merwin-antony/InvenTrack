package com.inventory.model;

import java.sql.Timestamp;

/**
 * User Entity POJO representing an application user account.
 * 
 * Schema in 'users' table:
 * - user_id INT AUTO_INCREMENT PRIMARY KEY
 * - username VARCHAR(50) NOT NULL UNIQUE
 * - password_hash VARCHAR(255) NOT NULL
 * - salt VARCHAR(64) NOT NULL
 * - full_name VARCHAR(100) NOT NULL
 * - role VARCHAR(20) NOT NULL DEFAULT 'ADMIN'
 * - created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
 */
public class User {

    private Integer userId;
    private String username;
    private String passwordHash;
    private String salt;
    private String fullName;
    private String role;
    private Timestamp createdAt;

    public User() {
        this.role = "ADMIN";
    }

    public User(String username, String passwordHash, String salt, String fullName, String role) {
        this(null, username, passwordHash, salt, fullName, role, null);
    }

    public User(Integer userId, String username, String passwordHash, String salt, String fullName, String role, Timestamp createdAt) {
        this.userId = userId;
        this.username = username;
        this.passwordHash = passwordHash;
        this.salt = salt;
        this.fullName = fullName;
        this.role = role != null ? role : "ADMIN";
        this.createdAt = createdAt;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", fullName='" + fullName + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
