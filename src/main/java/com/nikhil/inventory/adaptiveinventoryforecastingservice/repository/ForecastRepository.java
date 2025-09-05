package com.nikhil.inventory.adaptiveinventoryforecastingservice.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.nikhil.inventory.adaptiveinventoryforecastingservice.entity.Forecast;
import com.nikhil.inventory.adaptiveinventoryforecastingservice.entity.Product;


@Repository
public interface ForecastRepository extends JpaRepository<Forecast, Long> {

//	@Query("SELECT f FROM Forecast f WHERE f.product.productId = :productId ORDER BY f.date DESC")
//	List<Forecast> findLatessts(@Param("productId") Long productId, Pageable pageable);

	  Optional<Forecast> findTopByProductIdOrderByDateDesc(Long productId);

	  Optional<Forecast> findByProductAndDate(Product product, LocalDate date);

	  List<Forecast> findByDateBetween(LocalDate start, LocalDate end);
	
	
}
