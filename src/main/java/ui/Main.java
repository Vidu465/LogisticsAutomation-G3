package ui;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // Set look and feel to system default
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}
        // Launch login frame
        SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
    }
} 