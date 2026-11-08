package com.inventory.ui.theme;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * Reusable custom UI components styled for a modern web-dashboard appearance in Java Swing.
 */
public class CustomComponents {

    private static final NumberFormat CURRENCY_FORMAT = NumberFormat.getCurrencyInstance(Locale.of("en", "IN"));

    /**
     * Dashboard KPI Stat Card component.
     */
    public static class StatCard extends JPanel {
        private final JLabel valueLabel;
        private final JLabel captionLabel;

        public StatCard(String title, String initialValue, String caption, Color accentColor, String iconSymbol) {
            setLayout(new BorderLayout(12, 8));
            setBackground(UITheme.CARD_BG);
            setBorder(new CompoundBorder(
                    new LineBorder(UITheme.CARD_BORDER, 1, true),
                    new EmptyBorder(16, 18, 16, 18)
            ));

            // Top Header: Title and Icon Badge
            JPanel topPanel = new JPanel(new BorderLayout());
            topPanel.setOpaque(false);

            JLabel titleLabel = new JLabel(title.toUpperCase());
            titleLabel.setFont(UITheme.FONT_SMALL_BOLD);
            titleLabel.setForeground(UITheme.TEXT_MUTED);

            JLabel iconBadge = new JLabel(iconSymbol, SwingConstants.CENTER) {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(new Color(accentColor.getRed(), accentColor.getGreen(), accentColor.getBlue(), 30));
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                    g2.dispose();
                    super.paintComponent(g);
                }
            };
            iconBadge.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 18));
            iconBadge.setForeground(accentColor);
            iconBadge.setPreferredSize(new Dimension(36, 36));

            topPanel.add(titleLabel, BorderLayout.WEST);
            topPanel.add(iconBadge, BorderLayout.EAST);

            // Center Panel: Large Metric Value
            valueLabel = new JLabel(initialValue);
            valueLabel.setFont(UITheme.FONT_METRIC);
            valueLabel.setForeground(UITheme.TEXT_PRIMARY);

            // Bottom Panel: Subtitle / Caption
            captionLabel = new JLabel(caption);
            captionLabel.setFont(UITheme.FONT_SMALL);
            captionLabel.setForeground(UITheme.TEXT_SECONDARY);

            JPanel centerPanel = new JPanel();
            centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
            centerPanel.setOpaque(false);
            centerPanel.add(valueLabel);
            centerPanel.add(Box.createVerticalStrut(4));
            centerPanel.add(captionLabel);

            add(topPanel, BorderLayout.NORTH);
            add(centerPanel, BorderLayout.CENTER);
        }

        public void setValue(String value) {
            valueLabel.setText(value);
        }

        public void setCaption(String caption) {
            captionLabel.setText(caption);
        }
    }

    /**
     * Primary action button (Modern Indigo filled button with hover effect).
     */
    public static JButton createPrimaryButton(String text, String icon) {
        JButton btn = new JButton((icon != null && !icon.isEmpty() ? icon + "  " : "") + text);
        btn.setFont(UITheme.FONT_BOLD);
        btn.setForeground(Color.WHITE);
        btn.setBackground(UITheme.PRIMARY);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(new EmptyBorder(10, 18, 10, 18));

        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(UITheme.PRIMARY_HOVER);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                btn.setBackground(UITheme.PRIMARY);
            }
        });

        return btn;
    }

    /**
     * Secondary / Outlined button for neutral actions (e.g. Cancel, Reset, Refresh).
     */
    public static JButton createSecondaryButton(String text, String icon) {
        JButton btn = new JButton((icon != null && !icon.isEmpty() ? icon + "  " : "") + text);
        btn.setFont(UITheme.FONT_BOLD);
        btn.setForeground(UITheme.TEXT_PRIMARY);
        btn.setBackground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(new CompoundBorder(
                new LineBorder(UITheme.CARD_BORDER, 1, true),
                new EmptyBorder(9, 16, 9, 16)
        ));

        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(UITheme.CONTENT_BG);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                btn.setBackground(Color.WHITE);
            }
        });

        return btn;
    }

    /**
     * Danger button for deletion actions with warning hover.
     */
    public static JButton createDangerButton(String text, String icon) {
        JButton btn = new JButton((icon != null && !icon.isEmpty() ? icon + "  " : "") + text);
        btn.setFont(UITheme.FONT_BOLD);
        btn.setForeground(UITheme.DANGER);
        btn.setBackground(UITheme.DANGER_BG);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(new CompoundBorder(
                new LineBorder(new Color(254, 202, 202), 1, true),
                new EmptyBorder(9, 16, 9, 16)
        ));

        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(UITheme.DANGER);
                btn.setForeground(Color.WHITE);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                btn.setBackground(UITheme.DANGER_BG);
                btn.setForeground(UITheme.DANGER);
            }
        });

        return btn;
    }

    /**
     * Table Cell Renderer that displays stock status as an elegant colored pill badge:
     * - IN STOCK (Green badge)
     * - LOW STOCK (Amber badge)
     * - OUT OF STOCK (Red badge)
     */
    public static class StatusBadgeRenderer extends DefaultTableCellRenderer {
        private final JLabel badge = new JLabel("", SwingConstants.CENTER) {
            private Color bgBadge = UITheme.SUCCESS_BG;
            private Color borderBadge = UITheme.SUCCESS;

            public void setBadgeColors(Color bg, Color border) {
                this.bgBadge = bg;
                this.borderBadge = border;
            }

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(bgBadge);
                g2.fillRoundRect(8, 6, getWidth() - 16, getHeight() - 12, 12, 12);
                g2.setColor(borderBadge);
                g2.drawRoundRect(8, 6, getWidth() - 16, getHeight() - 12, 12, 12);
                g2.dispose();
                super.paintComponent(g);
            }
        };

        public StatusBadgeRenderer() {
            badge.setOpaque(false);
            badge.setFont(UITheme.FONT_SMALL_BOLD);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {
            String status = value != null ? value.toString() : "";
            badge.setText(status);

            if ("OUT OF STOCK".equalsIgnoreCase(status)) {
                badge.setForeground(new Color(185, 28, 28));
                badge.setBackground(UITheme.DANGER_BG);
            } else if ("LOW STOCK".equalsIgnoreCase(status)) {
                badge.setForeground(new Color(180, 83, 9));
                badge.setBackground(UITheme.WARNING_BG);
            } else {
                badge.setForeground(new Color(4, 120, 87));
                badge.setBackground(UITheme.SUCCESS_BG);
            }

            JPanel container = new JPanel(new BorderLayout());
            container.setOpaque(true);
            container.setBackground(isSelected ? table.getSelectionBackground() : (row % 2 == 0 ? Color.WHITE : UITheme.CONTENT_BG));
            container.add(badge, BorderLayout.CENTER);
            return container;
        }
    }

    /**
     * Formats a monetary amount into the Indian currency numbering format (Lakhs and Crores).
     * e.g. 207000.00 -> ₹2,07,000.00 (commas in 2-digit groups after the first 3 digits).
     */
    public static String formatIndianCurrency(BigDecimal amount) {
        if (amount == null) return "₹0.00";
        boolean isNegative = amount.compareTo(BigDecimal.ZERO) < 0;
        BigDecimal absAmount = amount.abs().setScale(2, java.math.RoundingMode.HALF_UP);
        String str = absAmount.toPlainString();
        String[] parts = str.split("\\.");
        String intPart = parts[0];
        String decPart = parts.length > 1 ? parts[1] : "00";

        String formattedInt;
        if (intPart.length() <= 3) {
            formattedInt = intPart;
        } else {
            String last3 = intPart.substring(intPart.length() - 3);
            String rest = intPart.substring(0, intPart.length() - 3);
            String formattedRest = rest.replaceAll("\\B(?=(\\d{2})+(?!\\d))", ",");
            formattedInt = formattedRest + "," + last3;
        }

        return (isNegative ? "-₹" : "₹") + formattedInt + "." + decPart;
    }

    public static String formatIndianCurrency(Number amount) {
        if (amount == null) return "₹0.00";
        return formatIndianCurrency(BigDecimal.valueOf(amount.doubleValue()));
    }

    /**
     * Table Cell Renderer for currency values in Indian format (₹ XX,XX,XXX.XX)
     */
    public static class CurrencyRenderer extends DefaultTableCellRenderer {
        public CurrencyRenderer() {
            setHorizontalAlignment(SwingConstants.RIGHT);
            setFont(UITheme.FONT_BODY);
        }

        @Override
        protected void setValue(Object value) {
            if (value instanceof BigDecimal bd) {
                setText(formatIndianCurrency(bd));
            } else if (value instanceof Number n) {
                setText(formatIndianCurrency(n));
            } else if (value != null) {
                try {
                    BigDecimal bd = new BigDecimal(value.toString().replace("₹", "").replace(",", "").trim());
                    setText(formatIndianCurrency(bd));
                } catch (Exception e) {
                    setText(value.toString());
                }
            } else {
                setText("₹0.00");
            }
        }
    }
}
