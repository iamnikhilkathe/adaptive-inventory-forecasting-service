package com.nikhil.inventory.adaptiveinventoryforecastingservice.service;

import java.time.LocalDate;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.nikhil.inventory.adaptiveinventoryforecastingservice.entity.Product;
import com.nikhil.inventory.adaptiveinventoryforecastingservice.entity.Sale;
import com.nikhil.inventory.adaptiveinventoryforecastingservice.models.SaleRequestDto;
import com.nikhil.inventory.adaptiveinventoryforecastingservice.models.SaleResponseDto;
import com.nikhil.inventory.adaptiveinventoryforecastingservice.repository.ProductRepository;
import com.nikhil.inventory.adaptiveinventoryforecastingservice.repository.SaleRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class SaleService {
	
	    private final ProductRepository productRepository;
	    private final SaleRepository saleRepository;
	    private final ForecastService forecastService;


	    public SaleResponseDto recordSale(SaleRequestDto requestDto) {
	    	
	        Product product = productRepository.findById(requestDto.getProductId())
	                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));

	        
	     // Update stock
	        product.setCurrentStock(product.getCurrentStock() - requestDto.getQuantity());
	        if (product.getCurrentStock() < 0) {
	            throw new ResponseStatusException(HttpStatus.CONFLICT, "Insufficient stock for product");
	        }
	        
	        
	        // Save Sale
	        Sale sale = Sale.builder()
	                .product(product)
	                .quantity(requestDto.getQuantity())
	                .date(requestDto.getDate() != null ? requestDto.getDate() : LocalDate.now())
	                .build();
	        saleRepository.save(sale);
	        productRepository.save(product);
	        
	        

	        // Recalculate forecast
	        forecastService.recalculateForecast(product);

	        return SaleResponseDto.builder()
	                .saleId(sale.getId())
	                .productId(product.getId())
	                .quantity(sale.getQuantity())
	                .date(sale.getDate())
	                .message("Sale recorded & forecast updated")
	                .build();
	    }

}
