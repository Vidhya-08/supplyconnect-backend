package com.vidhya.supplyconnect.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "po_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class POItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer lineNumber;

    @Column(nullable = false)
    private String product;

    private String productDescription;

    @Column(nullable = false)
    private Double orderedQuantity;

    @Column(nullable = false)
    private String quantityUom;

    @Column(nullable = false)
    private LocalDate requestedDeliveryDate;

    private Double confirmedQuantity;

    private LocalDate confirmedDeliveryDate;

    private Double receivedQuantity;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PurchaseOrderStatus status;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "purchase_order_id", nullable = false)
    private PurchaseOrder purchaseOrder;
}