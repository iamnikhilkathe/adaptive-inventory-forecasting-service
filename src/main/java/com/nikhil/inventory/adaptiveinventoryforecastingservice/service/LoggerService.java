package com.nikhil.inventory.adaptiveinventoryforecastingservice.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nikhil.inventory.adaptiveinventoryforecastingservice.entity.LogEntry;
import com.nikhil.inventory.adaptiveinventoryforecastingservice.repository.LogRepository;

@Service
public class LoggerService {

    @Autowired
    private LogRepository logRepository;

    public void logBusinessEvent(Long userId, String eventType, String details, String correlationId) {
        LogEntry log = new LogEntry();
        log.setTimestamp(LocalDateTime.now());
        log.setUserId(userId);
        log.setEventType(eventType);  // e.g. "BUSINESS"
        log.setDetails(details);
        log.setCorrelationId(correlationId);
        logRepository.save(log);
    }
}
