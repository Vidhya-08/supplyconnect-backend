package com.vidhya.supplyconnect.controller;

import com.vidhya.supplyconnect.dto.DeliveryRequest;
import com.vidhya.supplyconnect.dto.POItemResponse;
import com.vidhya.supplyconnect.service.DeliveryService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/delivery")
public class DeliveryController {

    private final DeliveryService deliveryService;

    public DeliveryController(DeliveryService deliveryService) {
        this.deliveryService = deliveryService;
    }

    @PostMapping
    public void receiveDelivery(
            @Valid @RequestBody DeliveryRequest request) {

        deliveryService.receiveDelivery(request);
    }

    @GetMapping
    public List<POItemResponse> getEligiblePOItemsForBuyer() {
        return deliveryService.getEligiblePOItemsForBuyer();
    }
}
