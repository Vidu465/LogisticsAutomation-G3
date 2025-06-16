package model;

import java.util.Date;

public class Delivery {
    private int id;
    private int shipmentId;
    private int customerId;
    private Date deliverySlot;

    public Delivery() {}
    public Delivery(int id, int shipmentId, int customerId, Date deliverySlot) {
        this.id = id;
        this.shipmentId = shipmentId;
        this.customerId = customerId;
        this.deliverySlot = deliverySlot;
    }
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getShipmentId() { return shipmentId; }
    public void setShipmentId(int shipmentId) { this.shipmentId = shipmentId; }
    public int getCustomerId() { return customerId; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }
    public Date getDeliverySlot() { return deliverySlot; }
    public void setDeliverySlot(Date deliverySlot) { this.deliverySlot = deliverySlot; }
    @Override
    public String toString() {
        return "Delivery{" +
                "id=" + id +
                ", shipmentId=" + shipmentId +
                ", customerId=" + customerId +
                ", deliverySlot=" + deliverySlot +
                '}';
    }
} 