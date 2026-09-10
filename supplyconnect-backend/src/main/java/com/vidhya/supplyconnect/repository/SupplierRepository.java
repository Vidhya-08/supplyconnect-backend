package com.vidhya.supplyconnect.repository;

import com.vidhya.supplyconnect.entity.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SupplierRepository extends JpaRepository<Supplier, Long> {
}