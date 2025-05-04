package com.logisticsautomationg3.scheduling.controller;

import com.logisticsautomationg3.scheduling.model.DeliverySlot;
import com.logisticsautomationg3.scheduling.service.ScheduleService;
import java.time.LocalDateTime;
import java.util.List;

public class ScheduleController {
    private final ScheduleService scheduleService;
    
    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }
    
    public boolean createSlot(LocalDateTime startTime, LocalDateTime endTime) {
        return scheduleService.createDeliverySlot(startTime, endTime);
    }
    
    public boolean bookSlot(int slotId, String shipmentId) {
        return scheduleService.bookDeliverySlot(slotId, shipmentId);
    }
    
    public List<DeliverySlot> getAvailableSlots(LocalDateTime date) {
        return scheduleService.getAvailableSlots(date);
    }
    
    public List<DeliverySlot> getAllSlots() {
        return scheduleService.getAllSlots();
    }
}