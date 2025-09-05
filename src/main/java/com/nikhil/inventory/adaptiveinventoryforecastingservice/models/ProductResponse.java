package com.nikhil.inventory.adaptiveinventoryforecastingservice.models;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductResponse {
	   private long id;
	   
	   private String name;

	    private String category;

	    private Integer currentStock;

	    private Integer reorderThreshold;

	    private Integer leadTimeDays;

	    
	    private ForecastResponse latestForecast;
	    
	    
}
