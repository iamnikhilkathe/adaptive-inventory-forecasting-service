package com.nikhil.inventory.adaptiveinventoryforecastingservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "products", indexes = {
        @Index(name = "ix_products_category", columnList = "category")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false, length = 150)
    private String name;

    @NotBlank
    @Column(nullable = false, length = 100)
    private String category;

    @Min(0)
    @Column(nullable = false)
    private Integer currentStock;

    @Min(0)
    @Column(nullable = false)
    private Integer reorderThreshold;

    @Min(0)
    @Column(nullable = false)
    private Integer leadTimeDays;

}