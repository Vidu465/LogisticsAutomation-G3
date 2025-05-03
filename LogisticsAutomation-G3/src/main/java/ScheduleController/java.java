/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ScheduleController;

/**
 *
 * @author LENOVO
 */
public class java {
    package com.logisticsautomationg3.scheduling.controller;

import com.logisticsautomationg3.scheduling.model.DeliverySlot;
import com.logisticsautomationg3.scheduling.service.ScheduleService;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 * Controller class for the scheduling functionality.
 * Acts as intermediary between the view and service layer.
 */
public class ScheduleController {
    
    private final ScheduleService scheduleService;
    private static final Logger LOGGER = Logger.getLogger(ScheduleController.class.getName());
    
    /**
     * Default constructor
     */
    public ScheduleController() {
        this.scheduleService = new ScheduleService();
    }
    
    /**
     * Constructor with service injection
}
