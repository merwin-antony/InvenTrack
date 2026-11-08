package com.inventory.ui;

import com.inventory.model.Product;
import com.inventory.service.ProductService;
import com.inventory.service.ValidationException;
import com.inventory.ui.theme.CustomComponents;
import com.inventory.ui.theme.UITheme;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.util.List;

/**
 * Modal dialog for Adding and Editing Products.
 */
public class ProductDialog extends JDialog {

    private final ProductService productService;
    private final Product existingProduct; // null if adding new product

    private JTextField nameField;
    private JComboBox<String> categoryComboBox;
    private JSpinner quantitySpinner;
    private JTextField priceField;
    private JLabel errorLabel;
    private JButton saveButton;

    private boolean saved = false;

    public ProductDialog(Frame parent, Product existingProduct, ProductService productService) {
        super(parent, existingProduct == null ? "Add New Product" : "Edit Product (#" + existingProduct.getProductId() + ")", true);
        this.existingProduct = existingProduct;
        this.productService = productService;

        initializeUI();
        loadCategories();
        populateFields();
    }

    private void initializeUI() {
        setSize(460, 520);
        setResizable(false);
        setLocationRelativeTo(getParent());

        JPanel rootPanel = new JPanel(new BorderLayout());
        rootPanel.setBackground(UITheme.CARD_BG);
        rootPanel.setBorder(new EmptyBorder(24, 28, 24, 28));

        // Header Title
        JLabel titleLabel = new JLabel(existingProduct == null ? "Add New Product" : "Edit Product Details");
        titleLabel.setFont(UITheme.FONT_TITLE);
        titleLabel.setForeground(UITheme.TEXT_PRIMARY);

        JLabel subLabel = new JLabel(existingProduct == null ? "Enter the product information to insert into the catalog" : "Update product attributes and pricing");
        subLabel.setFont(UITheme.FONT_SMALL);
        subLabel.setForeground(UITheme.TEXT_MUTED);

        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setOpaque(false);
        headerPanel.add(titleLabel);
        headerPanel.add(Box.createVerticalStrut(4));
        headerPanel.add(subLabel);
        headerPanel.add(Box.createVerticalStrut(16));

        rootPanel.add(headerPanel, BorderLayout.NORTH);

        // Form Fields (GridBagLayout)
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(6, 0, 6, 0);
        gbc.weightx = 1.0;

        int row = 0;

        // Product ID (only shown in Edit Mode)
        if (existingProduct != null) {
            JLabel idLabel = new JLabel("Product ID");
            idLabel.setFont(UITheme.FONT_BOLD);
            idLabel.setForeground(UITheme.TEXT_SECONDARY);

            JTextField idField = new JTextField(String.valueOf(existingProduct.getProductId()));
            idField.setFont(UITheme.FONT_BODY);
            idField.setEditable(false);
            idField.setBackground(UITheme.CONTENT_BG);
            idField.setBorder(new CompoundBorder(new LineBorder(UITheme.CARD_BORDER, 1, true), new EmptyBorder(8, 10, 8, 10)));

            gbc.gridy = row++;
            formPanel.add(idLabel, gbc);
            gbc.gridy = row++;
            formPanel.add(idField, gbc);
        }

        // Product Name
        JLabel nameLabel = new JLabel("Product Name *");
        nameLabel.setFont(UITheme.FONT_BOLD);
        nameLabel.setForeground(UITheme.TEXT_SECONDARY);

        nameField = new JTextField();
        nameField.setFont(UITheme.FONT_BODY);
        nameField.setBorder(new CompoundBorder(new LineBorder(UITheme.CARD_BORDER, 1, true), new EmptyBorder(8, 10, 8, 10)));

        gbc.gridy = row++;
        formPanel.add(nameLabel, gbc);
        gbc.gridy = row++;
        formPanel.add(nameField, gbc);

        // Category (Editable ComboBox)
        JLabel catLabel = new JLabel("Category");
        catLabel.setFont(UITheme.FONT_BOLD);
        catLabel.setForeground(UITheme.TEXT_SECONDARY);

        categoryComboBox = new JComboBox<>();
        categoryComboBox.setEditable(true);
        categoryComboBox.setFont(UITheme.FONT_BODY);
        categoryComboBox.setPreferredSize(new Dimension(0, 38));

        gbc.gridy = row++;
        formPanel.add(catLabel, gbc);
        gbc.gridy = row++;
        formPanel.add(categoryComboBox, gbc);

        // Quantity & Price side-by-side
        JPanel splitRow = new JPanel(new GridLayout(1, 2, 16, 0));
        splitRow.setOpaque(false);

        // Quantity Spinner
        JPanel qtyPanel = new JPanel(new BorderLayout(0, 4));
        qtyPanel.setOpaque(false);
        JLabel qtyLabel = new JLabel("Stock Quantity *");
        qtyLabel.setFont(UITheme.FONT_BOLD);
        qtyLabel.setForeground(UITheme.TEXT_SECONDARY);

        SpinnerNumberModel spinnerModel = new SpinnerNumberModel(0, 0, 1_000_000, 1);
        quantitySpinner = new JSpinner(spinnerModel);
        quantitySpinner.setFont(UITheme.FONT_BODY);
        quantitySpinner.setPreferredSize(new Dimension(0, 38));

        qtyPanel.add(qtyLabel, BorderLayout.NORTH);
        qtyPanel.add(quantitySpinner, BorderLayout.CENTER);

        // Price Field
        JPanel pricePanel = new JPanel(new BorderLayout(0, 4));
        pricePanel.setOpaque(false);
        JLabel priceLabel = new JLabel("Unit Price (₹) *");
        priceLabel.setFont(UITheme.FONT_BOLD);
        priceLabel.setForeground(UITheme.TEXT_SECONDARY);

        priceField = new JTextField("0.00");
        priceField.setFont(UITheme.FONT_BODY);
        priceField.setBorder(new CompoundBorder(new LineBorder(UITheme.CARD_BORDER, 1, true), new EmptyBorder(8, 10, 8, 10)));

        pricePanel.add(priceLabel, BorderLayout.NORTH);
        pricePanel.add(priceField, BorderLayout.CENTER);

        splitRow.add(qtyPanel);
        splitRow.add(pricePanel);

        gbc.gridy = row++;
        formPanel.add(splitRow, gbc);

        // Error message label
        errorLabel = new JLabel(" ");
        errorLabel.setFont(UITheme.FONT_SMALL_BOLD);
        errorLabel.setForeground(UITheme.DANGER);

        gbc.gridy = row++;
        formPanel.add(errorLabel, gbc);

        rootPanel.add(formPanel, BorderLayout.CENTER);

        // Action Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(new EmptyBorder(16, 0, 0, 0));

        JButton cancelButton = CustomComponents.createSecondaryButton("Cancel", null);
        cancelButton.addActionListener(e -> dispose());

        saveButton = CustomComponents.createPrimaryButton(existingProduct == null ? "Save Product" : "Update Product", "💾");
        saveButton.addActionListener(e -> saveProduct());

        buttonPanel.add(cancelButton);
        buttonPanel.add(saveButton);

        rootPanel.add(buttonPanel, BorderLayout.SOUTH);
        setContentPane(rootPanel);
    }

