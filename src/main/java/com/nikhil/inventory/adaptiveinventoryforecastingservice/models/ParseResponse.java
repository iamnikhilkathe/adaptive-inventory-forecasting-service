package com.nikhil.inventory.adaptiveinventoryforecastingservice.models;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class ParseResponse {
    @JsonProperty("product_name")
    private String productName;

    @JsonProperty("category")
    private String category;

    @JsonProperty("quantity")
    private Integer quantity;

    @JsonProperty("sale_date")
    @JsonDeserialize(using = DateDeserializer.class)
    private LocalDate saleDate;

    @JsonProperty("location")
    private String location;

  
}
