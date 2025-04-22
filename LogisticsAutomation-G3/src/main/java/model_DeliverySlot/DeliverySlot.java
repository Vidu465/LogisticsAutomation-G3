package model_DeliverySlot;  //  package name

import java.time.LocalDateTime;  // Added import

/**
 * Represents a delivery time slot in the scheduling system.
 */
public class DeliverySlot {
    private String slotId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String orderId;      // Links to Shipment class
    private String driverId;     // Links to Driver class
    private boolean isBooked;

    // Constructor
    public DeliverySlot(String slotId, LocalDateTime startTime, LocalDateTime endTime) {
        this.slotId = slotId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.isBooked = false;  // Default to available
    }

    // Full constructor (for booked slots)
    public DeliverySlot(String slotId, LocalDateTime startTime, LocalDateTime endTime, 
                       String orderId, String driverId, boolean isBooked) {
        this.slotId = slotId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.orderId = orderId;
        this.driverId = driverId;
        this.isBooked = isBooked;
    }

    // ----- Getters ----- //
    public String getSlotId() {
        return slotId;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getDriverId() {
        return driverId;
    }

    public boolean isBooked() {
        return isBooked;
    }

    // ----- Setters ----- //
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public void setBooked(boolean booked) {
        isBooked = booked;
    }

    // ----- Utility Methods ----- //
    public boolean isAvailable() {
        return !isBooked;
    }

    public long getDurationMinutes() {
        return java.time.Duration.between(startTime, endTime).toMinutes();
    }

    @Override
    public String toString() {
        return String.format(
            "DeliverySlot[ID: %s, %s to %s, Booked: %s, Order: %s, Driver: %s]",
            slotId, startTime, endTime, isBooked, orderId, driverId
        );
    }
}