    private void loadCategories() {
        try {
            List<String> categories = productService.getAllCategories();
            categoryComboBox.addItem("General");
            categoryComboBox.addItem("Electronics");
            categoryComboBox.addItem("Accessories");
            categoryComboBox.addItem("Hardware");
            categoryComboBox.addItem("Office Supplies");

            for (String cat : categories) {
                boolean exists = false;
                for (int i = 0; i < categoryComboBox.getItemCount(); i++) {
                    if (cat.equalsIgnoreCase(categoryComboBox.getItemAt(i))) {
                        exists = true;
                        break;
                    }
                }
                if (!exists) {
                    categoryComboBox.addItem(cat);
                }
            }
        } catch (Exception ignored) {}
    }

    private void populateFields() {
        if (existingProduct != null) {
            nameField.setText(existingProduct.getProductName());
            categoryComboBox.setSelectedItem(existingProduct.getCategory());
            quantitySpinner.setValue(existingProduct.getQuantity());
            priceField.setText(existingProduct.getPrice().toPlainString());
        }
    }

    private void saveProduct() {
        errorLabel.setText(" ");
        String name = nameField.getText().trim();
        Object catObj = categoryComboBox.getSelectedItem();
        String category = catObj != null ? catObj.toString().trim() : "General";
        int quantity = (Integer) quantitySpinner.getValue();
        String priceStr = priceField.getText().trim().replace("₹", "").replace("$", "").replace(",", "");

        if (name.isEmpty()) {
            showError("Product name cannot be empty.");
            nameField.requestFocus();
            return;
        }

        BigDecimal price;
        try {
            price = new BigDecimal(priceStr).setScale(2, RoundingMode.HALF_UP);
            if (price.compareTo(BigDecimal.ZERO) < 0) {
                showError("Price cannot be negative.");
                priceField.requestFocus();
                return;
            }
        } catch (NumberFormatException e) {
            showError("Invalid price. Please enter a valid decimal number (e.g. 19.99).");
            priceField.requestFocus();
            return;
        }

        saveButton.setEnabled(false);

        try {
            if (existingProduct == null) {
                // Add new product
                Product newProd = new Product(name, category, quantity, price);
                productService.addProduct(newProd);
            } else {
                // Update existing product
                existingProduct.setProductName(name);
                existingProduct.setCategory(category);
                existingProduct.setQuantity(quantity);
                existingProduct.setPrice(price);
                productService.updateProduct(existingProduct);
            }

            saved = true;
            dispose();
        } catch (ValidationException ve) {
            showError(ve.getMessage());
            saveButton.setEnabled(true);
        } catch (SQLException se) {
            showError("Database error: " + se.getMessage());
            saveButton.setEnabled(true);
        }
    }

    private void showError(String message) {
        errorLabel.setText(message);
    }

    public boolean isSaved() {
        return saved;
    }
}
