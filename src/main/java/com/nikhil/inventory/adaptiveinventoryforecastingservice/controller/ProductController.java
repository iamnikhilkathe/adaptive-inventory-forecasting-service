package com.nikhil.inventory.adaptiveinventoryforecastingservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nikhil.inventory.adaptiveinventoryforecastingservice.models.ProductCreateRequest;
import com.nikhil.inventory.adaptiveinventoryforecastingservice.models.ProductResponse;
import com.nikhil.inventory.adaptiveinventoryforecastingservice.service.ProductService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

	private final ProductService productService;
	
  @PostMapping
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<ProductResponse> create(@RequestBody @Valid ProductCreateRequest req) {
	  ProductResponse respose = productService.createProduct(req);
    return ResponseEntity.ok(respose);
  }

  @GetMapping("/{id}")
  public ResponseEntity<ProductResponse> getProductById(@PathVariable("id") Long id) {
      ProductResponse response = productService.getProductInfo(id);
      
      if (response == null) {
         
          return ResponseEntity.notFound().build(); // return 404 if product not found
      }
      
      return ResponseEntity.ok(response);
  }
 

}