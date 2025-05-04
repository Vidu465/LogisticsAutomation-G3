package com.logisticsautomationg3.scheduling.service;

import com.logisticsautomationg3.scheduling.dao.ScheduleDAO;
import com.logisticsautomationg3.scheduling.model.DeliverySlot;
import java.time.LocalDateTime;
import java.util.List;

public class ScheduleService {
    private final ScheduleDAO scheduleDAO;
    
    public ScheduleService(ScheduleDAO scheduleDAO) {
        this.scheduleDAO = scheduleDAO;
    }
    
    public boolean createDeliverySlot(LocalDateTime startTime, LocalDateTime endTime) {
        if (startTime.isAfter(endTime)) {
            throw new IllegalArgumentException("Start time must be before end time");
        }
        DeliverySlot slot = new DeliverySlot(startTime, endTime);
        return scheduleDAO.createSlot(slot);
    }
    
    public boolean bookDeliverySlot(int slotId, String shipmentId) {
        return scheduleDAO.bookSlot(slotId, shipmentId);
    }
    
    public List<DeliverySlot> getAvailableSlots(LocalDateTime date) {
        return scheduleDAO.getAvailableSlots(date);
    }
    
    public List<DeliverySlot> getAllSlots() {
        return scheduleDAO.getAllSlots();
    }
}