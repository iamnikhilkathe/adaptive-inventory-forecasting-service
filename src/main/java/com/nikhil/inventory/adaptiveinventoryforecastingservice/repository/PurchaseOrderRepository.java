package com.nikhil.inventory.adaptiveinventoryforecastingservice.repository;

import com.nikhil.inventory.adaptiveinventoryforecastingservice.entity.PurchaseOrder;
import com.nikhil.inventory.adaptiveinventoryforecastingservice.entity.PurchaseOrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, Long> {
    List<PurchaseOrder> findByStatus(PurchaseOrderStatus status);
}
