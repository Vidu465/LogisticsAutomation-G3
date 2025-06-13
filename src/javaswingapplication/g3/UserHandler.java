/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package javaswingapplication.g3;

import java.sql.*;

public class UserHandler {
     public boolean createUser(String username, String password) {
        String sql = "INSERT INTO users (username, password) VALUES (?, SHA2(?, 256))";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, username);
            stmt.setString(2, password);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            if (e.getErrorCode() == 1062) { // Duplicate entry error
                JOptionPane.showMessageDialog(null, "Username already exists!");
            } else {
                e.printStackTrace();
            }
            return false;
        }
    }
}
