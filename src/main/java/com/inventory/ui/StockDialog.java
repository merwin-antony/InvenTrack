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
import java.sql.SQLException;

/**
 * Quick Stock Adjustment Dialog (Restock & Outward stock adjustment).
 */
public class StockDialog extends JDialog {

    private final Product product;
    private final ProductService productService;

    private JRadioButton addRadio;
    private JRadioButton subtractRadio;
    private JSpinner amountSpinner;
    private JLabel previewLabel;
    private JLabel errorLabel;
    private boolean saved = false;

    public StockDialog(Frame parent, Product product, ProductService productService) {
        super(parent, "Quick Stock Adjustment — " + product.getProductName(), true);
        this.product = product;
        this.productService = productService;

        initializeUI();
    }

    private void initializeUI() {
        setSize(440, 420);
        setResizable(false);
        setLocationRelativeTo(getParent());

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(UITheme.CARD_BG);
        root.setBorder(new EmptyBorder(22, 24, 22, 24));

        // Header Title
        JLabel title = new JLabel("Adjust Stock Level");
        title.setFont(UITheme.FONT_TITLE);
        title.setForeground(UITheme.TEXT_PRIMARY);

        JLabel subtitle = new JLabel("Quickly receive inventory or record dispatch");
        subtitle.setFont(UITheme.FONT_SMALL);
        subtitle.setForeground(UITheme.TEXT_MUTED);

        JPanel header = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setOpaque(false);
        header.add(title);
        header.add(Box.createVerticalStrut(4));
        header.add(subtitle);
        header.add(Box.createVerticalStrut(14));

        root.add(header, BorderLayout.NORTH);

        // Center Panel: Product Summary & Adjustment Controls
        JPanel center = new JPanel();
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.setOpaque(false);

        // Summary Card
        JPanel summaryCard = new JPanel(new GridLayout(2, 2, 8, 8));
        summaryCard.setBackground(UITheme.CONTENT_BG);
        summaryCard.setBorder(new CompoundBorder(
                new LineBorder(UITheme.CARD_BORDER, 1, true),
                new EmptyBorder(10, 14, 10, 14)
        ));
        summaryCard.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));

        JLabel nameKey = new JLabel("Product:");
        nameKey.setFont(UITheme.FONT_SMALL);
        nameKey.setForeground(UITheme.TEXT_MUTED);

        JLabel nameVal = new JLabel(product.getProductName());
        nameVal.setFont(UITheme.FONT_BOLD);
        nameVal.setForeground(UITheme.TEXT_PRIMARY);

        JLabel curKey = new JLabel("Current Stock:");
        curKey.setFont(UITheme.FONT_SMALL);
        curKey.setForeground(UITheme.TEXT_MUTED);

        JLabel curVal = new JLabel(product.getQuantity() + " units");
        curVal.setFont(UITheme.FONT_BOLD);
        curVal.setForeground(product.getQuantity() <= 5 ? UITheme.WARNING : UITheme.SUCCESS);

        summaryCard.add(nameKey);
        summaryCard.add(nameVal);
        summaryCard.add(curKey);
        summaryCard.add(curVal);

        center.add(summaryCard);
        center.add(Box.createVerticalStrut(16));

        // Radio options: Add vs Deduct
        addRadio = new JRadioButton("Add Stock (Receiving inventory)", true);
        addRadio.setFont(UITheme.FONT_BOLD);
        addRadio.setOpaque(false);

        subtractRadio = new JRadioButton("Deduct Stock (Sale / Outward dispatch)", false);
        subtractRadio.setFont(UITheme.FONT_BOLD);
        subtractRadio.setOpaque(false);

        ButtonGroup group = new ButtonGroup();
        group.add(addRadio);
        group.add(subtractRadio);

        center.add(addRadio);
        center.add(Box.createVerticalStrut(4));
        center.add(subtractRadio);
        center.add(Box.createVerticalStrut(14));

        // Amount input
        JLabel amountLabel = new JLabel("Quantity to adjust:");
        amountLabel.setFont(UITheme.FONT_BOLD);
        amountLabel.setForeground(UITheme.TEXT_SECONDARY);

        amountSpinner = new JSpinner(new SpinnerNumberModel(10, 1, 100_000, 1));
        amountSpinner.setFont(UITheme.FONT_BODY);
        amountSpinner.setPreferredSize(new Dimension(0, 36));
        amountSpinner.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));

        center.add(amountLabel);
        center.add(Box.createVerticalStrut(4));
        center.add(amountSpinner);
        center.add(Box.createVerticalStrut(14));

        // Live preview label
        previewLabel = new JLabel("New Stock Level: " + (product.getQuantity() + 10) + " units");
        previewLabel.setFont(UITheme.FONT_BOLD);
        previewLabel.setForeground(UITheme.PRIMARY);
        center.add(previewLabel);

        // Error label
        errorLabel = new JLabel(" ");
        errorLabel.setFont(UITheme.FONT_SMALL_BOLD);
        errorLabel.setForeground(UITheme.DANGER);
        center.add(Box.createVerticalStrut(6));
        center.add(errorLabel);

        // Update preview dynamically
        Runnable updatePreview = () -> {
            int amount = (Integer) amountSpinner.getValue();
            int newTotal = addRadio.isSelected() ? (product.getQuantity() + amount) : (product.getQuantity() - amount);
            if (newTotal < 0) {
                previewLabel.setText("Invalid: Resulting stock would be negative (" + newTotal + ")");
                previewLabel.setForeground(UITheme.DANGER);
            } else {
                previewLabel.setText("New Stock Level: " + newTotal + " units");
                previewLabel.setForeground(UITheme.PRIMARY);
            }
        };

        addRadio.addActionListener(e -> updatePreview.run());
        subtractRadio.addActionListener(e -> updatePreview.run());
        amountSpinner.addChangeListener(e -> updatePreview.run());

        root.add(center, BorderLayout.CENTER);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(new EmptyBorder(16, 0, 0, 0));

        JButton cancelBtn = CustomComponents.createSecondaryButton("Cancel", null);
        cancelBtn.addActionListener(e -> dispose());

        JButton confirmBtn = CustomComponents.createPrimaryButton("Update Stock", "✔");
        confirmBtn.addActionListener(e -> executeAdjustment());

        buttonPanel.add(cancelBtn);
        buttonPanel.add(confirmBtn);

        root.add(buttonPanel, BorderLayout.SOUTH);
        setContentPane(root);
    }

    private void executeAdjustment() {
        errorLabel.setText(" ");
        int amount = (Integer) amountSpinner.getValue();
        int delta = addRadio.isSelected() ? amount : -amount;

        try {
            productService.adjustStock(product.getProductId(), delta);
            saved = true;
            dispose();
        } catch (ValidationException ve) {
            errorLabel.setText(ve.getMessage());
        } catch (SQLException se) {
            errorLabel.setText("Database error: " + se.getMessage());
        }
    }

    public boolean isSaved() {
        return saved;
    }
}
