package com.vidhya.supplyconnect.controller;

import com.vidhya.supplyconnect.dto.POConfirmationRequest;
import com.vidhya.supplyconnect.dto.PurchaseOrderRequest;
import com.vidhya.supplyconnect.dto.PurchaseOrderResponse;
import com.vidhya.supplyconnect.entity.PurchaseOrder;
import com.vidhya.supplyconnect.service.PurchaseOrderService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/purchase-orders")
public class PurchaseOrderController {

    private final PurchaseOrderService purchaseOrderService;

    public PurchaseOrderController(PurchaseOrderService purchaseOrderService) {
        this.purchaseOrderService = purchaseOrderService;
    }

    @PostMapping
    public PurchaseOrderResponse createPurchaseOrder(
            @Valid @RequestBody PurchaseOrderRequest request) {

        return purchaseOrderService.createPurchaseOrder(request);
    }

    @GetMapping("/{poNumber}")
    public PurchaseOrderResponse getPurchaseOrderByPoNumber(@PathVariable String poNumber){
        return purchaseOrderService.getPurchaseOrderByPoNumber(poNumber);
    }

    @GetMapping
    public List<PurchaseOrderResponse> getAllPurchaseOrders() {

        return purchaseOrderService.getAllPurchaseOrders();
    }


    @PutMapping("/{poNumber}/items/{itemId}/confirm")
    public PurchaseOrderResponse confirmPOItem(
            @PathVariable String poNumber,
            @PathVariable Long itemId,
            @Valid @RequestBody POConfirmationRequest request) {

        return purchaseOrderService.confirmPOItem(
                poNumber, itemId, request);
    }
}
