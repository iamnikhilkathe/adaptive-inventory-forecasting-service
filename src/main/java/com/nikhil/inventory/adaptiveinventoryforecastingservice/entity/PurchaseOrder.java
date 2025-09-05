package com.nikhil.inventory.adaptiveinventoryforecastingservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "purchase_orders", indexes = {
    @Index(name = "ix_po_product", columnList = "product_id"),
    @Index(name = "ix_po_status", columnList = "status")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PurchaseOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false)
    private LocalDate orderDate;

    @Min(1)
    @Column(nullable = false)
    private Integer quantityOrdered;

    @Column(nullable = false)
    private LocalDate expectedArrivalDate;

    private LocalDate actualArrivalDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PurchaseOrderStatus status;
}
