package com.logisticsautomationg3.scheduling.model;  

import java.time.LocalDateTime;

/**
 * Represents a delivery time slot in the scheduling system.
 * 
 * <p>Contains information about slot timing, booking status, 
 * and associated order/driver references.</p>
 */
public class DeliverySlot {
    // Required fields
    private String slotId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String orderId;      // Links to Shipment model
    private String driverId;     // Links to Driver model
    private boolean isBooked;

    // Constructors
    public DeliverySlot() {
        // Default constructor for JPA/Serialization
    }

    /**
     * Creates an available delivery slot.
     */
    public DeliverySlot(String slotId, LocalDateTime startTime, LocalDateTime endTime) {
        this.slotId = slotId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.isBooked = false;
    }

    /**
     * Creates a fully-defined delivery slot (for database loading).
     */
    public DeliverySlot(String slotId, LocalDateTime startTime, LocalDateTime endTime,
                      String orderId, String driverId, boolean isBooked) {
        this(slotId, startTime, endTime);
        this.orderId = orderId;
        this.driverId = driverId;
        this.isBooked = isBooked;
    }

    // Getters
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

    // Setters
    public void setSlotId(String slotId) {
        this.slotId = slotId;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public void setBooked(boolean booked) {
        isBooked = booked;
    }

    // Business Logic Methods
    /**
     * Checks if this slot overlaps with another slot.
     */
    public boolean overlapsWith(DeliverySlot other) {
        return this.startTime.isBefore(other.endTime) && 
               this.endTime.isAfter(other.startTime);
    }

    /**
     * Calculates slot duration in minutes.
     */
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