package com.vidhya.supplyconnect.service;

import com.vidhya.supplyconnect.dto.ASNRequest;
import com.vidhya.supplyconnect.dto.POItemResponse;
import com.vidhya.supplyconnect.entity.*;
import com.vidhya.supplyconnect.repository.ASNRepository;
import com.vidhya.supplyconnect.repository.POItemRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ASNService {

    private final ASNRepository asnRepository;
    private final POItemRepository poItemRepository;

    public ASNService(
            ASNRepository asnRepository,
            POItemRepository poItemRepository) {

        this.asnRepository = asnRepository;
        this.poItemRepository = poItemRepository;
    }

    public ASN createASN(ASNRequest request) {

        POItem poItem = poItemRepository.findById(request.getPoItemId())
                .orElseThrow(() -> new RuntimeException("PO Item not found"));

        // Get logged-in user
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        CustomUserDetails userDetails =
                (CustomUserDetails) authentication.getPrincipal();

        User user = userDetails.getUser();

        // Get supplier associated with logged-in user
        Supplier loggedInSupplier = user.getSupplier();

        if (loggedInSupplier == null) {
            throw new RuntimeException(
                    "Logged-in user is not associated with a supplier");
        }

        // Check whether this PO belongs to the logged-in supplier
        Supplier poSupplier = poItem.getPurchaseOrder().getSupplier();

        if (!poSupplier.getId().equals(loggedInSupplier.getId())) {
            throw new RuntimeException(
                    "You are not authorized to create ASN for this Purchase Order");
        }

        // ASN only for confirmed item
        if (poItem.getStatus() != PurchaseOrderStatus.CONFIRMED) {
            throw new RuntimeException(
                    "ASN can only be created for a confirmed PO item");
        }

        double confirmedQuantity = poItem.getConfirmedQuantity();

        double alreadyShipped = asnRepository.findAll()
                .stream()
                .filter(asn ->
                        asn.getPoItem().getId().equals(poItem.getId()))
                .mapToDouble(ASN::getShippedQuantity)
                .sum();

        double remainingQuantity =
                confirmedQuantity - alreadyShipped;

        if (request.getShippedQuantity() > remainingQuantity) {
            throw new RuntimeException(
                    "Shipped quantity cannot exceed remaining confirmed quantity");
        }

        ASN asn = new ASN();

        asn.setAsnNumber("ASN-" + UUID.randomUUID());
        asn.setPoItem(poItem);
        asn.setShippedQuantity(request.getShippedQuantity());
        asn.setShipmentDate(request.getShipmentDate());
        asn.setStatus(ASNStatus.CREATED);

        return asnRepository.save(asn);
    }

    public List<POItemResponse> getConfirmedPOItemsForSupplier() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        CustomUserDetails userDetails =
                (CustomUserDetails) authentication.getPrincipal();

        User user = userDetails.getUser();

        Supplier loggedInSupplier = user.getSupplier();

        if (loggedInSupplier == null) {
            throw new RuntimeException(
                    "Logged-in user is not associated with a supplier");
        }

        List<POItem> confirmedItems =
                poItemRepository.findByPurchaseOrderSupplierIdAndStatus(
                        loggedInSupplier.getId(),
                        PurchaseOrderStatus.CONFIRMED);

        return confirmedItems.stream()
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