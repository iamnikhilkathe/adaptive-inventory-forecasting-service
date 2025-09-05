package com.nikhil.inventory.adaptiveinventoryforecastingservice.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.nikhil.inventory.adaptiveinventoryforecastingservice.entity.Forecast;
import com.nikhil.inventory.adaptiveinventoryforecastingservice.entity.Product;
import com.nikhil.inventory.adaptiveinventoryforecastingservice.models.ForecastResponse;
import com.nikhil.inventory.adaptiveinventoryforecastingservice.models.ProductCreateRequest;
import com.nikhil.inventory.adaptiveinventoryforecastingservice.models.ProductResponse;
import com.nikhil.inventory.adaptiveinventoryforecastingservice.repository.ForecastRepository;
import com.nikhil.inventory.adaptiveinventoryforecastingservice.repository.ProductRepository;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ProductService {

	private final ProductRepository productRepository;

	private final ForecastRepository forecastRepository;
	
	public ProductResponse createProduct(@Valid ProductCreateRequest req) {
		
		
	Product prod=	productRepository.save(
				Product.builder()
				.name(req.getName()).category(req.getCategory()).currentStock(req.getCurrentStock())
				.reorderThreshold(req.getReorderThreshold())
				.leadTimeDays(req.getLeadTimeDays())
				 .build() 
				 );
		    
		return ProductResponse.builder().id(prod.getId())
				.name(prod.getName()).category(prod.getCategory()).currentStock(prod.getCurrentStock())
				.reorderThreshold(prod.getReorderThreshold())
				.leadTimeDays(prod.getLeadTimeDays())
				.build();  
	}


	public ProductResponse getProductInfo(long id) {
		 // Fetch product
	    Product prod = productRepository.findById(id)
	            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));

	    // Fetch latest forecast for the product
	    Forecast latestForecast = forecastRepository
	            .findTopByProductIdOrderByDateDesc(id)
	            .orElse(null); // can be null if no forecast exists

	    // Build ForecastDto if forecast exists
	    ForecastResponse forecastDto = null;
	    if (latestForecast != null) {
	        forecastDto = ForecastResponse.builder()
	                .date(latestForecast.getDate())
	                .expectedDemand(latestForecast.getExpectedDemand())
	                .projectedStock(latestForecast.getProjectedStock())
	                .build();
	    }

	    // Build and return ProductResponse
	    return ProductResponse.builder()
	            .id(prod.getId())
	            .name(prod.getName())
	            .category(prod.getCategory())
	            .currentStock(prod.getCurrentStock())
	            .reorderThreshold(prod.getReorderThreshold())
	            .leadTimeDays(prod.getLeadTimeDays())
	            .latestForecast(forecastDto)
	            .build();
	}

}
