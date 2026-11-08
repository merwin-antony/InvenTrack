package com.inventory.config;

import com.inventory.dao.UserDAO;
import com.inventory.model.User;
import com.inventory.service.PasswordHasher;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Bootstraps the application database environment safely.
 *
 */
public class DatabaseInitializer {

    public static final String DEFAULT_ADMIN_USERNAME = "Admin";
    public static final String DEFAULT_ADMIN_PASSWORD = "admin@123";
    public static final String DEFAULT_ADMIN_FULLNAME = "System Administrator";

    /**
     * Runs database initialization checks.
     * 1. Creates 'users' table if not exists.
     * 2. Checks if an admin account exists; if none exists, creates the default admin.
     *
     * @return true if initialization succeeded, false otherwise
     */
    public static boolean initialize() {
        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement()) {

            // Step 1: Create 'users' table if missing (leaves 'products' table completely untouched)
            String createUsersTableSql =
                    "CREATE TABLE IF NOT EXISTS users (" +
                    "  user_id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "  username VARCHAR(50) NOT NULL UNIQUE, " +
                    "  password_hash VARCHAR(255) NOT NULL, " +
                    "  salt VARCHAR(64) NOT NULL, " +
                    "  full_name VARCHAR(100) NOT NULL, " +
                    "  role VARCHAR(20) NOT NULL DEFAULT 'ADMIN', " +
                    "  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                    ")";

            stmt.executeUpdate(createUsersTableSql);

            // Step 2: Check if any user accounts exist
            UserDAO userDAO = new UserDAO();
            int userCount = userDAO.countUsers();

            if (userCount == 0) {
                System.out.println("[DatabaseInitializer] No existing users found. Seeding default administrator account...");

                String salt = PasswordHasher.generateSalt();
                String hash = PasswordHasher.hashPassword(DEFAULT_ADMIN_PASSWORD.toCharArray(), salt);

                User defaultAdmin = new User(
                        DEFAULT_ADMIN_USERNAME,
                        hash,
                        salt,
                        DEFAULT_ADMIN_FULLNAME,
                        "ADMIN"
                );

                userDAO.createUser(defaultAdmin);
                System.out.println("[DatabaseInitializer] Default Administrator initialized successfully!");
                System.out.println("  Username: " + DEFAULT_ADMIN_USERNAME);
                System.out.println("  Password: " + DEFAULT_ADMIN_PASSWORD);
            }

            return true;
        } catch (SQLException e) {
            System.err.println("[DatabaseInitializer] Initialization failed: " + e.getMessage());
            return false;
        }
    }
}
