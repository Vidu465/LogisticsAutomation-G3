package ui;

import dao.DeliveryPersonnelDAO;
import model.DeliveryPersonnel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

import static ui.UIStyle.*;

public class DeliveryPersonnelPanel extends JPanel {
    private JTable table;
    private DefaultTableModel model;

    public DeliveryPersonnelPanel() {
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);

        // Header
        JPanel header = new JPanel();
        header.setBackground(PRIMARY);
        JLabel title = new JLabel("Delivery Personnel Management");
        title.setForeground(Color.WHITE);
        title.setFont(HEADER_FONT);
        header.add(title);
        add(header, BorderLayout.NORTH);

        // Table
        model = new DefaultTableModel(new Object[]{"ID", "Name", "Contact", "Schedule", "Assigned Routes"}, 0) {
            public boolean isCellEditable(int row, int column) { return false; }
        };
        table = new JTable(model);
        table.setFont(LABEL_FONT);
        table.setRowHeight(24);
        table.getTableHeader().setFont(SECTION_FONT);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Buttons
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        btnPanel.setBackground(Color.WHITE);
        JButton addBtn = new JButton("Add");
        JButton updateBtn = new JButton("Update");
        JButton deleteBtn = new JButton("Delete");
        JButton refreshBtn = new JButton("Refresh");
        styleButton(addBtn, ACCENT);
        styleButton(updateBtn, SUCCESS);
        styleButton(deleteBtn, DANGER);
        styleButton(refreshBtn, INFO);
        btnPanel.add(addBtn); btnPanel.add(updateBtn); btnPanel.add(deleteBtn); btnPanel.add(refreshBtn);
        add(btnPanel, BorderLayout.SOUTH);

        refreshTable();

        addBtn.addActionListener(e -> addPersonnel());
        updateBtn.addActionListener(e -> updatePersonnel());
        deleteBtn.addActionListener(e -> deletePersonnel());
        refreshBtn.addActionListener(e -> refreshTable());
    }

    private void refreshTable() {
        model.setRowCount(0);
        List<DeliveryPersonnel> list = DeliveryPersonnelDAO.getAll();
        for (DeliveryPersonnel dp : list) {
            model.addRow(new Object[]{dp.getId(), dp.getName(), dp.getContact(), dp.getSchedule(), dp.getAssignedRoutes()});
        }
    }

    private void addPersonnel() {
        JTextField nameField = new JTextField();
        JTextField contactField = new JTextField();
        JTextField scheduleField = new JTextField();
        JTextField routesField = new JTextField();
        Object[] fields = {"Name:", nameField, "Contact:", contactField, "Schedule:", scheduleField, "Assigned Routes:", routesField};
        int res = JOptionPane.showConfirmDialog(this, fields, "Add Delivery Personnel", JOptionPane.OK_CANCEL_OPTION);
        if (res == JOptionPane.OK_OPTION) {
            DeliveryPersonnel dp = new DeliveryPersonnel(0, nameField.getText(), contactField.getText(), scheduleField.getText(), routesField.getText());
            if (DeliveryPersonnelDAO.add(dp)) {
                refreshTable();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to add.");
            }
        }
    }

    private void updatePersonnel() {
        int row = table.getSelectedRow();
        if (row == -1) { JOptionPane.showMessageDialog(this, "Select a row to update."); return; }
        int id = (int) model.getValueAt(row, 0);
        JTextField nameField = new JTextField((String) model.getValueAt(row, 1));
        JTextField contactField = new JTextField((String) model.getValueAt(row, 2));
        JTextField scheduleField = new JTextField((String) model.getValueAt(row, 3));
        JTextField routesField = new JTextField((String) model.getValueAt(row, 4));
        Object[] fields = {"Name:", nameField, "Contact:", contactField, "Schedule:", scheduleField, "Assigned Routes:", routesField};
        int res = JOptionPane.showConfirmDialog(this, fields, "Update Delivery Personnel", JOptionPane.OK_CANCEL_OPTION);
        if (res == JOptionPane.OK_OPTION) {
            DeliveryPersonnel dp = new DeliveryPersonnel(id, nameField.getText(), contactField.getText(), scheduleField.getText(), routesField.getText());
            if (DeliveryPersonnelDAO.update(dp)) {
                refreshTable();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update.");
            }
        }
    }

    private void deletePersonnel() {
        int row = table.getSelectedRow();
        if (row == -1) { JOptionPane.showMessageDialog(this, "Select a row to delete."); return; }
        int id = (int) model.getValueAt(row, 0);
        int res = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this personnel?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (res == JOptionPane.YES_OPTION) {
            if (DeliveryPersonnelDAO.delete(id)) {
                refreshTable();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete.");
            }
        }
    }
} 