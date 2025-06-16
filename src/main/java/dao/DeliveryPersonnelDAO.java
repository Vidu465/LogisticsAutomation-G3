package dao;

import model.DeliveryPersonnel;
import util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DeliveryPersonnelDAO {
    // Add new delivery personnel
    public static boolean add(DeliveryPersonnel dp) {
        String sql = "INSERT INTO DeliveryPersonnel (Name, Contact, Schedule, AssignedRoutes) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, dp.getName());
            ps.setString(2, dp.getContact());
            ps.setString(3, dp.getSchedule());
            ps.setString(4, dp.getAssignedRoutes());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Update delivery personnel
    public static boolean update(DeliveryPersonnel dp) {
        String sql = "UPDATE DeliveryPersonnel SET Name=?, Contact=?, Schedule=?, AssignedRoutes=? WHERE Id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, dp.getName());
            ps.setString(2, dp.getContact());
            ps.setString(3, dp.getSchedule());
            ps.setString(4, dp.getAssignedRoutes());
            ps.setInt(5, dp.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Delete delivery personnel
    public static boolean delete(int id) {
        String sql = "DELETE FROM DeliveryPersonnel WHERE Id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Get all delivery personnel
    public static List<DeliveryPersonnel> getAll() {
        List<DeliveryPersonnel> list = new ArrayList<>();
        String sql = "SELECT * FROM DeliveryPersonnel";
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                DeliveryPersonnel dp = new DeliveryPersonnel(
                        rs.getInt("Id"),
                        rs.getString("Name"),
                        rs.getString("Contact"),
                        rs.getString("Schedule"),
                        rs.getString("AssignedRoutes")
                );
                list.add(dp);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
} 