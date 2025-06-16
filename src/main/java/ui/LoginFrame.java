package ui;

import dao.AdminDAO;
import javax.swing.*;
import java.awt.*;

import static ui.UIStyle.*;

public class LoginFrame extends JFrame {
    public LoginFrame() {
        setTitle("Logistic Automation - Login");
        setSize(400, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Main panel with background color
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(BACKGROUND);
        add(mainPanel);

        // Logo/Title panel
        JPanel logoPanel = new JPanel();
        logoPanel.setBackground(PRIMARY);
        logoPanel.setBorder(BorderFactory.createEmptyBorder(40, 0, 40, 0));
        JLabel titleLabel = new JLabel("Logistic Automation", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        logoPanel.add(titleLabel);
        mainPanel.add(logoPanel, BorderLayout.NORTH);

        // Login form panel
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        // Welcome text
        JLabel welcomeLabel = new JLabel("Welcome Back");
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        welcomeLabel.setForeground(TEXT_PRIMARY);
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        formPanel.add(welcomeLabel);
        formPanel.add(Box.createVerticalStrut(30));

        // Username field
        JLabel userLabel = new JLabel("Username");
        styleLabel(userLabel);
        userLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(userLabel);
        formPanel.add(Box.createVerticalStrut(5));

        JTextField userField = new JTextField(20);
        styleTextField(userField);
        userField.setMaximumSize(new Dimension(300, 35));
        userField.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(userField);
        formPanel.add(Box.createVerticalStrut(20));

        // Password field
        JLabel passLabel = new JLabel("Password");
        styleLabel(passLabel);
        passLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(passLabel);
        formPanel.add(Box.createVerticalStrut(5));

        JPasswordField passField = new JPasswordField(20);
        styleTextField(passField);
        passField.setMaximumSize(new Dimension(300, 35));
        passField.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(passField);
        formPanel.add(Box.createVerticalStrut(30));

        // Login button
        JButton loginBtn = new JButton("Login");
        styleButton(loginBtn, PRIMARY);
        loginBtn.setMaximumSize(new Dimension(300, BUTTON_HEIGHT));
        loginBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(loginBtn);

        // Add action listener
        loginBtn.addActionListener(e -> {
            String user = userField.getText();
            String pass = new String(passField.getPassword());
            if (AdminDAO.validate(user, pass)) {
                new MainDashboard().setVisible(true);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this,
                    "Invalid username or password",
                    "Login Failed",
                    JOptionPane.ERROR_MESSAGE);
                passField.setText("");
            }
        });

        // Add form panel to main panel
        mainPanel.add(formPanel, BorderLayout.CENTER);

        // Add enter key listener
        getRootPane().setDefaultButton(loginBtn);
    }
} 