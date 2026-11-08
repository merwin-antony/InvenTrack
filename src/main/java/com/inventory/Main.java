package com.inventory;

import com.inventory.config.DatabaseConfig;
import com.inventory.config.DatabaseInitializer;
import com.inventory.ui.LoginFrame;
import com.inventory.ui.theme.UITheme;

import javax.swing.*;

/**
 * Main application entry point for the Inventory Management Desktop System.
 */
public class Main {

    public static void main(String[] args) {
        System.out.println("=================================================================");
        System.out.println("  InvenTrack — Desktop Inventory Management System");
        System.out.println("=================================================================");

        // Step 1: Initialize Modern Look & Feel (FlatLaf)
        UITheme.setupLookAndFeel();

        // Step 2: Load external configuration
        DatabaseConfig.loadConfiguration();

        // Step 3: Run database schema bootstrap (creates 'users' table and initial admin if needed)
        try {
            boolean initialized = DatabaseInitializer.initialize();
            if (initialized) {
                System.out.println("[Main] Database initialization completed successfully.");
            } else {
                System.out.println("[Main] Notice: Database could not be initialized automatically. Please check MySQL server status.");
            }
        } catch (Exception e) {
            System.err.println("[Main] Warning: Could not reach MySQL during startup: " + e.getMessage());
        }

        // Step 4: Launch GUI on the Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(() -> {
            try {
                LoginFrame loginFrame = new LoginFrame();
                loginFrame.setVisible(true);
            } catch (Exception e) {
                System.err.println("[Main] Fatal error creating GUI: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }
}
