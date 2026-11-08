package com.inventory.ui;

import com.inventory.config.DatabaseConfig;
import com.inventory.model.InventoryStats;
import com.inventory.model.Product;
import com.inventory.service.ProductService;
import com.inventory.ui.theme.CustomComponents;
import com.inventory.ui.theme.UITheme;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

/**
 * Dashboard View Panel containing KPI metric stat cards, low-stock alerts, and quick actions.
 */
public class DashboardPanel extends JPanel {

    private final ProductService productService;
    private final MainDashboardFrame mainFrame;

    private CustomComponents.StatCard totalProductsCard;
    private CustomComponents.StatCard totalUnitsCard;
    private CustomComponents.StatCard lowStockCard;
    private CustomComponents.StatCard totalValueCard;

    private JTable alertTable;
    private DefaultTableModel alertTableModel;
    private JLabel lastUpdatedLabel;
    private JLabel alertStatusSummary;

    private static final NumberFormat CURRENCY_FORMAT = NumberFormat.getCurrencyInstance(Locale.of("en", "IN"));
    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("hh:mm:ss a");

    public DashboardPanel(ProductService productService, MainDashboardFrame mainFrame) {
        this.productService = productService;
        this.mainFrame = mainFrame;
        initializeUI();
        refreshData();
    }

    private void initializeUI() {
        setLayout(new BorderLayout(0, 20));
        setBackground(UITheme.CONTENT_BG);
        setBorder(new EmptyBorder(24, 28, 24, 28));

        // 1. Top Header Bar
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);

        JPanel titleBlock = new JPanel();
        titleBlock.setLayout(new BoxLayout(titleBlock, BoxLayout.Y_AXIS));
        titleBlock.setOpaque(false);

        JLabel pageTitle = new JLabel("Executive Dashboard");
        pageTitle.setFont(UITheme.FONT_TITLE_LARGE);
        pageTitle.setForeground(UITheme.TEXT_PRIMARY);

        lastUpdatedLabel = new JLabel("Loading metrics...");
        lastUpdatedLabel.setFont(UITheme.FONT_SMALL);
        lastUpdatedLabel.setForeground(UITheme.TEXT_MUTED);

        titleBlock.add(pageTitle);
        titleBlock.add(Box.createVerticalStrut(4));
        titleBlock.add(lastUpdatedLabel);

        // Header Action: Refresh Button
        JButton refreshBtn = CustomComponents.createSecondaryButton("Refresh Metrics", "🔄");
        refreshBtn.addActionListener(e -> refreshData());

        headerPanel.add(titleBlock, BorderLayout.WEST);
        headerPanel.add(refreshBtn, BorderLayout.EAST);
        add(headerPanel, BorderLayout.NORTH);

        // 2. Center Content: Stat Cards + Alerts Table
        JPanel centerContainer = new JPanel(new BorderLayout(0, 20));
        centerContainer.setOpaque(false);

        // A. KPI Stat Cards Grid (1 row, 4 equal cards)
        JPanel statsGrid = new JPanel(new GridLayout(1, 4, 16, 0));
        statsGrid.setOpaque(false);

        totalProductsCard = new CustomComponents.StatCard(
                "Distinct Products", "0", "Catalog items in database", UITheme.PRIMARY, "📦"
        );
        totalUnitsCard = new CustomComponents.StatCard(
                "Total Units in Stock", "0", "Physical items across inventory", UITheme.SUCCESS, "📊"
        );
        lowStockCard = new CustomComponents.StatCard(
                "Low Stock Alerts", "0", "Items ≤ " + DatabaseConfig.getLowStockThreshold() + " units remaining", UITheme.WARNING, "⚠️"
        );
        totalValueCard = new CustomComponents.StatCard(
                "Total Inventory Value", "₹0.00", "Calculated as price × quantity", new Color(147, 51, 234), "💰"
        );

        statsGrid.add(totalProductsCard);
        statsGrid.add(totalUnitsCard);
        statsGrid.add(lowStockCard);
        statsGrid.add(totalValueCard);

        centerContainer.add(statsGrid, BorderLayout.NORTH);

        // B. Lower Section: Split between Critical Alerts and Quick Actions
        JPanel lowerSplitPanel = new JPanel(new GridBagLayout());
        lowerSplitPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;

        // B1. Left Panel: Low Stock Alert Table (70% width)
        JPanel alertCard = new JPanel(new BorderLayout(0, 12));
        alertCard.setBackground(UITheme.CARD_BG);
        alertCard.setBorder(UITheme.createCardBorder(18, 18, 18, 18));

