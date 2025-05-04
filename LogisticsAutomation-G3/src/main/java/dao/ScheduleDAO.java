package com.logisticsautomationg3.scheduling.dao;

import com.logisticsautomationg3.scheduling.model.DeliverySlot;
import java.time.LocalDateTime;
import java.util.List;

public interface ScheduleDAO {
    boolean createSlot(DeliverySlot slot);
    boolean bookSlot(int slotId, String shipmentId);
    boolean cancelSlot(int slotId);
    List<DeliverySlot> getAvailableSlots(LocalDateTime date);
    List<DeliverySlot> getBookedSlotsForDriver(String driverId);
    List<DeliverySlot> getAllSlots();
}