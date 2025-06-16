package ui;

import dao.CustomerDAO;
import model.Customer;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.util.List;

import static ui.UIStyle.*;

public class CustomerPanel extends JPanel {
    private JTable table;
    private DefaultTableModel model;

    public CustomerPanel() {
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);

        // Header
        JPanel header = new JPanel(new BorderLayout());
        styleHeader(header, "Customer Management");
        add(header, BorderLayout.NORTH);

        // Search panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBackground(Color.WHITE);
        searchPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        JTextField searchField = new JTextField(20);
        styleTextField(searchField);
        JButton searchBtn = new JButton("Search");
        styleButton(searchBtn, INFO);

        searchPanel.add(new JLabel("Search:"));
        searchPanel.add(searchField);
        searchPanel.add(searchBtn);

        // Table panel with border
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(Color.WHITE);
        tablePanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230)),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        // Table
       model = new DefaultTableModel(new Object[]{"ID", "Name", "Contact"}, 0) {
    public boolean isCellEditable(int row, int column) { return false; }
};
table = new JTable(model);
styleTable(table);

// Set font color to black
table.setForeground(Color.BLACK);
table.setFont(new Font("Segoe UI", Font.PLAIN, 12)); // Example font, adjust as needed

JScrollPane scrollPane = new JScrollPane(table);
scrollPane.setBorder(null);
tablePanel.add(scrollPane, BorderLayout.CENTER);
        // Button panel
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        btnPanel.setBackground(Color.WHITE);
        btnPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        JButton addBtn = new JButton("Add Customer");
        JButton updateBtn = new JButton("Update");
        JButton deleteBtn = new JButton("Delete");
        JButton refreshBtn = new JButton("Refresh");

        styleButton(addBtn, ACCENT);
        styleButton(updateBtn, SUCCESS);
        styleButton(deleteBtn, DANGER);
        styleButton(refreshBtn, INFO);

        btnPanel.add(addBtn);
        btnPanel.add(updateBtn);
        btnPanel.add(deleteBtn);
        btnPanel.add(refreshBtn);

        // Add all panels to main panel
        JPanel contentPanel = new JPanel(new BorderLayout(10, 10));
        contentPanel.setBackground(Color.WHITE);
        contentPanel.add(searchPanel, BorderLayout.NORTH);
        contentPanel.add(tablePanel, BorderLayout.CENTER);
        contentPanel.add(btnPanel, BorderLayout.SOUTH);

        add(contentPanel, BorderLayout.CENTER);

        // Add action listeners
        refreshTable();
        addBtn.addActionListener(e -> addCustomer());
        updateBtn.addActionListener(e -> updateCustomer());
        deleteBtn.addActionListener(e -> deleteCustomer());
        refreshBtn.addActionListener(e -> refreshTable());
        searchBtn.addActionListener(e -> {
            String searchText = searchField.getText().toLowerCase();
            model.setRowCount(0);
            List<Customer> list = CustomerDAO.getAll();
            for (Customer c : list) {
                if (c.getName().toLowerCase().contains(searchText) ||
                    c.getContact().toLowerCase().contains(searchText)) {
                    model.addRow(new Object[]{c.getId(), c.getName(), c.getContact()});
                }
            }
        });
    }

    private void refreshTable() {
        model.setRowCount(0);
        List<Customer> list = CustomerDAO.getAll();
        for (Customer c : list) {
            model.addRow(new Object[]{c.getId(), c.getName(), c.getContact()});
        }
    }

    private void addCustomer() {
        JPanel panel = new JPanel(new GridLayout(2, 2, 10, 10));
        panel.setBackground(Color.WHITE);
        
        JTextField nameField = new JTextField(20);
        JTextField contactField = new JTextField(20);
        
        styleTextField(nameField);
        styleTextField(contactField);
        
        panel.add(new JLabel("Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Contact:"));
        panel.add(contactField);

        int res = JOptionPane.showConfirmDialog(
            this,
            panel,
            "Add New Customer",
            JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.PLAIN_MESSAGE
        );

        if (res == JOptionPane.OK_OPTION) {
            Customer c = new Customer(0, nameField.getText(), contactField.getText());
            if (CustomerDAO.add(c)) {
                refreshTable();
                JOptionPane.showMessageDialog(this, "Customer added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Failed to add customer.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void updateCustomer() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a customer to update.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = (int) model.getValueAt(row, 0);
        JPanel panel = new JPanel(new GridLayout(2, 2, 10, 10));
        panel.setBackground(Color.WHITE);
        
        JTextField nameField = new JTextField((String) model.getValueAt(row, 1));
        JTextField contactField = new JTextField((String) model.getValueAt(row, 2));
        
        styleTextField(nameField);
        styleTextField(contactField);
        
        panel.add(new JLabel("Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Contact:"));
        panel.add(contactField);

        int res = JOptionPane.showConfirmDialog(
            this,
            panel,
            "Update Customer",
            JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.PLAIN_MESSAGE
        );

        if (res == JOptionPane.OK_OPTION) {
            Customer c = new Customer(id, nameField.getText(), contactField.getText());
            if (CustomerDAO.update(c)) {
                refreshTable();
                JOptionPane.showMessageDialog(this, "Customer updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update customer.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void deleteCustomer() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a customer to delete.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = (int) model.getValueAt(row, 0);
        int res = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to delete this customer?",
            "Confirm Delete",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );

        if (res == JOptionPane.YES_OPTION) {
            if (CustomerDAO.delete(id)) {
                refreshTable();
                JOptionPane.showMessageDialog(this, "Customer deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete customer.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
} 