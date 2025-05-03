/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package scheduling.dao;

import com.logisticsautomationg3.common.utils.DBConnector;
import com.logisticsautomationg3.scheduling.model.DeliverySlot;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author LENOVO
 */
public class ScheduleDAOImpl implements scheduling.dao{
    private final Connection connection;
    private static final Logger LOGGER = Logger.getLogger(ScheduleDAOImpl.class.getName());

    public ScheduleDAOImpl() {
        this.connection = DBConnector.getConnection();
    }

    @Override
    public boolean bookSlot(DeliverySlot slot) {
        String sql = "UPDATE delivery_slots SET is_booked = TRUE, driver_id = ?, shipment_id = ? WHERE slot_id = ? AND is_booked = FALSE";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, slot.getAssignedDriverId());
            stmt.setInt(2, slot.getShipmentId());
            stmt.setInt(3, slot.getId());
            
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error booking delivery slot", ex);
            return false;
        }
    }

    @Override
    public List<DeliverySlot> getAvailableSlots(LocalDateTime date) {
        List<DeliverySlot> availableSlots = new ArrayList<>();
        String sql = "SELECT * FROM delivery_slots WHERE DATE(start_time) = ? AND is_booked = FALSE ORDER BY start_time";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setDate(1, java.sql.Date.valueOf(date.toLocalDate()));
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    DeliverySlot slot = new DeliverySlot();
                    slot.setId(rs.getInt("slot_id"));
                    slot.setStartTime(rs.getTimestamp("start_time").toLocalDateTime());
                    slot.setEndTime(rs.getTimestamp("end_time").toLocalDateTime());
                    slot.setBooked(rs.getBoolean("is_booked"));
                    slot.setAssignedDriverId(rs.getString("driver_id"));
                    slot.setShipmentId(rs.getInt("shipment_id"));
                    
                    availableSlots.add(slot);
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error retrieving available slots", ex);
        }
        
        return availableSlots;
    }

    @Override
    public boolean cancelSlot(int slotId) {
        String sql = "UPDATE delivery_slots SET is_booked = FALSE, driver_id = NULL, shipment_id = NULL WHERE slot_id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, slotId);
            
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error canceling delivery slot", ex);
            return false;
        }
    }

    @Override
    public boolean createSlot(DeliverySlot slot) {
        String sql = "INSERT INTO delivery_slots (start_time, end_time) VALUES (?, ?)";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            stmt.setTimestamp(1, java.sql.Timestamp.valueOf(slot.getStartTime()));
            stmt.setTimestamp(2, java.sql.Timestamp.valueOf(slot.getEndTime()));
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        slot.setId(generatedKeys.getInt(1));
                        return true;
                    }
                }
            }
            return false;
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error creating delivery slot", ex);
            return false;
        }
    }
}