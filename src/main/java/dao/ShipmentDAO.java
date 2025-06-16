package dao;

import model.Shipment;
import util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ShipmentDAO {
    // Add new shipment
    public static boolean add(Shipment s) {
        String sql = "INSERT INTO Shipments (Sender, Receiver, Contents, Status, Location, EstimatedDelivery, AssignedDriverId) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, s.getSender());
            ps.setString(2, s.getReceiver());
            ps.setString(3, s.getContents());
            ps.setString(4, s.getStatus());
            ps.setString(5, s.getLocation());
            if (s.getEstimatedDelivery() != null) {
                ps.setTimestamp(6, new Timestamp(s.getEstimatedDelivery().getTime()));
            } else {
                ps.setNull(6, Types.TIMESTAMP);
            }
            if (s.getAssignedDriverId() != null) {
                ps.setInt(7, s.getAssignedDriverId());
            } else {
                ps.setNull(7, Types.INTEGER);
            }
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Update shipment
    public static boolean update(Shipment s) {
        String sql = "UPDATE Shipments SET Sender=?, Receiver=?, Contents=?, Status=?, Location=?, EstimatedDelivery=?, AssignedDriverId=? WHERE Id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, s.getSender());
            ps.setString(2, s.getReceiver());
            ps.setString(3, s.getContents());
            ps.setString(4, s.getStatus());
            ps.setString(5, s.getLocation());
            if (s.getEstimatedDelivery() != null) {
                ps.setTimestamp(6, new Timestamp(s.getEstimatedDelivery().getTime()));
            } else {
                ps.setNull(6, Types.TIMESTAMP);
            }
            if (s.getAssignedDriverId() != null) {
                ps.setInt(7, s.getAssignedDriverId());
            } else {
                ps.setNull(7, Types.INTEGER);
            }
            ps.setInt(8, s.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Delete shipment
    public static boolean delete(int id) {
        String sql = "DELETE FROM Shipments WHERE Id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Get all shipments
    public static List<Shipment> getAll() {
        List<Shipment> list = new ArrayList<>();
        String sql = "SELECT * FROM Shipments";
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Shipment s = new Shipment(
                        rs.getInt("Id"),
                        rs.getString("Sender"),
                        rs.getString("Receiver"),
                        rs.getString("Contents"),
                        rs.getString("Status"),
                        rs.getString("Location"),
                        rs.getTimestamp("EstimatedDelivery"),
                        rs.getObject("AssignedDriverId") != null ? rs.getInt("AssignedDriverId") : null
                );
                list.add(s);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
} 