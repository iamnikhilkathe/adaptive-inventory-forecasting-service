package com.nikhil.inventory.adaptiveinventoryforecastingservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nikhil.inventory.adaptiveinventoryforecastingservice.entity.LogEntry;

@Repository
public interface LogRepository extends JpaRepository<LogEntry, Long> {
}
