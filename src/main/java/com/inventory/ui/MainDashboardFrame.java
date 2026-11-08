package com.inventory.ui;

import com.inventory.model.User;
import com.inventory.service.AuthService;
import com.inventory.service.ProductService;
import com.inventory.ui.theme.UITheme;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

/**
 * Main Application Window featuring a modern dark sidebar and responsive CardLayout view container.
 */
public class MainDashboardFrame extends JFrame {

    private final User currentUser;
    private final ProductService productService;

    private CardLayout cardLayout;
    private JPanel contentContainer;

    private DashboardPanel dashboardPanel;
    private ProductPanel productPanel;

    private JButton navDashboardBtn;
    private JButton navProductsBtn;

    private static final String VIEW_DASHBOARD = "VIEW_DASHBOARD";
    private static final String VIEW_PRODUCTS = "VIEW_PRODUCTS";

    public MainDashboardFrame(User user) {
        this.currentUser = user;
        this.productService = new ProductService();

        initializeUI();
    }

    private void initializeUI() {
        setTitle("InvenTrack — Inventory Management System");
        setIconImage(UITheme.createAppIcon());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1260, 800);
        setMinimumSize(new Dimension(1020, 640));
        setLocationRelativeTo(null);

        JPanel rootPanel = new JPanel(new BorderLayout());
        rootPanel.setBackground(UITheme.CONTENT_BG);

        // 1. Sidebar Navigation (Left)
        JPanel sidebar = createSidebar();
        rootPanel.add(sidebar, BorderLayout.WEST);

        // 2. Main Content Area with CardLayout (Center)
        cardLayout = new CardLayout();
        contentContainer = new JPanel(cardLayout);
        contentContainer.setBackground(UITheme.CONTENT_BG);

        dashboardPanel = new DashboardPanel(productService, this);
        productPanel = new ProductPanel(productService, this);

        contentContainer.add(dashboardPanel, VIEW_DASHBOARD);
        contentContainer.add(productPanel, VIEW_PRODUCTS);

        rootPanel.add(contentContainer, BorderLayout.CENTER);

