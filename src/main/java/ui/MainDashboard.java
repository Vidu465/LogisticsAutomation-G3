package ui;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

import static ui.UIStyle.*;

public class MainDashboard extends JFrame {
    private final CardLayout cardLayout = new CardLayout();
    private final JPanel mainPanel = new JPanel(cardLayout);
    private final Map<String, JPanel> panelMap = new HashMap<>();
    private JPanel sidebar;

    public MainDashboard() {
        initializeFrame();
        setupSidebar();
        setupMainContent();
        
        // Show default panel
        cardLayout.show(mainPanel, "Shipments");
    }

    private void initializeFrame() {
        setTitle("Logistic Automation Dashboard");
        setSize(1280, 800);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(BACKGROUND);
    }

    private void setupSidebar() {
        sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(Color.WHITE);
        sidebar.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, new Color(230, 230, 230)));
        sidebar.setPreferredSize(new Dimension(250, 0));

        // Add logo/header
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBackground(PRIMARY);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        headerPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel logoLabel = new JLabel("Logistic Automation");
        logoLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        logoLabel.setForeground(Color.WHITE);
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        headerPanel.add(logoLabel);

        JLabel subtitleLabel = new JLabel("Management System");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(Color.WHITE);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        headerPanel.add(subtitleLabel);

        sidebar.add(headerPanel);
        sidebar.add(Box.createVerticalStrut(20));

        // Navigation buttons
        String[] functions = {
            "Shipments", "Tracking",
            "Delivery Personnel", "Delivery Assignment",
            "Customers", "Reports and Notification",
            "Deliveries"
        };

        for (String func : functions) {
            addSidebarButton(func);
        }

        sidebar.add(Box.createVerticalGlue());
        addLogoutButton();
        add(sidebar, BorderLayout.WEST);
    }

    private void addSidebarButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(BUTTON_FONT);
        btn.setForeground(TEXT_PRIMARY);
        btn.setBackground(Color.WHITE);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setMaximumSize(new Dimension(230, 45));
        btn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // Hover effects
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (!btn.getBackground().equals(PRIMARY)) {
                    btn.setBackground(BACKGROUND);
                }
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                if (!btn.getBackground().equals(PRIMARY)) {
                    btn.setBackground(Color.WHITE);
                }
            }
        });

        btn.addActionListener(e -> {
            // Reset all buttons
            for (Component comp : sidebar.getComponents()) {
                if (comp instanceof JButton) {
                    comp.setBackground(Color.WHITE);
                    ((JButton) comp).setForeground(TEXT_PRIMARY);
                }
            }
            // Set active button
            btn.setBackground(PRIMARY);
            btn.setForeground(Color.WHITE);
            
            cardLayout.show(mainPanel, text);
        });

        sidebar.add(btn);
        sidebar.add(Box.createVerticalStrut(5));
    }

    private void addLogoutButton() {
        JButton logoutBtn = new JButton("Logout");
        styleButton(logoutBtn, DANGER);
        logoutBtn.setMaximumSize(new Dimension(200, BUTTON_HEIGHT));
        logoutBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        logoutBtn.addActionListener(e -> {
            int res = JOptionPane.showConfirmDialog(
                this, 
                "Are you sure you want to logout?", 
                "Confirm Logout", 
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
            );
            if (res == JOptionPane.YES_OPTION) {
                new LoginFrame().setVisible(true);
                dispose();
            }
        });
        
        sidebar.add(logoutBtn);
        sidebar.add(Box.createVerticalStrut(20));
    }

    private void setupMainContent() {
        // Initialize all panels
        panelMap.put("Shipments", new ShipmentsPanel());
        panelMap.put("Tracking", new TrackingPanel());
        panelMap.put("Delivery Personnel", new DeliveryPersonnelPanel());
        panelMap.put("Delivery Assignment", new DriverAssignmentPanel());
        panelMap.put("Customers", new CustomerPanel());
        panelMap.put("Reports and Notification", new ReportsNotificationsPanel());
        panelMap.put("Deliveries", new DeliveryPanel());

        // Add all panels to main panel
        for (Map.Entry<String, JPanel> entry : panelMap.entrySet()) {
            JPanel panel = entry.getValue();
            panel.setBorder(BorderFactory.createEmptyBorder(PADDING, PADDING, PADDING, PADDING));
            mainPanel.add(panel, entry.getKey());
        }
        
        add(mainPanel, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> {
            MainDashboard dashboard = new MainDashboard();
            dashboard.setVisible(true);
        });
    }
}