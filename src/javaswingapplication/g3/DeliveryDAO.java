// DeliveryDAO.java
package javaswingapplication.g3.dao;

import javaswingapplication.g3.models.Delivery;
import java.util.List;

public interface DeliveryDAO {
    void addDelivery(Delivery delivery);
    void updateDelivery(Delivery delivery);
    void deleteDelivery(int id);
    Delivery getDeliveryById(int id);
    List<Delivery> getAllDeliveries();
    List<Delivery> getDeliveriesByDriver(int driverId);
}