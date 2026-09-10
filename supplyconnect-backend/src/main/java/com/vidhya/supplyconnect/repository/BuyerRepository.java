package com.vidhya.supplyconnect.repository;

import com.vidhya.supplyconnect.entity.Buyer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BuyerRepository extends JpaRepository<Buyer, Long> {
}