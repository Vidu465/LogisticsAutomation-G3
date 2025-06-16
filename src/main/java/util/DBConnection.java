package util;
import java.sql.*;
import static ui.UIStyle.*;

/**
 * Utility class for MySQL DB connection.
 */
public class DBConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/logisticautomationg3_db?useSSL=false&allowPublicKeyRetrieval=true";
    private static final String USER = "root";  // Default XAMPP MySQL username
    private static final String PASSWORD = "";  // Default XAMPP MySQL password is empty

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL JDBC Driver not found.", e);
        }
    }
}