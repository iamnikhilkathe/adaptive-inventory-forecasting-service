package com.nikhil.inventory.adaptiveinventoryforecastingservice.models;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductForecastsResponse {
	   
	    private long id;
	   
	    private String name;

	    private String category;

	    private Integer currentStock;

	    private Integer reorderThreshold;

	    private Integer leadTimeDays;

	    
	    private List<ForecastResponse> latestForecastlist;
	
	
	
}
