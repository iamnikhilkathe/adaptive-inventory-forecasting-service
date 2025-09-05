package com.nikhil.inventory.adaptiveinventoryforecastingservice.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "logs")
@Data
public class LogEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime timestamp;

    private Long userId;

    private String eventType;   // API_REQUEST, API_RESPONSE, BUSINESS, AUTH

    private String endpoint;

    @Column(columnDefinition = "TEXT")
    private String details;

    private String correlationId;
}
