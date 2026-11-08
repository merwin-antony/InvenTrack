package com.inventory.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Manages database connection properties and establishes JDBC connections to MySQL.
 * 
 */
public class DatabaseConfig {

    private static final String CONFIG_FILE_NAME = "config.properties";

    private static String dbUrl = "jdbc:mysql://localhost:3306/inventory_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static String dbUser = "root";
    private static String dbPassword = "YOUR_MYSQL_PASSWORD";
    private static int lowStockThreshold = 5;

    private static boolean initialized = false;

    // Load configuration upon class loading
    static {
        loadConfiguration();
    }

    /**
     * Loads database connection settings from config.properties file if present.
     * Falls back to default values if the file is missing or contains empty properties.
     */
    public static synchronized void loadConfiguration() {
        Properties props = new Properties();
        File configFile = new File(CONFIG_FILE_NAME);

        if (configFile.exists() && configFile.isFile()) {
            try (InputStream input = new FileInputStream(configFile)) {
                props.load(input);
                if (props.getProperty("db.url") != null && !props.getProperty("db.url").trim().isEmpty()) {
                    dbUrl = props.getProperty("db.url").trim();
                }
                if (props.getProperty("db.username") != null) {
                    dbUser = props.getProperty("db.username").trim();
                }
                if (props.getProperty("db.password") != null) {
                    dbPassword = props.getProperty("db.password").trim();
                }
                if (props.getProperty("inventory.low_stock_threshold") != null) {
                    try {
                        lowStockThreshold = Integer.parseInt(props.getProperty("inventory.low_stock_threshold").trim());
                    } catch (NumberFormatException ignored) {}
                }
            } catch (IOException e) {
                System.err.println("[DatabaseConfig] Warning: Could not read " + CONFIG_FILE_NAME + ". Using defaults.");
            }
        } else {
            System.out.println("[DatabaseConfig] " + CONFIG_FILE_NAME + " not found in working directory. Using defaults.");
        }

        // Register MySQL JDBC Driver
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            initialized = true;
        } catch (ClassNotFoundException e) {
            System.err.println("[DatabaseConfig] Error: MySQL Connector/J driver class not found in classpath.");
        }
    }

    /**
     * Creates and returns a fresh JDBC Connection.
     * The calling code MUST close this connection (preferably with try-with-resources).
     *
     * @return active java.sql.Connection
     * @throws SQLException if a database access error occurs
     */
    public static Connection getConnection() throws SQLException {
        if (!initialized) {
            loadConfiguration();
        }
        return DriverManager.getConnection(dbUrl, dbUser, dbPassword);
    }

    /**
     * Tests the database connection and returns a diagnostic result.
     */
    public static ConnectionTestResult testConnection() {
        try (Connection conn = getConnection()) {
            DatabaseMetaData meta = conn.getMetaData();
            String info = String.format("Connected to %s (Version: %s) as user '%s'",
                    meta.getDatabaseProductVersion(),
                    meta.getDatabaseProductName(),
                    dbUser);
            return new ConnectionTestResult(true, info);
        } catch (SQLException e) {
            String errorMsg;
            int errorCode = e.getErrorCode();
            if (errorCode == 1045) {
                errorMsg = "Access Denied: Invalid MySQL username or password in config.properties.";
            } else if (errorCode == 1049) {
                errorMsg = "Database 'inventory_db' does not exist. Please create it in MySQL Workbench first.";
            } else if (e.getMessage() != null && e.getMessage().contains("Communications link failure")) {
                errorMsg = "Communications link failure: MySQL Server is not running or port 3306 is not accessible.";
            } else {
                errorMsg = "Database error (Code " + errorCode + "): " + e.getMessage();
            }
            return new ConnectionTestResult(false, errorMsg);
        }
    }

    // Getters
    public static String getDbUrl() { return dbUrl; }
    public static String getDbUser() { return dbUser; }
    public static int getLowStockThreshold() { return lowStockThreshold; }

    /**
     * Helper record/class for testing connection health
     */
    public static class ConnectionTestResult {
        private final boolean successful;
        private final String message;

        public ConnectionTestResult(boolean successful, String message) {
            this.successful = successful;
            this.message = message;
        }

        public boolean isSuccessful() { return successful; }
        public String getMessage() { return message; }

        @Override
        public String toString() {
            return (successful ? "[SUCCESS] " : "[FAILURE] ") + message;
        }
    }
}
