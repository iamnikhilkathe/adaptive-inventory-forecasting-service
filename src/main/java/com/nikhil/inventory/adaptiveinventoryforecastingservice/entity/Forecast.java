package com.nikhil.inventory.adaptiveinventoryforecastingservice.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "forecasts", indexes = {
        @Index(name = "ix_forecasts_product_date", columnList = "product_id, forecastDate")
})
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class Forecast {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @NotNull
    @Column(name = "forecastDate", nullable = false)
    private LocalDate date;

    @Min(0)
    @Column(nullable = false)
    private Integer expectedDemand;

    @Min(0)
    @Column(nullable = false)
    private Integer projectedStock;

    
}
