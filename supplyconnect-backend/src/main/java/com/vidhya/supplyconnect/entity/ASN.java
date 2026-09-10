package com.vidhya.supplyconnect.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "asns")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ASN {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String asnNumber;

    @ManyToOne
    @JoinColumn(name = "po_item_id", nullable = false)
    private POItem poItem;

    @Column(nullable = false)
    private Double shippedQuantity;

    @Column(nullable = false)
    private LocalDate shipmentDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ASNStatus status;
}