        JPanel alertHeader = new JPanel(new BorderLayout());
        alertHeader.setOpaque(false);

        JLabel alertTitle = new JLabel("Critical Stock Alerts");
        alertTitle.setFont(UITheme.FONT_TITLE);
        alertTitle.setForeground(UITheme.TEXT_PRIMARY);

        alertStatusSummary = new JLabel("Checking stock levels...");
        alertStatusSummary.setFont(UITheme.FONT_SMALL);
        alertStatusSummary.setForeground(UITheme.TEXT_MUTED);

        alertHeader.add(alertTitle, BorderLayout.WEST);
        alertHeader.add(alertStatusSummary, BorderLayout.EAST);
        alertCard.add(alertHeader, BorderLayout.NORTH);

        // Alert Table
        String[] columns = {"ID", "Product Name", "Category", "Units Left", "Unit Price", "Status"};
        alertTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        alertTable = new JTable(alertTableModel);
        alertTable.setAutoCreateRowSorter(true);
        alertTable.setRowHeight(36);
        alertTable.getTableHeader().setFont(UITheme.FONT_BOLD);
        alertTable.getTableHeader().setReorderingAllowed(false);

        // Column widths and renderers
        alertTable.getColumnModel().getColumn(0).setPreferredWidth(50);
        alertTable.getColumnModel().getColumn(1).setPreferredWidth(180);
        alertTable.getColumnModel().getColumn(2).setPreferredWidth(110);
        alertTable.getColumnModel().getColumn(3).setPreferredWidth(80);
        alertTable.getColumnModel().getColumn(4).setPreferredWidth(90);
        alertTable.getColumnModel().getColumn(5).setPreferredWidth(110);

        // Renderers
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        alertTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        alertTable.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
        alertTable.getColumnModel().getColumn(4).setCellRenderer(new CustomComponents.CurrencyRenderer());
        alertTable.getColumnModel().getColumn(5).setCellRenderer(new CustomComponents.StatusBadgeRenderer());

        JScrollPane scrollPane = new JScrollPane(alertTable);
        scrollPane.setBorder(new LineBorder(UITheme.CARD_BORDER, 1, true));
        scrollPane.getViewport().setBackground(Color.WHITE);
        alertCard.add(scrollPane, BorderLayout.CENTER);

