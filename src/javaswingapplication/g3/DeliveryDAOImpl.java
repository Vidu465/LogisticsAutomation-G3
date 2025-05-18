// DeliveryDAOImpl.java
package javaswingapplication.g3.dao;

import javaswingapplication.g3.db.DatabaseConnection;
import javaswingapplication.g3.models.Delivery;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DeliveryDAOImpl implements DeliveryDAO {

    @Override
    public void addDelivery(Delivery delivery) {
        String sql = "INSERT INTO delivery_schedule (customer_name, delivery_date, shipment_id) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, delivery.getCustomerName());

            if (delivery.getDeliveryDate() != null) {
                stmt.setDate(2, Date.valueOf(delivery.getDeliveryDate().toLocalDate()));
            } else {
                stmt.setNull(2, Types.DATE);
            }

            stmt.setInt(3, delivery.getShipmentId());
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Delivery> getAllDeliveries() {
        List<Delivery> deliveries = new ArrayList<>();
        String sql = "SELECT * FROM delivery_schedule";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Delivery delivery = new Delivery();
                delivery.setId(rs.getInt("id"));
                delivery.setCustomerName(rs.getString("customer_name"));
                delivery.setDeliveryDate(rs.getDate("delivery_date").toLocalDate().atStartOfDay());
                delivery.setShipmentId(rs.getInt("shipment_id"));
                deliveries.add(delivery);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return deliveries;
    }

    @Override
    public void updateDelivery(Delivery delivery) {
        String sql = "UPDATE delivery_schedule SET customer_name = ?, delivery_date = ?, shipment_id = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, delivery.getCustomerName());

            if (delivery.getDeliveryDate() != null) {
                stmt.setDate(2, Date.valueOf(delivery.getDeliveryDate().toLocalDate()));
            } else {
                stmt.setNull(2, Types.DATE);
            }

            stmt.setInt(3, delivery.getShipmentId());
            stmt.setInt(4, delivery.getId());
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteDelivery(int id) {
        String sql = "DELETE FROM delivery_schedule WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
