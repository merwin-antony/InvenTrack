package com.inventory.ui;

import com.inventory.model.User;
import com.inventory.service.AuthService;
import com.inventory.service.ValidationException;
import com.inventory.ui.theme.CustomComponents;
import com.inventory.ui.theme.UITheme;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.sql.SQLException;

/**
 * Modern dialog for registering a new user / administrator in InvenTrack.
 */
public class SignUpDialog extends JDialog {

    private final AuthService authService;
    private final Frame parentFrame;

    private JTextField usernameField;
    private JTextField fullNameField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JComboBox<String> roleCombo;
    private JLabel errorLabel;
    private JButton submitButton;
    private boolean registeredSuccessfully = false;

    public SignUpDialog(Frame owner, AuthService authService) {
        super(owner, "Create New Account — InvenTrack", true);
        this.parentFrame = owner;
        this.authService = authService;

        initializeUI();
    }

    private void initializeUI() {
        setSize(480, 600);
        setResizable(false);
        setLocationRelativeTo(parentFrame);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(UITheme.CARD_BG);
        mainPanel.setBorder(new EmptyBorder(28, 36, 28, 36));

        // Header Title
        JLabel titleLabel = new JLabel("Create Account");
        titleLabel.setFont(UITheme.FONT_TITLE_LARGE);
        titleLabel.setForeground(UITheme.TEXT_PRIMARY);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subtitleLabel = new JLabel("Register a new user to access the inventory system");
        subtitleLabel.setFont(UITheme.FONT_BODY);
        subtitleLabel.setForeground(UITheme.TEXT_MUTED);
        subtitleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Error Feedback Label
        errorLabel = new JLabel(" ");
        errorLabel.setFont(UITheme.FONT_SMALL_BOLD);
        errorLabel.setForeground(UITheme.DANGER);
        errorLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Full Name
        JLabel nameLabel = createFieldLabel("Full Name *");
        fullNameField = createStyledTextField();

        // Username
        JLabel userLabel = createFieldLabel("Username *");
        usernameField = createStyledTextField();

        // Password
        JLabel passLabel = createFieldLabel("Password *");
        passwordField = createStyledPasswordField();

        // Confirm Password
        JLabel confirmPassLabel = createFieldLabel("Confirm Password *");
        confirmPasswordField = createStyledPasswordField();

        // Role Selection
        JLabel roleLabel = createFieldLabel("Role *");
        roleCombo = new JComboBox<>(new String[]{"ADMIN", "MANAGER", "STAFF"});
        roleCombo.setFont(UITheme.FONT_BODY);
        roleCombo.setPreferredSize(new Dimension(380, 38));
        roleCombo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        roleCombo.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Buttons
        JPanel buttonRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonRow.setOpaque(false);
        buttonRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        buttonRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));

        JButton cancelButton = CustomComponents.createSecondaryButton("Cancel", null);
        cancelButton.addActionListener(e -> dispose());

        submitButton = CustomComponents.createPrimaryButton("Create Account", "✓");
        submitButton.addActionListener(e -> performRegistration());

        buttonRow.add(cancelButton);
        buttonRow.add(submitButton);

        // Assemble
        mainPanel.add(titleLabel);
        mainPanel.add(Box.createVerticalStrut(4));
        mainPanel.add(subtitleLabel);
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(errorLabel);
        mainPanel.add(Box.createVerticalStrut(6));

        mainPanel.add(nameLabel);
        mainPanel.add(Box.createVerticalStrut(4));
        mainPanel.add(fullNameField);
        mainPanel.add(Box.createVerticalStrut(10));

        mainPanel.add(userLabel);
        mainPanel.add(Box.createVerticalStrut(4));
        mainPanel.add(usernameField);
        mainPanel.add(Box.createVerticalStrut(10));

        mainPanel.add(passLabel);
        mainPanel.add(Box.createVerticalStrut(4));
        mainPanel.add(passwordField);
        mainPanel.add(Box.createVerticalStrut(10));

        mainPanel.add(confirmPassLabel);
        mainPanel.add(Box.createVerticalStrut(4));
        mainPanel.add(confirmPasswordField);
        mainPanel.add(Box.createVerticalStrut(10));

        mainPanel.add(roleLabel);
        mainPanel.add(Box.createVerticalStrut(4));
        mainPanel.add(roleCombo);
        mainPanel.add(Box.createVerticalStrut(20));

        mainPanel.add(buttonRow);

        setContentPane(mainPanel);
    }

    private JLabel createFieldLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(UITheme.FONT_BOLD);
        label.setForeground(UITheme.TEXT_SECONDARY);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    private JTextField createStyledTextField() {
        JTextField field = new JTextField();
        field.setFont(UITheme.FONT_BODY);
        field.setPreferredSize(new Dimension(380, 38));
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        field.setBorder(new CompoundBorder(
                new LineBorder(UITheme.CARD_BORDER, 1, true),
                new EmptyBorder(6, 12, 6, 12)
        ));
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        return field;
    }

    private JPasswordField createStyledPasswordField() {
        JPasswordField field = new JPasswordField();
        field.setFont(UITheme.FONT_BODY);
        field.setPreferredSize(new Dimension(380, 38));
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        field.setBorder(new CompoundBorder(
                new LineBorder(UITheme.CARD_BORDER, 1, true),
                new EmptyBorder(6, 12, 6, 12)
        ));
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        return field;
    }

    private void performRegistration() {
        String fullName = fullNameField.getText().trim();
        String username = usernameField.getText().trim();
        char[] password = passwordField.getPassword();
        char[] confirmPass = confirmPasswordField.getPassword();
        String role = (String) roleCombo.getSelectedItem();

        errorLabel.setText(" ");

        if (fullName.isEmpty()) {
            showError("Please enter full name.");
            fullNameField.requestFocus();
            return;
        }

        if (username.isEmpty()) {
            showError("Please enter a username.");
            usernameField.requestFocus();
            return;
        }

        if (password.length < 4) {
            showError("Password must be at least 4 characters.");
            passwordField.requestFocus();
            return;
        }

        if (password.length != confirmPass.length) {
            showError("Passwords do not match.");
            confirmPasswordField.requestFocus();
            return;
        }

        boolean match = true;
        for (int i = 0; i < password.length; i++) {
            if (password[i] != confirmPass[i]) {
                match = false;
                break;
            }
        }

        if (!match) {
            showError("Passwords do not match.");
            confirmPasswordField.requestFocus();
            return;
        }

        submitButton.setEnabled(false);
        submitButton.setText("Creating...");

        SwingWorker<User, Void> worker = new SwingWorker<>() {
            @Override
            protected User doInBackground() throws Exception {
                return authService.registerUser(username, password, fullName, role);
            }

            @Override
            protected void done() {
                submitButton.setEnabled(true);
                submitButton.setText("Create Account");

                try {
                    get();
                    registeredSuccessfully = true;
                    JOptionPane.showMessageDialog(
                            SignUpDialog.this,
                            "Account '" + username + "' created successfully!\nYou can now log in.",
                            "Registration Successful",
                            JOptionPane.INFORMATION_MESSAGE
                    );
                    dispose();
                } catch (Exception ex) {
                    Throwable cause = ex.getCause() != null ? ex.getCause() : ex;
                    if (cause instanceof ValidationException) {
                        showError(cause.getMessage());
                    } else if (cause instanceof SQLException) {
                        showError("Database error during registration: " + cause.getMessage());
                    } else {
                        showError("Registration failed: " + cause.getMessage());
                    }
                }
            }
        };
        worker.execute();
    }

    private void showError(String msg) {
        errorLabel.setText(msg);
    }

    public boolean isRegisteredSuccessfully() {
        return registeredSuccessfully;
    }

    public String getRegisteredUsername() {
        return usernameField.getText().trim();
    }
}
