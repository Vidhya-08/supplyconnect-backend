package com.vidhya.supplyconnect.repository;

import com.vidhya.supplyconnect.entity.ASN;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ASNRepository extends JpaRepository<ASN,Long> {
}
