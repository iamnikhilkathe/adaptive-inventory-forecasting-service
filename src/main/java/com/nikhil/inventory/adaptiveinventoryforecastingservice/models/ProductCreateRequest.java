package com.nikhil.inventory.adaptiveinventoryforecastingservice.models;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;



public class ProductCreateRequest {
	
    @NotBlank
    private String name;

    @NotBlank
    private String category;

    @Min(0)
    private Integer currentStock;

    @Min(0)
    private Integer reorderThreshold;

    @Min(0)
    private Integer leadTimeDays;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public Integer getCurrentStock() {
		return currentStock;
	}

	public void setCurrentStock(Integer currentStock) {
		this.currentStock = currentStock;
	}

	public Integer getReorderThreshold() {
		return reorderThreshold;
	}

	public void setReorderThreshold(Integer reorderThreshold) {
		this.reorderThreshold = reorderThreshold;
	}

	public Integer getLeadTimeDays() {
		return leadTimeDays;
	}

	public void setLeadTimeDays(Integer leadTimeDays) {
		this.leadTimeDays = leadTimeDays;
	}
    
}
