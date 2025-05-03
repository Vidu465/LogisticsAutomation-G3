package com.logisticsautomationg3.scheduling.dao;

import com.logisticsautomationg3.scheduling.model.DeliverySlot;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Implementation of the ScheduleDAO interface.
 * Handles database operations for delivery slot scheduling.
 */
public class ScheduleDAOImpl implements ScheduleDAO {
    
    private Connection connection;
    private static final Logger LOGGER = Logger.getLogger(ScheduleDAOImpl.class.getName());
    
    /**
     * Constructor that initializes database connection
     */
    public ScheduleDAOImpl() {
        try {
            // Assuming DBConnector implementation from another module
            this.connection = com.logisticsautomationg3.common.utils.DBConnector.getConnection();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Failed to establish database connection", e);
        }
    }
    
    /**
     * Constructor with connection parameter for testing purposes
     * 
     * @param connection Database connection object
     */
    public ScheduleDAOImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public boolean createSlot(DeliverySlot slot) {
        String sql = "INSERT INTO delivery_slots (start_time, end_time, is_booked, location) VALUES (?, ?, ?, ?)";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setTimestamp(1, Timestamp.valueOf(slot.getStartTime()));
            stmt.setTimestamp(2, Timestamp.valueOf(slot.getEndTime()));
            stmt.setBoolean(3, slot.isBooked());
            stmt.setString(4, slot.getLocation());
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        slot.setSlotId(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error creating delivery slot", e);
        }
        
        return false;
    }

    @Override
    public boolean bookSlot(DeliverySlot slot) {
        String sql = "UPDATE delivery_slots SET is_booked = true, driver_id = ?, shipment_id = ? WHERE slot_id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, slot.getDriverId());
            stmt.setInt(2, slot.getShipmentId());
            stmt.setInt(3, slot.getSlotId());
            
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error booking slot", e);
        }
        
        return false;
    }

    @Override
    public boolean cancelSlot(int slotId) {
        String sql = "UPDATE delivery_slots SET is_booked = false, driver_id = NULL, shipment_id = NULL WHERE slot_id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, slotId);
            
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error canceling slot", e);
        }
        
        return false;
    }

    @Override
    public List<DeliverySlot> getAvailableSlots(LocalDateTime date) {
        List<DeliverySlot> availableSlots = new ArrayList<>();
        
        // Find slots for the same day that aren't booked
        String sql = "SELECT * FROM delivery_slots WHERE DATE(start_time) = DATE(?) AND is_booked = false ORDER BY start_time";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setTimestamp(1, Timestamp.valueOf(date));
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    DeliverySlot slot = mapResultSetToDeliverySlot(rs);
                    availableSlots.add(slot);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error retrieving available slots", e);
        }
        
        return availableSlots;
    }

    @Override
    public DeliverySlot getSlotById(int slotId) {
        String sql = "SELECT * FROM delivery_slots WHERE slot_id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, slotId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToDeliverySlot(rs);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error retrieving slot by ID", e);
        }
        
        return null;
    }

    @Override
    public boolean updateSlot(DeliverySlot slot) {
        String sql = "UPDATE delivery_slots SET start_time = ?, end_time = ?, is_booked = ?, " +
                    "driver_id = ?, shipment_id = ?, location = ? WHERE slot_id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setTimestamp(1, Timestamp.valueOf(slot.getStartTime()));
            stmt.setTimestamp(2, Timestamp.valueOf(slot.getEndTime()));
            stmt.setBoolean(3, slot.isBooked());
            
            if (slot.getDriverId() > 0) {
                stmt.setInt(4, slot.getDriverId());
            } else {
                stmt.setNull(4, java.sql.Types.INTEGER);
            }
            
            if (slot.getShipmentId() > 0) {
                stmt.setInt(5, slot.getShipmentId());
            } else {
                stmt.setNull(5, java.sql.Types.INTEGER);
            }
            
            stmt.setString(6, slot.getLocation());
            stmt.setInt(7, slot.getSlotId());
            
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating slot", e);
        }
        
        return false;
    }

    @Override
    public boolean deleteSlot(int slotId) {
        String sql = "DELETE FROM delivery_slots WHERE slot_id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, slotId);
            
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error deleting slot", e);
        }
        
        return false;
    }

    @Override
    public List<DeliverySlot> getSlotsByDriver(int driverId) {
        List<DeliverySlot> driverSlots = new ArrayList<>();
        
        String sql = "SELECT * FROM delivery_slots WHERE driver_id = ? ORDER BY start_time";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, driverId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    DeliverySlot slot = mapResultSetToDeliverySlot(rs);
                    driverSlots.add(slot);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error retrieving slots by driver", e);
        }
        
        return driverSlots;
    }

    @Override
    public DeliverySlot getSlotByShipment(int shipmentId) {
        String sql = "SELECT * FROM delivery_slots WHERE shipment_id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, shipmentId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToDeliverySlot(rs);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error retrieving slot by shipment", e);
        }
        
        return null;
    }
    
    /**
     * Helper method to map ResultSet to DeliverySlot object
     * 
     * @param rs ResultSet containing slot data
     * @return Mapped DeliverySlot object
     * @throws SQLException if there's an error accessing the ResultSet
     */
    private DeliverySlot mapResultSetToDeliverySlot(ResultSet rs) throws SQLException {
        DeliverySlot slot = new DeliverySlot();
        
        slot.setSlotId(rs.getInt("slot_id"));
        slot.setStartTime(rs.getTimestamp("start_time").toLocalDateTime());
        slot.setEndTime(rs.getTimestamp("end_time").toLocalDateTime());
        slot.setBooked(rs.getBoolean("is_booked"));
        
        // Handle nullable columns
        int driverId = rs.getInt("driver_id");
        if (!rs.wasNull()) {
            slot.setDriverId(driverId);
        }
        
        int shipmentId = rs.getInt("shipment_id");
        if (!rs.wasNull()) {
            slot.setShipmentId(shipmentId);
        }
        
        slot.setLocation(rs.getString("location"));
        
        return slot;
    }
}