package com.vidhya.supplyconnect.repository;

import com.vidhya.supplyconnect.entity.POItem;
import com.vidhya.supplyconnect.entity.PurchaseOrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface POItemRepository extends JpaRepository<POItem,Long> {
    List<POItem> findByPurchaseOrderSupplierIdAndStatus(
            Long supplierId,
            PurchaseOrderStatus status);

    List<POItem> findByPurchaseOrderBuyerIdAndStatusIn(
            Long buyerId,
            List<PurchaseOrderStatus> statuses);
}
