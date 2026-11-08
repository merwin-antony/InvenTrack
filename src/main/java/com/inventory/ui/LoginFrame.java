package com.inventory.ui;

import com.inventory.config.DatabaseConfig;
import com.inventory.config.DatabaseInitializer;
import com.inventory.model.User;
import com.inventory.service.AuthenticationException;
import com.inventory.service.AuthService;
import com.inventory.ui.theme.CustomComponents;
import com.inventory.ui.theme.UITheme;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.SQLException;

/**
 * Modern Widescreen Desktop Login Screen with a Clean Minimalist Hero Showcase.
 */
public class LoginFrame extends JFrame {

    private final AuthService authService;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JLabel errorLabel;
    private JLabel dbStatusLabel;
    private JButton loginButton;

    public LoginFrame() {
        this.authService = new AuthService();
        initializeUI();
        checkDatabaseStatus();
    }

    private void initializeUI() {
        setTitle("InvenTrack — Inventory Management System");
        setIconImage(UITheme.createAppIcon());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1160, 720);
        setMinimumSize(new Dimension(960, 620));
        setResizable(true);
        setLocationRelativeTo(null); // Center on desktop screen

        // Main Container: Split into Left Brand Showcase and Right Sign-in Portal
        JPanel rootPanel = new JPanel(new GridLayout(1, 2));
        rootPanel.setBackground(UITheme.CONTENT_BG);

        // 1. Left Showcase / Hero Panel (Clean, Minimalist branding from design)
        JPanel leftHeroPanel = createHeroPanel();
        rootPanel.add(leftHeroPanel);

        // 2. Right Sign-in Portal Panel
        JPanel rightLoginPanel = createLoginPortalPanel();
        rootPanel.add(rightLoginPanel);

