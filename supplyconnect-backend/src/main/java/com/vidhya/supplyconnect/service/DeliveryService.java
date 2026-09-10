package com.vidhya.supplyconnect.service;

import com.vidhya.supplyconnect.dto.DeliveryRequest;
import com.vidhya.supplyconnect.dto.POItemResponse;
import com.vidhya.supplyconnect.entity.*;
import com.vidhya.supplyconnect.repository.POItemRepository;
import com.vidhya.supplyconnect.repository.PurchaseOrderRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeliveryService {

    private final POItemRepository poItemRepository;
    private final PurchaseOrderRepository purchaseOrderRepository;

    public DeliveryService(POItemRepository poItemRepository,
                           PurchaseOrderRepository purchaseOrderRepository) {
        this.poItemRepository = poItemRepository;
        this.purchaseOrderRepository = purchaseOrderRepository;
    }

    public void receiveDelivery(DeliveryRequest request) {

        POItem item = poItemRepository.findById(request.getPoItemId())
                .orElseThrow(() -> new RuntimeException("PO Item not found"));

        // Get logged-in user
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        CustomUserDetails userDetails =
                (CustomUserDetails) authentication.getPrincipal();

        User user = userDetails.getUser();

        // Get buyer associated with logged-in user
        Buyer loggedInBuyer = user.getBuyer();

        if (loggedInBuyer == null) {
            throw new RuntimeException(
                    "Logged-in user is not associated with a buyer");
        }

        // Check whether PO belongs to logged-in buyer
        Buyer poBuyer = item.getPurchaseOrder().getBuyer();

        if (!poBuyer.getId().equals(loggedInBuyer.getId())) {
            throw new RuntimeException(
                    "You are not authorized to receive this Purchase Order");
        }

        if (item.getConfirmedQuantity() == null) {
            throw new RuntimeException(
                    "Cannot receive an item that has not been confirmed");
        }

        double alreadyReceived = item.getReceivedQuantity() == null
                ? 0
                : item.getReceivedQuantity();

        double remainingQuantity =
                item.getConfirmedQuantity() - alreadyReceived;

        if (request.getReceivedQuantity() > remainingQuantity) {
            throw new RuntimeException(
                    "Received quantity cannot exceed remaining quantity");
        }

        double newReceivedQuantity =
                alreadyReceived + request.getReceivedQuantity();

        item.setReceivedQuantity(newReceivedQuantity);

        if (newReceivedQuantity == item.getConfirmedQuantity()) {
            item.setStatus(PurchaseOrderStatus.DELIVERED);
        } else {
            item.setStatus(PurchaseOrderStatus.PARTIALLY_DELIVERED);
        }

        poItemRepository.save(item);

        PurchaseOrder purchaseOrder = item.getPurchaseOrder();

        List<POItem> items = purchaseOrder.getItems();

        boolean allDelivered = items.stream()
                .allMatch(i ->
                        i.getStatus() == PurchaseOrderStatus.DELIVERED);

        boolean anyDelivered = items.stream()
                .anyMatch(i ->
                        i.getStatus() == PurchaseOrderStatus.DELIVERED ||
                                i.getStatus() == PurchaseOrderStatus.PARTIALLY_DELIVERED);

        if (allDelivered) {
            purchaseOrder.setStatus(PurchaseOrderStatus.DELIVERED);
        } else if (anyDelivered) {
            purchaseOrder.setStatus(PurchaseOrderStatus.PARTIALLY_DELIVERED);
        }

        purchaseOrderRepository.save(purchaseOrder);
    }

    public List<POItemResponse> getEligiblePOItemsForBuyer() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        CustomUserDetails userDetails =
                (CustomUserDetails) authentication.getPrincipal();

        User user = userDetails.getUser();

        Buyer loggedInBuyer = user.getBuyer();

        if (loggedInBuyer == null) {
            throw new RuntimeException(
                    "Logged-in user is not associated with a buyer");
        }

        List<POItem> eligibleItems =
                poItemRepository.findByPurchaseOrderBuyerIdAndStatusIn(
                        loggedInBuyer.getId(),
                        List.of(
                                PurchaseOrderStatus.CONFIRMED,
                                PurchaseOrderStatus.PARTIALLY_DELIVERED
                        )
                );

        return eligibleItems.stream()
                .map(item -> new POItemResponse(
                        item.getId(),
                        item.getLineNumber(),
                        item.getProduct(),
                        item.getProductDescription(),
                        item.getOrderedQuantity(),
                        item.getQuantityUom(),
                        item.getRequestedDeliveryDate(),
                        item.getConfirmedQuantity(),
                        item.getConfirmedDeliveryDate(),
                        item.getStatus().name()
                ))
                .toList();
    }
}