        // Double click to open restock dialog directly from dashboard!
        alertTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2 && alertTable.getSelectedRow() != -1) {
                    int row = alertTable.getSelectedRow();
                    int productId = (int) alertTableModel.getValueAt(row, 0);
                    openRestockDialogForId(productId);
                }
            }
        });

        // B2. Right Panel: Quick Actions & System Info (30% width)
        JPanel actionsCard = new JPanel();
        actionsCard.setLayout(new BoxLayout(actionsCard, BoxLayout.Y_AXIS));
        actionsCard.setBackground(UITheme.CARD_BG);
        actionsCard.setBorder(UITheme.createCardBorder(18, 18, 18, 18));
        actionsCard.setPreferredSize(new Dimension(280, 0));

        JLabel actionsTitle = new JLabel("Quick Actions");
        actionsTitle.setFont(UITheme.FONT_TITLE);
        actionsTitle.setForeground(UITheme.TEXT_PRIMARY);

        JButton addProductBtn = CustomComponents.createPrimaryButton("Add New Product", "➕");
        addProductBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        addProductBtn.addActionListener(e -> {
            mainFrame.showProductView();
            mainFrame.getProductPanel().openAddProductDialog();
        });

        JButton viewCatalogBtn = CustomComponents.createSecondaryButton("Manage Full Inventory", "📦");
        viewCatalogBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        viewCatalogBtn.addActionListener(e -> mainFrame.showProductView());

        // System Info card
        JPanel infoSubcard = new JPanel();
        infoSubcard.setLayout(new BoxLayout(infoSubcard, BoxLayout.Y_AXIS));
        infoSubcard.setBackground(UITheme.CONTENT_BG);
        infoSubcard.setBorder(new CompoundBorder(
                new LineBorder(UITheme.CARD_BORDER, 1, true),
                new EmptyBorder(12, 12, 12, 12)
        ));
        infoSubcard.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));

        JLabel infoHeader = new JLabel("System Information");
        infoHeader.setFont(UITheme.FONT_BOLD);
        infoHeader.setForeground(UITheme.TEXT_PRIMARY);

        JLabel dbInfo = new JLabel("Database: MySQL (inventory_db)");
        dbInfo.setFont(UITheme.FONT_SMALL);
        dbInfo.setForeground(UITheme.TEXT_SECONDARY);

        JLabel thresholdInfo = new JLabel("Low Stock Threshold: ≤ " + DatabaseConfig.getLowStockThreshold() + " units");
        thresholdInfo.setFont(UITheme.FONT_SMALL);
        thresholdInfo.setForeground(UITheme.TEXT_SECONDARY);

        JLabel tipInfo = new JLabel("Tip: Double-click alert row to restock");
        tipInfo.setFont(UITheme.FONT_SMALL);
        tipInfo.setForeground(UITheme.PRIMARY);

        infoSubcard.add(infoHeader);
        infoSubcard.add(Box.createVerticalStrut(6));
        infoSubcard.add(dbInfo);
        infoSubcard.add(Box.createVerticalStrut(4));
        infoSubcard.add(thresholdInfo);
        infoSubcard.add(Box.createVerticalStrut(6));
        infoSubcard.add(tipInfo);

        actionsCard.add(actionsTitle);
        actionsCard.add(Box.createVerticalStrut(14));
        actionsCard.add(addProductBtn);
        actionsCard.add(Box.createVerticalStrut(10));
        actionsCard.add(viewCatalogBtn);
        actionsCard.add(Box.createVerticalGlue());
        actionsCard.add(infoSubcard);

        // Add to gridbag
        gbc.gridx = 0;
        gbc.weightx = 0.72;
        gbc.insets = new Insets(0, 0, 0, 16);
        lowerSplitPanel.add(alertCard, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.28;
        gbc.insets = new Insets(0, 0, 0, 0);
        lowerSplitPanel.add(actionsCard, gbc);

        centerContainer.add(lowerSplitPanel, BorderLayout.CENTER);
        add(centerContainer, BorderLayout.CENTER);
    }

    /**
     * Refreshes metrics and low stock table on a background SwingWorker.
     */
    public void refreshData() {
        lastUpdatedLabel.setText("Refreshing data from MySQL...");

        SwingWorker<DashboardData, Void> worker = new SwingWorker<>() {
            @Override
            protected DashboardData doInBackground() throws Exception {
                InventoryStats stats = productService.getInventoryStats();
                List<Product> lowStockList = productService.getLowStockProducts();
                return new DashboardData(stats, lowStockList);
            }

            @Override
            protected void done() {
                try {
                    DashboardData data = get();
                    // Update KPI Stat Cards
                    totalProductsCard.setValue(String.valueOf(data.stats.getTotalDistinctProducts()));
                    totalUnitsCard.setValue(String.format("%,d", data.stats.getTotalUnitsInStock()));
                    lowStockCard.setValue(String.valueOf(data.stats.getLowStockCount()));
                    totalValueCard.setValue(CustomComponents.formatIndianCurrency(data.stats.getTotalInventoryValue()));

                    // Update Alert Table
                    alertTableModel.setRowCount(0);
                    for (Product p : data.lowStockProducts) {
                        String status = p.getQuantity() == 0 ? "OUT OF STOCK" : "LOW STOCK";
                        alertTableModel.addRow(new Object[]{
                                p.getProductId(),
                                p.getProductName(),
                                p.getCategory(),
                                p.getQuantity(),
                                p.getPrice(),
                                status
                        });
                    }

                    if (data.lowStockProducts.isEmpty()) {
                        alertStatusSummary.setText("Healthy: All products have sufficient stock");
                        alertStatusSummary.setForeground(new Color(4, 120, 87));
                    } else {
                        alertStatusSummary.setText(data.lowStockProducts.size() + " items require restocking");
                        alertStatusSummary.setForeground(UITheme.DANGER);
                    }

                    lastUpdatedLabel.setText("Last updated: " + LocalDateTime.now().format(TIME_FORMAT));
                } catch (Exception e) {
                    lastUpdatedLabel.setText("Failed to refresh metrics. Check MySQL connection.");
                    lastUpdatedLabel.setForeground(UITheme.DANGER);
                }
            }
        };
        worker.execute();
    }

    private void openRestockDialogForId(int productId) {
        try {
            productService.getProductById(productId).ifPresent(p -> {
                StockDialog dialog = new StockDialog((Frame) SwingUtilities.getWindowAncestor(this), p, productService);
                dialog.setVisible(true);
                if (dialog.isSaved()) {
                    refreshData();
                    mainFrame.getProductPanel().loadProducts();
                }
            });
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Could not load product details: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private record DashboardData(InventoryStats stats, List<Product> lowStockProducts) {}
}
