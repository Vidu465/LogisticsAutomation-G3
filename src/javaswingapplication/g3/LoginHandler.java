/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package javaswingapplication.g3;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LoginHandler {
     public boolean authenticate(String username, String password) {
        String query = "SELECT * FROM users WHERE username = ? AND password = SHA2 (?, 256)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, username);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();

            return rs.next(); // If a record exists, login is successful

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
