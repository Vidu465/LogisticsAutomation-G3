package com.logisticsautomationg3.scheduling.dao;

import com.logisticsautomationg3.scheduling.model.DeliverySlot;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ScheduleDAOImpl implements ScheduleDAO {
    private static final Logger LOGGER = Logger.getLogger(ScheduleDAOImpl.class.getName());
    private Connection connection;
    
    public ScheduleDAOImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public boolean createSlot(DeliverySlot slot) {
        String sql = "INSERT INTO delivery_slots (start_time, end_time, is_available) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setTimestamp(1, Timestamp.valueOf(slot.getStartTime()));
            stmt.setTimestamp(2, Timestamp.valueOf(slot.getEndTime()));
            stmt.setBoolean(3, slot.isAvailable());
            return stmt.executeUpdate() > 0;
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error creating slot", ex);
            return false;
        }
    }

    @Override
    public boolean bookSlot(int slotId, String shipmentId) {
        String sql = "UPDATE delivery_slots SET is_available = false, shipment_id = ? WHERE slot_id = ? AND is_available = true";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, shipmentId);
            stmt.setInt(2, slotId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error booking slot", ex);
            return false;
        }
    }

    @Override
    public List<DeliverySlot> getAvailableSlots(LocalDateTime date) {
        List<DeliverySlot> slots = new ArrayList<>();
        String sql = "SELECT * FROM delivery_slots WHERE DATE(start_time) = ? AND is_available = true";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setDate(1, Date.valueOf(date.toLocalDate()));
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                DeliverySlot slot = new DeliverySlot();
                slot.setSlotId(rs.getInt("slot_id"));
                slot.setStartTime(rs.getTimestamp("start_time").toLocalDateTime());
                slot.setEndTime(rs.getTimestamp("end_time").toLocalDateTime());
                slot.setAvailable(rs.getBoolean("is_available"));
                slots.add(slot);
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error fetching available slots", ex);
        }
        return slots;
    }

    // Implement other methods similarly
}