        setContentPane(rootPanel);
        showDashboardView(); // Default landing page
    }

    private JPanel createSidebar() {
        JPanel sidebar = new JPanel(new BorderLayout());
        sidebar.setPreferredSize(new Dimension(250, 0));
        sidebar.setBackground(UITheme.SIDEBAR_BG);
        sidebar.setBorder(new EmptyBorder(24, 16, 20, 16));

        // Top Section: Logo & Branding (Matching InvenTrack Emblem & Typography)
        JPanel topBlock = new JPanel();
        topBlock.setLayout(new BoxLayout(topBlock, BoxLayout.Y_AXIS));
        topBlock.setOpaque(false);

        JPanel logoRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        logoRow.setOpaque(false);

        // Stylized emblem card in sidebar
        JPanel iconCard = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(30, 41, 59)); // Slate 800
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.setColor(new Color(51, 65, 85)); // Slate 700 border
                g2.setStroke(new BasicStroke(1.0f));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        iconCard.setOpaque(false);
        iconCard.setPreferredSize(new Dimension(38, 38));
        iconCard.setLayout(new GridBagLayout());

        JLabel emblemIcon = new JLabel("❖", SwingConstants.CENTER);
        emblemIcon.setFont(new Font("Segoe UI Symbol", Font.BOLD, 20));
        emblemIcon.setForeground(new Color(52, 211, 153)); // Mint Green #34D399
        iconCard.add(emblemIcon);

        JPanel brandTextPanel = new JPanel();
        brandTextPanel.setLayout(new BoxLayout(brandTextPanel, BoxLayout.Y_AXIS));
        brandTextPanel.setOpaque(false);

        JLabel brandTitle = new JLabel("InvenTrack");
        brandTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        brandTitle.setForeground(Color.WHITE);

        JLabel brandTagline = new JLabel("“Track Smarter”");
        brandTagline.setFont(UITheme.FONT_SMALL_BOLD);
        brandTagline.setForeground(new Color(52, 211, 153)); // Mint Green #34D399

        brandTextPanel.add(brandTitle);
        brandTextPanel.add(Box.createVerticalStrut(1));
        brandTextPanel.add(brandTagline);

        logoRow.add(iconCard);
        logoRow.add(brandTextPanel);

        topBlock.add(logoRow);
        topBlock.add(Box.createVerticalStrut(28));

        // Navigation Menu
        JLabel menuHeader = new JLabel("MAIN MENU");
        menuHeader.setFont(UITheme.FONT_SMALL_BOLD);
        menuHeader.setForeground(new Color(100, 116, 139)); // Slate 500
        menuHeader.setBorder(new EmptyBorder(0, 12, 8, 0));

        topBlock.add(menuHeader);

        navDashboardBtn = createNavButton("Dashboard", "📊");
        navDashboardBtn.addActionListener(e -> showDashboardView());

        navProductsBtn = createNavButton("Products Catalog", "📦");
        navProductsBtn.addActionListener(e -> showProductView());

        topBlock.add(navDashboardBtn);
        topBlock.add(Box.createVerticalStrut(6));
        topBlock.add(navProductsBtn);

        sidebar.add(topBlock, BorderLayout.NORTH);

        // Bottom Section: Current User Profile Card & Logout
        JPanel bottomBlock = new JPanel();
        bottomBlock.setLayout(new BoxLayout(bottomBlock, BoxLayout.Y_AXIS));
        bottomBlock.setOpaque(false);

        JPanel userCard = new JPanel(new BorderLayout(10, 0));
        userCard.setBackground(UITheme.SIDEBAR_CARD);
        userCard.setBorder(new CompoundBorder(
                new LineBorder(new Color(51, 65, 85), 1, true),
                new EmptyBorder(10, 12, 10, 12)
        ));

        // User Avatar Initials
        String initials = getInitials(currentUser != null ? currentUser.getFullName() : "Admin");
        JLabel avatarLabel = new JLabel(initials, SwingConstants.CENTER) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(UITheme.PRIMARY);
                g2.fillOval(0, 0, getWidth(), getHeight());
                g2.dispose();
                super.paintComponent(g);
            }
        };
        avatarLabel.setFont(UITheme.FONT_SMALL_BOLD);
        avatarLabel.setForeground(Color.WHITE);
        avatarLabel.setPreferredSize(new Dimension(34, 34));

        JPanel userInfo = new JPanel();
        userInfo.setLayout(new BoxLayout(userInfo, BoxLayout.Y_AXIS));
        userInfo.setOpaque(false);

        String displayName = currentUser != null ? currentUser.getFullName() : "Administrator";
        if (displayName.length() > 16) displayName = displayName.substring(0, 14) + "..";
        JLabel nameLabel = new JLabel(displayName);
        nameLabel.setFont(UITheme.FONT_BOLD);
        nameLabel.setForeground(Color.WHITE);

        String roleName = currentUser != null ? currentUser.getRole() : "ADMIN";
        JLabel roleLabel = new JLabel(roleName);
        roleLabel.setFont(UITheme.FONT_SMALL);
        roleLabel.setForeground(UITheme.SIDEBAR_TEXT);

        userInfo.add(nameLabel);
        userInfo.add(Box.createVerticalStrut(2));
        userInfo.add(roleLabel);

        userCard.add(avatarLabel, BorderLayout.WEST);
        userCard.add(userInfo, BorderLayout.CENTER);

        // Logout Button
        JButton logoutBtn = new JButton("🚪  Sign Out");
        logoutBtn.setFont(UITheme.FONT_BOLD);
        logoutBtn.setForeground(new Color(248, 113, 113)); // Red 400
        logoutBtn.setBackground(new Color(30, 41, 59));
        logoutBtn.setFocusPainted(false);
        logoutBtn.setBorder(new CompoundBorder(
                new LineBorder(new Color(51, 65, 85), 1, true),
                new EmptyBorder(8, 12, 8, 12)
        ));
        logoutBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        logoutBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoutBtn.addActionListener(e -> logout());

        logoutBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                logoutBtn.setBackground(new Color(153, 27, 27));
                logoutBtn.setForeground(Color.WHITE);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                logoutBtn.setBackground(new Color(30, 41, 59));
                logoutBtn.setForeground(new Color(248, 113, 113));
            }
        });

        bottomBlock.add(userCard);
        bottomBlock.add(Box.createVerticalStrut(10));
        bottomBlock.add(logoutBtn);

        sidebar.add(bottomBlock, BorderLayout.SOUTH);

        return sidebar;
    }

    private JButton createNavButton(String text, String icon) {
        JButton btn = new JButton(icon + "   " + text);
        btn.setFont(UITheme.FONT_BOLD);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setForeground(UITheme.SIDEBAR_TEXT);
        btn.setBackground(UITheme.SIDEBAR_BG);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(new EmptyBorder(10, 14, 10, 14));
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));

        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (btn.getBackground() != UITheme.SIDEBAR_ACTIVE) {
                    btn.setBackground(UITheme.SIDEBAR_HOVER);
                    btn.setForeground(Color.WHITE);
                }
            }
            @Override
            public void mouseExited(MouseEvent e) {
                if (btn.getBackground() != UITheme.SIDEBAR_ACTIVE) {
                    btn.setBackground(UITheme.SIDEBAR_BG);
                    btn.setForeground(UITheme.SIDEBAR_TEXT);
                }
            }
        });

        return btn;
    }

    public void showDashboardView() {
        cardLayout.show(contentContainer, VIEW_DASHBOARD);
        setNavActive(navDashboardBtn, navProductsBtn);
        dashboardPanel.refreshData();
    }

    public void showProductView() {
        cardLayout.show(contentContainer, VIEW_PRODUCTS);
        setNavActive(navProductsBtn, navDashboardBtn);
        productPanel.loadProducts();
    }

    private void setNavActive(JButton activeBtn, JButton inactiveBtn) {
        activeBtn.setBackground(UITheme.SIDEBAR_ACTIVE);
        activeBtn.setForeground(Color.WHITE);

        inactiveBtn.setBackground(UITheme.SIDEBAR_BG);
        inactiveBtn.setForeground(UITheme.SIDEBAR_TEXT);
    }

    private String getInitials(String name) {
        if (name == null || name.trim().isEmpty()) return "AD";
        String[] parts = name.trim().split("\\s+");
        if (parts.length == 1) {
            return parts[0].substring(0, Math.min(2, parts[0].length())).toUpperCase();
        }
        return (parts[0].substring(0, 1) + parts[parts.length - 1].substring(0, 1)).toUpperCase();
    }

    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to log out of InvenTrack?",
                "Confirm Sign Out",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (confirm == JOptionPane.YES_OPTION) {
            AuthService.logout();
            dispose();
            SwingUtilities.invokeLater(() -> {
                LoginFrame loginFrame = new LoginFrame();
                loginFrame.setVisible(true);
            });
        }
    }

    /**
     * Creates a branded taskbar/window icon featuring the mint green InvenTrack emblem.
     */
    public static Image createAppIcon() {
        BufferedImage img = new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(new Color(15, 23, 42)); // Slate 900
        g2.fillRoundRect(2, 2, 60, 60, 16, 16);
        g2.setColor(new Color(51, 65, 85));
        g2.drawRoundRect(2, 2, 59, 59, 16, 16);
        g2.setFont(new Font("Segoe UI Symbol", Font.BOLD, 36));
        g2.setColor(new Color(52, 211, 153)); // Mint Green #34D399
        FontMetrics fm = g2.getFontMetrics();
        int x = (64 - fm.stringWidth("❖")) / 2;
        int y = ((64 - fm.getHeight()) / 2) + fm.getAscent();
        g2.drawString("❖", x, y);
        g2.dispose();
        return img;
    }

    public DashboardPanel getDashboardPanel() {
        return dashboardPanel;
    }

    public ProductPanel getProductPanel() {
        return productPanel;
    }
}
