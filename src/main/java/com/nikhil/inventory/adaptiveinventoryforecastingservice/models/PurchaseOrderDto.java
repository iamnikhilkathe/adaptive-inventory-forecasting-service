package com.nikhil.inventory.adaptiveinventoryforecastingservice.models;

import java.time.LocalDate;

import com.nikhil.inventory.adaptiveinventoryforecastingservice.entity.PurchaseOrder;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PurchaseOrderDto {
    private Long id;
    private Long productId;
    private LocalDate orderDate;
    private Integer quantityOrdered;
    private LocalDate expectedArrivalDate;
    private LocalDate actualArrivalDate;
    private String status;

    public static PurchaseOrderDto fromEntity(PurchaseOrder po) {
        return PurchaseOrderDto.builder()
            .id(po.getId())
            .productId(po.getProduct().getId())
            .orderDate(po.getOrderDate())
            .quantityOrdered(po.getQuantityOrdered())
            .expectedArrivalDate(po.getExpectedArrivalDate())
            .actualArrivalDate(po.getActualArrivalDate())
            .status(po.getStatus().name())
            .build();
    }
}
