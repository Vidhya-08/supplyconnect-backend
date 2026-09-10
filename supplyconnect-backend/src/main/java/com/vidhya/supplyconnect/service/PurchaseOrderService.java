package com.vidhya.supplyconnect.service;

import com.vidhya.supplyconnect.dto.*;
import com.vidhya.supplyconnect.entity.*;
import com.vidhya.supplyconnect.repository.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PurchaseOrderService {

    private final PurchaseOrderRepository purchaseOrderRepository;
    private final BuyerRepository buyerRepository;
    private final SupplierRepository supplierRepository;
    private final SiteRepository siteRepository;
    private final POItemRepository poItemRepository;

    public PurchaseOrderService(
            PurchaseOrderRepository purchaseOrderRepository,
            BuyerRepository buyerRepository,
            SupplierRepository supplierRepository,
            SiteRepository siteRepository,
            POItemRepository poItemRepository) {

        this.purchaseOrderRepository = purchaseOrderRepository;
        this.buyerRepository = buyerRepository;
        this.supplierRepository = supplierRepository;
        this.siteRepository = siteRepository;
        this.poItemRepository=poItemRepository;
    }

    public PurchaseOrderResponse createPurchaseOrder(PurchaseOrderRequest request) {

        // Get currently logged-in user
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        CustomUserDetails userDetails =
                (CustomUserDetails) authentication.getPrincipal();

        User user = userDetails.getUser();

        // Get buyer associated with logged-in user
        Buyer buyer = user.getBuyer();

        if (buyer == null) {
            throw new RuntimeException("Logged-in user is not associated with a buyer");
        }


        Supplier supplier = supplierRepository.findById(request.getSupplierId())
                .orElseThrow(() -> new RuntimeException("Supplier not found"));

        Site site = siteRepository.findById(request.getSiteId())
                .orElseThrow(() -> new RuntimeException("Site not found"));

        PurchaseOrder purchaseOrder = new PurchaseOrder();

        purchaseOrder.setPoNumber("PO-" + UUID.randomUUID());
        purchaseOrder.setBuyer(buyer);
        purchaseOrder.setSupplier(supplier);
        purchaseOrder.setSite(site);
        purchaseOrder.setPoDate(request.getPoDate());
        purchaseOrder.setStatus(PurchaseOrderStatus.NEW);

        for (POItemRequest itemRequest : request.getItems()) {

            POItem item = new POItem();

            item.setLineNumber(itemRequest.getLineNumber());
            item.setProduct(itemRequest.getProduct());
            item.setProductDescription(itemRequest.getProductDescription());
            item.setOrderedQuantity(itemRequest.getOrderedQuantity());
            item.setQuantityUom(itemRequest.getQuantityUom());
            item.setRequestedDeliveryDate(itemRequest.getRequestedDeliveryDate());
            item.setStatus(PurchaseOrderStatus.NEW);

            item.setPurchaseOrder(purchaseOrder);

            purchaseOrder.getItems().add(item);
        }

        PurchaseOrder savedPurchaseOrder = purchaseOrderRepository.save(purchaseOrder);
        return mapToResponse(savedPurchaseOrder);
    }

    private PurchaseOrderResponse mapToResponse (PurchaseOrder purchaseOrder){

            List<POItemResponse> itemResponses = purchaseOrder.getItems()
                    .stream()
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

            return new PurchaseOrderResponse(
                    purchaseOrder.getId(),
                    purchaseOrder.getPoNumber(),
                    purchaseOrder.getBuyer().getName(),
                    purchaseOrder.getSupplier().getName(),
                    purchaseOrder.getSite().getSiteNumber(),
                    purchaseOrder.getPoDate(),
                    purchaseOrder.getStatus().name(),
                    itemResponses
            );
    }

    public PurchaseOrderResponse getPurchaseOrderByPoNumber(String poNumber) {

        PurchaseOrder purchaseOrder = purchaseOrderRepository
                .findByPoNumber(poNumber)
                .orElseThrow(() ->
                        new RuntimeException("Purchase Order not found"));

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        CustomUserDetails userDetails =
                (CustomUserDetails) authentication.getPrincipal();

        User user = userDetails.getUser();

        String role = user.getRole().getRoleName();

        if ("BUYER".equals(role)) {

            if (user.getBuyer() == null ||
                    !purchaseOrder.getBuyer().getId()
                            .equals(user.getBuyer().getId())) {

                throw new RuntimeException(
                        "You are not authorized to view this Purchase Order");
            }

        } else if ("SUPPLIER".equals(role)) {

            if (user.getSupplier() == null ||
                    !purchaseOrder.getSupplier().getId()
                            .equals(user.getSupplier().getId())) {

                throw new RuntimeException(
                        "You are not authorized to view this Purchase Order");
            }
        }

        return mapToResponse(purchaseOrder);
    }

    public List<PurchaseOrderResponse> getAllPurchaseOrders() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        CustomUserDetails userDetails =
                (CustomUserDetails) authentication.getPrincipal();

        User user = userDetails.getUser();

        String role = user.getRole().getRoleName();

        return purchaseOrderRepository.findAll()
                .stream()
                .filter(po -> {

                    if ("BUYER".equals(role)) {

                        return user.getBuyer() != null &&
                                po.getBuyer().getId()
                                        .equals(user.getBuyer().getId());

                    } else if ("SUPPLIER".equals(role)) {

                        return user.getSupplier() != null &&
                                po.getSupplier().getId()
                                        .equals(user.getSupplier().getId());
                    }

                    return false;
                })
                .map(this::mapToResponse)
                .toList();
    }

    public PurchaseOrderResponse confirmPOItem(
            String poNumber,
            Long itemId,
            POConfirmationRequest request) {

        PurchaseOrder purchaseOrder = purchaseOrderRepository
                .findByPoNumber(poNumber)
                .orElseThrow(() ->
                        new RuntimeException("Purchase Order not found"));

        // Get logged-in supplier
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

        // Check PO belongs to logged-in supplier
        if (!purchaseOrder.getSupplier().getId()
                .equals(loggedInSupplier.getId())) {

            throw new RuntimeException(
                    "You are not authorized to modify this Purchase Order");
        }

        POItem item = poItemRepository.findById(itemId)
                .orElseThrow(() ->
                        new RuntimeException("PO Item not found"));

        if (!item.getPurchaseOrder().getId()
                .equals(purchaseOrder.getId())) {

            throw new RuntimeException(
                    "PO Item does not belong to this Purchase Order");
        }

        if (item.getStatus() == PurchaseOrderStatus.CANCELLED) {
            throw new RuntimeException(
                    "Cancelled PO cannot be modified");
        }

        double orderedQuantity = item.getOrderedQuantity();
        double confirmedQuantity = request.getConfirmedQuantity();

        if (confirmedQuantity > orderedQuantity) {
            throw new RuntimeException(
                    "Confirmed quantity cannot exceed ordered quantity");
        }

        item.setConfirmedQuantity(confirmedQuantity);
        item.setConfirmedDeliveryDate(
                request.getConfirmedDeliveryDate());

        if (confirmedQuantity == 0) {
            item.setStatus(PurchaseOrderStatus.CANCELLED);
        } else if (confirmedQuantity < orderedQuantity) {
            item.setStatus(PurchaseOrderStatus.CHANGED);
        } else {
            item.setStatus(PurchaseOrderStatus.CONFIRMED);
        }

        updatePurchaseOrderStatus(purchaseOrder);

        poItemRepository.save(item);
        purchaseOrderRepository.save(purchaseOrder);

        return mapToResponse(purchaseOrder);
    }
    private void updatePurchaseOrderStatus(PurchaseOrder purchaseOrder) {

        List<POItem> items = purchaseOrder.getItems();

        boolean allCancelled = items.stream()
                .allMatch(item-> item.getStatus()==PurchaseOrderStatus.CANCELLED);

        boolean allConfirmed = items.stream()
                .allMatch(item -> item.getStatus() == PurchaseOrderStatus.CONFIRMED);

        boolean anyChanged = items.stream()
                .anyMatch(item -> item.getStatus() == PurchaseOrderStatus.CHANGED);

        if (allCancelled) {
            purchaseOrder.setStatus(PurchaseOrderStatus.CANCELLED);
        } else if (allConfirmed) {
            purchaseOrder.setStatus(PurchaseOrderStatus.CONFIRMED);
        } else if (anyChanged) {
            purchaseOrder.setStatus(PurchaseOrderStatus.CHANGED);
        } else {
            purchaseOrder.setStatus(PurchaseOrderStatus.NEW);
        }
    }
}