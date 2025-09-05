package com.nikhil.inventory.adaptiveinventoryforecastingservice.service;

import com.nikhil.inventory.adaptiveinventoryforecastingservice.entity.*;
import com.nikhil.inventory.adaptiveinventoryforecastingservice.exceptions.ResourceNotFoundException;
import com.nikhil.inventory.adaptiveinventoryforecastingservice.repository.*;
import com.nikhil.inventory.adaptiveinventoryforecastingservice.logging.BizLogs;
import lombok.AllArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class PurchaseOrderService {

    private final ProductRepository productRepository;
    private final SaleRepository saleRepository;
    private final PurchaseOrderRepository purchaseOrderRepository;

    // config – ideally inject from properties
    private final int movingAverageDays = 7;
    private final int safetyStockDays = 2;
    private final int minOrderQuantity = 1;

    
    /**
     * Auto-generate POs for products that require restocking.
     * Returns list of created POs.
     */
    @Transactional
    public List<PurchaseOrder> generateAutoPurchaseOrders() {
    	
        LocalDate today = LocalDate.now();
        
        List<Product> candidates = productRepository.findProductsBelowReorderThreshold();
     
        List<PurchaseOrder> created = new ArrayList<>();

        for (Product p : candidates) {
            int avgDailyDemand = calculateAverageDailyDemand(p, movingAverageDays);

            // If no demand and you want to skip, continue; else still create minimal order
            if (avgDailyDemand == 0) {
                // optional: skip completely, or place min order
                // continue;
            }

            int targetStock = avgDailyDemand * (p.getLeadTimeDays() + safetyStockDays);
            int qtyToOrder = Math.max(0, targetStock - p.getCurrentStock());

            if (qtyToOrder < minOrderQuantity) {
                qtyToOrder = 0; // skip or set to minOrderQuantity depending on business rule
            }

            if (qtyToOrder > 0) {
                PurchaseOrder po = PurchaseOrder.builder()
                        .product(p)
                        .orderDate(today)
                        .quantityOrdered(qtyToOrder)
                        .expectedArrivalDate(today.plusDays(p.getLeadTimeDays()))
                        .status(PurchaseOrderStatus.PENDING)
                        .build();
                purchaseOrderRepository.save(po);
                created.add(po);
                BizLogs.ORDER.info("ORDER_CREATED id={} productId={} quantityOrdered={} orderDate={} expectedArrival={} status={}",
                		po.getId(),
                		po.getProduct().getId(),
                		po.getQuantityOrdered(),
                		po.getOrderDate(),
                		po.getExpectedArrivalDate(),
                		po.getStatus());

            }
            
        }
        
        return created;
    }

    private int calculateAverageDailyDemand(Product product, int days) {
        LocalDate end = LocalDate.now();
        LocalDate start = end.minusDays(days);
        
        List<Sale> sales = saleRepository.findByProductAndDateBetween(product, start, end);
        
        int total = sales.stream().mapToInt(Sale::getQuantity).sum();
        // average over 'days' to avoid inflated result when fewer sales entries exist
        return days == 0 ? 0 : (int) Math.ceil((double) total / days);
    }

    /**
     * Mark PO as received and update the product's stock (idempotent).
     */
    @Transactional
    public PurchaseOrder receivePurchaseOrder(Long poId) {
        PurchaseOrder po = null;
        PurchaseOrderStatus oldStatus;
		try {
			po = purchaseOrderRepository.findById(poId)
			        .orElseThrow(() -> new ResourceNotFoundException("PurchaseOrder not found"));
		} catch (ResourceNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
        if ((oldStatus=po.getStatus()) == PurchaseOrderStatus.RECEIVED) {
            // idempotent behavior
            return po;
        }

        Product product = po.getProduct();

        // increment stock
        int newStock = product.getCurrentStock() + po.getQuantityOrdered();
        product.setCurrentStock(newStock);
        // save product first (optimistic locking will ensure correctness)
        productRepository.save(product);

        // mark PO as received
        po.setStatus(PurchaseOrderStatus.RECEIVED);
        po.setActualArrivalDate(LocalDate.now());
        purchaseOrderRepository.save(po);
        BizLogs.ORDER.info("ORDER_STATUS_UPDATED id={} oldStatus={} newStatus={}",
        		po.getId(), oldStatus, po.getStatus());
        return po;
    }
}
