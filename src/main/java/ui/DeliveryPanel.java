package ui;

import dao.DeliveryDAO;
import dao.ShipmentDAO;
import dao.CustomerDAO;
import model.Delivery;
import model.Shipment;
import model.Customer;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static ui.UIStyle.*;

public class DeliveryPanel extends JPanel {
    private JTable table;
    private DefaultTableModel model;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    public DeliveryPanel() {
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);

        // Header
        JPanel header = new JPanel();
        header.setBackground(PRIMARY);
        JLabel title = new JLabel("Delivery Scheduling System");
        title.setForeground(Color.WHITE);
        title.setFont(HEADER_FONT);
        header.add(title);
        add(header, BorderLayout.NORTH);

        // Table
        model = new DefaultTableModel(new Object[]{"ID", "Shipment ID", "Customer ID", "Delivery Slot"}, 0) {
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

        addBtn.addActionListener(e -> addDelivery());
        updateBtn.addActionListener(e -> updateDelivery());
        deleteBtn.addActionListener(e -> deleteDelivery());
        refreshBtn.addActionListener(e -> refreshTable());
    }

    private void refreshTable() {
        model.setRowCount(0);
        List<Delivery> list = DeliveryDAO.getAll();
        for (Delivery d : list) {
            model.addRow(new Object[]{
                    d.getId(),
                    d.getShipmentId(),
                    d.getCustomerId(),
                    d.getDeliverySlot() != null ? sdf.format(d.getDeliverySlot()) : ""
            });
        }
    }

    private void addDelivery() {
        JComboBox<Shipment> shipmentBox = new JComboBox<>();
        for (Shipment s : ShipmentDAO.getAll()) shipmentBox.addItem(s);
        JComboBox<Customer> customerBox = new JComboBox<>();
        for (Customer c : CustomerDAO.getAll()) customerBox.addItem(c);
        JTextField slotField = new JTextField();
        Object[] fields = {"Shipment:", shipmentBox, "Customer:", customerBox, "Delivery Slot (yyyy-MM-dd HH:mm):", slotField};
        int res = JOptionPane.showConfirmDialog(this, fields, "Schedule Delivery", JOptionPane.OK_CANCEL_OPTION);
        if (res == JOptionPane.OK_OPTION) {
            Date slot = null;
            try { if (!slotField.getText().trim().isEmpty()) slot = sdf.parse(slotField.getText().trim()); } catch (ParseException ignored) {}
            Shipment s = (Shipment) shipmentBox.getSelectedItem();
            Customer c = (Customer) customerBox.getSelectedItem();
            if (s == null || c == null) {
                JOptionPane.showMessageDialog(this, "Select shipment and customer.");
                return;
            }
            Delivery d = new Delivery(0, s.getId(), c.getId(), slot);
            if (DeliveryDAO.add(d)) {
                refreshTable();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to schedule delivery.");
            }
        }
    }

    private void updateDelivery() {
        int row = table.getSelectedRow();
        if (row == -1) { JOptionPane.showMessageDialog(this, "Select a row to update."); return; }
        int id = (int) model.getValueAt(row, 0);
        JComboBox<Shipment> shipmentBox = new JComboBox<>();
        for (Shipment s : ShipmentDAO.getAll()) shipmentBox.addItem(s);
        JComboBox<Customer> customerBox = new JComboBox<>();
        for (Customer c : CustomerDAO.getAll()) customerBox.addItem(c);
        // Set selected items
        int shipmentId = (int) model.getValueAt(row, 1);
        int customerId = (int) model.getValueAt(row, 2);
        for (int i = 0; i < shipmentBox.getItemCount(); i++) if (shipmentBox.getItemAt(i).getId() == shipmentId) shipmentBox.setSelectedIndex(i);
        for (int i = 0; i < customerBox.getItemCount(); i++) if (customerBox.getItemAt(i).getId() == customerId) customerBox.setSelectedIndex(i);
        JTextField slotField = new JTextField((String) model.getValueAt(row, 3));
        Object[] fields = {"Shipment:", shipmentBox, "Customer:", customerBox, "Delivery Slot (yyyy-MM-dd HH:mm):", slotField};
        int res = JOptionPane.showConfirmDialog(this, fields, "Update Delivery", JOptionPane.OK_CANCEL_OPTION);
        if (res == JOptionPane.OK_OPTION) {
            Date slot = null;
            try { if (!slotField.getText().trim().isEmpty()) slot = sdf.parse(slotField.getText().trim()); } catch (ParseException ignored) {}
            Shipment s = (Shipment) shipmentBox.getSelectedItem();
            Customer c = (Customer) customerBox.getSelectedItem();
            if (s == null || c == null) {
                JOptionPane.showMessageDialog(this, "Select shipment and customer.");
                return;
            }
            Delivery d = new Delivery(id, s.getId(), c.getId(), slot);
            if (DeliveryDAO.update(d)) {
                refreshTable();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update delivery.");
            }
        }
    }

    private void deleteDelivery() {
        int row = table.getSelectedRow();
        if (row == -1) { JOptionPane.showMessageDialog(this, "Select a row to delete."); return; }
        int id = (int) model.getValueAt(row, 0);
        int res = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this delivery?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (res == JOptionPane.YES_OPTION) {
            if (DeliveryDAO.delete(id)) {
                refreshTable();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete delivery.");
            }
        }
    }
} 