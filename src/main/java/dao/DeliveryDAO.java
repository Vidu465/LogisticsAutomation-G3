package dao;

import model.Delivery;
import util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DeliveryDAO {
    // Add new delivery
    public static boolean add(Delivery d) {
        String sql = "INSERT INTO Deliveries (ShipmentId, CustomerId, DeliverySlot) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, d.getShipmentId());
            ps.setInt(2, d.getCustomerId());
            if (d.getDeliverySlot() != null) {
                ps.setTimestamp(3, new Timestamp(d.getDeliverySlot().getTime()));
            } else {
                ps.setNull(3, Types.TIMESTAMP);
            }
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Update delivery
    public static boolean update(Delivery d) {
        String sql = "UPDATE Deliveries SET ShipmentId=?, CustomerId=?, DeliverySlot=? WHERE Id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, d.getShipmentId());
            ps.setInt(2, d.getCustomerId());
            if (d.getDeliverySlot() != null) {
                ps.setTimestamp(3, new Timestamp(d.getDeliverySlot().getTime()));
            } else {
                ps.setNull(3, Types.TIMESTAMP);
            }
            ps.setInt(4, d.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Delete delivery
    public static boolean delete(int id) {
        String sql = "DELETE FROM Deliveries WHERE Id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Get all deliveries
    public static List<Delivery> getAll() {
        List<Delivery> list = new ArrayList<>();
        String sql = "SELECT * FROM Deliveries";
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Delivery d = new Delivery(
                        rs.getInt("Id"),
                        rs.getInt("ShipmentId"),
                        rs.getInt("CustomerId"),
                        rs.getTimestamp("DeliverySlot")
                );
                list.add(d);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
} 