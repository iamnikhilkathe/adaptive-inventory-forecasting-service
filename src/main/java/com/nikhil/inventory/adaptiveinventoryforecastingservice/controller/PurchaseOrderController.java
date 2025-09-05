package com.nikhil.inventory.adaptiveinventoryforecastingservice.controller;

import com.nikhil.inventory.adaptiveinventoryforecastingservice.entity.PurchaseOrder;
import com.nikhil.inventory.adaptiveinventoryforecastingservice.models.PurchaseOrderDto;
import com.nikhil.inventory.adaptiveinventoryforecastingservice.service.PurchaseOrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/orders")
public class PurchaseOrderController {

    private final PurchaseOrderService purchaseOrderService;

    public PurchaseOrderController(PurchaseOrderService purchaseOrderService) {
        this.purchaseOrderService = purchaseOrderService;
    }

    @PostMapping("/auto")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<PurchaseOrderDto>> generateAutoOrders() {
        List<PurchaseOrder> created = purchaseOrderService.generateAutoPurchaseOrders();
        List<PurchaseOrderDto> dtos = created.stream().map(PurchaseOrderDto::fromEntity).collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }


    @PutMapping("/{id}/receive")
    @PreAuthorize("hasRole('ADMIN')") 
    public ResponseEntity<PurchaseOrderDto> receiveOrder(@PathVariable("id") Long id) {
        PurchaseOrder po = purchaseOrderService.receivePurchaseOrder(id);
        return ResponseEntity.ok(PurchaseOrderDto.fromEntity(po));
    }
}
