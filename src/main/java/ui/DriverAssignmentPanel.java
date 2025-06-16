package ui;

import dao.DeliveryPersonnelDAO;
import dao.ShipmentDAO;
import model.DeliveryPersonnel;
import model.Shipment;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

import static ui.UIStyle.*;

public class DriverAssignmentPanel extends JPanel {
    private JTable table;
    private DefaultTableModel model;

    public DriverAssignmentPanel() {
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);

        // Header
        JPanel header = new JPanel();
        header.setBackground(PRIMARY);
        JLabel title = new JLabel("Driver Assignment");
        title.setForeground(Color.WHITE);
        title.setFont(HEADER_FONT);
        header.add(title);
        add(header, BorderLayout.NORTH);

        // Table
        model = new DefaultTableModel(new Object[]{"ID", "Sender", "Receiver", "Status", "Location", "Est. Delivery"}, 0) {
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
        JButton assignBtn = new JButton("Assign Driver");
        JButton addShipmentBtn = new JButton("Add Shipment");
        JButton editShipmentBtn = new JButton("Edit Shipment");
        JButton refreshBtn = new JButton("Refresh");
        styleButton(assignBtn, ACCENT);
        styleButton(addShipmentBtn, SUCCESS);
        styleButton(editShipmentBtn, INFO);
        styleButton(refreshBtn, INFO);
        btnPanel.add(addShipmentBtn); btnPanel.add(editShipmentBtn); btnPanel.add(assignBtn); btnPanel.add(refreshBtn);
        add(btnPanel, BorderLayout.SOUTH);

        refreshTable();

        assignBtn.addActionListener(e -> assignDriver());
        addShipmentBtn.addActionListener(e -> addShipment());
        editShipmentBtn.addActionListener(e -> editShipment());
        refreshBtn.addActionListener(e -> refreshTable());
    }

    private void refreshTable() {
        model.setRowCount(0);
        List<Shipment> list = ShipmentDAO.getAll();
        for (Shipment s : list) {
            if (s.getAssignedDriverId() == null) {
                model.addRow(new Object[]{
                        s.getId(),
                        s.getSender(),
                        s.getReceiver(),
                        s.getStatus(),
                        s.getLocation(),
                        s.getEstimatedDelivery()
                });
            }
        }
    }

    private void assignDriver() {
        int row = table.getSelectedRow();
        if (row == -1) { JOptionPane.showMessageDialog(this, "Select a shipment to assign a driver."); return; }
        int shipmentId = (int) model.getValueAt(row, 0);
        JComboBox<DeliveryPersonnel> driverBox = new JComboBox<>();
        for (DeliveryPersonnel dp : DeliveryPersonnelDAO.getAll()) driverBox.addItem(dp);
        Object[] fields = {"Assign Driver:", driverBox};
        int res = JOptionPane.showConfirmDialog(this, fields, "Assign Driver", JOptionPane.OK_CANCEL_OPTION);
        if (res == JOptionPane.OK_OPTION) {
            DeliveryPersonnel dp = (DeliveryPersonnel) driverBox.getSelectedItem();
            if (dp == null) {
                JOptionPane.showMessageDialog(this, "Select a driver.");
                return;
            }
            // Get the shipment and update its assignedDriverId
            List<Shipment> shipments = ShipmentDAO.getAll();
            Shipment selected = null;
            for (Shipment s : shipments) if (s.getId() == shipmentId) selected = s;
            if (selected != null) {
                selected.setAssignedDriverId(dp.getId());
                if (ShipmentDAO.update(selected)) {
                    refreshTable();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to assign driver.");
                }
            }
        }
    }

    private void addShipment() {
        JTextField senderField = new JTextField();
        JTextField receiverField = new JTextField();
        JTextField contentsField = new JTextField();
        JTextField statusField = new JTextField();
        JTextField locationField = new JTextField();
        JTextField estDeliveryField = new JTextField();
        Object[] fields = {"Sender:", senderField, "Receiver:", receiverField, "Contents:", contentsField, "Status:", statusField, "Location:", locationField, "Est. Delivery (yyyy-MM-dd HH:mm):", estDeliveryField};
        int res = JOptionPane.showConfirmDialog(this, fields, "Add Shipment", JOptionPane.OK_CANCEL_OPTION);
        if (res == JOptionPane.OK_OPTION) {
            java.util.Date estDelivery = null;
            try { if (!estDeliveryField.getText().trim().isEmpty()) estDelivery = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm").parse(estDeliveryField.getText().trim()); } catch (Exception ignored) {}
            model.Shipment s = new model.Shipment(0, senderField.getText(), receiverField.getText(), contentsField.getText(), statusField.getText(), locationField.getText(), estDelivery, null);
            if (dao.ShipmentDAO.add(s)) {
                refreshTable();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to add shipment.");
            }
        }
    }

    private void editShipment() {
        int row = table.getSelectedRow();
        if (row == -1) { JOptionPane.showMessageDialog(this, "Select a shipment to edit."); return; }
        int id = (int) model.getValueAt(row, 0);
        JTextField senderField = new JTextField((String) model.getValueAt(row, 1));
        JTextField receiverField = new JTextField((String) model.getValueAt(row, 2));
        JTextField statusField = new JTextField((String) model.getValueAt(row, 3));
        JTextField locationField = new JTextField((String) model.getValueAt(row, 4));
        JTextField estDeliveryField = new JTextField(model.getValueAt(row, 5) != null ? model.getValueAt(row, 5).toString() : "");
        JTextField contentsField = new JTextField(); // Not shown in table, so leave blank or fetch from DB if needed
        Object[] fields = {"Sender:", senderField, "Receiver:", receiverField, "Contents:", contentsField, "Status:", statusField, "Location:", locationField, "Est. Delivery (yyyy-MM-dd HH:mm):", estDeliveryField};
        int res = JOptionPane.showConfirmDialog(this, fields, "Edit Shipment", JOptionPane.OK_CANCEL_OPTION);
        if (res == JOptionPane.OK_OPTION) {
            java.util.Date estDelivery = null;
            try { if (!estDeliveryField.getText().trim().isEmpty()) estDelivery = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm").parse(estDeliveryField.getText().trim()); } catch (Exception ignored) {}
            model.Shipment s = new model.Shipment(id, senderField.getText(), receiverField.getText(), contentsField.getText(), statusField.getText(), locationField.getText(), estDelivery, null);
            if (dao.ShipmentDAO.update(s)) {
                refreshTable();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update shipment.");
            }
        }
    }
} 