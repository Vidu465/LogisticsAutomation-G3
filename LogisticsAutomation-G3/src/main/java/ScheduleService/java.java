/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ScheduleService;

/**
 *
 * @author LENOVO
 */
public class java {
    

package com.logisticsautomationg3.scheduling.service;

import com.logisticsautomationg3.scheduling.dao.ScheduleDAO;
import com.logisticsautomationg3.scheduling.dao.ScheduleDAOImpl;
import com.logisticsautomationg3.scheduling.model.DeliverySlot;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.logging.Logger;

/**
 * Service class that implements business logic for delivery scheduling.
 * Acts as an intermediary between the UI and data access layer.
 */
public class ScheduleService {
    
    private final ScheduleDAO scheduleDAO;
    private static final Logger LOGGER = Logger.getLogger(ScheduleService.class.getName());
    
    /**
     * Default constructor
     */
    public ScheduleService() {
        this.scheduleDAO = new ScheduleDAOImpl();
    }
    
    /**
     * Constructor with DAO dependency injection for testing
     * 
     * @param scheduleDAO The ScheduleDAO implementation to use
     */
    public ScheduleService(ScheduleDAO scheduleDAO) {
        this.scheduleDAO = scheduleDAO;
    }
    
    /**
     * Creates a new delivery slot
     * 
     * @param startTime The start time of the slot
     * @param endTime The end time of the slot
     * @param location The location for delivery
     * @return The created DeliverySlot if successful, null otherwise
     */
    public DeliverySlot createDeliverySlot(LocalDateTime startTime, LocalDateTime endTime, String location) {
        // Validate input parameters
        if (startTime == null || endTime == null || location == null || location.trim().isEmpty()) {
            LOGGER.warning("Invalid slot parameters provided");
            return null;
        }
        
        if (startTime.isAfter(endTime)) {
            LOGGER.warning("Start time cannot be after end time");
            return null;
        }
        
        // Create and persist the slot
        DeliverySlot slot = new DeliverySlot(startTime, endTime, location);
        
        if (scheduleDAO.createSlot(slot)) {
            return slot;
        }
        
        return null;
    }
    
    /**
     * Assigns a shipment to an available slot
     * 
     * @param slotId The ID of the slot to book
     * @param shipmentId The ID of the shipment
     * @param driverId The ID of the driver assigned to the delivery
     * @return boolean indicating success or failure
     */
    public boolean bookDeliverySlot(int slotId, int shipmentId, int driverId) {
        // Validate input parameters
        if (slotId <= 0 || shipmentId <= 0 || driverId <= 0) {
            LOGGER.warning("Invalid booking parameters provided");
            return false;
        }
        
        // Retrieve the slot to check if it's available
        DeliverySlot slot = scheduleDAO.getSlotById(slotId);
        
        if (slot == null) {
            LOGGER.warning("Slot with ID " + slotId + " not found");
            return false;
        }
        
        if (slot.isBooked()) {
            LOGGER.warning("Slot with ID " + slotId + " is already booked");
            return false;
        }
        
        // Update the slot with shipment and driver information
        slot.setBooked(true);
        slot.setShipmentId(shipmentId);
        slot.setDriverId(driverId);
        
        return scheduleDAO.bookSlot(slot);
    }
    
    /**
     * Cancels a booked delivery slot
     * 
     * @param slotId The ID of the slot to cancel
     * @return boolean indicating success or failure
     */
    public boolean cancelDeliverySlot(int slotId) {
        // Validate input parameter
        if (slotId <= 0) {
            LOGGER.warning("Invalid slot ID provided");
            return false;
        }
        
        // Check if the slot exists and is booked
        DeliverySlot slot = scheduleDAO.getSlotById(slotId);
        
        if (slot == null) {
            LOGGER.warning("Slot with ID " + slotId + " not found");
            return false;
        }
        
        if (!slot.isBooked()) {
            LOGGER.warning("Slot with ID " + slotId + " is not booked");
            return false;
        }
        
        return scheduleDAO.cancelSlot(slotId);
    }
    
    /**
     * Gets all available delivery slots for a specific date
     * 
     * @param date The date to check for available slots
     * @return List of available DeliverySlot objects
     */
    public List<DeliverySlot> getAvailableSlotsForDate(LocalDate date) {
        // Convert LocalDate to LocalDateTime at the start of the day
        LocalDateTime dateTime = LocalDateTime.of(date, LocalTime.MIDNIGHT);
        return scheduleDAO.getAvailableSlots(dateTime);
    }
    
    /**
     * Gets all delivery slots assigned to a specific driver
     * 
     * @param driverId The ID of the driver
     * @return List of DeliverySlot objects assigned to the driver
     */
    public List<DeliverySlot> getDriverSchedule(int driverId) {
        if (driverId <= 0) {
            LOGGER.warning("Invalid driver ID provided");
            return List.of(); // Return empty list
        }
        
        return scheduleDAO.getSlotsByDriver(driverId);
    }
    
    /**
     * Gets the delivery slot for a specific shipment
     * 
     * @param shipmentId The ID of the shipment
     * @return The DeliverySlot associated with the shipment
     */
    public DeliverySlot getShipmentSchedule(int shipmentId) {
        if (shipmentId <= 0) {
            LOGGER.warning("Invalid shipment ID provided");
            return null;
        }
        
        return scheduleDAO.getSlotByShipment(shipmentId);
    }
    
    /**
     * Updates an existing delivery slot
     * 
     * @param slot The DeliverySlot with updated information
     * @return boolean indicating success or failure
     */
    public boolean updateDeliverySlot(DeliverySlot slot) {
        if (slot == null || slot.getSlotId() <= 0) {
            LOGGER.warning("Invalid slot provided for update");
            return false;
        }
        
        // Validate time range
        if (slot.getStartTime().isAfter(slot.getEndTime())) {
            LOGGER.warning("Start time cannot be after end time");
            return false;
        }
        
        return scheduleDAO.updateSlot(slot);
    }
    
    /**
     * Deletes a delivery slot
     * 
     * @param slotId The ID of the slot to delete
     * @return boolean indicating success or failure
     */
    public boolean deleteDeliverySlot(int slotId) {
        if (slotId <= 0) {
            LOGGER.warning("Invalid slot ID provided for deletion");
            return false;
        }
        
        return scheduleDAO.deleteSlot(slotId);
    }
    
    /**
     * Generates a set of standard delivery slots for a specific date
     * 
     * @param date The date to generate slots for
     * @param location The delivery location
     * @return Number of slots created
     */
    public int generateStandardSlotsForDate(LocalDate date, String location) {
        if (date == null || location == null || location.trim().isEmpty()) {
            LOGGER.warning("Invalid parameters for slot generation");
            return 0;
        }
        
        // Define standard slot times (e.g., 2-hour slots from 8AM to 6PM)
        LocalTime[] startTimes = {
            LocalTime.of(8, 0),
            LocalTime.of(10, 0),
            LocalTime.of(12, 0),
            LocalTime.of(14, 0),
            LocalTime.of(16, 0)
        };
        
        int slotsCreated = 0;
        
        // Create each standard slot
        for (LocalTime startTime : startTimes) {
            LocalDateTime slotStart = LocalDateTime.of(date, startTime);
            LocalDateTime slotEnd = slotStart.plusHours(2);
            
            DeliverySlot slot = createDeliverySlot(slotStart, slotEnd, location);
            
            if (slot != null) {
                slotsCreated++;
            }
        }
        
        return slotsCreated;
    }
}
}