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

public class TrackingPanel extends JPanel {
    private JTable table;
    private DefaultTableModel model;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    public TrackingPanel() {
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);

        // Header
        JPanel header = new JPanel();
        header.setBackground(PRIMARY);
        JLabel title = new JLabel("Shipment Tracking");
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
        JButton updateBtn = new JButton("Update");
        JButton refreshBtn = new JButton("Refresh");
        styleButton(updateBtn, ACCENT);
        styleButton(refreshBtn, INFO);
        btnPanel.add(updateBtn); btnPanel.add(refreshBtn);
        add(btnPanel, BorderLayout.SOUTH);

        refreshTable();

        updateBtn.addActionListener(e -> updateShipment());
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
                    s.getStatus(),
                    s.getLocation(),
                    s.getEstimatedDelivery() != null ? sdf.format(s.getEstimatedDelivery()) : ""
            });
        }
    }

    private void updateShipment() {
        int row = table.getSelectedRow();
        if (row == -1) { JOptionPane.showMessageDialog(this, "Select a shipment to update."); return; }
        int id = (int) model.getValueAt(row, 0);
        JTextField statusField = new JTextField((String) model.getValueAt(row, 3));
        JTextField locationField = new JTextField((String) model.getValueAt(row, 4));
        JTextField estDeliveryField = new JTextField((String) model.getValueAt(row, 5));
        Object[] fields = {"Status:", statusField, "Location:", locationField, "Est. Delivery (yyyy-MM-dd HH:mm):", estDeliveryField};
        int res = JOptionPane.showConfirmDialog(this, fields, "Update Shipment Progress", JOptionPane.OK_CANCEL_OPTION);
        if (res == JOptionPane.OK_OPTION) {
            String status = statusField.getText();
            String location = locationField.getText();
            Date estDelivery = null;
            try { if (!estDeliveryField.getText().trim().isEmpty()) estDelivery = sdf.parse(estDeliveryField.getText().trim()); } catch (ParseException ignored) {}
            // Get the rest of the shipment info from the table
            String sender = (String) model.getValueAt(row, 1);
            String receiver = (String) model.getValueAt(row, 2);
            Shipment s = new Shipment(id, sender, receiver, null, status, location, estDelivery, null);
            if (ShipmentDAO.update(s)) {
                refreshTable();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update shipment.");
            }
        }
    }
} 