package com.nikhil.inventory.adaptiveinventoryforecastingservice.models;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SaleResponseDto {

	private Long saleId;         // Optional in request, set in response
    private Long productId;      // Required in request
    private Integer quantity;    // Required in request
    private LocalDate date;      // Required in request
    private String message;      // Optional, set in response
	
}