        setContentPane(rootPanel);
    }

    /**
     * Creates the left-hand hero showcase panel matching the clean modern InvenTrack branding:
     * - Stylized icon card
     * - "InvenTrack" title
     * - “Track Smarter. Manage Better.” tagline in mint green
     * - "Inventory Management System" subtitle
     */
    private JPanel createHeroPanel() {
        JPanel hero = new JPanel(new GridBagLayout());
        hero.setBackground(new Color(15, 23, 42)); // Modern deep slate #0F172A

        JPanel contentBlock = new JPanel();
        contentBlock.setLayout(new BoxLayout(contentBlock, BoxLayout.Y_AXIS));
        contentBlock.setOpaque(false);

        // 1. Stylized Icon Card (Rounded dark card with glowing mint cube emblem)
        JPanel iconCard = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(30, 41, 59)); // Slate 800
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 24, 24);
                g2.setColor(new Color(51, 65, 85)); // Slate 700 border
                g2.setStroke(new BasicStroke(1.2f));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 24, 24);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        iconCard.setOpaque(false);
        iconCard.setPreferredSize(new Dimension(86, 86));
        iconCard.setMaximumSize(new Dimension(86, 86));
        iconCard.setLayout(new GridBagLayout());
        iconCard.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel emblemIcon = new JLabel("❖", SwingConstants.CENTER);
        emblemIcon.setFont(new Font("Segoe UI Symbol", Font.BOLD, 42));
        emblemIcon.setForeground(new Color(52, 211, 153)); // Vibrant Mint Green #34D399
        iconCard.add(emblemIcon);

        // 2. Application Name: InvenTrack
        JLabel brandName = new JLabel("InvenTrack");
        brandName.setFont(new Font("Segoe UI", Font.BOLD, 38));
        brandName.setForeground(Color.WHITE);
        brandName.setAlignmentX(Component.CENTER_ALIGNMENT);

        // 3. Tagline: “Track Smarter. Manage Better.”
        JLabel tagline = new JLabel("“Track Smarter. Manage Better.”");
        tagline.setFont(new Font("Segoe UI", Font.BOLD, 21));
        tagline.setForeground(new Color(52, 211, 153)); // Mint Green #34D399
        tagline.setAlignmentX(Component.CENTER_ALIGNMENT);

        // 4. Subtitle: Inventory Management System
        JLabel subtitle = new JLabel("Inventory Management System");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        subtitle.setForeground(new Color(148, 163, 184)); // Slate 400
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Assemble Centered Brand Elements
        contentBlock.add(iconCard);
        contentBlock.add(Box.createVerticalStrut(24));
        contentBlock.add(brandName);
        contentBlock.add(Box.createVerticalStrut(14));
        contentBlock.add(tagline);
        contentBlock.add(Box.createVerticalStrut(12));
        contentBlock.add(subtitle);

        hero.add(contentBlock);
        return hero;
    }

    /**
     * Creates the right-hand administrative sign-in portal.
     */
    private JPanel createLoginPortalPanel() {
        JPanel portalContainer = new JPanel(new GridBagLayout());
        portalContainer.setBackground(UITheme.CONTENT_BG);

        // Card Panel
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(UITheme.CARD_BG);
        card.setBorder(new CompoundBorder(
                new LineBorder(UITheme.CARD_BORDER, 1, true),
                new EmptyBorder(36, 40, 36, 40)
        ));
        card.setPreferredSize(new Dimension(440, 560));
        card.setMaximumSize(new Dimension(460, 580));

        // 1. Header
        JLabel portalTitle = new JLabel("Sign In to Workspace");
        portalTitle.setFont(UITheme.FONT_TITLE_LARGE);
        portalTitle.setForeground(UITheme.TEXT_PRIMARY);
        portalTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel portalSubtitle = new JLabel("Enter your administrator credentials to access inventory");
        portalSubtitle.setFont(UITheme.FONT_BODY);
        portalSubtitle.setForeground(UITheme.TEXT_MUTED);
        portalSubtitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        // 2. Error Message Label (dynamic feedback)
        errorLabel = new JLabel(" ");
        errorLabel.setFont(UITheme.FONT_SMALL_BOLD);
        errorLabel.setForeground(UITheme.DANGER);
        errorLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // 3. Username Field
        JLabel usernameLabel = new JLabel("Username");
        usernameLabel.setFont(UITheme.FONT_BOLD);
        usernameLabel.setForeground(UITheme.TEXT_SECONDARY);
        usernameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        usernameField = new JTextField();
        usernameField.setFont(UITheme.FONT_BODY);
        usernameField.setPreferredSize(new Dimension(360, 42));
        usernameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        usernameField.setBorder(new CompoundBorder(
                new LineBorder(UITheme.CARD_BORDER, 1, true),
                new EmptyBorder(8, 14, 8, 14)
        ));

        // 4. Password Field
        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setFont(UITheme.FONT_BOLD);
        passwordLabel.setForeground(UITheme.TEXT_SECONDARY);
        passwordLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        passwordField = new JPasswordField();
        passwordField.setFont(UITheme.FONT_BODY);
        passwordField.setPreferredSize(new Dimension(360, 42));
        passwordField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        passwordField.setBorder(new CompoundBorder(
                new LineBorder(UITheme.CARD_BORDER, 1, true),
                new EmptyBorder(8, 14, 8, 14)
        ));

        // Submit on Enter key
        KeyAdapter enterListener = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    performLogin();
                }
            }
        };
        usernameField.addKeyListener(enterListener);
        passwordField.addKeyListener(enterListener);

        // 5. Submit Button
        loginButton = CustomComponents.createPrimaryButton("Sign In to Workspace", "➔");
        loginButton.setPreferredSize(new Dimension(360, 44));
        loginButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        loginButton.setFont(UITheme.FONT_SUBTITLE);
        loginButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        loginButton.addActionListener(e -> performLogin());

        // 6. Sign Up / Register Prompt Row
        JPanel signUpRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 4, 0));
        signUpRow.setOpaque(false);
        signUpRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        signUpRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

        JLabel noAccountLabel = new JLabel("Don't have an account?");
        noAccountLabel.setFont(UITheme.FONT_BODY);
        noAccountLabel.setForeground(UITheme.TEXT_MUTED);

        JButton signUpLink = new JButton("Sign Up");
        signUpLink.setFont(UITheme.FONT_BOLD);
        signUpLink.setForeground(UITheme.PRIMARY);
        signUpLink.setBorderPainted(false);
        signUpLink.setContentAreaFilled(false);
        signUpLink.setFocusPainted(false);
        signUpLink.setCursor(new Cursor(Cursor.HAND_CURSOR));
        signUpLink.addActionListener(e -> openSignUpDialog());

        signUpRow.add(noAccountLabel);
        signUpRow.add(signUpLink);

        // 7. MySQL Live Status Badge at bottom
        dbStatusLabel = new JLabel("Checking MySQL connection...", SwingConstants.LEFT);
        dbStatusLabel.setFont(UITheme.FONT_SMALL);
        dbStatusLabel.setForeground(UITheme.TEXT_MUTED);
        dbStatusLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Assemble Right Card
        card.add(portalTitle);
        card.add(Box.createVerticalStrut(4));
        card.add(portalSubtitle);
        card.add(Box.createVerticalStrut(14));
        card.add(errorLabel);
        card.add(Box.createVerticalStrut(8));
        card.add(usernameLabel);
        card.add(Box.createVerticalStrut(6));
        card.add(usernameField);
        card.add(Box.createVerticalStrut(14));
        card.add(passwordLabel);
        card.add(Box.createVerticalStrut(6));
        card.add(passwordField);
        card.add(Box.createVerticalStrut(20));
        card.add(loginButton);
        card.add(Box.createVerticalStrut(12));
        card.add(signUpRow);
        card.add(Box.createVerticalStrut(12));
        card.add(dbStatusLabel);

        portalContainer.add(card);
        return portalContainer;
    }

    private void openSignUpDialog() {
        errorLabel.setText(" ");
        SignUpDialog dialog = new SignUpDialog(this, authService);
        dialog.setVisible(true);

        if (dialog.isRegisteredSuccessfully()) {
            usernameField.setText(dialog.getRegisteredUsername());
            passwordField.setText("");
            passwordField.requestFocus();
            errorLabel.setForeground(new Color(4, 120, 87)); // Green
            errorLabel.setText("Account created! Enter your password to sign in.");
        }
    }

    private void checkDatabaseStatus() {
        SwingWorker<DatabaseConfig.ConnectionTestResult, Void> worker = new SwingWorker<>() {
            @Override
            protected DatabaseConfig.ConnectionTestResult doInBackground() {
                DatabaseInitializer.initialize();
                return DatabaseConfig.testConnection();
            }

            @Override
            protected void done() {
                try {
                    DatabaseConfig.ConnectionTestResult result = get();
                    if (result.isSuccessful()) {
                        dbStatusLabel.setText("● Connected to MySQL (inventory_db)");
                        dbStatusLabel.setForeground(new Color(4, 120, 87)); // Green
                    } else {
                        dbStatusLabel.setText("⚠ MySQL Offline: " + result.getMessage());
                        dbStatusLabel.setForeground(UITheme.DANGER);
                        dbStatusLabel.setToolTipText(result.getMessage());
                    }
                } catch (Exception e) {
                    dbStatusLabel.setText("⚠ Database status check failed.");
                    dbStatusLabel.setForeground(UITheme.DANGER);
                }
            }
        };
        worker.execute();
    }

    private void performLogin() {
        String username = usernameField.getText().trim();
        char[] password = passwordField.getPassword();

        errorLabel.setText(" "); // Reset error display

        if (username.isEmpty()) {
            showError("Please enter your username.");
            usernameField.requestFocus();
            return;
        }

        if (password.length == 0) {
            showError("Please enter your password.");
            passwordField.requestFocus();
            return;
        }

        loginButton.setEnabled(false);
        loginButton.setText("Verifying credentials...");

        SwingWorker<User, Void> worker = new SwingWorker<>() {
            @Override
            protected User doInBackground() throws Exception {
                return authService.authenticate(username, password);
            }

            @Override
            protected void done() {
                loginButton.setEnabled(true);
                loginButton.setText(" Sign In to Workspace");

                try {
                    User user = get();
                    // Transition smoothly to Main Dashboard
                    dispose();
                    SwingUtilities.invokeLater(() -> {
                        MainDashboardFrame dashboard = new MainDashboardFrame(user);
                        dashboard.setVisible(true);
                    });
                } catch (Exception e) {
                    Throwable cause = e.getCause() != null ? e.getCause() : e;
                    if (cause instanceof AuthenticationException) {
                        showError(cause.getMessage());
                    } else if (cause instanceof SQLException) {
                        showError("Database error. Ensure MySQL is running and password matches config.properties.");
                    } else {
                        showError("Login failed: " + cause.getMessage());
                    }
                }
            }
        };
        worker.execute();
    }

    private void showError(String message) {
        errorLabel.setText(message);
    }
}
