package com.logisticsautomationg3.scheduling.util;

import java.time.LocalDateTime;
import java.time.LocalTime;

public class CalendarUtils {
    public static boolean isTimeSlotAvailable(LocalDateTime start, LocalDateTime end, 
            List<LocalDateTime> bookedStarts, List<LocalDateTime> bookedEnds) {
        for (int i = 0; i < bookedStarts.size(); i++) {
            if (start.isBefore(bookedEnds.get(i)) && end.isAfter(bookedStarts.get(i))) {
                return false; // Overlapping slot found
            }
        }
        return true;
    }
    
    public static LocalDateTime roundToNearestHour(LocalDateTime datetime) {
        return datetime.withMinute(0).withSecond(0).withNano(0);
    }
}