package ui;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public final class UIStyle {
    // Modern color scheme
    public static final Color PRIMARY = new Color(41, 128, 185);    // Modern blue
    public static final Color SECONDARY = new Color(52, 152, 219);  // Lighter blue
    public static final Color ACCENT = new Color(46, 204, 113);     // Fresh green
    public static final Color SUCCESS = new Color(39, 174, 96);     // Dark green
    public static final Color DANGER = new Color(231, 76, 60);      // Red
    public static final Color WARNING = new Color(241, 196, 15);    // Yellow
    public static final Color INFO = new Color(52, 152, 219);       // Info blue
    public static final Color BACKGROUND = new Color(236, 240, 241); // Light gray
    public static final Color TEXT_PRIMARY = new Color(44, 62, 80);  // Dark blue-gray
    public static final Color TEXT_SECONDARY = new Color(149, 165, 166); // Gray

    // Fonts
    public static final Font HEADER_FONT = new Font("Segoe UI", Font.BOLD, 24);
    public static final Font SECTION_FONT = new Font("Segoe UI", Font.BOLD, 18);
    public static final Font LABEL_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font BUTTON_FONT = new Font("Segoe UI", Font.BOLD, 14);
    public static final Font TABLE_HEADER_FONT = new Font("Segoe UI", Font.BOLD, 14);
    public static final Font TABLE_FONT = new Font("Segoe UI", Font.PLAIN, 13);

    // Common dimensions
    public static final int PADDING = 15;
    public static final int BUTTON_HEIGHT = 40;
    public static final int BUTTON_WIDTH = 120;
    public static final int TABLE_ROW_HEIGHT = 35;

    public static void styleButton(JButton btn, Color bg) {
        btn.setFont(BUTTON_FONT);
        btn.setForeground(Color.WHITE);
        btn.setBackground(bg);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setOpaque(true);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        btn.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));

        // Add hover effect
        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(bg.darker());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btn.setBackground(bg);
            }
        });
    }

    public static void styleTable(JTable table) {
        table.setFont(TABLE_FONT);
        table.setRowHeight(TABLE_ROW_HEIGHT);
        table.setGridColor(new Color(236, 240, 241));
        table.setSelectionBackground(SECONDARY);
        table.setSelectionForeground(Color.WHITE);
        table.setShowGrid(true);
        table.setIntercellSpacing(new Dimension(0, 0));
        
        // Style header
        table.getTableHeader().setFont(TABLE_HEADER_FONT);
        table.getTableHeader().setBackground(PRIMARY);
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        // Remove focus border
        table.setFocusable(false);
        table.setRowSelectionAllowed(true);
        table.setColumnSelectionAllowed(false);
    }

    public static void stylePanel(JPanel panel) {
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(PADDING, PADDING, PADDING, PADDING));
    }

    public static void styleHeader(JPanel header, String title) {
        header.setBackground(PRIMARY);
        header.setBorder(BorderFactory.createEmptyBorder(PADDING, PADDING * 2, PADDING, PADDING * 2));
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(HEADER_FONT);
        titleLabel.setForeground(Color.WHITE);
        header.add(titleLabel);
    }

    public static void styleTextField(JTextField field) {
        field.setFont(LABEL_FONT);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(TEXT_SECONDARY, 1),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
    }

    public static void styleLabel(JLabel label) {
        label.setFont(LABEL_FONT);
        label.setForeground(TEXT_PRIMARY);
    }
}