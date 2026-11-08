package com.inventory.ui.theme;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Centralized Design System & Theme configuration.
 */
public class UITheme {

    // Brand & Primary Colors (Modern Indigo)
    public static final Color PRIMARY = new Color(79, 70, 229);         // #4F46E5
    public static final Color PRIMARY_HOVER = new Color(67, 56, 202);   // #4338CA
    public static final Color PRIMARY_LIGHT = new Color(238, 242, 255); // #EEF2FF

    // Sidebar Colors (Deep Dark Slate)
    public static final Color SIDEBAR_BG = new Color(15, 23, 42);        // #0F172A
    public static final Color SIDEBAR_CARD = new Color(30, 41, 59);      // #1E293B
    public static final Color SIDEBAR_HOVER = new Color(51, 65, 85);     // #334155
    public static final Color SIDEBAR_ACTIVE = new Color(79, 70, 229);   // #4F46E5
    public static final Color SIDEBAR_TEXT = new Color(148, 163, 184);   // #94A3B8
    public static final Color SIDEBAR_TEXT_ACTIVE = Color.WHITE;

    // Content Canvas & Card Colors
    public static final Color CONTENT_BG = new Color(248, 250, 252);     // #F8FAFC
    public static final Color CARD_BG = Color.WHITE;
    public static final Color CARD_BORDER = new Color(226, 232, 240);    // #E2E8F0
    public static final Color CARD_BORDER_FOCUSED = PRIMARY;

    // Text Colors
    public static final Color TEXT_PRIMARY = new Color(15, 23, 42);      // #0F172A
    public static final Color TEXT_SECONDARY = new Color(71, 85, 105);   // #475569
    public static final Color TEXT_MUTED = new Color(148, 163, 184);     // #94A3B8

    // Status Colors (Badges and alerts)
    public static final Color SUCCESS = new Color(16, 185, 129);         // #10B981
    public static final Color SUCCESS_BG = new Color(236, 253, 245);     // #ECFDF5
    public static final Color WARNING = new Color(245, 158, 11);         // #F59E0B
    public static final Color WARNING_BG = new Color(254, 243, 199);     // #FEF3C7
    public static final Color DANGER = new Color(239, 68, 68);           // #EF4444
    public static final Color DANGER_BG = new Color(254, 242, 242);      // #FEF2F2
    public static final Color INFO = new Color(59, 130, 246);             // #3B82F6
    public static final Color INFO_BG = new Color(239, 246, 255);        // #EFF6FF

    // Typography
    private static final String FONT_FAMILY = "Segoe UI";
    public static final Font FONT_TITLE_LARGE = new Font(FONT_FAMILY, Font.BOLD, 22);
    public static final Font FONT_TITLE = new Font(FONT_FAMILY, Font.BOLD, 17);
    public static final Font FONT_SUBTITLE = new Font(FONT_FAMILY, Font.BOLD, 14);
    public static final Font FONT_BODY = new Font(FONT_FAMILY, Font.PLAIN, 13);
    public static final Font FONT_BOLD = new Font(FONT_FAMILY, Font.BOLD, 13);
    public static final Font FONT_SMALL = new Font(FONT_FAMILY, Font.PLAIN, 11);
    public static final Font FONT_SMALL_BOLD = new Font(FONT_FAMILY, Font.BOLD, 11);
    public static final Font FONT_METRIC = new Font(FONT_FAMILY, Font.BOLD, 26);

    /**
     * Initializes the modern FlatLaf Look and Feel.
     * Falls back to System Look & Feel if FlatLaf cannot be loaded.
     */
    public static void setupLookAndFeel() {
        try {
            FlatLightLaf.setup();

            // Set global UI defaults for a modern dashboard look
            UIManager.put("Button.arc", 8);
            UIManager.put("Component.arc", 8);
            UIManager.put("TextComponent.arc", 8);
            UIManager.put("ScrollBar.thumbArc", 8);
            UIManager.put("ScrollBar.thumbInsets", new Insets(2, 2, 2, 2));
            UIManager.put("Table.selectionBackground", PRIMARY_LIGHT);
            UIManager.put("Table.selectionForeground", TEXT_PRIMARY);
            UIManager.put("Table.rowHeight", 38);
            UIManager.put("TableHeader.height", 36);
            UIManager.put("TableHeader.font", FONT_BOLD);
        } catch (Throwable t) {
            System.err.println("[UITheme] FlatLaf could not be initialized. Falling back to System L&F.");
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {}
        }
    }

    /**
     * Creates a styled card border with 1px outline and internal padding.
     */
    public static Border createCardBorder(int top, int left, int bottom, int right) {
        return new CompoundBorder(
                new LineBorder(CARD_BORDER, 1, true),
                new EmptyBorder(top, left, bottom, right)
        );
    }

    public static Border createCardBorder() {
        return createCardBorder(16, 16, 16, 16);
    }

    /**
     * Creates the branded application window & taskbar icon featuring the mint green InvenTrack emblem.
     */
    public static Image createAppIcon() {
        BufferedImage img = new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(new Color(15, 23, 42)); // Slate 900
        g2.fillRoundRect(2, 2, 60, 60, 16, 16);
        g2.setColor(new Color(51, 65, 85)); // Slate 700 border
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
}
