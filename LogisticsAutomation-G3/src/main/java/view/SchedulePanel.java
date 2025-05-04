package com.logisticsautomationg3.scheduling.view;

import com.logisticsautomationg3.scheduling.controller.ScheduleController;
import com.logisticsautomationg3.scheduling.model.DeliverySlot;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.LocalDateTime;
import java.util.List;

public class SchedulePanel extends JPanel {
    private final ScheduleController controller;
    private JTable slotsTable;
    private JSpinner dateSpinner;
    
    public SchedulePanel(ScheduleController controller) {
        this.controller = controller;
        initializeUI();
    }
    
    private void initializeUI() {
        setLayout(new BorderLayout());
        
        // Date selection panel
        JPanel datePanel = new JPanel();
        dateSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(dateSpinner, "yyyy-MM-dd");
        dateSpinner.setEditor(dateEditor);
        JButton loadButton = new JButton("Load Slots");
        loadButton.addActionListener(this::loadSlots);
        datePanel.add(new JLabel("Select Date:"));
        datePanel.add(dateSpinner);
        datePanel.add(loadButton);
        
        // Slots table
        slotsTable = new JTable();
        String[] columns = {"Slot ID", "Start Time", "End Time", "Status"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        slotsTable.setModel(model);
        JScrollPane scrollPane = new JScrollPane(slotsTable);
        
        // Booking panel
        JPanel bookingPanel = new JPanel();
        JButton bookButton = new JButton("Book Selected Slot");
        bookButton.addActionListener(this::bookSlot);
        bookingPanel.add(bookButton);
        
        // Add components to main panel
        add(datePanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(bookingPanel, BorderLayout.SOUTH);
    }
    
    private void loadSlots(ActionEvent e) {
        LocalDateTime selectedDate = ((java.util.Date) dateSpinner.getValue()).toInstant()
                .atZone(java.time.ZoneId.systemDefault())
                .toLocalDateTime();
        
        List<DeliverySlot> slots = controller.getAvailableSlots(selectedDate);
        DefaultTableModel model = (DefaultTableModel) slotsTable.getModel();
        model.setRowCount(0); // Clear existing rows
        
        for (DeliverySlot slot : slots) {
            model.addRow(new Object[]{
                slot.getSlotId(),
                slot.getStartTime(),
                slot.getEndTime(),
                slot.isAvailable() ? "Available" : "Booked"
            });
        }
    }
    
    private void bookSlot(ActionEvent e) {
        int selectedRow = slotsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a slot to book", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int slotId = (int) slotsTable.getValueAt(selectedRow, 0);
        String shipmentId = JOptionPane.showInputDialog(this, "Enter Shipment ID:");
        
        if (shipmentId != null && !shipmentId.isEmpty()) {
            boolean success = controller.bookSlot(slotId, shipmentId);
            if (success) {
                JOptionPane.showMessageDialog(this, "Slot booked successfully!");
                loadSlots(null); // Refresh the table
            } else {
                JOptionPane.showMessageDialog(this, "Failed to book slot", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}