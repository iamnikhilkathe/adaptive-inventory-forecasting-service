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
public class ForecastResponse {

	
	    private LocalDate date;           // Forecasted date
	    private Integer expectedDemand;   // Predicted sales for that date
	    private Integer projectedStock;   // Stock expected after demand
	
}
