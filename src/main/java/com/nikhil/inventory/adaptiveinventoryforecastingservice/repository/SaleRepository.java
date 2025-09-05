package com.nikhil.inventory.adaptiveinventoryforecastingservice.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nikhil.inventory.adaptiveinventoryforecastingservice.entity.Product;
import com.nikhil.inventory.adaptiveinventoryforecastingservice.entity.Sale;

@Repository
public interface SaleRepository  extends  JpaRepository<Sale, Long> {

	List<Sale> findByProductAndDateBetween(Product product, LocalDate startDate, LocalDate today);

}
