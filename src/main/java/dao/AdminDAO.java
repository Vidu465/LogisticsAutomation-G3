package dao;

import util.DBConnection;
import java.sql.*;

public class AdminDAO {
    /**
     * Validates admin login credentials.
     */
    public static boolean validate(String username, String password) {
        String sql = "SELECT * FROM Admin WHERE Username = ? AND Password = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
} 