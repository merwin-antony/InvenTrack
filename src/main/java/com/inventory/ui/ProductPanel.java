package com.inventory.ui;

import com.inventory.config.DatabaseConfig;
import com.inventory.model.Product;
import com.inventory.service.ProductService;
import com.inventory.ui.theme.CustomComponents;
import com.inventory.ui.theme.UITheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Product Catalog Management Panel.
 */
public class ProductPanel extends JPanel {

    private final ProductService productService;
    private final MainDashboardFrame mainFrame;

    private JTable productTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private JComboBox<String> categoryFilterBox;
    private JLabel statusLabel;
    private JLabel selectionInfoLabel;

    private JButton editButton;
    private JButton deleteButton;
    private JButton restockButton;

    private List<Product> currentProducts = new ArrayList<>();
    private static final NumberFormat CURRENCY_FORMAT = NumberFormat.getCurrencyInstance(Locale.of("en", "IN"));

    public ProductPanel(ProductService productService, MainDashboardFrame mainFrame) {
        this.productService = productService;
        this.mainFrame = mainFrame;

        initializeUI();
        loadCategories();
        loadProducts();
    }

    private void initializeUI() {
        setLayout(new BorderLayout(0, 16));
        setBackground(UITheme.CONTENT_BG);
        setBorder(new EmptyBorder(24, 28, 24, 28));

        // 1. Header & Actions Toolbar
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setOpaque(false);

        // Header Title Row
        JPanel titleRow = new JPanel(new BorderLayout());
        titleRow.setOpaque(false);

        JLabel titleLabel = new JLabel("Product Catalog");
        titleLabel.setFont(UITheme.FONT_TITLE_LARGE);
        titleLabel.setForeground(UITheme.TEXT_PRIMARY);

        JLabel subtitleLabel = new JLabel("Browse inventory, update stock levels, and manage catalog items");
        subtitleLabel.setFont(UITheme.FONT_SMALL);
        subtitleLabel.setForeground(UITheme.TEXT_MUTED);

        JPanel titleBlock = new JPanel();
        titleBlock.setLayout(new BoxLayout(titleBlock, BoxLayout.Y_AXIS));
        titleBlock.setOpaque(false);
        titleBlock.add(titleLabel);
        titleBlock.add(Box.createVerticalStrut(4));
        titleBlock.add(subtitleLabel);

        // Right-side Primary Add button
        JButton addBtn = CustomComponents.createPrimaryButton("Add New Product", "➕");
        addBtn.addActionListener(e -> openAddProductDialog());

        titleRow.add(titleBlock, BorderLayout.WEST);
        titleRow.add(addBtn, BorderLayout.EAST);
        topPanel.add(titleRow);
        topPanel.add(Box.createVerticalStrut(16));

        // Filter & Search Toolbar Card
        JPanel toolbarCard = new JPanel(new BorderLayout(16, 0));
        toolbarCard.setBackground(UITheme.CARD_BG);
        toolbarCard.setBorder(UITheme.createCardBorder(12, 16, 12, 16));

        // Left side: Search and Category Filter
        JPanel filterControls = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        filterControls.setOpaque(false);

        JLabel searchIcon = new JLabel("🔍");
        searchIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 14));

        searchField = new JTextField();
        searchField.setPreferredSize(new Dimension(240, 36));
        searchField.setFont(UITheme.FONT_BODY);
        searchField.putClientProperty("JTextField.placeholderText", "Search name or ID...");
        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    performSearch();
                }
            }
        });

        JButton searchBtn = CustomComponents.createSecondaryButton("Search", null);
        searchBtn.addActionListener(e -> performSearch());

        JLabel catLabel = new JLabel("Category:");
        catLabel.setFont(UITheme.FONT_BOLD);
        catLabel.setForeground(UITheme.TEXT_SECONDARY);

        categoryFilterBox = new JComboBox<>(new String[]{"All Categories"});
        categoryFilterBox.setPreferredSize(new Dimension(160, 36));
        categoryFilterBox.setFont(UITheme.FONT_BODY);
        categoryFilterBox.addActionListener(e -> performSearch());

        JButton resetBtn = CustomComponents.createSecondaryButton("Reset", "↺");
        resetBtn.addActionListener(e -> resetFilters());

        filterControls.add(searchIcon);
        filterControls.add(searchField);
        filterControls.add(searchBtn);
        filterControls.add(Box.createHorizontalStrut(10));
        filterControls.add(catLabel);
        filterControls.add(categoryFilterBox);
        filterControls.add(resetBtn);

        // Right side: Table Row Actions (Edit, Restock, Delete)
        JPanel actionControls = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        actionControls.setOpaque(false);

        restockButton = CustomComponents.createSecondaryButton("Restock", "⚡");
        restockButton.setEnabled(false);
        restockButton.addActionListener(e -> openRestockDialog());

        editButton = CustomComponents.createSecondaryButton("Edit", "✏️");
        editButton.setEnabled(false);
        editButton.addActionListener(e -> openEditProductDialog());

        deleteButton = CustomComponents.createDangerButton("Delete", "🗑️");
        deleteButton.setEnabled(false);
        deleteButton.addActionListener(e -> confirmAndDeleteProduct());

        actionControls.add(restockButton);
        actionControls.add(editButton);
        actionControls.add(deleteButton);

        toolbarCard.add(filterControls, BorderLayout.WEST);
        toolbarCard.add(actionControls, BorderLayout.EAST);
        topPanel.add(toolbarCard);

        add(topPanel, BorderLayout.NORTH);

        // 2. Table Section inside a modern Card
        JPanel tableCard = new JPanel(new BorderLayout());
        tableCard.setBackground(UITheme.CARD_BG);
        tableCard.setBorder(UITheme.createCardBorder(0, 0, 0, 0));

        String[] columns = {"ID", "Product Name", "Category", "Quantity", "Price", "Total Value", "Stock Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        productTable = new JTable(tableModel);
        productTable.setAutoCreateRowSorter(true);
        productTable.setRowHeight(38);
        productTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        productTable.getTableHeader().setFont(UITheme.FONT_BOLD);
        productTable.getTableHeader().setReorderingAllowed(false);

        // Adjust column sizes
        productTable.getColumnModel().getColumn(0).setPreferredWidth(60);   // ID
        productTable.getColumnModel().getColumn(1).setPreferredWidth(260);  // Name
        productTable.getColumnModel().getColumn(2).setPreferredWidth(140);  // Category
        productTable.getColumnModel().getColumn(3).setPreferredWidth(90);   // Quantity
        productTable.getColumnModel().getColumn(4).setPreferredWidth(110);  // Price
        productTable.getColumnModel().getColumn(5).setPreferredWidth(120);  // Total Value
        productTable.getColumnModel().getColumn(6).setPreferredWidth(130);  // Status

        // Alignments and Renderers
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        productTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        productTable.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);

        productTable.getColumnModel().getColumn(4).setCellRenderer(new CustomComponents.CurrencyRenderer());
        productTable.getColumnModel().getColumn(5).setCellRenderer(new CustomComponents.CurrencyRenderer());
        productTable.getColumnModel().getColumn(6).setCellRenderer(new CustomComponents.StatusBadgeRenderer());

        // Row Selection Listener: Enable/disable actions
        productTable.getSelectionModel().addListSelectionListener(e -> {
            boolean hasSelection = productTable.getSelectedRow() != -1;
            editButton.setEnabled(hasSelection);
            deleteButton.setEnabled(hasSelection);
            restockButton.setEnabled(hasSelection);

            if (hasSelection) {
                int row = productTable.getSelectedRow();
                String name = (String) tableModel.getValueAt(row, 1);
                int id = (int) tableModel.getValueAt(row, 0);
                selectionInfoLabel.setText("Selected: #" + id + " — " + name);
            } else {
                selectionInfoLabel.setText("No product selected");
            }
        });

        // Double-click row to open Edit Dialog
        productTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && productTable.getSelectedRow() != -1) {
                    openEditProductDialog();
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(productTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(Color.WHITE);
        tableCard.add(scrollPane, BorderLayout.CENTER);

        add(tableCard, BorderLayout.CENTER);

        // 3. Status Bar at Bottom
        JPanel statusBar = new JPanel(new BorderLayout());
        statusBar.setOpaque(false);
        statusBar.setBorder(new EmptyBorder(8, 4, 0, 4));

        statusLabel = new JLabel("Loading products from MySQL...");
        statusLabel.setFont(UITheme.FONT_SMALL);
        statusLabel.setForeground(UITheme.TEXT_MUTED);

        selectionInfoLabel = new JLabel("No product selected");
        selectionInfoLabel.setFont(UITheme.FONT_SMALL);
        selectionInfoLabel.setForeground(UITheme.TEXT_SECONDARY);

        statusBar.add(statusLabel, BorderLayout.WEST);
        statusBar.add(selectionInfoLabel, BorderLayout.EAST);
        add(statusBar, BorderLayout.SOUTH);
    }

    public void loadCategories() {
        SwingWorker<List<String>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<String> doInBackground() throws Exception {
                return productService.getAllCategories();
            }

            @Override
            protected void done() {
                try {
                    List<String> categories = get();
                    categoryFilterBox.removeAllItems();
                    categoryFilterBox.addItem("All Categories");
                    for (String cat : categories) {
                        categoryFilterBox.addItem(cat);
                    }
                } catch (Exception ignored) {}
            }
        };
        worker.execute();
    }

    public void loadProducts() {
        performSearch();
    }

    public void performSearch() {
        String keyword = searchField.getText().trim();
        String selectedCategory = (String) categoryFilterBox.getSelectedItem();

        statusLabel.setText("Fetching products...");

        SwingWorker<List<Product>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<Product> doInBackground() throws Exception {
                return productService.searchProducts(keyword, selectedCategory);
            }

            @Override
            protected void done() {
                try {
                    currentProducts = get();
                    tableModel.setRowCount(0);

                    int totalUnits = 0;
                    BigDecimal totalValue = BigDecimal.ZERO;
                    int threshold = DatabaseConfig.getLowStockThreshold();

                    for (Product p : currentProducts) {
                        String status;
                        if (p.getQuantity() <= 0) {
                            status = "OUT OF STOCK";
                        } else if (p.getQuantity() <= threshold) {
                            status = "LOW STOCK";
                        } else {
                            status = "IN STOCK";
                        }

                        tableModel.addRow(new Object[]{
                                p.getProductId(),
                                p.getProductName(),
                                p.getCategory(),
                                p.getQuantity(),
                                p.getPrice(),
                                p.getTotalValue(),
                                status
                        });

                        totalUnits += p.getQuantity();
                        totalValue = totalValue.add(p.getTotalValue());
                    }

                    statusLabel.setText(String.format("Showing %d products  |  Total Units: %,d  |  Catalog Valuation: %s",
                            currentProducts.size(), totalUnits, CustomComponents.formatIndianCurrency(totalValue)));
                } catch (Exception e) {
                    statusLabel.setText("Failed to load products: " + e.getMessage());
                    statusLabel.setForeground(UITheme.DANGER);
                }
            }
        };
        worker.execute();
    }

    private void resetFilters() {
        searchField.setText("");
        categoryFilterBox.setSelectedIndex(0);
        performSearch();
    }

    public void openAddProductDialog() {
        ProductDialog dialog = new ProductDialog((Frame) SwingUtilities.getWindowAncestor(this), null, productService);
        dialog.setVisible(true);

        if (dialog.isSaved()) {
            loadCategories();
            loadProducts();
            mainFrame.getDashboardPanel().refreshData();
            JOptionPane.showMessageDialog(this, "Product added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void openEditProductDialog() {
        int selectedRow = productTable.getSelectedRow();
        if (selectedRow == -1) return;

        int productId = (int) tableModel.getValueAt(selectedRow, 0);
        try {
            productService.getProductById(productId).ifPresent(p -> {
                ProductDialog dialog = new ProductDialog((Frame) SwingUtilities.getWindowAncestor(this), p, productService);
                dialog.setVisible(true);

                if (dialog.isSaved()) {
                    loadCategories();
                    loadProducts();
                    mainFrame.getDashboardPanel().refreshData();
                    JOptionPane.showMessageDialog(this, "Product updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                }
            });
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error fetching product: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void openRestockDialog() {
        int selectedRow = productTable.getSelectedRow();
        if (selectedRow == -1) return;

        int productId = (int) tableModel.getValueAt(selectedRow, 0);
        try {
            productService.getProductById(productId).ifPresent(p -> {
                StockDialog dialog = new StockDialog((Frame) SwingUtilities.getWindowAncestor(this), p, productService);
                dialog.setVisible(true);

                if (dialog.isSaved()) {
                    loadProducts();
                    mainFrame.getDashboardPanel().refreshData();
                    JOptionPane.showMessageDialog(this, "Stock quantity updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                }
            });
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading product: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void confirmAndDeleteProduct() {
        int selectedRow = productTable.getSelectedRow();
        if (selectedRow == -1) return;

        int productId = (int) tableModel.getValueAt(selectedRow, 0);
        String productName = (String) tableModel.getValueAt(selectedRow, 1);

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to permanently delete:\n\"" + productName + "\" (Product ID: #" + productId + ")?\n\nThis action cannot be undone.",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                boolean deleted = productService.deleteProduct(productId);
                if (deleted) {
                    loadProducts();
                    loadCategories();
                    mainFrame.getDashboardPanel().refreshData();
                    JOptionPane.showMessageDialog(this, "Product deleted successfully.", "Product Removed", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Product could not be deleted (not found).", "Notice", JOptionPane.WARNING_MESSAGE);
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Failed to delete product: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
