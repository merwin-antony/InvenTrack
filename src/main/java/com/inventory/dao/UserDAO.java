package com.inventory.dao;

import com.inventory.config.DatabaseConfig;
import com.inventory.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Data Access Object (DAO) for application users.
 * Handles user persistence, retrieval, and administrative lookups.
 */
public class UserDAO {

    /**
     * Finds a user by their unique username (case-insensitive search).
     */
    public Optional<User> findByUsername(String username) throws SQLException {
        if (username == null || username.trim().isEmpty()) {
            return Optional.empty();
        }

        String sql = "SELECT user_id, username, password_hash, salt, full_name, role, created_at " +
                     "FROM users WHERE LOWER(username) = LOWER(?)";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username.trim());

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRowToUser(rs));
                }
            }
        }
        return Optional.empty();
    }

    /**
     * Creates a new user record.
     */
    public int createUser(User user) throws SQLException {
        String sql = "INSERT INTO users (username, password_hash, salt, full_name, role) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, user.getUsername().trim());
            stmt.setString(2, user.getPasswordHash());
            stmt.setString(3, user.getSalt());
            stmt.setString(4, user.getFullName().trim());
            stmt.setString(5, user.getRole() != null ? user.getRole() : "ADMIN");

            stmt.executeUpdate();

            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    int id = keys.getInt(1);
                    user.setUserId(id);
                    return id;
                } else {
                    throw new SQLException("Failed to create user, no ID generated.");
                }
            }
        }
    }

    /**
     * Counts how many user accounts exist in the database.
     */
    public int countUsers() throws SQLException {
        String sql = "SELECT COUNT(*) FROM users";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }

    /**
     * Retrieves all users.
     */
    public List<User> getAllUsers() throws SQLException {
        String sql = "SELECT user_id, username, password_hash, salt, full_name, role, created_at FROM users ORDER BY user_id ASC";
        List<User> list = new ArrayList<>();

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                list.add(mapRowToUser(rs));
            }
        }
        return list;
    }

    private User mapRowToUser(ResultSet rs) throws SQLException {
        return new User(
                rs.getInt("user_id"),
                rs.getString("username"),
                rs.getString("password_hash"),
                rs.getString("salt"),
                rs.getString("full_name"),
                rs.getString("role"),
                rs.getTimestamp("created_at")
        );
    }
}
