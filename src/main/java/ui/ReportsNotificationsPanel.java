package ui;

import report.ReportGenerator;
import notification.EmailService;
import notification.SMSService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static ui.UIStyle.*;

public class ReportsNotificationsPanel extends JPanel {
    private static final int FIELD_WIDTH = 25;
    private static final int SMALL_FIELD_WIDTH = 15;
    
    public ReportsNotificationsPanel() {
        setLayout(new BorderLayout());
        setBackground(BACKGROUND);
        setBorder(new EmptyBorder(PADDING, PADDING, PADDING, PADDING));

        // Header
        JPanel header = createHeaderPanel();
        add(header, BorderLayout.NORTH);

        // Main content
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(BACKGROUND);
        
        // Report Generation Section
        contentPanel.add(createReportSection());
        contentPanel.add(Box.createVerticalStrut(30));
        
        // Notifications Section
        contentPanel.add(createNotificationSection());
        
        // Add scroll
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);
    }

    private JPanel createHeaderPanel() {
        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT));
        header.setBackground(PRIMARY);
        header.setBorder(new EmptyBorder(15, 20, 15, 20));
        
        JLabel title = new JLabel("Reports & Notifications");
        title.setForeground(Color.WHITE);
        title.setFont(HEADER_FONT);
        
        header.add(title);
        return header;
    }

    private JPanel createReportSection() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(230, 230, 230)),
                "Report Generation",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                SECTION_FONT,
                TEXT_PRIMARY
            ),
            new EmptyBorder(15, 15, 15, 15)
        ));
        
        // Date selection
        JPanel datePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        datePanel.setBackground(Color.WHITE);
        
        JLabel monthLabel = new JLabel("Select Month:");
        styleLabel(monthLabel);
        JComboBox<String> monthCombo = new JComboBox<>(getMonths());
        monthCombo.setFont(LABEL_FONT);
        
        JLabel yearLabel = new JLabel("Year:");
        styleLabel(yearLabel);
        JComboBox<Integer> yearCombo = new JComboBox<>(getYears());
        yearCombo.setFont(LABEL_FONT);
        
        datePanel.add(monthLabel);
        datePanel.add(monthCombo);
        datePanel.add(yearLabel);
        datePanel.add(yearCombo);
        panel.add(datePanel);
        panel.add(Box.createVerticalStrut(15));
        
        // Report type selection
        ButtonGroup reportGroup = new ButtonGroup();
        JRadioButton summaryRadio = new JRadioButton("Summary Report", true);
        JRadioButton detailedRadio = new JRadioButton("Detailed Report");
        summaryRadio.setFont(LABEL_FONT);
        detailedRadio.setFont(LABEL_FONT);
        reportGroup.add(summaryRadio);
        reportGroup.add(detailedRadio);
        
        JPanel typePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 5));
        typePanel.setBackground(Color.WHITE);
        
        JLabel typeLabel = new JLabel("Report Type:");
        styleLabel(typeLabel);
        typePanel.add(typeLabel);
        typePanel.add(summaryRadio);
        typePanel.add(detailedRadio);
        panel.add(typePanel);
        panel.add(Box.createVerticalStrut(20));
        
        // Generate button
        JButton generateBtn = new JButton("Generate Report");
        styleButton(generateBtn, ACCENT);
        generateBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        generateBtn.addActionListener(e -> generateReport(
            monthCombo.getSelectedItem().toString(),
            (Integer) yearCombo.getSelectedItem(),
            summaryRadio.isSelected() ? "summary" : "detailed"
        ));
        panel.add(generateBtn);
        
        return panel;
    }

    private JPanel createNotificationSection() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(230, 230, 230)),
                "Notification Services",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                SECTION_FONT,
                TEXT_PRIMARY
            ),
            new EmptyBorder(15, 15, 15, 15)
        ));
        
        // Email Notification
        JPanel emailPanel = createEmailPanel();
        panel.add(emailPanel);
        panel.add(Box.createVerticalStrut(20));
        
        // SMS Notification
        JPanel smsPanel = createSmsPanel();
        panel.add(smsPanel);
        
        return panel;
    }

    private JPanel createEmailPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(SUCCESS, 1),
            new EmptyBorder(15, 15, 15, 15)
        ));
        
        JLabel title = new JLabel("Email Notification");
        title.setFont(SECTION_FONT);
        title.setForeground(SUCCESS);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(title);
        panel.add(Box.createVerticalStrut(15));
        
        // To field
        JPanel toPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        toPanel.setBackground(Color.WHITE);
        JLabel toLabel = new JLabel("To:");
        styleLabel(toLabel);
        JTextField toField = new JTextField(FIELD_WIDTH);
        styleTextField(toField);
        toPanel.add(toLabel);
        toPanel.add(toField);
        panel.add(toPanel);
        panel.add(Box.createVerticalStrut(10));
        
        // Subject field
        JPanel subjectPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        subjectPanel.setBackground(Color.WHITE);
        JLabel subjectLabel = new JLabel("Subject:");
        styleLabel(subjectLabel);
        JTextField subjectField = new JTextField(FIELD_WIDTH);
        styleTextField(subjectField);
        subjectPanel.add(subjectLabel);
        subjectPanel.add(subjectField);
        panel.add(subjectPanel);
        panel.add(Box.createVerticalStrut(10));
        
        // Body field
        JPanel bodyPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        bodyPanel.setBackground(Color.WHITE);
        JLabel bodyLabel = new JLabel("Body:");
        styleLabel(bodyLabel);
        JTextField bodyField = new JTextField(FIELD_WIDTH);
        styleTextField(bodyField);
        bodyPanel.add(bodyLabel);
        bodyPanel.add(bodyField);
        panel.add(bodyPanel);
        panel.add(Box.createVerticalStrut(20));
        
        // Send button
        JButton sendBtn = new JButton("Send Test Email");
        styleButton(sendBtn, SUCCESS);
        sendBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        sendBtn.addActionListener(e -> {
            if (toField.getText().isEmpty() || subjectField.getText().isEmpty() || bodyField.getText().isEmpty()) {
                showError("Please fill all email fields");
                return;
            }
            EmailService.sendEmail(toField.getText(), subjectField.getText(), bodyField.getText());
            showSuccess("Email sent (if configured correctly)");
        });
        panel.add(sendBtn);
        
        return panel;
    }

    private JPanel createSmsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(INFO, 1),
            new EmptyBorder(15, 15, 15, 15)
        ));
        
        JLabel title = new JLabel("SMS Notification");
        title.setFont(SECTION_FONT);
        title.setForeground(INFO);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(title);
        panel.add(Box.createVerticalStrut(15));
        
        // To field
        JPanel toPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        toPanel.setBackground(Color.WHITE);
        JLabel toLabel = new JLabel("To:");
        styleLabel(toLabel);
        JTextField toField = new JTextField(FIELD_WIDTH);
        styleTextField(toField);
        toPanel.add(toLabel);
        toPanel.add(toField);
        panel.add(toPanel);
        panel.add(Box.createVerticalStrut(10));
        
        // Message field
        JPanel messagePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        messagePanel.setBackground(Color.WHITE);
        JLabel messageLabel = new JLabel("Message:");
        styleLabel(messageLabel);
        JTextField messageField = new JTextField(FIELD_WIDTH);
        styleTextField(messageField);
        messagePanel.add(messageLabel);
        messagePanel.add(messageField);
        panel.add(messagePanel);
        panel.add(Box.createVerticalStrut(20));
        
        // Send button
        JButton sendBtn = new JButton("Send Test SMS");
        styleButton(sendBtn, INFO);
        sendBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        sendBtn.addActionListener(e -> {
            if (toField.getText().isEmpty() || messageField.getText().isEmpty()) {
                showError("Please fill all SMS fields");
                return;
            }
            SMSService.sendSMS(toField.getText(), messageField.getText());
            showSuccess("SMS sent (if configured correctly)");
        });
        panel.add(sendBtn);
        
        return panel;
    }

    private void generateReport(String month, int year, String reportType) {
        String filePath = String.format("%sReport-%d-%s.pdf", 
            reportType.substring(0, 1).toUpperCase() + reportType.substring(1),
            year,
            month.toLowerCase());
        
        String content = String.format("This is a %s report for %s %d.\nReplace with real data as needed.",
            reportType, month, year);
        
        ReportGenerator.generateMonthlyReport(filePath, content);
        
        JOptionPane.showMessageDialog(this, 
            String.format("%s report generated:\n%s", 
                reportType.substring(0, 1).toUpperCase() + reportType.substring(1),
                filePath),
            "Report Generated",
            JOptionPane.INFORMATION_MESSAGE);
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void showSuccess(String message) {
        JOptionPane.showMessageDialog(this, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    private String[] getMonths() {
        return new String[]{"January", "February", "March", "April", "May", "June", 
                          "July", "August", "September", "October", "November", "December"};
    }

    private Integer[] getYears() {
        int currentYear = LocalDate.now().getYear();
        return new Integer[]{currentYear - 1, currentYear, currentYear + 1};
    }
} 