package com.vidhya.supplyconnect.controller;

import com.vidhya.supplyconnect.dto.ASNRequest;
import com.vidhya.supplyconnect.dto.POItemResponse;
import com.vidhya.supplyconnect.entity.ASN;
import com.vidhya.supplyconnect.service.ASNService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/asn")
public class ASNController {

    private final ASNService asnService;

    public ASNController(ASNService asnService) {
        this.asnService = asnService;
    }

    @GetMapping
    public List<POItemResponse> getConfirmedPOItemsForSupplier() {
        return asnService.getConfirmedPOItemsForSupplier();
    }

    @PostMapping
    public ASN createASN(@Valid @RequestBody ASNRequest request) {
        return asnService.createASN(request);
    }
}
