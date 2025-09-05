package com.nikhil.inventory.adaptiveinventoryforecastingservice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nikhil.inventory.adaptiveinventoryforecastingservice.models.SaleRequestDto;
import com.nikhil.inventory.adaptiveinventoryforecastingservice.models.SaleResponseDto;
import com.nikhil.inventory.adaptiveinventoryforecastingservice.service.ProductService;
import com.nikhil.inventory.adaptiveinventoryforecastingservice.service.SaleService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/sales")
@RequiredArgsConstructor
public class SalesRecordForecastController {

	 private final SaleService saleService;

	    @PostMapping
	    public ResponseEntity<SaleResponseDto> recordSale(@Valid @RequestBody SaleRequestDto requestDto) {
	        SaleResponseDto responseDto = saleService.recordSale(requestDto);
	        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
	    }
	
	
	
}
