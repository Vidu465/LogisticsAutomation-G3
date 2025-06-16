package ui;

import dao.ShipmentDAO;
import model.Shipment;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static ui.UIStyle.*;

public class ShipmentsPanel extends JPanel {
    private JTable table;
    private DefaultTableModel model;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    public ShipmentsPanel() {
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);

        // Header
        JPanel header = new JPanel();
        header.setBackground(PRIMARY);
        JLabel title = new JLabel("Shipment Management System");
        title.setForeground(Color.WHITE);
        title.setFont(HEADER_FONT);
        header.add(title);
        add(header, BorderLayout.NORTH);

        // Table
        model = new DefaultTableModel(new Object[]{"ID", "Sender", "Receiver", "Contents", "Status", "Location", "Est. Delivery", "Driver ID"}, 0) {
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

        addBtn.addActionListener(e -> addShipment());
        updateBtn.addActionListener(e -> updateShipment());
        deleteBtn.addActionListener(e -> deleteShipment());
        refreshBtn.addActionListener(e -> refreshTable());
    }

    private void refreshTable() {
        model.setRowCount(0);
        List<Shipment> list = ShipmentDAO.getAll();
        for (Shipment s : list) {
            model.addRow(new Object[]{
                    s.getId(),
                    s.getSender(),
                    s.getReceiver(),
                    s.getContents(),
                    s.getStatus(),
                    s.getLocation(),
                    s.getEstimatedDelivery() != null ? sdf.format(s.getEstimatedDelivery()) : "",
                    s.getAssignedDriverId() != null ? s.getAssignedDriverId() : ""
            });
        }
    }

    private void addShipment() {
        JTextField senderField = new JTextField();
        JTextField receiverField = new JTextField();
        JTextField contentsField = new JTextField();
        JTextField statusField = new JTextField();
        JTextField locationField = new JTextField();
        JTextField estDeliveryField = new JTextField();
        JTextField driverIdField = new JTextField();
        Object[] fields = {"Sender:", senderField, "Receiver:", receiverField, "Contents:", contentsField, "Status:", statusField, "Location:", locationField, "Est. Delivery (yyyy-MM-dd HH:mm):", estDeliveryField, "Driver ID (optional):", driverIdField};
        int res = JOptionPane.showConfirmDialog(this, fields, "Add Shipment", JOptionPane.OK_CANCEL_OPTION);
        if (res == JOptionPane.OK_OPTION) {
            Date estDelivery = null;
            try { if (!estDeliveryField.getText().trim().isEmpty()) estDelivery = sdf.parse(estDeliveryField.getText().trim()); } catch (ParseException ignored) {}
            Integer driverId = null;
            try { if (!driverIdField.getText().trim().isEmpty()) driverId = Integer.parseInt(driverIdField.getText().trim()); } catch (Exception ignored) {}
            Shipment s = new Shipment(0, senderField.getText(), receiverField.getText(), contentsField.getText(), statusField.getText(), locationField.getText(), estDelivery, driverId);
            if (ShipmentDAO.add(s)) {
                refreshTable();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to add.");
            }
        }
    }

    private void updateShipment() {
        int row = table.getSelectedRow();
        if (row == -1) { JOptionPane.showMessageDialog(this, "Select a row to update."); return; }
        int id = (int) model.getValueAt(row, 0);
        JTextField senderField = new JTextField((String) model.getValueAt(row, 1));
        JTextField receiverField = new JTextField((String) model.getValueAt(row, 2));
        JTextField contentsField = new JTextField((String) model.getValueAt(row, 3));
        JTextField statusField = new JTextField((String) model.getValueAt(row, 4));
        JTextField locationField = new JTextField((String) model.getValueAt(row, 5));
        JTextField estDeliveryField = new JTextField((String) model.getValueAt(row, 6));
        JTextField driverIdField = new JTextField(model.getValueAt(row, 7) != null ? model.getValueAt(row, 7).toString() : "");
        Object[] fields = {"Sender:", senderField, "Receiver:", receiverField, "Contents:", contentsField, "Status:", statusField, "Location:", locationField, "Est. Delivery (yyyy-MM-dd HH:mm):", estDeliveryField, "Driver ID (optional):", driverIdField};
        int res = JOptionPane.showConfirmDialog(this, fields, "Update Shipment", JOptionPane.OK_CANCEL_OPTION);
        if (res == JOptionPane.OK_OPTION) {
            Date estDelivery = null;
            try { if (!estDeliveryField.getText().trim().isEmpty()) estDelivery = sdf.parse(estDeliveryField.getText().trim()); } catch (ParseException ignored) {}
            Integer driverId = null;
            try { if (!driverIdField.getText().trim().isEmpty()) driverId = Integer.parseInt(driverIdField.getText().trim()); } catch (Exception ignored) {}
            Shipment s = new Shipment(id, senderField.getText(), receiverField.getText(), contentsField.getText(), statusField.getText(), locationField.getText(), estDelivery, driverId);
            if (ShipmentDAO.update(s)) {
                refreshTable();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update.");
            }
        }
    }

    private void deleteShipment() {
        int row = table.getSelectedRow();
        if (row == -1) { JOptionPane.showMessageDialog(this, "Select a row to delete."); return; }
        int id = (int) model.getValueAt(row, 0);
        int res = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this shipment?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (res == JOptionPane.YES_OPTION) {
            if (ShipmentDAO.delete(id)) {
                refreshTable();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete.");
            }
        }
    }
} 