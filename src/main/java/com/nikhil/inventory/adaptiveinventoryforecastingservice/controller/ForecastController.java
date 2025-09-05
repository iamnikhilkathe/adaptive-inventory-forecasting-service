package com.nikhil.inventory.adaptiveinventoryforecastingservice.controller;

import com.nikhil.inventory.adaptiveinventoryforecastingservice.models.ForecastResponse;
import com.nikhil.inventory.adaptiveinventoryforecastingservice.models.ProductForecastsResponse;
import com.nikhil.inventory.adaptiveinventoryforecastingservice.service.ForecastService;

import lombok.AllArgsConstructor;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/forecasts")
@AllArgsConstructor
public class ForecastController {

    private final ForecastService forecastService;

   

//    @GetMapping
//    public ResponseEntity<List<ForecastResponse>> getForecasts(
//            @RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
//            @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
//
//        List<ForecastResponse> forecasts = forecastService.getForecastsInRange(start, end);
//        return ResponseEntity.ok(forecasts);
//    }
    
    @GetMapping
    public ResponseEntity<List<ProductForecastsResponse>> getForecasts(
            @RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {

        List<ProductForecastsResponse> forecasts = forecastService.getProductForecastsInRange(start, end);
        return ResponseEntity.ok(forecasts);
    }
    
    
    
}
