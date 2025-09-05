package com.nikhil.inventory.adaptiveinventoryforecastingservice.service;

import com.nikhil.inventory.adaptiveinventoryforecastingservice.entity.Forecast;
import com.nikhil.inventory.adaptiveinventoryforecastingservice.entity.Product;
import com.nikhil.inventory.adaptiveinventoryforecastingservice.entity.Sale;
import com.nikhil.inventory.adaptiveinventoryforecastingservice.logging.BizLogs;
import com.nikhil.inventory.adaptiveinventoryforecastingservice.models.ForecastResponse;
import com.nikhil.inventory.adaptiveinventoryforecastingservice.models.ProductForecastsResponse;
import com.nikhil.inventory.adaptiveinventoryforecastingservice.repository.ForecastRepository;
import com.nikhil.inventory.adaptiveinventoryforecastingservice.repository.ProductRepository;
import com.nikhil.inventory.adaptiveinventoryforecastingservice.repository.SaleRepository;

import lombok.AllArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ForecastService {

    private final ForecastRepository forecastRepository;
    private final SaleRepository saleRepository;
    protected final ProductRepository productRepository;
    // Configurable number of days for moving average
  //  @Value("forecast.movingAverageDays")
    private final int MOVING_AVERAGE_DAYS=7;


    @Transactional
    public void recalculateForecast(Product product) {
        LocalDate today = LocalDate.now();

        // 1. Get past N days of sales for this product
        LocalDate startDate = today.minusDays(MOVING_AVERAGE_DAYS);
        
        List<Sale> pastSales = saleRepository.findByProductAndDateBetween(
                product, startDate, today);

        // 2. Calculate average daily sales (moving average)
        int totalQuantity = pastSales.stream()
                .mapToInt(Sale::getQuantity)
                .sum();
        
        int averageDemand = pastSales.isEmpty() ? 0 : totalQuantity / MOVING_AVERAGE_DAYS;

        // 3. Determine projected stock starting from current stock
        int projectedStock = product.getCurrentStock();

        // 4. Update forecasts for next N days (or configurable horizon)
        for (int i = 1; i <= MOVING_AVERAGE_DAYS; i++) {
            LocalDate forecastDate = today.plusDays(i);

            // Check if a forecast already exists for this date
            Forecast forecast = forecastRepository
                    .findByProductAndDate(product, forecastDate)
                    .orElse(Forecast.builder()
                            .product(product)
                            .date(forecastDate)
                            .build());

            forecast.setExpectedDemand(averageDemand);
            projectedStock -= averageDemand;
            forecast.setProjectedStock(Math.max(projectedStock, 0)); // avoid negative stock
            
            forecastRepository.save(forecast);
           BizLogs.FORECAST.info("FORECAST_GENERATED productId={} forecastDate={} expectedDemand={} projectedStock", 
        		   forecast.getProduct().getId(), forecast.getDate(), forecast.getExpectedDemand(),forecast.getProjectedStock());
        }
    }


    public List<ForecastResponse> getForecastsInRange(LocalDate start, LocalDate end) {
        List<Forecast> forecasts = forecastRepository.findByDateBetween(start, end);

        return forecasts.stream()
                .map(f -> ForecastResponse.builder()
                        .date(f.getDate())
                        .expectedDemand(f.getExpectedDemand())
                        .projectedStock(f.getProjectedStock())
                        .build())
                .collect(Collectors.toList());
    }


	public List<ProductForecastsResponse> getProductForecastsInRange(LocalDate start, LocalDate end) {
		List<Forecast> allForecasts = forecastRepository.findByDateBetween(start, end);
		
		Map<Long, List<Forecast>> forecastsByProduct = allForecasts.stream()
		        .collect(Collectors.groupingBy(forecast -> forecast.getProduct().getId()));

		// Example: iterate
		List<ProductForecastsResponse> returnList=new ArrayList<ProductForecastsResponse>();
		
		forecastsByProduct.forEach((productId, forecasts) -> {
		Optional<Product> prod=	productRepository.findById(productId);
		
		Product p=prod.get();
		
		returnList.add(ProductForecastsResponse.builder().id(p.getId())
				.name(p.getName())
				.category(p.getCategory())
				.currentStock(p.getCurrentStock())
				.reorderThreshold(p.getReorderThreshold())
				.leadTimeDays(p.getLeadTimeDays())
				.latestForecastlist(forecasts.stream()
		                .map(f -> ForecastResponse.builder()
		                        .date(f.getDate())
		                        .expectedDemand(f.getExpectedDemand())
		                        .projectedStock(f.getProjectedStock())
		                        .build())
		                .collect(Collectors.toList()))
				.build());
		
		
		});
		return returnList;
	}
}
